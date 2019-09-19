package com.yunda.base.system.controller;

import java.util.List;
import java.util.Map;

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
import com.yunda.base.system.domain.SysConfigDO;
import com.yunda.base.system.service.SysConfigService;

/**
 * 
 * 
 * @author zhanghan
 * @email zhanghan813@163.com
 * @date 2018-04-27 23:40:06
 */

@Controller
@RequestMapping("/system/config")
public class SysConfigController {
	@Autowired
	private SysConfigService configService;

	@GetMapping()
	@RequiresPermissions("system:config:config")
	String Config() {
		return "system/config/config";
	}

	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("system:config:config")
	public PageUtils list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<SysConfigDO> configList = configService.list(query);
		int total = configService.count(query);
		PageUtils pageUtils = new PageUtils(configList, total);
		return pageUtils;
	}

	@GetMapping("/add")
	@RequiresPermissions("system:config:add")
	String add() {
		return "system/config/add";
	}

	@GetMapping("/edit/{id}")
	@RequiresPermissions("system:config:edit")
	String edit(@PathVariable("id") Integer id, Model model) {
		SysConfigDO config = configService.get(id);
		model.addAttribute("config", config);
		return "system/config/edit";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	@RequiresPermissions("system:config:add")
	public R save(SysConfigDO config) {
		if (configService.save(config) > 0) {
			configService.initConfig();
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("system:config:edit")
	public R update(SysConfigDO config) {
		configService.update(config);
		configService.initConfig();
		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	@RequiresPermissions("system:config:remove")
	public R remove(Integer id) {
		if (configService.remove(id) > 0) {
			configService.initConfig();
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	@RequiresPermissions("system:config:batchRemove")
	public R remove(@RequestParam("ids[]") Integer[] ids) {
		configService.initConfig();
		configService.batchRemove(ids);
		return R.ok();
	}

}
