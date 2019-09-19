package com.yunda.base.bigcustomer.service;

import java.util.List;

import com.yunda.base.bigcustomer.domain.ConsultFileDO;

/**
 * @program: bigCustomer->ConsultFileService
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-24 18:47
 * @description: 咨询单文件服务接口
 */
public interface ConsultFileService {

    void saveBatch(List<ConsultFileDO> list);

    List<ConsultFileDO> getListByOrderIdAndTime(String orderId, String time);

    List<ConsultFileDO> findByOrderIdAndTime(String uploadPathPrefix, String orderId, String time);
}
