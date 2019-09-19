package com.yunda.base.bigcustomer.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yunda.base.bigcustomer.domain.ConsultFileDO;

/**
 * @program: bigCustomer->ConsultFileDao
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-24 18:44
 * @description: 咨询单文件数据层
 */
@Mapper
public interface ConsultFileDao {

    void saveBatch(List<ConsultFileDO> list);

    List<ConsultFileDO> getListByOrderIdAndTime(@Param("orderId") String orderId, @Param("time") String time);

    List<ConsultFileDO> findByOrderIdAndTime(@Param("uploadPathPrefix") String uploadPathPrefix, @Param("orderId") String orderId, @Param("time") String time);
}
