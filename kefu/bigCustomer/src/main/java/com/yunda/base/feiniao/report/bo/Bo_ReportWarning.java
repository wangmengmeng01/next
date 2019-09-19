package com.yunda.base.feiniao.report.bo;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.yunda.base.common.bo.Bo_Interface;
import com.yunda.ydmbspringbootstarter.common.annotation.ValidCrossWeek;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "预警表验参")
@ValidCrossWeek(fromDate = "startDate", toDate = "endDate" ,format="yyyy-MM-dd",Country="China", message = "开始时间和结束时间不能跨周")
public class Bo_ReportWarning implements Bo_Interface{
	
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
	
	@ApiModelProperty(value = "priceLevel")
	private String priceLevel;
	
	@ApiModelProperty(value = "search_month")
	private String search_month;
	//预警表月改周   预警周基础表的标识字段
	@ApiModelProperty(value = "search_week")
	private String search_week;
	/** 
	* 网点编码(网点权限)  省 市 大区的id  作为传入下钻列表的参数
	* 网点权限  为一级网点  包括该网点/下属分部/服务部
	*/
	@ApiModelProperty(value = "网点编码 ")
	private Integer branchCode;
	
	@ApiModelProperty(value = "provinceId")
	private String provinceId;
	
	@ApiModelProperty(value = "cityId")
	private String cityId;
	
	@ApiModelProperty(value = "bigarea")
	private String bigarea;
	
	@ApiModelProperty(value = "权限类型标识  S省 	D大区	 W网点")
	private String tmp_field;
	
	//管辖省份的id   省权限
	private List<Long> provinceIdsqx;
	//管辖大区   大区权限
	private List<String> regionIds;
	
	@ApiModelProperty(value = "每页查询数   偏移量")
	private int limit;
	
	@ApiModelProperty(value = "查询开始位置")
	private int offset;

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

	public Integer getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(Integer branchCode) {
		this.branchCode = branchCode;
	}

	public String getPriceLevel() {
		return priceLevel;
	}

	public void setPriceLevel(String priceLevel) {
		this.priceLevel = priceLevel;
	}
	
	public String getSearch_month() {
		return search_month;
	}

	public void setSearch_month(String search_month) {
		this.search_month = search_month;
	}

	public String getSearch_week() {
		return search_week;
	}

	public void setSearch_week(String search_week) {
		this.search_week = search_week;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getBigarea() {
		return bigarea;
	}

	public void setBigarea(String bigarea) {
		this.bigarea = bigarea;
	}

	public String getTmp_field() {
		return tmp_field;
	}

	public void setTmp_field(String tmp_field) {
		this.tmp_field = tmp_field;
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
	
	
}
