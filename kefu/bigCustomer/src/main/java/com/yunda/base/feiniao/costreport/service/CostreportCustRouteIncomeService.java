package com.yunda.base.feiniao.costreport.service;

import java.util.List;

import com.yunda.base.feiniao.costreport.bo.Bo_costreportCustRouteIncome_list;
import com.yunda.base.feiniao.costreport.domain.CostreportCustRouteIncomeDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportCRIncomeDetailDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportCustRouteIncomeDO;
import com.yunda.base.system.domain.ProvinceDO;

/**
 * 客户报表订单统计/客户线路收入
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-20160543
 */
public interface CostreportCustRouteIncomeService {
	
	List<CostreportCustRouteIncomeDO> getIncomelist(Bo_costreportCustRouteIncome_list boCostreportCustRouteIncomeList,String dsId) throws Exception;
	
	int getIncomeCount(Bo_costreportCustRouteIncome_list boCostreportCustRouteIncomeList,String dsId);
	
	List<ProvinceDO> getAllProvinces() throws Exception;
	
	List<CostreportCustRouteIncomeDO> getIncomeDetailList(Bo_costreportCustRouteIncome_list boCostreportCustRouteIncomeList,String dsId) throws Exception;
	
	List<ExportCostreportCustRouteIncomeDO> filterData(
			List<CostreportCustRouteIncomeDO> costreportCustRouteIncomeData);
	
	List<ExportCostreportCRIncomeDetailDO> filterData2(
			List<CostreportCustRouteIncomeDO> costreportCustRouteIncomeData);
	
	int getIncomeDetailCount(Bo_costreportCustRouteIncome_list boCostreportCustRouteIncomeList,String dsId);
}
