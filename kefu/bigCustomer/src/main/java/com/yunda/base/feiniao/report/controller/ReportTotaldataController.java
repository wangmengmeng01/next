package com.yunda.base.feiniao.report.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.validation.FieldError;
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
import com.yunda.base.common.config.Constant;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.feiniao.report.bo.Bo_CustOdSumdata;
import com.yunda.base.feiniao.report.bo.Bo_ReportTotaldata;
import com.yunda.base.feiniao.report.domain.ExportCustBranchReportTotaldataDO;
import com.yunda.base.feiniao.report.domain.ExportCustReportTotaldataDO;
import com.yunda.base.feiniao.report.domain.ExportReportTotaldataDO;
import com.yunda.base.feiniao.report.domain.ReportTotaldataDO;
import com.yunda.base.feiniao.report.service.ReportTotaldataService;
import com.yunda.base.system.annotation.SensitiveOperateLog;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;

import io.swagger.annotations.ApiOperation;
/**
 * 客户报表订单统计-总表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-11 14:54:40
 */
 
@Controller
@RequestMapping("/report/reportTotaldata")
public class ReportTotaldataController extends BaseController{
	
    private static final Logger log = Logger.getLogger(ReportTotaldataController.class);

	/*@Value("${excel.targetFile.path}")
	private String target;
	
	@Value("${excel.template.path}")
	private String excelTemplate;*/	
	
	@Autowired
	private ReportTotaldataService reportTotaldataService;	
	@GetMapping()
	@RequiresPermissions("report:reportTotaldata:reportTotaldata")
	String ReportTotaldata(Model model,HttpServletRequest request){
		return "feiniao/report/reportTotaldata/reportTotaldata";
	}
	
	@ApiOperation(value = "获取总表")
	@ResponseBody
	@GetMapping("/list")
	@SensitiveOperateLog(value = "总表",type = "查询")
	@RequiresPermissions("report:reportTotaldata:reportTotaldata")
	@MethodLock(key = "request")
	public RspBean<PageUtils> list(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata ,BindingResult bindingResult,@RequestParam Map<String, Object> params){
		
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			//如果验参报错，返回空值给页面
			PageUtils pageUtils = new PageUtils(null, 0);
			return success(pageUtils);			
		}
		
		final UserDO loginUser = getUser(request);
		//查询列表数据
        //Query query = new Query(params);
		List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService.list(bo_ReportTotaldata,loginUser);
		//int total = reportTotaldataService.count(query);
		PageUtils pageUtils = new PageUtils(reportTotaldataList, 1);
		return success(pageUtils);
	}

	
	
	@ApiOperation(value = "查询客户所属信息表")
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
		//UserDO loginUser = getUser(request);
		//查询列表数据
        Query query = new Query(params);
		List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService.searchData(query);
		int total = reportTotaldataService.countSearchData(query);
		PageUtils pageUtils = new PageUtils(reportTotaldataList, total);
		return success(pageUtils);
	}
			
			
		// 客户导出excel
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
	        UserDO loginUser = getUser(request);

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
//					e.printStackTrace();
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
				//e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}	
		}	
		
		// 客户导出excel
				@RequestMapping("/exportCustBranchExcel")
				@MethodLock(key = "export15CustExcel")
				@SensitiveOperateLog(value = "总表",type = "导出")
				@RequiresPermissions("report:reportTotaldata:reportTotaldata")
				public void exportCustBranchExcel(HttpServletResponse response,HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata ,BindingResult bindingResult,@RequestParam Map<String, Object> params) {
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

					List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService.searchData(query);
					String targetFile = SysConfig.TARGET + "总表-客户表"+ DateUtils.formatHMS(new java.util.Date()) + ".xlsx";
					File downloadFile = new File(targetFile);

					try {
//							ExcelUtils.getInstance().exportObjects2Excel(result, ReportTotaldataDO.class, targetFile);
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
							ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, reportTotaldataService.filterCustBranchData(reportTotaldataList), data, ExportCustBranchReportTotaldataDO.class,
									true, targetFile);
						} else {
							// 模版文件不存在，默认输出
							ExcelUtils.getInstance().exportObjects2Excel(reportTotaldataService.filterCustBranchData(reportTotaldataList), ExportCustBranchReportTotaldataDO.class, targetFile);
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
//							e.printStackTrace();
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
						//e.printStackTrace();
						log.error(e.getMessage(), e);
					}
				}	
				}	
		
	
	@ApiOperation(value = "获取地图数据")
	@ResponseBody
	@GetMapping("/provinceMapData")
	@RequiresPermissions("report:reportTotaldata:reportTotaldata")
	public List<ReportTotaldataDO> provinceMapData(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata ,BindingResult bindingResult,@RequestParam Map<String, Object> params){
		
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			//如果验参报错，返回空值给页面
			PageUtils pageUtils = new PageUtils(null, 0);
			return null;			
		}
		UserDO loginUser = getUser(request);


		//查询列表数据
        Query query = new Query(params);
		List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService.queryProvinceMapReport(bo_ReportTotaldata,loginUser);
//		int total = reportTotaldataService.count(query);
//		PageUtils pageUtils = new PageUtils(reportTotaldataList, total);
		return reportTotaldataList;
	
	}
	
	@ApiOperation(value = "获取前100网点")
	@ResponseBody
	@GetMapping("/branchMapData")
	@RequiresPermissions("report:reportTotaldata:reportTotaldata")
	public List<ReportTotaldataDO> branchMapData(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata ,BindingResult bindingResult,@RequestParam Map<String, Object> params){
		
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			//如果验参报错，返回空值给页面
			PageUtils pageUtils = new PageUtils(null, 0);
			/*
			 * 
			 */
			return null;			
		}
		
		UserDO loginUser = getUser(request);

		//查询列表数据
        Query query = new Query(params);
		List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService.queryBranchMapReport(bo_ReportTotaldata,loginUser);
//		int total = reportTotaldataService.count(query);
//		PageUtils pageUtils = new PageUtils(reportTotaldataList, total);
		return reportTotaldataList;
	
	}
	
	
	@ApiOperation(value = "获取汇总数据")
	@ResponseBody
	@GetMapping("/totalMapData")
	@RequiresPermissions("report:reportTotaldata:reportTotaldata")
	public List<ReportTotaldataDO> totalMapData(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata ,BindingResult bindingResult,@RequestParam Map<String, Object> params){
		
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			//如果验参报错，返回空值给页面
			PageUtils pageUtils = new PageUtils(null, 0);
			/*
			 * 
			 */
			return null;			
		}
		UserDO loginUser = getUser(request);

		//查询列表数据
        Query query = new Query(params);
		List<ReportTotaldataDO> reportTotaldataList = reportTotaldataService.queryProvinceMapReport(bo_ReportTotaldata,loginUser);
//		int total = reportTotaldataService.count(query);
//		PageUtils pageUtils = new PageUtils(reportTotaldataList, total);
		return reportTotaldataList;
	
	}
	
	
//	var date_style  = $("#date_style").find("option:selected").attr("value");
//	var qu_date  = $('#qu_date').val();
//	var month_year  = $('#month_year').val();
//	var quarter_year  = $('#quarter_year').val();
//	var quarter_date  = $('#quarter_date').val();
//	var year  = $('#year').val();
//	var start_date  = $('#start_date').val();
//	var end_date  = $('#end_date').val();
//	var bigarea  = param.split(',')[0];
//	var provinceid  = param.split(',')[1];
//	var cityid  = param.split(',')[2];
	
	
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
//	content : prefix  + '/reportTotalCity/'+start_date+"/"+end_date+"/"+bigarea+"/"+provinceid// iframe的url

	@GetMapping("/reportTotalCity/{startDate}/{endDate}/{bigarea}/{provinceid}")
	@RequiresPermissions("report:reportTotal:reportTotalCity")
	String ReportFluctuateSheng(@PathVariable("startDate") String startDate
			,@PathVariable("endDate") String endDate,@PathVariable("bigarea") String bigarea
			,@PathVariable("provinceid") String provinceid,Model model){
		Bo_ReportTotaldata reportTotaldata = new Bo_ReportTotaldata();
//		reportTotaldata.setDate_style(dateStyle);
//		reportTotaldata.setQu_date(quDate);
//		reportTotaldata.setMonth_year(monthYear);
//		reportTotaldata.setQuarter_year(quarterYear);
//		reportTotaldata.setQuarter_date(quarterDate);
//		reportTotaldata.setYear(year);
		reportTotaldata.setStart_date(startDate);
		reportTotaldata.setEnd_date(endDate);
		reportTotaldata.setBig_area(bigarea);
		reportTotaldata.setProvince_id(provinceid);
//		reportTotaldata.setCity_id(cityid);

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
//	content : prefix  + '/reportTotalBranch/'+start_date+"/"+end_date+"/"+bigarea+"/"+provinceid+"/"+cityid
	@GetMapping("/reportTotalBranch/{start_date}/{end_date}/{bigarea}/{provinceid}/{cityid}")
	@RequiresPermissions("report:reportTotal:reportTotalBranch")
	String ReportTotalBranch(@PathVariable("start_date") String startDate
			,@PathVariable("end_date") String endDate,@PathVariable("bigarea") String bigarea
			,@PathVariable("provinceid") String provinceid,@PathVariable("cityid") String cityid,Model model){
		Bo_ReportTotaldata reportTotaldata = new Bo_ReportTotaldata();
//		reportTotaldata.setDate_style(dateStyle);
//		reportTotaldata.setQu_date(quDate);
//		reportTotaldata.setMonth_year(monthYear);
//		reportTotaldata.setQuarter_year(quarterYear);
//		reportTotaldata.setQuarter_date(quarterDate);
//		reportTotaldata.setYear(year);
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
	@RequestMapping("/exportExcel")
	@SensitiveOperateLog(value = "总表",type = "导出")
//	@MethodLock(key = "totalexportExcel")
	@RequiresPermissions("report:reportTotaldata:exportExcel")
	public void exportExcel(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportTotaldata bo_ReportTotaldata ,BindingResult bindingResult,HttpServletResponse response) {
		//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
			
		Map<String, Object> params = new HashMap<>(16);
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);
        BufferedInputStream bin = null;
        OutputStream out = null;

		//int nums = reportTotaldataService.count(query);

        UserDO loginUser = getUser(request);
		List<ReportTotaldataDO> result = reportTotaldataService.list(bo_ReportTotaldata,loginUser);
		String targetFile = SysConfig.TARGET + "总表"+ DateUtils.formatHMS(new java.util.Date()) + ".xlsx";
		File downloadFile = new File(targetFile);

		try {
//			ExcelUtils.getInstance().exportObjects2Excel(result, ReportTotaldataDO.class, targetFile);
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
			
			// 写入response
			/*try {
				InputStream myStream = new FileInputStream(targetFile);
				 bin = new BufferedInputStream(myStream);
		           out = response.getOutputStream();
		            int size = 0;
		            byte[] buf = new byte[1024];
		            while ((size = bin.read(buf)) != -1) {
		                out.write(buf, 0, size);
		            }
			
			} catch (IOException e) {
//				e.printStackTrace();
				log.error(e.getMessage(), e);
			}finally {
	            try {
	                bin.close();
	                out.close(); 
	            } catch (IOException e) {
	                log.warn(e.getMessage(),e);;
	            }
	        }*/			
		} catch (Exception e) {
//			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}
	}

	// 下载excel模版
	@RequestMapping("/downTemplate")
	@MethodLock(key = "downTemplate")
	// 下载模版和导入共用同一个权限
	@RequiresPermissions("report:reportTotaldata:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<ReportTotaldataDO>(), ReportTotaldataDO.class, targetFile);

			// 写入response
			File downloadFile = new File(targetFile);
			FileUtil.downloadByResponse(response, downloadFile);
			downloadFile.delete();
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}

	// 导入excel
	@ResponseBody
	@MethodLock(key = "importExcel")
	@RequestMapping(value = "/importExcel", consumes = "multipart/*", headers = "content-type=mutipart/form-data", method = RequestMethod.POST)
	@RequiresPermissions("report:reportTotaldata:importExcel")
	public R importExcel(MultipartFile file) {
		List<ReportTotaldataDO> list = null;

		String fileKey = UUID.randomUUID().toString();
		// 获取后缀
		String fileName = file.getOriginalFilename();
		if (fileName.lastIndexOf(".") != -1) {
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			String uploadFile = Constant.UPLOAD_PATH + File.separatorChar + fileKey + suffix;

			File _f = new File(uploadFile);
			if (!_f.getParentFile().exists()) {
				_f.getParentFile().mkdirs();
			}

			BufferedOutputStream out = null;
			try {
				out = new BufferedOutputStream(new FileOutputStream(_f));
				out.write(file.getBytes());
				out.flush();

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, ReportTotaldataDO.class, 0, 0);
			} catch (Exception e) {
				//e.printStackTrace();
				log.error(e.getMessage(), e);
			} finally {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}

		if (list != null) {
			for (ReportTotaldataDO _do : list) {
				reportTotaldataService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("report:reportTotaldata:add")
	String add(){
	    return "feiniao/report/reportTotaldata/add";
	}

	@GetMapping("/edit/{bigarea}")
	@RequiresPermissions("report:reportTotaldata:edit")
	String edit(@PathVariable("bigarea") String bigarea,Model model){
		ReportTotaldataDO reportTotaldata = reportTotaldataService.get(bigarea);
		model.addAttribute("reportTotaldata", reportTotaldata);
	    return "feiniao/report/reportTotaldata/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("report:reportTotaldata:add")
	public R save( ReportTotaldataDO reportTotaldata){
		if(reportTotaldataService.save(reportTotaldata)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("report:reportTotaldata:edit")
	public R update( ReportTotaldataDO reportTotaldata){
		reportTotaldataService.update(reportTotaldata);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("report:reportTotaldata:remove")
	public R remove( String bigarea){
		if(reportTotaldataService.remove(bigarea)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("report:reportTotaldata:batchRemove")
	public R remove(@RequestParam("ids[]") String[] bigareas){
		reportTotaldataService.batchRemove(bigareas);
		return R.ok();
	}
	
}
