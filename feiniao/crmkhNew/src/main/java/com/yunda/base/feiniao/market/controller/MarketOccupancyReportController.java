package com.yunda.base.feiniao.market.controller;

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

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
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
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.feiniao.market.bo.Bo_marketKeyProvince;
import com.yunda.base.feiniao.market.bo.Bo_marketOccupancyReport;
import com.yunda.base.feiniao.market.domain.ExportMarketKeyProvinceReportDO;
import com.yunda.base.feiniao.market.domain.ExportMarketOccupancyReportDO;
import com.yunda.base.feiniao.market.domain.MarketKeyProvinceReportDO;
import com.yunda.base.feiniao.market.domain.MarketOccupancyReportDO;
import com.yunda.base.feiniao.market.service.MarketOccupancyReportService;
import com.yunda.base.feiniao.report.bo.Bo_ReportTotaldata;
import com.yunda.base.system.annotation.SensitiveOperateLog;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.FileUtil;
/**
 * 市场占有率数据上报
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-10-12103231
 */
 
@Controller
@RequestMapping("/market/marketOccupancyReport")
public class MarketOccupancyReportController extends BaseController{
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private MarketOccupancyReportService marketOccupancyReportService;
	
	@GetMapping()
	@RequiresPermissions("market:marketOccupancyReport:marketOccupancyReport")
	String MarketOccupancyReport(){
	    return "feiniao/market/marketOccupancyReport/marketOccupancyReport";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@SensitiveOperateLog(value = "市场占有率数据上报表",type = "查询")
	@RequiresPermissions("market:marketOccupancyReport:marketOccupancyReport")
	public PageUtils list(HttpServletRequest request,@ModelAttribute @Validated Bo_marketOccupancyReport boMarketOccupancyReport ,BindingResult bindingResult){
		final UserDO loginUser = getUser(request);
/*		
        Query query = new Query(params);
        String reortDate = query.get("date")+"";
        if(StringUtils.isNoneBlank(reortDate)){
        	String[] report = reortDate.split("-");
        	if(report.length>1){
        		query.put("reportNian", report[0]);
        		query.put("reportYue", report[1]);
        	}
        }*/
		//查询列表数据
		List<MarketOccupancyReportDO> marketOccupancyReportList = marketOccupancyReportService.list(boMarketOccupancyReport,loginUser);
		int total = marketOccupancyReportService.count(boMarketOccupancyReport,loginUser);
		for(MarketOccupancyReportDO report : marketOccupancyReportList){
			if(loginUser.isProvinceqx()){
				report.setType("1");
			}else{
				report.setType("2");
			}
		}
		PageUtils pageUtils = new PageUtils(marketOccupancyReportList, total);
		return pageUtils;
	}
	
	
	/**
	 * 查看、编辑、审核、上报获取某条数据
	 * @param params
	 * @return
	 */
	@ResponseBody
	@GetMapping("/listSearch")
	@RequiresPermissions("market:marketOccupancyReport:marketOccupancyReport")
	public PageUtils listSearch(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
        String reportDate = query.get("report")+"";
        String[] ny = reportDate.split("-");
        query.put("year", ny[0]);
        query.put("month", ny[1]);

		List<MarketOccupancyReportDO> marketOccupancyReportList = marketOccupancyReportService.listSearch(query);
		int total = marketOccupancyReportService.countSearch(query);
		PageUtils pageUtils = new PageUtils(marketOccupancyReportList, total);
		return pageUtils;
	}
	
	/**
	 * 暂存数据
	 * 每一条（行）数据符合规则就保存到缓存
	 */
	@ResponseBody
	@RequestMapping("/cacheSave")
	@RequiresPermissions("market:marketOccupancyReport:marketOccupancyReport")
	public R cacheSave(HttpServletRequest request, MarketOccupancyReportDO occupancyReport) {
		UserDO loginUser = getUser(request);
		marketOccupancyReportService.cacheSave(request, occupancyReport,loginUser);
		return R.ok();
	}
	
	
	/**
	 * 修改  点击确认
	 * @param request
	 * @param occupancyReport
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/upData")
	@RequiresPermissions("market:marketOccupancyReport:doEdit")
	public R  upData(HttpServletRequest request,MarketOccupancyReportDO occupancyReport){
		UserDO loginUser = getUser(request);
		HashMap<String,Object> map = new HashMap<>();
        String reportDate = occupancyReport.getReportDate();
        String[] ny = reportDate.split("-");
        map.put("year", ny[0]);
        map.put("month", ny[1]);
        map.put("provinceID",occupancyReport.getProvinceid());
		int count = marketOccupancyReportService.checkResult(map);
		
		if(count == 0){
			marketOccupancyReportService.upDataResult(request,occupancyReport,loginUser);
		}
		else{
			R.error();
		}
		return R.ok();
	
	}
	
	/*
	 *确认审核  market:marketOccupancyReport:auditReport需要此权限
	 */
	@ResponseBody
	@RequestMapping(value="/auditData")
	@RequiresPermissions("market:marketOccupancyReport:auditReport")
	public R  auditData(HttpServletRequest request,MarketOccupancyReportDO occupancyReport){	
		String result = marketOccupancyReportService.auditData(occupancyReport);
		if(StringUtils.isBlank(result)){
			return R.ok();
		}
			return R.error();
	}	
	
		/**
		 * 
		 * 查询省份信息前端查询条件。
		 * 
		 * {@link Map}
		 * @param request
		 * @param params
		 * @return
		 * @author bianxiaolong
		 * @since 1.0.0_2018年9月20日
		 */
		@ResponseBody
		@PostMapping("/searchProvinceData")
		@RequiresPermissions("market:marketOccupancyReport:marketOccupancyReport")
		public String searchCustomerData(HttpServletRequest request,@RequestParam Map<String, Object> params){
	        List<Map<String, Object>> costreportOrderCostFinishList = marketOccupancyReportService.searchShengData();
			String jsonStr = JSONArray.fromObject(costreportOrderCostFinishList).toString();
			return jsonStr;
		}

	/*// 占有率导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@SensitiveOperateLog(value = "市场占有率数据上报表",type = "导出")
	@RequiresPermissions("market:marketOccupancyReport:exportExcel")
	public void exportExcel(HttpServletResponse response,HttpServletRequest request,@ModelAttribute @Validated Bo_marketOccupancyReport boMarketOccupancyReport ,BindingResult bindingResult) {
        BufferedInputStream bin = null;
        OutputStream out = null;
        UserDO loginUser = getUser(request);
		//查询列表数据
		List<MarketOccupancyReportDO> marketOccupancyReportList = marketOccupancyReportService.list(boMarketOccupancyReport,loginUser);
		String targetFile = SysConfig.TARGET + "市场占有率数据上报"+ DateUtils.formatHMS(new java.util.Date()) + ".xlsx";
		File downloadFile = new File(targetFile);

		try {
//				ExcelUtils.getInstance().exportObjects2Excel(result, ReportTotaldataDO.class, targetFile);
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
				ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, marketOccupancyReportService.filterData(marketOccupancyReportList), data, ExportMarketOccupancyReportDO.class,
						true, targetFile);
			} else {
				// 模版文件不存在，默认输出
				ExcelUtils.getInstance().exportObjects2Excel(marketOccupancyReportService.filterData(marketOccupancyReportList), ExportMarketOccupancyReportDO.class, targetFile);
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
//				e.printStackTrace();
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
	}	*/
	
	
	// 导出excel
		@RequestMapping("/exportExcel")
		@MethodLock(key = "exportExcel")
		@SensitiveOperateLog(value = "市场占有率数据上报表",type = "导出")
		@RequiresPermissions("market:marketOccupancyReport:exportExcel")
		public void exportExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute @Validated Bo_marketOccupancyReport boMarketOccupancyReport, BindingResult bindingResult) {
			if (bindingResult.hasErrors()) {
				FieldError fieldError = bindingResult.getFieldError();

				notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
				// 如果验参报错，不忘下走
				return;
			}
			//导出功能是否开放  true表示开放
			if (SysConfig.DAOCHU.equals("false")) {
				return;
			} else if (SysConfig.DAOCHU.equals("true")) {
				UserDO loginUser = getUser(request);
/*				if(StringUtils.isNoneBlank(boMarketKeyProvince.getSearchDate())){
		        	String[] report = boMarketKeyProvince.getSearchDate().split("-");
		        	if(report.length>1){
		        		boMarketKeyProvince.setMonthYear(report[0]);
		        		boMarketKeyProvince.setMonthDate(report[1]);
		        	}
			    }*/
				// 查询列表数据
				List<MarketOccupancyReportDO> marketOccupancyReportList = marketOccupancyReportService.list(boMarketOccupancyReport,loginUser);
				String targetFile = SysConfig.TARGET + "demo"+ DateUtils.format(new java.util.Date()) + ".xlsx";
				File downloadFile = new File(targetFile);
				
				try {
					// 按命名规则找模版文件
					File file = new File(SysConfig.TEMPLATE+"demo.xlsx");
					response.setContentType("application/vnd.ms-excel;charset=utf-8");
					response.setCharacterEncoding("utf-8");
					
					// set headers for the response
					String headerKey = "Content-Disposition";
					String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
					response.setHeader(headerKey, headerValue);
					
					List<ExportMarketOccupancyReportDO> result = marketOccupancyReportService.filterData(marketOccupancyReportList);
					if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
						Map<String, String> data = new HashMap<>();
						// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
						ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0,
								result,data, ExportMarketOccupancyReportDO.class, false, response.getOutputStream());
						
					} else {
						//模板文件不存在  默认输出
						ExcelUtils.getInstance().exportObjects2Excel(result,ExportMarketOccupancyReportDO.class,response.getOutputStream());
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
	@RequiresPermissions("market:marketOccupancyReport:importExcel")
	public void downTemplate(HttpServletResponse response) {
		String targetFile = SysConfig.tempDownload + UUID.randomUUID().toString() + ".xlsx";
		try {
			ExcelUtils.getInstance().exportObjects2Excel(new ArrayList<MarketOccupancyReportDO>(), MarketOccupancyReportDO.class, targetFile);

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
	@RequiresPermissions("market:marketOccupancyReport:importExcel")
	public R importExcel(MultipartFile file) {
		List<MarketOccupancyReportDO> list = null;

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

				list = ExcelUtils.getInstance().readExcel2Objects(uploadFile, MarketOccupancyReportDO.class, 0, 0);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				//e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}

		if (list != null) {
			for (MarketOccupancyReportDO _do : list) {
				marketOccupancyReportService.save(_do);
			}
		}

		return R.ok();
	}		
	
	@GetMapping("/add")
	@RequiresPermissions("market:marketOccupancyReport:add")
	String add(){
	    return "feiniao/market/marketOccupancyReport/add";
	}

	@GetMapping("/edit/{recordId}")
	@RequiresPermissions("market:marketOccupancyReport:edit")
	String edit(@PathVariable("recordId") Integer recordId,Model model){
		MarketOccupancyReportDO marketOccupancyReport = marketOccupancyReportService.get(recordId);
		model.addAttribute("marketOccupancyReport", marketOccupancyReport);
	    return "feiniao/market/marketOccupancyReport/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("market:marketOccupancyReport:add")
	public R save( MarketOccupancyReportDO marketOccupancyReport){
		if(marketOccupancyReportService.save(marketOccupancyReport)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("market:marketOccupancyReport:edit")
	public R update( MarketOccupancyReportDO marketOccupancyReport){
		marketOccupancyReportService.update(marketOccupancyReport);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("market:marketOccupancyReport:remove")
	public R remove( Integer recordId){
		if(marketOccupancyReportService.remove(recordId)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("market:marketOccupancyReport:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] recordIds){
		marketOccupancyReportService.batchRemove(recordIds);
		return R.ok();
	}
	
	/**
	 * 上报   总部无此权限
	 * @param provinceid
	 * @param reportDate
	 * @param type
	 * @param model
	 * @return
	 */
	@GetMapping("/openShangBao/{provinceid}/{reportDate}/{type}")
	@RequiresPermissions("market:marketOccupancyReport:doReport")
	public String openShangBao(@PathVariable("provinceid") String provinceid
			,@PathVariable("reportDate") String reportDate,@PathVariable("type") String type,Model model){
		MarketOccupancyReportDO marketOccupancyReportDO = new MarketOccupancyReportDO();
		marketOccupancyReportDO.setProvinceid(provinceid);
		marketOccupancyReportDO.setReportDate(reportDate);
		marketOccupancyReportDO.setReportStatus(type);
		model.addAttribute("marketOccupancyReportDO", marketOccupancyReportDO);
	    return "feiniao/market/marketOccupancyReport/searchReport";
	}
	
	/**
	 * 审核   网点无此权限
	 * @param provinceid
	 * @param reportDate
	 * @param type
	 * @param model
	 * @return
	 */
	@GetMapping("/openShangBaoShengHe/{provinceid}/{reportDate}/{type}")
	@RequiresPermissions("market:marketOccupancyReport:auditReport")
	public String openShangBaoShenHe(@PathVariable("provinceid") String provinceid
			,@PathVariable("reportDate") String reportDate,@PathVariable("type") String type,Model model){
		MarketOccupancyReportDO marketOccupancyReportDO = new MarketOccupancyReportDO();
		marketOccupancyReportDO.setProvinceid(provinceid);
		marketOccupancyReportDO.setReportDate(reportDate);
		marketOccupancyReportDO.setReportStatus(type);
		model.addAttribute("marketOccupancyReportDO", marketOccupancyReportDO);
	    return "feiniao/market/marketOccupancyReport/searchReport";
	}
	
	/**
	 * 修改   总部无此权限
	 * @param provinceid
	 * @param reportDate
	 * @param type
	 * @param model
	 * @return
	 */
	@GetMapping("/openShangBaoXiuGai/{provinceid}/{reportDate}/{type}")
	@RequiresPermissions("market:marketOccupancyReport:doEdit")
	public String openShangBaoXiuGai(@PathVariable("provinceid") String provinceid
			,@PathVariable("reportDate") String reportDate,@PathVariable("type") String type,Model model){
		MarketOccupancyReportDO marketOccupancyReportDO = new MarketOccupancyReportDO();
		marketOccupancyReportDO.setProvinceid(provinceid);
		marketOccupancyReportDO.setReportDate(reportDate);
		marketOccupancyReportDO.setReportStatus(type);
		model.addAttribute("marketOccupancyReportDO", marketOccupancyReportDO);
	    return "feiniao/market/marketOccupancyReport/searchReport";
	}
	
	
	/**
	 * 查看   总部、网点都有此权限
	 * @param provinceid
	 * @param reportDate
	 * @param type
	 * @param model
	 * @return
	 */
	@GetMapping("/openShangBaoSee/{provinceid}/{reportDate}/{type}")
	@RequiresPermissions("market:marketOccupancyReport:seeReport")
	public String openShangBaoSee(@PathVariable("provinceid") String provinceid
			,@PathVariable("reportDate") String reportDate,@PathVariable("type") String type,Model model){
		MarketOccupancyReportDO marketOccupancyReportDO = new MarketOccupancyReportDO();
		marketOccupancyReportDO.setProvinceid(provinceid);
		marketOccupancyReportDO.setReportDate(reportDate);
		marketOccupancyReportDO.setReportStatus(type);
		model.addAttribute("marketOccupancyReportDO", marketOccupancyReportDO);
	    return "feiniao/market/marketOccupancyReport/searchReport";
	}
	
}



