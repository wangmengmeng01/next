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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 员工基本信息表实体类
 *
 * @author BladeX
 * @since 2019-10-18
 */
@Data
@TableName("pc_employee_basic_info")
@ApiModel(value = "EmployeeBasicInfo对象", description = "员工基本信息表")
public class EmployeeBasicInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 主键
	*/
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "主键")
	@TableId(
			value = "emp_id",
			type = IdType.ID_WORKER
	)
	private Long empId;
	/**
	* 员工编码
	*/
	@ApiModelProperty(value = "员工编码")
	private String empCode;
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
	* 年龄
	*/
	@ApiModelProperty(value = "年龄")
	private Integer age;
	/**
	* 证件类型
	*/
	@ApiModelProperty(value = "证件类型")
	private Integer idType;
	/**
	* 证件号码
	*/
	@ApiModelProperty(value = "证件号码")
	private String idCode;
	/**
	* 出身日期
	*/
	@ApiModelProperty(value = "出身日期")
	private LocalDate birthday;
	/**
	* 籍贯
	*/
	@ApiModelProperty(value = "籍贯")
	private String nativePlace;
	/**
	* 手机号码
	*/
	@ApiModelProperty(value = "手机号码")
	private String phoneNo;
	/**
	* 民族
	*/
	@ApiModelProperty(value = "民族")
	private Integer nation;
	/**
	* 学历
	*/
	@ApiModelProperty(value = "学历")
	private Integer education;
	/**
	* 毕业学校
	*/
	@ApiModelProperty(value = "毕业学校")
	private String graduateSchool;
	/**
	* 政治面貌
	*/
	@ApiModelProperty(value = "政治面貌")
	private Integer politicsStatus;
	/**
	* 入职日期
	*/
	@ApiModelProperty(value = "入职日期")
	private LocalDate hiredate;
	/**
	* 专业
	*/
	@ApiModelProperty(value = "专业")
	private String major;
	/**
	* 试用期期限
	*/
	@ApiModelProperty(value = "试用期期限")
	private Integer period;
	/**
	* 在职状态（离职，试用，正式）
	*/
	@ApiModelProperty(value = "在职状态（离职，试用，正式）")
	private Integer workingState;
	/**
	* 部门编码
	*/
	@ApiModelProperty(value = "部门编码")
	private String dpmentCode;
	/**
	* 岗位编码
	*/
	@ApiModelProperty(value = "岗位编码")
	private String jobCode;
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
	private String finalBy;
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
	* soa工号/业务员工号
	*/
	@ApiModelProperty(value = "soa工号/业务员工号")
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
	/**
	* 转正日期
	*/
	@ApiModelProperty(value = "转正日期")
	private LocalDate positiveDate;


}
