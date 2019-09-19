package com.yunda.base.feiniao.market.dao;

import com.yunda.base.feiniao.market.domain.MarketKeyCitiesReportDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 重点城市同行间市场份额对比表
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-10-18132725
 */
@Mapper
public interface MarketKeyCitiesReportDao {

	
	List<MarketKeyCitiesReportDO> list(Map<String,Object> map);
	
	int approveCount(Map<String,Object> map);
	
	int dateCount(Map<String,Object> map);
	
	List<MarketKeyCitiesReportDO> allDataList(Map<String,Object> map);
	
	List<MarketKeyCitiesReportDO> bigareaDataList(Map<String,Object> map);
	
	List<MarketKeyCitiesReportDO> provinceDataList(Map<String,Object> map);
	
	List<MarketKeyCitiesReportDO> cityDataList(Map<String,Object> map);
}
