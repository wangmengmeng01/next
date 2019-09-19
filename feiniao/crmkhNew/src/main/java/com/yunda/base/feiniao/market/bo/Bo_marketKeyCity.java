package com.yunda.base.feiniao.market.bo;

import java.util.List;

import com.yunda.base.common.bo.Bo_Interface;

import io.swagger.annotations.ApiModelProperty;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 重点城市同行间市场份额对比表 验参
 * @author admin
 *
 */
public class Bo_marketKeyCity implements Bo_Interface {
	
	//选择年
	private String monthYear;
	
	//选择月
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
	
	/* 权限标识*/
	private String tmpField;
	
	/* 省份权限*/
	private List<Long> provinceids;
	
	

	public List<Long> getProvinceids() {
		return provinceids;
	}

	public void setProvinceids(List<Long> provinceids) {
		this.provinceids = provinceids;
	}
	
	
	public String getTmpField() {
		return tmpField;
	}
	public void setTmpField(String tmpField) {
		this.tmpField = tmpField;
	}
	
	
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
