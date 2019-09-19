package com.yunda.base.feiniao.costreport.domain;

import com.github.crab2died.annotation.ExcelField;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 客户报表订单统计/客户支出报表(完成统计)
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-13145339
 */
public class CostreportCustCostFinishDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//数据记录主键ID
	@ExcelField(title = "数据记录主键ID", order = 1)
	private Integer recordId;
	//下单日期
	@ExcelField(title = "下单日期", order = 2)
	private Integer accountDt;
	//票数
	@ExcelField(title = "票数", order = 3)
	private Integer orderSum;
	//重量
	@ExcelField(title = "重量", order = 4)
	private BigDecimal weight;
	//中转费
	@ExcelField(title = "中转费", order = 5)
	private BigDecimal tsfFee;
	//派费
	@ExcelField(title = "派费", order = 6)
	private BigDecimal deliveryFee;
	//续重派费
	@ExcelField(title = "续重派费", order = 7)
	private BigDecimal deliveryAdditionalWeightFee;
	//平衡派费
	@ExcelField(title = "平衡派费", order = 8)
	private BigDecimal deliveryBalanceFee;
	//面单费
	@ExcelField(title = "面单费", order = 9)
	private BigDecimal shipmentFee;
	//扫描费73
	@ExcelField(title = "扫描费73", order = 10)
	private BigDecimal scanFee;
	//客户编码
	@ExcelField(title = "客户编码", order = 11)
	private String customerId;
	//网点编码
	@ExcelField(title = "网点编码", order = 12)
	private Integer branchCode;
	//创建时间
	@ExcelField(title = "创建时间", order = 13)
	private Integer createTime;
	//客户名称
	@ExcelField(title = "客户名称", order = 14)
	private String customerName;
		
	//人力成本
	@ExcelField(title = "人力成本", order = 15)
	private BigDecimal peopleCost;
	//物料费
	@ExcelField(title = "物料费", order = 16)
	private BigDecimal materielCost;
	//运输成本
	@ExcelField(title = "运输成本", order = 17)
	private BigDecimal tsfCost;
	//回扣
	@ExcelField(title = "回扣", order = 18)
	private BigDecimal hhCost;
	//税金
	@ExcelField(title = "税金", order = 19)
	private BigDecimal taxesCost;
	//票均包仓费
	@ExcelField(title = "票均包仓费", order = 20)
	private BigDecimal packingCharge;
	//其他费用
	@ExcelField(title = "其他费用", order = 21)
	private BigDecimal otherCost;
	//始发省ID
	@ExcelField(title = "始发省ID", order = 22)
	private Integer startProvinceId;
	//目的省ID
	@ExcelField(title = "目的省ID", order = 23)
	private Integer endProvinceId;
	//始发省名称
	@ExcelField(title = "始发省名称", order = 24)
	private String startProvinceName;
	//目的省名称
	@ExcelField(title = "目的省名称", order = 25)
	private String endProvinceName;
	
	//商家id和店铺名称
	@ExcelField(title = "商家id", order = 19)
	private String sellerId;
	@ExcelField(title = "店铺名称", order = 20)
	private String sellerName;
	
	
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
	public BigDecimal getOtherCost() {
		return otherCost;
	}
	public void setOtherCost(BigDecimal otherCost) {
		this.otherCost = otherCost;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	//客户来源
	@ExcelField(title = "客户来源", order = 23)
	private String customerSourceType;
	
	public String getCustomerSourceType() {
		return customerSourceType;
	}
	public void setCustomerSourceType(String customerSourceType) {
		this.customerSourceType = customerSourceType;
	}
	/**
	 * 设置：数据记录主键ID
	 */
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	/**
	 * 获取：数据记录主键ID
	 */
	public Integer getRecordId() {
		return recordId;
	}
	/**
	 * 设置：下单日期
	 */
	public void setAccountDt(Integer accountDt) {
		this.accountDt = accountDt;
	}
	/**
	 * 获取：下单日期
	 */
	public Integer getAccountDt() {
		return accountDt;
	}
	/**
	 * 设置：票数
	 */
	public void setOrderSum(Integer orderSum) {
		this.orderSum = orderSum;
	}
	/**
	 * 获取：票数
	 */
	public Integer getOrderSum() {
		return orderSum;
	}
	/**
	 * 设置：重量
	 */
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	/**
	 * 获取：重量
	 */
	public BigDecimal getWeight() {
		return weight;
	}
	/**
	 * 设置：中转费
	 */
	public void setTsfFee(BigDecimal tsfFee) {
		this.tsfFee = tsfFee;
	}
	/**
	 * 获取：中转费
	 */
	public BigDecimal getTsfFee() {
		return tsfFee;
	}
	/**
	 * 设置：派费
	 */
	public void setDeliveryFee(BigDecimal deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
	/**
	 * 获取：派费
	 */
	public BigDecimal getDeliveryFee() {
		return deliveryFee;
	}
	/**
	 * 设置：续重派费
	 */
	public void setDeliveryAdditionalWeightFee(BigDecimal deliveryAdditionalWeightFee) {
		this.deliveryAdditionalWeightFee = deliveryAdditionalWeightFee;
	}
	/**
	 * 获取：续重派费
	 */
	public BigDecimal getDeliveryAdditionalWeightFee() {
		return deliveryAdditionalWeightFee;
	}
	/**
	 * 设置：平衡派费
	 */
	public void setDeliveryBalanceFee(BigDecimal deliveryBalanceFee) {
		this.deliveryBalanceFee = deliveryBalanceFee;
	}
	/**
	 * 获取：平衡派费
	 */
	public BigDecimal getDeliveryBalanceFee() {
		return deliveryBalanceFee;
	}
	/**
	 * 设置：面单费
	 */
	public void setShipmentFee(BigDecimal shipmentFee) {
		this.shipmentFee = shipmentFee;
	}
	/**
	 * 获取：面单费
	 */
	public BigDecimal getShipmentFee() {
		return shipmentFee;
	}
	/**
	 * 设置：扫描费73
	 */
	public void setScanFee(BigDecimal scanFee) {
		this.scanFee = scanFee;
	}
	/**
	 * 获取：扫描费73
	 */
	public BigDecimal getScanFee() {
		return scanFee;
	}
	/**
	 * 设置：客户编码
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	/**
	 * 获取：客户编码
	 */
	public String getCustomerId() {
		return customerId;
	}
	/**
	 * 设置：网点编码
	 */
	public void setBranchCode(Integer branchCode) {
		this.branchCode = branchCode;
	}
	/**
	 * 获取：网点编码
	 */
	public Integer getBranchCode() {
		return branchCode;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Integer getCreateTime() {
		return createTime;
	}
}
