package com.yunda.base.feiniao.market.service;

import com.yunda.base.feiniao.market.bo.Bo_marketKeyCity;
import com.yunda.base.feiniao.market.domain.ExportMarketKeyCityReportDO;
import com.yunda.base.feiniao.market.domain.MarketKeyCitiesReportDO;
import com.yunda.base.system.domain.UserDO;

import java.util.List;

/**
 * 市场占有率数据上报
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-10-18132725
 */
public interface MarketKeyCitiesReportService {
	
	List<MarketKeyCitiesReportDO> list(Bo_marketKeyCity boMarketKeyCity,UserDO loginUser);
	
	List<ExportMarketKeyCityReportDO> filterData(
			List<MarketKeyCitiesReportDO> marketKeyCityReportList);
}
