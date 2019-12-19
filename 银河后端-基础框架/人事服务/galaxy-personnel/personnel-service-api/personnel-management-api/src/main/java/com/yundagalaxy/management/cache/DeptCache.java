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
package com.yundagalaxy.management.cache;

import com.yundagalaxy.management.entity.DepartmentInfo;
import com.yundagalaxy.management.feign.IDeptClient;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.SpringUtil;

/**
 * 字典缓存工具类
 *
 * @author Chill
 */
public class DeptCache {

	private static final String DEPT_ID = "dept:id:";
	private static final String CP_NAME_ID = "dept:cpName:";
	private static final String DEPT_VALUE = "dept:value:";
	private static final String DEPT_LIST = "dept:list:";

	private static final String DEPT_CACHE = "personnel:dept";
	private static final String CP_NAME_CACHE = "personnel:cpName";
	private static final String SYS_CACHE = "blade:sys";
	private static IDeptClient deptClient;

	static {
		deptClient = SpringUtil.getBean(IDeptClient.class);
	}

	/**
	 * 获取字典实体
	 *
	 * @param id 主键
	 * @return
	 */
//	public static DepartmentInfo getById(String id) {
//		return CacheUtil.get(DEPT_CACHE, DEPT_ID, id, () -> {
//			R<DepartmentInfo> result = deptClient.getDept(id);
//			return result.getData();
//		});
//	}
	/**
	 * 获取部门
	 *
	 * @param id 主键
	 * @return
	 */
	public static DepartmentInfo getDept(String id) {
		return CacheUtil.get(DEPT_CACHE, DEPT_ID, id, () -> {
			R<DepartmentInfo> result = deptClient.getDept(id);
			return result.getData();
		});
	}
	/**
	 * 获取部门
	 *
	 * @param
	 * @return
	 */
	public static String getCpName(Integer cpCode) {
		return CacheUtil.get(CP_NAME_CACHE, CP_NAME_ID, cpCode, () -> {
			R<String> result = deptClient.getCpName(cpCode);
			return result.getData();
		});
	}



}
