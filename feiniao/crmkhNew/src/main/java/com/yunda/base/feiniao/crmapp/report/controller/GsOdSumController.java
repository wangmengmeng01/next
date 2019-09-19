package com.yunda.base.feiniao.crmapp.report.controller;

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
import com.yunda.base.feiniao.crmapp.report.bo.Bo_gsOdSum;
import com.yunda.base.feiniao.crmapp.report.domain.GsOdSumDO;
import com.yunda.base.feiniao.crmapp.report.service.GsOdSumService;
import com.yunda.base.system.vo.RspBean;
import com.yunda.ydmbspringbootstarter.common.annotation.MethodLock;

/**
 * crmapp业务量相关接口
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-07-29165448
 */
 
@Controller
@RequestMapping("/report/gsOdSum")
public class GsOdSumController extends BaseController{
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private GsOdSumService gsOdSumService;
	
	/**
	 * 10天揽件量接口
	 * @param request
	 * @param response
	 * @param boGsOdSum
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@GetMapping("/getBranchlist")
	//@MethodLock(key = "request")
	//@RequiresPermissions("report:gsOdSum:gsOdSum")
	public RspBean<PageUtils> getBranchlist(HttpServletRequest request, HttpServletResponse response,
		@ModelAttribute @Validated Bo_gsOdSum boGsOdSum, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			notifyPage(bindingResult.getFieldError().getDefaultMessage());
			// 如果验参报错，返回空值给页面
			return success(new PageUtils());
		}
		List<GsOdSumDO> gsOdSumList = gsOdSumService.getBranchlist(boGsOdSum);
		int total = 0;
		if(null != gsOdSumList && gsOdSumList.size() >0){
			total = gsOdSumList.size();
		}
		PageUtils pageUtils = new PageUtils(gsOdSumList, total);
		return success(pageUtils);
	}

	/**
	 * 客户揽件量接口
	 * @param request
	 * @param response
	 * @param boGsOdSum
	 * @param bindingResult
	 * @return
	 */
	@ResponseBody
	@GetMapping("/getCustlist")
	//@MethodLock(key = "request")
	//@RequiresPermissions("report:gsOdSum:gsOdSum")
	public RspBean<PageUtils> getCustlist(HttpServletRequest request, HttpServletResponse response,
		@ModelAttribute @Validated Bo_gsOdSum boGsOdSum, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			notifyPage(bindingResult.getFieldError().getDefaultMessage());
			// 如果验参报错，返回空值给页面
			return success(new PageUtils());
		}
		List<GsOdSumDO> gsOdSumList = gsOdSumService.getCustlist(boGsOdSum);
		int total = gsOdSumService.custlistCount(boGsOdSum);
		PageUtils pageUtils = new PageUtils(gsOdSumList, total);
		return success(pageUtils);
	}
	
}
