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
package com.yundagalaxy.management.service.impl;

import com.yundagalaxy.common.utils.DateUtils;
import com.yundagalaxy.management.entity.EmployeeStatistics;
import com.yundagalaxy.management.vo.EmployeeStatisticsVO;
import com.yundagalaxy.management.mapper.EmployeeStatisticsMapper;
import com.yundagalaxy.management.service.IEmployeeStatisticsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.*;

/**
 * 人事流动趋势表 服务实现类
 *
 * @author feng.dong
 * @since 2019-10-24
 */
@Service
public class EmployeeStatisticsServiceImpl extends ServiceImpl<EmployeeStatisticsMapper, EmployeeStatistics> implements IEmployeeStatisticsService {

	@Override
	public IPage<EmployeeStatisticsVO> selectEmployeeStatisticsPage(IPage<EmployeeStatisticsVO> page, EmployeeStatisticsVO employeeStatistics) {
		return page.setRecords(baseMapper.selectEmployeeStatisticsPage(page, employeeStatistics));
	}

	@Override
	public List<EmployeeStatisticsVO> selectEmployeeStatisticsAll(Map<String,Object> map){
		List<EmployeeStatisticsVO> lists = new ArrayList<>();
		Object timeTypeStr = map.get("timeType");
		Integer timeType = Integer.parseInt(timeTypeStr.toString());
		if(null==timeType){
        	timeType = 0;
		}
        Date nowTime = new Date();

        Date startTime = nowTime;
        Date endTime = nowTime;
        switch (timeType){
			case 0:
                startTime =  DateUtils.addMonths(nowTime,-1);
				break;
			case 1:
                startTime = DateUtils.addMonths(nowTime,-3);
				break;
			case 2:
                startTime = DateUtils.addMonths(nowTime,-6);
				break;
			case 3:
                startTime = DateUtils.addYears(nowTime,-1);
				break;
			default:
				break;
		}
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        try {
			lists = baseMapper.selectEmployeeStatisticsAll(map);
		}catch (Exception e){
        	e.printStackTrace();
        	log.error(e.getMessage());
		}
		return lists;
	}

}
