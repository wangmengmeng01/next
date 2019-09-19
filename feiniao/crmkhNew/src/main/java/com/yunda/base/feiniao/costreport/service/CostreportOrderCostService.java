package com.yunda.base.feiniao.costreport.service;

import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostDO;

import java.util.List;
import java.util.Map;

/**
 * 客户报表订单统计/客户单票成本
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-14092504
 */
public interface CostreportOrderCostService {
	
	CostreportOrderCostDO get(Integer recordId,String dsId);
	
	List<CostreportOrderCostDO> list(Map<String, Object> map,String dsId);
	
	int count(Map<String, Object> map,String dsId);
	
	int save(CostreportOrderCostDO costreportOrderCost,String dsId);
	
	int update(CostreportOrderCostDO costreportOrderCost,String dsId);
	
	int remove(Integer recordId,String dsId);
	
	int batchRemove(Integer[] recordIds,String dsId);
}
