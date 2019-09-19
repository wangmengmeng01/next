package com.yunda.base.bigcustomer.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.bigcustomer.dao.ConsultStatementTypeDao;
import com.yunda.base.bigcustomer.service.ConsultStatementTypeService;

/**
 * @program: bigCustomer->ConsultStatementTypeServiceImpl
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-25 11:32
 * @description: 咨询类型和结单结果服务实现
 */
@Service
public class ConsultStatementTypeServiceImpl implements ConsultStatementTypeService {

    @Autowired
    private ConsultStatementTypeDao consultStatementTypeDao;

    @Override
    public List<String> listByConsultType(String consultType) {
        return consultStatementTypeDao.listByConsultType(consultType);
    }
}
