package com.yunda.base.feiniao.workorder.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunda.base.common.controller.BaseController;
import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.feiniao.workorder.bo.Bo_ServiceWorkOrder;
import com.yunda.base.feiniao.workorder.domain.ServiceWorkorderDO;
import com.yunda.base.feiniao.workorder.service.ServiceWorkorderService;
import com.yunda.ydmbspringbootstarter.common.utils.ObjectUtils;

import io.swagger.annotations.ApiOperation;
/**
 * 客户咨询单管理/客户咨询单
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-06-29 09:20:45
 */
 
@Controller
@RequestMapping("/workorder/serviceWorkorder")
public class ServiceWorkorderController  extends BaseController {
	private static final Logger log = Logger.getLogger(ServiceWorkorderController.class);
	
	@Autowired
	private ServiceWorkorderService serviceWorkorderService;
	
	@GetMapping()
	@RequiresPermissions("workorder:serviceWorkorder:serviceWorkorder")
	String ServiceWorkorder(){
	    return "feiniao/workorder/serviceWorkorder/serviceWorkorder";
	}
	
	@ApiOperation(value = "获取咨询单列表")
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("workorder:serviceWorkorder:serviceWorkorder")
	//此方法会先从model去获取key为"params"的对象,如果获取不到会通过反射实例化一个Bo_GetWorkOrderList对象,再从request里面拿值set到这个对象,然后把这个Bo_GetWorkOrderList对象添加到model(其中key为"params").
	//使用了@ModelAttribute可修改这个key,不一定是"params",此情况下,用与不用@ModelAttribute没有区别.
	public PageUtils list(
			@ModelAttribute
			@Validated	Bo_ServiceWorkOrder params, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			FieldError fieldError = bindingResult.getFieldError();
			
			notifyPage(fieldError.getField() + ":" + fieldError.getDefaultMessage());
			//如果驗參報錯，返回空值給頁面
			PageUtils pageUtils = new PageUtils(null, 0);
			return pageUtils;
		}
		
		//查询列表数据
        Query query = new Query(ObjectUtils.objectToMap(params));
		List<ServiceWorkorderDO> serviceWorkorderList = serviceWorkorderService.list(query);
		int total = serviceWorkorderService.count(query);
		PageUtils pageUtils = new PageUtils(serviceWorkorderList, total);
		return pageUtils;
	}
	/*@ApiOperation(value = "获取咨询单列表")
	@ResponseBody
	@GetMapping("/list")
	//@RequestMapping(value = "/list", method = RequestMethod.GET)
	@RequiresPermissions("workorder:serviceWorkorder:serviceWorkorder")
	public RspBean<PageUtils> list(
			@ApiParam(name = "params", value = "参数", required = false)
			@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ServiceWorkorderDO> serviceWorkorderList = serviceWorkorderService.list(query);
		int total = serviceWorkorderService.count(query);
		PageUtils pageUtils = new PageUtils(serviceWorkorderList, total);
		return success(pageUtils);
	}*/
	
	@GetMapping("/add")
	@RequiresPermissions("workorder:serviceWorkorder:add")
	String add(){
	    return "feiniao/workorder/serviceWorkorder/add";
	}

	@GetMapping("/edit/{workOrderId}")
	@RequiresPermissions("workorder:serviceWorkorder:edit")
	String edit(@PathVariable("workOrderId") Integer workOrderId,Model model){
		ServiceWorkorderDO serviceWorkorder = serviceWorkorderService.get(workOrderId);
		model.addAttribute("serviceWorkorder", serviceWorkorder);
	    return "feiniao/workorder/serviceWorkorder/edit";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("workorder:serviceWorkorder:add")
	public R save(ServiceWorkorderDO serviceWorkorder){
		if(serviceWorkorderService.save(serviceWorkorder)>0){
			return R.ok();
		}
		return R.error(700,"xxxxxxxx");
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("workorder:serviceWorkorder:edit")
	public R update( ServiceWorkorderDO serviceWorkorder){
		serviceWorkorderService.update(serviceWorkorder);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/remove")
	@ResponseBody
	@RequiresPermissions("workorder:serviceWorkorder:remove")
	public R remove( Integer workOrderId){
		if(serviceWorkorderService.remove(workOrderId)>0){
		return R.ok();
		}
		return R.error();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/batchRemove")
	@ResponseBody
	@RequiresPermissions("workorder:serviceWorkorder:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] workOrderIds){
		serviceWorkorderService.batchRemove(workOrderIds);
		return R.ok();
	}
	
}
