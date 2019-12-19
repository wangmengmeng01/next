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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 非员工基本信息表实体类
 *
 * @author dongfeng
 * @since 2019-10-21
 */
@Data
@TableName("pc_tmp_employee_info")
@ApiModel(value = "TmpEmployeeInfo对象", description = "非员工基本信息表")
public class TmpEmployeeInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 主键
	*/
	@ApiModelProperty(value = "主键tmp_emp_id")
	@TableId(
			value = "tmp_emp_id",
			type = IdType.ID_WORKER
	)
	private Long tmpEmpId;
	/**
	* 非员工编码(LE00000001)
	*/
	@ApiModelProperty(value = "非员工编码(LE00000001)")
	private String tmpEmpCode;
	/**
	* 真实姓名
	*/
	@ApiModelProperty(value = "真实姓名")
	private String name;
	/**
	* 性别
	*/
	@ApiModelProperty(value = "性别")
	private Integer sex;
	/**
	* 客户，临时工，外派人员
	*/
	@ApiModelProperty(value = "客户，临时工，外派人员")
	private Integer accountNo;
	/**
	* 外包客服、历史代派员、历史操作员、其他
	*/
	@ApiModelProperty(value = "外包客服、历史代派员、历史操作员、其他")
	private Integer accountType;
	/**
	* 证件类型(①大陆居民身份证（默认）②港澳台身份证③护照④外籍驾驶证⑤外籍身份证)
	*/
	@ApiModelProperty(value = "证件类型(①大陆居民身份证（默认）②港澳台身份证③护照④外籍驾驶证⑤外籍身份证)")
	private Integer idType;
	/**
	* 证件号码
	*/
	@ApiModelProperty(value = "证件号码")
	private String idCode;
	/**
	* 手机号码
	*/
	@ApiModelProperty(value = "手机号码")
	private String phoneNo;
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
	* 入职日期
	*/
	@ApiModelProperty(value = "入职日期")
	private String hiredate;
	/**
	* 在职状态（离职，试用，正式）
	*/
	@ApiModelProperty(value = "在职状态（离职，试用，正式）")
	private Integer workingState;
	/**
	* 创建时间
	*/
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;
	/**
	* 创建人
	*/
	@ApiModelProperty(value = "创建人")
	private String createBy;
	/**
	* 更新时间
	*/
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime lastUpdate;
	/**
	* 更新人
	*/
	@ApiModelProperty(value = "更新人")
	private String finalOperator;
	/**
	* 删除标志
	*/
	@ApiModelProperty(value = "删除标志")
	private Integer delFlag;
	/**
	* 部门所属公司编码，即网点编码
	*/
	@ApiModelProperty(value = "部门所属公司编码，即网点编码")
	private Integer cpCode;
	/**
	* soa账号
	*/
	@ApiModelProperty(value = "soa账号")
	private String soaCode;
	/**
	* 离职日期
	*/
	@ApiModelProperty(value = "离职日期")
	private LocalDate leaveingDate;
	/**
	* 是否交接完毕（0默认，1是，2否）
	*/
	@ApiModelProperty(value = "是否交接完毕（0默认，1是，2否）")
	private Integer handover;


}
