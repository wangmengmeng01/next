package com.yunda.base.feiniao.costreport.service;

import com.yunda.base.feiniao.costreport.bo.Bo_costreportCustCostExt;
import com.yunda.base.feiniao.costreport.domain.CostreportCustCostExtDO;
import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportCustCostExtDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportOrderCostDO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 客户报表订单统计/客户拓展支出
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-13145432
 */
public interface CostreportCustCostExtService {
	
	CostreportCustCostExtDO get(Integer recordId,String dsId);
	
	List<CostreportOrderCostDO> list(Map<String, Object> map,String dsId);
	
	List<CostreportOrderCostDO> list(Bo_costreportCustCostExt boCostreportCustCostExt,String dsId);
	
	int count(Map<String, Object> map,String dsId);
	
	int count(Bo_costreportCustCostExt boCostreportCustCostExt,String dsId);
	
	int save(CostreportCustCostExtDO costreportCustCostExt,String dsId);
	
	int update(CostreportCustCostExtDO costreportCustCostExt,String dsId);
	
	int remove(Integer recordId,String dsId);
	
	int batchRemove(Integer[] recordIds,String dsId);
	
	void groupCostExtData(Date date, String dsId);
	
	List<ExportCostreportOrderCostDO> filterCustData(
            List<CostreportOrderCostDO> reportTotaldata, String dsId);
	
	List<ExportCostreportCustCostExtDO> filterData(
			List<CostreportOrderCostDO> costreportCustCostExtList);
}
