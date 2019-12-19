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

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 人事流动趋势表实体类
 *
 * @author dongfeng
 * @since 2019-10-24
 */
@Data
@TableName("pc_employee_statistics")
@ApiModel(value = "EmployeeStatistics对象", description = "人事流动趋势表")
public class EmployeeStatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 主键
	*/
	@ApiModelProperty(value = "主键")
	private Long empsId;
	/**
	* 统计日期
	*/
	@ApiModelProperty(value = "统计日期")
	private LocalDateTime everydayTime;
	/**
	* 入职人数
	*/
	@ApiModelProperty(value = "入职人数")
	private Integer inCnt;
	/**
	* 离职人数
	*/
	@ApiModelProperty(value = "离职人数")
	private Integer leaveCnt;
	/**
	* 在职总人数
	*/
	@ApiModelProperty(value = "在职总人数")
	private Integer totalCnt;
	/**
	* 删除标识：0-正常，1-删除
	*/

	@ApiModelProperty(value = "删除标识：0-正常，1-删除")
	private Integer delFlag;


}
