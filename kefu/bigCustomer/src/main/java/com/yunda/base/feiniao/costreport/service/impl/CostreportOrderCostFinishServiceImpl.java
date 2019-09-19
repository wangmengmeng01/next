package com.yunda.base.feiniao.costreport.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Objects;
import com.yunda.base.common.config.Constant;
import com.yunda.base.common.multi.annotation.DataSourceAnnotation;
import com.yunda.base.feiniao.costreport.bo.Bo_costreportOrderCostFinish_two;
import com.yunda.base.feiniao.costreport.dao.CostreportOrderCostFinishDao;
import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostFinishDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportOrderCostFinishDO;
import com.yunda.base.feiniao.costreport.service.CostreportOrderCostFinishService;
import com.yunda.base.feiniao.report.utils.CachePrefixConformity;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;
import com.yunda.base.system.domain.UserDO;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;


@Service
public class CostreportOrderCostFinishServiceImpl implements CostreportOrderCostFinishService {
	protected static final Logger logger = LoggerFactory.getLogger(CostreportOrderCostFinishServiceImpl.class);

	@Autowired
	private CostreportOrderCostFinishDao costreportOrderCostFinishDao;
	
	@Override
	@DataSourceAnnotation
	public CostreportOrderCostFinishDO get(Integer recordId,String dsId){
		return costreportOrderCostFinishDao.get(recordId);
	}
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	@Override
	@DataSourceAnnotation
	public List<CostreportOrderCostFinishDO> list(Map<String, Object> map,UserDO user,String dsId){
		List<Map<String, Object>> result =new ArrayList<Map<String, Object>>();
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
		String orgType = "";
		if(!StringUtils.isBlank(user.getOrgCode())){
		     //查询公司类别
		   result = costreportOrderCostFinishDao.searchLbByGsInfo(user.getOrgCode());			
		}	
		if(result.size()>0){
		  orgType = result.get(0).get("lb").toString();
		}
	    if(StringUtils.isEmpty(orgType)){
		   orgType = user.getOrgType();
	    }		
		  //查询非总部的登录人所有下属网点(登录人为一级网点)
		if(!Objects.equal("1", orgType)&&!Objects.equal("50", orgType)){			
			Map<String, Object> customerInfo = operationsCqkh.get(user.getUserId());
			if (customerInfo != null && customerInfo.get("khmc") != null) {
			String khgs = customerInfo.get("gs").toString();
			StringBuffer xj = new StringBuffer();
			String xjNew;
			if (Objects.equal(khgs, user.getOrgCode())) {
				List<Map<String, Object>> customerBranchOrg;
				try {
				   customerBranchOrg = costreportOrderCostFinishDao.queryWdByOrgCodeNew(khgs);
	               if(customerBranchOrg.size()>0){
	            	  for(Map<String, Object> xjwd:customerBranchOrg){
	            	     xj.append(xjwd.get("wd").toString()).append(",");
	            	   }
	            	   xjNew = xj.toString();
	            	   xjNew = xj.substring(0, xjNew.length()-1);
	            	   map.put("branchCodeAll", xjNew);
	               }
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}else{
            	 map.put("branchCodeAll", user.getOrgCode());
			}
			}else{
            	 map.put("branchCodeAll", user.getOrgCode());
			}			
		}
		 CachePrefixConformity cache = new CachePrefixConformity();
		 ValueOperations<String,List<CostreportOrderCostFinishDO>> operations = redisTemplate.opsForValue();
		 map.put("test","test10");
		 String cacheString = JSON.toJSONString(map);
		 String cacheResult = MD5Utils.encrypt(cacheString);
//		  从redis里查询是否有缓存数据
		 List<CostreportOrderCostFinishDO> searchCache = operations.get(cache.getSeed(Constant.QUERYCOSTREPORT+cacheResult,SuckCacheKeyPerfixEnum.costReport.getCode()));
		 List<CostreportOrderCostFinishDO> resultOrderCost = new ArrayList<CostreportOrderCostFinishDO>();
			if(searchCache==null||searchCache.size()<1){
				 java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#0.00"); 
				 resultOrderCost =  costreportOrderCostFinishDao.list(map);
//					给查询结果加上客户名称
				   for(CostreportOrderCostFinishDO orderCost : resultOrderCost){
					if(orderCost.getCustomerId() != null && !orderCost.getCustomerId().isEmpty()){
						Map<String, Object> customerInfo = operationsCqkh.get(orderCost.getCustomerId());
						if(customerInfo != null && customerInfo.get("khmc") != null) {
							orderCost.setCustomerName(customerInfo.get("khmc").toString());
						}
					}
				 }
				 if(StringUtils.isNotBlank(map.get("accountDt")+"")){
				 map.put("accountDt", (map.get("accountDt")+"").replace("-", ""));
				 }
				 List<CostreportOrderCostFinishDO> resultOrderCostTotal = costreportOrderCostFinishDao.totalList(map);
				 if(resultOrderCostTotal.size()>0&&resultOrderCost.size()>0){
				 Double incomeTotal  =Double.parseDouble(resultOrderCostTotal.get(0).getIncome().toString());
				 Double weightTotal  =Double.parseDouble(resultOrderCostTotal.get(0).getWeightAll().toString());
				 int report_numberTotal  =Integer.parseInt(resultOrderCostTotal.get(0).getOrderSum().toString());
				 Double profitTotal  =Double.parseDouble(resultOrderCostTotal.get(0).getProfit().toString());
				 
				 resultOrderCostTotal.get(0).setIncomeEach(new BigDecimal(df.format(incomeTotal/report_numberTotal)));
				 resultOrderCostTotal.get(0).setKilogramEach(new BigDecimal(df.format(incomeTotal/weightTotal)));
				 resultOrderCostTotal.get(0).setProfitEach(new BigDecimal(df.format(profitTotal/report_numberTotal)));
				 resultOrderCostTotal.get(0).setKilogramProfitEach(new BigDecimal(df.format(profitTotal/weightTotal)));
				 resultOrderCostTotal.get(0).setCustomerName("首行合计");
				 resultOrderCost.addAll(0, resultOrderCostTotal);				
				 operations.set(cache.getSeed(Constant.QUERYCOSTREPORT+cacheResult,SuckCacheKeyPerfixEnum.costReport.getCode()), resultOrderCost,86400, TimeUnit.SECONDS);
				
				 }
				 return resultOrderCost;
			}
			else
			   return searchCache;
	}
	
	
	
	
	@Override
	@DataSourceAnnotation
	public List<CostreportOrderCostFinishDO> list(Bo_costreportOrderCostFinish_two boCostreportOrderCostFinishTwo,UserDO user,String dsId){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		//paramMap.put("accountDt", boCostreportOrderCostFinishTwo.getAccountDt());
		paramMap.put("accountDt", (boCostreportOrderCostFinishTwo.getAccountDt().replace("-", "")));
		paramMap.put("customerId",boCostreportOrderCostFinishTwo.getCustomerId());
		//疑问：某一级网点只能查询自己的一级网点数据，总部可以查询所有网点数据，如果查询网点属于一级网点，则此branchCode参数只能是自己本网点的一级网点编码
		paramMap.put("branchCode",boCostreportOrderCostFinishTwo.getBranchCode());
		paramMap.put("offset",boCostreportOrderCostFinishTwo.getOffset());
		paramMap.put("limit", boCostreportOrderCostFinishTwo.getLimit());
		paramMap.put("test10","test12");
		List<Map<String, Object>> result =new ArrayList<Map<String, Object>>();
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
		String orgType = "";
		if(!StringUtils.isBlank(user.getOrgCode())){
		   /**
		    * 查询公司类别
			*/
		   result = costreportOrderCostFinishDao.searchLbByGsInfo(user.getOrgCode());			
		}	
		if(result.size()>0){
		  orgType = result.get(0).get("lb").toString();
		}
	    if(StringUtils.isEmpty(orgType)){
		   orgType = user.getOrgType();
	    }		
		 /**
		  * 查询非总部的登录人所有下属网点(登录人为一级网点)
		  */
		if(!Objects.equal("1", orgType)&&!Objects.equal("50", orgType)){			
			Map<String, Object> customerInfo = operationsCqkh.get(user.getUserId());
			if (customerInfo != null && customerInfo.get("khmc") != null) {
			String khgs = customerInfo.get("gs").toString();
			StringBuffer xj = new StringBuffer();
			String xjNew;
			if (Objects.equal(khgs, user.getOrgCode())) {
				List<Map<String, Object>> customerBranchOrg;
				try {
				   customerBranchOrg = costreportOrderCostFinishDao.queryWdByOrgCodeNew(khgs);
	               if(customerBranchOrg.size()>0){
	            	  for(Map<String, Object> xjwd:customerBranchOrg){
	            	     xj.append(xjwd.get("wd").toString()).append(",");
	            	   }
	            	   xjNew = xj.toString();
	            	   xjNew = xj.substring(0, xjNew.length()-1);
	            	   paramMap.put("branchCodeAll", xjNew);
	               }
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}else{
				paramMap.put("branchCodeAll", user.getOrgCode());
			}
			}else{
				paramMap.put("branchCodeAll", user.getOrgCode());
			}			
		}
		 CachePrefixConformity cache = new CachePrefixConformity();
		 ValueOperations<String,List<CostreportOrderCostFinishDO>> operations = redisTemplate.opsForValue();
		 String cacheString = JSON.toJSONString(paramMap);
		 String cacheResult = MD5Utils.encrypt(cacheString);
//		  从redis里查询是否有缓存数据
		 List<CostreportOrderCostFinishDO> searchCache = operations.get(cache.getSeed(Constant.QUERYCOSTREPORT+cacheResult,SuckCacheKeyPerfixEnum.costReport.getCode()));
		 List<CostreportOrderCostFinishDO> resultOrderCost = new ArrayList<CostreportOrderCostFinishDO>();
			if(searchCache==null||searchCache.size()<1){
				 java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#0.00"); 
				 resultOrderCost =  costreportOrderCostFinishDao.list(paramMap);
//					给查询结果加上客户名称
				   for(CostreportOrderCostFinishDO orderCost : resultOrderCost){
					if(orderCost.getCustomerId() != null && !orderCost.getCustomerId().isEmpty()){
						Map<String, Object> customerInfo = operationsCqkh.get(orderCost.getCustomerId());
						if(customerInfo != null && customerInfo.get("khmc") != null) {
							orderCost.setCustomerName(customerInfo.get("khmc").toString());
						}
					}
				 }
				 if(StringUtils.isNotBlank(boCostreportOrderCostFinishTwo.getAccountDt())){
					paramMap.put("accountDt", (boCostreportOrderCostFinishTwo.getAccountDt().replace("-", "")));
				 }
				 List<CostreportOrderCostFinishDO> resultOrderCostTotal = costreportOrderCostFinishDao.totalList(paramMap);
				 if(resultOrderCostTotal.size()>0&&resultOrderCost.size()>0){
				/*	 int report_numberTotal  =Integer.parseInt(resultOrderCostTotal.get(0).getOrderSum().toString());
					 resultOrderCostTotal.get(0).getWeightAll().toString();
					 Double incomeTotal  =Double.parseDouble(resultOrderCostTotal.get(0).getIncome().toString());
					 Double weightTotal  =Double.parseDouble(resultOrderCostTotal.get(0).getWeightAll().toString());
				
					 Double profitTotal  =Double.parseDouble(resultOrderCostTotal.get(0).getProfit().toString());
				 
					 resultOrderCostTotal.get(0).setIncomeEach(new BigDecimal(df.format(incomeTotal/report_numberTotal)));
					 resultOrderCostTotal.get(0).setKilogramEach(new BigDecimal(df.format(incomeTotal/weightTotal)));
					 resultOrderCostTotal.get(0).setProfitEach(new BigDecimal(df.format(profitTotal/report_numberTotal)));
					 resultOrderCostTotal.get(0).setKilogramProfitEach(new BigDecimal(df.format(profitTotal/weightTotal)));*/
					 resultOrderCostTotal.get(0).setCustomerName("首行合计");
					 resultOrderCost.addAll(0, resultOrderCostTotal);				
					 operations.set(cache.getSeed(Constant.QUERYCOSTREPORT+cacheResult,SuckCacheKeyPerfixEnum.costReport.getCode()), resultOrderCost,86400, TimeUnit.SECONDS);
				
				 }
				 return resultOrderCost;
			}
			else
			   return searchCache;
	}
	
	@Override
	@DataSourceAnnotation
	public List<Map<String, Object>> searchCustomerData(Map<String, Object> map,UserDO user,String dsId){
		List<Map<String, Object>> result =new ArrayList<Map<String, Object>>();

		String orgType = "";
		if(!StringUtils.isBlank(user.getOrgCode())){
		   /**
		    * 查询公司类别
			*/
		   result = costreportOrderCostFinishDao.searchLbByGsInfo(user.getOrgCode());			
		}	
		if(result.size()>0){
		  orgType = result.get(0).get("lb").toString();
		}
	    if(StringUtils.isEmpty(orgType)){
		   orgType = user.getOrgType();
	    }	
	    map.put("orgType", orgType);
	    map.put("userId", user.getUserId());		
	    
	    List<Map<String, Object>> customerList = costreportOrderCostFinishDao.searchCustomerData(map);
		return customerList;
		
	}
	
	@Override
	@DataSourceAnnotation
	public List<Map<String, Object>> searchCustomerBraData(Map<String, Object> map,UserDO user,String dsId){
		List<Map<String, Object>> result =new ArrayList<Map<String, Object>>();

		String orgType = "";
		if(!StringUtils.isBlank(user.getOrgCode())){
		   /**
		    * 查询公司类别
			*/
		   result = costreportOrderCostFinishDao.searchLbByGsInfo(user.getOrgCode());			
		}	
		if(result.size()>0){
		  orgType = result.get(0).get("lb").toString();
		}
	    if(StringUtils.isEmpty(orgType)){
		   orgType = user.getOrgType();
	    }	
	    map.put("orgType", orgType);
	    map.put("userId", user.getUserId());		
	    
	    List<Map<String, Object>> customerList = costreportOrderCostFinishDao.searchCustBraData(map);
		return customerList;
		
	}
	
	
	
	@Override
	@DataSourceAnnotation
	public int count(Map<String, Object> map,String dsId){
		return costreportOrderCostFinishDao.count(map);
	}
	
	@Override
	@DataSourceAnnotation
	public int count(
			Bo_costreportOrderCostFinish_two boCostreportOrderCostFinishTwo,
			UserDO user,String dsId) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("accountDt", boCostreportOrderCostFinishTwo.getAccountDt());
		paramMap.put("customerId",boCostreportOrderCostFinishTwo.getCustomerId());
		//疑问：某一级网点只能查询自己的一级网点数据，总部可以查询所有网点数据，如果查询网点属于一级网点，则此branchCode参数只能是自己本网点的一级网点编码
		paramMap.put("branchCode",boCostreportOrderCostFinishTwo.getBranchCode());
		paramMap.put("offset",boCostreportOrderCostFinishTwo.getOffset());
		paramMap.put("limit", boCostreportOrderCostFinishTwo.getLimit());
		paramMap.put("test1","test10");
		List<Map<String, Object>> result =new ArrayList<Map<String, Object>>();
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
		String orgType = "";
		if(!StringUtils.isBlank(user.getOrgCode())){
		     //查询公司类别
		   result = costreportOrderCostFinishDao.searchLbByGsInfo(user.getOrgCode());			
		}	
		if(result.size()>0){
		  orgType = result.get(0).get("lb").toString();
		}
	    if(StringUtils.isEmpty(orgType)){
		   orgType = user.getOrgType();
	    }		
		  //查询非总部的登录人所有下属网点(登录人为一级网点)
		if(!Objects.equal("1", orgType)&&!Objects.equal("50", orgType)){			
			Map<String, Object> customerInfo = operationsCqkh.get(user.getUserId());
			if (customerInfo != null && customerInfo.get("khmc") != null) {
			String khgs = customerInfo.get("gs").toString();
			StringBuffer xj = new StringBuffer();
			String xjNew;
			if (Objects.equal(khgs, user.getOrgCode())) {
				List<Map<String, Object>> customerBranchOrg;
				try {
				   customerBranchOrg = costreportOrderCostFinishDao.queryWdByOrgCodeNew(khgs);
	               if(customerBranchOrg.size()>0){
	            	  for(Map<String, Object> xjwd:customerBranchOrg){
	            	     xj.append(xjwd.get("wd").toString()).append(",");
	            	   }
	            	   xjNew = xj.toString();
	            	   xjNew = xj.substring(0, xjNew.length()-1);
	            	   paramMap.put("branchCodeAll", xjNew);
	               }
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}else{
				paramMap.put("branchCodeAll", user.getOrgCode());
			}
			}else{
				paramMap.put("branchCodeAll", user.getOrgCode());
			}			
		}
		
		
		 return costreportOrderCostFinishDao.count(paramMap);
	}
	
	@Override
	@DataSourceAnnotation
	public int save(CostreportOrderCostFinishDO costreportOrderCostFinish,String dsId){
		return costreportOrderCostFinishDao.save(costreportOrderCostFinish);
	}
	
	@Override
	@DataSourceAnnotation
	public int update(CostreportOrderCostFinishDO costreportOrderCostFinish,String dsId){
		return costreportOrderCostFinishDao.update(costreportOrderCostFinish);
	}
	
	@Override
	@DataSourceAnnotation
	public int remove(Integer recordId,String dsId){
		return costreportOrderCostFinishDao.remove(recordId);
	}
	
	@Override
	@DataSourceAnnotation
	public int batchRemove(Integer[] recordIds,String dsId){
		return costreportOrderCostFinishDao.batchRemove(recordIds);
	}

	@Override
	public List<Map<String, Object>> getProvinceCodeNameMap() {
		return costreportOrderCostFinishDao.getProvinceCodeNameMap();
	}

	@Override
	public List<Map<String, Object>> getCityCodeNameMap() {
		return costreportOrderCostFinishDao.getCityCodeNameMap();
	}

	@Override
	@DataSourceAnnotation
	public List<ExportCostreportOrderCostFinishDO> filterCustData(
			List<CostreportOrderCostFinishDO> reportTotaldata,String dsId) {
		List<ExportCostreportOrderCostFinishDO> totalDate = new ArrayList<ExportCostreportOrderCostFinishDO>();
		ExportCostreportOrderCostFinishDO newTotal = new ExportCostreportOrderCostFinishDO();
		for(CostreportOrderCostFinishDO data : reportTotaldata){			
	    		  newTotal = new ExportCostreportOrderCostFinishDO();  
	    		  newTotal.setCustomerId(data.getCustomerId());
	    		  newTotal.setCustomerName(data.getCustomerName());
	    		  newTotal.setOrderSum(data.getOrderSum());
	    		  newTotal.setWeightAll(data.getWeightAll());
	    		  newTotal.setIncome(data.getIncome());
	    		  newTotal.setExpenditure(data.getExpenditure());
	    		  newTotal.setProfit(data.getProfit());
	    		  newTotal.setIncomeEach(data.getIncomeEach());
	    		  newTotal.setKilogramEach(data.getKilogramEach());
	    		  newTotal.setProfitEach(data.getProfitEach());
	    		  newTotal.setKilogramProfitEach(data.getKilogramProfitEach());    		  
	    		  totalDate.add(newTotal);	    	  
		}
		return totalDate;
	}




	@Override
	public List<ExportCostreportOrderCostFinishDO> filterData(
			List<CostreportOrderCostFinishDO> costreportOrderCostFinishList) {
		List<ExportCostreportOrderCostFinishDO> totalDate = new ArrayList<ExportCostreportOrderCostFinishDO>();
		ExportCostreportOrderCostFinishDO newTotal = new ExportCostreportOrderCostFinishDO();
		for(CostreportOrderCostFinishDO data : costreportOrderCostFinishList){			
	    		  newTotal = new ExportCostreportOrderCostFinishDO();  
	    		  newTotal.setCustomerId(data.getCustomerId());
	    		 // newTotal.setAccountDt(data.getAccountDt());
	    		  newTotal.setCustomerName(data.getCustomerName());
	    		  newTotal.setOrderSum(data.getOrderSum());
	    		  newTotal.setWeightAll(data.getWeightAll());
	    		  newTotal.setIncome(data.getIncome());
	    		  newTotal.setExpenditure(data.getExpenditure());
	    		  newTotal.setProfit(data.getProfit());
	    		  newTotal.setIncomeEach(data.getIncomeEach());
	    		  newTotal.setKilogramEach(data.getKilogramEach());
	    		  newTotal.setProfitEach(data.getProfitEach());
	    		  newTotal.setKilogramProfitEach(data.getKilogramProfitEach());    		  
	    		  totalDate.add(newTotal);	    	  
		}
		return totalDate;
	}






}
