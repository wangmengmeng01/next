package com.yunda.base.feiniao.report.domain;

import com.github.crab2died.annotation.ExcelField;

import java.io.Serializable;


/**
 * 客户报表订单统计-总表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-11 14:54:40
 */
public class ExportReportTotaldataDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
//	newTotal.put("customerName", data.getCustomerName());
//	  newTotal.put("orderSum", data.getOrderSum());
//	  newTotal.put("orderAvg", data.getOrderAvg());
//	  newTotal.put("dianziOrderSum", data.getDianziOrderSum());
//	  newTotal.put("ordinaryOrderSum", data.getOrdinaryOrderSum());
//	  newTotal.put("dianziPercent", data.getDianziPercent());
	
//	  newTotal.put("customerSum", data.getCustomerSum());
//	  newTotal.put("customerAvgSum", data.getCustomerAvgSum());
//	  newTotal.put("customerPriceSum", data.getCustomerPriceSum());
//	  newTotal.put("customerAllPriceSum", data.getCustomerAllPriceSum());
//	  newTotal.put("customerAllPriceSum", data.getCustomerAllPriceSum());
//	  
//	  newTotal.put("acustomerSum", data.getACustomerSum());
//	  newTotal.put("aorderAvg", data.getAOrderAvg());
//	  newTotal.put("aorderSum", data.getAOrderSum());
//	  newTotal.put("apricePercent", data.getAPricePercent());
//	  newTotal.put("apriceSum", data.getAPriceSum());
//	  newTotal.put("aallPriceSum", data.getAAllPriceSum());
//
//	  newTotal.put("bcustomerSum", data.getBCustomerSum());
//	  newTotal.put("borderAvg", data.getBOrderAvg());
//	  newTotal.put("borderSum", data.getBOrderSum());
//	  newTotal.put("bpricePercent", data.getBPricePercent());
//	  newTotal.put("bpriceSum", data.getBPriceSum());
//	  newTotal.put("ballPriceSum", data.getBAllPriceSum());	  
//	
//	  newTotal.put("ccustomerSum", data.getCCustomerSum());
//	  newTotal.put("corderAvg", data.getCOrderAvg());
//	  newTotal.put("corderSum", data.getCOrderSum());
//	  newTotal.put("cpricePercent", data.getCPricePercent());
//	  newTotal.put("cpriceSum", data.getCPriceSum());
//	  newTotal.put("callPriceSum", data.getCAllPriceSum());
//	  
//	  newTotal.put("dcustomerSum", data.getDCustomerSum());
//	  newTotal.put("dorderAvg", data.getDOrderAvg());
//	  newTotal.put("dorderSum", data.getDOrderSum());
//	  newTotal.put("dpricePercent", data.getDPricePercent());
//	  newTotal.put("dpriceSum", data.getDPriceSum());
//	  newTotal.put("dallPriceSum", data.getDAllPriceSum());
//	  
//	  newTotal.put("ecustomerSum", data.getECustomerSum());
//	  newTotal.put("eorderAvg", data.getEOrderAvg());
//	  newTotal.put("eorderSum", data.getEOrderSum());
//	  newTotal.put("epricePercent", data.getEPricePercent());
//	  newTotal.put("epriceSum", data.getEPriceSum());
//	  newTotal.put("eallPriceSum", data.getEAllPriceSum());
//	  
//	  newTotal.put("fcustomerSum", data.getFCustomerSum());
//	  newTotal.put("forderAvg", data.getFOrderAvg());
//	  newTotal.put("forderSum", data.getFOrderSum());
//	  newTotal.put("fpricePercent", data.getFPricePercent());
//	  newTotal.put("fpriceSum", data.getFPriceSum());
//	  newTotal.put("fallPriceSum", data.getFAllPriceSum());
//	  
//	  newTotal.put("gcustomerSum", data.getGCustomerSum());
//	  newTotal.put("gorderAvg", data.getGOrderAvg());
//	  newTotal.put("gorderSum", data.getGOrderSum());
//	  newTotal.put("gpricePercent", data.getGPricePercent());
//	  newTotal.put("gpriceSum", data.getGPriceSum());
//	  newTotal.put("gallPriceSum", data.getGAllPriceSum());
	
	//客户名称
	@ExcelField(title = "客户名称", order = 1)
	private String customerName;
	//面单量
	@ExcelField(title = "面单量", order = 2)
	private double orderSum = 0;
	//日均单量
	@ExcelField(title = "日均单量", order = 3)
	private double orderAvg = 0;	
	//电子面单返利金额
	@ExcelField(title = "电子面单返利金额", order = 4)
	private double dianziOrderSum = 0;
	//电子面单量
	@ExcelField(title = "电子面单量", order = 5)
	private double ordinaryOrderSum = 0;
	//电子面单百分比
	@ExcelField(title = "电子面单百分比", order = 6)
	private String dianziPercent;
	//客户订单量
	@ExcelField(title = "客户数", order = 7)
	private double customerSum=0;
	//客户平均单量
	@ExcelField(title = "日均单量(电子面单总量)", order = 8)
	private double dianziOrderSumAvg=0;
	//客户返利单量
	@ExcelField(title = "日均奖励金额(/元,电子面单总量)", order = 9)
	private double dianziPriceSumAvg=0;
	//客户返利单量
	@ExcelField(title = "总奖励金额(/元,电子面单总量)", order = 10)
	private double dianziPriceSum=0;
	
	//a类客户总数
	@ExcelField(title = "a类客户总数", order = 11)
	private double aCustomerSum=0;
	//a类日均单量
	@ExcelField(title = "a类日均单量", order = 12)
	private double aOrderAvg=0;
	//a类面单量
	@ExcelField(title = "a类面单量", order = 13)
	private double aOrderSum=0;
	//a类返利金额百分比
	@ExcelField(title = "a类返利金额百分比", order = 14)
	private String aPricePercent;
	//a类返利金额面单数
	@ExcelField(title = "a类返利金额面单数", order = 15)
	private double aPriceSum=0;
	//a类返利金额总数
	@ExcelField(title = "a类返利金额总数", order = 16)
	private double aAllPriceSum=0;
	
	
	//b类客户总数
	@ExcelField(title = "b类客户总数", order = 17)
	private double bCustomerSum=0;
	//b类日均单量
	@ExcelField(title = "b类日均单量", order = 18)
	private double bOrderAvg=0;
	//b类面单量
	@ExcelField(title = "b类面单量", order = 19)
	private double bOrderSum=0;
	//b类返利金额百分比
	@ExcelField(title = "b类返利金额百分比", order = 20)
	private String bPricePercent;
	//b类返利金额面单数
	@ExcelField(title = "b类返利金额面单数", order = 21)
	private double bPriceSum=0;
	//b类返利金额总数
	@ExcelField(title = "b类返利金额总数", order = 22)
	private double bAllPriceSum=0;
	
	
	//c类客户总数
	@ExcelField(title = "c类客户总数", order = 23)
	private double cCustomerSum=0;
	//c类日均单量
	@ExcelField(title = "c类日均单量", order = 24)
	private double cOrderAvg=0;
	//c类面单量
	@ExcelField(title = "c类面单量", order = 25)
	private double cOrderSum=0;
	//c类返利金额百分比
	@ExcelField(title = "c类返利金额百分比", order = 26)
	private String cPricePercent;
	//c类返利金额面单数
	@ExcelField(title = "c类返利金额面单数", order = 27)
	private double cPriceSum=0;
	//c类返利金额总数
	@ExcelField(title = "c类返利金额总数", order = 28)
	private double cAllPriceSum=0;
	
	
	//d类客户总数
	@ExcelField(title = "d类客户总数", order = 29)
	private double dCustomerSum=0;
	//d类日均单量
	@ExcelField(title = "d类日均单量", order = 30)
	private double dOrderAvg=0;
	//d类面单量
	@ExcelField(title = "d类面单量", order = 31)
	private double dOrderSum=0;
	//d类返利金额百分比
	@ExcelField(title = "d类返利金额百分比", order = 32)
	private String dPricePercent;
	//d类返利金额面单数
	@ExcelField(title = "d类返利金额面单数", order = 33)
	private double dPriceSum=0;
	//d类返利金额总数
	@ExcelField(title = "d类返利金额总数", order = 34)
	private double dAllPriceSum=0;
	
	
	
	//e类客户总数
		@ExcelField(title = "e类客户总数", order = 35)
		private double eCustomerSum=0;
		//e类日均单量
		@ExcelField(title = "e类日均单量", order = 36)
		private double eOrderAvg=0;
		//e类面单量
		@ExcelField(title = "e类面单量", order = 37)
		private double eOrderSum=0;
		//e类返利金额百分比
		@ExcelField(title = "e类返利金额百分比", order = 38)
		private String ePricePercent;
		//e类返利金额面单数
		@ExcelField(title = "e类返利金额面单数", order = 39)
		private double ePriceSum=0;
		//e类返利金额总数
		@ExcelField(title = "e类返利金额总数", order = 40)
		private double eAllPriceSum=0;
	
		
		//f类客户总数
		@ExcelField(title = "f类客户总数", order = 41)
		private double fCustomerSum=0;
		//f类日均单量
		@ExcelField(title = "f类日均单量", order = 42)
		private double fOrderAvg=0;
		//f类面单量
		@ExcelField(title = "f类面单量", order = 43)
		private double fOrderSum=0;
		//f类返利金额百分比
		@ExcelField(title = "f类返利金额百分比", order = 44)
		private String fPricePercent;
		//f类返利金额面单数
		@ExcelField(title = "f类返利金额面单数", order = 45)
		private double fPriceSum=0;
		//f类返利金额总数
		@ExcelField(title = "f类返利金额总数", order = 46)
		private double fAllPriceSum=0;
		
		//g类客户总数
		@ExcelField(title = "g类客户总数", order = 47)
		private double gCustomerSum=0;
		//g类日均单量
		@ExcelField(title = "g类日均单量", order = 48)
		private double gOrderAvg=0;
		//g类面单量
		@ExcelField(title = "g类面单量", order = 49)
		private double gOrderSum=0;
		//g类返利金额百分比
		@ExcelField(title = "g类返利金额百分比", order = 50)
		private String gPricePercent;
		//g类返利金额面单数
		@ExcelField(title = "g类返利金额面单数", order = 51)
		private double gPriceSum=0;
		//g类返利金额总数
		@ExcelField(title = "g类返利金额总数", order = 52)
		private double gAllPriceSum=0;
		public String getCustomerName() {
			return customerName;
		}
		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}
		public double getOrderSum() {
			return orderSum;
		}
		public void setOrderSum(double orderSum) {
			this.orderSum = orderSum;
		}
		public double getOrderAvg() {
			return orderAvg;
		}
		public void setOrderAvg(double orderAvg) {
			this.orderAvg = orderAvg;
		}
		public double getDianziOrderSum() {
			return dianziOrderSum;
		}
		public void setDianziOrderSum(double dianziOrderSum) {
			this.dianziOrderSum = dianziOrderSum;
		}
		public double getOrdinaryOrderSum() {
			return ordinaryOrderSum;
		}
		public void setOrdinaryOrderSum(double ordinaryOrderSum) {
			this.ordinaryOrderSum = ordinaryOrderSum;
		}
		public String getDianziPercent() {
			return dianziPercent;
		}
		public void setDianziPercent(String dianziPercent) {
			this.dianziPercent = dianziPercent;
		}
		public double getCustomerSum() {
			return customerSum;
		}
		public void setCustomerSum(double customerSum) {
			this.customerSum = customerSum;
		}
		public double getDianziOrderSumAvg() {
			return dianziOrderSumAvg;
		}
		public void setDianziOrderSumAvg(double dianziOrderSumAvg) {
			this.dianziOrderSumAvg = dianziOrderSumAvg;
		}
		public double getDianziPriceSumAvg() {
			return dianziPriceSumAvg;
		}
		public void setDianziPriceSumAvg(double dianziPriceSumAvg) {
			this.dianziPriceSumAvg = dianziPriceSumAvg;
		}
		public double getDianziPriceSum() {
			return dianziPriceSum;
		}
		public void setDianziPriceSum(double dianziPriceSum) {
			this.dianziPriceSum = dianziPriceSum;
		}
		public double getaCustomerSum() {
			return aCustomerSum;
		}
		public void setaCustomerSum(double aCustomerSum) {
			this.aCustomerSum = aCustomerSum;
		}
		public double getaOrderAvg() {
			return aOrderAvg;
		}
		public void setaOrderAvg(double aOrderAvg) {
			this.aOrderAvg = aOrderAvg;
		}
		public double getaOrderSum() {
			return aOrderSum;
		}
		public void setaOrderSum(double aOrderSum) {
			this.aOrderSum = aOrderSum;
		}
		public String getaPricePercent() {
			return aPricePercent;
		}
		public void setaPricePercent(String aPricePercent) {
			this.aPricePercent = aPricePercent;
		}
		public double getaPriceSum() {
			return aPriceSum;
		}
		public void setaPriceSum(double aPriceSum) {
			this.aPriceSum = aPriceSum;
		}
		public double getaAllPriceSum() {
			return aAllPriceSum;
		}
		public void setaAllPriceSum(double aAllPriceSum) {
			this.aAllPriceSum = aAllPriceSum;
		}
		public double getbCustomerSum() {
			return bCustomerSum;
		}
		public void setbCustomerSum(double bCustomerSum) {
			this.bCustomerSum = bCustomerSum;
		}
		public double getbOrderAvg() {
			return bOrderAvg;
		}
		public void setbOrderAvg(double bOrderAvg) {
			this.bOrderAvg = bOrderAvg;
		}
		public double getbOrderSum() {
			return bOrderSum;
		}
		public void setbOrderSum(double bOrderSum) {
			this.bOrderSum = bOrderSum;
		}
		public String getbPricePercent() {
			return bPricePercent;
		}
		public void setbPricePercent(String bPricePercent) {
			this.bPricePercent = bPricePercent;
		}
		public double getbPriceSum() {
			return bPriceSum;
		}
		public void setbPriceSum(double bPriceSum) {
			this.bPriceSum = bPriceSum;
		}
		public double getbAllPriceSum() {
			return bAllPriceSum;
		}
		public void setbAllPriceSum(double bAllPriceSum) {
			this.bAllPriceSum = bAllPriceSum;
		}
		public double getcCustomerSum() {
			return cCustomerSum;
		}
		public void setcCustomerSum(double cCustomerSum) {
			this.cCustomerSum = cCustomerSum;
		}
		public double getcOrderAvg() {
			return cOrderAvg;
		}
		public void setcOrderAvg(double cOrderAvg) {
			this.cOrderAvg = cOrderAvg;
		}
		public double getcOrderSum() {
			return cOrderSum;
		}
		public void setcOrderSum(double cOrderSum) {
			this.cOrderSum = cOrderSum;
		}
		public String getcPricePercent() {
			return cPricePercent;
		}
		public void setcPricePercent(String cPricePercent) {
			this.cPricePercent = cPricePercent;
		}
		public double getcPriceSum() {
			return cPriceSum;
		}
		public void setcPriceSum(double cPriceSum) {
			this.cPriceSum = cPriceSum;
		}
		public double getcAllPriceSum() {
			return cAllPriceSum;
		}
		public void setcAllPriceSum(double cAllPriceSum) {
			this.cAllPriceSum = cAllPriceSum;
		}
		public double getdCustomerSum() {
			return dCustomerSum;
		}
		public void setdCustomerSum(double dCustomerSum) {
			this.dCustomerSum = dCustomerSum;
		}
		public double getdOrderAvg() {
			return dOrderAvg;
		}
		public void setdOrderAvg(double dOrderAvg) {
			this.dOrderAvg = dOrderAvg;
		}
		public double getdOrderSum() {
			return dOrderSum;
		}
		public void setdOrderSum(double dOrderSum) {
			this.dOrderSum = dOrderSum;
		}
		public String getdPricePercent() {
			return dPricePercent;
		}
		public void setdPricePercent(String dPricePercent) {
			this.dPricePercent = dPricePercent;
		}
		public double getdPriceSum() {
			return dPriceSum;
		}
		public void setdPriceSum(double dPriceSum) {
			this.dPriceSum = dPriceSum;
		}
		public double getdAllPriceSum() {
			return dAllPriceSum;
		}
		public void setdAllPriceSum(double dAllPriceSum) {
			this.dAllPriceSum = dAllPriceSum;
		}
		public double geteCustomerSum() {
			return eCustomerSum;
		}
		public void seteCustomerSum(double eCustomerSum) {
			this.eCustomerSum = eCustomerSum;
		}
		public double geteOrderAvg() {
			return eOrderAvg;
		}
		public void seteOrderAvg(double eOrderAvg) {
			this.eOrderAvg = eOrderAvg;
		}
		public double geteOrderSum() {
			return eOrderSum;
		}
		public void seteOrderSum(double eOrderSum) {
			this.eOrderSum = eOrderSum;
		}
		public String getePricePercent() {
			return ePricePercent;
		}
		public void setePricePercent(String ePricePercent) {
			this.ePricePercent = ePricePercent;
		}
		public double getePriceSum() {
			return ePriceSum;
		}
		public void setePriceSum(double ePriceSum) {
			this.ePriceSum = ePriceSum;
		}
		public double geteAllPriceSum() {
			return eAllPriceSum;
		}
		public void seteAllPriceSum(double eAllPriceSum) {
			this.eAllPriceSum = eAllPriceSum;
		}
		public double getfCustomerSum() {
			return fCustomerSum;
		}
		public void setfCustomerSum(double fCustomerSum) {
			this.fCustomerSum = fCustomerSum;
		}
		public String getfPricePercent() {
			return fPricePercent;
		}
		public void setfPricePercent(String fPricePercent) {
			this.fPricePercent = fPricePercent;
		}
		public double getfPriceSum() {
			return fPriceSum;
		}
		public void setfPriceSum(double fPriceSum) {
			this.fPriceSum = fPriceSum;
		}
		public double getfAllPriceSum() {
			return fAllPriceSum;
		}
		public void setfAllPriceSum(double fAllPriceSum) {
			this.fAllPriceSum = fAllPriceSum;
		}
		public double getgCustomerSum() {
			return gCustomerSum;
		}
		public void setgCustomerSum(double gCustomerSum) {
			this.gCustomerSum = gCustomerSum;
		}

		public double getfOrderAvg() {
			return fOrderAvg;
		}
		public void setfOrderAvg(double fOrderAvg) {
			this.fOrderAvg = fOrderAvg;
		}
		public double getfOrderSum() {
			return fOrderSum;
		}
		public void setfOrderSum(double fOrderSum) {
			this.fOrderSum = fOrderSum;
		}
		public double getgOrderAvg() {
			return gOrderAvg;
		}
		public void setgOrderAvg(double gOrderAvg) {
			this.gOrderAvg = gOrderAvg;
		}
		public double getgOrderSum() {
			return gOrderSum;
		}
		public void setgOrderSum(double gOrderSum) {
			this.gOrderSum = gOrderSum;
		}
		public String getgPricePercent() {
			return gPricePercent;
		}
		public void setgPricePercent(String gPricePercent) {
			this.gPricePercent = gPricePercent;
		}
		public double getgPriceSum() {
			return gPriceSum;
		}
		public void setgPriceSum(double gPriceSum) {
			this.gPriceSum = gPriceSum;
		}
		public double getgAllPriceSum() {
			return gAllPriceSum;
		}
		public void setgAllPriceSum(double gAllPriceSum) {
			this.gAllPriceSum = gAllPriceSum;
		}
	

}
