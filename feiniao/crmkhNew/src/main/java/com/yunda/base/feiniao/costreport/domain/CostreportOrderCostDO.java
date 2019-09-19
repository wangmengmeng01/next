package com.yunda.base.feiniao.costreport.domain;

import com.github.crab2died.annotation.ExcelField;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 客户报表订单统计/客户单票成本
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @String 2018-09-14092504
 */
public class CostreportOrderCostDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//数据记录主键ID
	@ExcelField(title = "数据记录主键ID", order = 1)
	private Integer recordId;
	//结算日期
	@ExcelField(title = "结算日期", order = 2)
	private String accountDt;
	//运单号
	@ExcelField(title = "运单号", order = 3)
	private Long shipmentNo;
	//始发城市ID
	@ExcelField(title = "始发城市ID", order = 4)
	private Integer startCityId;
	//始发省ID
	@ExcelField(title = "始发省ID", order = 5)
	private Integer startProvinceId;
	//目的城市ID
	@ExcelField(title = "目的城市ID", order = 6)
	private Integer endCityId;
	//目的省ID
	@ExcelField(title = "目的省ID", order = 7)
	private Integer endProvinceId;
	//重量
	@ExcelField(title = "重量", order = 8)
	private BigDecimal weight;
	//首重
	@ExcelField(title = "首重", order = 9)
	private BigDecimal firstWeight;
	//首重费
	@ExcelField(title = "首重费", order = 10)
	private BigDecimal firstWeightFee;
	//续重
	@ExcelField(title = "续重", order = 11)
	private BigDecimal additionalWeight;
	//续重费
	@ExcelField(title = "续重费", order = 12)
	private BigDecimal additionalWeightFee;
	//运费
	@ExcelField(title = "运费", order = 13)
	private BigDecimal fee;
	//中转费
	@ExcelField(title = "中转费", order = 14)
	private BigDecimal tsfFee;
	//派费
	@ExcelField(title = "派费", order = 15)
	private BigDecimal deliveryFee;
	//续重派费
	@ExcelField(title = "续重派费", order = 16)
	private BigDecimal deliveryAdditionalWeightFee;
	//平衡派费
	@ExcelField(title = "平衡派费", order = 17)
	private BigDecimal deliveryBalanceFee;
	//面单费
	@ExcelField(title = "面单费", order = 18)
	private BigDecimal shipmentFee;
	//扫描费73
	@ExcelField(title = "扫描费73", order = 19)
	private BigDecimal scanFee;
	//网点编码
	@ExcelField(title = "网点编码", order = 20)
	private Integer branchCode;
	//客户编码
	@ExcelField(title = "客户编码", order = 21)
	private String customerId;
	//客户来源
	@ExcelField(title = "客户来源", order = 22)
	private String customerSourceType;
	//结算费用计费方式
	@ExcelField(title = "结算费用计费方式", order = 23)
	private String chargeModeFlag;
	//创建时间
	@ExcelField(title = "创建时间", order = 24)
	private String createTime;
	//TD结算日期
	@ExcelField(title = "TD结算日期", order = 25)
	private String tdAccountDt;
	//揽件日期
	@ExcelField(title = "揽件日期", order = 26)
	private String senderAccount;
	//下单日期
	@ExcelField(title = "下单日期", order = 27)
	private String orderAccount;
	//揽件日期_时分秒
	@ExcelField(title = "揽件日期_时分秒", order = 28)
	private String senderAccountDt;
	//揽件日期_时分秒
	@ExcelField(title = "下单日期_时分秒", order = 29)
	private String orderAccountDt;
	//揽件日期_时分秒
	@ExcelField(title = "揽件重量", order = 30)
	private String senderWeight;
	//T+3重量
	@ExcelField(title = "T+3重量", order = 31)
	private String T3Weight;
	//客户名称
	@ExcelField(title = "客户名称", order = 32)
	private String customerName;
	//起始省
	@ExcelField(title = "起始省", order = 33)
	private String startProvinceName;
	//结束省
	@ExcelField(title = "结束省", order = 34)
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getSenderAccount() {
		return senderAccount;
	}
	public void setSenderAccount(String senderAccount) {
		this.senderAccount = senderAccount;
	}
	public String getOrderAccount() {
		return orderAccount;
	}
	public void setOrderAccount(String orderAccount) {
		this.orderAccount = orderAccount;
	}
	public String getSenderWeight() {
		return senderWeight;
	}
	public void setSenderWeight(String senderWeight) {
		this.senderWeight = senderWeight;
	}
	public String getT3Weight() {
		return T3Weight;
	}
	public void setT3Weight(String t3Weight) {
		T3Weight = t3Weight;
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
	 * 设置：结算日期
	 */
	public void setAccountDt(String accountDt) {
		this.accountDt = accountDt;
	}
	/**
	 * 获取：结算日期
	 */
	public String getAccountDt() {
		return accountDt;
	}
	/**
	 * 设置：运单号
	 */
	public void setShipmentNo(Long shipmentNo) {
		this.shipmentNo = shipmentNo;
	}
	/**
	 * 获取：运单号
	 */
	public Long getShipmentNo() {
		return shipmentNo;
	}
	/**
	 * 设置：始发城市ID
	 */
	public void setStartCityId(Integer startCityId) {
		this.startCityId = startCityId;
	}
	/**
	 * 获取：始发城市ID
	 */
	public Integer getStartCityId() {
		return startCityId;
	}
	/**
	 * 设置：始发省ID
	 */
	public void setStartProvinceId(Integer startProvinceId) {
		this.startProvinceId = startProvinceId;
	}
	/**
	 * 获取：始发省ID
	 */
	public Integer getStartProvinceId() {
		return startProvinceId;
	}
	/**
	 * 设置：目的城市ID
	 */
	public void setEndCityId(Integer endCityId) {
		this.endCityId = endCityId;
	}
	/**
	 * 获取：目的城市ID
	 */
	public Integer getEndCityId() {
		return endCityId;
	}
	/**
	 * 设置：目的省ID
	 */
	public void setEndProvinceId(Integer endProvinceId) {
		this.endProvinceId = endProvinceId;
	}
	/**
	 * 获取：目的省ID
	 */
	public Integer getEndProvinceId() {
		return endProvinceId;
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
	 * 设置：首重
	 */
	public void setFirstWeight(BigDecimal firstWeight) {
		this.firstWeight = firstWeight;
	}
	/**
	 * 获取：首重
	 */
	public BigDecimal getFirstWeight() {
		return firstWeight;
	}
	/**
	 * 设置：首重费
	 */
	public void setFirstWeightFee(BigDecimal firstWeightFee) {
		this.firstWeightFee = firstWeightFee;
	}
	/**
	 * 获取：首重费
	 */
	public BigDecimal getFirstWeightFee() {
		return firstWeightFee;
	}
	/**
	 * 设置：续重
	 */
	public void setAdditionalWeight(BigDecimal additionalWeight) {
		this.additionalWeight = additionalWeight;
	}
	/**
	 * 获取：续重
	 */
	public BigDecimal getAdditionalWeight() {
		return additionalWeight;
	}
	/**
	 * 设置：续重费
	 */
	public void setAdditionalWeightFee(BigDecimal additionalWeightFee) {
		this.additionalWeightFee = additionalWeightFee;
	}
	/**
	 * 获取：续重费
	 */
	public BigDecimal getAdditionalWeightFee() {
		return additionalWeightFee;
	}
	/**
	 * 设置：运费
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	/**
	 * 获取：运费
	 */
	public BigDecimal getFee() {
		return fee;
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
	 * 设置：客户来源
	 */
	public void setCustomerSourceType(String customerSourceType) {
		this.customerSourceType = customerSourceType;
	}
	/**
	 * 获取：客户来源
	 */
	public String getCustomerSourceType() {
		return customerSourceType;
	}
	/**
	 * 设置：结算费用计费方式
	 */
	public void setChargeModeFlag(String chargeModeFlag) {
		this.chargeModeFlag = chargeModeFlag;
	}
	/**
	 * 获取：结算费用计费方式
	 */
	public String getChargeModeFlag() {
		return chargeModeFlag;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：TD结算日期
	 */
	public void setTdAccountDt(String tdAccountDt) {
		this.tdAccountDt = tdAccountDt;
	}
	/**
	 * 获取：TD结算日期
	 */
	public String getTdAccountDt() {
		return tdAccountDt;
	}
	/**
	 * 设置：揽件日期
	 */
	public void setSenderAccountDt(String senderAccountDt) {
		this.senderAccountDt = senderAccountDt;
	}
	/**
	 * 获取：揽件日期
	 */
	public String getSenderAccountDt() {
		return senderAccountDt;
	}
	/**
	 * 设置：下单日期
	 */
	public void setOrderAccountDt(String orderAccountDt) {
		this.orderAccountDt = orderAccountDt;
	}
	/**
	 * 获取：下单日期
	 */
	public String getOrderAccountDt() {
		return orderAccountDt;
	}
}
