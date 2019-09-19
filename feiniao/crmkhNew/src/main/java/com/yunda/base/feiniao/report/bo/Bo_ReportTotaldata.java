package com.yunda.base.feiniao.report.bo;

import com.yunda.base.common.bo.Bo_Interface;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

//需要分页参数的时候可以extends PageBo
@ApiModel(value = "客户咨询单参数")
public class Bo_ReportTotaldata implements Bo_Interface {
private static final long serialVersionUID = 1L;

@ApiModelProperty(value = "ydyzl")
private double ydyzl = 0;
@ApiModelProperty(value = "kdhyzl")
private double kdhyzl = 0;
/** 
* record_id - 数据记录主键ID 
*/
private int record_id;

/** 
* region_id - 大区 
*/
@ApiModelProperty(value = "所属大区")
private String region_id;

/** 
* province_id - 省编码 
*/
@ApiModelProperty(value = "省编码")
private String province_id;

/** 
* big_area - 大区 
*/
@ApiModelProperty(value = "大区")
private String big_area;

/** 
* province_name - 省份名称 
*/
@ApiModelProperty(value = "省份名称")
private String province_name;

/** 
* cityId - 城市编码 
*/
@ApiModelProperty(value = "城市编码")
private String cityId;

/** 
* city_id - 城市编码 
*/
@ApiModelProperty(value = "城市编码")
private String city_id;


/** 
* city_name - 城市名称 
*/
@ApiModelProperty(value = "城市名称 ")
private String city_name;

/** 
* fb_id - 分拨中心编码 
*/
@ApiModelProperty(value = "分拨中心编码 ")
private String fb_id;

/** 
* fb_name - 分拨中心名称 
*/
@ApiModelProperty(value = "分拨中心名称 ")
private String fb_name;

/** 
* customer_id - 客户编码 
*/
@ApiModelProperty(value = "客户编码 ")
private String customer_id;

/** 
* customer_name - 客户名称 
*/
@ApiModelProperty(value = "客户名称 ")
private String customer_name;

/** 
* branch_code - 网点编码 
*/
@ApiModelProperty(value = "网点编码 ")
private String branch_code;

/** 
* branch_name - 所属网点 
*/
@ApiModelProperty(value = "网点编码 ")
private String branch_name;

/** 
* customer_source_type - 客户来源 
*/
@ApiModelProperty(value = "客户来源")
private String customer_source_type;

/** 
* daily_order_num - 日订单量 
*/
@ApiModelProperty(value = "日订单量")
private String daily_order_num;

/** 
* week_ratio - 周环比 
*/
@ApiModelProperty(value = "周环比")
private String week_ratio;

/** 
* month_ratio - 月环比 
*/
@ApiModelProperty(value = "月环比")
private String month_ratio;

/** 
* cre_date - 创建时间 
*/
@ApiModelProperty(value = "创建时间")
private String cre_date;

/** 
* qu_date - 查询日期 
*/
@ApiModelProperty(value = "查询日期")
private String qu_date;

/** 大区权限 */
@ApiModelProperty(value = "大区权限")
private List<String> bigarea_filter;
/** 分拨中心权限 */
@ApiModelProperty(value = "分拨中心权限")
private List<String> fb_filter;
/** 省份权限 */
@ApiModelProperty(value = "省份权限")
private List<String> province_filter;
/** 客户创建时间 */
@ApiModelProperty(value = "客户创建时间")
private String customer_addtime;
/** 是否为新客户 */ 
@ApiModelProperty(value = "是否为新客户")
private String is_new;
/** 比较方式：大于等于，小于等于 */
@ApiModelProperty(value = "oper_daily_order_num")
private String oper_daily_order_num;
@ApiModelProperty(value = "oper_week_ratio")
private String oper_week_ratio;
@ApiModelProperty(value = "oper_month_ratio")
private String oper_month_ratio;
/*开始时间*/
@ApiModelProperty(value = "开始时间")
private String start_date;
/* 结束时间*/
@ApiModelProperty(value = "结束时间")
private String end_date;
/* 时间样式*/
@ApiModelProperty(value = "时间样式")
private String date_style;
/*选择周*/
@ApiModelProperty(value = "week_month_year")
private String week_month_year;
@ApiModelProperty(value = "week_month_date")
private String week_month_date;
@ApiModelProperty(value = "week_week_date")
private String week_week_date;
/*选择月*/
@ApiModelProperty(value = "month_year")
private String month_year;
@ApiModelProperty(value = "month_date")
private String month_date;
/*选择季度*/
@ApiModelProperty(value = "quarter_date")
private String quarter_date;
@ApiModelProperty(value = "quarter_year")
private String quarter_year;
@ApiModelProperty(value = "number_level")
private String number_level;
@ApiModelProperty(value = "report_date")
private String report_date;
@ApiModelProperty(value = "r_date")
private String r_date;
@ApiModelProperty(value = "y_date")
private String y_date;
@ApiModelProperty(value = "tb_type")
private String tb_type;
@ApiModelProperty(value = "btxz")
private String[] btxz;
@ApiModelProperty(value = "defen")
private int defen;
/*选择年*/
@ApiModelProperty(value = "选择年")
private String year;
/** 展示类型，默认展示总部即省汇总，city为展示城市，branch为展示网点,customer为展示客户*/
@ApiModelProperty(value = "展示类型，默认展示总部即省汇总，city为展示城市，branch为展示网点,customer为展示客户")
private String showType;
/**bdtype 流失类型或新增类型*/
@ApiModelProperty(value = "bdtype 流失类型或新增类型")
private String bd_type;
@ApiModelProperty(value = "tmp_field")
private String tmp_field;
@ApiModelProperty(value = "yue")
private String yue;
@ApiModelProperty(value = "ri_date")
private String ri_date;

@ApiModelProperty(value = "jichuxinxi")
private String jichuxinxi;
/** 
 * province_id - 省编码 
 */
@ApiModelProperty(value = "省编码")
private String provinceId;
@ApiModelProperty(value = "checkarea")
private int checkarea;

@ApiModelProperty(value = "total_flag")
private String total_flag;

@ApiModelProperty(value = "userId")
private String userId;
@ApiModelProperty(value = "userName")
private String userName;
@ApiModelProperty(value = "orgCode")
private String orgCode;
@ApiModelProperty(value = "orgName")
private String orgName;
@ApiModelProperty(value = "orgType")
private String orgType;
@ApiModelProperty(value = "userRoles")
private List<String> userRoles;
@ApiModelProperty(value = "lang")
private String lang;

@ApiModelProperty(value = "provinceIds")
private String[] provinceIds;

//管辖省份的id
private List<Long> provinceIdsqx;
//管辖大区
private List<String> regionIds;
//基础信息生效时间
private String startDay;

/* 大区权限*/
private List<String> bigareaNames;

/* 权限标识*/
private String tmpField;

/* 省份权限*/
private List<Long> provinceids;

/* 上级网点编码*/
private String branchCode;

/* 公司编码*/
private String gsbm;


/*开始时间*/
@ApiModelProperty(value = "开始时间")
//@NotBlank()
private String startDate;


/* 结束时间*/
@ApiModelProperty(value = "结束时间")
//@NotBlank()
private String endDate;


/* 每页查询数*/
@ApiModelProperty(value = "每页查询数")
private int limit;

/* 查询开始位置*/
@ApiModelProperty(value = "查询开始位置")
private int offset;


/* 省份名称*/
@ApiModelProperty(value = "省份名称")
private String provinceName;

/* 省份名称*/
@ApiModelProperty(value = "客户编码")
private String customerId;





public String getCustomerId() {
	return customerId;
}
public void setCustomerId(String customerId) {
	this.customerId = customerId;
}
public String getGsbm() {
	return gsbm;
}
public void setGsbm(String gsbm) {
	this.gsbm = gsbm;
}
public String getCity_id() {
	return city_id;
}
public void setCity_id(String city_id) {
	this.city_id = city_id;
}
public String getProvinceName() {
	return provinceName;
}
public void setProvinceName(String provinceName) {
	this.provinceName = provinceName;
}
public int getLimit() {
	return limit;
}
public void setLimit(int limit) {
	this.limit = limit;
}
public int getOffset() {
	return offset;
}
public void setOffset(int offset) {
	this.offset = offset;
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
public String getBranchCode() {
	return branchCode;
}
public void setBranchCode(String branchCode) {
	this.branchCode = branchCode;
}
public List<Long> getProvinceids() {
	return provinceids;
}
public void setProvinceids(List<Long> provinceids) {
	this.provinceids = provinceids;
}
public String getTmpField() {
	return tmpField;
}
public void setTmpField(String tmpField) {
	this.tmpField = tmpField;
}
public List<String> getBigareaNames() {
	return bigareaNames;
}
public void setBigareaNames(List<String> bigareaNames) {
	this.bigareaNames = bigareaNames;
}
public String getStartDay() {
	return startDay;
}
public void setStartDay(String startDay) {
	this.startDay = startDay;
}
public List<Long> getProvinceIdsqx() {
	return provinceIdsqx;
}
public void setProvinceIdsqx(List<Long> provinceIdsqx) {
	this.provinceIdsqx = provinceIdsqx;
}
public List<String> getRegionIds() {
	return regionIds;
}
public void setRegionIds(List<String> regionIds) {
	this.regionIds = regionIds;
}

public Object[] getKeys(){
	return new Object[]{ 
	record_id
	};
}

public String getBig_area() {
	return big_area;
}
public void setBig_area(String big_area) {
	this.big_area = big_area;
}
public String[] getProvinceIds() {
	return provinceIds;
}
public void setProvinceIds(String[] provinceIds) {
	this.provinceIds = provinceIds;
}
public double getYdyzl() {
	return ydyzl;
}
public void setYdyzl(double ydyzl) {
	this.ydyzl = ydyzl;
}
public double getKdhyzl() {
	return kdhyzl;
}
public void setKdhyzl(double kdhyzl) {
	this.kdhyzl = kdhyzl;
}
public int getRecord_id() {
	return record_id;
}
public void setRecord_id(int record_id) {
	this.record_id = record_id;
}
public String getRegion_id() {
	return region_id;
}
public void setRegion_id(String region_id) {
	this.region_id = region_id;
}
public String getProvince_id() {
	return province_id;
}
public void setProvince_id(String province_id) {
	this.province_id = province_id;
}
public String getProvince_name() {
	return province_name;
}
public void setProvince_name(String province_name) {
	this.province_name = province_name;
}
public String getCityId() {
	return cityId;
}
public void setCityId(String cityId) {
	this.cityId = cityId;
}
public String getCity_name() {
	return city_name;
}
public void setCity_name(String city_name) {
	this.city_name = city_name;
}
public String getFb_id() {
	return fb_id;
}
public void setFb_id(String fb_id) {
	this.fb_id = fb_id;
}
public String getFb_name() {
	return fb_name;
}
public void setFb_name(String fb_name) {
	this.fb_name = fb_name;
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
public String getBranch_code() {
	return branch_code;
}
public void setBranch_code(String branch_code) {
	this.branch_code = branch_code;
}
public String getBranch_name() {
	return branch_name;
}
public void setBranch_name(String branch_name) {
	this.branch_name = branch_name;
}
public String getCustomer_source_type() {
	return customer_source_type;
}
public void setCustomer_source_type(String customer_source_type) {
	this.customer_source_type = customer_source_type;
}
public String getDaily_order_num() {
	return daily_order_num;
}
public void setDaily_order_num(String daily_order_num) {
	this.daily_order_num = daily_order_num;
}
public String getWeek_ratio() {
	return week_ratio;
}
public void setWeek_ratio(String week_ratio) {
	this.week_ratio = week_ratio;
}
public String getMonth_ratio() {
	return month_ratio;
}
public void setMonth_ratio(String month_ratio) {
	this.month_ratio = month_ratio;
}
public String getCre_date() {
	return cre_date;
}
public void setCre_date(String cre_date) {
	this.cre_date = cre_date;
}
public String getQu_date() {
	return qu_date;
}
public void setQu_date(String qu_date) {
	this.qu_date = qu_date;
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
public String getCustomer_addtime() {
	return customer_addtime;
}
public void setCustomer_addtime(String customer_addtime) {
	this.customer_addtime = customer_addtime;
}
public String getIs_new() {
	return is_new;
}
public void setIs_new(String is_new) {
	this.is_new = is_new;
}
public String getOper_daily_order_num() {
	return oper_daily_order_num;
}
public void setOper_daily_order_num(String oper_daily_order_num) {
	this.oper_daily_order_num = oper_daily_order_num;
}
public String getOper_week_ratio() {
	return oper_week_ratio;
}
public void setOper_week_ratio(String oper_week_ratio) {
	this.oper_week_ratio = oper_week_ratio;
}
public String getOper_month_ratio() {
	return oper_month_ratio;
}
public void setOper_month_ratio(String oper_month_ratio) {
	this.oper_month_ratio = oper_month_ratio;
}
public String getStart_date() {
	return start_date;
}
public void setStart_date(String start_date) {
	this.start_date = start_date;
}
public String getEnd_date() {
	return end_date;
}
public void setEnd_date(String end_date) {
	this.end_date = end_date;
}
public String getDate_style() {
	return date_style;
}
public void setDate_style(String date_style) {
	this.date_style = date_style;
}
public String getWeek_month_year() {
	return week_month_year;
}
public void setWeek_month_year(String week_month_year) {
	this.week_month_year = week_month_year;
}
public String getWeek_month_date() {
	return week_month_date;
}
public void setWeek_month_date(String week_month_date) {
	this.week_month_date = week_month_date;
}
public String getWeek_week_date() {
	return week_week_date;
}
public void setWeek_week_date(String week_week_date) {
	this.week_week_date = week_week_date;
}
public String getMonth_year() {
	return month_year;
}
public void setMonth_year(String month_year) {
	this.month_year = month_year;
}
public String getMonth_date() {
	return month_date;
}
public void setMonth_date(String month_date) {
	this.month_date = month_date;
}
public String getQuarter_date() {
	return quarter_date;
}
public void setQuarter_date(String quarter_date) {
	this.quarter_date = quarter_date;
}
public String getQuarter_year() {
	return quarter_year;
}
public void setQuarter_year(String quarter_year) {
	this.quarter_year = quarter_year;
}
public String getNumber_level() {
	return number_level;
}
public void setNumber_level(String number_level) {
	this.number_level = number_level;
}
public String getReport_date() {
	return report_date;
}
public void setReport_date(String report_date) {
	this.report_date = report_date;
}
public String getR_date() {
	return r_date;
}
public void setR_date(String r_date) {
	this.r_date = r_date;
}
public String getY_date() {
	return y_date;
}
public void setY_date(String y_date) {
	this.y_date = y_date;
}
public String getTb_type() {
	return tb_type;
}
public void setTb_type(String tb_type) {
	this.tb_type = tb_type;
}
public String[] getBtxz() {
	return btxz;
}
public void setBtxz(String[] btxz) {
	this.btxz = btxz;
}
public int getDefen() {
	return defen;
}
public void setDefen(int defen) {
	this.defen = defen;
}
public String getYear() {
	return year;
}
public void setYear(String year) {
	this.year = year;
}
public String getShowType() {
	return showType;
}
public void setShowType(String showType) {
	this.showType = showType;
}
public String getBd_type() {
	return bd_type;
}
public void setBd_type(String bd_type) {
	this.bd_type = bd_type;
}
public String getTmp_field() {
	return tmp_field;
}
public void setTmp_field(String tmp_field) {
	this.tmp_field = tmp_field;
}
public String getYue() {
	return yue;
}
public void setYue(String yue) {
	this.yue = yue;
}
public String getRi_date() {
	return ri_date;
}
public void setRi_date(String ri_date) {
	this.ri_date = ri_date;
}
public String getJichuxinxi() {
	return jichuxinxi;
}
public void setJichuxinxi(String jichuxinxi) {
	this.jichuxinxi = jichuxinxi;
}
public String getProvinceId() {
	return provinceId;
}
public void setProvinceId(String provinceId) {
	this.provinceId = provinceId;
}
public int getCheckarea() {
	return checkarea;
}
public void setCheckarea(int checkarea) {
	this.checkarea = checkarea;
}
public String getTotal_flag() {
	return total_flag;
}
public void setTotal_flag(String total_flag) {
	this.total_flag = total_flag;
}
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public String getOrgCode() {
	return orgCode;
}
public void setOrgCode(String orgCode) {
	this.orgCode = orgCode;
}
public String getOrgName() {
	return orgName;
}
public void setOrgName(String orgName) {
	this.orgName = orgName;
}
public String getOrgType() {
	return orgType;
}
public void setOrgType(String orgType) {
	this.orgType = orgType;
}
public List<String> getUserRoles() {
	return userRoles;
}
public void setUserRoles(List<String> userRoles) {
	this.userRoles = userRoles;
}
public String getLang() {
	return lang;
}
public void setLang(String lang) {
	this.lang = lang;
}





///**
// * 设置：大区
// */
//public void setBigarea(String bigarea) {
//	this.bigarea = bigarea;
//}
///**
// * 获取：大区
// */
//public String getBigarea() {
//	return bigarea;
//}
///**
// * 设置：省ID
// */
//public void setProvinceid(String provinceid) {
//	this.provinceid = provinceid;
//}
///**
// * 获取：省ID
// */
//public String getProvinceid() {
//	return provinceid;
//}
///**
// * 设置：省名称
// */
//public void setProvincename(String provincename) {
//	this.provincename = provincename;
//}
///**
// * 获取：省名称
// */
//public String getProvincename() {
//	return provincename;
//}
///**
// * 设置：市ID
// */
//public void setCityid(String cityid) {
//	this.cityid = cityid;
//}
///**
// * 获取：市ID
// */
//public String getCityid() {
//	return cityid;
//}
///**
// * 设置：市名称
// */
//public void setCityname(String cityname) {
//	this.cityname = cityname;
//}
///**
// * 获取：市名称
// */
//public String getCityname() {
//	return cityname;
//}
///**
// * 设置：网点编码
// */
//public void setBranchCode(Integer branchCode) {
//	this.branchCode = branchCode;
//}
///**
// * 获取：网点编码
// */
//public Integer getBranchCode() {
//	return branchCode;
//}
///**
// * 设置：网点名称
// */
//public void setBranchName(String branchName) {
//	this.branchName = branchName;
//}
///**
// * 获取：网点名称
// */
//public String getBranchName() {
//	return branchName;
//}
///**
// * 设置：客户编码
// */
//public void setCustomerId(String customerId) {
//	this.customerId = customerId;
//}
///**
// * 获取：客户编码
// */
//public String getCustomerId() {
//	return customerId;
//}
///**
// * 设置：客户名称
// */
//public void setCustomerName(String customerName) {
//	this.customerName = customerName;
//}
///**
// * 获取：客户名称
// */
//public String getCustomerName() {
//	return customerName;
//}
///**
// * 设置：一级网点编码
// */
//public void setYjbm(Integer yjbm) {
//	this.yjbm = yjbm;
//}
///**
// * 获取：一级网点编码
// */
//public Integer getYjbm() {
//	return yjbm;
//}
///**
// * 设置：一级网点名称
// */
//public void setYjmc(String yjmc) {
//	this.yjmc = yjmc;
//}
///**
// * 获取：一级网点名称
// */
//public String getYjmc() {
//	return yjmc;
//}
///**
// * 设置：客户来源
// */
//public void setCustomerSourceType(String customerSourceType) {
//	this.customerSourceType = customerSourceType;
//}
///**
// * 获取：客户来源
// */
//public String getCustomerSourceType() {
//	return customerSourceType;
//}
///**
// * 设置：面单量
// */
//public void setOrderSum(Double orderSum) {
//	this.orderSum = orderSum;
//}
///**
// * 获取：面单量
// */
//public Double getOrderSum() {
//	return orderSum;
//}
///**
// * 设置：二维码面单量
// */
//public void setQrcodeOrderSum(Double qrcodeOrderSum) {
//	this.qrcodeOrderSum = qrcodeOrderSum;
//}
///**
// * 获取：二维码面单量
// */
//public Double getQrcodeOrderSum() {
//	return qrcodeOrderSum;
//}
///**
// * 设置：菜鸟面单量
// */
//public void setCainiaoOrderSum(Double cainiaoOrderSum) {
//	this.cainiaoOrderSum = cainiaoOrderSum;
//}
///**
// * 获取：菜鸟面单量
// */
//public Double getCainiaoOrderSum() {
//	return cainiaoOrderSum;
//}
///**
// * 设置：电子面单量
// */
//public void setOrdinaryOrderSum(Double ordinaryOrderSum) {
//	this.ordinaryOrderSum = ordinaryOrderSum;
//}
///**
// * 获取：电子面单量
// */
//public Double getOrdinaryOrderSum() {
//	return ordinaryOrderSum;
//}
///**
// * 设置：日均单量
// */
//public void setOrderAvg(Double orderAvg) {
//	this.orderAvg = orderAvg;
//}
///**
// * 获取：日均单量
// */
//public Double getOrderAvg() {
//	return orderAvg;
//}
///**
// * 设置：面单量返利金额
// */
//public void setPriceSum(Double priceSum) {
//	this.priceSum = priceSum;
//}
///**
// * 获取：面单量返利金额
// */
//public Double getPriceSum() {
//	return priceSum;
//}
///**
// * 设置：电子面单返利金额
// */
//public void setDianziOrderSum(Double dianziOrderSum) {
//	this.dianziOrderSum = dianziOrderSum;
//}
///**
// * 获取：电子面单返利金额
// */
//public Double getDianziOrderSum() {
//	return dianziOrderSum;
//}
///**
// * 设置：电子面单占比
// */
//public void setDianziNumPercent(Float dianziNumPercent) {
//	this.dianziNumPercent = dianziNumPercent;
//}
///**
// * 获取：电子面单占比
// */
//public Float getDianziNumPercent() {
//	return dianziNumPercent;
//}
///**
// * 设置：创建日期
// */
//public void setCreateDate(Date createDate) {
//	this.createDate = createDate;
//}
///**
// * 获取：创建日期
// */
//public Date getCreateDate() {
//	return createDate;
//}
///**
// * 设置：订单日期
// */
//public void setReportDate(Date reportDate) {
//	this.reportDate = reportDate;
//}
///**
// * 获取：订单日期
// */
//public Date getReportDate() {
//	return reportDate;
//}
///**
// * 设置：电子面单百分比
// */
//public void setDianziPercent(String dianziPercent) {
//	this.dianziPercent = dianziPercent;
//}
///**
// * 获取：电子面单百分比
// */
//public String getDianziPercent() {
//	return dianziPercent;
//}
///**
// * 设置：a类客户总数
// */
//public void setACustomerSum(BigDecimal aCustomerSum) {
//	this.aCustomerSum = aCustomerSum;
//}
///**
// * 获取：a类客户总数
// */
//public BigDecimal getACustomerSum() {
//	return aCustomerSum;
//}
///**
// * 设置：b类客户总数
// */
//public void setBCustomerSum(BigDecimal bCustomerSum) {
//	this.bCustomerSum = bCustomerSum;
//}
///**
// * 获取：b类客户总数
// */
//public BigDecimal getBCustomerSum() {
//	return bCustomerSum;
//}
///**
// * 设置：c类客户总数
// */
//public void setCCustomerSum(BigDecimal cCustomerSum) {
//	this.cCustomerSum = cCustomerSum;
//}
///**
// * 获取：c类客户总数
// */
//public BigDecimal getCCustomerSum() {
//	return cCustomerSum;
//}
///**
// * 设置：d类客户总数
// */
//public void setDCustomerSum(BigDecimal dCustomerSum) {
//	this.dCustomerSum = dCustomerSum;
//}
///**
// * 获取：d类客户总数
// */
//public BigDecimal getDCustomerSum() {
//	return dCustomerSum;
//}
///**
// * 设置：e类客户总数
// */
//public void setECustomerSum(BigDecimal eCustomerSum) {
//	this.eCustomerSum = eCustomerSum;
//}
///**
// * 获取：e类客户总数
// */
//public BigDecimal getECustomerSum() {
//	return eCustomerSum;
//}
///**
// * 设置：f类客户总数
// */
//public void setFCustomerSum(BigDecimal fCustomerSum) {
//	this.fCustomerSum = fCustomerSum;
//}
///**
// * 获取：f类客户总数
// */
//public BigDecimal getFCustomerSum() {
//	return fCustomerSum;
//}
///**
// * 设置：g类客户总数
// */
//public void setGCustomerSum(BigDecimal gCustomerSum) {
//	this.gCustomerSum = gCustomerSum;
//}
///**
// * 获取：g类客户总数
// */
//public BigDecimal getGCustomerSum() {
//	return gCustomerSum;
//}
///**
// * 设置：a类日均单量
// */
//public void setAOrderAvg(Double aOrderAvg) {
//	this.aOrderAvg = aOrderAvg;
//}
///**
// * 获取：a类日均单量
// */
//public Double getAOrderAvg() {
//	return aOrderAvg;
//}
///**
// * 设置：b类日均单量
// */
//public void setBOrderAvg(Double bOrderAvg) {
//	this.bOrderAvg = bOrderAvg;
//}
///**
// * 获取：b类日均单量
// */
//public Double getBOrderAvg() {
//	return bOrderAvg;
//}
///**
// * 设置：c类日均单量
// */
//public void setCOrderAvg(Double cOrderAvg) {
//	this.cOrderAvg = cOrderAvg;
//}
///**
// * 获取：c类日均单量
// */
//public Double getCOrderAvg() {
//	return cOrderAvg;
//}
///**
// * 设置：d类日均单量
// */
//public void setDOrderAvg(Double dOrderAvg) {
//	this.dOrderAvg = dOrderAvg;
//}
///**
// * 获取：d类日均单量
// */
//public Double getDOrderAvg() {
//	return dOrderAvg;
//}
///**
// * 设置：e类日均单量
// */
//public void setEOrderAvg(Double eOrderAvg) {
//	this.eOrderAvg = eOrderAvg;
//}
///**
// * 获取：e类日均单量
// */
//public Double getEOrderAvg() {
//	return eOrderAvg;
//}
///**
// * 设置：f类日均单量
// */
//public void setFOrderAvg(Double fOrderAvg) {
//	this.fOrderAvg = fOrderAvg;
//}
///**
// * 获取：f类日均单量
// */
//public Double getFOrderAvg() {
//	return fOrderAvg;
//}
///**
// * 设置：g类日均单量
// */
//public void setGOrderAvg(Double gOrderAvg) {
//	this.gOrderAvg = gOrderAvg;
//}
///**
// * 获取：g类日均单量
// */
//public Double getGOrderAvg() {
//	return gOrderAvg;
//}
///**
// * 设置：a类面单量
// */
//public void setAOrderSum(Double aOrderSum) {
//	this.aOrderSum = aOrderSum;
//}
///**
// * 获取：a类面单量
// */
//public Double getAOrderSum() {
//	return aOrderSum;
//}
///**
// * 设置：b类面单量
// */
//public void setBOrderSum(Double bOrderSum) {
//	this.bOrderSum = bOrderSum;
//}
///**
// * 获取：b类面单量
// */
//public Double getBOrderSum() {
//	return bOrderSum;
//}
///**
// * 设置：c类面单量
// */
//public void setCOrderSum(Double cOrderSum) {
//	this.cOrderSum = cOrderSum;
//}
///**
// * 获取：c类面单量
// */
//public Double getCOrderSum() {
//	return cOrderSum;
//}
///**
// * 设置：d类面单量
// */
//public void setDOrderSum(Double dOrderSum) {
//	this.dOrderSum = dOrderSum;
//}
///**
// * 获取：d类面单量
// */
//public Double getDOrderSum() {
//	return dOrderSum;
//}
///**
// * 设置：e类面单量
// */
//public void setEOrderSum(Double eOrderSum) {
//	this.eOrderSum = eOrderSum;
//}
///**
// * 获取：e类面单量
// */
//public Double getEOrderSum() {
//	return eOrderSum;
//}
///**
// * 设置：f类面单量
// */
//public void setFOrderSum(Double fOrderSum) {
//	this.fOrderSum = fOrderSum;
//}
///**
// * 获取：f类面单量
// */
//public Double getFOrderSum() {
//	return fOrderSum;
//}
///**
// * 设置：g类面单量
// */
//public void setGOrderSum(Double gOrderSum) {
//	this.gOrderSum = gOrderSum;
//}
///**
// * 获取：g类面单量
// */
//public Double getGOrderSum() {
//	return gOrderSum;
//}
///**
// * 设置：a类返利金额面单数
// */
//public void setAPriceSum(Double aPriceSum) {
//	this.aPriceSum = aPriceSum;
//}
///**
// * 获取：a类返利金额面单数
// */
//public Double getAPriceSum() {
//	return aPriceSum;
//}
///**
// * 设置：b类返利金额面单数
// */
//public void setBPriceSum(Double bPriceSum) {
//	this.bPriceSum = bPriceSum;
//}
///**
// * 获取：b类返利金额面单数
// */
//public Double getBPriceSum() {
//	return bPriceSum;
//}
///**
// * 设置：c类返利金额面单数
// */
//public void setCPriceSum(Double cPriceSum) {
//	this.cPriceSum = cPriceSum;
//}
///**
// * 获取：c类返利金额面单数
// */
//public Double getCPriceSum() {
//	return cPriceSum;
//}
///**
// * 设置：d类返利金额面单数
// */
//public void setDPriceSum(Double dPriceSum) {
//	this.dPriceSum = dPriceSum;
//}
///**
// * 获取：d类返利金额面单数
// */
//public Double getDPriceSum() {
//	return dPriceSum;
//}
///**
// * 设置：e类返利金额面单数
// */
//public void setEPriceSum(Double ePriceSum) {
//	this.ePriceSum = ePriceSum;
//}
///**
// * 获取：e类返利金额面单数
// */
//public Double getEPriceSum() {
//	return ePriceSum;
//}
///**
// * 设置：f类返利金额面单数
// */
//public void setFPriceSum(Double fPriceSum) {
//	this.fPriceSum = fPriceSum;
//}
///**
// * 获取：f类返利金额面单数
// */
//public Double getFPriceSum() {
//	return fPriceSum;
//}
///**
// * 设置：g类返利金额面单数
// */
//public void setGPriceSum(Double gPriceSum) {
//	this.gPriceSum = gPriceSum;
//}
///**
// * 获取：g类返利金额面单数
// */
//public Double getGPriceSum() {
//	return gPriceSum;
//}
///**
// * 设置：a类返利金额总数
// */
//public void setAAllPriceSum(Double aAllPriceSum) {
//	this.aAllPriceSum = aAllPriceSum;
//}
///**
// * 获取：a类返利金额总数
// */
//public Double getAAllPriceSum() {
//	return aAllPriceSum;
//}
///**
// * 设置：b类返利金额总数
// */
//public void setBAllPriceSum(Double bAllPriceSum) {
//	this.bAllPriceSum = bAllPriceSum;
//}
///**
// * 获取：b类返利金额总数
// */
//public Double getBAllPriceSum() {
//	return bAllPriceSum;
//}
///**
// * 设置：c类返利金额总数
// */
//public void setCAllPriceSum(Double cAllPriceSum) {
//	this.cAllPriceSum = cAllPriceSum;
//}
///**
// * 获取：c类返利金额总数
// */
//public Double getCAllPriceSum() {
//	return cAllPriceSum;
//}
///**
// * 设置：d类返利金额总数
// */
//public void setDAllPriceSum(Double dAllPriceSum) {
//	this.dAllPriceSum = dAllPriceSum;
//}
///**
// * 获取：d类返利金额总数
// */
//public Double getDAllPriceSum() {
//	return dAllPriceSum;
//}
///**
// * 设置：e类返利金额总数
// */
//public void setEAllPriceSum(Double eAllPriceSum) {
//	this.eAllPriceSum = eAllPriceSum;
//}
///**
// * 获取：e类返利金额总数
// */
//public Double getEAllPriceSum() {
//	return eAllPriceSum;
//}
///**
// * 设置：f类返利金额总数
// */
//public void setFAllPriceSum(Double fAllPriceSum) {
//	this.fAllPriceSum = fAllPriceSum;
//}
///**
// * 获取：f类返利金额总数
// */
//public Double getFAllPriceSum() {
//	return fAllPriceSum;
//}
///**
// * 设置：g类返利金额总数
// */
//public void setGAllPriceSum(Double gAllPriceSum) {
//	this.gAllPriceSum = gAllPriceSum;
//}
///**
// * 获取：g类返利金额总数
// */
//public Double getGAllPriceSum() {
//	return gAllPriceSum;
//}
///**
// * 设置：a类返利金额百分比
// */
//public void setAPricePercent(Float aPricePercent) {
//	this.aPricePercent = aPricePercent;
//}
///**
// * 获取：a类返利金额百分比
// */
//public Float getAPricePercent() {
//	return aPricePercent;
//}
///**
// * 设置：b类返利金额百分比
// */
//public void setBPricePercent(Float bPricePercent) {
//	this.bPricePercent = bPricePercent;
//}
///**
// * 获取：b类返利金额百分比
// */
//public Float getBPricePercent() {
//	return bPricePercent;
//}
///**
// * 设置：c类返利金额百分比
// */
//public void setCPricePercent(Float cPricePercent) {
//	this.cPricePercent = cPricePercent;
//}
///**
// * 获取：c类返利金额百分比
// */
//public Float getCPricePercent() {
//	return cPricePercent;
//}
///**
// * 设置：d类返利金额百分比
// */
//public void setDPricePercent(Float dPricePercent) {
//	this.dPricePercent = dPricePercent;
//}
///**
// * 获取：d类返利金额百分比
// */
//public Float getDPricePercent() {
//	return dPricePercent;
//}
///**
// * 设置：e类返利金额百分比
// */
//public void setEPricePercent(Float ePricePercent) {
//	this.ePricePercent = ePricePercent;
//}
///**
// * 获取：e类返利金额百分比
// */
//public Float getEPricePercent() {
//	return ePricePercent;
//}
///**
// * 设置：f类返利金额百分比
// */
//public void setFPricePercent(Float fPricePercent) {
//	this.fPricePercent = fPricePercent;
//}
///**
// * 获取：f类返利金额百分比
// */
//public Float getFPricePercent() {
//	return fPricePercent;
//}
///**
// * 设置：g类返利金额百分比
// */
//public void setGPricePercent(Float gPricePercent) {
//	this.gPricePercent = gPricePercent;
//}
///**
// * 获取：g类返利金额百分比
// */
//public Float getGPricePercent() {
//	return gPricePercent;
//}
///**
// * 设置：客户订单量
// */
//public void setCustomerSum(Double customerSum) {
//	this.customerSum = customerSum;
//}
///**
// * 获取：客户订单量
// */
//public Double getCustomerSum() {
//	return customerSum;
//}
///**
// * 设置：客户平均单量
// */
//public void setCustomerAvgSum(Double customerAvgSum) {
//	this.customerAvgSum = customerAvgSum;
//}
///**
// * 获取：客户平均单量
// */
//public Double getCustomerAvgSum() {
//	return customerAvgSum;
//}
///**
// * 设置：客户返利单量
// */
//public void setCustomerPriceSum(Double customerPriceSum) {
//	this.customerPriceSum = customerPriceSum;
//}
///**
// * 获取：客户返利单量
// */
//public Double getCustomerPriceSum() {
//	return customerPriceSum;
//}
///**
// * 设置：客户返利单量
// */
//public void setCustomerAllPriceSum(Double customerAllPriceSum) {
//	this.customerAllPriceSum = customerAllPriceSum;
//}
///**
// * 获取：客户返利单量
// */
//public Double getCustomerAllPriceSum() {
//	return customerAllPriceSum;
//}

	
}