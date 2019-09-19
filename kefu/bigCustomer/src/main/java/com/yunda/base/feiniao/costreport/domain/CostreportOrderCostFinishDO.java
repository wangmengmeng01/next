package com.yunda.base.feiniao.costreport.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.github.crab2died.annotation.ExcelField;


/**
 * 客户报表订单统计/客户收益报表(完成统计)
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-14092455
 */
public class CostreportOrderCostFinishDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//数据记录主键ID
	@ExcelField(title = "数据记录主键ID", order = 1)
	private Integer recordId;
	//下单日期
	@ExcelField(title = "下单日期", order = 2)
	private Integer accountDt;
	//客户编码
	@ExcelField(title = "客户编码", order = 3)
	private String customerId;
	//网点编码
	@ExcelField(title = "网点编码", order = 4)
	private Integer branchCode;
	//票数
	@ExcelField(title = "票数", order = 5)
	private Integer orderSum;
	//总重量
	@ExcelField(title = "总重量", order = 6)
	private BigDecimal weightAll;
	//收入
	@ExcelField(title = "收入", order = 7)
	private BigDecimal income;
	//支出
	@ExcelField(title = "支出", order = 8)
	private BigDecimal expenditure;
	//利润
	@ExcelField(title = "利润", order = 9)
	private BigDecimal profit;
	//单票收入
	@ExcelField(title = "单票收入", order = 10)
	private BigDecimal incomeEach;
	//单公斤收入
	@ExcelField(title = "单公斤收入", order = 11)
	private BigDecimal kilogramEach;
	//单票收益
	@ExcelField(title = "单票收益", order = 12)
	private BigDecimal profitEach;
	//单公斤收益
	@ExcelField(title = "单公斤收益", order = 13)
	private BigDecimal kilogramProfitEach;
	//创建时间
	@ExcelField(title = "创建时间", order = 14)
	private Integer createTime;
	//结算标识
	@ExcelField(title = "结算标识", order = 15)
	private String finishFlag;
	//揽件重量
	@ExcelField(title = "揽件重量", order = 16)
	private BigDecimal senderWeight;
	//T+3重量
	@ExcelField(title = "T+3重量", order = 17)
	private BigDecimal t3Weight;

	//客户名称
	@ExcelField(title = "客户名称", order = 18)
	private String customerName;

	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
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
	 * 设置：总重量
	 */
	public void setWeightAll(BigDecimal weightAll) {
		this.weightAll = weightAll;
	}
	/**
	 * 获取：总重量
	 */
	public BigDecimal getWeightAll() {
		return weightAll;
	}
	/**
	 * 设置：收入
	 */
	public void setIncome(BigDecimal income) {
		this.income = income;
	}
	/**
	 * 获取：收入
	 */
	public BigDecimal getIncome() {
		return income;
	}
	/**
	 * 设置：支出
	 */
	public void setExpenditure(BigDecimal expenditure) {
		this.expenditure = expenditure;
	}
	/**
	 * 获取：支出
	 */
	public BigDecimal getExpenditure() {
		return expenditure;
	}
	/**
	 * 设置：利润
	 */
	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}
	/**
	 * 获取：利润
	 */
	public BigDecimal getProfit() {
		return profit;
	}
	/**
	 * 设置：单票收入
	 */
	public void setIncomeEach(BigDecimal incomeEach) {
		this.incomeEach = incomeEach;
	}
	/**
	 * 获取：单票收入
	 */
	public BigDecimal getIncomeEach() {
		return incomeEach;
	}
	/**
	 * 设置：单公斤收入
	 */
	public void setKilogramEach(BigDecimal kilogramEach) {
		this.kilogramEach = kilogramEach;
	}
	/**
	 * 获取：单公斤收入
	 */
	public BigDecimal getKilogramEach() {
		return kilogramEach;
	}
	/**
	 * 设置：单票收益
	 */
	public void setProfitEach(BigDecimal profitEach) {
		this.profitEach = profitEach;
	}
	/**
	 * 获取：单票收益
	 */
	public BigDecimal getProfitEach() {
		return profitEach;
	}
	/**
	 * 设置：单公斤收益
	 */
	public void setKilogramProfitEach(BigDecimal kilogramProfitEach) {
		this.kilogramProfitEach = kilogramProfitEach;
	}
	/**
	 * 获取：单公斤收益
	 */
	public BigDecimal getKilogramProfitEach() {
		return kilogramProfitEach;
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
	/**
	 * 设置：结算标识
	 */
	public void setFinishFlag(String finishFlag) {
		this.finishFlag = finishFlag;
	}
	/**
	 * 获取：结算标识
	 */
	public String getFinishFlag() {
		return finishFlag;
	}
	/**
	 * 设置：揽件重量
	 */
	public void setSenderWeight(BigDecimal senderWeight) {
		this.senderWeight = senderWeight;
	}
	/**
	 * 获取：揽件重量
	 */
	public BigDecimal getSenderWeight() {
		return senderWeight;
	}
	/**
	 * 设置：T+3重量
	 */
	public void setT3Weight(BigDecimal t3Weight) {
		this.t3Weight = t3Weight;
	}
	/**
	 * 获取：T+3重量
	 */
	public BigDecimal getT3Weight() {
		return t3Weight;
	}
}
