package com.yunda.base.feiniao.report.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-16 10:45:24
 */
public class ExportReportWarningBranchDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//bigarea
	@ExcelField(title = "大区", order = 1)
	private String bigarea;
	//省名称
	@ExcelField(title = "省", order = 2)
	private String provinceName;
	//市名称
	@ExcelField(title = "市", order = 3)
	private String cityName;
	//yjmc
	@ExcelField(title = "所属网点", order = 4)
	private String yjmc;
	//所属1级网点名称
	@ExcelField(title = "上级公司", order = 5)
	private String branchName;
	//customer_id
	@ExcelField(title = "客户编码", order = 6)
	private String customerId;
	//客户名称
	@ExcelField(title = "客户名称", order = 7)
	private String customerName;
	
	@ExcelField(title = "商家id", order = 8)
	private String sellerId;
	@ExcelField(title = "店铺名称", order = 9)
	private String sellerName;
	@ExcelField(title = "客户电话", order = 10)
	private String mobile;
	@ExcelField(title = "客户公司地址", order = 11)
	private String gsaddr;
	//页面展示客户来源
	@ExcelField(title = "客户来源", order = 12)
	private String showCustomerSourceType;
	@ExcelField(title = "揽件量", order = 13)
	private double lastOrderSum;
	//last_order_avg
	@ExcelField(title = "日均量", order = 14)
	private double lastOrderAvg;
	//order_avg
	@ExcelField(title = "上周日均量", order = 15)
	private double orderAvg;
	@ExcelField(title = "周环比", order = 16)
	private String monthRatio;
	//页面展示客户分类
	@ExcelField(title = "客户类型", order = 17)
	private String showPriceLevel;
	public String getBigarea() {
		return bigarea;
	}
	public void setBigarea(String bigarea) {
		this.bigarea = bigarea;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getYjmc() {
		return yjmc;
	}
	public void setYjmc(String yjmc) {
		this.yjmc = yjmc;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getGsaddr() {
		return gsaddr;
	}
	public void setGsaddr(String gsaddr) {
		this.gsaddr = gsaddr;
	}
	public String getShowCustomerSourceType() {
		return showCustomerSourceType;
	}
	public void setShowCustomerSourceType(String showCustomerSourceType) {
		this.showCustomerSourceType = showCustomerSourceType;
	}
	public double getLastOrderSum() {
		return lastOrderSum;
	}
	public void setLastOrderSum(double lastOrderSum) {
		this.lastOrderSum = lastOrderSum;
	}
	public double getLastOrderAvg() {
		return lastOrderAvg;
	}
	public void setLastOrderAvg(double lastOrderAvg) {
		this.lastOrderAvg = lastOrderAvg;
	}
	public double getOrderAvg() {
		return orderAvg;
	}
	public void setOrderAvg(double orderAvg) {
		this.orderAvg = orderAvg;
	}
	public String getMonthRatio() {
		return monthRatio;
	}
	public void setMonthRatio(String monthRatio) {
		this.monthRatio = monthRatio;
	}
	public String getShowPriceLevel() {
		return showPriceLevel;
	}
	public void setShowPriceLevel(String showPriceLevel) {
		this.showPriceLevel = showPriceLevel;
	}
	
	

}
