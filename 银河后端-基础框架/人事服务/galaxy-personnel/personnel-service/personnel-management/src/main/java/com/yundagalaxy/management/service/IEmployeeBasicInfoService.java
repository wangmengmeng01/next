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

import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import com.yundagalaxy.management.vo.EmployeeBasicInfoVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Map;

/**
 * 员工基本信息表 服务类
 *
 * @author BladeX
 * @since 2019-10-18
 */
public interface IEmployeeBasicInfoService extends IService<EmployeeBasicInfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param employeeBasicInfo
	 * @return
	 */
	IPage<EmployeeBasicInfoVO> selectEmployeeBasicInfoPage(IPage<EmployeeBasicInfoVO> page, EmployeeBasicInfoVO employeeBasicInfo);

	/**
	 * 更新 - 时间
	 * @param empCode
	 * @return
	 */
	boolean updateDateTimeByEmpCode(String empCode,String updateBy);
	/**
	 * 获取最大编号
	 *
	 * @param
	 * @return
	 */
	String getMaxEmpCode();
	/**
	 * 统计员工数量
	 *
	 * @param
	 * @return
	 */
	Long getEmployeeCount(Map<String,Object> map);
	/**
	 * 自定义查询分页
	 *
	 * @param
	 * @return
	 */

	IPage<EmployeeBasicInfoVO> selectEmpBasicInfoPage(IPage<EmployeeBasicInfoVO> page, EmployeeBasicInfoVO employeeBasicInfo);
}
