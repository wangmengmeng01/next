package com.yunda.base.bigcustomer.domain;

import com.github.crab2died.annotation.ExcelField;

import lombok.Data;

@Data
public class OrderTemplateDO {

    //序号
    private Integer id;
    //咨询订单号
    private String orderId;
    //咨询来电电话
    @ExcelField(title = "咨询来电电话", order = 1)
    private String phone;
    //运单号
    @ExcelField(title = "运单号", order = 2)
    private String waybillNum;
    //物流订单号
    @ExcelField(title = "物流订单号", order = 3)
    private String logisticOrderNum;
    //客户名称
    @ExcelField(title = "客户", order = 4)
    private String merchant;
    //咨询类型
    @ExcelField(title = "咨询类型", order = 5)
    private String consultType;
    //优先级
    @ExcelField(title = "优先级", order = 6)
    private String priority;
    //责任机构
    @ExcelField(title = "责任机构编码", order = 7)
    private String organizationNum;
    //处理机构名称
    private String organizationName;
    //问题描述
    @ExcelField(title = "问题描述", order = 8)
    private String problemDescription;
    //发件人姓名
    private String sendName;
    //发件人电话
    private String sendPhone;
    //发件地址
    private String sendAddress;
    //收件人姓名
    private String receiverName;
    //收件人电话
    private String receiverPhone;
    //收件地址
    private String receiverAddress;
    //分配给的客户名称
    private String customerName;
    //分配给的客户编码
    private String customerCode;
    //咨询时间
    private String consultTime;
    //咨询来源
    private String consultSource;
    //发起人账号
    private String faqiCode;
    //发起人姓名
    private String faqiName;
    //发起机构编码
    private String faqiOrgCode;
    //发起人机构名称
    private String faqiOrgName;
    //处理人账号
    private String dealCode;
    //处理人
    private String dealPersion;
    //状态
    private String state;
    //结单结果
    private String jieDanResult;
    //时效预警的状态
    private String shiXiaoState;
    //最近处理时间
    private String dealTime;
    //最近处理描述
    private String dealContent;
    //剩余时间
    private String shengYuTime;
    //vip编码
    private String vipCode;
    //网点编码
    private String branchCode;
    //网点名称
    private String branchName;
    //-------------------------------------------------------
    //配置表对应的字段
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

}
