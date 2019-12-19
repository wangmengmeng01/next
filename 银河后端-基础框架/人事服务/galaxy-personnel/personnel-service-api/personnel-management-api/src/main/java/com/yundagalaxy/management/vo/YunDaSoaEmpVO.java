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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 韵达soa
 *
 * @author dongfeng
 * @since 2019-11-08
 */
@Data
@ApiModel(value = "YunDaSoaEmpVO对象", description = "YunDaSoaEmpVO对象")
public class YunDaSoaEmpVO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * @网点编码@身份证号@手机号码@密钥
	 * MD5加密
	 */
	@ApiModelProperty(value = "签名")
	private String SIGN;
	/**
	 * 6位纯数字的网点编码
	 */
	@ApiModelProperty(value = "网点编码")
	private String WCODE;
	/**
	 * （新增用户非必填）唯一，不可重复，网点代码+002开始递增
	 */
	@ApiModelProperty(value = "员工工号")
	private String EMPID;
	/**
	 * 0或者1（0：男，1：女）
	 */
	@ApiModelProperty(value = "性别")
	private String GENDER;
	/**
	 * 唯一，不可重复
	 */
	@ApiModelProperty(value = "手机号码")
	private String MOBILENO;
	/**
	 * 电话号码
	 */
	@ApiModelProperty(value = "电话号码")
	private String PHONENO;
	/**
	 * 姓名
	 */
	@ApiModelProperty(value = "姓名")
	private String EMPNAME;
	/**
	 * 身份证号
	 */
	@ApiModelProperty(value = "身份证号")
	private String CARDNO;
	/**
	 * 岗位
	 */
	@ApiModelProperty(value = "岗位")
	private String POST;
	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	private String STATUS;
	/**
	 * 流水号
	 */
	@ApiModelProperty(value = "流水号")
	private String SERIALNUM;

}
