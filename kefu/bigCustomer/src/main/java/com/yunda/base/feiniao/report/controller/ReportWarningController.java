package com.yunda.base.feiniao.report.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
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
import com.yunda.base.common.utils.R;
import com.yunda.base.feiniao.report.bo.Bo_ReportWarning;
import com.yunda.base.feiniao.report.domain.ExportReportWarningBranchDO;
import com.yunda.base.feiniao.report.domain.ExportReportWarningdataDO;
import com.yunda.base.feiniao.report.domain.ReportWarningDO;
import com.yunda.base.feiniao.report.service.ReportWarningService;
import com.yunda.base.system.annotation.SensitiveOperateLog;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;

import io.swagger.annotations.ApiOperation;
/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-16 10:45:24
 */
 
@Controller
@RequestMapping("/report/reportWarning")
public class ReportWarningController extends BaseController{
	Logger log = Logger.getLogger(getClass());
	
	/*改用SysConfig界面喂参的方式   灵活
	@Value("${excel.targetFile.path}")
	private String target;	
	@Value("${excel.template.path}")
	private String excelTemplate;*/
	
	@Autowired
	private ReportWarningService reportWarningService;
	
	//跳转到主页面
	@GetMapping()
	@RequiresPermissions("report:reportWarning:reportWarning")
	String ReportWarning(){
	    return "feiniao/report/reportWarning/reportWarning";
	}
	//跳转到省份预警表
	@GetMapping("/reportWarningSheng/{bigarea}/{provinceId}/{startDate}/{endDate}/{showType}")
	@RequiresPermissions("report:reportWarning:reportWarningSheng")
	String ReportWarningSheng(@PathVariable("bigarea") String bigarea,@PathVariable("provinceId") String provinceId,
			@PathVariable("startDate") String startDate,@PathVariable("endDate") String endDate,@PathVariable("showType") String showType,Model model){
		//System.out.println("bigarea	:"+ bigarea);
		//System.out.println("provinceId	:"+ provinceId);
		ReportWarningDO reportWarningDO = new ReportWarningDO();
		reportWarningDO.setBigarea(bigarea);
		reportWarningDO.setProvinceId(provinceId);
		reportWarningDO.setStartDate(startDate);
		reportWarningDO.setEndDate(endDate);
		reportWarningDO.setShowType(showType);
		model.addAttribute("reportWarningDO", reportWarningDO);
	    return "feiniao/report/reportWarning/reportWarningSheng";
	}
	//跳转到城市预警表
	@GetMapping("/reportWarningCity/{bigarea}/{provinceId}/{cityId}/{startDate}/{endDate}/{showType}")
	@RequiresPermissions("report:reportWarning:reportWarningCity")
	String ReportWarningCity(@PathVariable("bigarea") String bigarea,@PathVariable("provinceId") String provinceId,@PathVariable("cityId") String cityId,
			@PathVariable("startDate") String startDate,@PathVariable("endDate") String endDate,@PathVariable("showType") String showType,Model model){
		
		ReportWarningDO reportWarningDO = new ReportWarningDO();
		reportWarningDO.setBigarea(bigarea);
		reportWarningDO.setProvinceId(provinceId);
		reportWarningDO.setCityId(cityId);
		reportWarningDO.setStartDate(startDate);
		reportWarningDO.setEndDate(endDate);
		reportWarningDO.setShowType(showType);
		model.addAttribute("reportWarningDO", reportWarningDO);
	    return "feiniao/report/reportWarning/reportWarningCity";
	}
	
	//点击网点  跳转到客户预警表
	@GetMapping("/reportWarningBranch/{branchCode}/{startDate}/{endDate}/{showType}")
	@RequiresPermissions("report:reportWarning:reportWarningBranch")
	String ReportWarningBranch(@PathVariable("branchCode") Integer branchCode,
			@PathVariable("startDate") String startDate,@PathVariable("endDate") String endDate,@PathVariable("showType") String showType,Model model){
		
		ReportWarningDO reportWarningDO = new ReportWarningDO();
		//reportWarningDO.setBigarea(bigarea);
		//reportWarningDO.setProvinceId(provinceId);
		reportWarningDO.setBranchCode(branchCode);
		reportWarningDO.setStartDate(startDate);
		reportWarningDO.setEndDate(endDate);
		reportWarningDO.setShowType(showType);
		model.addAttribute("reportWarningDO", reportWarningDO);
	    return "feiniao/report/reportWarning/reportWarningBranch";
	}
	
	//点击网点  跳转到客户预警表    custpronumber   集团     省
	@GetMapping("/reportWarningNumberLevel/{provinceId}/{numberLevel}/{startDate}/{endDate}/{showType}")
	@RequiresPermissions("report:reportWarning:reportWarningBranch")
	String ReportWarningNumberLevel(@PathVariable("provinceId") String provinceId,@PathVariable("numberLevel") String numberLevel,
			@PathVariable("startDate") String startDate,@PathVariable("endDate") String endDate,@PathVariable("showType") String showType,Model model){
		
		ReportWarningDO reportWarningDO = new ReportWarningDO();
		//reportWarningDO.setBigarea(bigarea);
		//reportWarningDO.setProvinceId(provinceId);
		reportWarningDO.setProvinceId(provinceId);
		reportWarningDO.setPriceLevel(numberLevel);
		reportWarningDO.setStartDate(startDate);
		reportWarningDO.setEndDate(endDate);
		reportWarningDO.setShowType(showType);
		model.addAttribute("reportWarningDO", reportWarningDO);
	    return "feiniao/report/reportWarning/reportWarningBranch";
	}

	//点击网点  跳转到客户预警表   custcitnumber     城市
	@GetMapping("/reportWarningNumberLevelcity/{cityId}/{numberLevel}/{startDate}/{endDate}/{showType}")
	@RequiresPermissions("report:reportWarning:reportWarningBranch")
	String ReportWarningNumberLevelcity(@PathVariable("cityId") String cityId,@PathVariable("numberLevel") String numberLevel,
			@PathVariable("startDate") String startDate,@PathVariable("endDate") String endDate,@PathVariable("showType") String showType,Model model){
		
		ReportWarningDO reportWarningDO = new ReportWarningDO();
		//reportWarningDO.setBigarea(bigarea);
		//reportWarningDO.setProvinceId(provinceId);
		reportWarningDO.setCityId(cityId);
		reportWarningDO.setPriceLevel(numberLevel);
		reportWarningDO.setStartDate(startDate);
		reportWarningDO.setEndDate(endDate);
		reportWarningDO.setShowType(showType);
		model.addAttribute("reportWarningDO", reportWarningDO);
	    return "feiniao/report/reportWarning/reportWarningBranch";
	}
	//点击网点  跳转到客户预警表   custcitnumber     网点
	@GetMapping("/reportWarningNumberLevelBranch/{branchCode}/{numberLevel}/{startDate}/{endDate}/{showType}")
	@RequiresPermissions("report:reportWarning:reportWarningBranch")
	String ReportWarningNumberLevelBranch(@PathVariable("branchCode") Integer branchCode,@PathVariable("numberLevel") String numberLevel,
			@PathVariable("startDate") String startDate,@PathVariable("endDate") String endDate,@PathVariable("showType") String showType,Model model){
		//System.out.println("bigarea	:"+ bigarea);
		//System.out.println("provinceId	:"+ provinceId);
		ReportWarningDO reportWarningDO = new ReportWarningDO();
		//reportWarningDO.setBigarea(bigarea);
		//reportWarningDO.setProvinceId(provinceId);
		reportWarningDO.setBranchCode(branchCode);
		reportWarningDO.setPriceLevel(numberLevel);
		reportWarningDO.setStartDate(startDate);
		reportWarningDO.setEndDate(endDate);
		reportWarningDO.setShowType(showType);
		model.addAttribute("reportWarningDO", reportWarningDO);
	    return "feiniao/report/reportWarning/reportWarningBranch";
	}

	@ApiOperation(value = "获取预警表")
	@ResponseBody
	@GetMapping("/list")
	@SensitiveOperateLog(value = "预警表",type = "查询")
	@RequiresPermissions("report:reportWarning:reportWarning")
	@MethodLock(key = "request")
	public RspBean<PageUtils> list(HttpServletRequest request, HttpServletResponse response,@ModelAttribute @Validated Bo_ReportWarning boReportWarning,BindingResult bindingResult){
		
		if (bindingResult.hasErrors()) {
			notifyPage(bindingResult.getFieldError().getDefaultMessage());
			// 如果验参报错，返回空值給頁面
			return success(new PageUtils());
		}
		UserDO loginUser = getUser(request);
		
		List<ReportWarningDO> reportWarningList = reportWarningService.list(boReportWarning,loginUser);
		
		//String search_month = DateUtils.format(_startDate, "yyyyMM");
		ReportWarningDO reportWarningDO = new ReportWarningDO();
		reportWarningDO.setBranchCode(boReportWarning.getBranchCode());
		reportWarningDO.setProvinceId(boReportWarning.getProvinceId());
		reportWarningDO.setCityId(boReportWarning.getCityId());
		//reportWarningDO.setSearch_month(search_month);
		//reportWarningDO.setSearch_week(searchWeek);//预警周基础表数据标识字段
		reportWarningDO.setPriceLevel(boReportWarning.getPriceLevel());
		reportWarningDO.setStartDate(boReportWarning.getStartDate());
		reportWarningDO.setEndDate(boReportWarning.getEndDate());
		reportWarningDO.setShowType(boReportWarning.getShowType());
		
		int total = reportWarningService.count(reportWarningDO);
		//int total = 0;
		PageUtils pageUtils = new PageUtils(reportWarningList, total);
		return success(pageUtils);
	}


	// 导出excel
	@RequestMapping("/exportExcel")
	@SensitiveOperateLog(value = "预警表",type = "导出")
	@MethodLock(key = "exportWarningExcel")
	@RequiresPermissions("report:reportWarning:exportExcel")
	public void exportExcel(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportWarning boReportWarning ,BindingResult bindingResult,HttpServletResponse response) {
	//内存溢出问题https://blog.csdn.net/xishanxinyue/article/details/15336551	
	//导出功能是否开放  true表示开放
	if (SysConfig.DAOCHU.equals("false")) {
		return;
	} else if (SysConfig.DAOCHU.equals("true")) {
		BufferedInputStream bin = null;
        OutputStream out = null;
		//Map<String, Object> params = new HashMap<>(16);
		/**params.put("offset", "0");
		params.put("limit", "10000");// 上限保护
		Query query = new Query(params);*/
		
		UserDO loginUser = getUser(request);
		
		List<ReportWarningDO> reportWarningList = reportWarningService.list(boReportWarning,loginUser);
		String targetFile = SysConfig.TARGET + "warning"+ DateUtils.format(new Date()) + ".xlsx";
		File downloadFile = new File(targetFile);

		try {
			//ExcelUtils.getInstance().exportObjects2Excel(result, ReportTotaldataDO.class, targetFile);
			// 按命名规则找模版文件
			File file = new File(SysConfig.TEMPLATE+"warning.xlsx");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			
			// set headers for the response
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
			response.setHeader(headerKey, headerValue);
			
			List<ExportReportWarningdataDO> result = reportWarningService.filterData(reportWarningList);
			if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
				//前端界面喂参  控制是否使用模板
				Map<String, String> data = new HashMap<>();
				// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
				ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, 
						result,data, ExportReportWarningdataDO.class, false, response.getOutputStream());
				
			} else {
				//模板文件不存在  默认输出
				ExcelUtils.getInstance().exportObjects2Excel(result,ExportReportWarningdataDO.class,response.getOutputStream());
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		}
	}
	// 导出excel
	@RequestMapping("/exportBranchExcel")
	@SensitiveOperateLog(value = "预警表-客户",type = "导出")
	@MethodLock(key = "exportBranchExcel")
	@RequiresPermissions("report:reportWarning:exportExcel")
	public void exportBranchExcel(HttpServletRequest request,@ModelAttribute @Validated Bo_ReportWarning boReportWarning ,BindingResult bindingResult,HttpServletResponse response) {
	//内存溢出问题https://blog.csdn.net/xishanxinyue/article/details/15336551	
	//导出功能是否开放  true表示开放
	if (SysConfig.DAOCHU.equals("false")) {
		return;
	} else if (SysConfig.DAOCHU.equals("true")) {
		BufferedInputStream bin = null;
        OutputStream out = null;
        //该客户层级的数据  是经过一堆条件筛选的  故数据量不大  仅集团合计的B类客户可能过万   上限保护
        boReportWarning.setLimit(20000);
        boReportWarning.setOffset(0);
		UserDO loginUser = getUser(request);
		
		List<ReportWarningDO> reportWarningList = reportWarningService.list(boReportWarning,loginUser);
		String targetFile = SysConfig.TARGET + "warning"+ DateUtils.format(new Date()) + ".xlsx";
		File downloadFile = new File(targetFile);

		try {
			//ExcelUtils.getInstance().exportObjects2Excel(result, ReportTotaldataDO.class, targetFile);
			// 按命名规则找模版文件
			/*File file = new File(SysConfig.TEMPLATE+"warning.xlsx");
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			
			// set headers for the response
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
			response.setHeader(headerKey, headerValue);
			*/
			List<ExportReportWarningBranchDO> result = reportWarningService.filterBranchData(reportWarningList);
			/*if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
				//前端界面喂参  控制是否使用模板
				Map<String, String> data = new HashMap<>();
				// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
				ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, 
						result,data, ExportReportWarningdataDO.class, false, response.getOutputStream());
				
			} else {*/
				//模板文件不存在  默认输出
				ExcelUtils.getInstance().exportObjects2Excel(result,ExportReportWarningBranchDO.class,response.getOutputStream());
			//}
			
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
	@RequiresPermissions("report:reportWarning:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<ReportWarningDO>(), ReportWarningDO.class, targetFile);

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
	@RequiresPermissions("report:reportWarning:importExcel")
	public R importExcel(MultipartFile file) {
		List<ReportWarningDO> list = null;

		String fileKey = UUID.randomUUID().toString();
		// 获取后缀
		String fileName = file.getOriginalFilename();
		if (fileName.lastIndexOf(".") != -1) {
			String suffix = fileName.substring(fileName.lastIndexOf("."));
			String uploadFile = SysConfig.uploadPath + fileKey + suffix;
			//String uploadFile = Constant.UPLOAD_PATH + File.separatorChar + fileKey + suffix;

			File _f = new File(uploadFile);
			if (!_f.getParentFile().exists()) {
				_f.getParentFile().mkdirs();
			}

			BufferedOutputStream out = null;
			try {
				out = new BufferedOutputStream(new FileOutputStream(_f));
				out.write(file.getBytes());
				out.flush();

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, ReportWarningDO.class, 0, 0);
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
			for (ReportWarningDO _do : list) {
				reportWarningService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("report:reportWarning:add")
	String add(){
	    return "feiniao/report/reportWarning/add";
	}

	@GetMapping("/edit/{bigarea}")
	@RequiresPermissions("report:reportWarning:edit")
	String edit(@PathVariable("bigarea") String bigarea,Model model){
		ReportWarningDO reportWarning = reportWarningService.get(bigarea);
		model.addAttribute("reportWarning", reportWarning);
	    return "feiniao/report/reportWarning/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("report:reportWarning:add")
	public R save( ReportWarningDO reportWarning){
		if(reportWarningService.save(reportWarning)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("report:reportWarning:edit")
	public R update( ReportWarningDO reportWarning){
		reportWarningService.update(reportWarning);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("report:reportWarning:remove")
	public R remove( String bigarea){
		if(reportWarningService.remove(bigarea)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("report:reportWarning:batchRemove")
	public R remove(@RequestParam("ids[]") String[] bigareas){
		reportWarningService.batchRemove(bigareas);
		return R.ok();
	}
	
}
