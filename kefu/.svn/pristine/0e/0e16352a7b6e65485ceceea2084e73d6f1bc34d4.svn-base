package com.yunda.base.feiniao.report.bo;

import com.yunda.base.common.bo.Bo_Interface;
import com.yunda.ydmbspringbootstarter.common.annotation.ValidBetweenDays;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

//需要分页参数的时候可以extends PageBo
@ApiModel(value = "波动表验参")
@ValidBetweenDays(fromField = "startDate", toField = "endDate" ,format="yyyy-MM-dd", days = 31, message = "开始时间和结束时间间隔大于30天")
public class Bo_ReportFluctuate implements Bo_Interface {
	
	/** 
	* bigarea - 大区名 
	*/
	@ApiModelProperty(value = "大区大区名")
	private String bigarea;
	
	/** 
	* regionId - 大区 
	*/
	@ApiModelProperty(value = "大区")
	private String regionId;
	
	/** 
	* provinceid - 省编码 
	*/
	@ApiModelProperty(value = "省编码")
	private String provinceid;

	/** 
	* province_name - 省份名称 
	*/
	@ApiModelProperty(value = "省份名称")
	private String provinceName;

	/** 
	* cityid - 城市编码 
	*/
	@ApiModelProperty(value = "城市编码")
	private String cityid;

	/** 
	* cityName - 城市名称 
	*/
	@ApiModelProperty(value = "城市名称 ")
	private String cityName;

	/** 
	* customerId - 客户编码 
	*/
	@ApiModelProperty(value = "客户编码 ")
	private String customerId;

	/** 
	* customerName - 客户名称 
	*/
	@ApiModelProperty(value = "客户名称 ")
	private String customerName;

	/** 
	* branchCode - 网点编码 
	*/
	@ApiModelProperty(value = "网点编码 ")
	private String branchCode;

	/** 
	* branchName - 所属网点 
	*/
	@ApiModelProperty(value = "网点编码 ")
	private String branchName;

	/** 
	* customerSourceType - 客户来源 
	*/
	@ApiModelProperty(value = "客户来源")
	private String customerSourceType;

	/** 
	* daily_order_num - 日订单量 
	*/
	@ApiModelProperty(value = "日订单量")
	private String dailyOrderNum;

	/** 
	* quDate - 查询日期 
	*/
	@ApiModelProperty(value = "查询日期")
	private String quDate;


	/*开始时间*/
	@ApiModelProperty(value = "开始时间")
	@NotBlank()
	private String startDate;
	
	
	/* 结束时间*/
	@ApiModelProperty(value = "结束时间")
	@NotBlank()
	private String endDate;

	/** 展示类型，默认展示总部即省汇总，city为展示城市，branch为展示网点,customer为展示客户*/
	@ApiModelProperty(value = "展示类型，默认展示总部即省汇总，city为展示城市，branch为展示网点,customer为展示客户")
	private String showType;
	
	/**bdtype 流失类型或新增类型*/
	@ApiModelProperty(value = "bdtype 流失类型或新增类型")
	private String bdType;
	
	@ApiModelProperty(value = "tmp_field")
	private String tmpField;

	/** 
	 * provinceid - 省编码 
	 */
	@ApiModelProperty(value = "省编码")
	private String provinceId;

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
	
	@ApiModelProperty(value = "每页查询数")
	private int limit;
	
	@ApiModelProperty(value = "查询开始位置")
	private int offset;
	
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

	public String getBigarea() {
		return bigarea;
	}

	public void setBigarea(String bigarea) {
		this.bigarea = bigarea;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getProvinceid() {
		return provinceid;
	}

	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
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

	public String getDailyOrderNum() {
		return dailyOrderNum;
	}

	public void setDailyOrderNum(String dailyOrderNum) {
		this.dailyOrderNum = dailyOrderNum;
	}

	public String getQuDate() {
		return quDate;
	}

	public void setQuDate(String quDate) {
		this.quDate = quDate;
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

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getBdType() {
		return bdType;
	}

	public void setBdType(String bdType) {
		this.bdType = bdType;
	}

	public String getTmpField() {
		return tmpField;
	}

	public void setTmpField(String tmpField) {
		this.tmpField = tmpField;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
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

	public String[] getProvinceIds() {
		return provinceIds;
	}

	public void setProvinceIds(String[] provinceIds) {
		this.provinceIds = provinceIds;
	}

	public List<String> getBigareaNames() {
		return bigareaNames;
	}

	public void setBigareaNames(List<String> bigareaNames) {
		this.bigareaNames = bigareaNames;
	}

	public List<Long> getProvinceids() {
		return provinceids;
	}

	public void setProvinceids(List<Long> provinceids) {
		this.provinceids = provinceids;
	}

	@ApiModelProperty(value = "userRoles")
	private List<String> userRoles;
	
	@ApiModelProperty(value = "provinceIds")
	private String[] provinceIds;

	/**
	 * 大区权限
	 */
	private List<String> bigareaNames;
	
	/**
	 * 省份权限
	 */
	private List<Long> provinceids;

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
}
