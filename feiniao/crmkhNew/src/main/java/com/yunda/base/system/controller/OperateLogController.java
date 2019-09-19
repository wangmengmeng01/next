package com.yunda.base.system.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.crab2died.ExcelUtils;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.feiniao.report.bo.Bo_ReportTotaldata;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.SensitiveOperateLogDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.SensitiveOperateLogService;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-01-23133904
 */
 
@Controller
@RequestMapping("/system/operateLog")
public class OperateLogController extends BaseController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private SensitiveOperateLogService operateLogService;
	
	@GetMapping()
	@RequiresPermissions("system:loginLog:edit")
	String OperateLog(){
	    return "system/operateLog/detail";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:operateLog:operateLog")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<SensitiveOperateLogDO> operateLogList = operateLogService.list(query);
		int total = operateLogService.count(query);
		PageUtils pageUtils = new PageUtils(operateLogList, total);
		return pageUtils;
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@RequiresPermissions("system:loginLog:exportExcel")
	public void exportExcel(HttpServletResponse response, HttpServletRequest request, @ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata , BindingResult bindingResult, @RequestParam Map<String, Object> params) {
		//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
			BufferedInputStream bin = null;
			OutputStream out = null;
			UserDO loginUser = getUser(request);

			//查询列表数据
			Query query = new Query(params);
			query.put("offset", 0);
			query.put("limit", 10000);
			List<SensitiveOperateLogDO> listOperationDO = operateLogService.list(query);
			String targetFile = SysConfig.TARGET + "operation"+ DateUtils.format(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"LoginLog.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");

				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);

				if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
					//前端界面喂参  控制是否使用模板
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0,
							listOperationDO,data, SensitiveOperateLogDO.class, false, response.getOutputStream());
				} else {
					//模板文件不存在  默认输出
					ExcelUtils.getInstance().exportObjects2Excel(listOperationDO,SensitiveOperateLogDO.class,response.getOutputStream());
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

		}
	}

	// 下载excel模版
	@RequestMapping("/downTemplate")
	@MethodLock(key = "downTemplate")
	// 下载模版和导入共用同一个权限
	@RequiresPermissions("system:operateLog:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<SensitiveOperateLogDO>(), SensitiveOperateLogDO.class, targetFile);

			// 写入response
			File downloadFile = new File(targetFile);
			FileUtil.downloadByResponse(response, downloadFile);
			downloadFile.delete();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			//e.printStackTrace();
		}
	}

	// 导入excel
	@ResponseBody
	@MethodLock(key = "importExcel")
	@RequestMapping(value = "/importExcel", consumes = "multipart/*", headers = "content-type=mutipart/form-data", method = RequestMethod.POST)
	@RequiresPermissions("system:operateLog:importExcel")
	public R importExcel(MultipartFile file) {
		List<SensitiveOperateLogDO> list = null;

		String fileKey = UUID.randomUUID().toString();
		// 获取后缀
		String fileName = file.getOriginalFilename();
		if (fileName.lastIndexOf(".") != -1) {
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			String uploadFile = SysConfig.uploadPath + fileKey + suffix;

			File _f = new File(uploadFile);
			if (!_f.getParentFile().exists()) {
				_f.getParentFile().mkdirs();
			}

			BufferedOutputStream out = null;
			try {
				out = new BufferedOutputStream(new FileOutputStream(_f));
				out.write(file.getBytes());
				out.flush();

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, SensitiveOperateLogDO.class, 0, 0);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			} finally {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}

		if (list != null) {
			for (SensitiveOperateLogDO _do : list) {
				operateLogService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("system:operateLog:add")
	String add(){
	    return "system/operateLog/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("system:operateLog:edit")
	String edit(@PathVariable("id") Long id,Model model){
		SensitiveOperateLogDO operateLog = operateLogService.get(id);
		model.addAttribute("operateLog", operateLog);
	    return "system/operateLog/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:operateLog:add")
	public R save( SensitiveOperateLogDO operateLog){
		if(operateLogService.save(operateLog)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:operateLog:edit")
	public R update( SensitiveOperateLogDO operateLog){
		operateLogService.update(operateLog);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("system:operateLog:remove")
	public R remove( Long id){
		if(operateLogService.remove(id)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:operateLog:batchRemove")
	public R remove(@RequestParam("ids[]") Long[] ids){
		operateLogService.batchRemove(ids);
		return R.ok();
	}

	@ResponseBody
	@GetMapping("/detailList")
	public PageUtils DetailList(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<SensitiveOperateLogDO> RepushOrderDetailList = operateLogService.list(query);
		int total = operateLogService.count(query);
		PageUtils pageUtils = new PageUtils(RepushOrderDetailList, total);
		return pageUtils;

	}
	
}
