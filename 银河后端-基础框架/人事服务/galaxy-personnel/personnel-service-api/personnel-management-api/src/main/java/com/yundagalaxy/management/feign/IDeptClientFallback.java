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
import org.springframework.stereotype.Component;

/**
 * Feign失败配置
 *
 * @author Chill
 */
@Component
public class IDeptClientFallback implements IDeptClient {


	@Override
	public R<DepartmentInfo> getDept(String id) {
		return R.fail("获取数据失败");
	}

	@Override
	public R<String> getCpName(Integer cpCode) {
		return R.fail("获取数据失败");
	}

}
