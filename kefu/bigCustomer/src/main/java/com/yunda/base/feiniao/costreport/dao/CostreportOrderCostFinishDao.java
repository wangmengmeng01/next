package com.yunda.base.feiniao.costreport.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostFinishDO;

/**
 * 客户报表订单统计/客户收益报表(完成统计)
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-14092455
 */
@Mapper
public interface CostreportOrderCostFinishDao {

	CostreportOrderCostFinishDO get(Integer recordId);
	
	List<CostreportOrderCostFinishDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(CostreportOrderCostFinishDO costreportOrderCostFinish);
	
	int update(CostreportOrderCostFinishDO costreportOrderCostFinish);
	
	int remove(Integer record_id);
	
	int batchRemove(Integer[] recordIds);
	
	List<Map<String, Object>> searchLbByGsInfo(String orgCode);
	
	List<Map<String, Object>> queryWdByOrgCodeNew(String orgCode);

	List<Map<String, Object>> costreportOrderCostFinishDao(Map<String, Object> map);
	
	List<Map<String, Object>> searchCustBraData(Map<String, Object> map);
	
	List<Map<String, Object>> searchCustomerData(Map<String, Object> map);

	
	List<CostreportOrderCostFinishDO> totalList(Map<String,Object> map);

	List<Map<String, Object>> getProvinceCodeNameMap();

	List<Map<String, Object>> getCityCodeNameMap();

}
