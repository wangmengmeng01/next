package com.yunda.base.feiniao.report.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.feiniao.report.domain.ReportCustRewardDetailsDO;

/**
 * 客户奖励明细表
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-17153137
 */
@Mapper
public interface ReportCustRewardDetailsDao {

	
	List<ReportCustRewardDetailsDO> list(Map<String,Object> map);
	
	
	
	
	
	
	List<ReportCustRewardDetailsDO> getCustRewardDetailsList(Map<String,Object> map);
	
	List<ReportCustRewardDetailsDO> getBranchCustRewardDetailsList(Map<String,Object> map);
	
	List<ReportCustRewardDetailsDO> getCustRewardDetailsListNew(Map<String,Object> map);
	
	List<ReportCustRewardDetailsDO> getBranchCustRewardDetailsListNew(Map<String,Object> map);
	
	int getCustRewardDetailsCount(Map<String,Object> map);
	
	int getCustRewardDetailsCountNew(Map<String,Object> map);
	
	List<ReportCustRewardDetailsDO> getCustBraData(String queryInfo);
	
	List<Map<String, Object>> searchLbByGsInfo(String orgCode);
	
	List<Map<String, Object>> searchCustBraData(Map<String, Object> map);
}
