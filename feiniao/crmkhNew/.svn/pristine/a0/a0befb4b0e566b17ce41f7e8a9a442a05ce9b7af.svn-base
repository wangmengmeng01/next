package com.yunda.base.feiniao.market.service.impl;

import com.alibaba.fastjson.JSON;
import com.yunda.base.common.config.Constant;
import com.yunda.base.feiniao.market.bo.Bo_marketKeyCity;
import com.yunda.base.feiniao.market.dao.MarketKeyCitiesReportDao;
import com.yunda.base.feiniao.market.domain.ExportMarketKeyCityReportDO;
import com.yunda.base.feiniao.market.domain.MarketKeyCitiesReportDO;
import com.yunda.base.feiniao.market.domain.MarketKeyProvinceReportDO;
import com.yunda.base.feiniao.market.service.MarketKeyCitiesReportService;
import com.yunda.base.feiniao.report.utils.CachePrefixConformity;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;
import com.yunda.base.system.domain.UserDO;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class MarketKeyCitiesReportServiceImpl implements MarketKeyCitiesReportService {
	@Autowired
	private MarketKeyCitiesReportDao marketKeyCitiesReportDao;
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	public List<MarketKeyCitiesReportDO> list(Bo_marketKeyCity boMarketKeyCity,UserDO loginUser){
		List<MarketKeyCitiesReportDO> allcountDatas = new ArrayList<MarketKeyCitiesReportDO>();
		String startDate = "";
		String endDate = "";
		//加日期条件---月
		if(boMarketKeyCity.getMonthDate() !=null && ! boMarketKeyCity.getMonthDate().isEmpty() && boMarketKeyCity.getMonthYear() !=null && ! boMarketKeyCity.getMonthYear().isEmpty()){  
			 String a = boMarketKeyCity.getMonthYear()+"-"+boMarketKeyCity.getMonthDate()+"-01";
			 String endDatenum = DateUtils.getMonthEnd(a);
			 String[] b = endDatenum.split("-");
			 String daynum = b[2];
			 startDate = boMarketKeyCity.getMonthYear()+"-"+boMarketKeyCity.getMonthDate()+"-01";
			 endDate = boMarketKeyCity.getMonthYear()+"-"+boMarketKeyCity.getMonthDate()+"-"+daynum;
		}
		
		//查询时否审核完全通过
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("reportNian",boMarketKeyCity.getMonthYear());
		paramMap.put("reportYue",boMarketKeyCity.getMonthDate());
/*		int shResult = marketKeyCitiesReportDao.approveCount(paramMap);
		if(shResult < 1){
			return allcountDatas;//未完全通过审核，返回空集合
		}*/
		
		//不能选今天和今天之后
		paramMap.put("quDate", endDate);
		int dataResult = marketKeyCitiesReportDao.dateCount(paramMap);
		if(dataResult == 0){
			return allcountDatas;
		}
		// 权限控制-----此权限只有总部和省总有
		if (loginUser.hasPerms(Constant.REPORT_ADMIN_PERMS)) {
			// 超级用户权限 无限制
			// 系统菜单配置了report:admin:allperms权限标识 角色管理中当该用户的角色勾选该权限标识就说明该用户是超级用户
			// 能查看所有报表的集团大区省市等所有数据
			boMarketKeyCity.setTmpField("zb");
		} else {
			if (loginUser.isProvinceqx()) {// 是否有省权限
				boMarketKeyCity.setProvinceids(loginUser.getProvinceIds());
				boMarketKeyCity.setTmpField("S");
			} 
		}
		
		List<MarketKeyCitiesReportDO> allDatas = getAllData(
				boMarketKeyCity, startDate, endDate);
		
		return allDatas;
	}
	
	private List<MarketKeyCitiesReportDO> getAllData(Bo_marketKeyCity boMarketKeyCity, String startDate,String endDate) {
	    List<MarketKeyCitiesReportDO> allDataList = new ArrayList<MarketKeyCitiesReportDO>();
		//CachePrefixConformity cache = new CachePrefixConformity();
		//ValueOperations<String, List<MarketKeyCitiesReportDO>> operations = redisTemplate.opsForValue();
		String[] ny = startDate.split("-");
		Map<String, Object> cityMap = new HashMap<String, Object>();
		cityMap.put("startDate", startDate);
		cityMap.put("endDate", endDate);
		cityMap.put("reportNian",ny[0]);
		cityMap.put("reportYue",ny[1]);
		cityMap.put("TTmpField",boMarketKeyCity.getTmpField());
		if("zb".equals(boMarketKeyCity.getTmpField())){
			//String khJLMapStrin  = JSON.toJSONString(cityMap);
		   // String khJLMapprefix = MD5Utils.encrypt(khJLMapStrin);
		   // List<MarketKeyCitiesReportDO> allDatas = operations.get(cache.getSeed(Constant.ZDCSTHJSCFEDBB+khJLMapprefix,SuckCacheKeyPerfixEnum.marketKeyCity.getCode()));
		   // if(allDatas == null || allDatas.size() < 1){
		    List<MarketKeyCitiesReportDO> allcountDatas = marketKeyCitiesReportDao.allDataList(cityMap);
			
			//韵达月总量
			double ydyzl = allcountDatas.get(0).getOrderSum();
			//韵达占比
			double ydzb = allcountDatas.get(0).getProportionYd();
			//50重点城市快递行业月总量
			if(ydzb == 0.0){
				return allDataList;
			}
			double kdhyzl = ydyzl/(ydzb/100);
			boMarketKeyCity.setYdyzl(ydyzl);
			boMarketKeyCity.setKdhyzl(kdhyzl);
			allcountDatas.get(0).toDataHandle(boMarketKeyCity);
			allcountDatas.get(0).setBasicalInformation("总合计");
			allDataList.add(allcountDatas.get(0));
			
			// 循环大区
			List<MarketKeyCitiesReportDO> allBigareaDatas = marketKeyCitiesReportDao.bigareaDataList(cityMap);
			for(MarketKeyCitiesReportDO bigareaData : allBigareaDatas){
				if(StringUtils.isEmpty(bigareaData.getBigarea())) {
					continue;
				}
				bigareaData.toDataHandle(boMarketKeyCity);
				bigareaData.setColorFlag(1);
				bigareaData.setBasicalInformation(bigareaData.getBigarea()+"合计");
				allDataList.add(bigareaData);
				//省份数据
				cityMap.put("bigarea", bigareaData.getBigarea());
				List<MarketKeyCitiesReportDO> allProvinceDatas = marketKeyCitiesReportDao.provinceDataList(cityMap);
				for(MarketKeyCitiesReportDO provinceDate : allProvinceDatas){
					provinceDate.toDataHandle(boMarketKeyCity);
					provinceDate.setBasicalInformation(provinceDate.getProvincename());
					provinceDate.setColorFlag(2);
					allDataList.add(provinceDate);
					//市数据
					cityMap.put("provinceId", provinceDate.getProvinceid());
					List<MarketKeyCitiesReportDO> allCityDatas = marketKeyCitiesReportDao.cityDataList(cityMap);
					for(MarketKeyCitiesReportDO cityData : allCityDatas){
						cityData.toDataHandle(boMarketKeyCity);
						cityData.setBasicalInformation(cityData.getCityname());
						cityData.setColorFlag(3);
						allDataList.add(cityData);
					}
					
				}
				
		//	}
				//operations.set(cache.getSeed(Constant.ZDCSTHJSCFEDBB+khJLMapprefix,SuckCacheKeyPerfixEnum.marketKeyCity.getCode()), allDataList,86400, TimeUnit.SECONDS);
				
		    }
		}else{
			if("S".equals(boMarketKeyCity.getTmpField())){
				cityMap.put("TProvinceID",boMarketKeyCity.getProvinceids());
				List<MarketKeyCitiesReportDO> allProvinceDatas = marketKeyCitiesReportDao.provinceDataList(cityMap);
		    	//省韵达月总量
				double ydyzl = allProvinceDatas.get(0).getOrderSum();
				//韵达占比
				double ydzb = allProvinceDatas.get(0).getProportionYd();
				if(ydzb == 0.0){
					return allProvinceDatas;
				}
				//31省快递行业月总量
				double kdhyzl = ydyzl/(ydzb/100);
				boMarketKeyCity.setYdyzl(ydyzl);
				boMarketKeyCity.setKdhyzl(kdhyzl);
				for(MarketKeyCitiesReportDO provinceDate : allProvinceDatas){
					provinceDate.toDataHandle(boMarketKeyCity);
					provinceDate.setBasicalInformation(provinceDate.getProvincename());
					provinceDate.setColorFlag(2);
					allDataList.add(provinceDate);
					//市数据
					cityMap.put("provinceId", provinceDate.getProvinceid());
					List<MarketKeyCitiesReportDO> allCityDatas = marketKeyCitiesReportDao.cityDataList(cityMap);
			    	//省韵达月总量
					ydyzl = allProvinceDatas.get(0).getOrderSum();
					//韵达占比
					ydzb = allProvinceDatas.get(0).getProportionYd();
					if(ydzb == 0.0){
						return allProvinceDatas;
					}
					//31省快递行业月总量
					kdhyzl = ydyzl/(ydzb/100);
					boMarketKeyCity.setYdyzl(ydyzl);
					boMarketKeyCity.setKdhyzl(kdhyzl);
					for(MarketKeyCitiesReportDO cityData : allCityDatas){
						cityData.toDataHandle(boMarketKeyCity);
						cityData.setBasicalInformation(cityData.getCityname());
						cityData.setColorFlag(3);
						allDataList.add(cityData);
					}
					
				}
			}
		}

		return allDataList;
	}

	@Override
	public List<ExportMarketKeyCityReportDO> filterData(
			List<MarketKeyCitiesReportDO> marketKeyCityReportList) {
		List<ExportMarketKeyCityReportDO> marketDataList = new ArrayList<ExportMarketKeyCityReportDO>();
		ExportMarketKeyCityReportDO newMarketDO = null;
		for(MarketKeyCitiesReportDO data : marketKeyCityReportList){		
		    	
		    	 newMarketDO = new ExportMarketKeyCityReportDO();
		    		 
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
