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
package com.yundagalaxy.management.vo;

import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 员工基本信息表视图实体类
 *
 * @author BladeX
 * @since 2019-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "EmployeeBasicInfoVO对象", description = "员工基本信息表")
public class EmployeeBasicInfoVO extends EmployeeBasicInfo {
	private static final long serialVersionUID = 1L;

	//性别
	private String sexDictValue;
	//证件
	private String idTypeDictValue;
	//民族
	private String nationDictValue;
	//学历
	private String educationDictValue;
	//政治面貌
	private String politicsStatusDictValue;
	//部门名称
	private String dpmentName;
	//岗位名称
	private String jobName;
	//岗位类型
	private String jobTypeDictValue;
	//类型
	private Integer jobType;
	//级别
	private Integer jobLevel;
	//岗位级别
	private String jobLevelDictValue;
	//工齡 - 年
	private Integer workYear;
	//工龄 - 月
	private Integer workMouth;
	//经营模式
	private Integer businessModel;
	//经营模式
	private String businessModelDictValue;
	//在职状态
	private String workingStateDictValue;

	private Integer startWorkingState;
	private Integer endWorkingState;
	private Integer cpCode;

}
