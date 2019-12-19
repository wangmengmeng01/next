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
package com.yundagalaxy.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yundagalaxy.management.entity.DepartmentInfo;
import com.yundagalaxy.management.vo.DepartmentInfoVO;
import org.springblade.core.mp.base.BaseService;
import org.springblade.core.secure.BladeUser;

import java.util.List;
import java.util.Map;

/**
 * 部门岗位表 服务类
 *
 * @author dongfeng
 * @since 2019-10-16
 */
public interface IDepartmentInfoService extends IService<DepartmentInfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param departmentInfo
	 * @return
	 */
	IPage<DepartmentInfoVO> selectDepartmentInfoPage(IPage<DepartmentInfoVO> page, DepartmentInfoVO departmentInfo);


	/**
	 * 树形结构
	 *
	 * @param
	 * @return
	 */
	List<DepartmentInfoVO> tree(Map map);
	/**
	 * 新增部门
	 *
	 * @param departmentInfo
	 * @return
	 */
	boolean saveDepartmentInfo(DepartmentInfo departmentInfo, BladeUser bladeUser);
	/**
	 * 修改部门
	 *
	 * @param departmentInfo
	 * @return
	 */
	boolean updateDepartmentInfo(DepartmentInfo departmentInfo, BladeUser bladeUser);
	/**
	 * 批量删除部门
	 *
	 * @param dpmentIds
	 * @return
	 */
	boolean deleteDepartmentInfo(List<Long> dpmentIds, BladeUser bladeUser);
	/**
	 * 获取最大编号
	 *
	 * @param
	 * @return
	 */
	String getMaxDpmentCode();

	/**
	 * 树形结构
	 *
	 * @param
	 * @return
	 */
	List<DepartmentInfoVO> getList(DepartmentInfo departmentInfo);
	/**
	 * 加锁
	 *
	 * @param
	 * @return
	 */
	List<DepartmentInfoVO> getLockList(DepartmentInfo departmentInfo);

	/**
	 * 查询网点名称
	 *
	 * @param
	 * @return
	 */
	String getYdserverCpName(Integer cpCode);

	/**
	 * 组织架构管理
	 *
	 * @param
	 * @return
	 */
	Map<String,Object> getStructure(Integer cpCode, BladeUser bladeUser);
	/**
	 *
	 *
	 * @param
	 * @return
	 */
	String getDpInfoUrl();

}
