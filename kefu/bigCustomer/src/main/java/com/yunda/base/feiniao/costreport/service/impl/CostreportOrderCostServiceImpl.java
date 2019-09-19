package com.yunda.base.feiniao.costreport.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.yunda.base.common.config.Constant;
import com.yunda.base.common.multi.annotation.DataSourceAnnotation;
import com.yunda.base.feiniao.costreport.dao.CostreportOrderCostDao;
import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostDO;
import com.yunda.base.feiniao.costreport.service.CostreportOrderCostService;



@Service
public class CostreportOrderCostServiceImpl implements CostreportOrderCostService {
	
	private  final CostreportOrderCostDao costreportOrderCostDao;
	
	private  final RedisTemplate redisTemplate;

	
	@Autowired
	public CostreportOrderCostServiceImpl(
			CostreportOrderCostDao costreportOrderCostDao,RedisTemplate redisTemplate) {
		super();
		this.costreportOrderCostDao = costreportOrderCostDao;
		this.redisTemplate = redisTemplate;

	}

	@Override
	@DataSourceAnnotation
	public CostreportOrderCostDO get(Integer recordId,String dsId){
		return costreportOrderCostDao.get(recordId);
	}
	
	@Override
	@DataSourceAnnotation
	public List<CostreportOrderCostDO> list(Map<String, Object> map,String dsId){
		
		List<CostreportOrderCostDO> list = costreportOrderCostDao.list(map);
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
		for (CostreportOrderCostDO data : list) {		
			if(data.getCustomerId() != null && !data.getCustomerId().isEmpty()){
				Map<String, Object> customerInfo = operationsCqkh.get(data.getCustomerId());
				if(customerInfo != null && customerInfo.get("khmc") != null) {
					data.setCustomerName(customerInfo.get("khmc").toString());						
			}
		 }		
			String result =Constant.province_code_name.get(data.getStartProvinceId()+"")+"";
			String result1 =Constant.province_code_name.get(data.getEndProvinceId()+"")+"";
			data.setStartProvinceName(result);
			data.setEndProvinceName(result1);
	    }	
		return list;
	}
	
	@Override
	@DataSourceAnnotation
	public int count(Map<String, Object> map,String dsId){
		return costreportOrderCostDao.count(map);
	}
	
	@Override
	@DataSourceAnnotation
	public int save(CostreportOrderCostDO costreportOrderCost,String dsId){
		return costreportOrderCostDao.save(costreportOrderCost);
	}
	
	@Override
	@DataSourceAnnotation
	public int update(CostreportOrderCostDO costreportOrderCost,String dsId){
		return costreportOrderCostDao.update(costreportOrderCost);
	}
	
	@Override
	@DataSourceAnnotation
	public int remove(Integer recordId,String dsId){
		return costreportOrderCostDao.remove(recordId);
	}
	
	@Override
	@DataSourceAnnotation
	public int batchRemove(Integer[] recordIds,String dsId){
		return costreportOrderCostDao.batchRemove(recordIds);
	}
	
}
