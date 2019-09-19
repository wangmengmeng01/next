package com.yunda.base.system.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-02-18091552
 */
public class ResourceSafeConfigDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	@ExcelField(title = "id", order = 1)
	private Integer id;
	//页面路径
	@ExcelField(title = "页面路径", order = 2)
	private String resourcePath;
	//安全等级
	@ExcelField(title = "安全等级", order = 3)
	private String safeLevel;

 

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
	 * 设置：页面路径
	 */
	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	/**
	 * 获取：页面路径
	 */
	public String getResourcePath() {
		return resourcePath;
	}
	/**
	 * 设置：安全等级
	 */
	public void setSafeLevel(String safeLevel) {
		this.safeLevel = safeLevel;
	}
	/**
	 * 获取：安全等级
	 */
	public String getSafeLevel() {
		return safeLevel;
	}
}
