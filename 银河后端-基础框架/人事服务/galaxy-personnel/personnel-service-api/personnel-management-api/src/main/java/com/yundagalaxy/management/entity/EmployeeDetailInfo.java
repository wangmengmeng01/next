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
 * 员工详细信息表实体类
 *
 * @author BladeX
 * @since 2019-10-18
 */
@Data
@TableName("pc_employee_detail_info")
@ApiModel(value = "EmployeeDetailInfo对象", description = "员工详细信息表")
public class EmployeeDetailInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 员工详细信息表主键
	*/
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "员工详细信息表主键")
	@TableId(
			value = "empd_id",
			type = IdType.ID_WORKER
	)
	private Long empdId;
	/**
	* 员工编码
	*/
	@ApiModelProperty(value = "员工编码")
	private String empCode;
	/**
	* 婚姻状态（未婚、已婚、丧偶、离婚、其他）
	*/
	@ApiModelProperty(value = "婚姻状态（未婚、已婚、丧偶、离婚、其他）")
	private Integer maritalStatus;
	/**
	* 户口性质(本地城镇、本地非城镇、外地城镇、外地非城镇)
	*/
	@ApiModelProperty(value = "户口性质(本地城镇、本地非城镇、外地城镇、外地非城镇)")
	private Integer householdType;
	/**
	* 户籍省
	*/
	@ApiModelProperty(value = "户籍省")
	private String householdProvince;
	/**
	* 户籍市
	*/
	@ApiModelProperty(value = "户籍市")
	private String householdCity;
	/**
	* 户籍区县
	*/
	@ApiModelProperty(value = "户籍区县")
	private String householdCounty;
	/**
	* 户籍详细地址
	*/
	@ApiModelProperty(value = "户籍详细地址")
	private String householdStreet;
	/**
	* 户籍省
	*/
	@ApiModelProperty(value = "户籍省")
	private String currentProvince;
	/**
	* 户籍市
	*/
	@ApiModelProperty(value = "户籍市")
	private String currentCity;
	/**
	* 户籍区县
	*/
	@ApiModelProperty(value = "户籍区县")
	private String currentCounty;
	/**
	* 户籍详细地址
	*/
	@ApiModelProperty(value = "户籍详细地址")
	private String currentStreet;
	/**
	* 紧急联系人
	*/
	@ApiModelProperty(value = "紧急联系人")
	private String emgContact;
	/**
	* 紧急联系人关系
	*/
	@ApiModelProperty(value = "紧急联系人关系")
	private String emgContactRlp;
	/**
	* 紧急联系人电话
	*/
	@ApiModelProperty(value = "紧急联系人电话")
	private String emgContactMobile;
	/**
	* 快递资格证等级(高级、中级、初级、初中级、中高级、初中高级、无)
	*/
	@ApiModelProperty(value = "快递资格证等级(高级、中级、初级、初中级、中高级、初中高级、无)")
	private Integer expCcieLevel;
	/**
	* 快递资格证编码
	*/
	@ApiModelProperty(value = "快递资格证编码")
	private String expCcieNo;
	/**
	* 技能与特长
	*/
	@ApiModelProperty(value = "技能与特长")
	private String skillsSpeciality;
	/**
	* 工作经历
	*/
	@ApiModelProperty(value = "工作经历")
	private String workExp;


}
