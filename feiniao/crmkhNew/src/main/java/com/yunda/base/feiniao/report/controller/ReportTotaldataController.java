package com.yunda.base.feiniao.report.controller;

import io.swagger.annotations.ApiOperation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.crab2died.ExcelUtils;
import com.yunda.base.common.config.Constant;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.enums.ExportEnum;
import com.yunda.base.common.enums.RespEnum;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.SpringUtil;
import com.yunda.base.feiniao.report.bo.Bo_CustOdSumdata;
import com.yunda.base.feiniao.report.bo.Bo_ReportTotaldata;
import com.yunda.base.feiniao.report.bo.Bo_reportCustRewardDetails_list;
import com.yunda.base.feiniao.report.domain.ExportBranchCRDDataDO;
import com.yunda.base.feiniao.report.domain.ExportCRDDataDO;
import com.yunda.base.feiniao.report.domain.ExportCustBranchReportTotaldataDO;
import com.yunda.base.feiniao.report.domain.ExportCustReportTotaldataDO;
import com.yunda.base.feiniao.report.domain.ExportReportTotaldataDO;
import com.yunda.base.feiniao.report.domain.ReportCustRewardDetailsDO;
import com.yunda.base.feiniao.report.domain.ReportTotaldataDO;
import com.yunda.base.feiniao.report.service.ReportCustRewardDetailsService;
import com.yunda.base.feiniao.report.service.ReportTotaldataService;
import com.yunda.base.feiniao.report.utils.JsonFilterUtils;
import com.yunda.base.system.annotation.SensitiveOperateLog;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.FileExportDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.service.FileExportService;
import com.yunda.base.system.service.SessionService;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.StringUtils;
/**
 * 客户报表订单统计-总表
 * 
 * @author yunda
 */
 
@Controller
@RequestMapping("/report/reportTotaldata")
public class ReportTotaldataController extends BaseController{
	
    private static final Logger log = Logger.getLogger(ReportTotaldataController.class);
	
/*	@Autowired
	private ReportTotaldataService reportTotaldataService;	
	@GetMapping()
	@RequiresPermissions("report:reportTotaldata:reportTotaldata")
	String ReportTotaldata(Model model,HttpServletRequest request){
		return "feiniao/report/reportTotaldata/reportTotaldata";
	}*/
	
/*	@ApiOperation(value = "获取总表")
	@ResponseBody
	@GetMapping("/list")
	@SensitiveOperateLog(value = "总表",type = "查询")
	@RequiresPermissions("report:reportTotaldata:reportTotaldata")
	@MethodLock(key = "request")
	public RspBean<PageUtils> list(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata boReportTotaldata ,BindingResult bindingResult){
		
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			//如果验参报错，返回空值给页面
			PageUtils pageUtils = new PageUtils(null, 0);
			return success(pageUtils);			
		}
		
		final UserDO loginUser = getUser(request);
		//查询列表数据
		List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService.list(boReportTotaldata,loginUser);
		//int total = reportTotaldataService.count(query);
		PageUtils pageUtils = new PageUtils(reportTotaldataList, 1);
		return success(pageUtils);
	}*/
	
    @Autowired
	private ReportTotaldataService reportTotaldataService;
	@Autowired
	public SessionService sessionService;
	@Autowired
	public FileExportService fileExportService;
    
    @GetMapping()
	@RequiresPermissions("report:reportTotaldata:reportTotaldataAll")
	String ReportTotaldata(Model model,HttpServletRequest request){
		return "feiniao/report/reportTotaldata/reportTotaldataAll";
	}



    
	@ApiOperation(value = "获取总表-省表")
	@ResponseBody
	@GetMapping("/listAll")
	@SensitiveOperateLog(value = "总表-省表",type = "查询")
	@RequiresPermissions("report:reportTotaldata:reportTotaldataAll")
	@MethodLock(key = "request")
	public RspBean<PageUtils> listAll(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata boReportTotaldata ,BindingResult bindingResult){
		
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			//如果验参报错，返回空值给页面
			PageUtils pageUtils = new PageUtils(null, 0);
			return success(pageUtils);			
		}
		
		final UserDO loginUser = getUser(request);
		//查询列表数据
		List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService.listNew(boReportTotaldata,loginUser);
		PageUtils pageUtils = new PageUtils(reportTotaldataList, 1);
		return success(pageUtils);
	}
	
	/**
	 * 跳转总表-城市页面
	 * 菜单地址：	/report/reportTotaldata/totalCity
	 * 菜单权限：	report:reportTotaldata:reportTotaldataCity
	 * @param model
	 * @param request
	 * @return
	 */
	@GetMapping("/totalCity")
	@RequiresPermissions("report:reportTotaldata:reportTotaldataCity")
	String ReportTotaldataCity(Model model,HttpServletRequest request){
		return "feiniao/report/reportTotaldata/reportTotaldataCity";
	}
	
	@ApiOperation(value = "获取总表—市表")
	@ResponseBody
	@GetMapping("/listCity")
	@SensitiveOperateLog(value = "总表-市表",type = "查询")
	@RequiresPermissions("report:reportTotaldata:reportTotaldataCity")
	@MethodLock(key = "request")
	public RspBean<PageUtils> listCity(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata boReportTotaldata ,BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			//如果验参报错，返回空值给页面
			PageUtils pageUtils = new PageUtils(null, 0);
			return success(pageUtils);			
		}
		final UserDO loginUser = getUser(request);
		//查询列表数据
		List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService.listNew(boReportTotaldata,loginUser);
		int total = 0;
		if(reportTotaldataList == null || reportTotaldataList.size() <= 0){
			
		}else{
			total = reportTotaldataService.cityCount(boReportTotaldata,loginUser);
		}
		PageUtils pageUtils = new PageUtils(reportTotaldataList, total);
		return success(pageUtils);
	}

	/**
	 * 跳转总表-网点页面
	 * 菜单地址：	/report/reportTotaldata/totalBranch
	 * 菜单权限：	report:reportTotaldata:reportTotaldataBranch
	 * @param model
	 * @param request
	 * @return
	 */
	@GetMapping("/totalBranch")
	@RequiresPermissions("report:reportTotaldata:reportTotaldataBranch")
	String ReportTotaldataBranch(Model model,HttpServletRequest request){
		return "feiniao/report/reportTotaldata/reportTotaldataBranch";
	}
	
	@ApiOperation(value = "获取总表-网点总表")
	@ResponseBody
	@GetMapping("/listBranch")
	@SensitiveOperateLog(value = "总表-网点",type = "查询")
	@RequiresPermissions("report:reportTotaldata:reportTotaldataBranch")
	@MethodLock(key = "request")
	public RspBean<PageUtils> listBranch(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata boReportTotaldata ,BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			//如果验参报错，返回空值给页面
			PageUtils pageUtils = new PageUtils(null, 0);
			return success(pageUtils);			
		}
		final UserDO loginUser = getUser(request);
		//查询列表数据
		List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService.listNew(boReportTotaldata,loginUser);
		int total = 0;
		if(reportTotaldataList == null || reportTotaldataList.size() <= 0){
			
		}else{
			total = reportTotaldataService.branchCount(boReportTotaldata,loginUser);
		}
		PageUtils pageUtils = new PageUtils(reportTotaldataList, total);
		return success(pageUtils);
	}
	
	/**
	 * 跳转总表-客户页面
	 * 菜单地址：	/report/reportTotaldata/totalCust
	 * 菜单权限：	report:reportTotaldata:reportTotaldataCust
	 * @param model
	 * @param request
	 * @return
	 */
	@GetMapping("/totalCust")
	@RequiresPermissions("report:reportTotaldata:reportTotaldataCust")
	String ReportTotaldataCust(Model model,HttpServletRequest request){
		UserDO loginUser = getUser(request);
		if (loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			// 超级用户权限 无限制
			// 系统菜单配置了report:admin:allperms权限标识 角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户
			// 跳到非网点权限页面
			return "feiniao/report/reportTotaldata/reportTotaldataCust";
		}else{
			if(loginUser.isBigareaqx()){//是否有大区权限
				return "feiniao/report/reportTotaldata/reportTotaldataCust";
			}else if(loginUser.isProvinceqx()){//是否有省权限
				return "feiniao/report/reportTotaldata/reportTotaldataCust";
			}else {
				//账号是网点权限,到网点页面； 跳到非网点权限页面
				if(loginUser.getOrgCode() != null & !loginUser.getOrgCode().isEmpty()){
					return "feiniao/report/reportTotaldata/reportTotaldataCustBranch";
				}else{
					return "feiniao/report/reportTotaldata/reportTotaldataCust";
				}
			}
		}
		
	}
	
	@ApiOperation(value = "获取总表-客户总表")
	@ResponseBody
	@GetMapping("/listCust")
	@SensitiveOperateLog(value = "总表-客户",type = "查询")
	@RequiresPermissions("report:reportTotaldata:reportTotaldataCust")
	@MethodLock(key = "request")
	public RspBean<PageUtils> listCust(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata boReportTotaldata ,BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			//如果验参报错，返回空值给页面
			PageUtils pageUtils = new PageUtils(null, 0);
			return success(pageUtils);			
		}
		final UserDO loginUser = getUser(request);
		//查询列表数据
		List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService.listNew(boReportTotaldata,loginUser);
		int total = 0;
		if(reportTotaldataList == null || reportTotaldataList.size() <= 0){
			
		}else{
			total = reportTotaldataService.custCount(boReportTotaldata,loginUser);
		}
		PageUtils pageUtils = new PageUtils(reportTotaldataList, total);
		return success(pageUtils);
	}
	
	@ApiOperation(value = "查询网点公司所有客户信息")
	@ResponseBody
	@GetMapping("/searchData")
	@RequiresPermissions("report:reportTotaldata:reportTotaldata")
	@MethodLock(key="request")
	public RspBean<PageUtils> searchData(HttpServletRequest request, HttpServletResponse response,@ModelAttribute @Validated Bo_CustOdSumdata bo_ReportTotaldata ,BindingResult bindingResult,@RequestParam Map<String, Object> params){
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			//如果验参报错，返回空值给页面
			PageUtils pageUtils = new PageUtils(null, 0);
			return success(pageUtils);			
		}
		//查询列表数据
        Query query = new Query(params);
		List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService.searchData(query);
		int total = reportTotaldataService.countSearchData(query);
		PageUtils pageUtils = new PageUtils(reportTotaldataList, total);
		return success(pageUtils);
	}
			
			
/*		// 客户导出excel
		@RequestMapping("/exportCustExcel")
		@MethodLock(key = "export15CustExcel")
		@SensitiveOperateLog(value = "总表",type = "导出")
		@RequiresPermissions("report:reportTotaldata:reportTotaldata")
		public void exportCustExcel(HttpServletResponse response,HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata ,BindingResult bindingResult,@RequestParam Map<String, Object> params) {
			//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
			BufferedInputStream bin = null;
	        OutputStream out = null;
			//查询列表数据
	        Query query = new Query(params);
	        query.put("offset", 0);
	        query.put("limit", 10000);

			List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService.searchData(query);
			String targetFile = SysConfig.TARGET + "总表-客户表"+ DateUtils.formatHMS(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);

			try {
//					ExcelUtils.getInstance().exportObjects2Excel(result, ReportTotaldataDO.class, targetFile);
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"demo.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");

				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);

				
				if (file.exists()) {
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, reportTotaldataService.filterCustData(reportTotaldataList), data, ExportCustReportTotaldataDO.class,
							true, targetFile);
				} else {
					// 模版文件不存在，默认输出
					ExcelUtils.getInstance().exportObjects2Excel(reportTotaldataService.filterCustData(reportTotaldataList), ExportCustReportTotaldataDO.class, targetFile);
				}
				
				
				// 写入response
				try {
					InputStream myStream = new FileInputStream(targetFile);
					 bin = new BufferedInputStream(myStream);
			           out = response.getOutputStream();
			            int size = 0;
			            byte[] buf = new byte[1024];
			            while ((size = bin.read(buf)) != -1) {
			                out.write(buf, 0, size);
			            }
				
				} catch (IOException e) {
					log.error(e.getMessage(), e);
				}finally {
		            try {
		                bin.close();
		                out.close(); 
		            } catch (IOException e) {
		                log.warn(e.getMessage(),e);
                    }
		        }	
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}	
		}	
		
	// 客户导出excel
	@RequestMapping("/exportCustBranchExcel")
	@MethodLock(key = "export15CustExcel")
	@SensitiveOperateLog(value = "总表", type = "导出")
	@RequiresPermissions("report:reportTotaldata:reportTotaldata")
	public void exportCustBranchExcel(HttpServletResponse response,
			HttpServletRequest request,
			@ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata,
			BindingResult bindingResult,
			@RequestParam Map<String, Object> params) {
		// 导出功能是否开放 true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
			BufferedInputStream bin = null;
			OutputStream out = null;
			//UserDO loginUser = getUser(request);

			// 查询列表数据
			Query query = new Query(params);
			query.put("offset", 0);
			query.put("limit", 10000);

			List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService
					.searchData(query);
			String targetFile = SysConfig.TARGET + "总表-客户表"
					+ DateUtils.formatHMS(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);

			try {
				// ExcelUtils.getInstance().exportObjects2Excel(result,
				// ReportTotaldataDO.class, targetFile);
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE + "demo.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");

				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format(
						"attachment; filename=\"%s\"", new String(downloadFile
								.getName().getBytes(StandardCharsets.UTF_8),
								"iso8859-1"));
				response.setHeader(headerKey, headerValue);

				if (file.exists()) {
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(
							file.getPath(),
							0,
							reportTotaldataService
									.filterCustBranchData(reportTotaldataList),
							data, ExportCustBranchReportTotaldataDO.class,
							true, targetFile);
				} else {
					// 模版文件不存在，默认输出
					ExcelUtils
							.getInstance()
							.exportObjects2Excel(
									reportTotaldataService
											.filterCustBranchData(reportTotaldataList),
									ExportCustBranchReportTotaldataDO.class,
									targetFile);
				}

				// 写入response
				try {
					InputStream myStream = new FileInputStream(targetFile);
					bin = new BufferedInputStream(myStream);
					out = response.getOutputStream();
					int size = 0;
					byte[] buf = new byte[1024];
					while ((size = bin.read(buf)) != -1) {
						out.write(buf, 0, size);
					}

				} catch (IOException e) {
					// e.printStackTrace();
					log.error(e.getMessage(), e);
				} finally {
					try {
						bin.close();
						out.close();
					} catch (IOException e) {
						log.warn(e.getMessage(), e);
					}
				}
			} catch (Exception e) {
				// e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
	}*/
	
	
	
	
	//为解决导出excel数据量超大的问题，引入异步及串行处理方案，该导出接口只将用户请求放入队列。方案详情参看ExportTask类注释  exportExcelBranch
	//各业务方法自行根据业务特性对导出excel的数据进行查询，本方法命名必须和Constant.exportQueryMethodName中定义值一致，处理任务ExportTask会依据此命名反射本方法。
	@ApiOperation(value = "总表-客户表导出")
	@RequestMapping("/exportExcelCust")
	@RequiresPermissions("report:reportTotaldataCust:exportExcel")
	@ResponseBody
	@SuppressWarnings("all")
	public RspBean exportExcel(HttpServletRequest request, HttpServletResponse response, @ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			// 如果验参报错，不往下走
			return failure(RespEnum.ERROR_BUSINESS_OPERATE.getCode());
		}		
		
		UserDO loginUser = getUser(request);
		String filterJson = JsonFilterUtils.filterJson(bo_ReportTotaldata);
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
			fileExportDO.setSpecClass(ExportCustReportTotaldataDO.class.getName());
			fileExportDO.setTitle(Constant.reportTotaldataCust);
		}else{
			if(loginUser.isBigareaqx()){//是否有大区权限
				fileExportDO.setExecuteMethod(Constant.exportQueryMethodName);
				fileExportDO.setSpecClass(ExportCustReportTotaldataDO.class.getName());
				fileExportDO.setTitle(Constant.reportTotaldataCust);
			}else if(loginUser.isProvinceqx()){//是否有省权限
				fileExportDO.setExecuteMethod(Constant.exportQueryMethodName);
				fileExportDO.setSpecClass(ExportCustReportTotaldataDO.class.getName());
				fileExportDO.setTitle(Constant.reportTotaldataCust);
			}else {
				//账号是网点权限,到网点页面； 跳到非网点权限页面
				if(loginUser.getOrgCode() != null & !loginUser.getOrgCode().isEmpty()){
					/*fileExportDO.setExecuteMethod(Constant.exportBranchQueryMethodName);
					fileExportDO.setSpecClass(ExportCustBranchReportTotaldataDO.class.getName());
					fileExportDO.setTitle(Constant.reportTotaldataCustBranch);*/
				}else{
					fileExportDO.setExecuteMethod(Constant.exportQueryMethodName);
					fileExportDO.setSpecClass(ExportCustReportTotaldataDO.class.getName());
					fileExportDO.setTitle(Constant.reportTotaldataCust);
				}
			}
		}
		fileExportDO.setExecuteParam(filterJson);
		fileExportDO.setState(ExportEnum.StandingBy.getNum());// 等待中
		fileExportDO.setUserId(filteruser);
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

	//非网点导出方法
	@SuppressWarnings("all")
	public List<ExportCustReportTotaldataDO> exportQuery(String boReportTotaldata, UserDO loginUser,String offset) {
		if (reportTotaldataService == null) {
			// 由于反射无法进行自动注入,所以手动进行注入
			reportTotaldataService = SpringUtil.getBean(ReportTotaldataService.class);
		} 
		
		Bo_ReportTotaldata boreportTotaldata = JSON.parseObject(boReportTotaldata, Bo_ReportTotaldata.class);
		boreportTotaldata.setOffset(Integer.valueOf(offset));
		boreportTotaldata.setLimit(Integer.valueOf(SysConfig.exportExcelLimit));
		if (!ObjectUtils.isEmpty(boreportTotaldata)) {
			List<ReportTotaldataDO> reportTotaldataList = null;
			try {
				reportTotaldataList = reportTotaldataService.listNew(boreportTotaldata, loginUser);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			return reportTotaldataService.filterCustData(reportTotaldataList);
		}
		return null;
	}
	
	//网点导出方法
	@SuppressWarnings("all")
	public List<ExportCustBranchReportTotaldataDO> exportBranchQuery(String boReportTotaldata, UserDO loginUser,String offset) {
		if (reportTotaldataService == null) {
			// 由于反射无法进行自动注入,所以手动进行注入
			reportTotaldataService = SpringUtil.getBean(ReportTotaldataService.class);
		} 
		
		Bo_ReportTotaldata boreportTotaldata = JSON.parseObject(boReportTotaldata, Bo_ReportTotaldata.class);
		boreportTotaldata.setOffset(Integer.valueOf(offset));
		boreportTotaldata.setLimit(Integer.valueOf(SysConfig.exportExcelLimit));
		if (!ObjectUtils.isEmpty(boreportTotaldata)) {
			List<ReportTotaldataDO> reportTotaldataList = null;
			try {
				reportTotaldataList = reportTotaldataService.listNew(boreportTotaldata, loginUser);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			return reportTotaldataService.filterCustBranchData(reportTotaldataList);
		}
		return null;
	}
	
	
	// 导出excel
	@RequestMapping("/exportExcelCustBranch")
	@SensitiveOperateLog(value = "总表-客户表(网点)",type = "导出")
//	@MethodLock(key = "totalexportExcel")
	@RequiresPermissions("report:reportTotaldataCustBranch:exportExcel")
	public void exportExcel2(HttpServletRequest request,@ModelAttribute  @Validated final Bo_ReportTotaldata bo_ReportTotaldata, BindingResult bindingResult,HttpServletResponse response) {
		//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
	        UserDO loginUser = getUser(request);
			List<ReportTotaldataDO> result = null;
			try {
				result = reportTotaldataService.listNew(bo_ReportTotaldata, loginUser);
			} catch (Exception e) {
				//e1.printStackTrace();
				log.error(e.getMessage(), e);
			}
			String targetFile = SysConfig.TARGET + "总表-客户表(网点)"+ DateUtils.formatHMS(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"demotest.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
	
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
	
				
				if (file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")) {
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, reportTotaldataService.filterCustBranchData(result), data, ExportCustBranchReportTotaldataDO.class,
							false, response.getOutputStream());
				} else {
					// 模版文件不存在，默认输出
					ExcelUtils.getInstance().exportObjects2Excel(reportTotaldataService.filterCustBranchData(result), ExportCustBranchReportTotaldataDO.class, response.getOutputStream());
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
		
	/**
	 * 点击省，获取对应省的市的数据
	 * 
	 * @param provinceid
	 * @param startDate
	 * @param endDate
	 * @param showType
	 * @param model
	 * @return
	 */

	@GetMapping("/reportTotalCity/{startDate}/{endDate}/{bigarea}/{provinceid}")
	@RequiresPermissions("report:reportTotal:reportTotalCity")
	String ReportFluctuateSheng(@PathVariable("startDate") String startDate
			,@PathVariable("endDate") String endDate,@PathVariable("bigarea") String bigarea
			,@PathVariable("provinceid") String provinceid,Model model){
		Bo_ReportTotaldata reportTotaldata = new Bo_ReportTotaldata();
		reportTotaldata.setStart_date(startDate);
		reportTotaldata.setEnd_date(endDate);
		reportTotaldata.setBig_area(bigarea);
		reportTotaldata.setProvince_id(provinceid);

		model.addAttribute("reportTotaldata", reportTotaldata);
	    return "feiniao/report/reportTotaldata/reportCitydata";

	}
	
	
	/**
	 * 点击市，获取对应市的网点的数据
	 * 
	 * @param provinceid
	 * @param startDate
	 * @param endDate
	 * @param showType
	 * @param model
	 * @return
	 */
	@GetMapping("/reportTotalBranch/{start_date}/{end_date}/{bigarea}/{provinceid}/{cityid}")
	@RequiresPermissions("report:reportTotal:reportTotalBranch")
	String ReportTotalBranch(@PathVariable("start_date") String startDate
			,@PathVariable("end_date") String endDate,@PathVariable("bigarea") String bigarea
			,@PathVariable("provinceid") String provinceid,@PathVariable("cityid") String cityid,Model model){
		Bo_ReportTotaldata reportTotaldata = new Bo_ReportTotaldata();
		reportTotaldata.setStart_date(startDate);
		reportTotaldata.setEnd_date(endDate);
		reportTotaldata.setBig_area(bigarea);
		reportTotaldata.setProvince_id(provinceid);
		reportTotaldata.setCity_id(cityid);

		model.addAttribute("reportTotaldata", reportTotaldata);
	    return "feiniao/report/reportTotaldata/reportBranchdata";

	}
	
	
	/**
	 * 点击网点，获取对应客户的数据
	 * 
	 * @param provinceid
	 * @param startDate
	 * @param endDate
	 * @param showType
	 * @param model
	 * @return
	 */
	@GetMapping("/reportTotalCustomer/{start_date}/{end_date}/{tmp_field}/{branch_code}")
	@RequiresPermissions("report:reportTotal:reportTotalCustomer")
	String ReportTotalCustomer(HttpServletRequest request,@PathVariable("start_date") String startDate
			,@PathVariable("end_date") String endDate,@PathVariable("tmp_field") String tmpField
			,@PathVariable("branch_code") String branchCode,Model model){
		Bo_ReportTotaldata reportTotaldata = new Bo_ReportTotaldata();
		reportTotaldata.setStart_date(startDate);
		reportTotaldata.setEnd_date(endDate);
		reportTotaldata.setTmp_field(tmpField);
		reportTotaldata.setBranch_code(branchCode);
		model.addAttribute("reportTotaldata", reportTotaldata);
		UserDO loginUser = getUser(request);
		if (loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			// 超级用户权限 无限制
			// 系统菜单配置了report:admin:allperms权限标识 角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户
			// 跳到非网点权限页面
			return "feiniao/report/reportTotaldata/reportCustomerdata";
		}else{
			if(loginUser.isBigareaqx()){//是否有大区权限
				return "feiniao/report/reportTotaldata/reportCustomerdata";
			}else if(loginUser.isProvinceqx()){//是否有省权限
				return "feiniao/report/reportTotaldata/reportCustomerdata";
			}else {
				//账号是网点权限,到网点页面； 跳到非网点权限页面
				if(loginUser.getOrgCode() != null & !loginUser.getOrgCode().isEmpty()){
					return "feiniao/report/reportTotaldata/reportBranchCustomerdata";
				}else{
					return "feiniao/report/reportTotaldata/reportCustomerdata";
					}
			}
		}
	    

	}

	// 导出excel
	@RequestMapping("/exportExcelAll")
	@SensitiveOperateLog(value = "总表-省表",type = "导出")
//	@MethodLock(key = "totalexportExcel")
	@RequiresPermissions("report:reportTotaldataAll:exportExcel")
	public void exportExcel(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata ,BindingResult bindingResult,HttpServletResponse response) {
		//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
	        UserDO loginUser = getUser(request);
			List<ReportTotaldataDO> result = reportTotaldataService.listNew(bo_ReportTotaldata,loginUser);
			String targetFile = SysConfig.TARGET + "总表"+ DateUtils.formatHMS(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
	
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"total.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
	
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
	
				
				if (file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")) {
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, reportTotaldataService.filterData(result), data, ExportReportTotaldataDO.class,
							false, response.getOutputStream());
				} else {
					// 模版文件不存在，默认输出
					ExcelUtils.getInstance().exportObjects2Excel(reportTotaldataService.filterData(result), ExportReportTotaldataDO.class, response.getOutputStream());
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}	

	
	// 导出总表-市表excel
	@RequestMapping("/exportExcelCity")
	@SensitiveOperateLog(value = "总表-市表",type = "导出")
//	@MethodLock(key = "totalexportExcel")
	@RequiresPermissions("report:reportTotaldataCity:exportExcel")
	public void exportExcelCity(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata ,BindingResult bindingResult,HttpServletResponse response) {
		//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
	        UserDO loginUser = getUser(request);
			List<ReportTotaldataDO> result = reportTotaldataService.listNew(bo_ReportTotaldata,loginUser);
			String targetFile = SysConfig.TARGET + "总表"+ DateUtils.formatHMS(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
	
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"total.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
	
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
	
				
				if (file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")) {
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, reportTotaldataService.filterData(result), data, ExportReportTotaldataDO.class,
							false, response.getOutputStream());
				} else {
					// 模版文件不存在，默认输出
					ExcelUtils.getInstance().exportObjects2Excel(reportTotaldataService.filterData(result), ExportReportTotaldataDO.class, response.getOutputStream());
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	// 导出总表-网点excel
	@RequestMapping("/exportExcelBranch")
	@SensitiveOperateLog(value = "总表-网点",type = "导出")
//	@MethodLock(key = "totalexportExcel")
	@RequiresPermissions("report:reportTotaldataBranch:exportExcel")
	public void exportExcelBranch(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata ,BindingResult bindingResult,HttpServletResponse response) {
		//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
	        UserDO loginUser = getUser(request);
			List<ReportTotaldataDO> result = reportTotaldataService.listNew(bo_ReportTotaldata,loginUser);
			String targetFile = SysConfig.TARGET + "总表"+ DateUtils.formatHMS(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
	
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"total.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
	
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
	
				
				if (file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")) {
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, reportTotaldataService.filterData(result), data, ExportReportTotaldataDO.class,
							false, response.getOutputStream());
				} else {
					// 模版文件不存在，默认输出
					ExcelUtils.getInstance().exportObjects2Excel(reportTotaldataService.filterData(result), ExportReportTotaldataDO.class, response.getOutputStream());
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
/*	// 导出总表-客户excel
	@RequestMapping("/exportExcelCust")
	@SensitiveOperateLog(value = "总表-客户",type = "导出")
//	@MethodLock(key = "totalexportExcel")
	@RequiresPermissions("report:reportTotaldataCust:exportExcel")
	public void exportExcelCust(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata ,BindingResult bindingResult,HttpServletResponse response) {
		//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
	        UserDO loginUser = getUser(request);
			List<ReportTotaldataDO> result = reportTotaldataService.listNew(bo_ReportTotaldata,loginUser);
			String targetFile = SysConfig.TARGET + "总表"+ DateUtils.formatHMS(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
	
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"totalCust.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
	
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
	
				
				if (file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")) {
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, reportTotaldataService.filterCustData(result), data, ExportCustReportTotaldataDO.class,
							false, response.getOutputStream());
				} else {
					// 模版文件不存在，默认输出
					ExcelUtils.getInstance().exportObjects2Excel(reportTotaldataService.filterData(result), ExportCustReportTotaldataDO.class, response.getOutputStream());
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}*/

	@GetMapping("/edit/{bigarea}")
	@RequiresPermissions("report:reportTotaldata:edit")
	String edit(@PathVariable("bigarea") String bigarea,Model model){
		ReportTotaldataDO reportTotaldata = reportTotaldataService.get(bigarea);
		model.addAttribute("reportTotaldata", reportTotaldata);
	    return "feiniao/report/reportTotaldata/edit";
	}
	
}
