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
import com.yundagalaxy.management.entity.SoaEmp;
import com.yundagalaxy.management.service.ISoaEmpService;
import com.yundagalaxy.management.vo.SoaEmpVO;
import com.yundagalaxy.management.vo.TmpEmployeeInfoVO;
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

/**
 * soa账号表 控制器
 *
 * @author dongfeng
 * @since 2019-11-08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/soaEmp")
@Api(value = "soa账号表", tags = "soa账号表接口")
public class SoaEmpController extends BladeController {

	private ISoaEmpService soaEmpService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入soaEmp")
	public R<SoaEmp> detail(SoaEmp soaEmp) {
		SoaEmp detail = soaEmpService.getOne(Condition.getQueryWrapper(soaEmp));
		return R.data(detail);
	}

	/**
	 * 分页 soa账号表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入soaEmp")
	public R<IPage<SoaEmp>> list(SoaEmp soaEmp, Query query) {
		IPage<SoaEmp> pages = soaEmpService.page(Condition.getPage(query), Condition.getQueryWrapper(soaEmp));
		return R.data(pages);
	}

	/**
	 * 自定义分页 soa账号表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入soaEmp")
	public R<IPage<SoaEmpVO>> page(SoaEmpVO soaEmp, Query query, BladeUser bladeUser) {
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		soaEmp.setCpCode(cpCode);
		IPage<SoaEmpVO> pages = soaEmpService.selectSoaEmpPage(Condition.getPage(query), soaEmp);
		return R.data(pages);
	}


	/**
	 * 修改soa新增非员工
	 */
	@PostMapping("/updateSoaToTmpEmp")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "修改soa新增非员工", notes = "传入tmpEmployeeInfoVO")
	public R updateSoaToTmpEmp(@Valid @RequestBody TmpEmployeeInfoVO tmpEmployeeInfoVO, BladeUser bladeUser) {

		return R.status(soaEmpService.updateSoaToTmpEmp(tmpEmployeeInfoVO,bladeUser));
	}

	/**
	 * 修改 soa账号表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入soaEmp")
	public R update(@Valid @RequestBody SoaEmp soaEmp) {
		return R.status(soaEmpService.updateById(soaEmp));
	}

	/**
	 * 新增或修改 soa账号表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入soaEmp")
	public R submit(@Valid @RequestBody SoaEmp soaEmp) {
		return R.status(soaEmpService.saveOrUpdate(soaEmp));
	}

	
	/**
	 * 删除 soa账号表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(soaEmpService.removeByIds(Func.toLongList(ids)));
	}

	
}
