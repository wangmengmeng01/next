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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yundagalaxy.management.entity.DepartmentInfo;
import com.yundagalaxy.management.service.IDepartmentInfoService;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 系统服务Feign实现类
 *
 * @author Chill
 */
@ApiIgnore
@RestController
@AllArgsConstructor
public class DepartmentClient implements IDeptClient {

	private IDepartmentInfoService departmentInfoService;



	@Override
	@GetMapping(DEPT)
	public R<DepartmentInfo> getDept(String id) {
		DepartmentInfo departmentInfo = departmentInfoService.getOne(new QueryWrapper<DepartmentInfo>().eq("dpment_code",id));
	    return R.data(departmentInfo);
	}
	@Override
	@GetMapping(CP_NAME)
	public R<String> getCpName(Integer cpCode) {
		return R.data(departmentInfoService.getYdserverCpName(cpCode));
	}




}
