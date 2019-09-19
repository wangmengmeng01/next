package com.yunda.base.feiniao.market.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.google.common.base.Objects;
import com.yunda.base.feiniao.market.dao.MarketOccupancyReportDao;
import com.yunda.base.feiniao.market.domain.ExportMarketOccupancyReportDO;
import com.yunda.base.feiniao.market.domain.MarketOccupancyReportDO;
import com.yunda.base.feiniao.market.service.MarketOccupancyReportService;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;
import com.yunda.base.system.domain.UserDO;



@Service
public class MarketOccupancyReportServiceImpl implements MarketOccupancyReportService {
	@Autowired
	private MarketOccupancyReportDao marketOccupancyReportDao;
	
	@Autowired
	private RedisTemplate redisTemplate;
	@Override
	public MarketOccupancyReportDO get(Integer recordId){
		return marketOccupancyReportDao.get(recordId);
	}
	
	@Override
	public List<MarketOccupancyReportDO> list(Map<String, Object> map){
		return marketOccupancyReportDao.list(map);
	}
	
	@Override
	public List<MarketOccupancyReportDO> listSearch(Map<String,Object> map){
		if("320000".equals(map.get("provinceid"))){
			map.put("type", "2");
		}else if("330000".equals(map.get("provinceid"))){
			map.put("type", "3");
		}else{
			map.put("type", "1");
		}
		return marketOccupancyReportDao.listSearch(map);
	}

	@Override
	public int count(Map<String, Object> map){
		return marketOccupancyReportDao.count(map);
	}
	
	@Override
	public int countSearch(Map<String, Object> map){
		if("320000".equals(map.get("provinceid"))){
			map.put("type", "2");
		}else if("330000".equals(map.get("provinceid"))){
			map.put("type", "3");
		}else{
			map.put("type", "1");
		}
		return marketOccupancyReportDao.countSearch(map);
	}
	
	@Override
	public int save(MarketOccupancyReportDO marketOccupancyReport){
		return marketOccupancyReportDao.save(marketOccupancyReport);
	}
	
	@Override
	public int update(MarketOccupancyReportDO marketOccupancyReport){
		return marketOccupancyReportDao.update(marketOccupancyReport);
	}
	
	@Override
	public int remove(Integer recordId){
		return marketOccupancyReportDao.remove(recordId);
	}
	
	@Override
	public int batchRemove(Integer[] recordIds){
		return marketOccupancyReportDao.batchRemove(recordIds);
	}

	@Override
	public List<Map<String, Object>> searchShengData() {
		return marketOccupancyReportDao.searchShengData();
	}
	
	
	    // 暂存的数据
		@SuppressWarnings("resource")
		@Override
		public  void cacheSave(HttpServletRequest request, MarketOccupancyReportDO ocupancyReportDO,UserDO loginUser) {
			ValueOperations<String, HashMap<String,MarketOccupancyReportDO>> operations = redisTemplate.opsForValue();					
			HashMap<String,MarketOccupancyReportDO> redisMap = operations.get(SuckCacheKeyPerfixEnum.marketReport.getCode()+ocupancyReportDO.getProvinceid()+ocupancyReportDO.getReportDate()+loginUser.getUserId());
            if(redisMap !=null){
            	redisMap.put(ocupancyReportDO.getRecordId()+"", ocupancyReportDO);
            	operations.set(SuckCacheKeyPerfixEnum.marketReport.getCode()+ocupancyReportDO.getProvinceid()+ocupancyReportDO.getReportDate()+loginUser.getUserId(), redisMap, 30, TimeUnit.MINUTES);
            }else{
    			HashMap<String,MarketOccupancyReportDO> map = new HashMap<String,MarketOccupancyReportDO>();
    			map.put(ocupancyReportDO.getRecordId()+"", ocupancyReportDO);
    			operations.set(SuckCacheKeyPerfixEnum.marketReport.getCode()+ocupancyReportDO.getProvinceid()+ocupancyReportDO.getReportDate()+loginUser.getUserId(), map, 30, TimeUnit.MINUTES);
            }		
		}

		@Override
		public int checkResult(HashMap<String, Object> map) {
			return marketOccupancyReportDao.checkResult(map);
		}

		@Override
		public void upDataResult(HttpServletRequest request,MarketOccupancyReportDO crmkh_market_occupancy_reportData,UserDO loginUser) {	
			ValueOperations<String, HashMap<String,MarketOccupancyReportDO>> operations = redisTemplate.opsForValue();					
			HashMap<String,MarketOccupancyReportDO> redisMap = operations.get(SuckCacheKeyPerfixEnum.marketReport.getCode()+crmkh_market_occupancy_reportData.getProvinceid()+crmkh_market_occupancy_reportData.getReportDate()+loginUser.getUserId());			
			String[] ny = crmkh_market_occupancy_reportData.getReportDate().split("-");
			HashMap<String,Object> map = new HashMap<>();
			map.put("provinceID", crmkh_market_occupancy_reportData.getProvinceid());
			map.put("year", ny[0]);
			map.put("month", ny[1]);
			crmkh_market_occupancy_reportData.setReportNian(ny[0]);
			crmkh_market_occupancy_reportData.setReportYue(ny[1]);
			if("320000".equals(crmkh_market_occupancy_reportData.getProvinceid())){
				map.put("type", "2");
			}else if("330000".equals(crmkh_market_occupancy_reportData.getProvinceid())){
				map.put("type", "3");
			}else{
				map.put("type", "1");
			}
			List<MarketOccupancyReportDO> upDatas = marketOccupancyReportDao.listSearch(map);			
			for(MarketOccupancyReportDO occupancyReportDO:upDatas){
				System.out.println(occupancyReportDO.getRecordId().toString());
				if(redisMap !=null &&redisMap.get(occupancyReportDO.getRecordId().toString())!=null){
					MarketOccupancyReportDO report = redisMap.get(occupancyReportDO.getRecordId().toString());
					report.setReportNian(ny[0]);
					report.setReportYue(ny[1]);
					marketOccupancyReportDao.upDataResult(report);	
				}				
			}
			marketOccupancyReportDao.upDataShangBao(crmkh_market_occupancy_reportData);	
		}

		@Override
		public String auditData(MarketOccupancyReportDO occupancyReportDO) {
			String[] ny = occupancyReportDO.getReportDate().split("-");
			occupancyReportDO.setReportNian(ny[0]);
			occupancyReportDO.setReportYue(ny[1]);
			List<MarketOccupancyReportDO> list = marketOccupancyReportDao.searchReportStatus(occupancyReportDO);			
			//判断本月得分
			if("1".equals(occupancyReportDO.getAuditResult())){
				occupancyReportDO.setMonthScore(2);
			}else if("2".equals(occupancyReportDO.getAuditResult())){
				occupancyReportDO.setMonthScore(-10);
			}else if("3".equals(occupancyReportDO.getAuditResult())){
				occupancyReportDO.setMonthScore(-2);
			}
			if("1".equals(list.get(0).getReportStatus())){
				return "ysh";
			}else{
				marketOccupancyReportDao.updateMarketStatus(occupancyReportDO);
			}			
			return null;
		}

		@Override
		public List<ExportMarketOccupancyReportDO> filterData(
				List<MarketOccupancyReportDO> reportTotaldata) {
			List<ExportMarketOccupancyReportDO> totalDate = new ArrayList<ExportMarketOccupancyReportDO>();
			ExportMarketOccupancyReportDO newTotal = new ExportMarketOccupancyReportDO();
			for(MarketOccupancyReportDO data : reportTotaldata){
				newTotal = new ExportMarketOccupancyReportDO();
				newTotal.setRecordId(data.getRecordId());
				newTotal.setAuditRemarks(data.getAuditRemarks());				
				if(Objects.equal(data.getAuditResult(),"2")){
					newTotal.setAuditResult("虚假上报");				
				}else if(Objects.equal(data.getAuditResult(),"1")){
					newTotal.setAuditResult("如实上报");				
				}else if(Objects.equal(data.getAuditResult(),"3")){
					newTotal.setAuditResult("未上报");				
				}
				newTotal.setBigarea(data.getBigarea());
				newTotal.setMonthScore(data.getMonthScore());
				newTotal.setProvincename(data.getProvincename());
				newTotal.setReportNian(data.getReportNian());			
				if(Objects.equal(data.getReportStatus(),"2")){
					newTotal.setReportStatus("待审核");					
				}else if(Objects.equal(data.getReportStatus(),"3")){
					newTotal.setReportStatus("未上报");
				}else if(Objects.equal(data.getReportStatus(),"1")){
					newTotal.setReportStatus("已审核");
				}
				newTotal.setReportYue(data.getReportYue());
				newTotal.setResponsiblePeople(data.getResponsiblePeople());	
	    		totalDate.add(newTotal);

			}
			return totalDate;
		}
	
}
