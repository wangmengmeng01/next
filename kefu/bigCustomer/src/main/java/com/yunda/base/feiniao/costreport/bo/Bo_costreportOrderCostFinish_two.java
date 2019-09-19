package com.yunda.base.feiniao.costreport.bo;

import org.hibernate.validator.constraints.NotBlank;

import io.swagger.annotations.ApiModelProperty;

public class Bo_costreportOrderCostFinish_two {
	@ApiModelProperty(value = "下单日期")
	@NotBlank
	private String accountDt;
	
	@ApiModelProperty(value = "网点编码")
	//@NotBlank
	private String branchCode;
	
	@ApiModelProperty(value = "客户编码")
	//@NotBlank
	private String customerId;
	
	@ApiModelProperty(value = "每页查询数")
	private int limit;
	
	@ApiModelProperty(value = "查询开始位置")
	private int offset;

	public String getAccountDt() {
		return accountDt;
	}

	public void setAccountDt(String accountDt) {
		this.accountDt = accountDt;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
	
	

}
