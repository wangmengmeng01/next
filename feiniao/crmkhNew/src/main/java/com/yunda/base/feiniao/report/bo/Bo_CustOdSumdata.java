package com.yunda.base.feiniao.report.bo;

import com.yunda.base.common.bo.Bo_Interface;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

//需要分页参数的时候可以extends PageBo
@ApiModel(value = "客户咨询单参数")
public class Bo_CustOdSumdata implements Bo_Interface {
private static final long serialVersionUID = 1L;
//
////大区
//@ApiModelProperty(value = "大区") 
//private String bigarea;
////省ID
//@ApiModelProperty(value = "省ID") 
//private String provinceid;
////省名称
//@ApiModelProperty(value = "省名称") 
//private String provincename;
////市ID
//@ApiModelProperty(value = "市ID") 
//private String cityid;
////市名称
//@ApiModelProperty(value = "市名称") 
//private String cityname;
////网点编码
//@ApiModelProperty(value = "网点编码") 
//private Integer branchCode;
////网点名称
//@ApiModelProperty(value = "网点名称") 
//private String branchName;
////客户编码
//@ApiModelProperty(value = "客户编码") 
//private String customerId;
////客户名称
//@ApiModelProperty(value = "客户名称") 
//private String customerName;
////一级网点编码
//@ApiModelProperty(value = "一级网点编码") 
//private Integer yjbm;
////一级网点名称
//@ApiModelProperty(value = "一级网点名称") 
//private String yjmc;
////客户来源
//@ApiModelProperty(value = "客户来源") 
//private String customerSourceType;
////面单量
//@ApiModelProperty(value = "面单量") 
//private Double orderSum;
////二维码面单量
//@ApiModelProperty(value = "二维码面单量") 
//private Double qrcodeOrderSum;
////菜鸟面单量
//@ApiModelProperty(value = "菜鸟面单量") 
//private Double cainiaoOrderSum;
////电子面单量
//@ApiModelProperty(value = "电子面单量") 
//private Double ordinaryOrderSum;
////日均单量
//@ApiModelProperty(value = "日均单量") 
//private Double orderAvg;
////面单量返利金额
//@ApiModelProperty(value = "面单量返利金额") 
//private Double priceSum;
////电子面单返利金额
//@ApiModelProperty(value = "电子面单返利金额") 
//private Double dianziOrderSum;
////电子面单占比
//@ApiModelProperty(value = "电子面单占比") 
//private Float dianziNumPercent;
////创建日期
//@ApiModelProperty(value = "创建日期")
//private Date createDate;
////订单日期
//@ApiModelProperty(value = "订单日期") 
//private Date reportDate;
////电子面单百分比
//@ApiModelProperty(value = "电子面单百分比") 
//private String dianziPercent;
////a类客户总数
//@ApiModelProperty(value = "a类客户总数") 
//private BigDecimal aCustomerSum;
////b类客户总数
//@ApiModelProperty(value = "b类客户总数") 
//private BigDecimal bCustomerSum;
////c类客户总数
//@ApiModelProperty(value = "c类客户总数") 
//private BigDecimal cCustomerSum;
////d类客户总数
//@ApiModelProperty(value = "d类客户总数") 
//private BigDecimal dCustomerSum;
////e类客户总数
//@ApiModelProperty(value = "e类客户总数") 
//private BigDecimal eCustomerSum;
////f类客户总数
//@ApiModelProperty(value = "f类客户总数") 
//private BigDecimal fCustomerSum;
////g类客户总数
//@ApiModelProperty(value = "g类客户总数") 
//private BigDecimal gCustomerSum;
////a类日均单量
//@ApiModelProperty(value = "a类日均单量") 
//private Double aOrderAvg;
////b类日均单量
//@ApiModelProperty(value = "b类日均单量")
//private Double bOrderAvg;
////c类日均单量
//@ApiModelProperty(value = "c类日均单量")
//private Double cOrderAvg;
////d类日均单量
//@ApiModelProperty(value = "d类日均单量")
//private Double dOrderAvg;
////e类日均单量
//@ApiModelProperty(value = "e类日均单量")
//private Double eOrderAvg;
////f类日均单量
//@ApiModelProperty(value = "f类日均单量")
//private Double fOrderAvg;
////g类日均单量
//@ApiModelProperty(value = "g类日均单量")
//private Double gOrderAvg;
////a类面单量
//@ApiModelProperty(value = "a类面单量")
//private Double aOrderSum;
////b类面单量
//@ApiModelProperty(value = "b类面单量")
//private Double bOrderSum;
////c类面单量
//@ApiModelProperty(value = "c类面单量")
//private Double cOrderSum;
////d类面单量
//@ApiModelProperty(value = "d类面单量")
//private Double dOrderSum;
////e类面单量
//@ApiModelProperty(value = "e类面单量")
//private Double eOrderSum;
////f类面单量
//@ApiModelProperty(value = "f类面单量")
//private Double fOrderSum;
////g类面单量
//@ApiModelProperty(value = "g类面单量")
//private Double gOrderSum;
////a类返利金额面单数
//@ApiModelProperty(value = "a类返利金额面单数")
//private Double aPriceSum;
////b类返利金额面单数
//@ApiModelProperty(value = "b类返利金额面单数")
//private Double bPriceSum;
////c类返利金额面单数
//@ApiModelProperty(value = "c类返利金额面单数")
//private Double cPriceSum;
////d类返利金额面单数
//@ApiModelProperty(value = "d类返利金额面单数")
//private Double dPriceSum;
////e类返利金额面单数
//@ApiModelProperty(value = "e类返利金额面单数") 
//private Double ePriceSum;
////f类返利金额面单数
//@ApiModelProperty(value = "f类返利金额面单数") 
//private Double fPriceSum;
////g类返利金额面单数
//@ApiModelProperty(value = "g类返利金额面单数") 
//private Double gPriceSum;
////a类返利金额总数
//@ApiModelProperty(value = "a类返利金额总数") 
//private Double aAllPriceSum;
////b类返利金额总数
//@ApiModelProperty(value = "b类返利金额总数")
//private Double bAllPriceSum;
////c类返利金额总数
//@ApiModelProperty(value = "c类返利金额总数")
//private Double cAllPriceSum;
////d类返利金额总数
//@ApiModelProperty(value = "d类返利金额总数")
//private Double dAllPriceSum;
////e类返利金额总数
//@ApiModelProperty(value = "e类返利金额总数")
//private Double eAllPriceSum;
////f类返利金额总数
//@ApiModelProperty(value = "f类返利金额总数")
//private Double fAllPriceSum;
////g类返利金额总数
//@ApiModelProperty(value = "g类返利金额总数")
//private Double gAllPriceSum;
////a类返利金额百分比
//@ApiModelProperty(value = "a类返利金额百分比")
//private Float aPricePercent;
////b类返利金额百分比
//@ApiModelProperty(value = "b类返利金额百分比")
//private Float bPricePercent;
////c类返利金额百分比
//@ApiModelProperty(value = "c类返利金额百分比")
//private Float cPricePercent;
////d类返利金额百分比
//@ApiModelProperty(value = "d类返利金额百分比")
//private Float dPricePercent;
////e类返利金额百分比
//@ApiModelProperty(value = "e类返利金额百分比")
//private Float ePricePercent;
////f类返利金额百分比
//@ApiModelProperty(value = "f类返利金额百分比")
//private Float fPricePercent;
////g类返利金额百分比
//@ApiModelProperty(value = "g类返利金额百分比")
//private Float gPricePercent;
////客户订单量
//@ApiModelProperty(value = "客户订单量")
//private Double customerSum;
////客户平均单量
//@ApiModelProperty(value = "客户平均单量")
//private Double customerAvgSum;
////客户返利单量
//@ApiModelProperty(value = "客户返利单量")
//private Double customerPriceSum;
////客户返利单量
//@ApiModelProperty(value = "客户返利单量")
//private Double customerAllPriceSum;

public Object[] getKeys(){
	return new Object[]{ 
	record_id
	};
}

	/** 
	 * record_id - 数据记录主键ID 
	 */
	@ApiModelProperty(value = "数据记录主键ID")
	private int record_id;
	 /** 
	 * customer_id - 客户编码 
	 */
	@ApiModelProperty(value = "客户编码")
	private String customer_id;
	 /** 
	 * customer_name - 客户名称 
	 */
	@ApiModelProperty(value = "客户名称")
	private String customer_name;
	 /** 
	 * branch_code - 所属1级网点编码 
	 */
	@ApiModelProperty(value = "所属1级网点编码")
	private int branch_code;
	 /** 
	 * customer_source_type - 客户来源 
	 */
	@ApiModelProperty(value = "客户来源")
	private String customer_source_type;
	 /** 
	 * gs - 上级公司编码 
	 */
	@ApiModelProperty(value = "上级公司编码")
	private int gs;
	 /** 
	 * order_sum - 单量 
	 */
	@ApiModelProperty(value = "单量")
	private double order_sum;
	 /** 
	 * order_avg - 平均单量 
	 */
	@ApiModelProperty(value = "平均单量")
	private double order_avg;
	 /** 
	 * price_level - 客户金额等级 
	 */
	@ApiModelProperty(value = "客户金额等级")
	private String price_level;
	 /** 
	 * price_num - 金额 
	 */
	@ApiModelProperty(value = "金额")
	private double price_num;
	 /** 
	 * all_price_num - 全部金额 
	 */
	@ApiModelProperty(value = "全部金额")
	private double all_price_num;
	/** 大区权限 */
	@ApiModelProperty(value = "大区权限")
	private List<String> bigarea_filter;
	 /** 分拨中心权限 */
	@ApiModelProperty(value = "分拨中心权限")
	private List<String> fb_filter;
	 /** 省份权限 */
	@ApiModelProperty(value = "省份权限")
	 private List<String> province_filter;
	 /** 网点编码 */
	@ApiModelProperty(value = "网点编码")
	 private String cust_branch;
	 /** 开始时间 */
	@ApiModelProperty(value = "开始时间")
	 private String start_time;
	 /** 结束时间 */
	@ApiModelProperty(value = "结束时间")
	 private String end_time;
	@ApiModelProperty(value = "prefix")
	 private String prefix;
	@ApiModelProperty(value = "fbbm")
	 private String fbbm;
	@ApiModelProperty(value = "yjbm")
	 private String yjbm;
	@ApiModelProperty(value = "yjmc")
	 private String yjmc;
	@ApiModelProperty(value = "branch_name")
	 private String branch_name;
	@ApiModelProperty(value = "biaolei")
	 private String biaolei;
	@ApiModelProperty(value = "wdlx")
	 private String wdlx;

	public int getRecord_id() {
		return record_id;
	}
	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	public int getBranch_code() {
		return branch_code;
	}
	public void setBranch_code(int branch_code) {
		this.branch_code = branch_code;
	}
	public String getCustomer_source_type() {
		return customer_source_type;
	}
	public void setCustomer_source_type(String customer_source_type) {
		this.customer_source_type = customer_source_type;
	}
	public int getGs() {
		return gs;
	}
	public void setGs(int gs) {
		this.gs = gs;
	}
	public double getOrder_sum() {
		return order_sum;
	}
	public void setOrder_sum(double order_sum) {
		this.order_sum = order_sum;
	}
	public double getOrder_avg() {
		return order_avg;
	}
	public void setOrder_avg(double order_avg) {
		this.order_avg = order_avg;
	}
	public String getPrice_level() {
		return price_level;
	}
	public void setPrice_level(String price_level) {
		this.price_level = price_level;
	}
	public double getPrice_num() {
		return price_num;
	}
	public void setPrice_num(double price_num) {
		this.price_num = price_num;
	}
	public double getAll_price_num() {
		return all_price_num;
	}
	public void setAll_price_num(double all_price_num) {
		this.all_price_num = all_price_num;
	}
	public List<String> getBigarea_filter() {
		return bigarea_filter;
	}
	public void setBigarea_filter(List<String> bigarea_filter) {
		this.bigarea_filter = bigarea_filter;
	}
	public List<String> getFb_filter() {
		return fb_filter;
	}
	public void setFb_filter(List<String> fb_filter) {
		this.fb_filter = fb_filter;
	}
	public List<String> getProvince_filter() {
		return province_filter;
	}
	public void setProvince_filter(List<String> province_filter) {
		this.province_filter = province_filter;
	}
	public String getCust_branch() {
		return cust_branch;
	}
	public void setCust_branch(String cust_branch) {
		this.cust_branch = cust_branch;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getFbbm() {
		return fbbm;
	}
	public void setFbbm(String fbbm) {
		this.fbbm = fbbm;
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
	public String getBranch_name() {
		return branch_name;
	}
	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}
	public String getBiaolei() {
		return biaolei;
	}
	public void setBiaolei(String biaolei) {
		this.biaolei = biaolei;
	}
	public String getWdlx() {
		return wdlx;
	}
	public void setWdlx(String wdlx) {
		this.wdlx = wdlx;
	}
	
}
