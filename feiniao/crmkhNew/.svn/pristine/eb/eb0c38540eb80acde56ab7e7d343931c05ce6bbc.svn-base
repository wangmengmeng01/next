package com.yunda.base.feiniao.report.service;

import com.yunda.base.feiniao.report.bo.Bo_reportCustRewardDetails_list;
import com.yunda.base.feiniao.report.domain.ExportBranchCRDDataDO;
import com.yunda.base.feiniao.report.domain.ExportCRDDataDO;
import com.yunda.base.feiniao.report.domain.ReportCustRewardDetailsDO;
import com.yunda.base.system.domain.UserDO;

import java.util.List;
import java.util.Map;

/**
 * 客户奖励明细表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-17153137
 */
public interface ReportCustRewardDetailsService {
	
	
	List<ReportCustRewardDetailsDO> getCustRewardDetailsList(Bo_reportCustRewardDetails_list boReportCustRewardDetailsList, UserDO loginUser) throws Exception;
	
	int getCustRewardDetailsCount(Bo_reportCustRewardDetails_list boReportCustRewardDetailsList,
			UserDO loginUser) throws Exception;
	List<ReportCustRewardDetailsDO> getCustBraData(String queryInfo) throws Exception;
	
	List<ExportCRDDataDO> filterData(
			List<ReportCustRewardDetailsDO> CRData);
	
	List<ExportBranchCRDDataDO> filterBranchData(
			List<ReportCustRewardDetailsDO> CRData);
	
	List<Map<String, Object>> searchCustomerBraData(Map<String, Object> map,UserDO user);

}
