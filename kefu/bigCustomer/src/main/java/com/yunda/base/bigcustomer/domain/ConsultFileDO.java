package com.yunda.base.bigcustomer.domain;

import java.io.Serializable;

import lombok.Data;

/**
 * @program: bigCustomer->ConsultFileDO
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-24 18:15
 * @description: 咨询订单文件封装类
 */
@Data
public class ConsultFileDO implements Serializable {

    private static final long serialVersionUID = 3544525961965181254L;

    //序号
    private Integer id;
    //咨询订单号
    private String orderId;
    //文件类型：0-图片,1-文件,2-其他
    private String fileType;
    //文件名
    private String fileName;
    //文件名后缀
    private String fileSuffix;
    //文件url
    private String uploadPath;
    //状态：0-不可用,1-可用
    private String status;
    //创建时间
    private String createTime;
    //更新时间
    private String updatedTime;
    //操作人
    private String operateCode;

}
