package com.yunda.base.system.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;

/**
 * 部门管理
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-27 14:28:36
 */
public class DeptDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ExcelField(title = "deptId", order = 1)
	private Long deptId;
	
	// 上级部门ID，一级部门为0
	@ExcelField(title = "上级部门ID", order = 2)
	private Long parentId;
	
	// 部门名称
	@ExcelField(title = "部门名称", order = 3)
	private String name;
	
	// 排序
	@ExcelField(title = "排序", order = 4)
	private Integer orderNum;
	
	// 是否删除 -1：已删除 0：正常
	@ExcelField(title = "delFlag", order = 5)
	private Integer delFlag = 0 ;

	/**
	 * 设置：
	 */
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	/**
	 * 获取：
	 */
	public Long getDeptId() {
		return deptId;
	}

	/**
	 * 设置：上级部门ID，一级部门为0
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * 获取：上级部门ID，一级部门为0
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * 设置：部门名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：部门名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * 获取：排序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}

	/**
	 * 设置：是否删除 -1：已删除 0：正常
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	/**
	 * 获取：是否删除 -1：已删除 0：正常
	 */
	public Integer getDelFlag() {
		return delFlag;
	}

	@Override
	public String toString() {
		return "DeptDO{" + "deptId=" + deptId + ", parentId=" + parentId + ", name='" + name + '\'' + ", orderNum="
				+ orderNum + ", delFlag=" + delFlag + '}';
	}
}
