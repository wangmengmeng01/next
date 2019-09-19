package com.yunda.base.feiniao.market.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.yunda.base.feiniao.market.bo.Bo_marketKeyProvince;
import com.yunda.base.feiniao.market.dao.MarketKeyProvinceReportDao;
import com.yunda.base.feiniao.market.domain.ExportMarketKeyProvinceReportDO;
import com.yunda.base.feiniao.market.domain.MarketKeyProvinceReportDO;
import com.yunda.base.feiniao.market.service.MarketKeyProvinceReportService;
import com.yunda.base.system.domain.UserDO;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;



@Service
public class MarketKeyProvinceReportServiceImpl implements MarketKeyProvinceReportService {
	@Autowired
	private MarketKeyProvinceReportDao marketKeyProvinceReportDao;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public List<MarketKeyProvinceReportDO> list(Bo_marketKeyProvince boMarketKeyProvince,UserDO loginUser){
		 List<MarketKeyProvinceReportDO> allcountDatas = new ArrayList<MarketKeyProvinceReportDO>();
		String startDate = "";
		String endDate = "";
		//加日期条件---月
		if(boMarketKeyProvince.getMonthDate() !=null && ! boMarketKeyProvince.getMonthDate().isEmpty() && boMarketKeyProvince.getMonthYear() !=null && ! boMarketKeyProvince.getMonthYear().isEmpty()){  
			 String a = boMarketKeyProvince.getMonthYear()+"-"+boMarketKeyProvince.getMonthDate()+"-01";
			 String endDatenum = DateUtils.getMonthEnd(a);
			 String[] b = endDatenum.split("-");
			 String daynum = b[2];
			 startDate = boMarketKeyProvince.getMonthYear()+"-"+boMarketKeyProvince.getMonthDate()+"-01";
			 endDate = boMarketKeyProvince.getMonthYear()+"-"+boMarketKeyProvince.getMonthDate()+"-"+daynum;
		}
		//查询时否审核完全通过
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("reportNian",boMarketKeyProvince.getMonthYear());
		paramMap.put("reportYue",boMarketKeyProvince.getMonthDate());
		int shResult = marketKeyProvinceReportDao.approveCount(paramMap);
		if(shResult < 1){
			return allcountDatas;//未完全通过审核，返回空集合
		}
		
		//不能选今天和今天之后
		paramMap.put("quDate", endDate);
		int dataResult = marketKeyProvinceReportDao.dateCount(paramMap);
		if(dataResult == 0){
			return allcountDatas;
		}
		
		List<MarketKeyProvinceReportDO> allDatas = getAllData(
				boMarketKeyProvince, startDate, endDate, paramMap);
		
		return allDatas;
	}

	private List<MarketKeyProvinceReportDO> getAllData(
			Bo_marketKeyProvince boMarketKeyProvince, String startDate,
			String endDate, Map<String, Object> paramMap) {
		String[] ny = startDate.split("-");
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("startDate", startDate);
		searchMap.put("endDate", endDate);
		searchMap.put("reportNian",ny[0]);
		searchMap.put("reportYue",ny[1]);
		searchMap.put("reportYuesv",ny[1]);
		//CachePrefixConformity cache = new CachePrefixConformity();
		//ValueOperations<String, List<MarketKeyProvinceReportDO>> operations = redisTemplate.opsForValue();
		//String khJLMapStrin  = JSON.toJSONString(searchMap);
	   // String khJLMapprefix = MD5Utils.encrypt(khJLMapStrin);
	    List<MarketKeyProvinceReportDO> allDataList = new ArrayList<MarketKeyProvinceReportDO>();
	    //key(查询条件) 缓存有数据从缓存拿，没有再去查数据库(若需求要求有不同账号有不同权限，此方法不行)
	   // List<MarketKeyProvinceReportDO> allDatas = operations.get(cache.getSeed(Constant.STHJSCFEFXDBB+khJLMapprefix,SuckCacheKeyPerfixEnum.marketKeyProvince.getCode()));
	    // if(allDatas == null || allDatas.size() < 1){
		    List<MarketKeyProvinceReportDO> allcountDatas = marketKeyProvinceReportDao.allDataList(searchMap);
	    	//韵达月总量
			double ydyzl = allcountDatas.get(0).getOrderSum();
			//韵达占比
			double ydzb = allcountDatas.get(0).getProportionYd();
			//31省快递行业月总量
			if(ydzb == 0.0){
				return allDataList;
			}
			double kdhyzl = ydyzl/(ydzb/100);
			boMarketKeyProvince.setYdyzl(ydyzl);
			boMarketKeyProvince.setKdhyzl(kdhyzl);
			allcountDatas.get(0).toDataHandle(boMarketKeyProvince);
			allcountDatas.get(0).setBasicalInformation("总合计");
			allDataList.add(allcountDatas.get(0));

			
			// 循环大区
			List<MarketKeyProvinceReportDO> allBigareaDatas = marketKeyProvinceReportDao.bigareaDataList(searchMap);
			for(MarketKeyProvinceReportDO bigareaData : allBigareaDatas){
				if(StringUtils.isEmpty(bigareaData.getBigarea())) {
					continue;
				}
				bigareaData.toDataHandle(boMarketKeyProvince);
				bigareaData.setBasicalInformation(bigareaData.getBigarea()+"大区合计");
				allDataList.add(bigareaData);
				//省份数据
				searchMap.put("bigarea", bigareaData.getBigarea());
				List<MarketKeyProvinceReportDO> allProvinceDatas = marketKeyProvinceReportDao.provinceDataList(searchMap);
				for(MarketKeyProvinceReportDO provinceDate : allProvinceDatas){
					provinceDate.toDataHandle(boMarketKeyProvince);
					provinceDate.setBasicalInformation(provinceDate.getProvincename());
					allDataList.add(provinceDate);
				}
				
			}
	    	//operations.set(cache.getSeed(Constant.STHJSCFEFXDBB+khJLMapprefix,SuckCacheKeyPerfixEnum.marketKeyProvince.getCode()), allDataList,86400, TimeUnit.SECONDS);
	    	
	    	return allDataList;
	  //  }
	
		
		
		//return allDatas;
	}
	
	@Override
	public List<ExportMarketKeyProvinceReportDO> filterData(
			List<MarketKeyProvinceReportDO> marketKeyProvinceReportList) {
		List<ExportMarketKeyProvinceReportDO> marketDataList = new ArrayList<ExportMarketKeyProvinceReportDO>();
		ExportMarketKeyProvinceReportDO newMarketDO = null;
		for(MarketKeyProvinceReportDO data : marketKeyProvinceReportList){		
		    	
		    	 newMarketDO = new ExportMarketKeyProvinceReportDO();
		    		 
		    	 newMarketDO.setBasicalInformation(data.getBasicalInformation());
		    	 newMarketDO.setResponsiblePeople(data.getResponsiblePeople());
		    	 newMarketDO.setMonthScore(data.getMonthScore());
		    	 newMarketDO.setAB(data.getAB());
		    	 newMarketDO.setCD(data.getCD());
		    	 newMarketDO.setKdqsrjdl(data.getKdqsrjdl());
		    	 newMarketDO.setKdqsyzl(data.getKdqsyzl());
		    	 newMarketDO.setYdqsyzl(data.getYdqsyzl());
		    	 newMarketDO.setYdqsrjl(data.getYdqsrjl());
		    	 newMarketDO.setYdqsscfezb(data.getYdqsscfezb());
		    	 newMarketDO.setZtqsyzl(data.getZtqsyzl());
		    	 newMarketDO.setZtqsrjl(data.getZtqsrjl());
		    	 newMarketDO.setZtqsscfezb(data.getZtqsscfezb());
		    	 newMarketDO.setYtqsyzl(data.getYtqsyzl());
		    	 newMarketDO.setYtqsrjl(data.getYtqsrjl());
		    	 newMarketDO.setYtqsscfezb(data.getYtqsscfezb());
		    	 newMarketDO.setStqsyzl(data.getStqsyzl());
		    	 newMarketDO.setStqsrjl(data.getStqsrjl());
		    	 newMarketDO.setStqsscfezb(data.getStqsscfezb());
		    	 newMarketDO.setBsqsyzl(data.getBsqsyzl());
		    	 newMarketDO.setBsqsrjl(data.getBsqsrjl());
		    	 newMarketDO.setBsqsscfezb(data.getBsqsscfezb());
		    	 
	    		 marketDataList.add(newMarketDO);		    		 
		    	 
		      
		}      
		return marketDataList;
	}
	
}
