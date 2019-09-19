package com.yunda.base.feiniao.crmapp.costReport.service;

import java.util.List;

import com.yunda.base.feiniao.crmapp.costReport.bo.Bo_custSingleCost;
import com.yunda.base.feiniao.crmapp.costReport.domain.CostreportLiangBenLiDO;

public interface CostreportLiangBenLiService {
	
	
	List<CostreportLiangBenLiDO> getCustSingleCost(Bo_custSingleCost boCustSingleCost);
	
	List<CostreportLiangBenLiDO> getCustSingleOrderDetail(Bo_custSingleCost boCustSingleCost);
	
	List<CostreportLiangBenLiDO> getMonthCostAndIncome(Bo_custSingleCost boCustSingleCost);
	
	int count(Bo_custSingleCost boCustSingleCost);
	
}
