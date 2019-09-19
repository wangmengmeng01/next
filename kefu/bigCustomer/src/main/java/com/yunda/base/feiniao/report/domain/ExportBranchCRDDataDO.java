package com.yunda.base.feiniao.report.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;

public class ExportBranchCRDDataDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ExcelField(title = "数据记录主键ID", order = 1)
	private Integer recordId;
	
	@ExcelField(title = "大区", order = 2)
	private String bigarea;
	
	@ExcelField(title = "省名称", order = 3)
	private String provinceName;
	
	@ExcelField(title = "城市名称", order = 4)
	private String cityName;
	
	@ExcelField(title = "网点名称", order = 5)
	private String mc;
	
	@ExcelField(title = "网点编码", order = 6)
	private String gs;
	
	@ExcelField(title = "上级站点编码", order = 7)
	private String branchCode;
	
	@ExcelField(title = "客户编码", order = 8)
	private String customerId;
	
	@ExcelField(title = "客户名称", order = 9)
	private String customerName;
	
	@ExcelField(title = "商家ID", order = 10)
	private String sellerId;
	@ExcelField(title = "店铺名称", order =11)
	private String sellerName;
	
	@ExcelField(title = "客户来源", order = 12)
	private String customerSourceType;
	
	@ExcelField(title = "揽件量", order = 13)
	private double orderSum;
	
	@ExcelField(title = "日均揽件量", order = 14)
	private double orderAvg;
	
	@ExcelField(title = "客户类型", order = 15)
	private String custLevel;
	
	public String getSellerId() {
		return sellerId;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public String getBigarea() {
		return bigarea;
	}

	public void setBigarea(String bigarea) {
		this.bigarea = bigarea;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public String getGs() {
		return gs;
	}

	public void setGs(String gs) {
		this.gs = gs;
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

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerSourceType() {
		return customerSourceType;
	}

	public void setCustomerSourceType(String customerSourceType) {
		this.customerSourceType = customerSourceType;
	}

	public double getOrderSum() {
		return orderSum;
	}

	public void setOrderSum(double orderSum) {
		this.orderSum = orderSum;
	}

	public double getOrderAvg() {
		return orderAvg;
	}

	public void setOrderAvg(double orderAvg) {
		this.orderAvg = orderAvg;
	}

	public String getCustLevel() {
		return custLevel;
	}

	public void setCustLevel(String custLevel) {
		this.custLevel = custLevel;
	}

}
