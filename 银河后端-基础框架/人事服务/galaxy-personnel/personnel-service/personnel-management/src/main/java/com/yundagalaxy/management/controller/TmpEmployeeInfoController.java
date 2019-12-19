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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yundagalaxy.management.commnon.utils.DictUtil;
import com.yundagalaxy.management.entity.TmpEmployeeInfo;
import com.yundagalaxy.management.service.ITmpEmployeeInfoService;
import com.yundagalaxy.management.vo.IdCommonVO;
import com.yundagalaxy.management.vo.TmpEmployeeInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 非员工基本信息表 控制器
 *
 * @author dongfeng
 * @since 2019-10-21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/tmpEmployeeInfo")
@Api(value = "非员工基本信息表", tags = "非员工基本信息表接口")
public class TmpEmployeeInfoController extends BladeController {

	private ITmpEmployeeInfoService tmpEmployeeInfoService;

	private DictUtil dictUtil;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入tmpEmployeeInfo")
	public R detail(TmpEmployeeInfo tmpEmployeeInfo) {
		TmpEmployeeInfo detail = tmpEmployeeInfoService.getOne(Condition.getQueryWrapper(tmpEmployeeInfo));
		TmpEmployeeInfoVO vo = BeanUtil.copy(detail,TmpEmployeeInfoVO.class);
		if(null!=detail){
			String accountNoValue = dictUtil.getDictValue(DictUtil.DictCode.ACCOUNT_NO,vo.getAccountNo());
			vo.setAccountNoValue(accountNoValue);
			String accountTypeValue = dictUtil.getDictValue(DictUtil.DictCode.ACCOUNT_TYPE,vo.getAccountType());
			vo.setAccountTypeValue(accountTypeValue);
			String idTypeValue = dictUtil.getDictValue(DictUtil.DictCode.ID_TYPE,vo.getIdType());
			vo.setIdTypeValue(idTypeValue);
			String workingStateValue = dictUtil.getDictValue(DictUtil.DictCode.WORKING_STATE,vo.getWorkingState());
			vo.setWorkingStateValue(workingStateValue);
			String handoverValue = dictUtil.getDictValue(DictUtil.DictCode.HANDOVER,vo.getHandover());
			vo.setHandoverValue(handoverValue);
		}

		return R.data(vo);
	}

	/**
	 * 分页 非员工基本信息表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入tmpEmployeeInfo")
	public R<IPage<TmpEmployeeInfo>> list(TmpEmployeeInfo tmpEmployeeInfo, Query query, BladeUser bladeUser) {
        Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		tmpEmployeeInfo.setCpCode(cpCode);
		IPage<TmpEmployeeInfo> pages = tmpEmployeeInfoService.page(Condition.getPage(query), Condition.getQueryWrapper(tmpEmployeeInfo));
		return R.data(pages);
	}

	/**
	 * 自定义分页 非员工基本信息表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入tmpEmployeeInfo")
	public R page(TmpEmployeeInfoVO tmpEmployeeInfoVO, Query query, BladeUser bladeUser) {
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		if(null==tmpEmployeeInfoVO.getCpCode()){
            tmpEmployeeInfoVO.setCpCode(cpCode);
		}
		IPage<TmpEmployeeInfoVO> pages = new Page<>();
		if(StringUtils.isNotEmpty(tmpEmployeeInfoVO.getRowType())&&"all".equals(tmpEmployeeInfoVO.getRowType())){
			List<TmpEmployeeInfoVO>  list = tmpEmployeeInfoService.selectTmpEmployeeInfoList(tmpEmployeeInfoVO);
			pages.setRecords(list);
			pages.setTotal(list.size());
			return R.data(pages);
		}
		pages = tmpEmployeeInfoService.selectTmpEmployeeInfoPage(Condition.getPage(query), tmpEmployeeInfoVO);
		return R.data(pages);
	}

	/**
	 * 新增 非员工基本信息表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增非员工基本信息", notes = "传入tmpEmployeeInfo")
	public R save(@Valid @RequestBody TmpEmployeeInfo tmpEmployeeInfo, BladeUser bladeUser) {
		return R.status(tmpEmployeeInfoService.saveTmpEmployeeInfo(tmpEmployeeInfo,bladeUser));
	}

	/**
	 * 修改 非员工基本信息表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改非员工基本信息", notes = "传入tmpEmployeeInfo")
	public R update(@Valid @RequestBody TmpEmployeeInfo tmpEmployeeInfo, BladeUser bladeUser) {
		return R.status(tmpEmployeeInfoService.updateTmpEmployeeInfo(tmpEmployeeInfo,bladeUser));
	}

	/**
	 * 新增或修改 非员工基本信息表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入tmpEmployeeInfo")
	public R submit(@Valid @RequestBody TmpEmployeeInfo tmpEmployeeInfo) {
		return R.status(tmpEmployeeInfoService.saveOrUpdate(tmpEmployeeInfo));
	}

	


	/**
	 * 删除 部门岗位表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除非员工（批量）", notes = "tmpEmpIds")
	public R remove(@RequestBody IdCommonVO idCommonVO, BladeUser bladeUser) {

		if(StringUtils.isEmpty(idCommonVO.getTmpEmpIds())){
			return R.fail("参数tmpEmpIds不能为空");
		}

		return R.status(tmpEmployeeInfoService.deleteTmpEmployeeInfo(Func.toLongList(idCommonVO.getTmpEmpIds()),bladeUser));
	}

	/**
	 * 网点下用户(非员工) - 下拉框
	 * @param
	 * @return
	 */
	@GetMapping("/getTmpEmp")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "请选择员工（非员工）", notes = "下拉框")
	public R<List<Map<String,Object>>> getTmpEmp(BladeUser bladeUser) {
		List<Map<String,Object>>  lsMap = new ArrayList<>();
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		List<TmpEmployeeInfo> list = tmpEmployeeInfoService.list(Condition.getQueryWrapper(new TmpEmployeeInfo()).lambda().eq(TmpEmployeeInfo::getCpCode,cpCode ));
        if(null!=list && list.size()>0){
			for (TmpEmployeeInfo vo:list) {
				Map<String,Object> map = new HashMap<>();
				map.put("tmpEmpCode", vo.getTmpEmpCode());
				map.put("name", vo.getName());
				map.put("tmpEmpId", vo.getTmpEmpId().toString());
				lsMap.add(map);
			}
		}
		return R.data(lsMap);
	}
}
