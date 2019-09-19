package com.yunda.base.feiniao.costreport.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.github.crab2died.annotation.ExcelField;


/**
 * 客户报表订单统计/客户月结账单
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-11-09140854
 */
public class CostreportOrderCostChangeDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//数据记录主键ID
	@ExcelField(title = "数据记录主键ID", order = 1)
	private Integer recordId;
	//揽件日期
	@ExcelField(title = "揽件日期", order = 2)
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
	//调整
	@ExcelField(title = "调整", order = 8)
	private BigDecimal changeIncome;
	//实收
	@ExcelField(title = "实收", order = 9)
	private BigDecimal actualIncome;
	//已收款金额
	@ExcelField(title = "已收款金额", order = 10)
	private BigDecimal finishIncome;
	//is_finish
	@ExcelField(title = "is_finish", order = 11)
	private String isFinish;
	//creater
	@ExcelField(title = "creater", order = 12)
	private String creater;
	//creater_branch
	@ExcelField(title = "creater_branch", order = 13)
	private Integer createrBranch;
	//create_time
	@ExcelField(title = "create_time", order = 14)
	private Date createTime;
	//editer
	@ExcelField(title = "editer", order = 15)
	private String editer;
	//editer_branch
	@ExcelField(title = "editer_branch", order = 16)
	private Integer editerBranch;
	//edit_time
	@ExcelField(title = "edit_time", order = 17)
	private Date editTime;
	//揽件重量
	@ExcelField(title = "揽件重量", order = 18)
	private BigDecimal senderWeight;
	//T+3重量
	@ExcelField(title = "T+3重量", order = 19)
	private BigDecimal t3Weight;   
    /**
     * 目的省
     */
    private String destinationProvinceId;
	    
    /**
     * 预估件数
     */
    private BigDecimal calculateOrderSum;

    /**
     * 预估均重
     */
    private BigDecimal calculateWeightAll;    
    
    /**
     * 单量
     */
    private String calculateSum; 
	    
    /**
     * 始发地
     */
    private String provenanceProvinceId;
	    
    /**
     * 面单类型
     */
    private String orderType;
	    
    /**
     * 调整类型
     */
    private String reduce;

	public String getDestinationProvinceId() {
		return destinationProvinceId;
	}
	public void setDestinationProvinceId(String destinationProvinceId) {
		this.destinationProvinceId = destinationProvinceId;
	}
	public BigDecimal getCalculateOrderSum() {
		return calculateOrderSum;
	}
	public void setCalculateOrderSum(BigDecimal calculateOrderSum) {
		this.calculateOrderSum = calculateOrderSum;
	}
	public BigDecimal getCalculateWeightAll() {
		return calculateWeightAll;
	}
	public void setCalculateWeightAll(BigDecimal calculateWeightAll) {
		this.calculateWeightAll = calculateWeightAll;
	}
	public String getCalculateSum() {
		return calculateSum;
	}
	public void setCalculateSum(String calculateSum) {
		this.calculateSum = calculateSum;
	}
	public String getProvenanceProvinceId() {
		return provenanceProvinceId;
	}
	public void setProvenanceProvinceId(String provenanceProvinceId) {
		this.provenanceProvinceId = provenanceProvinceId;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getReduce() {
		return reduce;
	}
	public void setReduce(String reduce) {
		this.reduce = reduce;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
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
	 * 设置：揽件日期
	 */
	public void setAccountDt(Integer accountDt) {
		this.accountDt = accountDt;
	}
	/**
	 * 获取：揽件日期
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
	 * 设置：调整
	 */
	public void setChangeIncome(BigDecimal changeIncome) {
		this.changeIncome = changeIncome;
	}
	/**
	 * 获取：调整
	 */
	public BigDecimal getChangeIncome() {
		return changeIncome;
	}
	/**
	 * 设置：实收
	 */
	public void setActualIncome(BigDecimal actualIncome) {
		this.actualIncome = actualIncome;
	}
	/**
	 * 获取：实收
	 */
	public BigDecimal getActualIncome() {
		return actualIncome;
	}
	/**
	 * 设置：已收款金额
	 */
	public void setFinishIncome(BigDecimal finishIncome) {
		this.finishIncome = finishIncome;
	}
	/**
	 * 获取：已收款金额
	 */
	public BigDecimal getFinishIncome() {
		return finishIncome;
	}
	/**
	 * 设置：is_finish
	 */
	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}
	/**
	 * 获取：is_finish
	 */
	public String getIsFinish() {
		return isFinish;
	}
	/**
	 * 设置：creater
	 */
	public void setCreater(String creater) {
		this.creater = creater;
	}
	/**
	 * 获取：creater
	 */
	public String getCreater() {
		return creater;
	}
	/**
	 * 设置：creater_branch
	 */
	public void setCreaterBranch(Integer createrBranch) {
		this.createrBranch = createrBranch;
	}
	/**
	 * 获取：creater_branch
	 */
	public Integer getCreaterBranch() {
		return createrBranch;
	}
	/**
	 * 设置：create_time
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：create_time
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：editer
	 */
	public void setEditer(String editer) {
		this.editer = editer;
	}
	/**
	 * 获取：editer
	 */
	public String getEditer() {
		return editer;
	}
	/**
	 * 设置：editer_branch
	 */
	public void setEditerBranch(Integer editerBranch) {
		this.editerBranch = editerBranch;
	}
	/**
	 * 获取：editer_branch
	 */
	public Integer getEditerBranch() {
		return editerBranch;
	}
	/**
	 * 设置：edit_time
	 */
	public void setEditTime(Date editTime) {
		this.editTime = editTime;
	}
	/**
	 * 获取：edit_time
	 */
	public Date getEditTime() {
		return editTime;
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
