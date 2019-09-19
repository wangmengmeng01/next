package com.yunda.base.feiniao.market.dao;

import com.yunda.base.feiniao.market.domain.MarketOccupancyTaoxiDO;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-07150734
 */
@Mapper
public interface MarketOccupancyTaoxiDao {

	MarketOccupancyTaoxiDO get(Long id);
	
	List<MarketOccupancyTaoxiDO> list(Map<String,Object> map);
	//List<MarketOccupancyTaoxiDO> listWeek(Map<String,Object> map);
	MarketOccupancyTaoxiDO listRangeSum(Map<String,Object> map);
	int count(Map<String,Object> map);
	
	int save(MarketOccupancyTaoxiDO marketOccupancyTaoxi);
	
	int update(MarketOccupancyTaoxiDO marketOccupancyTaoxi);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	//int saveByDate(String quDate);
	int removeByDate(String quDate);
}
