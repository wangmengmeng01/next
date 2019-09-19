package com.yunda.base.system.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;


/**
 * 用户与大区对应关系
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-08-17 11:08:47
 */
public class UserBigareaDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	@ExcelField(title = "id", order = 1)
	private Long id;
	//用户ID
	@ExcelField(title = "用户ID", order = 2)
	private Long userId;
	//大区名
	@ExcelField(title = "大区名", order = 3)
	private String bigareaName;

 

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
	 * 设置：大区名
	 */
	public void setBigareaName(String bigareaName) {
		this.bigareaName = bigareaName;
	}
	/**
	 * 获取：大区名
	 */
	public String getBigareaName() {
		return bigareaName;
	}
}
