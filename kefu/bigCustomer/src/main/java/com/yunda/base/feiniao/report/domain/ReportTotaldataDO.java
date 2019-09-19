package com.yunda.base.feiniao.report.domain;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.github.crab2died.annotation.ExcelField;


/**
 * 客户报表订单统计-总表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-11 14:54:40
 */
public class ReportTotaldataDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//大区
	@ExcelField(title = "大区", order = 1)
	private String bigarea;
	//省ID
	@ExcelField(title = "省ID", order = 2)
	private String provinceid;
	//省名称
	@ExcelField(title = "省名称", order = 3)
	private String provincename;
	//市ID
	@ExcelField(title = "市ID", order = 4)
	private String cityid;
	//市名称
	@ExcelField(title = "市名称", order = 5)
	private String cityname;
	//网点编码
	@ExcelField(title = "网点编码", order = 6)
	private Integer branchCode;
	//网点名称
	@ExcelField(title = "网点名称", order = 7)
	private String branchName;
	//客户编码
	@ExcelField(title = "客户编码", order = 8)
	private String customerId;
	//客户名称
	@ExcelField(title = "客户名称", order = 9)
	private String customerName;
	//一级网点编码
	@ExcelField(title = "一级网点编码", order = 10)
	private Integer yjbm;
	//一级网点名称
	@ExcelField(title = "一级网点名称", order = 11)
	private String yjmc;
	//客户来源
	@ExcelField(title = "客户来源", order = 12)
	private String customerSourceType;
	//面单量
	@ExcelField(title = "面单量", order = 13)
	private double orderSum = 0;
	//二维码面单量
	@ExcelField(title = "二维码面单量", order = 14)
	private double qrcodeOrderSum = 0;
	//菜鸟面单量
	@ExcelField(title = "菜鸟面单量", order = 15)
	private double cainiaoOrderSum = 0;
	//电子面单量
	@ExcelField(title = "电子面单量", order = 16)
	private double ordinaryOrderSum = 0;
	//日均单量
	@ExcelField(title = "日均单量", order = 17)
	private double orderAvg = 0;
	//面单量返利金额
	@ExcelField(title = "面单量返利金额", order = 18)
	private double priceSum = 0;
	//电子面单返利金额
	@ExcelField(title = "电子面单返利金额", order = 19)
	private double dianziOrderSum = 0;
	//电子面单占比
	@ExcelField(title = "电子面单占比", order = 20)
	private String dianziNumPercent="0.0%";
	//创建日期
	@ExcelField(title = "创建日期", order = 21)
	private Date createDate;
	//订单日期
	@ExcelField(title = "订单日期", order = 22)
	private Date reportDate;
	//电子面单百分比
	@ExcelField(title = "电子面单百分比", order = 23)
	private String dianziPercent;
	//a类客户总数
	@ExcelField(title = "a类客户总数", order = 24)
	private double aCustomerSum=0;
	//b类客户总数
	@ExcelField(title = "b类客户总数", order = 25)
	private double bCustomerSum=0;
	//c类客户总数
	@ExcelField(title = "c类客户总数", order = 26)
	private double cCustomerSum=0;
	//d类客户总数
	@ExcelField(title = "d类客户总数", order = 27)
	private double dCustomerSum=0;
	//e类客户总数
	@ExcelField(title = "e类客户总数", order = 28)
	private double eCustomerSum=0;
	//f类客户总数
	@ExcelField(title = "f类客户总数", order = 29)
	private double fCustomerSum=0;
	//g类客户总数
	@ExcelField(title = "g类客户总数", order = 30)
	private double gCustomerSum=0;
	//a类日均单量
	@ExcelField(title = "a类日均单量", order = 31)
	private double aOrderAvg=0;
	//b类日均单量
	@ExcelField(title = "b类日均单量", order = 32)
	private double bOrderAvg=0;
	//c类日均单量
	@ExcelField(title = "c类日均单量", order = 33)
	private double cOrderAvg=0;
	//d类日均单量
	@ExcelField(title = "d类日均单量", order = 34)
	private double dOrderAvg=0;
	//e类日均单量
	@ExcelField(title = "e类日均单量", order = 35)
	private double eOrderAvg=0;
	//f类日均单量
	@ExcelField(title = "f类日均单量", order = 36)
	private double fOrderAvg=0;
	//g类日均单量
	@ExcelField(title = "g类日均单量", order = 37)
	private double gOrderAvg=0;
	//a类面单量
	@ExcelField(title = "a类面单量", order = 38)
	private double aOrderSum=0;
	//b类面单量
	@ExcelField(title = "b类面单量", order = 39)
	private double bOrderSum=0;
	//c类面单量
	@ExcelField(title = "c类面单量", order = 40)
	private double cOrderSum=0;
	//d类面单量
	@ExcelField(title = "d类面单量", order = 41)
	private double dOrderSum=0;
	//e类面单量
	@ExcelField(title = "e类面单量", order = 42)
	private double eOrderSum=0;
	//f类面单量
	@ExcelField(title = "f类面单量", order = 43)
	private double fOrderSum=0;
	//g类面单量
	@ExcelField(title = "g类面单量", order = 44)
	private double gOrderSum=0;
	//a类返利金额面单数
	@ExcelField(title = "a类返利金额面单数", order = 45)
	private double aPriceSum=0;
	//b类返利金额面单数
	@ExcelField(title = "b类返利金额面单数", order = 46)
	private double bPriceSum=0;
	//c类返利金额面单数
	@ExcelField(title = "c类返利金额面单数", order = 47)
	private double cPriceSum=0;
	//d类返利金额面单数
	@ExcelField(title = "d类返利金额面单数", order = 48)
	private double dPriceSum=0;
	//e类返利金额面单数
	@ExcelField(title = "e类返利金额面单数", order = 49)
	private double ePriceSum=0;
	//f类返利金额面单数
	@ExcelField(title = "f类返利金额面单数", order = 50)
	private double fPriceSum=0;
	//g类返利金额面单数
	@ExcelField(title = "g类返利金额面单数", order = 51)
	private double gPriceSum=0;
	//a类返利金额总数
	@ExcelField(title = "a类返利金额总数", order = 52)
	private double aAllPriceSum=0;
	//b类返利金额总数
	@ExcelField(title = "b类返利金额总数", order = 53)
	private double bAllPriceSum=0;
	//c类返利金额总数
	@ExcelField(title = "c类返利金额总数", order = 54)
	private double cAllPriceSum=0;
	//d类返利金额总数
	@ExcelField(title = "d类返利金额总数", order = 55)
	private double dAllPriceSum=0;
	//e类返利金额总数
	@ExcelField(title = "e类返利金额总数", order = 56)
	private double eAllPriceSum=0;
	//f类返利金额总数
	@ExcelField(title = "f类返利金额总数", order = 57)
	private double fAllPriceSum=0;
	//g类返利金额总数
	@ExcelField(title = "g类返利金额总数", order = 58)
	private double gAllPriceSum=0;
	//a类返利金额百分比
	@ExcelField(title = "a类返利金额百分比", order = 59)
	private String aPricePercent;
	//b类返利金额百分比
	@ExcelField(title = "b类返利金额百分比", order = 60)
	private String bPricePercent;
	//c类返利金额百分比
	@ExcelField(title = "c类返利金额百分比", order = 61)
	private String cPricePercent;
	//d类返利金额百分比
	@ExcelField(title = "d类返利金额百分比", order = 62)
	private String dPricePercent;
	//e类返利金额百分比
	@ExcelField(title = "e类返利金额百分比", order = 63)
	private String ePricePercent;
	//f类返利金额百分比
	@ExcelField(title = "f类返利金额百分比", order = 64)
	private String fPricePercent;
	//g类返利金额百分比
	@ExcelField(title = "g类返利金额百分比", order = 65)
	private String gPricePercent;
	//客户订单量
	@ExcelField(title = "客户订单量", order = 66)
	private double customerSum=0;
	//客户平均单量
	@ExcelField(title = "客户平均单量", order = 67)
	private double customerAvgSum=0;
	//客户返利单量
	@ExcelField(title = "客户返利单量", order = 68)
	private double customerPriceSum=0;
	//客户返利单量
	@ExcelField(title = "客户返利单量", order = 69)
	private double customerAllPriceSum=0;
	
	   /** 
	    * price_level - 客户金额等级 
	    */
	@ExcelField(title = "客户金额等级 ", order = 70)
    private String priceLevel;

	@ExcelField(title = "登录人权限类型 ", order = 71)
	private String tmpField;
	
	/*开始时间*/
	@ExcelField(title = "开始时间 ", order = 72)
	private String startDate;
	/* 结束时间*/
	@ExcelField(title = "结束时间 ", order = 73)
	private String endDate;
	
	@ExcelField(title = "商家id", order = 74)
	private String sellerId;
	@ExcelField(title = "店铺名称", order = 75)
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setTmpField(String tmpField) {
		this.tmpField = tmpField;
	}

	public String getTmpField() {
		return tmpField;
	}

	/**
	 * 设置：客户金额等级
	 */
	public void setPriceLevel(String priceLevel) {
		this.priceLevel = priceLevel;
	}
	/**
	 * 设置：客户金额等级
	 */
	public String getPriceLevel() {
		return priceLevel;
	}
	/**
	 * 设置：大区
	 */
	public void setBigarea(String bigarea) {
		this.bigarea = bigarea;
	}
	/**
	 * 获取：大区
	 */
	public String getBigarea() {
		return bigarea;
	}
	/**
	 * 设置：省ID
	 */
	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}
	/**
	 * 获取：省ID
	 */
	public String getProvinceid() {
		return provinceid;
	}
	/**
	 * 设置：省名称
	 */
	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}
	/**
	 * 获取：省名称
	 */
	public String getProvincename() {
		return provincename;
	}
	/**
	 * 设置：市ID
	 */
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	/**
	 * 获取：市ID
	 */
	public String getCityid() {
		return cityid;
	}
	/**
	 * 设置：市名称
	 */
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	/**
	 * 获取：市名称
	 */
	public String getCityname() {
		return cityname;
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
	 * 设置：一级网点编码
	 */
	public void setYjbm(Integer yjbm) {
		this.yjbm = yjbm;
	}
	/**
	 * 获取：一级网点编码
	 */
	public Integer getYjbm() {
		return yjbm;
	}
	/**
	 * 设置：一级网点名称
	 */
	public void setYjmc(String yjmc) {
		this.yjmc = yjmc;
	}
	/**
	 * 获取：一级网点名称
	 */
	public String getYjmc() {
		return yjmc;
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
	 * 设置：面单量
	 */
	public void setOrderSum(double orderSum) {
		this.orderSum = orderSum;
	}
	/**
	 * 获取：面单量
	 */
	public double getOrderSum() {
		return orderSum;
	}
	/**
	 * 设置：二维码面单量
	 */
	public void setQrcodeOrderSum(double qrcodeOrderSum) {
		this.qrcodeOrderSum = qrcodeOrderSum;
	}
	/**
	 * 获取：二维码面单量
	 */
	public double getQrcodeOrderSum() {
		return qrcodeOrderSum;
	}
	/**
	 * 设置：菜鸟面单量
	 */
	public void setCainiaoOrderSum(double cainiaoOrderSum) {
		this.cainiaoOrderSum = cainiaoOrderSum;
	}
	/**
	 * 获取：菜鸟面单量
	 */
	public double getCainiaoOrderSum() {
		return cainiaoOrderSum;
	}
	/**
	 * 设置：电子面单量
	 */
	public void setOrdinaryOrderSum(double ordinaryOrderSum) {
		this.ordinaryOrderSum = ordinaryOrderSum;
	}
	/**
	 * 获取：电子面单量
	 */
	public double getOrdinaryOrderSum() {
		return ordinaryOrderSum;
	}
	/**
	 * 设置：日均单量
	 */
	public void setOrderAvg(double orderAvg) {
		this.orderAvg = orderAvg;
	}
	/**
	 * 获取：日均单量
	 */
	public double getOrderAvg() {
		return orderAvg;
	}
	/**
	 * 设置：面单量返利金额
	 */
	public void setPriceSum(double priceSum) {
		this.priceSum = priceSum;
	}
	/**
	 * 获取：面单量返利金额
	 */
	public double getPriceSum() {
		return priceSum;
	}
	/**
	 * 设置：电子面单返利金额
	 */
	public void setDianziOrderSum(double dianziOrderSum) {
		this.dianziOrderSum = dianziOrderSum;
	}
	/**
	 * 获取：电子面单返利金额
	 */
	public double getDianziOrderSum() {
		return dianziOrderSum;
	}
	/**
	 * 设置：电子面单占比
	 */
	public void setDianziNumPercent(String dianziNumPercent) {
		this.dianziNumPercent = dianziNumPercent;
	}
	/**
	 * 获取：电子面单占比
	 */
	public String getDianziNumPercent() {
		return dianziNumPercent;
	}
	/**
	 * 设置：创建日期
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 获取：创建日期
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * 设置：订单日期
	 */
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	/**
	 * 获取：订单日期
	 */
	public Date getReportDate() {
		return reportDate;
	}
	/**
	 * 设置：电子面单百分比
	 */
	public void setDianziPercent(String dianziPercent) {
		this.dianziPercent = dianziPercent;
	}
	/**
	 * 获取：电子面单百分比
	 */
	public String getDianziPercent() {
		return dianziPercent;
	}
	/**
	 * 设置：a类客户总数
	 */
	public void setACustomerSum(double aCustomerSum) {
		this.aCustomerSum = aCustomerSum;
	}
	/**
	 * 获取：a类客户总数
	 */
	public double getACustomerSum() {
		return aCustomerSum;
	}
	/**
	 * 设置：b类客户总数
	 */
	public void setBCustomerSum(double bCustomerSum) {
		this.bCustomerSum = bCustomerSum;
	}
	/**
	 * 获取：b类客户总数
	 */
	public double getBCustomerSum() {
		return bCustomerSum;
	}
	/**
	 * 设置：c类客户总数
	 */
	public void setCCustomerSum(double cCustomerSum) {
		this.cCustomerSum = cCustomerSum;
	}
	/**
	 * 获取：c类客户总数
	 */
	public double getCCustomerSum() {
		return cCustomerSum;
	}
	/**
	 * 设置：d类客户总数
	 */
	public void setDCustomerSum(double dCustomerSum) {
		this.dCustomerSum = dCustomerSum;
	}
	/**
	 * 获取：d类客户总数
	 */
	public double getDCustomerSum() {
		return dCustomerSum;
	}
	/**
	 * 设置：e类客户总数
	 */
	public void setECustomerSum(double eCustomerSum) {
		this.eCustomerSum = eCustomerSum;
	}
	/**
	 * 获取：e类客户总数
	 */
	public double getECustomerSum() {
		return eCustomerSum;
	}
	/**
	 * 设置：f类客户总数
	 */
	public void setFCustomerSum(double fCustomerSum) {
		this.fCustomerSum = fCustomerSum;
	}
	/**
	 * 获取：f类客户总数
	 */
	public double getFCustomerSum() {
		return fCustomerSum;
	}
	/**
	 * 设置：g类客户总数
	 */
	public void setGCustomerSum(double gCustomerSum) {
		this.gCustomerSum = gCustomerSum;
	}
	/**
	 * 获取：g类客户总数
	 */
	public double getGCustomerSum() {
		return gCustomerSum;
	}
	/**
	 * 设置：a类日均单量
	 */
	public void setAOrderAvg(double aOrderAvg) {
		this.aOrderAvg = aOrderAvg;
	}
	/**
	 * 获取：a类日均单量
	 */
	public double getAOrderAvg() {
		return aOrderAvg;
	}
	/**
	 * 设置：b类日均单量
	 */
	public void setBOrderAvg(double bOrderAvg) {
		this.bOrderAvg = bOrderAvg;
	}
	/**
	 * 获取：b类日均单量
	 */
	public double getBOrderAvg() {
		return bOrderAvg;
	}
	/**
	 * 设置：c类日均单量
	 */
	public void setCOrderAvg(double cOrderAvg) {
		this.cOrderAvg = cOrderAvg;
	}
	/**
	 * 获取：c类日均单量
	 */
	public double getCOrderAvg() {
		return cOrderAvg;
	}
	/**
	 * 设置：d类日均单量
	 */
	public void setDOrderAvg(double dOrderAvg) {
		this.dOrderAvg = dOrderAvg;
	}
	/**
	 * 获取：d类日均单量
	 */
	public double getDOrderAvg() {
		return dOrderAvg;
	}
	/**
	 * 设置：e类日均单量
	 */
	public void setEOrderAvg(double eOrderAvg) {
		this.eOrderAvg = eOrderAvg;
	}
	/**
	 * 获取：e类日均单量
	 */
	public double getEOrderAvg() {
		return eOrderAvg;
	}
	/**
	 * 设置：f类日均单量
	 */
	public void setFOrderAvg(double fOrderAvg) {
		this.fOrderAvg = fOrderAvg;
	}
	/**
	 * 获取：f类日均单量
	 */
	public double getFOrderAvg() {
		return fOrderAvg;
	}
	/**
	 * 设置：g类日均单量
	 */
	public void setGOrderAvg(double gOrderAvg) {
		this.gOrderAvg = gOrderAvg;
	}
	/**
	 * 获取：g类日均单量
	 */
	public double getGOrderAvg() {
		return gOrderAvg;
	}
	/**
	 * 设置：a类面单量
	 */
	public void setAOrderSum(double aOrderSum) {
		this.aOrderSum = aOrderSum;
	}
	/**
	 * 获取：a类面单量
	 */
	public double getAOrderSum() {
		return aOrderSum;
	}
	/**
	 * 设置：b类面单量
	 */
	public void setBOrderSum(double bOrderSum) {
		this.bOrderSum = bOrderSum;
	}
	/**
	 * 获取：b类面单量
	 */
	public double getBOrderSum() {
		return bOrderSum;
	}
	/**
	 * 设置：c类面单量
	 */
	public void setCOrderSum(double cOrderSum) {
		this.cOrderSum = cOrderSum;
	}
	/**
	 * 获取：c类面单量
	 */
	public double getCOrderSum() {
		return cOrderSum;
	}
	/**
	 * 设置：d类面单量
	 */
	public void setDOrderSum(double dOrderSum) {
		this.dOrderSum = dOrderSum;
	}
	/**
	 * 获取：d类面单量
	 */
	public double getDOrderSum() {
		return dOrderSum;
	}
	/**
	 * 设置：e类面单量
	 */
	public void setEOrderSum(double eOrderSum) {
		this.eOrderSum = eOrderSum;
	}
	/**
	 * 获取：e类面单量
	 */
	public double getEOrderSum() {
		return eOrderSum;
	}
	/**
	 * 设置：f类面单量
	 */
	public void setFOrderSum(double fOrderSum) {
		this.fOrderSum = fOrderSum;
	}
	/**
	 * 获取：f类面单量
	 */
	public double getFOrderSum() {
		return fOrderSum;
	}
	/**
	 * 设置：g类面单量
	 */
	public void setGOrderSum(double gOrderSum) {
		this.gOrderSum = gOrderSum;
	}
	/**
	 * 获取：g类面单量
	 */
	public double getGOrderSum() {
		return gOrderSum;
	}
	/**
	 * 设置：a类返利金额面单数
	 */
	public void setAPriceSum(double aPriceSum) {
		this.aPriceSum = aPriceSum;
	}
	/**
	 * 获取：a类返利金额面单数
	 */
	public double getAPriceSum() {
		return aPriceSum;
	}
	/**
	 * 设置：b类返利金额面单数
	 */
	public void setBPriceSum(double bPriceSum) {
		this.bPriceSum = bPriceSum;
	}
	/**
	 * 获取：b类返利金额面单数
	 */
	public double getBPriceSum() {
		return bPriceSum;
	}
	/**
	 * 设置：c类返利金额面单数
	 */
	public void setCPriceSum(double cPriceSum) {
		this.cPriceSum = cPriceSum;
	}
	/**
	 * 获取：c类返利金额面单数
	 */
	public double getCPriceSum() {
		return cPriceSum;
	}
	/**
	 * 设置：d类返利金额面单数
	 */
	public void setDPriceSum(double dPriceSum) {
		this.dPriceSum = dPriceSum;
	}
	/**
	 * 获取：d类返利金额面单数
	 */
	public double getDPriceSum() {
		return dPriceSum;
	}
	/**
	 * 设置：e类返利金额面单数
	 */
	public void setEPriceSum(double ePriceSum) {
		this.ePriceSum = ePriceSum;
	}
	/**
	 * 获取：e类返利金额面单数
	 */
	public double getEPriceSum() {
		return ePriceSum;
	}
	/**
	 * 设置：f类返利金额面单数
	 */
	public void setFPriceSum(double fPriceSum) {
		this.fPriceSum = fPriceSum;
	}
	/**
	 * 获取：f类返利金额面单数
	 */
	public double getFPriceSum() {
		return fPriceSum;
	}
	/**
	 * 设置：g类返利金额面单数
	 */
	public void setGPriceSum(double gPriceSum) {
		this.gPriceSum = gPriceSum;
	}
	/**
	 * 获取：g类返利金额面单数
	 */
	public double getGPriceSum() {
		return gPriceSum;
	}
	/**
	 * 设置：a类返利金额总数
	 */
	public void setAAllPriceSum(double aAllPriceSum) {
		this.aAllPriceSum = aAllPriceSum;
	}
	/**
	 * 获取：a类返利金额总数
	 */
	public double getAAllPriceSum() {
		return aAllPriceSum;
	}
	/**
	 * 设置：b类返利金额总数
	 */
	public void setBAllPriceSum(double bAllPriceSum) {
		this.bAllPriceSum = bAllPriceSum;
	}
	/**
	 * 获取：b类返利金额总数
	 */
	public double getBAllPriceSum() {
		return bAllPriceSum;
	}
	/**
	 * 设置：c类返利金额总数
	 */
	public void setCAllPriceSum(double cAllPriceSum) {
		this.cAllPriceSum = cAllPriceSum;
	}
	/**
	 * 获取：c类返利金额总数
	 */
	public double getCAllPriceSum() {
		return cAllPriceSum;
	}
	/**
	 * 设置：d类返利金额总数
	 */
	public void setDAllPriceSum(double dAllPriceSum) {
		this.dAllPriceSum = dAllPriceSum;
	}
	/**
	 * 获取：d类返利金额总数
	 */
	public double getDAllPriceSum() {
		return dAllPriceSum;
	}
	/**
	 * 设置：e类返利金额总数
	 */
	public void setEAllPriceSum(double eAllPriceSum) {
		this.eAllPriceSum = eAllPriceSum;
	}
	/**
	 * 获取：e类返利金额总数
	 */
	public double getEAllPriceSum() {
		return eAllPriceSum;
	}
	/**
	 * 设置：f类返利金额总数
	 */
	public void setFAllPriceSum(double fAllPriceSum) {
		this.fAllPriceSum = fAllPriceSum;
	}
	/**
	 * 获取：f类返利金额总数
	 */
	public double getFAllPriceSum() {
		return fAllPriceSum;
	}
	/**
	 * 设置：g类返利金额总数
	 */
	public void setGAllPriceSum(double gAllPriceSum) {
		this.gAllPriceSum = gAllPriceSum;
	}
	/**
	 * 获取：g类返利金额总数
	 */
	public double getGAllPriceSum() {
		return gAllPriceSum;
	}
	/**
	 * 设置：a类返利金额百分比
	 */
	public void setAPricePercent(String aPricePercent) {
		this.aPricePercent = aPricePercent;
	}
	/**
	 * 获取：a类返利金额百分比
	 */
	public String getAPricePercent() {
		return aPricePercent;
	}
	/**
	 * 设置：b类返利金额百分比
	 */
	public void setBPricePercent(String bPricePercent) {
		this.bPricePercent = bPricePercent;
	}
	/**
	 * 获取：b类返利金额百分比
	 */
	public String getBPricePercent() {
		return bPricePercent;
	}
	/**
	 * 设置：c类返利金额百分比
	 */
	public void setCPricePercent(String cPricePercent) {
		this.cPricePercent = cPricePercent;
	}
	/**
	 * 获取：c类返利金额百分比
	 */
	public String getCPricePercent() {
		return cPricePercent;
	}
	/**
	 * 设置：d类返利金额百分比
	 */
	public void setDPricePercent(String dPricePercent) {
		this.dPricePercent = dPricePercent;
	}
	/**
	 * 获取：d类返利金额百分比
	 */
	public String getDPricePercent() {
		return dPricePercent;
	}
	/**
	 * 设置：e类返利金额百分比
	 */
	public void setEPricePercent(String ePricePercent) {
		this.ePricePercent = ePricePercent;
	}
	/**
	 * 获取：e类返利金额百分比
	 */
	public String getEPricePercent() {
		return ePricePercent;
	}
	/**
	 * 设置：f类返利金额百分比
	 */
	public void setFPricePercent(String fPricePercent) {
		this.fPricePercent = fPricePercent;
	}
	/**
	 * 获取：f类返利金额百分比
	 */
	public String getFPricePercent() {
		return fPricePercent;
	}
	/**
	 * 设置：g类返利金额百分比
	 */
	public void setGPricePercent(String gPricePercent) {
		this.gPricePercent = gPricePercent;
	}
	/**
	 * 获取：g类返利金额百分比
	 */
	public String getGPricePercent() {
		return gPricePercent;
	}
	/**
	 * 设置：客户订单量
	 */
	public void setCustomerSum(double customerSum) {
		this.customerSum = customerSum;
	}
	/**
	 * 获取：客户订单量
	 */
	public double getCustomerSum() {
		return customerSum;
	}
	/**
	 * 设置：客户平均单量
	 */
	public void setCustomerAvgSum(double customerAvgSum) {
		this.customerAvgSum = customerAvgSum;
	}
	/**
	 * 获取：客户平均单量
	 */
	public double getCustomerAvgSum() {
		return customerAvgSum;
	}
	/**
	 * 设置：客户返利单量
	 */
	public void setCustomerPriceSum(double customerPriceSum) {
		this.customerPriceSum = customerPriceSum;
	}
	/**
	 * 获取：客户返利单量
	 */
	public double getCustomerPriceSum() {
		return customerPriceSum;
	}
	/**
	 * 设置：客户返利单量
	 */
	public void setCustomerAllPriceSum(double customerAllPriceSum) {
		this.customerAllPriceSum = customerAllPriceSum;
	}
	/**
	 * 获取：客户返利单量
	 */
	public double getCustomerAllPriceSum() {
		return customerAllPriceSum;
	}
	
	
	public ReportTotaldataDO() {
		super();
	}
	public ReportTotaldataDO(String bigarea, String provinceid,
			String provincename, String cityid, String cityname,
			Integer branchCode, String branchName, String customerId,
			String customerName, Integer yjbm, String yjmc,
			String customerSourceType, double orderSum, double qrcodeOrderSum,
			double cainiaoOrderSum, double ordinaryOrderSum, double orderAvg,
			double priceSum, double dianziOrderSum, String dianziNumPercent,
			Date createDate, Date reportDate, String dianziPercent,
			double aCustomerSum, double bCustomerSum,
			double cCustomerSum, double dCustomerSum,
			double eCustomerSum, double fCustomerSum,
			double gCustomerSum, double aOrderAvg, double bOrderAvg,
			double cOrderAvg, double dOrderAvg, double eOrderAvg,
			double fOrderAvg, double gOrderAvg, double aOrderSum,
			double bOrderSum, double cOrderSum, double dOrderSum,
			double eOrderSum, double fOrderSum, double gOrderSum,
			double aPriceSum, double bPriceSum, double cPriceSum,
			double dPriceSum, double ePriceSum, double fPriceSum,
			double gPriceSum, double aAllPriceSum, double bAllPriceSum,
			double cAllPriceSum, double dAllPriceSum, double eAllPriceSum,
			double fAllPriceSum, double gAllPriceSum, String aPricePercent,
			String bPricePercent, String cPricePercent, String dPricePercent,
			String ePricePercent, String fPricePercent, String gPricePercent,
			double customerSum, double customerAvgSum, double customerPriceSum,
			double customerAllPriceSum,String priceLevel) {
		super();
		this.priceLevel = priceLevel;
		this.bigarea = bigarea;
		this.provinceid = provinceid;
		this.provincename = provincename;
		this.cityid = cityid;
		this.cityname = cityname;
		this.branchCode = branchCode;
		this.branchName = branchName;
		this.customerId = customerId;
		this.customerName = customerName;
		this.yjbm = yjbm;
		this.yjmc = yjmc;
		this.customerSourceType = customerSourceType;
		this.orderSum = orderSum;
		this.qrcodeOrderSum = qrcodeOrderSum;
		this.cainiaoOrderSum = cainiaoOrderSum;
		this.ordinaryOrderSum = ordinaryOrderSum;
		this.orderAvg = orderAvg;
		this.priceSum = priceSum;
		this.dianziOrderSum = dianziOrderSum;
		this.dianziNumPercent = dianziNumPercent;
		this.createDate = createDate;
		this.reportDate = reportDate;
		this.dianziPercent = dianziPercent;
		this.aCustomerSum = aCustomerSum;
		this.bCustomerSum = bCustomerSum;
		this.cCustomerSum = cCustomerSum;
		this.dCustomerSum = dCustomerSum;
		this.eCustomerSum = eCustomerSum;
		this.fCustomerSum = fCustomerSum;
		this.gCustomerSum = gCustomerSum;
		this.aOrderAvg = aOrderAvg;
		this.bOrderAvg = bOrderAvg;
		this.cOrderAvg = cOrderAvg;
		this.dOrderAvg = dOrderAvg;
		this.eOrderAvg = eOrderAvg;
		this.fOrderAvg = fOrderAvg;
		this.gOrderAvg = gOrderAvg;
		this.aOrderSum = aOrderSum;
		this.bOrderSum = bOrderSum;
		this.cOrderSum = cOrderSum;
		this.dOrderSum = dOrderSum;
		this.eOrderSum = eOrderSum;
		this.fOrderSum = fOrderSum;
		this.gOrderSum = gOrderSum;
		this.aPriceSum = aPriceSum;
		this.bPriceSum = bPriceSum;
		this.cPriceSum = cPriceSum;
		this.dPriceSum = dPriceSum;
		this.ePriceSum = ePriceSum;
		this.fPriceSum = fPriceSum;
		this.gPriceSum = gPriceSum;
		this.aAllPriceSum = aAllPriceSum;
		this.bAllPriceSum = bAllPriceSum;
		this.cAllPriceSum = cAllPriceSum;
		this.dAllPriceSum = dAllPriceSum;
		this.eAllPriceSum = eAllPriceSum;
		this.fAllPriceSum = fAllPriceSum;
		this.gAllPriceSum = gAllPriceSum;
		this.aPricePercent = aPricePercent;
		this.bPricePercent = bPricePercent;
		this.cPricePercent = cPricePercent;
		this.dPricePercent = dPricePercent;
		this.ePricePercent = ePricePercent;
		this.fPricePercent = fPricePercent;
		this.gPricePercent = gPricePercent;
		this.customerSum = customerSum;
		this.customerAvgSum = customerAvgSum;
		this.customerPriceSum = customerPriceSum;
		this.customerAllPriceSum = customerAllPriceSum;
	}
	
	
	public ReportTotaldataDO toReckonList() {
		ReportTotaldataDO totalData =new ReportTotaldataDO();
		DecimalFormat    df   = new DecimalFormat("######0"); 
		DecimalFormat dfDouble = new DecimalFormat("######.00");

		 NumberFormat nt = NumberFormat.getPercentInstance();
	   //设置百分数精确度2即保留两位小数
	   nt.setMinimumFractionDigits(1);
	   double customerSum = aCustomerSum+bCustomerSum+cCustomerSum+dCustomerSum+eCustomerSum+fCustomerSum+gCustomerSum;
	   double customerAvgSum = aOrderAvg+bOrderAvg+cOrderAvg+dOrderAvg+eOrderAvg+fOrderAvg+gOrderAvg;
	   double customerPriceSum = aPriceSum+bPriceSum+cPriceSum+dPriceSum+ePriceSum+fPriceSum+gPriceSum;
	   double customerAllPriceSum = aAllPriceSum+bAllPriceSum+cAllPriceSum+dAllPriceSum+eAllPriceSum+fAllPriceSum+gAllPriceSum;
		 totalData.setOrderSum(orderSum);
		 totalData.setOrderAvg(Double.parseDouble(df.format(orderAvg)));
		 totalData.setDianziOrderSum(dianziOrderSum);
		 totalData.setOrdinaryOrderSum(ordinaryOrderSum);
		 totalData.setDianziPercent(orderSum>0?nt.format(dianziOrderSum/orderSum):"0.0%");
		 totalData.setCustomerSum(customerSum); 
		 totalData.setCustomerAvgSum(Double.parseDouble(df.format(customerAvgSum)));
		 totalData.setCustomerPriceSum(Double.parseDouble(dfDouble.format(customerPriceSum)));
		 totalData.setCustomerAllPriceSum(Double.parseDouble(dfDouble.format(customerAllPriceSum)));
		 totalData.setCustomerName(customerName);
		 totalData.setBigarea(bigarea);
		 totalData.setProvinceid(provinceid);
		 totalData.setCityid(cityid);
		 totalData.setBranchCode(branchCode);
		 totalData.setBranchName(branchName);
		 totalData.setTmpField(tmpField);
		 
		 totalData.setAAllPriceSum(Double.parseDouble(df.format(aAllPriceSum)));
		 totalData.setACustomerSum(aCustomerSum);
		 totalData.setAOrderAvg(Double.parseDouble(df.format(aOrderAvg)));
		 totalData.setAOrderSum(aOrderSum);
		 totalData.setAPriceSum(Double.parseDouble(df.format(aPriceSum)));
		 totalData.setAPricePercent(orderSum>0?nt.format(aOrderSum/orderSum):"0.0%");

		 totalData.setBAllPriceSum(Double.parseDouble(df.format(bAllPriceSum)));
		 totalData.setBCustomerSum(bCustomerSum);
		 totalData.setBOrderAvg(Double.parseDouble(df.format(bOrderAvg)));
		 totalData.setBOrderSum(bOrderSum);
		 totalData.setBPriceSum(Double.parseDouble(df.format(bPriceSum)));
		 totalData.setBPricePercent(orderSum>0?nt.format(bOrderSum/orderSum):"0.0%");
		 
		 totalData.setCAllPriceSum(Double.parseDouble(df.format(cAllPriceSum)));
		 totalData.setCCustomerSum(cCustomerSum);
		 totalData.setCOrderAvg(Double.parseDouble(df.format(cOrderAvg)));
		 totalData.setCOrderSum(cOrderSum);
		 totalData.setCPriceSum(Double.parseDouble(df.format(cPriceSum)));
		 totalData.setCPricePercent(orderSum>0?nt.format(cOrderSum/orderSum):"0.0%");
		 
		 totalData.setDAllPriceSum(Double.parseDouble(df.format(dAllPriceSum)));
		 totalData.setDCustomerSum(dCustomerSum);
		 totalData.setDOrderAvg(Double.parseDouble(df.format(dOrderAvg)));
		 totalData.setDOrderSum(dOrderSum);
		 totalData.setDPriceSum(Double.parseDouble(df.format(dPriceSum)));
		 totalData.setDPricePercent(orderSum>0?nt.format(dOrderSum/orderSum):"0.0%");
		 
		 totalData.setEAllPriceSum(Double.parseDouble(df.format(eAllPriceSum)));
		 totalData.setECustomerSum(eCustomerSum);
		 totalData.setEOrderAvg(Double.parseDouble(df.format(eOrderAvg)));
		 totalData.setEOrderSum(eOrderSum);
		 totalData.setEPriceSum(Double.parseDouble(df.format(ePriceSum)));
		 totalData.setEPricePercent(orderSum>0?nt.format(eOrderSum/orderSum):"0.0%");
		 
		 totalData.setFAllPriceSum(Double.parseDouble(df.format(fAllPriceSum)));
		 totalData.setFCustomerSum(fCustomerSum);
		 totalData.setFOrderAvg(Double.parseDouble(df.format(fOrderAvg)));
		 totalData.setFOrderSum(fOrderSum);
		 totalData.setFPriceSum(Double.parseDouble(df.format(fPriceSum)));
		 totalData.setFPricePercent(orderSum>0?nt.format(fOrderSum/orderSum):"0.0%");
		 
		 totalData.setGAllPriceSum(Double.parseDouble(df.format(gAllPriceSum)));
		 totalData.setGCustomerSum(gCustomerSum);
		 totalData.setGOrderAvg(Double.parseDouble(df.format(gOrderAvg)));
		 totalData.setGOrderSum(gOrderSum);
		 totalData.setGPriceSum(Double.parseDouble(df.format(gPriceSum)));
		 totalData.setGPricePercent(orderSum>0?nt.format(gOrderSum/orderSum):"0.0%");
		 return totalData;
	}

	public String toReckonHtmlString() {
		DecimalFormat    df   = new DecimalFormat("######0"); 
		 NumberFormat nt = NumberFormat.getPercentInstance();
	   //设置百分数精确度2即保留两位小数
	   nt.setMinimumFractionDigits(1);
	   double customerSum = aCustomerSum+bCustomerSum+cCustomerSum+dCustomerSum+eCustomerSum+fCustomerSum+gCustomerSum;
	   double customerAvgSum = aOrderAvg+bOrderAvg+cOrderAvg+dOrderAvg+eOrderAvg+fOrderAvg+gOrderAvg;
	   double customerPriceSum = aPriceSum+bPriceSum+cPriceSum+dPriceSum+ePriceSum+fPriceSum+gPriceSum;
	   double customerAllPriceSum = aAllPriceSum+bAllPriceSum+cAllPriceSum+dAllPriceSum+eAllPriceSum+fAllPriceSum+gAllPriceSum;
		return String
				.format("<!-- lie1 --><td >%s</td><!-- lie2 --><!-- lie3 --><td >%s</td><!-- lie4 --><!-- lie5 --><td >%s</td><td >%s</td><td >%s</td><!-- lie6 --><!-- lie7 --><td >%s</td><td >%s</td><td >%s</td><td >%s</td><!-- lie8 --><!-- lie9 --><td >%s</td><td >%s</td><td >%s</td><td >%s</td><td >%s</td><td >%s</td><!-- lie10 --><!-- lie11 --><td >%s</td><td >%s</td><td >%s</td><td >%s</td><td >%s</td><td >%s</td><!-- lie12 --><!-- lie13 --><td >%s</td><td >%s</td><td >%s</td><td >%s</td><td >%s</td><td >%s</td><!-- lie14 --><!-- lie15 --><td >%s</td><td >%s</td><td >%s</td><td >%s</td><td >%s</td><td >%s</td><!-- lie16 --><!-- lie17 --><td >%s</td><td >%s</td><td >%s</td><td >%s</td><td >%s</td><td >%s</td><!-- lie18 --><!-- lie19 --><td >%s</td><td >%s</td><td >%s</td><td >%s</td><td >%s</td><td >%s</td><!-- lie20 --><!-- lie21 --><td >%s</td><td >%s</td><td >%s</td><td >%s</td><td >%s</td><td >%s</td><!-- lie22 -->",
						(long)orderSum, 
						(long)orderAvg, (long)dianziOrderSum, (long)ordinaryOrderSum, orderSum>0?nt.format(dianziOrderSum/orderSum):"0.0%",
						(long)customerSum, (long)customerAvgSum,df.format(customerPriceSum),df.format(customerAllPriceSum),
						(long)aCustomerSum, (long)aOrderAvg, (long)aOrderSum,orderSum>0?nt.format(aOrderSum/orderSum):"0.0%",df.format(aPriceSum),df.format(aAllPriceSum),
						(long)bCustomerSum, (long)bOrderAvg, (long)bOrderSum,orderSum>0?nt.format(bOrderSum/orderSum):"0.0%",df.format(bPriceSum),df.format(bAllPriceSum),
						(long)cCustomerSum, (long)cOrderAvg, (long)cOrderSum,orderSum>0?nt.format(cOrderSum/orderSum):"0.0%",df.format(cPriceSum),df.format(cAllPriceSum),
						(long)dCustomerSum, (long)dOrderAvg, (long)dOrderSum,orderSum>0?nt.format(dOrderSum/orderSum):"0.0%",df.format(dPriceSum),df.format(dAllPriceSum),
						(long)eCustomerSum, (long)eOrderAvg, (long)eOrderSum,orderSum>0?nt.format(eOrderSum/orderSum):"0.0%",df.format(ePriceSum),df.format(eAllPriceSum),
						(long)fCustomerSum, (long)fOrderAvg, (long)fOrderSum,orderSum>0?nt.format(fOrderSum/orderSum):"0.0%",df.format(fPriceSum),df.format(fAllPriceSum),
						(long)gCustomerSum, (long)gOrderAvg, (long)gOrderSum,orderSum>0?nt.format(gOrderSum/orderSum):"0.0%",df.format(gPriceSum),df.format(gAllPriceSum));
	}
}
