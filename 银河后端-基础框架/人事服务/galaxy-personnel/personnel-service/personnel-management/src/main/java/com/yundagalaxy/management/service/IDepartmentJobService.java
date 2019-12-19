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
import com.yundagalaxy.management.entity.DepartmentJob;
import com.yundagalaxy.management.vo.DepartmentJobVO;
import org.springblade.core.secure.BladeUser;

import java.util.List;

/**
 * 部门岗位表 服务类
 *
 * @author dongfeng
 * @since 2019-10-19
 */
public interface IDepartmentJobService extends IService<DepartmentJob> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param departmentJob
	 * @return
	 */
	IPage<DepartmentJobVO> selectDepartmentJobPage(IPage<DepartmentJobVO> page, DepartmentJobVO departmentJob);

	List<DepartmentJobVO> selectDepartmentJobList(DepartmentJob departmentJob);
	/**
	 * 获取最大编号
	 *
	 * @param
	 * @return
	 */
	String getMaxJobCode();
	/**
	 * 新增岗位
	 *
	 * @param
	 * @return
	 */
	boolean saveDepartmentJob(DepartmentJob departmentJob, BladeUser bladeUser);

	/**
	 * 批量删除岗位
	 *
	 * @param jobIds
	 * @return
	 */
	boolean deleteDepartmentJob(List<Long> jobIds,BladeUser bladeUser);
	/**
	 * 修改岗位
	 *
	 * @param departmentJob
	 * @return
	 */
	boolean updateDepartmentJob(DepartmentJob departmentJob, BladeUser bladeUser);

	String getDpJobUrl();
}
