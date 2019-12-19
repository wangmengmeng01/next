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
package com.yundagalaxy.common.constant;

/**
 * 编码常量
 *
 * @author feng.dong
 */
public interface CodeConstant {

	/**
	 * pc_department_info表编码
	 */
	String PRE_GROUP_DPMENT_CODE = "JD";
	/**
	 *redis key
	 */
	String REDIS_MAX_DPMENT_CODE_KEY = "REDIS_MAX_DPMENT_CODE_KEY";


	String DPMENT_CODE_LOCK_KEY = "DPMENT_CODE_INC_LOCK";

	/**
	 *pc_department_job 表编码
	 */
	String PRE_GROUP_JOB_CODE = "JJ";
	/**
	 *redis key
	 */
	String REDIS_MAX_JOB_CODE_KEY = "REDIS_MAX_JOB_CODE_KEY";
	/**
	 *LOCK锁key
	 */
	String JOB_CODE_LOCK_KEY = "JOB_CODE_INC_LOCK";
	/**
	 *pc_employee_basic_info 表编码
	 */
	String PRE_GROUP_EMP_CODE = "JE";
	/**
	 *redis key
	 */
	String REDIS_MAX_EMP_CODE_KEY = "REDIS_MAX_EMP_CODE_KEY";

	String EMP_CODE_LOCK_KEY = "EMP_CODE_INC_LOCK";
	/**
	 *pc_tmp_employee_info 表编码
	 */
	String PRE_GROUP_TMP_EMP_CODE = "LE";
	/**
	 *redis key
	 */
	String REDIS_MAX_TMP_EMP_CODE_KEY = "REDIS_MAX_TMP_EMP_CODE_KEY";
	/**
	 *LOCK锁key
	 */
	String TMP_EMP_CODE_LOCK_KEY = "TMP_EMP_CODE_INC_LOCK";
	/**
	 *初始化值
	 */
	String INIT_NUMBER = "00000000";

	/**
	 *锁时间
	 */
	int LOCK_TIME = 5;


	/**
	 *部门初始化锁
	 */
	String IS_DEFAULT_LOCK_KEY = "IS_DEFAULT_LOCK_KEY";



}
