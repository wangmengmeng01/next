package com.yunda.base.feiniao.crmapp.costReport.domain;

import java.io.Serializable;

public class CostreportLiangBenLiDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//数据记录主键ID
	private String recordId;
	//下单日期
	private String accountDt;
	//票数
	private Integer orderSum;
	//重量(揽件重量)
	private Double weight;
	//均重
	private String weightAvg;
	//运费(收入)
	private Double fee;
	//中转费
	private Double tsfFee;
	//派费
	private Double deliveryFee;
	//续重派费
	private Double deliveryAdditionalWeightFee;
	//平衡派费
	private Double deliveryBalanceFee;
	//面单费
	private Double shipmentFee;
	//扫描费
	private Double scanFee;
	//始发分拨重量
	private Double startFbWeight;
	//目的分拨重量
	private Double endFbWeight;
	//计泡重量
	private Double bubbleWeight;
	//11天调整重量
	private Double elevenDayWeight;
	//客户编码
	private String customerId;
	//客户名称
	private String customerName;
	//网点编码
	private String branchCode;
	//创建时间
	private String createTime;
	//始发省ID(行政省)
	private String startProvinceId;
	//始发城市ID
	private String startCityId;
	//目的省ID(行政省)
	private String endProvinceId;
	//目的城市ID
	private String endCityId;
	//单票成本
	private String costAvg;
	//网点月总成本
	private Double totalCost;
	//单票中转费
	private String tsfFeeAvg;
	//单票派费
	private String deliveryFeeAvg;
	//单票续重派费
	private String deliveryAdditionalWeightFeeAvg;
	//单票平衡派费
	private String deliveryBalanceFeeAvg;
	//单票面单费
	private String shipmentFeeAvg;
	//单票扫描费
	private String scanFeeAvg;
	//运单号
	private String shipmentNo;
	//始发地（行政省+市）
	private String startPlace;
	//目的地（行政省+市）
	private String endPlace;
	//人力成本
	private Double peopleCost;
	//物料费
	private Double materielCost;
	//运输成本
	private Double tsfCost;
	//回扣
	private Double hhCost;
	//税金
	private Double taxesCost;
	//票均包仓费
	private Double packingCharge;
	//其他费用
	private Double otherCost;
	//利润
	private Double profit;
	
	public Double getFee() {
		return fee;
	}
	public void setFee(Double fee) {
		this.fee = fee;
	}
	public Double getProfit() {
		return profit;
	}
	public void setProfit(Double profit) {
		this.profit = profit;
	}
	public String getWeightAvg() {
		return weightAvg;
	}
	public void setWeightAvg(String weightAvg) {
		this.weightAvg = weightAvg;
	}
	public Double getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}
	public Double getPeopleCost() {
		return peopleCost;
	}
	public void setPeopleCost(Double peopleCost) {
		this.peopleCost = peopleCost;
	}
	public Double getMaterielCost() {
		return materielCost;
	}
	public void setMaterielCost(Double materielCost) {
		this.materielCost = materielCost;
	}
	public Double getTsfCost() {
		return tsfCost;
	}
	public void setTsfCost(Double tsfCost) {
		this.tsfCost = tsfCost;
	}
	public Double getHhCost() {
		return hhCost;
	}
	public void setHhCost(Double hhCost) {
		this.hhCost = hhCost;
	}
	public Double getTaxesCost() {
		return taxesCost;
	}
	public void setTaxesCost(Double taxesCost) {
		this.taxesCost = taxesCost;
	}
	public Double getPackingCharge() {
		return packingCharge;
	}
	public void setPackingCharge(Double packingCharge) {
		this.packingCharge = packingCharge;
	}
	public Double getOtherCost() {
		return otherCost;
	}
	public void setOtherCost(Double otherCost) {
		this.otherCost = otherCost;
	}
	public Integer getOrderSum() {
		return orderSum;
	}
	public void setOrderSum(Integer orderSum) {
		this.orderSum = orderSum;
	}
	public Double getStartFbWeight() {
		return startFbWeight;
	}
	public void setStartFbWeight(Double startFbWeight) {
		this.startFbWeight = startFbWeight;
	}
	public Double getEndFbWeight() {
		return endFbWeight;
	}
	public void setEndFbWeight(Double endFbWeight) {
		this.endFbWeight = endFbWeight;
	}
	public Double getBubbleWeight() {
		return bubbleWeight;
	}
	public void setBubbleWeight(Double bubbleWeight) {
		this.bubbleWeight = bubbleWeight;
	}
	public Double getElevenDayWeight() {
		return elevenDayWeight;
	}
	public void setElevenDayWeight(Double elevenDayWeight) {
		this.elevenDayWeight = elevenDayWeight;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getShipmentNo() {
		return shipmentNo;
	}
	public void setShipmentNo(String shipmentNo) {
		this.shipmentNo = shipmentNo;
	}
	public String getStartPlace() {
		return startPlace;
	}
	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
	}
	public String getEndPlace() {
		return endPlace;
	}
	public void setEndPlace(String endPlace) {
		this.endPlace = endPlace;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getAccountDt() {
		return accountDt;
	}
	public void setAccountDt(String accountDt) {
		this.accountDt = accountDt;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getTsfFee() {
		return tsfFee;
	}
	public void setTsfFee(Double tsfFee) {
		this.tsfFee = tsfFee;
	}
	public Double getDeliveryFee() {
		return deliveryFee;
	}
	public void setDeliveryFee(Double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
	public Double getDeliveryAdditionalWeightFee() {
		return deliveryAdditionalWeightFee;
	}
	public void setDeliveryAdditionalWeightFee(Double deliveryAdditionalWeightFee) {
		this.deliveryAdditionalWeightFee = deliveryAdditionalWeightFee;
	}
	public Double getDeliveryBalanceFee() {
		return deliveryBalanceFee;
	}
	public void setDeliveryBalanceFee(Double deliveryBalanceFee) {
		this.deliveryBalanceFee = deliveryBalanceFee;
	}
	public Double getShipmentFee() {
		return shipmentFee;
	}
	public void setShipmentFee(Double shipmentFee) {
		this.shipmentFee = shipmentFee;
	}
	public Double getScanFee() {
		return scanFee;
	}
	public void setScanFee(Double scanFee) {
		this.scanFee = scanFee;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getStartProvinceId() {
		return startProvinceId;
	}
	public void setStartProvinceId(String startProvinceId) {
		this.startProvinceId = startProvinceId;
	}
	public String getStartCityId() {
		return startCityId;
	}
	public void setStartCityId(String startCityId) {
		this.startCityId = startCityId;
	}
	public String getEndProvinceId() {
		return endProvinceId;
	}
	public void setEndProvinceId(String endProvinceId) {
		this.endProvinceId = endProvinceId;
	}
	public String getEndCityId() {
		return endCityId;
	}
	public void setEndCityId(String endCityId) {
		this.endCityId = endCityId;
	}
	public String getCostAvg() {
		return costAvg;
	}
	public void setCostAvg(String costAvg) {
		this.costAvg = costAvg;
	}
	public String getTsfFeeAvg() {
		return tsfFeeAvg;
	}
	public void setTsfFeeAvg(String tsfFeeAvg) {
		this.tsfFeeAvg = tsfFeeAvg;
	}
	public String getDeliveryFeeAvg() {
		return deliveryFeeAvg;
	}
	public void setDeliveryFeeAvg(String deliveryFeeAvg) {
		this.deliveryFeeAvg = deliveryFeeAvg;
	}
	public String getDeliveryAdditionalWeightFeeAvg() {
		return deliveryAdditionalWeightFeeAvg;
	}
	public void setDeliveryAdditionalWeightFeeAvg(
			String deliveryAdditionalWeightFeeAvg) {
		this.deliveryAdditionalWeightFeeAvg = deliveryAdditionalWeightFeeAvg;
	}
	public String getDeliveryBalanceFeeAvg() {
		return deliveryBalanceFeeAvg;
	}
	public void setDeliveryBalanceFeeAvg(String deliveryBalanceFeeAvg) {
		this.deliveryBalanceFeeAvg = deliveryBalanceFeeAvg;
	}
	public String getShipmentFeeAvg() {
		return shipmentFeeAvg;
	}
	public void setShipmentFeeAvg(String shipmentFeeAvg) {
		this.shipmentFeeAvg = shipmentFeeAvg;
	}
	public String getScanFeeAvg() {
		return scanFeeAvg;
	}
	public void setScanFeeAvg(String scanFeeAvg) {
		this.scanFeeAvg = scanFeeAvg;
	}

	
}
