package com.yunda.base.api.service;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.api.domain.JieDanDTO;
import com.yunda.base.api.domain.OrderDTO;
import com.yunda.base.api.domain.RoleDTO;
import com.yunda.base.bigcustomer.domain.OperateDO;
import com.yunda.base.bigcustomer.domain.OrderDO;
import com.yunda.base.bigcustomer.domain.OrganizationManageDO;
import com.yunda.base.system.domain.UserDO;

/**
 * @program: bigCustomer->OrderAppService
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-22 13:32
 * @description: App咨询单服务层
 */
@Mapper
public interface OrderAppService {

    OrderDO getOrderDetailByOrderId(String orderId);

    OrderDO getOrderByOrderId(String orderId);

    List<String> getAllConsultType();

    List<OrderDO> list(OrderDTO orderDTO);

    int save(OrderDO order, UserDO userDO);

    OrderDO getOrder(OrderDO order, UserDO userDO);

    String getOrgCodeByUserName(String username);

    int saveOperateInfo(OperateDO operateDO);

    List<String> getUserNameListByOrgCode(String orgCode);

    int count(OrderDTO orderDTO);

    int shenLing(String orderIds, String userName, String name, String state);

    String getRoleNameByUserName(String username);

    List<String> getAllUserInfoByOrgCode(String orgCode, String name);

    UserDO getUserByUserName(String dealCode);

    List<OperateDO> getListByOrderId(OrderDTO orderDTO);

    int countByOrderId(String orderId);

    List<OrderDO> getListByWaybillNum(OrderDTO orderDTO);

    int countByWaybillNum(String waybillNum);

    void updateOrderByDeal(JieDanDTO jieDanDTO);

    List<Long> findOrderMenuByParam(Long userId, Long parentId, Long subId);

    List<Long> findOrderMenuByUserIdAndParentId(Long userId, Long parentId);

    RoleDTO getRoleByUserId(Long userId);

    List<OrganizationManageDO> findAllOrgInfo(String organizationNum);

    void zhuanFa(String orderId, String organizationNum, String state);

}
