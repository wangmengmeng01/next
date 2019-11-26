package com.yunda.base.bigcustomer.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-22111446
 */
public class PermissionsManageDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//序号
	@ExcelField(title = "序号", order = 1)
	private Integer id;
	//角色编号
	@ExcelField(title = "角色编号", order = 2)
	private String roleNum;
	//角色名称
	@ExcelField(title = "角色名称", order = 3)
	private String roleName;
	//角色说明
	@ExcelField(title = "角色说明", order = 4)
	private String roleExplain;
	//上级角色
	@ExcelField(title = "上级角色", order = 5)
	private String superiorRole;
	//上级角色编码
	@ExcelField(title = "上级角色编码", order = 6)
	private String superiorRoleNum;
	//状态
	@ExcelField(title = "状态", order = 7)
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
	 * 设置：角色编号
	 */
	public void setRoleNum(String roleNum) {
		this.roleNum = roleNum;
	}
	/**
	 * 获取：角色编号
	 */
	public String getRoleNum() {
		return roleNum;
	}
	/**
	 * 设置：角色名称
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	/**
	 * 获取：角色名称
	 */
	public String getRoleName() {
		return roleName;
	}
	/**
	 * 设置：角色说明
	 */
	public void setRoleExplain(String roleExplain) {
		this.roleExplain = roleExplain;
	}
	/**
	 * 获取：角色说明
	 */
	public String getRoleExplain() {
		return roleExplain;
	}
	/**
	 * 设置：上级角色
	 */
	public void setSuperiorRole(String superiorRole) {
		this.superiorRole = superiorRole;
	}
	/**
	 * 获取：上级角色
	 */
	public String getSuperiorRole() {
		return superiorRole;
	}
	/**
	 * 设置：上级角色编码
	 */
	public void setSuperiorRoleNum(String superiorRoleNum) {
		this.superiorRoleNum = superiorRoleNum;
	}
	/**
	 * 获取：上级角色编码
	 */
	public String getSuperiorRoleNum() {
		return superiorRoleNum;
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