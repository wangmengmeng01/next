package com.yunda.base.system.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.yunda.base.system.domain.RoleDO;
import com.yunda.base.system.service.RoleService;
import com.yunda.ydmbspringbootstarter.common.annotation.Log;
@RequestMapping("/sys/role")
@Controller
public class RoleController extends BaseController {
	String prefix = "system/role";
	@Autowired
	RoleService roleService;

	@RequiresPermissions("sys:role:role")
	@GetMapping()
	String role() {
		return prefix + "/role";
	}

	@RequiresPermissions("sys:role:role")
	@GetMapping("/list")
	@ResponseBody()
	PageUtils list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<RoleDO> roles = roleService.listRole(query);
		int total = roleService.count(query);
		PageUtils pageUtil = new PageUtils(roles, total);
		return pageUtil;
	}

	@Log("添加角色")
	@RequiresPermissions("sys:role:add")
	@GetMapping("/add")
	String add() {
		return prefix + "/add";
	}

	@Log("编辑角色")
	@RequiresPermissions("sys:role:edit")
	@GetMapping("/edit/{roleId}")
	String edit(@PathVariable("roleId") Long roleId, Model model) {
		RoleDO roleDO = roleService.get(roleId);
		model.addAttribute("role", roleDO);
		return prefix + "/edit";
	}

	@Log("保存角色")
	@RequiresPermissions("sys:role:add")
	@PostMapping("/save")
	@ResponseBody()
	R save(RoleDO role) {
		//设置角色id(3位)
		//查看数据库中最后一个roleId是多少,然后新增
		Long lastRoleId = roleService.getLastRoleId();
		Long newRoleIdLong = ++lastRoleId;
		//1位左边补10,因为数据库类型原因导致前面存0不显示
		if(newRoleIdLong<10L){
			newRoleIdLong=newRoleIdLong+100;
		}
		//2位左边补1个1
		if(newRoleIdLong<100L){
			newRoleIdLong=newRoleIdLong+100;
		}
		if(newRoleIdLong>=10000L){
			return R.error("系统角色Id已经超过3位数了!");
		}
		//这种可能会出现
		role.setRoleId(newRoleIdLong);
		//创建用户角色状态为启用
		role.setState("1");
		if (roleService.save(role) > 0) {
			return R.ok();
		} else {
			return R.error(1, "保存失败");
		}
	}

	@Log("更新角色")
	@RequiresPermissions("sys:role:edit")
	@PostMapping("/update")
	@ResponseBody()
	R update(RoleDO role) {
		if (roleService.update(role) > 0) {
			return R.ok();
		} else {
			return R.error(1, "保存失败");
		}
	}

	@Log("删除角色")
	@RequiresPermissions("sys:role:remove")
	@PostMapping("/remove")
	@ResponseBody()
	R save(Long id) {
		if (roleService.remove(id) > 0) {
			return R.ok();
		} else {
			return R.error(1, "删除失败");
		}
	}
	
	@RequiresPermissions("sys:role:batchRemove")
	@Log("批量删除角色")
	@PostMapping("/batchRemove")
	@ResponseBody
	R batchRemove(@RequestParam("ids[]") Long[] ids) {
		int r = roleService.batchremove(ids);
		if (r > 0) {
			return R.ok();
		}
		return R.error();
	}

	/**
	 * 修改状态(启用或者禁止)
	 * @return
	 */
	@PostMapping( "/stateUpdate")
	@ResponseBody
	public R stateUpdate(@ModelAttribute RoleDO roleDO){
		String state = roleDO.getState();
		Long roleId = roleDO.getRoleId();
		if(state.equals("1")){
			//将状态改为禁用
			state="0";
			roleService.stateUpdate(roleId,state);
			return R.ok("已禁止");
		}else {
			//将状态改为启用
			state="1";
			roleService.stateUpdate(roleId,state);
			return R.ok("已启用");
		}

	}

	/**
	 * 分配资源
	 */
	@GetMapping("/fenpei/{roleId}")
	@RequiresPermissions("sys:role:fenpei")
	String fenpei(@PathVariable("roleId") Long roleId, Model model) {
		RoleDO roleDO = roleService.get(roleId);
		model.addAttribute("role", roleDO);
		return prefix + "/fenpei";
	}

	/**
	 * 保存分配资源信息
	 * @param role
	 * @return
	 */
	@RequiresPermissions("sys:role:fenpei")
	@PostMapping("/fenpeiSave")
	@ResponseBody()
	R fenpeiSave(RoleDO role) {
		//这个角色有多少人(这些角色的人只能看)
		//1.判断数据权限是本人,还是本部门还是全部,根据选择不同展示不同的数据
		//2.如果是本人,就只能查看本人创建的数据
		//2.1获取本人的账号
		//3.如果是本部门,就只能查看本部门创建的数据
		//3.1根据当前登录人的信息获取本部门所有的账号
		//4.如果勾选的是全部,拿就看到的是全部数据
		if (roleService.update(role) > 0) {
			return R.ok();
		} else {
			return R.error(1, "保存失败");
		}
	}

	/**
	 * 通过roleId获取数据权限
	 */
	@GetMapping("/getDataPermissionsByRoleId/{roleId}")
	@ResponseBody
	String getDataPermissionsByRoleId(@PathVariable("roleId") Long roleId) {
		RoleDO roleDO = roleService.get(roleId);
		return roleDO.getDataPermissions();
	}
}