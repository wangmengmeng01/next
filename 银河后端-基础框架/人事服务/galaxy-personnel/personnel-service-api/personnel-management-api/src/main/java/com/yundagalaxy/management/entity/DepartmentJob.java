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
import java.time.LocalDateTime;

/**
 * 部门岗位表实体类
 *
 * @author dongfeng
 * @since 2019-10-19
 */
@Data
@TableName("pc_department_job")
@ApiModel(value = "DepartmentJob对象", description = "部门岗位表")
public class DepartmentJob implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 岗位主键
	*/
	@ApiModelProperty(value = "岗位主键")
	@TableId(
			value = "job_id",
			type = IdType.ID_WORKER
	)
	private Long jobId;
	/**
	* 岗位编码示例JJ10000000
	*/
	@ApiModelProperty(value = "岗位编码示例JJ10000000")
	private String jobCode;
	/**
	* 岗位名称
	*/
	@ApiModelProperty(value = "岗位名称")
	private String jobName;
	/**
	* 法人、经理及法人、经理、客服、财务、司机、操作员、业务员、
            人事行政后勤类
	*/
	@ApiModelProperty(value = "法人、经理及法人、经理、客服、财务、司机、操作员、业务员、 人事行政后勤类")
	private Integer jobType;
	/**
	* 部门编码
	*/
	@ApiModelProperty(value = "部门编码")
	private String dpmentCode;
	/**
	* 1级员工、2级员工、3级主管、4级副经理、5级经理
	*/
	@ApiModelProperty(value = "1级员工、2级员工、3级主管、4级副经理、5级经理")
	private Integer jobLevel;
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
	* 最后操作人
	*/
	@ApiModelProperty(value = "最后操作人")
	private String finalBy;
	/**
	* 删除标识(0未删除，1已删除)
	*/
	@ApiModelProperty(value = "删除标识(0未删除，1已删除)")
	private Integer delFlag;
	/**
	* 部门所属公司编码，即网点编码
	*/
	@ApiModelProperty(value = "部门所属公司编码，即网点编码")
	private Integer cpCode;


}
