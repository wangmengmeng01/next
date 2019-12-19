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
package com.yundagalaxy.management.feign;

import com.yundagalaxy.management.entity.DepartmentInfo;
import org.springblade.core.tool.api.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign接口类
 *
 * @author Chill
 */
@FeignClient(
	value = "personnel",
	fallback = IDeptClientFallback.class
)
public interface IDeptClient {

	String API_PREFIX = "/client";
	String DEPT = API_PREFIX + "/dept";
	String CP_NAME = API_PREFIX + "/cpName";



	/**
	 * 获取部门
	 *
	 * @param id 主键
	 * @return Dept
	 */
	@GetMapping(DEPT)
	R<DepartmentInfo> getDept(@RequestParam("id") String id);
	/**
	 * 获取网点名称
	 *
	 * @param
	 * @return
	 */
	@GetMapping(CP_NAME)
	R<String> getCpName(@RequestParam("cpCode") Integer cpCode);



}
