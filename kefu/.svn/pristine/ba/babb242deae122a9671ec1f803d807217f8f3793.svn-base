package com.yunda.base.feiniao.report.controller;

import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.crab2died.ExcelUtils;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.feiniao.report.bo.Bo_ReportFluctuate;
import com.yunda.base.feiniao.report.core.CRM_constants;
import com.yunda.base.feiniao.report.domain.ExportReportFluctuateCustDO;
import com.yunda.base.feiniao.report.domain.ExportReportFluctuateDataDO;
import com.yunda.base.feiniao.report.domain.ReportFluctuateDO;
import com.yunda.base.feiniao.report.service.ReportFluctuateService;
import com.yunda.base.feiniao.workorder.controller.ServiceWorkorderController;
import com.yunda.base.system.annotation.SensitiveOperateLog;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;
/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-18 13:31:19
 */

@Controller
@RequestMapping("/report/reportFluctuate")
public class ReportFluctuateController extends BaseController {
	private static final Logger log = Logger.getLogger(ServiceWorkorderController.class);
	
	/*@Value("${excel.targetFile.path}")
	private String target;
	
	@Value("${excel.template.path}")
	private String excelTemplate;*/
	
	@Autowired
	private ReportFluctuateService reportFluctuateService;

	@Autowired
	private RedisTemplate redisTemplate;
	
	//创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
	static ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();

    @GetMapping()
	@RequiresPermissions("report:reportFluctuate:reportFluctuate")
	String ReportFluctuate() {
		return "feiniao/report/reportFluctuate/reportFluctuate";
	}

	@ApiOperation(value = "获取波动表主页面")
	@ResponseBody
	@GetMapping("/list")
	@SensitiveOperateLog(value = "波动表",type = "查询")
	@RequiresPermissions("report:reportFluctuate:reportFluctuate")
	public RspBean<PageUtils> list(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute @Validated final Bo_ReportFluctuate boReportFluctuate, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			notifyPage(bindingResult.getFieldError().getDefaultMessage());
			// 如果验参报错，返回空值給頁面
			return success(new PageUtils());
		}

		final UserDO loginUser = getUser(request);
		// 对请求参数做基因tag
		final String tag = MD5Utils.encrypt(JSON.toJSONString(boReportFluctuate) + JSON.toJSONString(loginUser));

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
						
						List<ReportFluctuateDO> reportFluctuateList1 = reportFluctuateService
								.custNumReport(boReportFluctuate, loginUser);
						int total = reportFluctuateService.custBDcount(boReportFluctuate, loginUser);
						PageUtils pageUtils = new PageUtils(reportFluctuateList1, total);

						// 移除队列标记
						redisTemplate.boundHashOps(getClass().getSimpleName() + "_list").delete(tag);
						// 放置结果缓存
						redisTemplate.opsForValue().set(tag, pageUtils, 7, TimeUnit.DAYS);
					} catch (ParseException e) {
						log.error(e.getMessage(), e);
					}
				}
			});
		}

		notifyPage("报表生成中，复杂报表需要较长时间处理，您稍后再查");
		return success(new PageUtils());
	}

	// 导出excel（全网数据）
	@RequestMapping("/exportExcel")
	@SensitiveOperateLog(value = "波动表",type = "导出")
	@MethodLock(key = "exportFluctuateExcel")
	@RequiresPermissions("report:reportFluctuate:exportExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute @Validated Bo_ReportFluctuate boReportFluctuate, BindingResult bindingResult) {
		//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
			if (bindingResult.hasErrors()) {
				FieldError fieldError = bindingResult.getFieldError();
				notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
				// 如果验参报错，不往下走
				return;
			}
			Object obj = request.getSession().getAttribute(CRM_constants.AUTH_USER);
			UserDO loginUser =new UserDO();
			if(obj == null){
				
			}else{
				loginUser = (UserDO)obj;
			}
			List<ReportFluctuateDO> reportFluctuateList = null;
			// 查询列表数据
			try {
				reportFluctuateList = reportFluctuateService.custNumReport(boReportFluctuate, loginUser);
			} catch (ParseException e) {
				log.error(e.getMessage(), e);
			}
			String targetFile = SysConfig.TARGET + "fluctuate"+ DateUtils.format(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
	
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"fluctuate.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
				
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
				
				List<ExportReportFluctuateDataDO> result = reportFluctuateService.filterData(reportFluctuateList);
				if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, 
							result,data, ExportReportFluctuateDataDO.class, false, response.getOutputStream());
				} else {
					//模板文件不存在  默认输出
					ExcelUtils.getInstance().exportObjects2Excel(result,ExportReportFluctuateDataDO.class,response.getOutputStream());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
	}
	
	
	// 导出一个省的数据
	@RequestMapping("/exportShengExcel")
	@SensitiveOperateLog(value = "波动表省表",type = "导出")
	@MethodLock(key = "exportFluctuateExcel")
	@RequiresPermissions("report:reportFluctuate:exportExcel")
	public void exportExcelSheng(HttpServletRequest request,HttpServletResponse response,@ModelAttribute @Validated Bo_ReportFluctuate boReportFluctuate, BindingResult bindingResult) {
		//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
			if (bindingResult.hasErrors()) {
				FieldError fieldError = bindingResult.getFieldError();
				notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
				// 如果验参报错，不往下走
				return;
			}
			Object obj = request.getSession().getAttribute(CRM_constants.AUTH_USER);
			UserDO loginUser =new UserDO();
			if(obj == null){
				
			}else{
				loginUser = (UserDO)obj;
			}
			List<ReportFluctuateDO> reportFluctuateList = null;
			// 查询列表数据
			try {
				reportFluctuateList = reportFluctuateService.custNumReport(boReportFluctuate, loginUser);
			} catch (ParseException e) {
				log.error(e.getMessage(), e);
			}
			String targetFile = SysConfig.TARGET + "fluctuate"+ DateUtils.format(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
	
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"fluctuate.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
				
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
				
				List<ExportReportFluctuateDataDO> result = reportFluctuateService.filterData(reportFluctuateList);
				if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, 
							result,data, ExportReportFluctuateDataDO.class, false, response.getOutputStream());
				} else {
					//模板文件不存在  默认输出
					ExcelUtils.getInstance().exportObjects2Excel(result,ExportReportFluctuateDataDO.class,response.getOutputStream());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
	}
	
	
	
	// 导出一个市的数据
	@RequestMapping("/exportShiExcel")
	@SensitiveOperateLog(value = "波动表市表",type = "导出")
	@MethodLock(key = "exportFluctuateExcel")
	@RequiresPermissions("report:reportFluctuate:exportExcel")
	public void exportExcelShi(HttpServletRequest request,HttpServletResponse response,@ModelAttribute @Validated Bo_ReportFluctuate boReportFluctuate, BindingResult bindingResult) {
			//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
			if (bindingResult.hasErrors()) {
				FieldError fieldError = bindingResult.getFieldError();
				notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
				// 如果验参报错，不往下走
				return;
			}
			Object obj = request.getSession().getAttribute(CRM_constants.AUTH_USER);
			UserDO loginUser =new UserDO();
			if(obj == null){
				
			}else{
				loginUser = (UserDO)obj;
			}
			List<ReportFluctuateDO> reportFluctuateList = null;
			// 查询列表数据
			try {
				reportFluctuateList = reportFluctuateService.custNumReport(boReportFluctuate, loginUser);
			} catch (ParseException e) {
				log.error(e.getMessage(), e);
			}
			String targetFile = SysConfig.TARGET + "fluctuate"+ DateUtils.format(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
	
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"fluctuate.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
				
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
				
				List<ExportReportFluctuateDataDO> result = reportFluctuateService.filterData(reportFluctuateList);
				if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, 
							result,data, ExportReportFluctuateDataDO.class, false, response.getOutputStream());
				} else {
					//模板文件不存在  默认输出
					ExcelUtils.getInstance().exportObjects2Excel(result,ExportReportFluctuateDataDO.class,response.getOutputStream());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
	}
	
	// 导出客户波动的数据
		@RequestMapping("/exportCustExcel")
		@SensitiveOperateLog(value = "波动表客户表",type = "导出")
		@MethodLock(key = "exportFluctuateExcel")
		@RequiresPermissions("report:reportFluctuate:exportExcel")
		public void exportExcelCust(HttpServletRequest request,HttpServletResponse response,@ModelAttribute @Validated Bo_ReportFluctuate boReportFluctuate, BindingResult bindingResult) {
				//导出功能是否开放  true表示开放
			if (SysConfig.DAOCHU.equals("false")) {
				return;
			} else if (SysConfig.DAOCHU.equals("true")) {
				if (bindingResult.hasErrors()) {
					FieldError fieldError = bindingResult.getFieldError();
					notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
					// 如果验参报错，不往下走
					return;
				}
				boReportFluctuate.setLimit(50000);
				Object obj = request.getSession().getAttribute(CRM_constants.AUTH_USER);
				UserDO loginUser =new UserDO();
				if(obj == null){
					
				}else{
					loginUser = (UserDO)obj;
				}
				List<ReportFluctuateDO> reportFluctuateList = null;
				// 查询列表数据
				try {
					reportFluctuateList = reportFluctuateService.custNumReport(boReportFluctuate, loginUser);
				} catch (ParseException e) {
					log.error(e.getMessage(), e);
				}
				String targetFile = SysConfig.TARGET + "fluctuateCustomer"+ DateUtils.format(new java.util.Date()) + ".xlsx";
				File downloadFile = new File(targetFile);
		
				try {
					// 按命名规则找模版文件
					File file = new File(SysConfig.TEMPLATE+"fluctuateCustomer.xlsx");
					response.setContentType("application/vnd.ms-excel;charset=utf-8");
					response.setCharacterEncoding("utf-8");
					
					// set headers for the response
					String headerKey = "Content-Disposition";
					String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
					response.setHeader(headerKey, headerValue);
					
					List<ExportReportFluctuateCustDO> result = reportFluctuateService.filterDataCust(reportFluctuateList);
					if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
						Map<String, String> data = new HashMap<>();
						// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
						ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, 
								result,data, ExportReportFluctuateCustDO.class, false, response.getOutputStream());
					} else {
						//模板文件不存在  默认输出
						ExcelUtils.getInstance().exportObjects2Excel(result,ExportReportFluctuateCustDO.class,response.getOutputStream());
					}
				} catch (Exception e) {
					//e.printStackTrace();
					log.error(e.getMessage(), e);
				}
			}
		}

	// 下载excel模版
	@RequestMapping("/downTemplate")
	@MethodLock(key = "downTemplate")
	// 下载模版和导入共用同一个权限
	@RequiresPermissions("report:reportFluctuate:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<ReportFluctuateDO>(), ReportFluctuateDO.class,
					targetFile);

			// 写入response
			File downloadFile = new File(targetFile);
			FileUtil.downloadByResponse(response, downloadFile);
			downloadFile.delete();
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}

	
	@GetMapping("/add")
	@RequiresPermissions("report:reportFluctuate:add")
	String add() {
		return "feiniao/report/reportFluctuate/add";
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
	@GetMapping("/reportFluctuateSheng/{provinceid}/{startDate}/{endDate}/{showType}")
	@RequiresPermissions("report:reportFluctuate:reportFluctuateSheng")
	String ReportFluctuateSheng(@PathVariable("provinceid") String provinceid,
			@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate,
			@PathVariable("showType") String showType, Model model) {
		System.out.println("provinceid	:" + provinceid);
		ReportFluctuateDO reportFluctuate = new ReportFluctuateDO();
		reportFluctuate.setProvinceid(provinceid);
		reportFluctuate.setStartDate(startDate);
		reportFluctuate.setEndDate(endDate);
		reportFluctuate.setShowType(showType);
		model.addAttribute("reportFluctuate", reportFluctuate);
		return "feiniao/report/reportFluctuate/reportFluctuateSheng";
	}

	/**
	 * 点击市，获取对应市的数据
	 * 
	 * @param regionId
	 * @param provinceid
	 * @param cityid
	 * @param startDate
	 * @param endDate
	 * @param showType
	 * @param model
	 * @return
	 */
	@GetMapping("/reportFluctuateShi/{regionId}/{provinceid}/{cityid}/{startDate}/{endDate}/{showType}")
	@RequiresPermissions("report:reportFluctuate:reportFluctuateShi")
	String ReportFluctuateShi(@PathVariable("regionId") String regionId, @PathVariable("provinceid") String provinceid,
			@PathVariable("cityid") String cityid, @PathVariable("startDate") String startDate,
			@PathVariable("endDate") String endDate, @PathVariable("showType") String showType, Model model) {
		ReportFluctuateDO reportFluctuate = new ReportFluctuateDO();
		reportFluctuate.setRegionId(regionId);
		reportFluctuate.setProvinceid(provinceid);
		reportFluctuate.setCityid(cityid);
		reportFluctuate.setStartDate(startDate);
		reportFluctuate.setEndDate(endDate);
		reportFluctuate.setShowType(showType);
		model.addAttribute("reportFluctuate", reportFluctuate);
		return "feiniao/report/reportFluctuate/reportFluctuateShi";
	}

	@GetMapping("/reportFluctuateCust/{startDate}/{endDate}/{bdType}/{showType}")
	@RequiresPermissions("report:reportFluctuate:reportFluctuateCust")
	String ReportFluctuateCust(@PathVariable("startDate") String startDate, @PathVariable("endDate") String endDate,
			@PathVariable("bdType") String bdType, @PathVariable("showType") String showType, Model model) {
		ReportFluctuateDO reportFluctuate = new ReportFluctuateDO();
		reportFluctuate.setStartDate(startDate);
		reportFluctuate.setEndDate(endDate);
		reportFluctuate.setBdType(bdType);
		reportFluctuate.setShowType(showType);
		model.addAttribute("reportFluctuate", reportFluctuate);
		return "feiniao/report/reportFluctuate/reportFluctuateCust";
	}

	@GetMapping("/reportFluctuateCust/{startDate}/{endDate}/{regionId}/{bdType}/{showType}")
	@RequiresPermissions("report:reportFluctuate:reportFluctuateCust")
	String ReportFluctuateBigAreaCust(@PathVariable("startDate") String startDate,
			@PathVariable("endDate") String endDate, @PathVariable("regionId") String regionId,
			@PathVariable("bdType") String bdType, @PathVariable("showType") String showType, Model model) {
		ReportFluctuateDO reportFluctuate = new ReportFluctuateDO();
		reportFluctuate.setStartDate(startDate);
		reportFluctuate.setEndDate(endDate);
		reportFluctuate.setRegionId(regionId);
		reportFluctuate.setProvinceid(regionId);
		reportFluctuate.setCityid(regionId);
		reportFluctuate.setBranchCode(regionId);
        reportFluctuate.setBdType(bdType);
		reportFluctuate.setShowType(showType);
		model.addAttribute("reportFluctuate", reportFluctuate);
		return "feiniao/report/reportFluctuate/reportFluctuateCust";
	}









}
