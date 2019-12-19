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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yundagalaxy.management.dto.EmployeeSalaryInfoDTO;
import com.yundagalaxy.management.entity.EmployeeSalaryInfo;
import com.yundagalaxy.management.service.IEmployeeBasicInfoService;
import com.yundagalaxy.management.service.IEmployeeSalaryInfoService;
import com.yundagalaxy.management.vo.EmployeeSalaryInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 员工薪酬信息表 控制器
 *
 * @author BladeX
 * @since 2019-10-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/employeesalaryinfo")
@Api(value = "员工薪酬信息表", tags = "员工薪酬信息表接口")
public class EmployeeSalaryInfoController extends BladeController {

	private IEmployeeSalaryInfoService employeeSalaryInfoService;

	private IEmployeeBasicInfoService employeeBasicInfoService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入employeeSalaryInfo")
	public R<EmployeeSalaryInfo> detail(EmployeeSalaryInfo employeeSalaryInfo) {
		EmployeeSalaryInfo detail = employeeSalaryInfoService.getOne(Condition.getQueryWrapper(employeeSalaryInfo));
		return R.data(detail);
	}

	/**
	 * 分页 员工薪酬信息表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入employeeSalaryInfo")
	public R<IPage<EmployeeSalaryInfo>> list(EmployeeSalaryInfo employeeSalaryInfo, Query query) {
		IPage<EmployeeSalaryInfo> pages = employeeSalaryInfoService.page(Condition.getPage(query), Condition.getQueryWrapper(employeeSalaryInfo));
		return R.data(pages);
	}

	/**
	 * 自定义分页 员工薪酬信息表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入employeeSalaryInfo")
	public R<IPage<EmployeeSalaryInfoVO>> page(EmployeeSalaryInfoVO employeeSalaryInfo, Query query) {
		IPage<EmployeeSalaryInfoVO> pages = employeeSalaryInfoService.selectEmployeeSalaryInfoPage(Condition.getPage(query), employeeSalaryInfo);
		return R.data(pages);
	}

	/**
	 * 新增 员工薪酬信息表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入employeeSalaryInfo")
	public R save(@Valid @RequestBody EmployeeSalaryInfo employeeSalaryInfo) {
		return R.status(employeeSalaryInfoService.save(employeeSalaryInfo));
	}

	/**
	 * 修改 员工薪酬信息表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入employeeSalaryInfo")
	public R update(@Valid @RequestBody EmployeeSalaryInfo employeeSalaryInfo) {
		return R.status(employeeSalaryInfoService.updateById(employeeSalaryInfo));
	}

	/**
	 * 新增或修改 员工薪酬信息表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入employeeSalaryInfoDTO")
	public R submit(@Valid @RequestBody EmployeeSalaryInfoDTO employeeSalaryInfoDTO) {
		employeeBasicInfoService.updateDateTimeByEmpCode(employeeSalaryInfoDTO.getEmpCode(), employeeSalaryInfoDTO.getFinalBy());
		return R.status(employeeSalaryInfoService.saveOrUpdate(employeeSalaryInfoDTO));
	}

	
	/**
	 * 删除 员工薪酬信息表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(employeeSalaryInfoService.removeByIds(Func.toLongList(ids)));
	}

	
}
