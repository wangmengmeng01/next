package com.yunda.base.feiniao.costreport.bo;

import java.math.BigDecimal;
import java.util.Date;

import com.yunda.base.common.bo.Bo_Interface;

import io.swagger.annotations.ApiModelProperty;

public class Bo_CostreportCustCostFinish  implements Bo_Interface {

	private static final long serialVersionUID = 1L;
	
	//数据记录主键ID
	@ApiModelProperty(value = "数据记录主键ID")
	private Integer recordId;
	//下单日期
	@ApiModelProperty(value = "下单日期")
	private String accountDt;
	//票数
	@ApiModelProperty(value = "票数")
	private Integer orderSum;
	//重量
	@ApiModelProperty(value = "重量")
	private BigDecimal weight;
	//中转费
	@ApiModelProperty(value = "中转费")
	private BigDecimal tsfFee;
	//派费
	@ApiModelProperty(value = "派费")
	private BigDecimal deliveryFee;
	//续重派费
	@ApiModelProperty(value = "续重派费")
	private BigDecimal deliveryAdditionalWeightFee;
	//平衡派费
	@ApiModelProperty(value = "平衡派费")
	private BigDecimal deliveryBalanceFee;
	//面单费
	@ApiModelProperty(value = "面单费")
	private BigDecimal shipmentFee;
	//扫描费73
	@ApiModelProperty(value = "扫描费73")
	private BigDecimal scanFee;
	//客户编码
	@ApiModelProperty(value = "客户编码")
	private String customerId;
	//网点编码
	@ApiModelProperty(value = "网点编码")
	private String branchCode;
	//创建时间
	@ApiModelProperty(value = "创建时间")
	private String createTime;
	
	@ApiModelProperty(value = "开始日期")
	private Date startAccountDt;
	
	//结束日期
	@ApiModelProperty(value = "结束日期")
	private Date endAccountDt;
	
	//人力成本
	@ApiModelProperty(value = "人力成本")
	private BigDecimal peopleCost;
	
	//物料费
	@ApiModelProperty(value = "物料费")
	private BigDecimal materielCost;
	
	//运输成本
	@ApiModelProperty(value = "运输成本")
	private BigDecimal tsfCost;
	
	//回扣
	@ApiModelProperty(value = "回扣")
	private BigDecimal hhCost;
	
	//税金
	@ApiModelProperty(value = "税金")
	private BigDecimal taxesCost;
	
	//票均包仓费
	@ApiModelProperty(value = "票均包仓费")
	private BigDecimal packingCharge;
	
	//包仓费类型:0按公斤,1按票
	@ApiModelProperty(value = "包仓费类型:0按公斤,1按票")
	private Integer packingChargeFlag;
	
	//其他费用
	@ApiModelProperty(value = "其他费用")
	private BigDecimal otherCost;

	//网点名称
	@ApiModelProperty(value = "网点名称")
	private String branchName;

	//客户名称
	@ApiModelProperty(value = "客户名称")
	private String customerName;
	
	//客户来源
	@ApiModelProperty(value = "客户来源")
	private String customerSourceType;
	
	//始发省ID
	@ApiModelProperty(value = "始发省ID")
	private Integer startProvinceId;
	//目的省ID
	@ApiModelProperty(value = "目的省ID")
	private Integer endProvinceId;
	
	

	public Integer getStartProvinceId() {
		return startProvinceId;
	}
	public void setStartProvinceId(Integer startProvinceId) {
		this.startProvinceId = startProvinceId;
	}
	public Integer getEndProvinceId() {
		return endProvinceId;
	}
	public void setEndProvinceId(Integer endProvinceId) {
		this.endProvinceId = endProvinceId;
	}
	public Integer getRecordId() {
		return recordId;
	}
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	public String getAccountDt() {
		return accountDt;
	}
	public void setAccountDt(String accountDt) {
		this.accountDt = accountDt;
	}
	public Integer getOrderSum() {
		return orderSum;
	}
	public void setOrderSum(Integer orderSum) {
		this.orderSum = orderSum;
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
	public Date getStartAccountDt() {
		return startAccountDt;
	}
	public void setStartAccountDt(Date startAccountDt) {
		this.startAccountDt = startAccountDt;
	}
	public Date getEndAccountDt() {
		return endAccountDt;
	}
	public void setEndAccountDt(Date endAccountDt) {
		this.endAccountDt = endAccountDt;
	}
	public BigDecimal getPeopleCost() {
		return peopleCost;
	}
	public void setPeopleCost(BigDecimal peopleCost) {
		this.peopleCost = peopleCost;
	}
	public BigDecimal getMaterielCost() {
		return materielCost;
	}
	public void setMaterielCost(BigDecimal materielCost) {
		this.materielCost = materielCost;
	}
	public BigDecimal getTsfCost() {
		return tsfCost;
	}
	public void setTsfCost(BigDecimal tsfCost) {
		this.tsfCost = tsfCost;
	}
	public BigDecimal getHhCost() {
		return hhCost;
	}
	public void setHhCost(BigDecimal hhCost) {
		this.hhCost = hhCost;
	}
	public BigDecimal getTaxesCost() {
		return taxesCost;
	}
	public void setTaxesCost(BigDecimal taxesCost) {
		this.taxesCost = taxesCost;
	}
	public BigDecimal getPackingCharge() {
		return packingCharge;
	}
	public void setPackingCharge(BigDecimal packingCharge) {
		this.packingCharge = packingCharge;
	}
	public Integer getPackingChargeFlag() {
		return packingChargeFlag;
	}
	public void setPackingChargeFlag(Integer packingChargeFlag) {
		this.packingChargeFlag = packingChargeFlag;
	}
	public BigDecimal getOtherCost() {
		return otherCost;
	}
	public void setOtherCost(BigDecimal otherCost) {
		this.otherCost = otherCost;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
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
	

}
