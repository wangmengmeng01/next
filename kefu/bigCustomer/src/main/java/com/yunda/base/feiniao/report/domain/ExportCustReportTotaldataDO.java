package com.yunda.base.feiniao.report.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;


/**
 * 客户报表订单统计-总表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-11 14:54:40
 */
public class ExportCustReportTotaldataDO implements Serializable {
	private static final long serialVersionUID = 1L;

	//客户编码
	@ExcelField(title = "客户编码", order = 1)
	private String customerId;
	//客户名称
	@ExcelField(title = "客户名称", order = 2)
	private String customerName;
	//上级公司编码
	@ExcelField(title = "上级公司编码", order = 3)
	private String branchCode;
	//上级公司名称
	@ExcelField(title = "上级公司名称", order = 4)
	private String branchName;	
	//客户来源
	@ExcelField(title = "客户来源", order = 5)
	private String customerSourceType;
	//网点编码
	@ExcelField(title = "网点编码", order = 6)
	private String yjbm;
	//网点名称
	@ExcelField(title = "网点名称", order = 7)
	private String yjmc;
	//单量
	@ExcelField(title = "单量", order = 8)
	private String orderSum;
	//平均单量
	@ExcelField(title = "平均单量", order = 9)
	private String orderAvg;
	//客户类别
	@ExcelField(title = "客户类别", order = 10)
	private String priceLevel;	
	//金额
	@ExcelField(title = "金额", order = 11)
	private String priceSum;
	//全部金额
	@ExcelField(title = "全部金额", order = 12)
	private String dianziOrderSum;
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
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getCustomerSourceType() {
		return customerSourceType;
	}
	public void setCustomerSourceType(String customerSourceType) {
		this.customerSourceType = customerSourceType;
	}
	public String getYjbm() {
		return yjbm;
	}
	public void setYjbm(String yjbm) {
		this.yjbm = yjbm;
	}
	public String getYjmc() {
		return yjmc;
	}
	public void setYjmc(String yjmc) {
		this.yjmc = yjmc;
	}
	public String getOrderSum() {
		return orderSum;
	}
	public void setOrderSum(String orderSum) {
		this.orderSum = orderSum;
	}
	public String getOrderAvg() {
		return orderAvg;
	}
	public void setOrderAvg(String orderAvg) {
		this.orderAvg = orderAvg;
	}
	public String getPriceLevel() {
		return priceLevel;
	}
	public void setPriceLevel(String priceLevel) {
		this.priceLevel = priceLevel;
	}
	public String getPriceSum() {
		return priceSum;
	}
	public void setPriceSum(String priceSum) {
		this.priceSum = priceSum;
	}
	public String getDianziOrderSum() {
		return dianziOrderSum;
	}
	public void setDianziOrderSum(String dianziOrderSum) {
		this.dianziOrderSum = dianziOrderSum;
	}

}
