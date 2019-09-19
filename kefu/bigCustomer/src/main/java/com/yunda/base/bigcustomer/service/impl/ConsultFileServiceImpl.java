package com.yunda.base.bigcustomer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.bigcustomer.dao.ConsultFileDao;
import com.yunda.base.bigcustomer.domain.ConsultFileDO;
import com.yunda.base.bigcustomer.service.ConsultFileService;

/**
 * @program: bigCustomer->ConsultFileServiceImpl
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-24 18:46
 * @description: 咨询单文件操作服务
 */
@Service
public class ConsultFileServiceImpl implements ConsultFileService {

    @Autowired
    private ConsultFileDao consultFileDao;

    @Override
    public void saveBatch(List<ConsultFileDO> list) {
        consultFileDao.saveBatch(list);
    }

    @Override
    public List<ConsultFileDO> getListByOrderIdAndTime(String orderId, String time) {
        return consultFileDao.getListByOrderIdAndTime(orderId,time);
    }

    @Override
    public List<ConsultFileDO> findByOrderIdAndTime(String uploadPathPrefix, String orderId, String time) {
        return consultFileDao.findByOrderIdAndTime(uploadPathPrefix, orderId, time);
    }
}
