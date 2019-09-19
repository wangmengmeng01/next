package com.yunda.base.feiniao.costreport.controller;

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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.crab2died.ExcelUtils;
import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.feiniao.costreport.bo.Bo_costreportCustRouteIncome_list;
import com.yunda.base.feiniao.costreport.domain.CostreportCustRouteIncomeDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportCRIncomeDetailDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportCustRouteIncomeDO;
import com.yunda.base.feiniao.costreport.service.CostreportCustRouteIncomeService;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.ProvinceDO;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
/**
 * 客户报表订单统计/客户线路收入
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-20160543
 */
 
@Controller
@RequestMapping("/costreport/costreportCustRouteIncome")
public class CostreportCustRouteIncomeController extends BaseController {
	Logger log = Logger.getLogger(getClass());
	
	@Value("${excel.targetFile.path}")
	private String target;
	
	@Value("${excel.template.path}")
	private String excelTemplate;
	
	@Autowired
	private CostreportCustRouteIncomeService costreportCustRouteIncomeService;
	
	/*@GetMapping()
	@RequiresPermissions("costreport:costreportCustRouteIncome:costreportCustRouteIncome")
	String CostreportCustRouteIncome(){
	    return "feiniao/costreport/costreportCustRouteIncome/costreportCustRouteIncome";
	}*/
	
	/**
	 * 获取收入列表数据
	 * @param request
	 * @param response
	 * @param boCostreportCustRouteIncomeList
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("costreport:costreportCustRouteIncome:costreportCustRouteIncome")
	public PageUtils list(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute @Validated Bo_costreportCustRouteIncome_list boCostreportCustRouteIncomeList, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();

			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			// 如果验参报错，返回空值給頁面
			PageUtils pageUtils = new PageUtils(null, 0);
			return pageUtils;
		}
		
		//查询列表数据
		List<CostreportCustRouteIncomeDO> costreportCustRouteIncomeList = null;
		try {
			costreportCustRouteIncomeList = costreportCustRouteIncomeService.getIncomelist(boCostreportCustRouteIncomeList,"db2");
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		int total = costreportCustRouteIncomeService.getIncomeCount(boCostreportCustRouteIncomeList,"db2");
		PageUtils pageUtils = new PageUtils(costreportCustRouteIncomeList, total);
		return pageUtils;
	}
	
	//获取省份
	@ResponseBody
	@GetMapping("/getAllProvinces")
	@RequiresPermissions("costreport:costreportCustRouteIncome:costreportCustRouteIncome")
	public List<ProvinceDO> getAllProvinces(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute @Validated Bo_costreportCustRouteIncome_list boCostreportCustRouteIncomeList, BindingResult bindingResult){
		List<ProvinceDO> provinceList = null;
		try {
			provinceList = costreportCustRouteIncomeService.getAllProvinces();
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return provinceList;
		
	}
	
	
	
	/**
	 * 点击收入，获取收入列表   js在 主页面js
	 * 
	 * @param branchCode
	 * @param customerId
	 * @param accountDt
	 * @param customerSourceType
	 * @param model
	 * @return
	 */
	@GetMapping("/getIncomeHtml/{accountDt}/{customerId}")
	@RequiresPermissions("costreport:costreportCustRouteIncome:costreportCustRouteIncome")
	String costreportCustIncomeHTML(@PathVariable("accountDt") String accountDt,
			@PathVariable("customerId") String customerId, Model model) {
		CostreportCustRouteIncomeDO custRouteIncomeDO = new CostreportCustRouteIncomeDO();
		custRouteIncomeDO.setCustomerId(customerId);
		custRouteIncomeDO.setAccountDt(accountDt);
		model.addAttribute("custRouteIncomeDO", custRouteIncomeDO);
		return "feiniao/costreport/costreportCustRouteIncome/costreportCustRouteIncome";
	}
	
	/**
	 *  点击票数，获取收入明细页面
	 *  
	 * @param customerSourceType
	 * @param accountDt
	 * @param branchCode
	 * @param customerId
	 * @param startProvinceId
	 * @param endProvinceId
	 * @param model
	 * @return
	 */
	@GetMapping("/getIncomeDetailHtml/{accountDt}/{branchCode}/{customerId}/{startProvinceId}/{endProvinceId}")
	@RequiresPermissions("costreport:costreportCustRouteIncome:costreportCustRouteIncome")
	String getIncomeDetailHtml(@PathVariable("accountDt") String accountDt, @PathVariable("branchCode") String branchCode,
			@PathVariable("customerId") String customerId,@PathVariable("startProvinceId") String startProvinceId,
			@PathVariable("endProvinceId") String endProvinceId, Model model) {
		CostreportCustRouteIncomeDO custRouteIncomeDO = new CostreportCustRouteIncomeDO();
		custRouteIncomeDO.setBranchCode(branchCode);
		custRouteIncomeDO.setCustomerId(customerId);
		custRouteIncomeDO.setAccountDt(accountDt);
		custRouteIncomeDO.setStartProvinceId(startProvinceId);
		custRouteIncomeDO.setEndProvinceId(endProvinceId);
		model.addAttribute("custRouteIncomeDetailDO", custRouteIncomeDO);
		return "feiniao/costreport/costreportCustRouteIncome/costreportCustRouteIncomeDetail";
	}

	
	
	
	// 导出收入报表
	@RequestMapping("/exportExcel")
	@RequiresPermissions("costrepor:costreportCustRouteIncome:exportExcel")
	public void exportExcel(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute @Validated Bo_costreportCustRouteIncome_list boCostreportCustRouteIncomeList, BindingResult bindingResult) {
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
			// 导出上限保护
			//boCostreportCustRouteIncomeList.setLimit(10000);
			//查询列表数据
			List<CostreportCustRouteIncomeDO> costreportCustRouteIncomeList = null;
			try {
				costreportCustRouteIncomeList = costreportCustRouteIncomeService.getIncomelist(boCostreportCustRouteIncomeList,"db2");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			String targetFile = SysConfig.TARGET + "custIncome"+ DateUtils.format(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
	
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"custIncome.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
				
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
				
				List<ExportCostreportCustRouteIncomeDO> result = costreportCustRouteIncomeService.filterData(costreportCustRouteIncomeList);
				if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, 
							result,data, ExportCostreportCustRouteIncomeDO.class, false, response.getOutputStream());
				} else {
					//模板文件不存在  默认输出
					ExcelUtils.getInstance().exportObjects2Excel(result,ExportCostreportCustRouteIncomeDO.class,response.getOutputStream());
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	

	/**
	 * 获取收入明细列表数据
	 * @param request
	 * @param response
	 * @param boCostreportCustRouteIncomeList
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@GetMapping("/incomeDetail")
	@RequiresPermissions("costreport:costreportCustRouteIncome:costreportCustRouteIncome")
	public PageUtils getIncomeDetail(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute @Validated Bo_costreportCustRouteIncome_list boCostreportCustRouteIncomeList, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();

			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			// 如果验参报错，返回空值給頁面
			PageUtils pageUtils = new PageUtils(null, 0);
			return pageUtils;
		}
		if(StringUtils.isNoneBlank(boCostreportCustRouteIncomeList.getAccountDt()+"")){
			boCostreportCustRouteIncomeList.setAccountDtStart(boCostreportCustRouteIncomeList.getAccountDt().substring(0, 4)+"-"+(boCostreportCustRouteIncomeList.getAccountDt()+"").substring(4)+"-01");       
			boCostreportCustRouteIncomeList.setAccountDtEnd(DateUtils.getMonthEnd(boCostreportCustRouteIncomeList.getAccountDtStart()+""));       
        }
		//查询列表数据
		List<CostreportCustRouteIncomeDO> costreportCustRouteIncomeList = null;
		try {
			costreportCustRouteIncomeList = costreportCustRouteIncomeService.getIncomeDetailList(boCostreportCustRouteIncomeList,"db2");
		} catch (Exception e) {
			//e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		int total = costreportCustRouteIncomeService.getIncomeDetailCount(boCostreportCustRouteIncomeList,"db2");
		PageUtils pageUtils = new PageUtils(costreportCustRouteIncomeList, total);
		return pageUtils;
	}
	
	
	// 导出收入明细报表
	@RequestMapping("/exportExcelDetail")
	@RequiresPermissions("costrepor:costreportCustRouteIncome:exportExcel")
	public void exportExcel2(HttpServletRequest request,HttpServletResponse response,
			@ModelAttribute @Validated Bo_costreportCustRouteIncome_list boCostreportCustRouteIncomeList) {
		//导出功能是否开放  true表示开放
		if (SysConfig.DAOCHU.equals("false")) {
			return;
		} else if (SysConfig.DAOCHU.equals("true")) {
			// 上限保护
			boCostreportCustRouteIncomeList.setLimit(10000);
			if(StringUtils.isNoneBlank(boCostreportCustRouteIncomeList.getAccountDt()+"")){
				boCostreportCustRouteIncomeList.setAccountDtStart(boCostreportCustRouteIncomeList.getAccountDt().substring(0, 4)+"-"+(boCostreportCustRouteIncomeList.getAccountDt()+"").substring(4)+"-01");       
				boCostreportCustRouteIncomeList.setAccountDtEnd(DateUtils.getMonthEnd(boCostreportCustRouteIncomeList.getAccountDtStart()+""));       
	        }
			//查询列表数据
			List<CostreportCustRouteIncomeDO> costreportCustRouteIncomeList = null;
			try {
				costreportCustRouteIncomeList = costreportCustRouteIncomeService.getIncomeDetailList(boCostreportCustRouteIncomeList,"db2");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
			String targetFile = SysConfig.TARGET + "custIncomeDetail"+ DateUtils.format(new java.util.Date()) + ".xlsx";
			File downloadFile = new File(targetFile);
	
			try {
				// 按命名规则找模版文件
				File file = new File(SysConfig.TEMPLATE+"custIncomeDetail.xlsx");
				response.setContentType("application/vnd.ms-excel;charset=utf-8");
				response.setCharacterEncoding("utf-8");
				
				// set headers for the response
				String headerKey = "Content-Disposition";
				String headerValue = String.format("attachment; filename=\"%s\"", new String(downloadFile.getName().getBytes(StandardCharsets.UTF_8), "iso8859-1"));
				response.setHeader(headerKey, headerValue);
				
				List<ExportCostreportCRIncomeDetailDO> result = costreportCustRouteIncomeService.filterData2(costreportCustRouteIncomeList);
				if(file.exists()  &&  SysConfig.USER_TEMPLATE.equals("true")){
					Map<String, String> data = new HashMap<>();
					// 基于模板导出Excel  https://gitee.com/Crab2Died/Excel4J
					ExcelUtils.getInstance().exportObjects2Excel(file.getPath(), 0, 
							result,data, ExportCostreportCRIncomeDetailDO.class, false, response.getOutputStream());
				} else {
					//模板文件不存在  默认输出
					ExcelUtils.getInstance().exportObjects2Excel(result,ExportCostreportCRIncomeDetailDO.class,response.getOutputStream());
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
}
