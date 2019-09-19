package com.yunda.base.bigcustomer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

/**
 * @program: bigCustomer->ConsultStatementTypeDao
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-25 11:30
 * @description: 咨询类型和结单结果关联接口
 */
@Mapper
public interface ConsultStatementTypeDao {

    List<String> listByConsultType(String consultType);
}
