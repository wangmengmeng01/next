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

import com.yundagalaxy.management.entity.EmployeeStatistics;
import com.yundagalaxy.management.vo.EmployeeStatisticsVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 人事流动趋势表 Mapper 接口
 *
 * @author dongfeng
 * @since 2019-10-24
 */
public interface EmployeeStatisticsMapper extends BaseMapper<EmployeeStatistics> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param employeeStatistics
	 * @return
	 */
	List<EmployeeStatisticsVO> selectEmployeeStatisticsPage(IPage page, EmployeeStatisticsVO employeeStatistics);
	/**
	 * 自定义分页
	 *
	 * @param
	 * @param
	 * @return
	 */
	List<EmployeeStatisticsVO> selectEmployeeStatisticsAll(@Param("map") Map<String,Object> map);

}
