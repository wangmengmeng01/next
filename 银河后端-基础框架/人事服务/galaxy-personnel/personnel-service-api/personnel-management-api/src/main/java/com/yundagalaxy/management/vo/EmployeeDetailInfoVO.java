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

import com.yundagalaxy.management.entity.EmployeeDetailInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 员工详细信息表视图实体类
 *
 * @author BladeX
 * @since 2019-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "EmployeeDetailInfoVO对象", description = "员工详细信息表")
public class EmployeeDetailInfoVO extends EmployeeDetailInfo {
	private static final long serialVersionUID = 1L;

	//婚姻状态
	private String maritalStatusDictValue;
	//户口性质
	private String householdTypeDictValue;
	//快递资格证等级(高级、中级、初级、初中级、中高级、初中高级、无)
	private String expCcieLevelDictValue;

}
