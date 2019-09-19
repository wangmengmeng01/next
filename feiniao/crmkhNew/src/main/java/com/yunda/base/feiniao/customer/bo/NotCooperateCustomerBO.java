package com.yunda.base.feiniao.customer.bo;

import com.github.crab2died.annotation.ExcelField;
import com.yunda.base.feiniao.customer.domain.CooperatePeerDO;

import io.swagger.annotations.ApiModelProperty;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

public class NotCooperateCustomerBO implements Serializable {
	private static final long serialVersionUID = 1L;

	//id
	private Integer id;
	//网点编码
	private String branchCode;
	//一级网点编码
	private String mcCode;
	//客户名称
	private String customerName;
	//客户联系电话
	private String customerPhone;
	/*开始时间*/
	private String startDate;
	/* 结束时间*/
	private String endDate;
	//状态
	private String state;
	/* 每页查询数*/
	@ApiModelProperty(value = "每页查询数")
	private int limit;

	/* 查询开始位置*/
	@ApiModelProperty(value = "查询开始位置")
	private int offset;
	/* 大区权限*/
	private List<String> bigareaNames;

	/* 权限标识*/
	private String tmpField;

	/* 省份权限*/
	private List<Long> provinceids;
	//省名称
	private String provinceName;
	//市名称
	private String city;
	/* 省总是否拜访*/
	private String provinceVisit;
	//拜访时间--开始
	private String startVisitDate;
	//拜访时间--结束
	private String endVisitDate;
	
	//管辖省份的名称   省权限
	private List<String> provinceNamesQX;
	//管辖大区   大区权限
	private List<String> regionIdsQX;
	
	public List<String> getProvinceNamesQX() {
		return provinceNamesQX;
	}

	public void setProvinceNamesQX(List<String> provinceNamesQX) {
		this.provinceNamesQX = provinceNamesQX;
	}

	public List<String> getRegionIdsQX() {
		return regionIdsQX;
	}

	public void setRegionIdsQX(List<String> regionIdsQX) {
		this.regionIdsQX = regionIdsQX;
	}

	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvinceVisit() {
		return provinceVisit;
	}

	public void setProvinceVisit(String provinceVisit) {
		this.provinceVisit = provinceVisit;
	}

	public String getStartVisitDate() {
		return startVisitDate;
	}

	public void setStartVisitDate(String startVisitDate) {
		this.startVisitDate = startVisitDate;
	}

	public String getEndVisitDate() {
		return endVisitDate;
	}

	public void setEndVisitDate(String endVisitDate) {
		this.endVisitDate = endVisitDate;
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

	public List<String> getBigareaNames() {
		return bigareaNames;
	}

	public void setBigareaNames(List<String> bigareaNames) {
		this.bigareaNames = bigareaNames;
	}

	public String getTmpField() {
		return tmpField;
	}

	public void setTmpField(String tmpField) {
		this.tmpField = tmpField;
	}

	public List<Long> getProvinceids() {
		return provinceids;
	}

	public void setProvinceids(List<Long> provinceids) {
		this.provinceids = provinceids;
	}

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getMcCode() {
		return mcCode;
	}

	public void setMcCode(String mcCode) {
		this.mcCode = mcCode;
	}

	@Override
	public String toString() {
		return "NotCooperateCustomerBO{" +
				"id=" + id +
				", branchCode='" + branchCode + '\'' +
				", mcCode='" + mcCode + '\'' +
				", customerName='" + customerName + '\'' +
				", customerPhone='" + customerPhone + '\'' +
				", startDate='" + startDate + '\'' +
				", endDate='" + endDate + '\'' +
				", state='" + state + '\'' +
				", limit=" + limit +
				", offset=" + offset +
				", bigareaNames=" + bigareaNames +
				", tmpField='" + tmpField + '\'' +
				", provinceids=" + provinceids +
				", provinceName='" + provinceName + '\'' +
				'}';
	}
}
