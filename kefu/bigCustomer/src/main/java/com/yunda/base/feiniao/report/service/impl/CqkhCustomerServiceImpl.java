package com.yunda.base.feiniao.report.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.yunda.base.feiniao.report.dao.ReportTotaldataDao;
import com.yunda.base.feiniao.report.service.CqkhCustomerService;
import com.yunda.base.feiniao.report.service.ReportTotaldataService;
import com.yunda.base.feiniao.schedule.suckdata.service.SuckDataService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

@Service
public class CqkhCustomerServiceImpl implements CqkhCustomerService{
	public static int batchValue = 5000;
	
	@Autowired
	ReportTotaldataDao dao;
	@Autowired
	CqkhCustomerCacheService cqkhCustomerCacheService;

	@Autowired
	ReportTotaldataService reportTotaldataService;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	SuckDataService suckDataService;
	@Override
	public void synCqkhCustomer() {
		int erweima_pageSize = batchValue;
		int skip = 0;
		HashMap<String, Object> cqkh_paramMap = new HashMap<String, Object>();
		cqkh_paramMap.put("limit", batchValue);
		//获取客户名称和客户电话
		cqkh_paramMap.put("skip", 0);
		String startDay = DateUtils.getMonthBegin(DateUtils.formatDate(new Date()));
		cqkh_paramMap.put("startDay", startDay);
		List<Map<String, Object>> queryForList = dao.searchCqkhCustomerData(cqkh_paramMap);
		while(queryForList != null && !queryForList.isEmpty()) {
			for (Map<String, Object> map : queryForList) {
				String customerId = map.get("gs")+""+map.get("bm")+"";
//			cqkhCustomerCacheService.setTotalReportStatus(customerId, map);
				ValueOperations<String, Map<String, Object>> operations = redisTemplate.opsForValue();
				operations.set(customerId, map,86400, TimeUnit.SECONDS);	
			}
			erweima_pageSize += batchValue;
			cqkh_paramMap.put("skip", erweima_pageSize);
			queryForList = dao.searchCqkhCustomerData(cqkh_paramMap);
		}
		
		
	}
	
	@Override
	public void synCNAndJDSeller(String dsId) {
		int erweima_pageSize = 0;
		int skip = 0;
		HashMap<String, Object> cqkh_paramMap = new HashMap<String, Object>();
		cqkh_paramMap.put("limit", batchValue);
		
		//获取cainiao库的 商家id和店铺名称
		cqkh_paramMap.put("skip", 0);
		List<Map<String, Object>> queryForList2 = reportTotaldataService.searchSellerCN(cqkh_paramMap,dsId);
				/*TaskBeanUtils.getReportTotaldataServiceImpl()
				.searchSellerCN(cqkh_paramMap, "db3");*/
				
		//System.out.println("~~~~branch_code~~~~~"+queryForList2.get(0).get("branch_code"));
		//System.out.println("~~~~customer_id~~~~~"+queryForList2.get(0).get("customer_id"));
		//System.out.println("~~~~seller_id~~~~~"+queryForList2.get(0).get("seller_id"));
		//System.out.println("~~~~seller_name~~~~~"+queryForList2.get(0).get("seller_name"));
		while(queryForList2 != null && !queryForList2.isEmpty()) {
			for (Map<String, Object> map : queryForList2) {
				//飞鸟系统的客户id是由网点编码和分配的vip编码组合而来   即:branch_code(gs)+customer_id(bm)
				String customerId = map.get("branch_code")+""+map.get("customer_id")+"cn";
				ValueOperations<String, Map<String, Object>> operations = redisTemplate.opsForValue();
				operations.set(customerId, map,86400, TimeUnit.SECONDS);
			}
			erweima_pageSize += batchValue;
			cqkh_paramMap.put("skip", erweima_pageSize);
			queryForList2 = reportTotaldataService.searchSellerCN(cqkh_paramMap,"db3");
					/*TaskBeanUtils.getReportTotaldataServiceImpl()
					.searchSellerCN(cqkh_paramMap, "db3");*/
					
		}
		//获取jingdong库的 京东商家id和店铺名称
		cqkh_paramMap.put("skip", 0);//重置skip和erweima_pageSize的值
		erweima_pageSize = 0;
		List<Map<String, Object>> queryForList3 = reportTotaldataService.searchSellerJD(cqkh_paramMap,"db3");
				/*TaskBeanUtils.getReportTotaldataServiceImpl()
				.searchSellerJD(cqkh_paramMap, "db3");*/
		//System.out.println("erweima_pageSize"+erweima_pageSize);		
		//System.out.println("~~~~vendor_code~~~~~"+queryForList3.get(0).get("vendor_code"));
		while(queryForList3 != null && !queryForList3.isEmpty()) {
			for (Map<String, Object> map : queryForList3) {
				String customerId = map.get("branch_code")+""+map.get("customer_id")+"jd";
				ValueOperations<String, Map<String, Object>> operations = redisTemplate.opsForValue();
				operations.set(customerId, map,86400, TimeUnit.SECONDS);
			}
			erweima_pageSize += batchValue;
			cqkh_paramMap.put("skip", erweima_pageSize);
			queryForList3 = reportTotaldataService.searchSellerJD(cqkh_paramMap,"db3");
					/*TaskBeanUtils.getReportTotaldataServiceImpl()
					.searchSellerJD(cqkh_paramMap, "db3");*/
					
		}
	}

}