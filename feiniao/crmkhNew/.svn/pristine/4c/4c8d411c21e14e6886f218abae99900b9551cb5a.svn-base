package com.yunda.base.feiniao.crmapp.report.service.impl;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.feiniao.crmapp.report.bo.Bo_gsOdSum;
import com.yunda.base.feiniao.crmapp.report.dao.GsOdSumDao;
import com.yunda.base.feiniao.crmapp.report.domain.GsOdSumDO;
import com.yunda.base.feiniao.crmapp.report.service.GsOdSumService;
import com.yunda.base.feiniao.report.utils.DateUtils;

@Service
public class GsOdSumServiceImpl implements GsOdSumService {
	@Autowired
	private GsOdSumDao gsOdSumDao;
	
	@Override
	public List<GsOdSumDO> getBranchlist(Bo_gsOdSum boGsOdSum){
		NumberFormat numberFormat = NumberFormat.getInstance();   
		numberFormat.setMaximumFractionDigits(2);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("quDate", boGsOdSum.getQuDate());
		map.put("branchCode", boGsOdSum.getBranchCode());
		List<GsOdSumDO> branchList = gsOdSumDao.getBranchList(map);
		for(GsOdSumDO data : branchList){
			if(data.getLastDayOrderSum() != 0){
				data.setOrderSumFluctuate(numberFormat.format((data.getOrderSum()/data.getLastDayOrderSum()-1)*100) + "%"); 
			}else{
				data.setOrderSumFluctuate("前一天单量为0"); 
			}
		}
		return branchList;
	}
	
	@Override
	public List<GsOdSumDO> getCustlist(Bo_gsOdSum boGsOdSum){
		NumberFormat numberFormat = NumberFormat.getInstance();   
		numberFormat.setMaximumFractionDigits(2);
		Map<String, Object> map = new HashMap<String, Object>();
		String lastDate = DateUtils.getSpecifiedDayBefore(boGsOdSum.getQuDate());
		map.put("quDate", boGsOdSum.getQuDate());
		map.put("lastDate", lastDate);
		map.put("branchCode", boGsOdSum.getBranchCode());
		map.put("limit", boGsOdSum.getLimit());
		map.put("offset", boGsOdSum.getOffset());
		List<GsOdSumDO> custList = gsOdSumDao.getBranchList(map);
		for(GsOdSumDO data : custList){
			if(data.getLastDayOrderSum() != 0){
				data.setOrderSumFluctuate(numberFormat.format((data.getOrderSum()/data.getLastDayOrderSum()-1)*100) + "%"); 
			}else{
				data.setOrderSumFluctuate("前一天单量为0"); 
			}
		}
		return custList;
	}
	
	@Override
	public int custlistCount(Bo_gsOdSum boGsOdSum){
		NumberFormat numberFormat = NumberFormat.getInstance();   
		numberFormat.setMaximumFractionDigits(2);
		Map<String, Object> map = new HashMap<String, Object>();
		String lastDate = DateUtils.getSpecifiedDayBefore(boGsOdSum.getQuDate());
		map.put("quDate", boGsOdSum.getQuDate());
		map.put("lastDate", lastDate);
		map.put("branchCode", boGsOdSum.getBranchCode());
		return gsOdSumDao.custlistCount(map);
	}
}
