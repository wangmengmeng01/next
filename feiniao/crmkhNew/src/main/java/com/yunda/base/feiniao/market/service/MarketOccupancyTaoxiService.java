package com.yunda.base.feiniao.market.service;

import com.yunda.base.feiniao.market.bo.Bo_marketOccupancyTaoxi;
import com.yunda.base.feiniao.market.domain.MarketOccupancyTaoxiDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-07150734
 */
public interface MarketOccupancyTaoxiService {
	
	MarketOccupancyTaoxiDO get(Long id);
	
	List<MarketOccupancyTaoxiDO> list(Bo_marketOccupancyTaoxi boInterface);
	
	int count(Bo_marketOccupancyTaoxi boInterface);
	
	int save(MarketOccupancyTaoxiDO marketOccupancyTaoxi);
	
	int update(MarketOccupancyTaoxiDO marketOccupancyTaoxi);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
