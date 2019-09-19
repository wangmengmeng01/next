package com.yunda.base.feiniao.report.controller;

import com.alibaba.fastjson.JSON;
import com.github.crab2died.ExcelUtils;
import com.yunda.base.common.config.Constant;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.enums.ExportEnum;
import com.yunda.base.common.enums.RespEnum;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.SpringUtil;
import com.yunda.base.feiniao.report.bo.Bo_ReportTotaldata;
import com.yunda.base.feiniao.report.bo.Bo_reportCustRewardDetails_list;
import com.yunda.base.feiniao.report.core.CRM_constants;
import com.yunda.base.feiniao.report.domain.ExportBranchCRDDataDO;
import com.yunda.base.feiniao.report.domain.ExportCRDDataDO;
import com.yunda.base.feiniao.report.domain.ExportReportTotaldataDO;
import com.yunda.base.feiniao.report.domain.ReportCustRewardDetailsDO;
import com.yunda.base.feiniao.report.domain.ReportFluctuateDO;
import com.yunda.base.feiniao.report.domain.ReportTotaldataDO;
import com.yunda.base.feiniao.report.service.ReportCustRewardDetailsService;
import com.yunda.base.feiniao.report.utils.JsonFilterUtils;
import com.yunda.base.system.annotation.SensitiveOperateLog;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.FileExportDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.FileExportService;
import com.yunda.base.system.service.SessionService;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;
import com.yunda.ydmbspringbootstarter.common.utils.StringUtils;

import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 客户奖励明细表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-17153137
 */
 
@Controller
@RequestMapping("/report/reportCustRewardDetails")
public class ReportCustRewardDetailsController  extends BaseController {
	Logger log = Logger.getLogger(getClass());
	
	@Value("${excel.targetFile.path}")
	private String target;
	@Autowired
	public FileExportService fileExportService;
	@Autowired
	public SessionService sessionService;
	@Autowired
	private ReportCustRewardDetailsService reportCustRewardDetailsService;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	//创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
	static ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();
	
	@GetMapping()
	@RequiresPermissions("report:reportCustRewardDetails:reportCustRewardDetails")
	String ReportCustRewardDetails(HttpServletRequest request){
		UserDO loginUser = getUser(request);
		if (loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			// 超级用户权限 无限制
			// 系统菜单配置了report:admin:allperms权限标识 角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户
			// 跳到非网点权限页面
			 return "feiniao/report/reportCustRewardDetails/reportCustRewardDetails";
		}else{
			if(loginUser.isBigareaqx()){//是否有大区权限
				return "feiniao/report/reportCustRewardDetails/reportCustRewardDetails";
			}else if(loginUser.isProvinceqx()){//是否有省权限
				return "feiniao/report/reportCustRewardDetails/reportCustRewardDetails";
			}else {
				//账号是网点权限,到网点页面； 跳到非网点权限页面
				if(loginUser.getOrgCode() != null & !loginUser.getOrgCode().isEmpty()){
					 return "feiniao/report/reportCustRewardDetails/reportBranchCustRewardDetails";
				}else{
					 return "feiniao/report/reportCustRewardDetails/reportCustRewardDetails";
				}
			}
		}
	}
	
	@ApiOperation(value = "获取客户奖励明细表主页面")
	@ResponseBody
	@GetMapping("/list")
	@SensitiveOperateLog(value = "客户奖励明细表",type = "查询")
	@RequiresPermissions("report:reportCustRewardDetails:reportCustRewardDetails")
	public RspBean<PageUtils> list(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute @Validated final Bo_reportCustRewardDetails_list boReportCustRewardDetailsList, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			notifyPage(bindingResult.getFieldError().getDefaultMessage());
			// 如果验参报错，返回空值給頁面
			return success(new PageUtils());
		}
		Object obj = request.getSession().getAttribute(CRM_constants.AUTH_USER);
		final UserDO loginUser = getUser(request);
		// 对请求参数做基因tag
		final String tag = MD5Utils.encrypt(JSON.toJSONString(boReportCustRewardDetailsList) + JSON.toJSONString(loginUser));

		// 先用标记提前缓存
		PageUtils pageUtils = (PageUtils) redisTemplate.opsForValue().get(tag);
		if (pageUtils != null) {
			//若取到，直接返回
			return success(pageUtils);
		}
		// 若没有缓存的结果，查看该tag是否在处理队列中
		Boolean waiting = redisTemplate.boundHashOps(getClass().getSimpleName() + "_list").hasKey(tag);
		if (!waiting) {
			// 若该tag没有在队列中，那么丢入队列并直接返回等待信息
			newSingleThreadExecutor.execute(new Runnable() {
				@Override
				public void run() {
					// 查询列表数据
					try {
						// 放置队列标记
						redisTemplate.boundHashOps(getClass().getSimpleName() + "_list").put(tag, true);
						
					/*	List<ReportFluctuateDO> reportFluctuateList1 = reportFluctuateService
								.custNumReport(boReportFluctuate, loginUser);*/
						List<ReportCustRewardDetailsDO> reportCustRewardDetailsList = reportCustRewardDetailsService
								.getCustRewardDetailsList(boReportCustRewardDetailsList,loginUser);
						int total = 0;
						if(reportCustRewardDetailsList == null || reportCustRewardDetailsList.size() < 1) {
							
						}else{
							try {
								total = reportCustRewardDetailsService.getCustRewardDetailsCount(boReportCustRewardDetailsList, loginUser);
							} catch (Exception e) {
								//e.printStackTrace();
								log.error(e.getMessage(), e);
							}
						}
/*						int total = reportFluctuateService.custBDcount(boReportFluctuate, loginUser);
						PageUtils pageUtils = new PageUtils(reportFluctuateList1, total);*/
						
						PageUtils pageUtils = new PageUtils(reportCustRewardDetailsList, total);

						// 移除队列标记
						redisTemplate.boundHashOps(getClass().getSimpleName() + "_list").delete(tag);
						// 放置结果缓存
						redisTemplate.opsForValue().set(tag, pageUtils, 7, TimeUnit.DAYS);
					} catch (ParseException e) {
						log.error(e.getMessage(), e);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
					}
				}
			});
		}

		notifyPage("报表生成中，复杂报表需要较长时间处理，您稍后再查");
		return success(new PageUtils());
		//return reporting(new PageUtils());
		
		
		//////////////////////////////////////////////////////
/*		List<ReportCustRewardDetailsDO> reportCustRewardDetailsList =null;
		//查询列表数据
		try {
			reportCustRewardDetailsList = reportCustRewardDetailsService.getCustRewardDetailsList(boReportCustRewardDetailsList,loginUser);
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		int total = 0;
		if(reportCustRewardDetailsList == null || reportCustRewardDetailsList.size() < 1) {
			
		}else{
			try {
				total = reportCustRewardDetailsService.getCustRewardDetailsCount(boReportCustRewardDetailsList, loginUser);
			} catch (Exception e) {
				//e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
		PageUtils pageUtils = new PageUtils(reportCustRewardDetailsList, total);
		return success(pageUtils);*/
	}
	
	@ApiOperation(value = "模糊搜索网点信息")
	@ResponseBody
	@PostMapping("/getCustBraData")
	@SensitiveOperateLog(value = "客户奖励明细表",type = "查询")
	@RequiresPermissions("report:reportCustRewardDetails:reportCustRewardDetails")
	public String getCustBraData(HttpServletRequest request,@RequestParam Map<String, Object> params){
		Object obj = request.getSession().getAttribute(CRM_constants.AUTH_USER);
		UserDO loginUser =new UserDO();
		if (null == obj){
		} else {
			loginUser = (UserDO)obj;
		}
		List<Map<String, Object>> costreportOrderCostFinishList = reportCustRewardDetailsService.searchCustomerBraData(params,loginUser);
		String jsonStr = JSONArray.fromObject(costreportOrderCostFinishList).toString();
		return jsonStr;
	}

	//为解决导出excel数据量超大的问题，引入异步及串行处理方案，该导出接口只将用户请求放入队列。方案详情参看ExportTask类注释  exportExcelBranch
	//各业务方法自行根据业务特性对导出excel的数据进行查询，本方法命名必须和Constant.exportQueryMethodName中定义值一致，处理任务ExportTask会依据此命名反射本方法。
	@ApiOperation(value = "导出excel")
	@RequestMapping("/exportExcel")
	// @MethodLock(key = "exportExcel")
	@RequiresPermissions("report:reportCustRewardDetails:exportExcel")
	@ResponseBody
	@SuppressWarnings("all")
	public RspBean exportExcel(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Validated Bo_reportCustRewardDetails_list boReportCustRewardDetailsList, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			// 如果验参报错，不往下走
			return failure(RespEnum.ERROR_BUSINESS_OPERATE.getCode());
		}		
		
		UserDO loginUser = getUser(request);
		String filterJson = JsonFilterUtils.filterJson(boReportCustRewardDetailsList);
		String filteruser = JsonFilterUtils.filterJson(loginUser);
		StackTraceElement[] stes = Thread.currentThread().getStackTrace();
		String userId = sessionService.findSession(request.getSession().getId()).getUsername();
		
		FileExportDO fileExportDO = new FileExportDO();
		fileExportDO.setCreateTime(new Date());
		fileExportDO.setExecuteClass(stes[1].getClassName());
		if (loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			// 超级用户权限 无限制
			// 系统菜单配置了report:admin:allperms权限标识 角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户
			// 跳到非网点权限页面
			fileExportDO.setExecuteMethod(Constant.exportQueryMethodName);
			fileExportDO.setSpecClass(ExportCRDDataDO.class.getName());
			fileExportDO.setTitle(Constant.custRewardTitle);
		}else{
			if(loginUser.isBigareaqx()){//是否有大区权限
				fileExportDO.setExecuteMethod(Constant.exportQueryMethodName);
				fileExportDO.setSpecClass(ExportCRDDataDO.class.getName());
				fileExportDO.setTitle(Constant.custRewardTitle);
			}else if(loginUser.isProvinceqx()){//是否有省权限
				fileExportDO.setExecuteMethod(Constant.exportQueryMethodName);
				fileExportDO.setSpecClass(ExportCRDDataDO.class.getName());
				fileExportDO.setTitle(Constant.custRewardTitle);
			}else {
				//账号是网点权限,到网点页面； 跳到非网点权限页面
				if(loginUser.getOrgCode() != null & !loginUser.getOrgCode().isEmpty()){
					//网点权限，直接导出，不通过导出队列
/*					fileExportDO.setExecuteMethod(Constant.exportBranchQueryMethodName);
					fileExportDO.setSpecClass(ExportBranchCRDDataDO.class.getName());
					fileExportDO.setTitle(Constant.branchCustRewardTitle);*/
				}else{
					fileExportDO.setExecuteMethod(Constant.exportQueryMethodName);
					fileExportDO.setSpecClass(ExportCRDDataDO.class.getName());
					fileExportDO.setTitle(Constant.custRewardTitle);
				}
			}
		}
		//fileExportDO.setExecuteMethod(Constant.exportQueryMethodName);
		fileExportDO.setExecuteParam(filterJson);
		fileExportDO.setState(ExportEnum.StandingBy.getNum());// 等待中
		fileExportDO.setUserId(filteruser);
/*			//账号是网点权限
			if(loginUser.getOrgCode() != null & !loginUser.getOrgCode().isEmpty()){
				fileExportDO.setTitle(Constant.branchCustRewardTitle);
			}else{
				fileExportDO.setTitle(Constant.custRewardTitle);
			}*/
		List<FileExportDO> repeatData = fileExportService.getRepeatData(filterJson, stes[1].getClassName(), Constant.exportQueryMethodName, filteruser);
		if (!ObjectUtils.isEmpty(repeatData)) {
			for (FileExportDO fileExportdo : repeatData) {
				if(StringUtils.equalsAny(fileExportdo.getState(), ExportEnum.StandingBy.getNum(), ExportEnum.Handing.getNum())){
					return new RspBean(RespEnum.NO_CLICK);
				}
			}
		} // 添加到队列中
		fileExportService.save(fileExportDO);// 开始保存数据的一些基本信息
		return new RspBean(RespEnum.STANDING_BY);
	}

		@SuppressWarnings("all")
		public List<ExportCRDDataDO> exportQuery(String boReportCustRewardDetails, UserDO loginUser,String offset) {
			if (reportCustRewardDetailsService == null) {
				// 由于反射无法进行自动注入,所以手动进行注入
				reportCustRewardDetailsService = SpringUtil.getBean(ReportCustRewardDetailsService.class);
			} 
			
			Bo_reportCustRewardDetails_list boReportCustRewardDetailsList = JSON.parseObject(boReportCustRewardDetails, Bo_reportCustRewardDetails_list.class);
			boReportCustRewardDetailsList.setOffset(Integer.valueOf(offset));
			boReportCustRewardDetailsList.setLimit(Integer.valueOf(SysConfig.exportExcelLimit));
			if (!ObjectUtils.isEmpty(boReportCustRewardDetailsList)) {
				List<ReportCustRewardDetailsDO> reportCustRewardDetailsList = null;
				try {
					reportCustRewardDetailsList = reportCustRewardDetailsService.getCustRewardDetailsList(boReportCustRewardDetailsList, loginUser);
					if (reportCustRewardDetailsList != null) {
						for (ReportCustRewardDetailsDO data : reportCustRewardDetailsList) {
							if ("1".equals(data.getCustomerSourceType())) {
								data.setCustomerSourceType("菜鸟");
							} else if ("2".equals(data.getCustomerSourceType())) {
								data.setCustomerSourceType("二维码");
							} else if ("4".equals(data.getCustomerSourceType())) {
								data.setCustomerSourceType("京东");
							} else if ("5".equals(data.getCustomerSourceType())) {
								data.setCustomerSourceType("拼多多");
							}
							
							if ("a".equals(data.getCustLevel())) {
								data.setCustLevel("A类");
							} else if ("b".equals(data.getCustLevel())) {
								data.setCustLevel("B类");
							} else if ("c".equals(data.getCustLevel())) {
								data.setCustLevel("C类");
							} else if ("d".equals(data.getCustLevel())) {
								data.setCustLevel("D类");
							} else if ("e".equals(data.getCustLevel())) {
								data.setCustLevel("E类");
							} else if ("f".equals(data.getCustLevel())) {
								data.setCustLevel("F类");
							} else if ("g".equals(data.getCustLevel())) {
								data.setCustLevel("G类");
							}
						}
					}

				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return reportCustRewardDetailsService.filterData(reportCustRewardDetailsList);
			}
			return null;
		}
		
		@SuppressWarnings("all")
		public List<ExportBranchCRDDataDO> exportBranchQuery(String boReportCustRewardDetails, UserDO loginUser,String offset) {
			if (reportCustRewardDetailsService == null) {
				// 由于反射无法进行自动注入,所以手动进行注入
				reportCustRewardDetailsService = SpringUtil.getBean(ReportCustRewardDetailsService.class);
			} 
			
			Bo_reportCustRewardDetails_list boReportCustRewardDetailsList = JSON.parseObject(boReportCustRewardDetails, Bo_reportCustRewardDetails_list.class);
			boReportCustRewardDetailsList.setOffset(Integer.valueOf(offset));
			boReportCustRewardDetailsList.setLimit(Integer.valueOf(SysConfig.exportExcelLimit));
			if (!ObjectUtils.isEmpty(boReportCustRewardDetailsList)) {
				List<ReportCustRewardDetailsDO> reportCustRewardDetailsList = null;
				try {
					reportCustRewardDetailsList = reportCustRewardDetailsService.getCustRewardDetailsList(boReportCustRewardDetailsList, loginUser);
					if (reportCustRewardDetailsList != null) {
						for (ReportCustRewardDetailsDO data : reportCustRewardDetailsList) {
							if ("1".equals(data.getCustomerSourceType())) {
								data.setCustomerSourceType("菜鸟");
							} else if ("2".equals(data.getCustomerSourceType())) {
								data.setCustomerSourceType("二维码");
							} else if ("4".equals(data.getCustomerSourceType())) {
								data.setCustomerSourceType("京东");
							} else if ("5".equals(data.getCustomerSourceType())) {
								data.setCustomerSourceType("拼多多");
							}
							
							if ("a".equals(data.getCustLevel())) {
								data.setCustLevel("A类");
							} else if ("b".equals(data.getCustLevel())) {
								data.setCustLevel("B类");
							} else if ("c".equals(data.getCustLevel())) {
								data.setCustLevel("C类");
							} else if ("d".equals(data.getCustLevel())) {
								data.setCustLevel("D类");
							} else if ("e".equals(data.getCustLevel())) {
								data.setCustLevel("E类");
							} else if ("f".equals(data.getCustLevel())) {
								data.setCustLevel("F类");
							} else if ("g".equals(data.getCustLevel())) {
								data.setCustLevel("G类");
							}
						}
					}

				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				return reportCustRewardDetailsService.filterBranchData(reportCustRewardDetailsList);
			}
			return null;
		}
		
		
		
		// 导出excel
		@RequestMapping("/exportBranchCustRewardDetailsExcel")
		@SensitiveOperateLog(value = "奖励明细表(网点)",type = "导出")
//		@MethodLock(key = "totalexportExcel")
		@RequiresPermissions("report:reportBranchCustRewardDetails:exportExcel")
		public void exportExcel(HttpServletRequest request,@ModelAttribute  @Validated final Bo_reportCustRewardDetails_list boReportCustRewardDetailsList, BindingResult bindingResult,HttpServletResponse response) {
			//导出功能是否开放  true表示开放
			if (SysConfig.DAOCHU.equals("false")) {
				return;
			} else if (SysConfig.DAOCHU.equals("true")) {
		        UserDO loginUser = getUser(request);
				List<ReportCustRewardDetailsDO> result = null;
				try {
					result = reportCustRewardDetailsService.getCustRewardDetailsList(boReportCustRewardDetailsList,loginUser);
				} catch (Exception e) {
					//e1.printStackTrace();
					log.error(e.getMessage(), e);
				}
				String targetFile = SysConfig.TARGET + "奖励明细表(网点)"+ DateUtils.formatHMS(new java.util.Date()) + ".xlsx";
				File downloadFile = new File(targetFile);
				try {
					// 按命名规则找模版文件
					File file = new File(SysConfig.TEMPLATE+"custBranchRewardDetail.xlsx");
					response.setContentType("application/vnd.ms-excel;charset=utf-8");
					response.setCharacterEncoding("utf-8");
		
					// set headers for the response
					String headerKey = "Content-Disposition";
					String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
					response.setHeader(headerKey, headerValue);
		
					
					if (file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")) {
						Map<String, String> data = new HashMap<>();
						// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
						ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, reportCustRewardDetailsService.filterBranchData(result), data, ExportBranchCRDDataDO.class,
								false, response.getOutputStream());
					} else {
						// 模版文件不存在，默认输出
						ExcelUtils.getInstance().exportObjects2Excel(reportCustRewardDetailsService.filterBranchData(result), ExportBranchCRDDataDO.class, response.getOutputStream());
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}
