package com.yunda.base.bigcustomer.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yunda.base.bigcustomer.domain.JieDanDO;
import com.yunda.base.bigcustomer.domain.OperateDO;
import com.yunda.base.bigcustomer.domain.OrderDO;
import com.yunda.base.bigcustomer.domain.OrderTemplateDO;
import com.yunda.base.bigcustomer.domain.ZhuanFaDO;
import com.yunda.base.system.domain.UserDO;


/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-04-25154305
 */
@Mapper
public interface OrderDao {

	OrderDO get(Integer id);
	
	List<OrderDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(OrderDO order);
	
	int update(OrderDO order);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

    int getMaxId();

    int shenLing(@Param("orderIds") String[] orderIds,@Param("userName") String userName,@Param("name") String name,@Param("state") String state);

    int updateByOrderId(JieDanDO jieDanDO);

	int updateByOrderIds(JieDanDO jieDanDO);

	OrderDO getByOrderId(String orderId);

	List<OperateDO> getListByOrderId(Map<String, Object> map);

	int countByOrderId(Map<String, Object> map);

	List<OperateDO> getListByWaybillNum(Map<String,Object> map);

	int countByWaybillNum(Map<String,Object> map);

    List getAllConsultype();

    List<String> getAllUserInfoByOrgCode(@Param("orgCode") String orgCode);

	UserDO getUserByUserName(@Param("dealCode") String dealCode);

	int saveOperateInfo(OperateDO operateDO);

	int countOrder(@Param("monthBegin") String monthBegin,@Param("monthEnd") String monthEnd);

	int countByState(@Param("monthBegin") String monthBegin,@Param("monthEnd") String monthEnd,@Param("state") String state);

	OrderDO getCustomerName(String res);

	String getOrgCodeByUserName(String username);

    List<String> getUserNameListByOrgCode(String orgCode);

	String getRoleNameByUserName(String username);

	int countByConsultType(String consultType);

	void saveOrderList(List<OrderTemplateDO> orderList);

	void saveOperateList(List<OperateDO> operateList);

	int updateOrganizationNumByOrderIds(ZhuanFaDO zhuanFaDO);

	int updateOrganizationNumByOrderId(ZhuanFaDO zhuanFaDO);

    OrderDO checkOrder(OrderDO order);
}
