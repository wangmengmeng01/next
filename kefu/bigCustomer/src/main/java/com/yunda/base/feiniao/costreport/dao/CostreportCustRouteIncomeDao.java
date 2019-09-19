package com.yunda.base.feiniao.costreport.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.feiniao.costreport.domain.CostreportCustRouteIncomeDO;

/**
 * 客户报表订单统计/客户线路收入
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-09-20160543
 */
@Mapper
public interface CostreportCustRouteIncomeDao {

	List<CostreportCustRouteIncomeDO> getIncomelist(Map<String,Object> map);
	
	int getIncomeCount(Map<String,Object> map);
	
	List<CostreportCustRouteIncomeDO> getIncomeDetailList(Map<String,Object> map);
	
	int getIncomeDetailCount(Map<String,Object> map);
	
}
