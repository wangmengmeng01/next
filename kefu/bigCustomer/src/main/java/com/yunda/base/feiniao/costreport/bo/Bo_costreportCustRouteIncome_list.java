package com.yunda.base.feiniao.costreport.bo;

import org.hibernate.validator.constraints.NotBlank;

import com.yunda.base.common.bo.Bo_Interface;

import io.swagger.annotations.ApiModelProperty;

public class Bo_costreportCustRouteIncome_list implements Bo_Interface {
	
	

	@ApiModelProperty(value = "下单日期")
	@NotBlank
	private String accountDt;
	
	@ApiModelProperty(value = "网点编码")
	//@NotBlank
	private Integer branchCode;
		
	@ApiModelProperty(value = "客户来源")
	//@NotBlank
	private String customerSourceType;
		
	@ApiModelProperty(value = "客户编码")
	@NotBlank
	private String customerId;
	
	@ApiModelProperty(value = "始发省ID")
	private String startProvinceId;
		
	@ApiModelProperty(value = "目的省ID")
	private String endProvinceId;
	
	@ApiModelProperty(value = "每页查询数")
	private int limit;
	
	@ApiModelProperty(value = "查询开始位置")
	private int offset;
	
	@ApiModelProperty(value = "运单号")
	private String shipmentNo;
	
	@ApiModelProperty(value = "查询月初时间")
	private String accountDtStart;
	
	@ApiModelProperty(value = "查询月末时间")
	private String accountDtEnd;
	
	

	public String getAccountDtStart() {
		return accountDtStart;
	}

	public void setAccountDtStart(String accountDtStart) {
		this.accountDtStart = accountDtStart;
	}

	public String getAccountDtEnd() {
		return accountDtEnd;
	}

	public void setAccountDtEnd(String accountDtEnd) {
		this.accountDtEnd = accountDtEnd;
	}

	public String getShipmentNo() {
		return shipmentNo;
	}

	public void setShipmentNo(String shipmentNo) {
		this.shipmentNo = shipmentNo;
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

	public Integer getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(Integer branchCode) {
		this.branchCode = branchCode;
	}

	public String getCustomerSourceType() {
		return customerSourceType;
	}

	public void setCustomerSourceType(String customerSourceType) {
		this.customerSourceType = customerSourceType;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getStartProvinceId() {
		return startProvinceId;
	}

	public void setStartProvinceId(String startProvinceId) {
		this.startProvinceId = startProvinceId;
	}

	public String getEndProvinceId() {
		return endProvinceId;
	}

	public void setEndProvinceId(String endProvinceId) {
		this.endProvinceId = endProvinceId;
	}
	
	public String getAccountDt() {
		return accountDt;
	}

	public void setAccountDt(String accountDt) {
		this.accountDt = accountDt;
	}

}
