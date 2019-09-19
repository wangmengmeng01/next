package com.yunda.base.common.task;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;
import com.github.crab2died.ExcelUtils;
import com.github.crab2died.exceptions.Excel4JException;
import com.google.common.collect.Lists;
import com.yunda.base.common.config.Constant;
import com.yunda.base.common.enums.ExportEnum;
import com.yunda.base.feiniao.report.utils.DateUtils;
import com.yunda.base.feiniao.report.utils.ZipUtil;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.FileExportDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.FileExportService;

/**
 * 【关于大数据量excel导出的解决方案】 <br>
 * 采用 并行改串行，同步变异步的机制
 * 
 * 1. 新增一张导出文件队列表
 * userid，creattime，执行导出的类，执行导出的方法名，执行导出的参数（json化），状态（等待中，处理中，完成），文件路径
 * 
 * 2.对导出文件队列做列表页面，当状态为完成的时候，操作提供下载功能。当状态为等待中，操作提供删除功能
 * 
 * 3.新增一个task，逐条扫导出文件队列表。对等待中的数据通过反射执行目标类的目标方法，喂参为json字符串（具体参数的json化状态）
 * 
 * 4.改造现有的导出代码，一份二 4.1：将核心业务处理改为收参为字符串的一个方法，实际参数自己反序列化json串。用于方便task反射处理
 * 4.2：接收前端请求后形成排队数据，注意把需要的参数放map然后json化，存入数据行中
 * 
 * 5.优化导出逻辑（优化4.1）
 * 考虑到excel单文件有30wan条数上限，导出逻辑采用分页的思路，每5万条数据落地一个文件。最后把文件打包成为一个zip文件
 * 
 * 6.优化前端用户感受 采用websocket在用户的目标文件生成完成后主动推送消息给用户，显示在右下角，用户点击该消息，到达文件队列的页面
 * 
 */
@DisallowConcurrentExecution
@SuppressWarnings("all")
public class ExportTask extends TaskAbs {
	Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private SimpMessagingTemplate template;
	@Autowired
	private FileExportService fileExportService;

	@Override
	public void run(JobExecutionContext arg0) {
		List<FileExportDO> fileList = fileExportService.getWaitingTask(ExportEnum.StandingBy.getNum());
		if (!ObjectUtils.isEmpty(fileList)) {
			for (FileExportDO fileExport : fileList) {
				try {
					long l = System.currentTimeMillis();
					process(fileExport);
					log.debug(fileExport.getTitle() + "处理耗时" + ((System.currentTimeMillis() - l) / 1000) + "秒");
				} catch (Exception e) {
					fileExport.setState(ExportEnum.fail.getNum());
					fileExportService.update(fileExport);
					log.error("生成数据失败,原因是" + e);
				}
			}
		}
	}

	private <T> void process(FileExportDO fileExport) throws Exception {
		// 将状态变为处理中
		fileExport.setState(ExportEnum.Handing.getNum());
		fileExportService.update(fileExport);

		List<T> result = null;// 生成数据
		Boolean runError = false;// 是否出现异常
		Boolean hasData = false;// 是否有数据
		String reqDate = DateUtils.getReqDate();// 当前时间
		String reqTime = new SimpleDateFormat("HHmmss").format(new Date());// 一次操作的时间
		String title = fileExport.getTitle();
		String excel = getExcelModel(title);
		Class<?> specClass = Class.forName(fileExport.getSpecClass());
		String splitLimit = SysConfig.splitLimit;

		String exportExcelPath = SysConfig.exportExcelPath + reqDate + "/" + reqTime;
		File d1 = new File(exportExcelPath);
		if (!d1.exists()) {
			// 创建当前操作的文件夹
			d1.mkdirs();
		}
		String exportZipPath = SysConfig.exportZIPPath + reqDate;
		File d2 = new File(exportZipPath);
		if (!d2.exists()) {
			// 创建当前操作的文件夹
			d2.mkdirs();
		}
		File file = new File(SysConfig.TEMPLATE + excel);
		UserDO user = JSON.parseObject(fileExport.getUserId(), UserDO.class);
		Object newInstance = Class.forName(fileExport.getExecuteClass()).newInstance();
		Method declaredMethod = newInstance.getClass().getDeclaredMethod(fileExport.getExecuteMethod(), String.class, UserDO.class, String.class);

		int offset = 0;
		fileExport.setStartTime(new Date());
		fileExportService.update(fileExport);// 记录开始处理时间
		while (true) {
			log.info(fileExport.getTitle() + ",offset=" + offset + ",limit=" + SysConfig.exportExcelLimit + "开始捞数据");
			try {
				Object invoke = declaredMethod.invoke(newInstance, fileExport.getExecuteParam(), user, String.valueOf(offset));
				if (ObjectUtils.isEmpty(invoke) || !(invoke instanceof List)) {
					break;
				}

				result = (List) invoke;
				if (null != result && result.size() > 0) {
					hasData = true;
					if (Integer.valueOf(splitLimit) < result.size()) {
						// 有必要切分
						List<List<T>> partition = Lists.partition(result, Integer.valueOf(splitLimit));
						for (List<T> list : partition) {
							offset = ExportMethod(fileExport, specClass, splitLimit, exportExcelPath, file, offset, list);
						}

					} else {
						offset = ExportMethod(fileExport, specClass, splitLimit, exportExcelPath, file, offset, result);
					}
				}
			} catch (Exception e) {
				runError = true;
				log.error(e.getMessage(), e);
				break;
			}
		}

		if (runError) {
			fileExport.setState(ExportEnum.fail.getNum());
		}
		if (!hasData) {
			fileExport.setState(ExportEnum.no_data.getNum());
		}
		if (!runError && hasData) {
			String fileName = reqTime;
			String finalZip = exportZipPath + "/" + fileName + ".zip";
			if (ZipUtil.zipFile(exportExcelPath, exportZipPath, fileName)) {
				fileExport.setState(ExportEnum.Complete.getNum());
				fileExport.setFilePath(finalZip);
				log.info("压缩文件已经生成:" + "[" + finalZip + "]");

				template.convertAndSendToUser(user.getUsername(), Constant.wsQueueName, fileExport.getTitle() + "处理完毕请下载文件");
			} else {
				fileExport.setState(ExportEnum.fail.getNum());
				log.error("压缩文件生成失败:" + "[" + finalZip + "]");
			}
		}
		fileExport.setEndTime(new Date());
		fileExportService.update(fileExport);
	}
	
    //导出方法
	private <T> int ExportMethod(FileExportDO fileExport, Class<?> specClass, String splitLimit, String exportExcelPath, File file, int offset, List<T> list) throws Excel4JException, IOException {
		if (file.exists() && SysConfig.USER_TEMPLATE.equals("true")) {
			BaseModelExport(fileExport, list, specClass, exportExcelPath, file, offset);
		} else {
			NoModelExport(fileExport, list, specClass, exportExcelPath, offset);
		}
		offset += Integer.valueOf(splitLimit);
		return offset;
	}

	// 不基于模板导出
	private <T> void NoModelExport(FileExportDO fileExport, List<T> result, Class<?> specClass, String exportExcelPath, int offset) throws Excel4JException, IOException {
		ExcelUtils.getInstance().exportObjects2Excel(result, specClass, exportExcelPath + "/" + System.nanoTime() + ".xlsx");
		fileExport.setHandCount(String.valueOf(offset + result.size()));
		fileExportService.update(fileExport);
	}

	// 基于模板导出
	private <T> void BaseModelExport(FileExportDO fileExport, List<T> result, Class<?> specClass, String exportExcelPath, File file, int offset) throws Excel4JException {
		Map<String, String> data = new HashMap<>();
		ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, result, data, specClass, false, exportExcelPath + "/" + System.nanoTime() + ".xlsx");
		fileExport.setHandCount(String.valueOf(offset + result.size()));
		fileExportService.update(fileExport);
	}

	private String getExcelModel(String title) {
		switch (title) {
		case Constant.custRewardTitle:
			return "custRewardDetails.xlsx";
		case Constant.branchCustRewardTitle:
			return "custBranchRewardDetail.xlsx";
		default:
			return null;
		}
	}

	@Override
	public String whoareyou() {
		return "导出定时任务";
	}
}