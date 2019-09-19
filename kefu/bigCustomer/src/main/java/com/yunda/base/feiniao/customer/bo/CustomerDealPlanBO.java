package com.yunda.base.feiniao.customer.bo;

import java.io.Serializable;

/**
 * @author beidouxing
 * @create 2019/03/19 16:48
 */
public class CustomerDealPlanBO implements Serializable {
    /*组织*/
    private String organization;
    /*隐藏的部分,用来判断是否是点击省公司名称*/
    private String organizationH;
    /*网点编码*/
    private String branchCode;
    //一级网点编码
    private String mcCode;
    /*开始时间*/
    private String startDate;
    /*状态*/
    private String state;
    /* 结束时间*/
    private String endDate;
    /* 统计揽件量开始时间*/
    private String tongjiStartDate;
    /* 统计揽件量结束时间*/
    private String tongjiEndDate;
    /*客户编码*/
    private String customerId;
    /* 每页查询数*/
    private Integer limit;

    /* 查询开始位置*/
    private Integer offset;

    private String provinceIds;


    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getOrganizationH() {
        return organizationH;
    }

    public void setOrganizationH(String organizationH) {
        this.organizationH = organizationH;
    }

    public String getMcCode() {
        return mcCode;
    }

    public void setMcCode(String mcCode) {
        this.mcCode = mcCode;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProvinceIds() {
        return provinceIds;
    }

    public void setProvinceIds(String provinceIds) {
        this.provinceIds = provinceIds;
    }

    public String getTongjiStartDate() {
        return tongjiStartDate;
    }

    public void setTongjiStartDate(String tongjiStartDate) {
        this.tongjiStartDate = tongjiStartDate;
    }

    public String getTongjiEndDate() {
        return tongjiEndDate;
    }

    public void setTongjiEndDate(String tongjiEndDate) {
        this.tongjiEndDate = tongjiEndDate;
    }
}
