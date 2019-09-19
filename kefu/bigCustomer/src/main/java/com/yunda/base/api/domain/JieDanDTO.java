package com.yunda.base.api.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * @program: bigCustomer->JieDanDTO
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-30 17:37
 * @description: DTO
 */
@Data
public class JieDanDTO implements Serializable {

    private static final long serialVersionUID = 8230647306118642652L;
    //咨询订单号
    private String orderIds;
    //结单原因
    private String jieDanCause;
    //责任方
    private String zeRenFang;
    //状态
    private String state;
    //结单原因
    private String jieDanResult;
    //结单时间
    private String jieDanTime;
    //上传地址和文件名
    private List<Map<String, String>> list;
    //上传地址多个,","号分割
    private String fileUrlList;
    //上传地址多个,","号分割
    private String fileNameList;

}
