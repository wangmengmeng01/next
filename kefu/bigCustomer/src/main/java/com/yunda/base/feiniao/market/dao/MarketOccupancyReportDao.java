package com.yunda.base.feiniao.market.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yunda.base.feiniao.market.domain.MarketOccupancyReportDO;

/**
 * 市场占有率数据上报
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-10-12103231
 */
@Mapper
public interface MarketOccupancyReportDao {

	MarketOccupancyReportDO get(Integer recordId);
	
	List<MarketOccupancyReportDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(MarketOccupancyReportDO marketOccupancyReport);
	
	int update(MarketOccupancyReportDO marketOccupancyReport);
	
	int remove(Integer record_id);
	
	int batchRemove(Integer[] recordIds);
	
	int saveMarketData(@Param(value="year") String year,@Param(value="month") String month);

	int countMarketData(@Param(value="year") String year,@Param(value="month") String month);

	Double provinceSum(@Param(value="year") String year,@Param(value="month") String month);

	int updateMarketData(HashMap<String,String> map);

	List<HashMap<String,String>> searchProvinceID (HashMap<String,String> map);
	
	int updateJiangSu(HashMap<String,String> map);
	
	int updateZheJiang(HashMap<String,String> map);

	int updateOther(HashMap<String,String> map);
	
	List<Map<String, Object>> searchShengData();

	List<MarketOccupancyReportDO> listSearch(Map<String,Object> map);

	int countSearch(Map<String,Object> map);

	int checkResult(HashMap<String,Object> map);
	
	void upDataResult(MarketOccupancyReportDO occupancyReportDO);
	
	void upDataShangBao(MarketOccupancyReportDO occupancyReportDO);

	String auditData(MarketOccupancyReportDO occupancyReportDO);

	List<MarketOccupancyReportDO> searchReportStatus(MarketOccupancyReportDO occupancyReportDO);

	void updateMarketStatus(MarketOccupancyReportDO occupancyReportDO);

}
