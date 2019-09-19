package com.yunda.base.feiniao.market.dao;

import com.yunda.base.feiniao.market.domain.MarketKeyProvinceReportDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 省同行间市场份额分析对比表
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-10-09105817
 */
@Mapper
public interface MarketKeyProvinceReportDao {

	
	List<MarketKeyProvinceReportDO> list(Map<String,Object> map);
	
	int approveCount(Map<String,Object> map);
	
	int dateCount(Map<String,Object> map);
	
	List<MarketKeyProvinceReportDO> allDataList(Map<String,Object> map);
	
	List<MarketKeyProvinceReportDO> bigareaDataList(Map<String,Object> map);
	
	List<MarketKeyProvinceReportDO> provinceDataList(Map<String,Object> map);
}
