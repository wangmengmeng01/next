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

import com.yundagalaxy.management.entity.SoaEmp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * soa账号表视图实体类
 *
 * @author dongfeng
 * @since 2019-11-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SoaEmpVO对象", description = "soa账号表")
public class SoaEmpVO extends SoaEmp {
	private static final long serialVersionUID = 1L;
	/**
	 * 网点编码
	 */
	@ApiModelProperty(value = "账号状态")
	private String statusValue;
	/**
	 * 员工工号
	 */
	@ApiModelProperty(value = "员工工号")
	private String empCode;
	/**
	 * 客户编码
	 */
	@ApiModelProperty(value = "客户编码")
	private String custCode;
	/**
	 * 客户名称
	 */
	@ApiModelProperty(value = "客户名称")
	private String custName;
	/**
	 * 客户名称
	 */
	@ApiModelProperty(value = "证件类型")
	private Integer idType;

}
