package com.yunda.base.bigcustomer.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;


/**
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-20111513
 */
public class CustomerManageDO implements Serializable {
    private static final long serialVersionUID = 1L;

    //序号
    @ExcelField(title = "序号", order = 1)
    private Integer id;
    //客户名称
    @ExcelField(title = "客户名称", order = 2)
    private String customerName;
    //客户编码
    private String customerId;
    //所属平台
    @ExcelField(title = "所属平台", order = 3)
    private String platform;
    //所属网点名称
    @ExcelField(title = "所属网点", order = 4)
    private String branch;
    //所属网点编码
    private String branchNum;
    //vip名称
    @ExcelField(title = "vip名称", order = 5)
    private String vipName;
    //vip编号
    @ExcelField(title = "vip编号", order = 6)
    private String vipNum;
    //所属机构
    @ExcelField(title = "所属机构", order = 7)
    private String organization;
    //机构名称
    private String organizationName;
    //机构编码
    private String organizationNum;
    //状态
    private String state;


    /**
     * 设置：序号
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：序号
     */
    public Integer getId() {
        return id;
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
     * 设置：所属平台
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * 获取：所属平台
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * 设置：所属网点
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     * 获取：所属网点
     */
    public String getBranch() {
        return branch;
    }

    /**
     * 设置：vip名称
     */
    public void setVipName(String vipName) {
        this.vipName = vipName;
    }

    /**
     * 获取：vip名称
     */
    public String getVipName() {
        return vipName;
    }

    /**
     * 设置：vip编号
     */
    public void setVipNum(String vipNum) {
        this.vipNum = vipNum;
    }

    /**
     * 获取：vip编号
     */
    public String getVipNum() {
        return vipNum;
    }

    /**
     * 设置：所属机构
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * 获取：所属机构
     */
    public String getOrganization() {
        return organization;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationNum() {
        return organizationNum;
    }

    public void setOrganizationNum(String organizationNum) {
        this.organizationNum = organizationNum;
    }

    public String getBranchNum() {
        return branchNum;
    }

    public void setBranchNum(String branchNum) {
        this.branchNum = branchNum;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}