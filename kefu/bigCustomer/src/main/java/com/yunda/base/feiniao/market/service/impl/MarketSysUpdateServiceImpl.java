package com.yunda.base.feiniao.market.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yunda.base.feiniao.market.dao.MarketOccupancyReportDao;
import com.yunda.base.feiniao.market.service.MarketSysUpdateService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;


@Service
@Transactional
public class MarketSysUpdateServiceImpl implements MarketSysUpdateService{

	@Autowired
	private MarketOccupancyReportDao marketOccupancyReportDao;
	
	@Override
	public void synUpdateData(Date date) {
		String[] ny = DateUtils.format(date).split("-");
		HashMap<String,String> map = new HashMap<>();
		map.put("year", ny[0]);
		map.put("month", ny[1]);
		int count = marketOccupancyReportDao.countMarketData(ny[0],ny[1]);
		if(count == 0){
			marketOccupancyReportDao.saveMarketData(ny[0],ny[1]);
		}
		try {
		Double result = marketOccupancyReportDao.provinceSum(ny[0],ny[1]);
		if(result == 0){
		String sssdate = DateUtils.getMonthBegin(DateUtils.format(date));
		String eeedate = DateUtils.getMonthEnd(DateUtils.format(date));
		map.put("startDate", sssdate);
		map.put("endDate", eeedate);
		//折线图需求
		marketOccupancyReportDao.updateMarketData(map);		
		List<HashMap<String,String>> searchProvinceID = marketOccupancyReportDao.searchProvinceID(map);		
			for (Map<String, String> mapList : searchProvinceID) {
			if("320000".equals(mapList.get("provinceID"))){
				map.put("provinceID", "320000");
				marketOccupancyReportDao.updateJiangSu(map);
 			}else if("330000".equals(mapList.get("provinceID"))){
				map.put("provinceID", "330000");
				marketOccupancyReportDao.updateZheJiang(map);
			}else{
				map.put("provinceID", mapList.get("provinceID"));
				marketOccupancyReportDao.updateOther(map);
			}
		}
	 }		
	} catch (Exception e) {
	}
	}

}
