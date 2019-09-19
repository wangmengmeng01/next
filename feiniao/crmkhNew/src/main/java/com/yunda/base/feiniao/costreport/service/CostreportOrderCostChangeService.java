package com.yunda.base.feiniao.costreport.service;

import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostChangeDO;
import com.yunda.base.system.domain.UserDO;

import java.util.List;
import java.util.Map;

/**
 * 客户报表订单统计/客户月结账单
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-11-09140854
 */
public interface CostreportOrderCostChangeService {
	
	CostreportOrderCostChangeDO get(Integer recordId);
	
	List<CostreportOrderCostChangeDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CostreportOrderCostChangeDO costreportOrderCostChange);
	
	int update(CostreportOrderCostChangeDO costreportOrderCostChange);
	
	int remove(Integer recordId);
	
	int batchRemove(Integer[] recordIds);
	
	/**
	 * 中转费计算
	 */
    Double calculateData(CostreportOrderCostChangeDO d,UserDO user);

}
