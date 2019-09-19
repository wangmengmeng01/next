package com.yunda.base.feiniao.market.bo;

import org.hibernate.validator.constraints.NotBlank;

import com.yunda.base.common.bo.Bo_Interface;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//需要分页参数的时候可以extends PageBo
@ApiModel(value = "省同行间市场份额分析对比表验参")
public class Bo_marketKeyProvince implements Bo_Interface {
	
	//选择年
	@ApiModelProperty(value = "选择年")
	private String monthYear;
	
	//选择月
	@ApiModelProperty(value = "选择月")
	private String monthDate;
	
	//选择年
	@ApiModelProperty(value = "查询月份")
	@NotBlank
	private String searchDate;
	
	/* 每页查询数*/
	@ApiModelProperty(value = "每页查询数")
	private int limit;
	
	/* 查询开始位置*/
	@ApiModelProperty(value = "查询开始位置")
	private int offset;
	
	
	
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public String getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}

	private double ydyzl = 0;
	private double kdhyzl = 0;

	public double getYdyzl() {
		return ydyzl;
	}
	public void setYdyzl(double ydyzl) {
		this.ydyzl = ydyzl;
	}
	public double getKdhyzl() {
		return kdhyzl;
	}
	public void setKdhyzl(double kdhyzl) {
		this.kdhyzl = kdhyzl;
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	public String getMonthDate() {
		return monthDate;
	}

	public void setMonthDate(String monthDate) {
		this.monthDate = monthDate;
	}
	
	
	

}
