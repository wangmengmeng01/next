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
package com.yundagalaxy.management.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 员工薪酬信息表实体类
 *
 * @author BladeX
 * @since 2019-10-18
 */
@Data
@TableName("pc_employee_salary_info")
@ApiModel(value = "EmployeeSalaryInfo对象", description = "员工薪酬信息表")
public class EmployeeSalaryInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 员工薪酬信息表主键
	*/
		@JsonSerialize(using = ToStringSerializer.class)
		@ApiModelProperty(value = "员工薪酬信息表主键")
		@TableId(
				value = "emps_id",
				type = IdType.ID_WORKER
		)
		private Long empsId;
	/**
	* 员工编码
	*/
		@ApiModelProperty(value = "员工编码")
		private String empCode;
	/**
	* 银行卡号
	*/
		@ApiModelProperty(value = "银行卡号")
		private String bankCardNo;
	/**
	* 支付宝账号
	*/
		@ApiModelProperty(value = "支付宝账号")
		private String alipayAccount;
	/**
	* 基本工资
	*/
		@ApiModelProperty(value = "基本工资")
		private BigDecimal basicSalary;
	/**
	* 岗位工资
	*/
		@ApiModelProperty(value = "岗位工资")
		private BigDecimal postSalary;
	/**
	* 试用期工资折扣方式(岗位工资扣减/比例)
	*/
		@ApiModelProperty(value = "试用期工资折扣方式(岗位工资扣减/比例)")
		private Integer salaryType;
	/**
	* 基本工资扣减/比例
	*/
		@ApiModelProperty(value = "基本工资扣减/比例")
		private BigDecimal basicSalaryDis;
	/**
	* 岗位工资扣减/比例
	*/
		@ApiModelProperty(value = "岗位工资扣减/比例")
		private BigDecimal postSalaryDis;
	/**
	* 计件工资
	*/
		@ApiModelProperty(value = "计件工资")
		private BigDecimal pieceSalary;
	/**
	* 其他工资
	*/
		@ApiModelProperty(value = "其他工资")
		private BigDecimal otherSalary;
	/**
	* 绩效工资
	*/
		@ApiModelProperty(value = "绩效工资")
		private BigDecimal meritSalary;
	/**
	* 岗位津贴
	*/
		@ApiModelProperty(value = "岗位津贴")
		private BigDecimal jobAllowance;
	/**
	* 时效奖
	*/
		@ApiModelProperty(value = "时效奖")
		private BigDecimal effBonus;
	/**
	* 全勤奖
	*/
		@ApiModelProperty(value = "全勤奖")
		private BigDecimal attBonus;
	/**
	* 职鉴津贴
	*/
		@ApiModelProperty(value = "职鉴津贴")
		private BigDecimal expCcieAllo;
	/**
	* 话费补贴
	*/
		@ApiModelProperty(value = "话费补贴")
		private BigDecimal tphBillAllo;
	/**
	* 交通补贴
	*/
		@ApiModelProperty(value = "交通补贴")
		private BigDecimal travelAllo;
	/**
	* 高温补贴
	*/
		@ApiModelProperty(value = "高温补贴")
		private BigDecimal highTemplAllo;
	/**
	* 餐补
	*/
		@ApiModelProperty(value = "餐补")
		private BigDecimal mealSubsidy;
	/**
	* 房补
	*/
		@ApiModelProperty(value = "房补")
		private BigDecimal housingSubsidy;
	/**
	* 里程补贴
	*/
		@ApiModelProperty(value = "里程补贴")
		private BigDecimal mileageAllo;
	/**
	* 工资发放方式(现金、银行卡、支付宝、预付款)
	*/
		@ApiModelProperty(value = "工资发放方式(现金、银行卡、支付宝、预付款)")
		private Integer payoffModel;
	/**
	* 是否缴纳社保
	*/
		@ApiModelProperty(value = "是否缴纳社保")
		private Integer socialInsur;
	/**
	* 社保扣减
	*/
		@ApiModelProperty(value = "社保扣减")
		private BigDecimal socialInsurDed;
	/**
	* 是否缴纳公积金
	*/
		@ApiModelProperty(value = "是否缴纳公积金")
		private Integer accumFund;
	/**
	* 公积金扣减
	*/
		@ApiModelProperty(value = "公积金扣减")
		private BigDecimal accumFundDed;
	/**
	* 是否缴纳个税
	*/
		@ApiModelProperty(value = "是否缴纳个税")
		private Integer tax;
	/**
	* 扣税基数
	*/
		@ApiModelProperty(value = "扣税基数")
		private BigDecimal taxBase;


}
