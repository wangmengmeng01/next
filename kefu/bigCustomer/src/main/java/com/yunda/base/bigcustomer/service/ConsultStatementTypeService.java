package com.yunda.base.bigcustomer.service;

import java.util.List;

/**
 * @program: bigCustomer->ConsultStatementTypeService
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-25 11:31
 * @description: 咨询类型和结单结果服务
 */
public interface ConsultStatementTypeService {

    List<String> listByConsultType(String consultType);
}
