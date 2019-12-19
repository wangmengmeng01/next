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
package com.yundagalaxy.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yundagalaxy.management.entity.DepartmentInfo;
import com.yundagalaxy.management.vo.DepartmentInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 部门岗位表 Mapper 接口
 *
 * @author dongfeng
 * @since 2019-10-16
 */
public interface DepartmentInfoMapper extends BaseMapper<DepartmentInfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param
	 * @return
	 */
	List<DepartmentInfoVO> selectDepartmentInfoPage(@Param("page") IPage page, @Param("rq") DepartmentInfoVO rq);
	/**
	 * 获取树形节点
	 *
	 * @param map
	 * @return
	 */
	List<DepartmentInfoVO> tree(@Param("map") Map map);
	/**
	 * 新增部门
	 *
	 * @param
	 * @return
	 */
	int saveDepartmentInfo(DepartmentInfo departmentInfo);
	/**
	 * 修改部门
	 *
	 * @param
	 * @return
	 */
	int updateDepartmentInfo(DepartmentInfo departmentInfo);

	/**
	 * 查询最大编号
	 *
	 * @param
	 * @return
	 */
	String findMaxDpmentCode();


	/**
	 * 树形结构
	 *
	 * @param
	 * @return
	 */
	List<DepartmentInfoVO> selectDepartmentInfoList(@Param("rq") DepartmentInfo rq);
	/**
	 * 查询网点名称
	 *
	 * @param
	 * @return
	 */
	String findYdserverCpName(@Param("cpCode") Integer cpCode);
}
