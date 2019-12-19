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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.yundagalaxy.management.entity.TmpEmployeeInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 非员工基本信息表视图实体类
 *
 * @author dongfeng
 * @since 2019-10-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TmpEmployeeInfoVO对象", description = "非员工基本信息表")
public class TmpEmployeeInfoVO extends TmpEmployeeInfo {
	private static final long serialVersionUID = 1L;



	@JsonSerialize(using = ToStringSerializer.class)
	private Long tmpEmpId;
	/**
	 * 账号角色
	 */
	@ApiModelProperty(value = "账号角色")
	private String accountNoValue;
	/**
	 * 角色类型
	 */
	@ApiModelProperty(value = "角色类型")
	private String accountTypeValue;
	/**
	 * 在职状态
	 */
	@ApiModelProperty(value = "在职状态")
	private String workingStateValue;
	/**
	 * 证件类型
	 */
	@ApiModelProperty(value = "证件类型")
	private String idTypeValue;
	/**
	 * 是否交接完毕
	 */
	@ApiModelProperty(value = "是否交接完毕")
	private String handoverValue;
	/**
	 * 是否交接完毕
	 */
	@ApiModelProperty(value = "网点名称")
	private String cpName;
	/**
	 * 员工工号
	 */
	@ApiModelProperty(value = "员工工号")
	private String soaCode;
	/**
	 * 员工维护状态
	 */
	@ApiModelProperty(value = "员工维护状态")
	private Integer joinStatus;
	/**
	 * 导出所有
	 */
	@ApiModelProperty(value = "导出所有")
	private String rowType;


}
