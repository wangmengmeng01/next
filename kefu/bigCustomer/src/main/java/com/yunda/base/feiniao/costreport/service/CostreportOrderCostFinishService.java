package com.yunda.base.feiniao.costreport.service;

import java.util.List;
import java.util.Map;

import com.yunda.base.feiniao.costreport.bo.Bo_costreportOrderCostFinish_two;
import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostFinishDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportOrderCostFinishDO;
import com.yunda.base.system.domain.UserDO;

/**
 * 客户报表订单统计/客户收益报表(完成统计)
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-14092455
 */
public interface CostreportOrderCostFinishService {
	
	CostreportOrderCostFinishDO get(Integer recordId,String dsId);
	
	List<CostreportOrderCostFinishDO> list(Map<String, Object> map,UserDO user,String dsId);
	
	List<CostreportOrderCostFinishDO> list(Bo_costreportOrderCostFinish_two boCostreportOrderCostFinishTwo,UserDO user,String dsId);
	
	int count(Map<String, Object> map,String dsId);
	int count(Bo_costreportOrderCostFinish_two boCostreportOrderCostFinishTwo,UserDO user,String dsId);
	
	int save(CostreportOrderCostFinishDO costreportOrderCostFinish,String dsId);
	
	int update(CostreportOrderCostFinishDO costreportOrderCostFinish,String dsId);
	
	int remove(Integer recordId,String dsId);
	
	int batchRemove(Integer[] recordIds,String dsId);
	
	List<Map<String, Object>> searchCustomerData(Map<String, Object> map,UserDO user,String dsId);

    List<Map<String, Object>> searchCustomerBraData(Map<String, Object> map,UserDO user,String dsId);

    List<Map<String, Object>> getProvinceCodeNameMap();

    List<Map<String, Object>> getCityCodeNameMap();
    
	List<ExportCostreportOrderCostFinishDO> filterCustData(
            List<CostreportOrderCostFinishDO> reportTotaldata, String dsId);
	
	List<ExportCostreportOrderCostFinishDO> filterData(
			List<CostreportOrderCostFinishDO> costreportOrderCostFinishList);
}
