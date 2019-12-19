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

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * soa账号表实体类
 *
 * @author dongfeng
 * @since 2019-11-08
 */
@Data
@TableName("pc_soa_emp")
@ApiModel(value = "SoaEmp对象", description = "soa账号表")
public class SoaEmp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 网点编码
	*/
		@ApiModelProperty(value = "网点编码")
		private Integer cpCode;
	/**
	* 员工工号
	*/
		@ApiModelProperty(value = "员工工号")
		private String soaCode;
	/**
	* 性别
	*/
		@ApiModelProperty(value = "性别")
		private Integer sex;
	/**
	* 电话号码
	*/
		@ApiModelProperty(value = "电话号码")
		private String telephone;
	/**
	* 手机号码
	*/
		@ApiModelProperty(value = "手机号码")
		private String phone;
	/**
	* 姓名
	*/
		@ApiModelProperty(value = "姓名")
		private String name;
	/**
	* 身份证号
	*/
		@ApiModelProperty(value = "身份证号")
		private String idCard;
	/**
	* 状态
	*/
		@ApiModelProperty(value = "状态")
		private Integer status;
	/**
	* 状态
	*/
		@ApiModelProperty(value = "原SOA岗位")
		private Integer job;
	/**
	* 未处理，已处理员工入职，已处理非员工入职
	*/
		@ApiModelProperty(value = "未处理，已处理员工入职，已处理非员工入职")
		private Integer joinStatus;
	/**
	* 创建时间
	*/
		@ApiModelProperty(value = "创建时间")
		private LocalDateTime createTime;
	/**
	* 更新时间
	*/
		@ApiModelProperty(value = "更新时间")
		private LocalDateTime lastUpdate;


}
