package com.yunda.base.feiniao.costreport.domain;

import java.math.BigDecimal;

import com.github.crab2died.annotation.ExcelField;

public class ExportCostreportCustCostExtDO {
	private static final long serialVersionUID = 1L;
/*	//数据记录主键ID
	@ExcelField(title = "数据记录主键ID", order = 1)
	private Integer recordId;*/
	
	
	//揽件日
	@ExcelField(title = "揽件日", order = 1)
	private String senderAccountDt;
	//TD结算日
	@ExcelField(title = "TD结算日", order = 2)
	private String tdAccountDt;
	//量本利结算日
	@ExcelField(title = "量本利结算日", order = 3)
	private String accountDt;
	
	//客户编码
	@ExcelField(title = "客户编码", order = 4)
	private String customerId;
	
	//客户名称
	@ExcelField(title = "客户名称", order = 5)
	private String customerName;
	
	//运单号
	@ExcelField(title = "运单号", order = 6)
	private long shipmentNo;
	
	//始发省名称
	@ExcelField(title = "始发省", order = 7)
	private String startProvinceName;
	
	
	//目的省名称
	@ExcelField(title = "目的省", order = 8)
	private String endProvinceName;
	//结算重量
	@ExcelField(title = "结算重量", order = 9)
	private BigDecimal weight;
	
	//中转费
	@ExcelField(title = "中转费", order = 10)
	private BigDecimal tsfFee;
	
	//派费
	@ExcelField(title = "派费", order = 11)
	private BigDecimal deliveryFee;
	
	//续重派费
	@ExcelField(title = "续重派费", order = 12)
	private BigDecimal deliveryAdditionalWeightFee;
	
	//平衡派费
	@ExcelField(title = "平衡派费", order = 13)
	private BigDecimal deliveryBalanceFee;
	
	//面单费
	@ExcelField(title = "面单费", order = 14)
	private BigDecimal shipmentFee;
	
	//扫描费73
	@ExcelField(title = "扫描费", order = 15)
	private BigDecimal scanFee;

	public String getSenderAccountDt() {
		return senderAccountDt;
	}

	public void setSenderAccountDt(String senderAccountDt) {
		this.senderAccountDt = senderAccountDt;
	}

	public String getTdAccountDt() {
		return tdAccountDt;
	}

	public void setTdAccountDt(String tdAccountDt) {
		this.tdAccountDt = tdAccountDt;
	}

	public String getAccountDt() {
		return accountDt;
	}

	public void setAccountDt(String accountDt) {
		this.accountDt = accountDt;
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

	public long getShipmentNo() {
		return shipmentNo;
	}

	public void setShipmentNo(long shipmentNo) {
		this.shipmentNo = shipmentNo;
	}

	public String getStartProvinceName() {
		return startProvinceName;
	}

	public void setStartProvinceName(String startProvinceName) {
		this.startProvinceName = startProvinceName;
	}

	public String getEndProvinceName() {
		return endProvinceName;
	}

	public void setEndProvinceName(String endProvinceName) {
		this.endProvinceName = endProvinceName;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public BigDecimal getTsfFee() {
		return tsfFee;
	}

	public void setTsfFee(BigDecimal tsfFee) {
		this.tsfFee = tsfFee;
	}

	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}

	public void setDeliveryFee(BigDecimal deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public BigDecimal getDeliveryAdditionalWeightFee() {
		return deliveryAdditionalWeightFee;
	}

	public void setDeliveryAdditionalWeightFee(
			BigDecimal deliveryAdditionalWeightFee) {
		this.deliveryAdditionalWeightFee = deliveryAdditionalWeightFee;
	}

	public BigDecimal getDeliveryBalanceFee() {
		return deliveryBalanceFee;
	}

	public void setDeliveryBalanceFee(BigDecimal deliveryBalanceFee) {
		this.deliveryBalanceFee = deliveryBalanceFee;
	}

	public BigDecimal getShipmentFee() {
		return shipmentFee;
	}

	public void setShipmentFee(BigDecimal shipmentFee) {
		this.shipmentFee = shipmentFee;
	}

	public BigDecimal getScanFee() {
		return scanFee;
	}

	public void setScanFee(BigDecimal scanFee) {
		this.scanFee = scanFee;
	}
	
	
	
}
