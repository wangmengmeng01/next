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
package com.yundagalaxy.management.wrapper;

import com.yundagalaxy.management.cache.DeptCache;
import com.yundagalaxy.management.entity.DepartmentInfo;
import com.yundagalaxy.management.node.ForestNodeMerger;
import com.yundagalaxy.management.node.INode;
import com.yundagalaxy.management.vo.DepartmentInfoVO;
import com.yundagalaxy.management.vo.StructureVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 包装类,返回视图层所需的字段
 *
 * @author Chill
 */
public class DeptWrapper extends BaseEntityWrapper<DepartmentInfo, DepartmentInfoVO> {

	public static DeptWrapper build() {
		return new DeptWrapper();
	}

	@Override
	public DepartmentInfoVO entityVO(DepartmentInfo departmentInfo) {
		DepartmentInfoVO departmentInfoVO = BeanUtil.copy(departmentInfo, DepartmentInfoVO.class);
		assert departmentInfoVO != null;
		if ("0".equals(departmentInfo.getParentDpmentCode())) {
			departmentInfoVO.setParentDpmentName(BladeConstant.TOP_PARENT_NAME);
		} else {
			DepartmentInfo parent = DeptCache.getDept(departmentInfo.getParentDpmentCode());
			departmentInfoVO.setParentDpmentName(parent.getDpmentName());
		}
		return departmentInfoVO;
	}


	public List<INode> listNodeVO(List<DepartmentInfoVO> list) {
		List<INode> collect = list.stream().map(dept -> {
			DepartmentInfoVO departmentInfoVO = BeanUtil.copy(dept, DepartmentInfoVO.class);
			Objects.requireNonNull(departmentInfoVO);
			return departmentInfoVO;
		}).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}
	public List<INode> listNodeTwoVO(List<StructureVO> list) {
		List<INode> collect = list.stream().map(structureVO -> {
			Objects.requireNonNull(structureVO);
			return structureVO;
		}).collect(Collectors.toList());
		return ForestNodeMerger.merge(collect);
	}



}
