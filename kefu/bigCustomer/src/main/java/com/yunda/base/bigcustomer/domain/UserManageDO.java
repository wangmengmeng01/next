package com.yunda.base.bigcustomer.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-21164900
 */
public class UserManageDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	@ExcelField(title = "id", order = 1)
	private Integer id;
	//user_num
	@ExcelField(title = "user_num", order = 2)
	private String userNum;
	//user_name
	@ExcelField(title = "user_name", order = 3)
	private String userName;
	//角色
	@ExcelField(title = "角色", order = 4)
	private String role;
	//所属机构
	@ExcelField(title = "所属机构", order = 5)
	private String organization;
	//状态
	@ExcelField(title = "状态", order = 6)
	private String state;

 

	/**
	 * 设置：id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：user_num
	 */
	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}
	/**
	 * 获取：user_num
	 */
	public String getUserNum() {
		return userNum;
	}
	/**
	 * 设置：user_name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * 获取：user_name
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * 设置：角色
	 */
	public void setRole(String role) {
		this.role = role;
	}
	/**
	 * 获取：角色
	 */
	public String getRole() {
		return role;
	}
	/**
	 * 设置：所属机构
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	/**
	 * 获取：所属机构
	 */
	public String getOrganization() {
		return organization;
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
