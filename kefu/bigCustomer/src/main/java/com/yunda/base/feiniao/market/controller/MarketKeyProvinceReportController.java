package com.yunda.base.feiniao.market.controller;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.crab2died.ExcelUtils;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.feiniao.market.bo.Bo_marketKeyProvince;
import com.yunda.base.feiniao.market.domain.ExportMarketKeyProvinceReportDO;
import com.yunda.base.feiniao.market.domain.MarketKeyProvinceReportDO;
import com.yunda.base.feiniao.market.service.MarketKeyProvinceReportService;
import com.yunda.base.system.annotation.SensitiveOperateLog;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.UserDO;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
/**
 * 省同行间市场份额分析对比表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-10-09105817
 */
 
@Controller
@RequestMapping("/market/marketKeyProvinceReport")
public class MarketKeyProvinceReportController extends BaseController {
	Logger log = Logger.getLogger(getClass());
	
	@Value("${excel.targetFile.path}")
	private String target;
	
	@Value("${excel.template.path}")
	private String excelTemplate;
	
	@Autowired
	private MarketKeyProvinceReportService marketKeyProvinceReportService;
	
	@GetMapping()
	@RequiresPermissions("market:marketKeyProvinceReport:marketKeyProvinceReport")
	String MarketKeyProvinceReport(){
	    return "feiniao/market/marketKeyProvinceReport/marketKeyProvinceReport";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@SensitiveOperateLog(value = "省同行间市场份额分析对比表",type = "查询")
	@RequiresPermissions("market:marketKeyProvinceReport:marketKeyProvinceReport")
	public PageUtils list(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute @Validated Bo_marketKeyProvince boMarketKeyProvince, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();

			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			// 如果验参报错，返回空值給頁面
			PageUtils pageUtils = new PageUtils(null, 0);
			return pageUtils;
		}
		if(StringUtils.isNoneBlank(boMarketKeyProvince.getSearchDate())){
        	String[] report = boMarketKeyProvince.getSearchDate().split("-");
        	if(report.length>1){
        		boMarketKeyProvince.setMonthYear(report[0]);
        		boMarketKeyProvince.setMonthDate(report[1]);
        	}
	    }
		final UserDO loginUser = getUser(request);
		/*Object obj = request.getSession().getAttribute(CRM_constants.AUTH_USER);
		UserDO loginUser =new UserDO();
		if(obj == null){
			
		}else{
			loginUser = (UserDO)obj;
		}*/
		//查询列表数据
		List<MarketKeyProvinceReportDO> marketKeyProvinceReportList = marketKeyProvinceReportService.list(boMarketKeyProvince,loginUser);
		int total = 0;
		if(marketKeyProvinceReportList != null && marketKeyProvinceReportList.size() > 0){
			total = marketKeyProvinceReportList.size();
		}
		PageUtils pageUtils = new PageUtils(marketKeyProvinceReportList, total);
		return pageUtils;
	}

	// 导出excel
	@RequestMapping("/exportExcel")
	@MethodLock(key = "exportExcel")
	@SensitiveOperateLog(value = "省同行间市场份额分析对比表",type = "导出")
	@RequiresPermissions("market:marketKeyProvinceReport:exportExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response,@ModelAttribute @Validated Bo_marketKeyProvince boMarketKeyProvince, BindingResult bindingResult) {
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
			if(StringUtils.isNoneBlank(boMarketKeyProvince.getSearchDate())){
	        	String[] report = boMarketKeyProvince.getSearchDate().split("-");
	        	if(report.length>1){
	        		boMarketKeyProvince.setMonthYear(report[0]);
	        		boMarketKeyProvince.setMonthDate(report[1]);
	        	}
		    }
			// 查询列表数据
			List<MarketKeyProvinceReportDO> marketKeyProvinceReportList =  marketKeyProvinceReportService.list(boMarketKeyProvince,loginUser);
			String targetFile = SysConfig.TARGET + "marketKeyProvinceReport"+ DateUtils.format(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
			
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"marketKeyProvinceReport.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
				
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
				
				List<ExportMarketKeyProvinceReportDO> result = marketKeyProvinceReportService.filterData(marketKeyProvinceReportList);
				if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0,
							result,data, ExportMarketKeyProvinceReportDO.class, false, response.getOutputStream());
					
				} else {
					//模板文件不存在  默认输出
					ExcelUtils.getInstance().exportObjects2Excel(result,ExportMarketKeyProvinceReportDO.class,response.getOutputStream());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				log.error(e.getMessage(), e);
			}
			
		}
		
	}
	
}
