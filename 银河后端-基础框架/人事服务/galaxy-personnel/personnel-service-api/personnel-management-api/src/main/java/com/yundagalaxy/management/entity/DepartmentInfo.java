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
import com.baomidou.mybatisplus.annotation.IdType;
import org.springblade.core.mp.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 部门岗位表实体类
 *
 * @author dongfeng
 * @since 2019-10-16
 */
@Data
@TableName("pc_department_info")
//@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "DepartmentInfo对象", description = "部门岗位表")
public class DepartmentInfo implements Serializable {

	private static final long serialVersionUID = 1L;


	@ApiModelProperty("主键id")
	@TableId(
			value = "dpment_id",
			type = IdType.ID_WORKER
	)
	private Long dpmentId;
	/**
	* 人事行政后勤、业务部、操作部、车队、财务部、客服部、经理室
	*/
	@ApiModelProperty(value = "人事行政后勤、业务部、操作部、车队、财务部、客服部、经理室")
	private String dpmentName;
	/**
	* 示例：YH00000001
	*/
//	@ApiModelProperty(value = "示例：YH00000001")
	private String dpmentCode;
	/**
	* 部门层级为一级时，该值为0；部门层级为二级，三级时为对应的上级部门编码
	*/
	@ApiModelProperty(value = "部门层级为一级时，该值为0；部门层级为二级，三级时为对应的上级部门编码")
	private String parentDpmentCode;
	/**
	* 1：直营和2：承包
	*/
	@ApiModelProperty(value = "1：直营和2：承包")
	private Integer businessModel;
	/**
	* 1：一级，2：二级，3：三级，最多三级
	*/
	@ApiModelProperty(value = "1：一级，2：二级，3：三级，最多三级")
	private Integer dpmentLevel;

	@ApiModelProperty(value = "部门负责人")
	private String dpmentHead;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 最后修改时间
	 */
	private LocalDateTime lastUpdate;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;



	private String finalBy;

	private Integer delFlag;
	/**
	* 部门所属公司编码，即网点编码
	*/
	@ApiModelProperty(value = "部门所属公司编码，即网点编码")
	private Integer cpCode;
	/**
	 * 系统默认部门：1-默认，2-非默认
	 */
	@ApiModelProperty(value = "系统默认部门：1-默认，2-非默认")
	private Integer isDefault;


}
