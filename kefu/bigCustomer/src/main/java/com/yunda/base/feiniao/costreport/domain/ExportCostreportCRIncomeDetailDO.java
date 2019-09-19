package com.yunda.base.feiniao.costreport.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.github.crab2died.annotation.ExcelField;

public class ExportCostreportCRIncomeDetailDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//揽件日期
	@ExcelField(title = "揽件日期", order = 1)
	private String senderAccountDt;
	//下单日期
	@ExcelField(title = "下单日期", order = 2)
	private String orderAccountDt;
	//运单号
	@ExcelField(title = "运单号", order = 3)
	private String shipmentNo;
	//始发省名称
	@ExcelField(title = "始发省名称", order = 4)
	private String startProvinceName;
	//始发省名称
	@ExcelField(title = "目的省名称", order = 5)
	private String endProvinceName;
	//重量
	@ExcelField(title = "重量", order = 6)
	private BigDecimal weight;
	//运费
	@ExcelField(title = "运费", order = 13)
	private BigDecimal fee;
	public String getSenderAccountDt() {
		return senderAccountDt;
	}
	public void setSenderAccountDt(String senderAccountDt) {
		this.senderAccountDt = senderAccountDt;
	}
	public String getOrderAccountDt() {
		return orderAccountDt;
	}
	public void setOrderAccountDt(String orderAccountDt) {
		this.orderAccountDt = orderAccountDt;
	}
	public String getShipmentNo() {
		return shipmentNo;
	}
	public void setShipmentNo(String shipmentNo) {
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
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	
}
