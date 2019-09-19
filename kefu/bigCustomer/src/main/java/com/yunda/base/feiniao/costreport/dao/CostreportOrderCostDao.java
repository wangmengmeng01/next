package com.yunda.base.feiniao.costreport.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostDO;

/**
 * 客户报表订单统计/客户单票成本
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-14092504
 */
@Mapper
public interface CostreportOrderCostDao {

	CostreportOrderCostDO get(Integer recordId);
	
	List<CostreportOrderCostDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(CostreportOrderCostDO costreportOrderCost);
	
	int update(CostreportOrderCostDO costreportOrderCost);
	
	int remove(Integer record_id);
	
	int batchRemove(Integer[] recordIds);
}
