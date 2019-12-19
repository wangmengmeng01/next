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

import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import com.yundagalaxy.management.vo.EmployeeBasicInfoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 员工基本信息表 Mapper 接口
 *
 * @author BladeX
 * @since 2019-10-18
 */
public interface EmployeeBasicInfoMapper extends BaseMapper<EmployeeBasicInfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param employeeBasicInfo
	 * @return
	 */
	List<EmployeeBasicInfoVO> selectEmployeeBasicInfoPage(IPage page, EmployeeBasicInfoVO employeeBasicInfo);
	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param rq
	 * @return
	 */
	List<EmployeeBasicInfoVO> selectEmpBasicInfoPage(@Param("page") IPage page,@Param("rq") EmployeeBasicInfoVO rq);

	/**
	 * 根据员工编码更新时间
	 * @param empCode
	 * @param localDateTime
	 * @param updateBy
	 */
	void updateDateTimeByEmpCode(@Param("empCode") String empCode,@Param("localDateTime") LocalDateTime localDateTime,@Param("updateBy") String updateBy);
	/**
	 * 查询最大编号
	 *
	 * @param
	 * @return
	 */
	String findMaxEmpCode();
	/**
	 * 统计员工数量
	 *
	 * @param
	 * @return
	 */
	Long selectEmployeeCount(@Param("map") Map<String,Object> map);
}
