/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.yundagalaxy.management.controller;

import com.yundagalaxy.management.service.IEmployeeKanbanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 人事流动趋势表 控制器
 *
 * @author dongfeng
 * @since 2019-10-24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/employKanban")
@Api(value = "人事看板", tags = "人事看板")
public class EmployeeKanbanController extends BladeController {

	private IEmployeeKanbanService employeeKanbanService;

	/**
	 * 员工概况（在职/离职）
	 */
	@GetMapping("/profile")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "员工概况（在职/离职）", notes = "员工概况（在职/离职）")
	public R profile(BladeUser bladeUser) {
		Map mm = new HashMap(1);
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		mm.put("cpCode",cpCode);
        Map<String,Long> data = employeeKanbanService.getProfile(mm);
		return R.data(data);
	}
	/**
	 * （在职/离职）分析
	 */
	@GetMapping("/analysis")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "员工分析（在职/离职）", notes = "员工发现（在职/离职）")
	public R analysis(Integer workingState,BladeUser bladeUser) {
		Map ma = new HashMap(2);
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		ma.put("cpCode",cpCode);
		if(null==workingState ||0==workingState){
			workingState=1;
            ma.put("endWorkingState",workingState);
		}else if(1==workingState){
            workingState=2;
            ma.put("startWorkingState",workingState);
        }


		Map<String,Object> data = employeeKanbanService.getAnalysis(ma);
		return R.data(data);
	}


}
