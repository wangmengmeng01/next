package com.yunda.base.feiniao.costreport.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.github.crab2died.annotation.ExcelField;


/**
 * 客户报表订单统计/客户拓展支出
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-13145432
 */
public class CostreportCustCostExtDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//数据记录主键ID
	@ExcelField(title = "数据记录主键ID", order = 1)
	private Integer recordId;
	//开始日期
	@ExcelField(title = "开始日期", order = 2)
	private Date startAccountDt;
	//结束日期
	@ExcelField(title = "结束日期", order = 3)
	private Date endAccountDt;
	//人力成本
	@ExcelField(title = "人力成本", order = 4)
	private BigDecimal peopleCost;
	//物料费
	@ExcelField(title = "物料费", order = 5)
	private BigDecimal materielCost;
	//运输成本
	@ExcelField(title = "运输成本", order = 6)
	private BigDecimal tsfCost;
	//回扣
	@ExcelField(title = "回扣", order = 7)
	private BigDecimal hhCost;
	//税金
	@ExcelField(title = "税金", order = 8)
	private BigDecimal taxesCost;
	//票均包仓费
	@ExcelField(title = "票均包仓费", order = 9)
	private BigDecimal packingCharge;
	//包仓费类型:0按公斤,1按票
	@ExcelField(title = "包仓费类型:0按公斤,1按票", order = 10)
	private Integer packingChargeFlag;
	//其他费用
	@ExcelField(title = "其他费用", order = 11)
	private BigDecimal otherCost;
	//网点编码
	@ExcelField(title = "网点编码", order = 12)
	private Integer branchCode;
	//网点名称
	@ExcelField(title = "网点名称", order = 13)
	private String branchName;
	//客户编码
	@ExcelField(title = "客户编码", order = 14)
	private String customerId;
	//客户名称
	@ExcelField(title = "客户名称", order = 15)
	private String customerName;
	//客户来源
	@ExcelField(title = "客户来源", order = 16)
	private String customerSourceType;

 

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
	 * 设置：开始日期
	 */
	public void setStartAccountDt(Date startAccountDt) {
		this.startAccountDt = startAccountDt;
	}
	/**
	 * 获取：开始日期
	 */
	public Date getStartAccountDt() {
		return startAccountDt;
	}
	/**
	 * 设置：结束日期
	 */
	public void setEndAccountDt(Date endAccountDt) {
		this.endAccountDt = endAccountDt;
	}
	/**
	 * 获取：结束日期
	 */
	public Date getEndAccountDt() {
		return endAccountDt;
	}
	/**
	 * 设置：人力成本
	 */
	public void setPeopleCost(BigDecimal peopleCost) {
		this.peopleCost = peopleCost;
	}
	/**
	 * 获取：人力成本
	 */
	public BigDecimal getPeopleCost() {
		return peopleCost;
	}
	/**
	 * 设置：物料费
	 */
	public void setMaterielCost(BigDecimal materielCost) {
		this.materielCost = materielCost;
	}
	/**
	 * 获取：物料费
	 */
	public BigDecimal getMaterielCost() {
		return materielCost;
	}
	/**
	 * 设置：运输成本
	 */
	public void setTsfCost(BigDecimal tsfCost) {
		this.tsfCost = tsfCost;
	}
	/**
	 * 获取：运输成本
	 */
	public BigDecimal getTsfCost() {
		return tsfCost;
	}
	/**
	 * 设置：回扣
	 */
	public void setHhCost(BigDecimal hhCost) {
		this.hhCost = hhCost;
	}
	/**
	 * 获取：回扣
	 */
	public BigDecimal getHhCost() {
		return hhCost;
	}
	/**
	 * 设置：税金
	 */
	public void setTaxesCost(BigDecimal taxesCost) {
		this.taxesCost = taxesCost;
	}
	/**
	 * 获取：税金
	 */
	public BigDecimal getTaxesCost() {
		return taxesCost;
	}
	/**
	 * 设置：票均包仓费
	 */
	public void setPackingCharge(BigDecimal packingCharge) {
		this.packingCharge = packingCharge;
	}
	/**
	 * 获取：票均包仓费
	 */
	public BigDecimal getPackingCharge() {
		return packingCharge;
	}
	/**
	 * 设置：包仓费类型:0按公斤,1按票
	 */
	public void setPackingChargeFlag(Integer packingChargeFlag) {
		this.packingChargeFlag = packingChargeFlag;
	}
	/**
	 * 获取：包仓费类型:0按公斤,1按票
	 */
	public Integer getPackingChargeFlag() {
		return packingChargeFlag;
	}
	/**
	 * 设置：其他费用
	 */
	public void setOtherCost(BigDecimal otherCost) {
		this.otherCost = otherCost;
	}
	/**
	 * 获取：其他费用
	 */
	public BigDecimal getOtherCost() {
		return otherCost;
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
	 * 设置：网点名称
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	/**
	 * 获取：网点名称
	 */
	public String getBranchName() {
		return branchName;
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
	 * 设置：客户名称
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	/**
	 * 获取：客户名称
	 */
	public String getCustomerName() {
		return customerName;
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
}
