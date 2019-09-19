package com.yunda.base.feiniao.market.bo;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import com.github.crab2died.annotation.ExcelField;
import com.yunda.base.common.bo.Bo_Interface;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-07150734
 */
public class Bo_marketOccupancyTaoxi implements Bo_Interface{
	

	@ApiModelProperty(value = "每页查询数   偏移量")
	private int limit;
	
	@ApiModelProperty(value = "查询开始位置")
	private int offset;
	@ApiModelProperty(value = "报表类型")
	private String timeType;
	//日或周报表-时间段
	private String startDate;
	private String endDate;
	//月报表-月
	private String month;

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
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
}
