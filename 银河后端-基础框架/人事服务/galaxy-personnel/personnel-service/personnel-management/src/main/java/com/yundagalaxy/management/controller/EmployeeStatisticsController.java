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
import com.yundagalaxy.management.entity.EmployeeStatistics;
import com.yundagalaxy.management.service.IEmployeeStatisticsService;
import com.yundagalaxy.management.vo.EmployeeStatisticsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 人事流动趋势表 控制器
 *
 * @author dongfeng
 * @since 2019-10-24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/employeeStatistics")
@Api(value = "人事流动趋势表", tags = "人事流动趋势表接口")
public class EmployeeStatisticsController extends BladeController {

	private IEmployeeStatisticsService employeeStatisticsService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入employeeStatistics")
	public R<EmployeeStatistics> detail(EmployeeStatistics employeeStatistics) {
		EmployeeStatistics detail = employeeStatisticsService.getOne(Condition.getQueryWrapper(employeeStatistics));
		return R.data(detail);
	}

	/**
	 * 分页 人事流动趋势表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入employeeStatistics")
	public R<IPage<EmployeeStatistics>> list(EmployeeStatistics employeeStatistics, Query query) {
		IPage<EmployeeStatistics> pages = employeeStatisticsService.page(Condition.getPage(query), Condition.getQueryWrapper(employeeStatistics));
		return R.data(pages);
	}

	/**
	 * 自定义分页 人事流动趋势表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入employeeStatistics")
	public R<IPage<EmployeeStatisticsVO>> page(EmployeeStatisticsVO employeeStatistics, Query query) {
		IPage<EmployeeStatisticsVO> pages = employeeStatisticsService.selectEmployeeStatisticsPage(Condition.getPage(query), employeeStatistics);
		return R.data(pages);
	}

	/**
	 * 新增 人事流动趋势表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入employeeStatistics")
	public R save(@Valid @RequestBody EmployeeStatistics employeeStatistics) {
		return R.status(employeeStatisticsService.save(employeeStatistics));
	}

	/**
	 * 修改 人事流动趋势表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入employeeStatistics")
	public R update(@Valid @RequestBody EmployeeStatistics employeeStatistics) {
		return R.status(employeeStatisticsService.updateById(employeeStatistics));
	}

	/**
	 * 新增或修改 人事流动趋势表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入employeeStatistics")
	public R submit(@Valid @RequestBody EmployeeStatistics employeeStatistics) {
		return R.status(employeeStatisticsService.saveOrUpdate(employeeStatistics));
	}

	
	/**
	 * 删除 人事流动趋势表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(employeeStatisticsService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 分页 人事流动趋势表
	 */
	@GetMapping("/all")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "查询所有", notes = "传入employeeStatistics")
	public R all(Integer timeType, BladeUser bladeUser) {
        if(null==timeType){
			timeType= 0;
		}
		Map ma = new HashMap(2);
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		ma.put("cpCode",cpCode);
		ma.put("timeType",timeType);
		List<EmployeeStatisticsVO> lists = employeeStatisticsService.selectEmployeeStatisticsAll(ma);
		return R.data(lists);
	}
}
