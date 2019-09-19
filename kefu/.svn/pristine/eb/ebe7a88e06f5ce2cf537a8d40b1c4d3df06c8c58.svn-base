package com.yunda.base.system.controller;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunda.base.common.utils.PageUtils;
import com.yunda.base.common.utils.Query;
import com.yunda.base.common.utils.R;
import com.yunda.base.system.domain.ProvinceDO;
import com.yunda.base.system.service.ProvinceService;

/**
 * 维护业务省和大区的关系  表sys_province
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-19094146
 */
 
@Controller
@RequestMapping("/system/provinceBigarea")
public class ProvinceBigareaController {
	Logger log = Logger.getLogger(getClass());
	@Autowired
	private ProvinceService provinceService;
	
	@GetMapping()
	@RequiresPermissions("system:provinceBigarea:province")
	String Province(){
	    return "system/province/provinceBigarea";
	}
	
	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:provinceBigarea:province")
	public PageUtils list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<ProvinceDO> provinceList = provinceService.list(query);
		int total = provinceService.count(query);
		PageUtils pageUtils = new PageUtils(provinceList, total);
		return pageUtils;
	}	
	
	@GetMapping("/addProvince")
	@RequiresPermissions("system:provinceBigarea:add")
	String addProvince(){
	    return "system/province/addProvinceBigarea";
	}

	@GetMapping("/editProvinceBigarea/{provinceid}")
	@RequiresPermissions("system:provinceBigarea:edit")
	String editProvinceBigarea(@PathVariable("provinceid") String provinceid,Model model){
		ProvinceDO province = provinceService.get(provinceid);
		model.addAttribute("province", province);
	    return "system/province/editProvinceBigarea";
	}
	
	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/saveProvinceBigarea")
	@RequiresPermissions("system:provinceBigarea:add")
	public R saveProvinceBigarea( ProvinceDO province){
		if(provinceService.save(province)>0){
			return R.ok();
		}
		return R.error();
	}
	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/updateProvinceBigarea")
	@RequiresPermissions("system:provinceBigarea:edit")
	public R updateProvinceBigarea( ProvinceDO province){
		provinceService.update(province);
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@PostMapping( "/removeProvinceBigarea")
	@ResponseBody
	@RequiresPermissions("system:provinceBigarea:remove")
	public R removeProvinceBigarea( String provinceid){
		if(provinceService.remove(provinceid)>0){
		return R.ok();
		}
		return R.error();
	}
	
}
