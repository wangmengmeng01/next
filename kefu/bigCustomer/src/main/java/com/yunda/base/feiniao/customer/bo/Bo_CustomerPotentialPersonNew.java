package com.yunda.base.feiniao.customer.bo;

import java.util.List;

import com.yunda.base.common.bo.Bo_Interface;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "潜在客户新表验参")
public class Bo_CustomerPotentialPersonNew  implements Bo_Interface{
	
	/** 每页查询数*/
	@ApiModelProperty(value = "每页查询数")
	private int limit;
	
	/** 查询开始位置*/
	@ApiModelProperty(value = "查询开始位置")
	private int offset;
	
	/** 客户名称*/
	@ApiModelProperty(value = "客户名称")
	private String customerName;
	
	//
	@ApiModelProperty(value = "店铺名称")
	private String shopName;
	
	@ApiModelProperty(value = "日均件量")
	private Double dailyOrderAvg;
	
	@ApiModelProperty(value = "省份")
	private String provinceId;
	@ApiModelProperty(value = "省份名称")
	private String provinceName;
	
	@ApiModelProperty(value = "大区")
	private String bigarea;
	
	@ApiModelProperty(value = "上传人员")
	private String handlerId;
	//上传人员姓名
	@ApiModelProperty(value = "上传人员姓名")
	private String handlerName;
	//维护时间
	@ApiModelProperty(value = "维护时间")
	private String updateTime;
	
	//维护时间
	@ApiModelProperty(value = "开始时间")
	private String startupdateTime;
	//维护时间
	@ApiModelProperty(value = "结束时间")
	private String endupdateTime;
	
	@ApiModelProperty(value = "查询日均件量")
	private String startDailyOrderAvg;
	
	@ApiModelProperty(value = "查询日均件量")
	private String endDailyOrderAvg;
	/**
	 * 省份权限
	 */
	private List<Long> provinceids;
	/** 
	* branchCode - 网点权限
	*/
	@ApiModelProperty(value = "网点权限 ")
	private String branchCodeQX;
	
	
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	
	public String getStartupdateTime() {
		return startupdateTime;
	}
	public void setStartupdateTime(String startupdateTime) {
		this.startupdateTime = startupdateTime;
	}
	public String getEndupdateTime() {
		return endupdateTime;
	}
	public void setEndupdateTime(String endupdateTime) {
		this.endupdateTime = endupdateTime;
	}
	public String getStartDailyOrderAvg() {
		return startDailyOrderAvg;
	}
	public void setStartDailyOrderAvg(String startDailyOrderAvg) {
		this.startDailyOrderAvg = startDailyOrderAvg;
	}
	public String getEndDailyOrderAvg() {
		return endDailyOrderAvg;
	}
	public void setEndDailyOrderAvg(String endDailyOrderAvg) {
		this.endDailyOrderAvg = endDailyOrderAvg;
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
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Double getDailyOrderAvg() {
		return dailyOrderAvg;
	}
	public void setDailyOrderAvg(Double dailyOrderAvg) {
		this.dailyOrderAvg = dailyOrderAvg;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getBigarea() {
		return bigarea;
	}
	public void setBigarea(String bigarea) {
		this.bigarea = bigarea;
	}
	public String getHandlerId() {
		return handlerId;
	}
	public void setHandlerId(String handlerId) {
		this.handlerId = handlerId;
	}
	public String getHandlerName() {
		return handlerName;
	}
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public List<Long> getProvinceids() {
		return provinceids;
	}
	public void setProvinceids(List<Long> provinceids) {
		this.provinceids = provinceids;
	}
	public String getBranchCodeQX() {
		return branchCodeQX;
	}
	public void setBranchCodeQX(String branchCodeQX) {
		this.branchCodeQX = branchCodeQX;
	}
	
	
	
}
