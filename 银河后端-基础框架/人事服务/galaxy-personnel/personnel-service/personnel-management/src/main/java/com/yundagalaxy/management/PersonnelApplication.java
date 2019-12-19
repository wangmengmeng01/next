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
package com.yundagalaxy.management;

import org.springblade.core.cloud.feign.EnableBladeFeign;
import org.springblade.core.launch.BladeApplication;
import org.springframework.cloud.client.SpringCloudApplication;

import static com.yundagalaxy.common.constant.AppConstant.APPLICATION_PERSONNEL_NAME;

/**
 * 系统模块启动器
 * @author Chill
 */
@EnableBladeFeign(basePackages = {"com.yundagalaxy"})
//原生支持方法如下
//@EnableFeignClients({"org.springblade","com.yundagalaxy"})
@SpringCloudApplication
public class PersonnelApplication {

	public static void main(String[] args) {
		BladeApplication.run(APPLICATION_PERSONNEL_NAME, PersonnelApplication.class, args);
	}

}

