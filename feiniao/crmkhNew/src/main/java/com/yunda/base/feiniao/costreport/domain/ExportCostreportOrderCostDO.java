package com.yunda.base.feiniao.costreport.domain;

import com.github.crab2died.annotation.ExcelField;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * (导出)客户报表订单统计/客户单票成本
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @String 2018-09-14092504
 */
public class ExportCostreportOrderCostDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//揽件日期
	@ExcelField(title = "揽件日", order = 1)
	private String senderAccountDt;	
	//TD结算日期
	@ExcelField(title = "TD结算日期", order = 2)
	private String tdAccountDt;
	//结算日期
	@ExcelField(title = "量本利结算日", order = 3)
	private String accountDt;
	//客户编号
	@ExcelField(title = "客户编号", order = 4)
	private String customerId;
	//客户名称
	@ExcelField(title = "客户名称", order = 5)
	private String customerName;
	//运单号
	@ExcelField(title = "运单号", order = 6)
	private Long shipmentNo;
	//始发省
	@ExcelField(title = "始发省", order = 7)
	private String startProvinceName;
	//目的省
	@ExcelField(title = "目的省", order = 8)
	private String endProvinceName;
	//重量
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
	@ExcelField(title = "扫描费73", order = 15)
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
	public Long getShipmentNo() {
		return shipmentNo;
	}
	public void setShipmentNo(Long shipmentNo) {
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
