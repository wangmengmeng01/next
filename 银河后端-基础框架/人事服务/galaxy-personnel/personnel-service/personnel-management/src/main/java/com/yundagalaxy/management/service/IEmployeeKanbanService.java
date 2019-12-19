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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import com.yundagalaxy.management.vo.EmployeeBasicInfoVO;
import org.springblade.core.tool.api.R;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author feng.dong
 * @since 2019-10-18
 */
public interface IEmployeeKanbanService{
	/**
	 *
	 *
	 * @param map
	 * @return
	 */
	Map<String,Long> getProfile(Map<String,Object> map);
	/**
	 *
	 *
	 * @param map
	 * @return
	 */
	Map<String,Object> getAnalysis(Map<String,Object> map);



	List<JSONObject> positionTypeSurveyList(Map<String,Object> pt);

	JSONObject positionTypeSurvey(Map<String,Object> pt);

	JSONObject strPositionTypeSurvey(Map<String,Object> pt);
}
