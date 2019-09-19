package com.yunda.base.system.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;


/**
 * 用户与省对应关系
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-08-15 17:28:17
 */
public class UserProvinceDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	@ExcelField(title = "id", order = 1)
	private Long id;
	//用户ID
	@ExcelField(title = "用户ID", order = 2)
	private Long userId;
	//省ID
	@ExcelField(title = "省ID", order = 3)
	private Long provinceId;

 

	/**
	 * 设置：id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：省ID
	 */
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	/**
	 * 获取：省ID
	 */
	public Long getProvinceId() {
		return provinceId;
	}
}
