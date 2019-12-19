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

import com.yundagalaxy.management.entity.EmployeeSalaryInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 员工薪酬信息表视图实体类
 *
 * @author BladeX
 * @since 2019-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "EmployeeSalaryInfoVO对象", description = "员工薪酬信息表")
public class EmployeeSalaryInfoVO extends EmployeeSalaryInfo {
	private static final long serialVersionUID = 1L;

	//试用期工资折扣方式(岗位工资扣减/比例)
	private String salarytypeDictValue;

	//工资发放方式(现金、银行卡、支付宝、预付款)
	private String payoffModelDictValue;

	//是否缴纳社保
	private String socialInsurDictValue;

	//是否缴纳公积金
	private String accumFundDictValue;

	//是否缴纳个税
	private String taxDictValue;

}
