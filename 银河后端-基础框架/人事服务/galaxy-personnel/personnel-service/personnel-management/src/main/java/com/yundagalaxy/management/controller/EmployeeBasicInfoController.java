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
import com.yundagalaxy.management.commnon.utils.DictCpToVoUtil;
import com.yundagalaxy.management.dto.SoaEmpDTO;
import com.yundagalaxy.management.entity.DepartmentInfo;
import com.yundagalaxy.management.entity.DepartmentJob;
import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import com.yundagalaxy.management.service.IDepartmentInfoService;
import com.yundagalaxy.management.service.IDepartmentJobService;
import com.yundagalaxy.management.service.IEmployeeBasicInfoService;
import com.yundagalaxy.management.service.IEmployeeInfoAggregationService;
import com.yundagalaxy.management.vo.EmployeeBasicInfoVO;
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
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工基本信息表 控制器
 *
 * @author BladeX
 * @since 2019-10-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/employeebasicinfo")
@Api(value = "员工基本信息表", tags = "员工基本信息表接口")
public class EmployeeBasicInfoController extends BladeController {

	private IEmployeeInfoAggregationService employeeInfoAggregationService;

	private IEmployeeBasicInfoService employeeBasicInfoService;

	private IDepartmentInfoService departmentInfoService;

	private IDepartmentJobService departmentJobService;

	@Autowired
	private DictCpToVoUtil dictCpToVoUtil;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入employeeBasicInfo")
	public R<EmployeeBasicInfo> detail(EmployeeBasicInfo employeeBasicInfo) {
		EmployeeBasicInfo detail = employeeBasicInfoService.getOne(Condition.getQueryWrapper(employeeBasicInfo));
		return R.data(detail);
	}

	/**
	 * 分页 员工基本信息表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入employeeBasicInfo")
	public R<IPage<EmployeeBasicInfo>> list(EmployeeBasicInfo employeeBasicInfo, Query query) {
		IPage<EmployeeBasicInfo> pages = employeeBasicInfoService.page(Condition.getPage(query), Condition.getQueryWrapper(employeeBasicInfo));
		return R.data(pages);
	}

	/**
	 * 自定义分页 员工基本信息表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入employeeBasicInfo")
	public R<IPage<EmployeeBasicInfoVO>> page(EmployeeBasicInfoVO employeeBasicInfo, Query query) {
		IPage<EmployeeBasicInfoVO> pages = employeeBasicInfoService.selectEmployeeBasicInfoPage(Condition.getPage(query), employeeBasicInfo);
		return R.data(pages);
	}

	/**
	 * 新增 员工基本信息表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入employeeBasicInfo")
	public R save(@Valid @RequestBody EmployeeBasicInfo employeeBasicInfo) {
		return R.status(employeeBasicInfoService.save(employeeBasicInfo));
	}

	/**
	 * 修改 员工基本信息表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入employeeBasicInfo")
	public R update(@Valid @RequestBody EmployeeBasicInfo employeeBasicInfo) {
		return R.status(employeeBasicInfoService.updateById(employeeBasicInfo));
	}

	/**
	 * 新增或修改 员工基本信息表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入employeeBasicInfo")
	public R submit(@Valid @RequestBody EmployeeBasicInfo employeeBasicInfo) {
		return R.status(employeeBasicInfoService.saveOrUpdate(employeeBasicInfo));
	}

	
	/**
	 * 删除 员工基本信息表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(employeeBasicInfoService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 网点下用户 - 下拉框
	 * @param bladeUser
	 * @return
	 */
	@GetMapping("/getUsers")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "获取用户", notes = "拉框")
	public R<List<Map<String,Object>>> getUsers(BladeUser bladeUser) {
		List<Map<String, Object>> listMapResult = new ArrayList<>();
		List<EmployeeBasicInfo> list = employeeBasicInfoService.list(Condition.getQueryWrapper(new EmployeeBasicInfo()).lambda().eq(EmployeeBasicInfo::getCpCode, bladeUser.getDeptId()));
		if (list==null){
			return null;
		}
		for (EmployeeBasicInfo vo:list) {
			Map<String,Object> map = new HashMap<>();
			map.put("empCode", vo.getEmpCode());
			map.put("name", vo.getName());
			listMapResult.add(map);
		}
		return R.data(listMapResult);
	}

	/**
	 * 网点下用户 - 下拉框 - 排除已经离职
	 * @param bladeUser
	 * @return
	 */
	@GetMapping("/getUsersNL")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "获取用户", notes = "排除已经离职")
	public R<List<Map<String,Object>>> getUsersNL(BladeUser bladeUser) {
		List<Map<String, Object>> listMapResult = new ArrayList<>();
		List<EmployeeBasicInfo> list = employeeBasicInfoService.list(Condition.getQueryWrapper(new EmployeeBasicInfo()).lambda()
				.eq(EmployeeBasicInfo::getCpCode, bladeUser.getDeptId())
				.ne(EmployeeBasicInfo::getWorkingState,2));
		if (list==null){
			return null;
		}
		for (EmployeeBasicInfo vo:list) {
			Map<String,Object> map = new HashMap<>();
			map.put("empCode", vo.getEmpCode());
			map.put("name", vo.getName());
			listMapResult.add(map);
		}
		return R.data(listMapResult);
	}

	/**
	 * 网点下用户 - 下拉框 - 试用
	 * @param bladeUser
	 * @return
	 */
	@GetMapping("/getUsersNW")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "获取用户", notes = "试用")
	public R<List<Map<String,Object>>> getUsersNW(BladeUser bladeUser) {
		List<Map<String, Object>> listMapResult = new ArrayList<>();
		List<EmployeeBasicInfo> list = employeeBasicInfoService.list(Condition.getQueryWrapper(new EmployeeBasicInfo()).lambda()
				.eq(EmployeeBasicInfo::getCpCode, bladeUser.getDeptId())
				.eq(EmployeeBasicInfo::getWorkingState, 0));
		if (list==null){
			return null;
		}
		for (EmployeeBasicInfo vo:list) {
			Map<String,Object> map = new HashMap<>();
			map.put("empCode", vo.getEmpCode());
			map.put("name", vo.getName());
			listMapResult.add(map);
		}
		return R.data(listMapResult);
	}

	/**
	 * 员工编码获取详情
	 */
	@GetMapping("/detailByEmpCode")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "员工编码获取详情", notes = "传入empCode")
	public R<EmployeeBasicInfoVO> detailByEmpCode(@RequestParam("empCode") String empCode) {
		EmployeeBasicInfo detail = employeeBasicInfoService.getOne(Condition.getQueryWrapper(new EmployeeBasicInfo())
				.lambda().eq(EmployeeBasicInfo::getEmpCode, empCode));
		EmployeeBasicInfoVO employeeBasicInfoVO = dictCpToVoUtil.entityVO(detail);
		return R.data(employeeBasicInfoVO);
	}

	/**
	 * 确认提交离职
	 */
	@GetMapping("/workQuit")
	@ApiOperationSupport(order = 11)
	@ApiOperation(value = "确认提交离职", notes = "传入empCode")
	public R workQuit(@RequestParam("empCode") String empCode,@RequestParam("leaveDate") String leaveDate,@RequestParam("handover") Integer handover,BladeUser bladeUser) {
		EmployeeBasicInfo detail = employeeBasicInfoService.getOne(Condition.getQueryWrapper(new EmployeeBasicInfo())
				.lambda().eq(EmployeeBasicInfo::getEmpCode, empCode));
		detail.setLeaveingDate(LocalDate.parse(leaveDate));
		detail.setHandover(handover);
		detail.setLastUpdate(LocalDateTime.now());
		detail.setFinalBy(bladeUser.getNickName());
		detail.setWorkingState(2);
		SoaEmpDTO soaEmpDTO = new SoaEmpDTO();
        soaEmpDTO.setCpCode(detail.getCpCode());
		soaEmpDTO.setIdCard(detail.getIdCode());
		soaEmpDTO.setPhone(detail.getPhoneNo());
		soaEmpDTO.setSoaCode(detail.getSoaCode());
		soaEmpDTO.setSex(detail.getSex());
		soaEmpDTO.setName(detail.getName());
		DepartmentJob departmentJob = departmentJobService.getOne(Condition.getQueryWrapper(new DepartmentJob()).lambda().eq(DepartmentJob::getJobCode, detail.getJobCode()));
		soaEmpDTO.setJob(departmentJob.getJobType());
		soaEmpDTO.setStringStatus(SoaEmpDTO.DictCode.LZ.getCode());
		R r =  employeeInfoAggregationService.updateToSoa(soaEmpDTO, bladeUser);
		if (r.getCode()==0){
			employeeBasicInfoService.updateById(detail);
			return R.status(true);
		}else {
			return R.fail(r.getMsg());
		}
	}

	/**
	 * 调岗登记
	 */
	@GetMapping("/jobChange")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "确认提交调岗", notes = "传入empCode")
	public R jobChange(@RequestParam("empCode") String empCode,@RequestParam("dpmentCode") String dpmentCode,@RequestParam("jobCode") String jobCode,BladeUser bladeUser) {
		EmployeeBasicInfo detail = employeeBasicInfoService.getOne(Condition.getQueryWrapper(new EmployeeBasicInfo())
				.lambda().eq(EmployeeBasicInfo::getEmpCode, empCode));
		detail.setDpmentCode(dpmentCode);
		detail.setJobCode(jobCode);
		detail.setLastUpdate(LocalDateTime.now());
		detail.setFinalBy(bladeUser.getNickName());
		SoaEmpDTO soaEmpDTO = new SoaEmpDTO();
        soaEmpDTO.setCpCode(detail.getCpCode());
		soaEmpDTO.setIdCard(detail.getIdCode());
		soaEmpDTO.setPhone(detail.getPhoneNo());
		soaEmpDTO.setSoaCode(detail.getSoaCode());
		soaEmpDTO.setSex(detail.getSex());
		soaEmpDTO.setName(detail.getName());
		DepartmentJob departmentJob = departmentJobService.getOne(Condition.getQueryWrapper(new DepartmentJob()).lambda().eq(DepartmentJob::getJobCode, detail.getJobCode()));
		soaEmpDTO.setJob(departmentJob.getJobType());
		soaEmpDTO.setStringStatus(SoaEmpDTO.DictCode.LZ.getCode());
		R r =  employeeInfoAggregationService.updateToSoa(soaEmpDTO, bladeUser);
		if (r.getCode()==0){
			employeeBasicInfoService.updateById(detail);
			return R.status(true);
		}else {
			return R.fail(r.getMsg());
		}
	}


	/**
	 * 调岗登记 - 新部门 - 下拉框
	 * @param bladeUser
	 * @return
	 */
	@GetMapping("/getDpments")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "新部门", notes = "下拉框")
	public R<List<Map<String,Object>>> getDpments(BladeUser bladeUser) {
		List<Map<String, Object>> listMapResult = new ArrayList<>();
		List<DepartmentInfo> list = departmentInfoService.list(Condition.getQueryWrapper(new DepartmentInfo()).lambda().eq(DepartmentInfo::getCpCode, bladeUser.getDeptId()));
		if (list==null){
			return null;
		}
		for (DepartmentInfo vo:list) {
			Map<String,Object> map = new HashMap<>();
			map.put("dpmentCode", vo.getDpmentCode());
			map.put("dpmentName", vo.getDpmentName());
			listMapResult.add(map);
		}
		return R.data(listMapResult);
	}
	/**
	 * 调岗登记 - 新岗位 - 下拉框
	 * @param bladeUser
	 * @return
	 */
	@GetMapping("/getjobs")
	@ApiOperationSupport(order = 14)
	@ApiOperation(value = "新岗位", notes = "下拉框")
	public R<List<Map<String,Object>>> getjobs(@RequestParam("dpmentCode") String dpmentCode,@RequestParam("jobType") String jobType,BladeUser bladeUser) {
		List<DepartmentJob> list = departmentJobService.list(Condition.getQueryWrapper(new DepartmentJob()).lambda()
					.eq(DepartmentJob::getCpCode, bladeUser.getDeptId())
					.eq(!StringUtil.isEmpty(dpmentCode),DepartmentJob::getDpmentCode, dpmentCode)
					.eq(!jobType.isEmpty(), DepartmentJob::getJobType, jobType));
		if (list==null){
			return null;
		}
		List<Map<String, Object>> listMapResult = new ArrayList<>();
		for (DepartmentJob vo:list) {
			Map<String,Object> map = new HashMap<>();
			map.put("jobCode", vo.getJobCode());
			map.put("jobName", vo.getJobName());
			map.put("jobLevel", vo.getJobLevel());
			listMapResult.add(map);
		}
		return R.data(listMapResult);
	}

	/**
	 * 确认提交转正
	 */
	@GetMapping("/workPositive")
	@ApiOperationSupport(order = 15)
	@ApiOperation(value = "确认提交离职", notes = "传入empCode")
	public R workPositive(@RequestParam("empCode") String empCode,@RequestParam("positiveDate") String positiveDate,BladeUser bladeUser) {
		EmployeeBasicInfo detail = employeeBasicInfoService.getOne(Condition.getQueryWrapper(new EmployeeBasicInfo())
				.lambda().eq(EmployeeBasicInfo::getEmpCode, empCode));
		detail.setPositiveDate(LocalDate.parse(positiveDate));
		detail.setLastUpdate(LocalDateTime.now());
		detail.setFinalBy(bladeUser.getNickName());
		detail.setWorkingState(1);
		SoaEmpDTO soaEmpDTO = new SoaEmpDTO();
        soaEmpDTO.setCpCode(detail.getCpCode());
		soaEmpDTO.setIdCard(detail.getIdCode());
		soaEmpDTO.setPhone(detail.getPhoneNo());
		soaEmpDTO.setSoaCode(detail.getSoaCode());
		soaEmpDTO.setSex(detail.getSex());
		soaEmpDTO.setName(detail.getName());
		DepartmentJob departmentJob = departmentJobService.getOne(Condition.getQueryWrapper(new DepartmentJob()).lambda().eq(DepartmentJob::getJobCode, detail.getJobCode()));
		soaEmpDTO.setJob(departmentJob.getJobType());
		soaEmpDTO.setStringStatus(SoaEmpDTO.DictCode.LZ.getCode());
		R r =  employeeInfoAggregationService.updateToSoa(soaEmpDTO, bladeUser);
		if (r.getCode()==0){
			employeeBasicInfoService.updateById(detail);
			return R.status(true);
		}else {
			return R.fail(r.getMsg());
		}
	}


	/**
	 * 转正登记 - 姓名 - 下拉框
	 * @param bladeUser
	 * @return
	 */
	@GetMapping("/getPositives")
	@ApiOperationSupport(order = 16)
	@ApiOperation(value = "转正登记", notes = "下拉框")
	public R<List<Map<String,Object>>> getPositives(BladeUser bladeUser) {
		List<Map<String, Object>> listMapResult = new ArrayList<>();
		List<EmployeeBasicInfo> list = employeeBasicInfoService.list(Condition.getQueryWrapper(new EmployeeBasicInfo()).lambda().eq(EmployeeBasicInfo::getCpCode, bladeUser.getDeptId())
		.eq(EmployeeBasicInfo::getWorkingState, 0));
		if (list==null){
			return null;
		}
		for (EmployeeBasicInfo vo:list) {
			Map<String,Object> map = new HashMap<>();
			map.put("empCode", vo.getEmpCode());
			map.put("name", vo.getName());
			listMapResult.add(map);
		}
		return R.data(listMapResult);
	}


}
