package com.yunda.base.bigcustomer.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;



/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-04-30113019
 */
public class ConfigDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	@ExcelField(title = "id", order = 1)
	private Integer id;
	//consult_source
	@ExcelField(title = "consult_source", order = 2)
	private String consultSource;
	//consult_type
	@ExcelField(title = "consult_type", order = 3)
	private String consultType;
	//deal_shi_xiao
	@ExcelField(title = "deal_shi_xiao", order = 4)
	private String dealShiXiao;
	//state
	@ExcelField(title = "state", order = 5)
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
	 * 设置：consult_source
	 */
	public void setConsultSource(String consultSource) {
		this.consultSource = consultSource;
	}
	/**
	 * 获取：consult_source
	 */
	public String getConsultSource() {
		return consultSource;
	}
	/**
	 * 设置：consult_type
	 */
	public void setConsultType(String consultType) {
		this.consultType = consultType;
	}
	/**
	 * 获取：consult_type
	 */
	public String getConsultType() {
		return consultType;
	}
	/**
	 * 设置：deal_shi_xiao
	 */
	public void setDealShiXiao(String dealShiXiao) {
		this.dealShiXiao = dealShiXiao;
	}
	/**
	 * 获取：deal_shi_xiao
	 */
	public String getDealShiXiao() {
		return dealShiXiao;
	}
	/**
	 * 设置：state
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * 获取：state
	 */
	public String getState() {
		return state;
	}
}
