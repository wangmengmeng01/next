package com.yunda.base.feiniao.report.domain;

import com.github.crab2died.annotation.ExcelField;

import java.io.Serializable;


/**
 * 客户奖励明细表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-17153137
 */
public class ReportCustRewardDetailsDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//数据记录主键ID
	@ExcelField(title = "数据记录主键ID", order = 1)
	private Integer recordId;
	
	//大区名
	@ExcelField(title = "大区名", order = 1)
	private String bigarea;
	//省名称
	@ExcelField(title = "省名称", order = 2)
	private String provinceName;
	
	//省名称
	@ExcelField(title = "省id", order = 2)
	private String provinceID;
	
	//上级站点
	@ExcelField(title = "上级站点", order = 4)
	private String branchCode;
	//网点编码
	@ExcelField(title = "网点编码", order = 5)
	private String gs;
	//网点名称
	@ExcelField(title = "网点名称", order = 6)
	private String mc;
	//市id
	@ExcelField(title = "市id", order = 2)
	private String CityID;
	//市名称
	@ExcelField(title = "市名称", order = 3)
	private String cityName;
	//customer_id
	@ExcelField(title = "customer_id", order = 7)
	private String customerId;
	//客户名称
	@ExcelField(title = "客户名称", order = 8)
	private String customerName;
	//客户来源（数据库查询原始数据）
	@ExcelField(title = "客户来源", order = 9)
	private String customerSourceType;

	//揽件量
	@ExcelField(title = "揽件量", order = 10)
	private Double orderSum;
	//日均揽件量
	@ExcelField(title = "日均揽件量", order = 11)
	private Double orderAvg;
	//奖励金额
	@ExcelField(title = "奖励金额", order = 12)
	private Double allPriceSum;
	
	//客户类型
	@ExcelField(title = "客户类型", order = 13)
	private String custLevel;
	
	//公司编码
	private String bm;

	private String sellerId;
	private String sellerName;
	
	//页面展示客户来源
	private String showCustomerSourceType;
	//页面展示客户类别
	private String showCustLevel;
	
	public String getCustomerSourceType() {
		return customerSourceType;
	}

	public void setCustomerSourceType(String customerSourceType) {
		this.customerSourceType = customerSourceType;
	}
	
	
	public String getShowCustLevel() {
		if("a".equals(custLevel)){
			showCustLevel =  "A类";
   	 }else if("b".equals(custLevel)){
   		showCustLevel =  "B类";
   	 }else if("c".equals(custLevel)){
   		showCustLevel =  "C类";
   	 }else if("d".equals(custLevel)){
   		showCustLevel =  "D类";
   	 }else if("e".equals(custLevel)){
   		showCustLevel =  "E类";
   	 }else if("f".equals(custLevel)){
   		showCustLevel =  "F类";
   	 }else if("g".equals(custLevel)){
   		showCustLevel =  "G类";
   	 }
		return showCustLevel;
	}

	public void setShowCustLevel(String showCustLevel) {
		this.showCustLevel = showCustLevel;
	}

	public String getShowCustomerSourceType(){
		if("1".equals(customerSourceType)){
			showCustomerSourceType = "菜鸟";
   	 }else if("2".equals(customerSourceType)){
   		 	showCustomerSourceType = "二维码";
   	 }else if("4".equals(customerSourceType)){
   		 	showCustomerSourceType ="京东";
   	 }else if("5".equals(customerSourceType)){
   		 	showCustomerSourceType ="拼多多";
   	 }
		return showCustomerSourceType;
	}
	
	public void setShowCustomerSourceType(String showCustomerSourceType) {
		this.showCustomerSourceType = showCustomerSourceType;
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

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

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

	public String getProvinceID() {
		return provinceID;
	}

	public void setProvinceID(String provinceID) {
		this.provinceID = provinceID;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getGs() {
		return gs;
	}

	public void setGs(String gs) {
		this.gs = gs;
	}

	public String getMc() {
		return mc;
	}

	public void setMc(String mc) {
		this.mc = mc;
	}

	public String getCityID() {
		return CityID;
	}

	public void setCityID(String cityID) {
		CityID = cityID;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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


	


	public Double getOrderSum() {
		return orderSum;
	}

	public void setOrderSum(Double orderSum) {
		this.orderSum = orderSum;
	}

	public Double getOrderAvg() {
		return orderAvg;
	}

	public void setOrderAvg(Double orderAvg) {
		this.orderAvg = orderAvg;
	}

	public Double getAllPriceSum() {
		return allPriceSum;
	}

	public void setAllPriceSum(Double allPriceSum) {
		this.allPriceSum = allPriceSum;
	}

	public String getCustLevel() {
		return custLevel;
	}

	public void setCustLevel(String custLevel) {
		this.custLevel = custLevel;
	}

	public String getBm() {
		return bm;
	}

	public void setBm(String bm) {
		this.bm = bm;
	}

	
	
}
