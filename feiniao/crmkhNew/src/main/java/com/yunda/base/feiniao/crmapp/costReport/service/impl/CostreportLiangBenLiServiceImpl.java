package com.yunda.base.feiniao.crmapp.costReport.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunda.base.feiniao.crmapp.costReport.bo.Bo_custSingleCost;
import com.yunda.base.feiniao.crmapp.costReport.dao.CostreportLiangBenLiDao;
import com.yunda.base.feiniao.crmapp.costReport.domain.CostreportLiangBenLiDO;
import com.yunda.base.feiniao.crmapp.costReport.service.CostreportLiangBenLiService;
import com.yunda.base.feiniao.report.utils.DateUtils;

@Service
public class CostreportLiangBenLiServiceImpl implements CostreportLiangBenLiService {
	@Autowired
	private CostreportLiangBenLiDao costreportCustCostFinishDao;

	
	@Override
	public List<CostreportLiangBenLiDO> getCustSingleCost(Bo_custSingleCost boCustSingleCost){
		NumberFormat numberFormat = NumberFormat.getInstance();   
		numberFormat.setMaximumFractionDigits(2);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("branchCode", boCustSingleCost.getBranchCode());
		map.put("accountDt", boCustSingleCost.getAccountDt());
		map.put("customerId", boCustSingleCost.getCustomerId());
		List<CostreportLiangBenLiDO> costData = costreportCustCostFinishDao.getCustSingleCost(map);
		Double totalCost =  null;
		for(CostreportLiangBenLiDO data : costData){
			if(data.getOrderSum() > 0 && null != data.getOrderSum()){
				totalCost = (data.getTsfFee()+data.getDeliveryFee()+data.getDeliveryAdditionalWeightFee()+data.getDeliveryBalanceFee()+data.getShipmentFee()+data.getScanFee())/data.getOrderSum();
				data.setCostAvg(numberFormat.format(totalCost));
				data.setTsfFeeAvg(numberFormat.format(data.getTsfFee()/data.getOrderSum()));
				data.setDeliveryFeeAvg(numberFormat.format(data.getDeliveryFee()/data.getOrderSum()));
				data.setDeliveryAdditionalWeightFeeAvg(numberFormat.format(data.getDeliveryAdditionalWeightFee()/data.getOrderSum()));
				data.setDeliveryBalanceFeeAvg(numberFormat.format(data.getDeliveryBalanceFee()/data.getOrderSum()));
				data.setShipmentFeeAvg(numberFormat.format(data.getShipmentFee()/data.getOrderSum()));
				data.setScanFeeAvg(numberFormat.format(data.getScanFee()/data.getOrderSum()));
			}
		}
		return costData;
	}
	
	@Override
	public List<CostreportLiangBenLiDO> getCustSingleOrderDetail(Bo_custSingleCost boCustSingleCost){
		NumberFormat numberFormat = NumberFormat.getInstance();   
		numberFormat.setMaximumFractionDigits(2);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("branchCode", boCustSingleCost.getBranchCode());
		map.put("accountDt", boCustSingleCost.getAccountDt());
		map.put("customerId", boCustSingleCost.getCustomerId());
		String firstDayOfMonth = null;
		String lastDayOfMonth = null;
		//格式为 201907
		String quMonth = boCustSingleCost.getAccountDt();
		int year = 2019;
		int month = 01;
		if(null != quMonth && quMonth.length() == 5){
			year = Integer.valueOf(quMonth.substring(0, 3));
			month = Integer.valueOf(quMonth.substring(4, 5));
		}
		firstDayOfMonth = DateUtils.getFirstDayOfMonth(year,month);
		lastDayOfMonth = DateUtils.getLastDayOfMonth(year,month);
		map.put("firstDayOfMonth", firstDayOfMonth);
		map.put("lastDayOfMonth", lastDayOfMonth);
		List<CostreportLiangBenLiDO> costData = costreportCustCostFinishDao.getCustSingleOrderDetail(map);
		return costData;
	}
	
	@Override
	public List<CostreportLiangBenLiDO> getMonthCostAndIncome(Bo_custSingleCost boCustSingleCost){
		NumberFormat numberFormat = NumberFormat.getInstance();   
		numberFormat.setMaximumFractionDigits(2);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("branchCode", boCustSingleCost.getBranchCode());
		map.put("accountDt", boCustSingleCost.getAccountDt());
		Double totalCost = null; 
		List<CostreportLiangBenLiDO> costData = costreportCustCostFinishDao.getMonthCostAndIncome(map);
		if(null != costData && costData.size() > 0){
			for(CostreportLiangBenLiDO data : costData){
				totalCost = data.getTsfFee()+data.getDeliveryFee()+data.getDeliveryAdditionalWeightFee()
						+data.getDeliveryBalanceFee()+data.getShipmentFee()+data.getScanFee();
				data.setTotalCost(totalCost);
				data.setProfit(data.getFee()-totalCost);
				data.setWeightAvg(numberFormat.format(data.getWeight()/data.getOrderSum()));
			}
		}
		
		
		return costData;
	}
	
	@Override
	public int count(Bo_custSingleCost boCustSingleCost){
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("XXX",boInterface.getXXX);
		return costreportCustCostFinishDao.count(map);
	}
	
	
}
