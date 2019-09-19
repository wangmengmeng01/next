package com.yunda.base.bigcustomer.domain;
import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-21110249
 */
public class OrganizationManageDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//序号
	@ExcelField(title = "序号", order = 1)
	private Integer id;
	//机构编码
	@ExcelField(title = "机构编码", order = 2)
	private String organizationNum;
	//机构名称
	@ExcelField(title = "机构名称", order = 3)
	private String organizationName;
	//机构等级
	@ExcelField(title = "机构等级", order = 4)
	private String organizationLevel;
	//状态
	@ExcelField(title = "状态", order = 5)
	private String state;

 

	/**
	 * 设置：序号
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：序号
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：机构编码
	 */
	public void setOrganizationNum(String organizationNum) {
		this.organizationNum = organizationNum;
	}
	/**
	 * 获取：机构编码
	 */
	public String getOrganizationNum() {
		return organizationNum;
	}
	/**
	 * 设置：机构名称
	 */
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	/**
	 * 获取：机构名称
	 */
	public String getOrganizationName() {
		return organizationName;
	}
	/**
	 * 设置：机构等级
	 */
	public void setOrganizationLevel(String organizationLevel) {
		this.organizationLevel = organizationLevel;
	}
	/**
	 * 获取：机构等级
	 */
	public String getOrganizationLevel() {
		return organizationLevel;
	}
	/**
	 * 设置：状态
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * 获取：状态
	 */
	public String getState() {
		return state;
	}
}