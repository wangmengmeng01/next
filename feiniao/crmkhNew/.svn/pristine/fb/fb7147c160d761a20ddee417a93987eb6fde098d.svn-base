package com.yunda.base.feiniao.crmapp.costReport.dao;

import com.yunda.base.feiniao.crmapp.costReport.domain.CostreportLiangBenLiDO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 客户报表订单统计/客户支出报表(完成统计)
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-07-31135523
 */
@Mapper
public interface CostreportLiangBenLiDao {

	
	List<CostreportLiangBenLiDO> getCustSingleCost(Map<String,Object> map);
	
	List<CostreportLiangBenLiDO> getCustSingleOrderDetail(Map<String,Object> map);
	
	List<CostreportLiangBenLiDO> getMonthCostAndIncome(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
}
