package com.yunda.base.feiniao.warning.domain;

import java.io.Serializable;
import java.util.Date;

import com.github.crab2died.annotation.ExcelField;


/**
 * 预警反馈表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-13095948
 */
public class WarningHandleDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	@ExcelField(title = "id", order = 1)
	private Long id;
	//客户编码
	@ExcelField(title = "客户编码", order = 2)
	private String customerId;
	//客户名称
	@ExcelField(title = "客户名称", order = 3)
	private String customerName;
	//所属站点编码(公司)
	@ExcelField(title = "所属站点编码(公司)", order = 4)
	private Integer gs;
	//所属站点名称(公司)
	@ExcelField(title = "所属站点名称(公司)", order = 5)
	private String gsmc;
	//上级网点编码
	@ExcelField(title = "上级网点编码", order = 6)
	private Integer branchCode;
	//上级网点名称
	@ExcelField(title = "上级网点名称", order = 7)
	private String branchName;
	//该条数据的预警日期
	@ExcelField(title = "该条数据的预警日期", order = 8)
	private Date warnDate;
	//日均单量
	@ExcelField(title = "日均单量", order = 9)
	private Double orderAvg;
	//上周日均量
	@ExcelField(title = "上周日均量", order = 10)
	private Double lastOrderAvg;
	//降幅比率
	@ExcelField(title = "降幅比率", order = 11)
	private String reducedRatio;
	//降幅单量
	@ExcelField(title = "降幅单量", order = 12)
	private Double reducedOrder;
	//客户来源
	@ExcelField(title = "客户来源", order = 13)
	private String customerSourceType;
	//客户类型
	@ExcelField(title = "客户类型", order = 14)
	private String priceLevel;
	//反馈截止时间--标识字段 无业务功能
	@ExcelField(title = "反馈截止时间--标识字段 无业务功能", order = 15)
	private Date feedbackDeadline;
	
	private String showWarnDate;
	private String showFeedbackDeadline;
	//反馈状态
	@ExcelField(title = "反馈状态", order = 16)
	private String feedbackStatus;
	//备注
	@ExcelField(title = "备注", order = 17)
	private String remark;
	//市id
	@ExcelField(title = "市id", order = 18)
	private String cityid;
	//市名称
	@ExcelField(title = "市名称", order = 19)
	private String cityname;
	//省id
	@ExcelField(title = "省id", order = 20)
	private String provinceid;
	//省名称
	@ExcelField(title = "省名称", order = 21)
	private String provincename;
	//大区
	@ExcelField(title = "大区", order = 22)
	private String bigarea;
	//网点反馈类型
	@ExcelField(title = "网点反馈类型", order = 23)
	private String branchDealType;
	//网点反馈内容
	@ExcelField(title = "网点反馈内容", order = 24)
	private String branchDealDesc;
	//网点反馈人
	@ExcelField(title = "网点反馈人", order = 25)
	private String barnchDealUser;
	//网点反馈时间--用来罚款的
	@ExcelField(title = "网点反馈时间--用来罚款的", order = 26)
	private Date branchFeedbackDate;
	//省总反馈内容
	@ExcelField(title = "省总反馈内容", order = 27)
	private String provinceDealDesc;
	//省总反馈人
	@ExcelField(title = "省总反馈人", order = 28)
	private String provinceDealUser;
	//省总反馈时间
	@ExcelField(title = "省总反馈时间", order = 29)
	private Date provinceFeedbackDate;
	//总部反馈内容
	@ExcelField(title = "总部反馈内容", order = 30)
	private String zbDealDesc;
	//总部反馈人
	@ExcelField(title = "总部反馈人", order = 31)
	private String zbDealUser;
	//总部反馈时间
	@ExcelField(title = "总部反馈时间", order = 32)
	private Date zbFeedbackDate;
	//客户来源
	@ExcelField(title = "客户来源")
	private String showCustomerSourceType;
	
	@ExcelField(title = "页面显示客户类型")
	private String showPriceLevel;
	//网点反馈类型
	@ExcelField(title = "网点反馈类型")
	private String showBranchDealType;
	//反馈状态
	@ExcelField(title = "页面显示反馈状态")
	private String showFeedbackStatus;
	
	@ExcelField(title = "网点反馈时间")
	private String showBranchFeedbackDate;
	@ExcelField(title = "省总反馈时间")
	private String showProvinceFeedbackDate;
	@ExcelField(title = "总部反馈时间")
	private String showZbFeedbackDate;
	
	public String getShowCustomerSourceType() {
		 if("1".equals(customerSourceType)){
			 showCustomerSourceType =  "菜鸟";
	   	 }else if("2".equals(priceLevel)){
	   		showCustomerSourceType =  "二维码";
	   	 }else if("3".equals(priceLevel)){
	   		showCustomerSourceType =  "京东";
	   	 }else if("5".equals(priceLevel)){
	   		showCustomerSourceType =  "拼多多";
	   	 }
		return showCustomerSourceType;
	}
	public void setShowCustomerSourceType(String showCustomerSourceType) {
		this.showCustomerSourceType = showCustomerSourceType;
	}
	
	public String getShowPriceLevel() {
		 if("b".equals(priceLevel)){
			 showPriceLevel =  "B类";
	   	 }else if("c".equals(priceLevel)){
	   		showPriceLevel =  "C类";
	   	 }else if("d".equals(priceLevel)){
	   		showPriceLevel =  "D类";
	   	 }else if("e".equals(priceLevel)){
	   		showPriceLevel =  "E类";
	   	 }else if("f".equals(priceLevel)){
	   		showPriceLevel =  "F类";
	   	 }else if("g".equals(priceLevel)){
	   		showPriceLevel =  "G类";
	   	 }
		return showPriceLevel;
	}
	public void setShowPriceLevel(String showPriceLevel) {
		this.showPriceLevel = showPriceLevel;
	}
	public String getShowFeedbackStatus() {
		 if("A".equals(feedbackStatus)){
			 showFeedbackStatus =  "网点待处理";
	   	 }else if("B".equals(feedbackStatus)){
	   		showFeedbackStatus =  "网点超时待处理";
	   	 }else if("C".equals(feedbackStatus)){
	   		showFeedbackStatus =  "省总待处理";
	   	 }else if("D".equals(feedbackStatus)){
	   		showFeedbackStatus =  "总部待处理";
	   	 }else if("E".equals(feedbackStatus)){
		   	showFeedbackStatus =  "总部已处理";
		 }
		return showFeedbackStatus;
	}
	public void setShowFeedbackStatus(String showFeedbackStatus) {
		this.showFeedbackStatus = showFeedbackStatus;
	}
	public String getShowBranchDealType() {
		 if("A".equals(branchDealType)){
			 showBranchDealType =  "A.价格政策";
	   	 }else if("B".equals(branchDealType)){
	   		showBranchDealType =  "B.服务质量";
	   	 }else if("C".equals(branchDealType)){
	   		showBranchDealType =  "C.增值服务";
	   	 }else if("D".equals(branchDealType)){
	   		showBranchDealType =  "D.资金压力";
	   	 }
		return showBranchDealType;
	}
	public void setShowBranchDealType(String showBranchDealType) {
		this.showBranchDealType = showBranchDealType;
	}
	
	

	public String getShowBranchFeedbackDate() {
		return showBranchFeedbackDate;
	}
	public void setShowBranchFeedbackDate(String showBranchFeedbackDate) {
		this.showBranchFeedbackDate = showBranchFeedbackDate;
	}
	public String getShowProvinceFeedbackDate() {
		return showProvinceFeedbackDate;
	}
	public void setShowProvinceFeedbackDate(String showProvinceFeedbackDate) {
		this.showProvinceFeedbackDate = showProvinceFeedbackDate;
	}
	public String getShowZbFeedbackDate() {
		return showZbFeedbackDate;
	}
	public void setShowZbFeedbackDate(String showZbFeedbackDate) {
		this.showZbFeedbackDate = showZbFeedbackDate;
	}
	public String getShowWarnDate() {
		return showWarnDate;
	}
	public void setShowWarnDate(String showWarnDate) {
		this.showWarnDate = showWarnDate;
	}
	public String getShowFeedbackDeadline() {
		return showFeedbackDeadline;
	}
	public void setShowFeedbackDeadline(String showFeedbackDeadline) {
		this.showFeedbackDeadline = showFeedbackDeadline;
	}
	/**
	 * 设置：id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Long getId() {
		return id;
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
	 * 设置：所属站点编码(公司)
	 */
	public void setGs(Integer gs) {
		this.gs = gs;
	}
	/**
	 * 获取：所属站点编码(公司)
	 */
	public Integer getGs() {
		return gs;
	}
	/**
	 * 设置：所属站点名称(公司)
	 */
	public void setGsmc(String gsmc) {
		this.gsmc = gsmc;
	}
	/**
	 * 获取：所属站点名称(公司)
	 */
	public String getGsmc() {
		return gsmc;
	}
	/**
	 * 设置：上级网点编码
	 */
	public void setBranchCode(Integer branchCode) {
		this.branchCode = branchCode;
	}
	/**
	 * 获取：上级网点编码
	 */
	public Integer getBranchCode() {
		return branchCode;
	}
	/**
	 * 设置：上级网点名称
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	/**
	 * 获取：上级网点名称
	 */
	public String getBranchName() {
		return branchName;
	}
	/**
	 * 设置：该条数据的预警日期
	 */
	public void setWarnDate(Date warnDate) {
		this.warnDate = warnDate;
	}
	/**
	 * 获取：该条数据的预警日期
	 */
	public Date getWarnDate() {
		return warnDate;
	}
	/**
	 * 设置：日均单量
	 */
	public void setOrderAvg(Double orderAvg) {
		this.orderAvg = orderAvg;
	}
	/**
	 * 获取：日均单量
	 */
	public Double getOrderAvg() {
		return orderAvg;
	}
	/**
	 * 设置：上周日均量
	 */
	public void setLastOrderAvg(Double lastOrderAvg) {
		this.lastOrderAvg = lastOrderAvg;
	}
	/**
	 * 获取：上周日均量
	 */
	public Double getLastOrderAvg() {
		return lastOrderAvg;
	}
	/**
	 * 设置：降幅比率
	 */
	public void setReducedRatio(String reducedRatio) {
		this.reducedRatio = reducedRatio;
	}
	/**
	 * 获取：降幅比率
	 */
	public String getReducedRatio() {
		return reducedRatio;
	}
	/**
	 * 设置：降幅单量
	 */
	public void setReducedOrder(Double reducedOrder) {
		this.reducedOrder = reducedOrder;
	}
	/**
	 * 获取：降幅单量
	 */
	public Double getReducedOrder() {
		return reducedOrder;
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
	 * 设置：客户类型
	 */
	public void setPriceLevel(String priceLevel) {
		this.priceLevel = priceLevel;
	}
	/**
	 * 获取：客户类型
	 */
	public String getPriceLevel() {
		return priceLevel;
	}
	/**
	 * 设置：反馈截止时间--标识字段 无业务功能
	 */
	public void setFeedbackDeadline(Date feedbackDeadline) {
		this.feedbackDeadline = feedbackDeadline;
	}
	/**
	 * 获取：反馈截止时间--标识字段 无业务功能
	 */
	public Date getFeedbackDeadline() {
		return feedbackDeadline;
	}
	/**
	 * 设置：反馈状态
	 */
	public void setFeedbackStatus(String feedbackStatus) {
		this.feedbackStatus = feedbackStatus;
	}
	/**
	 * 获取：反馈状态
	 */
	public String getFeedbackStatus() {
		return feedbackStatus;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：市id
	 */
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	/**
	 * 获取：市id
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
	 * 设置：省id
	 */
	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}
	/**
	 * 获取：省id
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
	 * 设置：网点反馈类型
	 */
	public void setBranchDealType(String branchDealType) {
		this.branchDealType = branchDealType;
	}
	/**
	 * 获取：网点反馈类型
	 */
	public String getBranchDealType() {
		return branchDealType;
	}
	/**
	 * 设置：网点反馈内容
	 */
	public void setBranchDealDesc(String branchDealDesc) {
		this.branchDealDesc = branchDealDesc;
	}
	/**
	 * 获取：网点反馈内容
	 */
	public String getBranchDealDesc() {
		return branchDealDesc;
	}
	/**
	 * 设置：网点反馈人
	 */
	public void setBarnchDealUser(String barnchDealUser) {
		this.barnchDealUser = barnchDealUser;
	}
	/**
	 * 获取：网点反馈人
	 */
	public String getBarnchDealUser() {
		return barnchDealUser;
	}
	/**
	 * 设置：网点反馈时间--用来罚款的
	 */
	public void setBranchFeedbackDate(Date branchFeedbackDate) {
		this.branchFeedbackDate = branchFeedbackDate;
	}
	/**
	 * 获取：网点反馈时间--用来罚款的
	 */
	public Date getBranchFeedbackDate() {
		return branchFeedbackDate;
	}
	/**
	 * 设置：省总反馈内容
	 */
	public void setProvinceDealDesc(String provinceDealDesc) {
		this.provinceDealDesc = provinceDealDesc;
	}
	/**
	 * 获取：省总反馈内容
	 */
	public String getProvinceDealDesc() {
		return provinceDealDesc;
	}
	/**
	 * 设置：省总反馈人
	 */
	public void setProvinceDealUser(String provinceDealUser) {
		this.provinceDealUser = provinceDealUser;
	}
	/**
	 * 获取：省总反馈人
	 */
	public String getProvinceDealUser() {
		return provinceDealUser;
	}
	/**
	 * 设置：省总反馈时间
	 */
	public void setProvinceFeedbackDate(Date provinceFeedbackDate) {
		this.provinceFeedbackDate = provinceFeedbackDate;
	}
	/**
	 * 获取：省总反馈时间
	 */
	public Date getProvinceFeedbackDate() {
		return provinceFeedbackDate;
	}
	/**
	 * 设置：总部反馈内容
	 */
	public void setZbDealDesc(String zbDealDesc) {
		this.zbDealDesc = zbDealDesc;
	}
	/**
	 * 获取：总部反馈内容
	 */
	public String getZbDealDesc() {
		return zbDealDesc;
	}
	/**
	 * 设置：总部反馈人
	 */
	public void setZbDealUser(String zbDealUser) {
		this.zbDealUser = zbDealUser;
	}
	/**
	 * 获取：总部反馈人
	 */
	public String getZbDealUser() {
		return zbDealUser;
	}
	/**
	 * 设置：总部反馈时间
	 */
	public void setZbFeedbackDate(Date zbFeedbackDate) {
		this.zbFeedbackDate = zbFeedbackDate;
	}
	/**
	 * 获取：总部反馈时间
	 */
	public Date getZbFeedbackDate() {
		return zbFeedbackDate;
	}
}
