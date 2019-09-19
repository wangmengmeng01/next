package com.yunda.base.api.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yunda.base.api.domain.JieDanDTO;
import com.yunda.base.api.domain.OrderDTO;
import com.yunda.base.api.domain.RoleDTO;
import com.yunda.base.bigcustomer.domain.OperateDO;
import com.yunda.base.bigcustomer.domain.OrderDO;
import com.yunda.base.bigcustomer.domain.OrganizationManageDO;
import com.yunda.base.system.domain.UserDO;

/**
 * @program: bigCustomer->OrderAppDao
 * @author: luzhiwei
 * @email: luzhiwei8794@yundasys.com
 * @create: 2019-07-22 13:30
 * @description: App咨询单数据层
 */
@Mapper
public interface OrderAppDao {

    /**
     * 获取咨询单详情
     * @param orderId
     * @return
     */
    OrderDO getOrderDetailByOrderId(String orderId);

    /**
     * 获取咨询单实例
     * @param orderId
     * @return
     */
    OrderDO getOrderByOrderId(@Param("orderId") String orderId);

    /**
     * 咨询单列表
     *
     * @param orderDTO
     * @return
     */
    List<OrderDO> list(OrderDTO orderDTO);

    /**
     * 获取咨询类型接口列表
     *
     * @return
     */
    List<String> getAllConsultType();

    /**
     * 发起咨询单保存
     *
     * @param order
     * @return
     */
    int save(OrderDO order);

    /**
     * 根据用户名获取机构代码
     *
     * @param username
     * @return
     */
    String getOrgCodeByUserName(String username);

    /**
     * 保存操作信息
     *
     * @param operateDO
     * @return
     */
    int saveOperateInfo(OperateDO operateDO);

    /**
     * @param custId
     * @return
     */
    OrderDO getCustomerName(String custId);

    List<String> getUserNameListByOrgCode(String orgCode);

    int count(OrderDTO orderDTO);

    int shenLing(@Param("orderId") String orderId, @Param("userName") String userName, @Param("name") String name, @Param("state") String state);

    String getRoleNameByUserName(String username);

    List<String> getAllUserInfoByOrgCode(@Param("orgCode") String orgCode, @Param("name") String name);

    UserDO getUserByUserName(String dealCode);

    List<OperateDO> getListByOrderId(OrderDTO orderDTO);

    int countByOrderId(String orderId);

    List<OrderDO> getListByWaybillNum(OrderDTO orderDTO);

    int countByWaybillNum(String waybillNum);

    void updateOrderByDeal(JieDanDTO jieDanDTO);

    List<Long> findOrderMenuByParam(@Param("userId") Long userId, @Param("parentId") Long parentId, @Param("subId") Long subId);

    List<Long> findOrderMenuByUserIdAndParentId(@Param("userId") Long userId, @Param("parentId") Long parentId);

    RoleDTO getRoleByUserId(@Param("userId") Long userId);

    List<OrganizationManageDO> findAllOrgInfo(@Param("organizationNum") String organizationNum);

    void zhuanFa(@Param("orderId") String orderId, @Param("organizationNum") String organizationNum, @Param("state") String state);

}
