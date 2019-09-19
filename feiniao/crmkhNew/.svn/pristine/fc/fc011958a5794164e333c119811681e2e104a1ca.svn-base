package com.yunda.base.feiniao.crmapp.costReport.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.feiniao.crmapp.costReport.bo.Bo_custSingleCost;
import com.yunda.base.feiniao.crmapp.costReport.domain.CostreportLiangBenLiDO;
import com.yunda.base.feiniao.crmapp.costReport.service.CostreportLiangBenLiService;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;

/**
 * 客户报表订单统计/客户支出报表(完成统计)
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-07-31135523
 */
 
@Controller
@RequestMapping("/costReport/costreportCustCostFinish")
public class CostreportLiangBenLiController extends BaseController{
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private CostreportLiangBenLiService costreportLiangBenLiService;

	
	/**
	 * 接口9 客户单票成本 
	 * 请求参数：网点编码，客户编码，请求月份，授权码
	 */
	@ResponseBody
	@GetMapping("/getCustSingleCost")
	@MethodLock(key = "request")
	//@RequiresPermissions("costReport:costreportCustCostFinish:costreportCustCostFinish")
	public RspBean<PageUtils> getCustSingleCost(HttpServletRequest request, HttpServletResponse response,
		@ModelAttribute @Validated Bo_custSingleCost boCustSingleCost, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			notifyPage(bindingResult.getFieldError().getDefaultMessage());
			// 如果验参报错，返回空值给页面
			return success(new PageUtils());
		}
		//UserDO loginUser = getUser(request);
		
		List<CostreportLiangBenLiDO> singleCostList = costreportLiangBenLiService.getCustSingleCost(boCustSingleCost);
		int total = 0;
		if(null != singleCostList && singleCostList.size() > 0){
			total = singleCostList.size();
		}
		PageUtils pageUtils = new PageUtils(singleCostList, total);
		return success(pageUtils);
	}

	/**
	 * 接口10 票件详情 
	 * 请求参数：网点编码，客户编码，请求月份，授权码
	 * 返回参数：推前20条单号信息
	 */
	@ResponseBody
	@GetMapping("/getCustSingleOrderDetail")
	@MethodLock(key = "request")
	//@RequiresPermissions("costReport:costreportCustCostFinish:costreportCustCostFinish")
	public RspBean<PageUtils> getCustSingleOrderDetail(HttpServletRequest request, HttpServletResponse response,
		@ModelAttribute @Validated Bo_custSingleCost boCustSingleCost, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			notifyPage(bindingResult.getFieldError().getDefaultMessage());
			// 如果验参报错，返回空值给页面
			return success(new PageUtils());
		}
		//UserDO loginUser = getUser(request);
		
		List<CostreportLiangBenLiDO> orderDetailList = costreportLiangBenLiService.getCustSingleOrderDetail(boCustSingleCost);
		int total = 0;
		if(null != orderDetailList && orderDetailList.size() > 0){
			total = orderDetailList.size();
		}
		PageUtils pageUtils = new PageUtils(orderDetailList, total);
		return success(pageUtils);
	}
	
	/**
	 * 接口6  月量本利信息接口 
	 * 请求参数：网点编码,请求月份，授权码
	 * 返回参数：推前20条单号信息
	 */
	@ResponseBody
	@GetMapping("/getMonthCostAndIncome")
	@MethodLock(key = "request")
	//@RequiresPermissions("costReport:costreportCustCostFinish:costreportCustCostFinish")
	public RspBean<PageUtils> getMonthCostAndIncome(HttpServletRequest request, HttpServletResponse response,
		@ModelAttribute @Validated Bo_custSingleCost boCustSingleCost, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			notifyPage(bindingResult.getFieldError().getDefaultMessage());
			// 如果验参报错，返回空值给页面
			return success(new PageUtils());
		}
		//UserDO loginUser = getUser(request);
		
		List<CostreportLiangBenLiDO> orderDetailList = costreportLiangBenLiService.getMonthCostAndIncome(boCustSingleCost);
		int total = 0;
		if(null != orderDetailList && orderDetailList.size() > 0){
			total = orderDetailList.size();
		}
		PageUtils pageUtils = new PageUtils(orderDetailList, total);
		return success(pageUtils);
	}
}
