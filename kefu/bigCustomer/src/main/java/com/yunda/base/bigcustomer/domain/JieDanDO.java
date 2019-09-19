package com.yunda.base.bigcustomer.domain;

import java.util.List;

import lombok.Data;

/**
 * @author beidouxing
 * @create 2019/05/13 10:05
 */
@Data
public class JieDanDO {
    //咨询订单号
    private String orderIds;
    //结单原因
    private String jieDanCause;
    //责任方
    private String zeRenFang;
    //上传文件地址
    private String uploadPath;
    //文件名
    private String fileName;
    //状态
    private String state;
    //结单结果
    private String jieDanResult;
    //结单时间
    private String jieDanTime;
    //多个文件
    private List<ConsultFileDO> fileDOS;

}