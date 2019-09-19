package com.yunda.base.feiniao.costreport.service;

import com.yunda.base.feiniao.costreport.bo.Bo_CostreportCustCostFinish_two;
import com.yunda.base.feiniao.costreport.domain.CostreportCustCostFinishDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportCustCostFinishDO;

import java.util.List;
import java.util.Map;

/**
 * 客户报表订单统计/客户支出报表(完成统计)
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-13145339
 */
public interface CostreportCustCostFinishService {
	
	CostreportCustCostFinishDO get(Integer recordId,String dsId);
	
	List<CostreportCustCostFinishDO> list(Map<String, Object> map,String dsId);
	List<CostreportCustCostFinishDO> list(Bo_CostreportCustCostFinish_two boCostreportCustCostFinishTwo,String dsId);
	
	int count(Map<String, Object> map,String dsId);
	int count(Bo_CostreportCustCostFinish_two boCostreportCustCostFinishTwo,String dsId);
	
	int save(CostreportCustCostFinishDO costreportCustCostFinish,String dsId);
	
	int update(CostreportCustCostFinishDO costreportCustCostFinish,String dsId);
	
	int remove(Integer recordId,String dsId);
	
	int batchRemove(Integer[] recordIds,String dsId);
	
	List<ExportCostreportCustCostFinishDO> filterData(
			List<CostreportCustCostFinishDO> costreportCustCostFinishList);
	
	List<ExportCostreportCustCostFinishDO> filterCustData(
            List<CostreportCustCostFinishDO> reportTotaldata, String dsId);
}
