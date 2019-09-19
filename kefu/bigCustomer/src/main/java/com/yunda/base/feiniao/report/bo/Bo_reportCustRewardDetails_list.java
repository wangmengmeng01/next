package com.yunda.base.feiniao.report.bo;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.yunda.base.common.bo.Bo_Interface;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//需要分页参数的时候可以extends PageBo
@ApiModel(value = "客户奖励明细表验参")
public class Bo_reportCustRewardDetails_list implements Bo_Interface {
	/*客户编码*/
	@ApiModelProperty(value = "客户编码")
	private String customerId;
	
	/*开始时间*/
	@ApiModelProperty(value = "开始时间")
	@NotBlank()
	private String startDate;
	
	
	/* 结束时间*/
	@ApiModelProperty(value = "结束时间")
	@NotBlank()
	private String endDate; 
	
	/* 每页查询数*/
	@ApiModelProperty(value = "每页查询数")
	private int limit;
	
	/* 查询开始位置*/
	@ApiModelProperty(value = "查询开始位置")
	private int offset;
	
	
	/* 网点编码*/
	@ApiModelProperty(value = "网点编码")
	private String gs;
	
	/* 客户来源*/
	@ApiModelProperty(value = "客户来源")
	private String customerSourceType;
	
	/* 起始揽件量*/
	@ApiModelProperty(value = "起始揽件量")
	private Double startOrderSum;
	
	/* 结束揽件量*/
	@ApiModelProperty(value = "结束揽件量")
	private Double endOrderSum;
	
	/* 大区权限*/
	private List<String> bigareaNames;
	
	/* 权限标识*/
	private String tmpField;
	
	/* 省份权限*/
	private List<Long> provinceids;
	
	/* 上级网点编码*/
	private String branchCode;
	
	
	
	public String getBranchCode() {
		return branchCode;
	}


	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}


	public List<Long> getProvinceids() {
		return provinceids;
	}


	public void setProvinceids(List<Long> provinceids) {
		this.provinceids = provinceids;
	}


	public List<String> getBigareaNames() {
		return bigareaNames;
	}


	public void setBigareaNames(List<String> bigareaNames) {
		this.bigareaNames = bigareaNames;
	}


	public String getTmpField() {
		return tmpField;
	}


	public void setTmpField(String tmpField) {
		this.tmpField = tmpField;
	}


	public Double getStartOrderSum() {
		return startOrderSum;
	}


	public void setStartOrderSum(Double startOrderSum) {
		this.startOrderSum = startOrderSum;
	}


	public Double getEndOrderSum() {
		return endOrderSum;
	}


	public void setEndOrderSum(Double endOrderSum) {
		this.endOrderSum = endOrderSum;
	}


	public String getCustomerSourceType() {
		return customerSourceType;
	}


	public void setCustomerSourceType(String customerSourceType) {
		this.customerSourceType = customerSourceType;
	}


	public String getGs() {
		return gs;
	}


	public void setGs(String gs) {
		this.gs = gs;
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


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
	
	
}
