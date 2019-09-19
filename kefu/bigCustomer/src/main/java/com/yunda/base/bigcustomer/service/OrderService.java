package com.yunda.base.bigcustomer.service;

import java.util.List;
import java.util.Map;

import com.yunda.base.bigcustomer.domain.JieDanDO;
import com.yunda.base.bigcustomer.domain.OperateDO;
import com.yunda.base.bigcustomer.domain.OrderDO;
import com.yunda.base.bigcustomer.domain.OrderTemplateDO;
import com.yunda.base.bigcustomer.domain.ZhuanFaDO;
import com.yunda.base.system.domain.UserDO;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-04-25154305
 */
public interface OrderService {
	
	OrderDO get(Integer id);
	
	List<OrderDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);

	OrderDO getOrder(OrderDO order);
	
	int update(OrderDO order);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

    int shenLing(String[] orderIds,String userName,String name,String state);

    int updateByOrderIds(JieDanDO jieDanDO);

	OrderDO getByOrderId(String orderId);

	List<OperateDO> getListByOrderId(Map<String, Object> map);

	int countByOrderId(Map<String, Object> map);

	List<OperateDO> getListByWaybillNum(Map<String, Object> map);

	int countByWaybillNum(Map<String, Object> map);

    List getAllConsultype();

    List<String> getAllUserInfoByOrgCode(String orgCode);

	UserDO getUserByUserName(String dealCode);

	int saveOperateInfo(OperateDO operateDO);

	int countOrder(String monthBegin,String monthEnd);

	int countByState(String monthBegin, String monthEnd, String state);

	String getOrgCodeByUserName(String username);

    List<String> getUserNameListByOrgCode(String orgCode);

	String getRoleNameByUserName(String username);

	int countByConsultType(String consultType);

	void saveList(List<OrderTemplateDO> list);

	int save(OrderDO orderDO);

	int updateOrganizationNumByOrderIds(ZhuanFaDO zhuanFaDO);

	OrderDO checkOrder(OrderDO order);
}
