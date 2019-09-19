package com.yunda.base.bigcustomer.domain;

import lombok.Data;

/**
 * @author beidouxing
 * @create 2019/05/13 10:05
 */
@Data
public class ZhuanFaDO {
    //咨询订单号
    private String orderIds;
    //结单原因
    private String zhuanFaRemark;
    //转发的机构编码
    private String organizationNum;
    //状态
    private String state;
    //处理人
    private String dealCode;
    //处理人名称
    private String dealPersion;
    //转发机构名称
    private String organizationName;
    //转发时间
    private String zhuanFaTime;

}
