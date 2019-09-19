package com.yunda.base.bigcustomer.domain;

import java.io.Serializable;

import com.github.crab2died.annotation.ExcelField;


/**
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-22162719
 */
public class ConsultConfigDO implements Serializable {
    private static final long serialVersionUID = 1L;

    //序号
    @ExcelField(title = "序号", order = 1)
    private Integer id;
    //咨询来源
    @ExcelField(title = "咨询来源", order = 2)
    private String consultSource;
    //咨询类型
    @ExcelField(title = "咨询类型", order = 3)
    private String consultType;
    //处理时效
    @ExcelField(title = "处理时效", order = 4)
    private String dealShiXiao;
    //状态
    @ExcelField(title = "状态", order = 5)
    private String state;
    //时效处理类型
    private String type;
    //咨询发单后多少小时内未结单，则咨询单处理超时
    private String orderAfterHoursT1;
    //当天发起的咨询单,第几天几点前未结单,则咨询单处理超时
    private String orderAfterDayT2;
    private String orderAfterTimeT2;
    //当天几点前发起的咨询单,第几天几点前未结单,则咨询单处理超时
    private String todayOrderBeforeTimeT31;
    private String orderAfterDayT31;
    private String orderAfterTimeT31;
    //当天几点后发起的咨询单,第几天几点前未结单,则咨询单处理超时
    private String todayOrderAfterTime32;
    private String orderBeforeDayT32;
    private String orderBeforeTimeT32;
    //超时前多少小时为预警状态
    private String yujingTime;



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
     * 设置：咨询来源
     */
    public void setConsultSource(String consultSource) {
        this.consultSource = consultSource;
    }

    /**
     * 获取：咨询来源
     */
    public String getConsultSource() {
        return consultSource;
    }

    /**
     * 设置：咨询类型
     */
    public void setConsultType(String consultType) {
        this.consultType = consultType;
    }

    /**
     * 获取：咨询类型
     */
    public String getConsultType() {
        return consultType;
    }

    /**
     * 设置：处理时效
     */
    public void setDealShiXiao(String dealShiXiao) {
        this.dealShiXiao = dealShiXiao;
    }

    /**
     * 获取：处理时效
     */
    public String getDealShiXiao() {
        return dealShiXiao;
    }

    /**
     * 设置：状态
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取：状态
     */
    public String getState() {
        return state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrderAfterHoursT1() {
        return orderAfterHoursT1;
    }

    public void setOrderAfterHoursT1(String orderAfterHoursT1) {
        this.orderAfterHoursT1 = orderAfterHoursT1;
    }

    public String getOrderAfterDayT2() {
        return orderAfterDayT2;
    }

    public void setOrderAfterDayT2(String orderAfterDayT2) {
        this.orderAfterDayT2 = orderAfterDayT2;
    }

    public String getOrderAfterTimeT2() {
        return orderAfterTimeT2;
    }

    public void setOrderAfterTimeT2(String orderAfterTimeT2) {
        this.orderAfterTimeT2 = orderAfterTimeT2;
    }

    public String getTodayOrderBeforeTimeT31() {
        return todayOrderBeforeTimeT31;
    }

    public void setTodayOrderBeforeTimeT31(String todayOrderBeforeTimeT31) {
        this.todayOrderBeforeTimeT31 = todayOrderBeforeTimeT31;
    }

    public String getOrderAfterDayT31() {
        return orderAfterDayT31;
    }

    public void setOrderAfterDayT31(String orderAfterDayT31) {
        this.orderAfterDayT31 = orderAfterDayT31;
    }

    public String getOrderAfterTimeT31() {
        return orderAfterTimeT31;
    }

    public void setOrderAfterTimeT31(String orderAfterTimeT31) {
        this.orderAfterTimeT31 = orderAfterTimeT31;
    }

    public String getTodayOrderAfterTime32() {
        return todayOrderAfterTime32;
    }

    public void setTodayOrderAfterTime32(String todayOrderAfterTime32) {
        this.todayOrderAfterTime32 = todayOrderAfterTime32;
    }

    public String getOrderBeforeDayT32() {
        return orderBeforeDayT32;
    }

    public void setOrderBeforeDayT32(String orderBeforeDayT32) {
        this.orderBeforeDayT32 = orderBeforeDayT32;
    }

    public String getOrderBeforeTimeT32() {
        return orderBeforeTimeT32;
    }

    public void setOrderBeforeTimeT32(String orderBeforeTimeT32) {
        this.orderBeforeTimeT32 = orderBeforeTimeT32;
    }

    public String getYujingTime() {
        return yujingTime;
    }

    public void setYujingTime(String yujingTime) {
        this.yujingTime = yujingTime;
    }
}
