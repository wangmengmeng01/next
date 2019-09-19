package com.yunda.base.feiniao.market.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.yunda.base.feiniao.market.domain.ExportMarketOccupancyReportDO;
import com.yunda.base.feiniao.market.domain.MarketOccupancyReportDO;
import com.yunda.base.system.domain.UserDO;

/**
 * 市场占有率数据上报
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-10-12103231
 */
public interface MarketOccupancyReportService {
	
	MarketOccupancyReportDO get(Integer recordId);
	
	List<MarketOccupancyReportDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(MarketOccupancyReportDO marketOccupancyReport);
	
	int update(MarketOccupancyReportDO marketOccupancyReport);
	
	int remove(Integer recordId);
	
	int batchRemove(Integer[] recordIds);
	
	List<Map<String, Object>> searchShengData();

	List<MarketOccupancyReportDO> listSearch(Map<String,Object> map);

	int countSearch(Map<String, Object> map);
	
	// 暂存数据
	void cacheSave(HttpServletRequest request, MarketOccupancyReportDO cycleDetailDO,UserDO loginUser);

	int checkResult(HashMap<String,Object> map);
	
	void upDataResult(HttpServletRequest request, MarketOccupancyReportDO occupancyReportDO, UserDO loginUser);
	
	String auditData(MarketOccupancyReportDO occupancyReportDO);
	
	List<ExportMarketOccupancyReportDO> filterData(
            List<MarketOccupancyReportDO> reportTotaldata);
}
