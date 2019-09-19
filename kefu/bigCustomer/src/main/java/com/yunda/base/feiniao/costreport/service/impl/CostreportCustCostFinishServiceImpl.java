package com.yunda.base.feiniao.costreport.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Objects;
import com.yunda.base.common.config.Constant;
import com.yunda.base.common.multi.annotation.DataSourceAnnotation;
import com.yunda.base.feiniao.costreport.bo.Bo_CostreportCustCostFinish_two;
import com.yunda.base.feiniao.costreport.dao.CostreportCustCostFinishDao;
import com.yunda.base.feiniao.costreport.domain.CostreportCustCostFinishDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportCustCostFinishDO;
import com.yunda.base.feiniao.costreport.service.CostreportCustCostFinishService;
import com.yunda.base.feiniao.report.utils.CachePrefixConformity;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;


@Service
public class CostreportCustCostFinishServiceImpl implements CostreportCustCostFinishService {
	
	private final CostreportCustCostFinishDao costreportCustCostFinishDao;	
	private final RedisTemplate redisTemplate;
	
	@Autowired
	public CostreportCustCostFinishServiceImpl(
			CostreportCustCostFinishDao costreportCustCostFinishDao,
			RedisTemplate redisTemplate) {
		super();
		this.costreportCustCostFinishDao = costreportCustCostFinishDao;
		this.redisTemplate = redisTemplate;
	}
	
	@Override
	@DataSourceAnnotation
	public CostreportCustCostFinishDO get(Integer recordId,String dsId){
		return costreportCustCostFinishDao.get(recordId);
	}
	
	@Override
	@DataSourceAnnotation
	public List<CostreportCustCostFinishDO> list(Map<String, Object> map,String dsId){
		
		 CachePrefixConformity cache = new CachePrefixConformity();
		 ValueOperations<String,List<CostreportCustCostFinishDO>> operations = redisTemplate.opsForValue();
		 map.put("test", "test6");
		 String cacheString = JSON.toJSONString(map);
		 
		 
		 String cacheResult = MD5Utils.encrypt(cacheString);
//		  从redis里查询是否有缓存数据
		List<CostreportCustCostFinishDO> searchCache = operations.get(cache.getSeed(Constant.QUERYCUSTREPORT+cacheResult,SuckCacheKeyPerfixEnum.custReport.getCode()));	 
		if(searchCache==null||searchCache.size()<1){
		List<CostreportCustCostFinishDO> list = costreportCustCostFinishDao.list(map);	
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
		for (CostreportCustCostFinishDO data : list) {		
			if(data.getCustomerId() != null && !data.getCustomerId().isEmpty()){
				Map<String, Object> customerInfo = operationsCqkh.get(data.getCustomerId());
				if(customerInfo != null && customerInfo.get("khmc") != null) {
					data.setCustomerName(customerInfo.get("khmc").toString());						
				}
			}
			//菜鸟和京东库取商家id和店铺名称
			if("1".equals(data.getCustomerSourceType())){
				String customerId = data.getCustomerId()+"cn";
				Map<String, Object> sellerCNInfo = operationsCqkh.get(customerId);
				if(sellerCNInfo != null && sellerCNInfo.get("seller_id") != null) {
					data.setSellerId(sellerCNInfo.get("seller_id").toString());
					data.setSellerName(sellerCNInfo.get("seller_name").toString());
				}
			}
			if("4".equals(data.getCustomerSourceType())){
				String customerId = data.getCustomerId()+"jd";
				Map<String, Object> sellerJDInfo = operationsCqkh.get(customerId);
				if(sellerJDInfo != null && sellerJDInfo.get("vendor_code") != null) {
					data.setSellerId(sellerJDInfo.get("vendor_code").toString());
					data.setSellerName(sellerJDInfo.get("vendor_name").toString());
				}
			}
			String resultStart =Constant.province_code_name.get(data.getStartProvinceId()+"")+"";
			String resultEnd =Constant.province_code_name.get(data.getEndProvinceId()+"")+"";
			data.setStartProvinceName(Objects.equal(resultStart, "null")?"-":resultStart);
			data.setEndProvinceName(Objects.equal(resultEnd, "null")?"-":resultEnd);
	    }
		 List<CostreportCustCostFinishDO> resultOrderCostTotal = costreportCustCostFinishDao.listTotal(map);
		 if(resultOrderCostTotal.size()>0&&list.size()>0){
			 resultOrderCostTotal.get(0).setStartProvinceName("首行合计");
			 list.addAll(0, resultOrderCostTotal);
			 operations.set(cache.getSeed(Constant.QUERYCUSTREPORT+cacheResult,SuckCacheKeyPerfixEnum.custReport.getCode()), list,86400, TimeUnit.SECONDS);
		 }	 
		 return list;
	}
		
		return searchCache;
	}
	
	

	
	@Override
	@DataSourceAnnotation
	public List<CostreportCustCostFinishDO> list(Bo_CostreportCustCostFinish_two boCostreportCustCostFinishTwo,String dsId){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("accountDt", boCostreportCustCostFinishTwo.getAccountDt());
		paramMap.put("customerId",boCostreportCustCostFinishTwo.getCustomerId());
		paramMap.put("startProvinceId", boCostreportCustCostFinishTwo.getStartProvinceId());
		paramMap.put("endProvinceId", boCostreportCustCostFinishTwo.getEndProvinceId());
		paramMap.put("accountDtExt", (boCostreportCustCostFinishTwo.getAccountDt().substring(0, 4)+"-"+(boCostreportCustCostFinishTwo.getAccountDt().substring(4)+"-01")));   
		paramMap.put("offset",boCostreportCustCostFinishTwo.getOffset());
		paramMap.put("limit", boCostreportCustCostFinishTwo.getLimit());
		paramMap.put("test4","test3");
		 CachePrefixConformity cache = new CachePrefixConformity();
		 ValueOperations<String,List<CostreportCustCostFinishDO>> operations = redisTemplate.opsForValue();
		 String cacheString = JSON.toJSONString(paramMap);
		 String cacheResult = MD5Utils.encrypt(cacheString);
		 List<CostreportCustCostFinishDO> list = null;
//		  从redis里查询是否有缓存数据
		List<CostreportCustCostFinishDO> searchCache = operations.get(cache.getSeed(Constant.QUERYCUSTREPORT+cacheResult,SuckCacheKeyPerfixEnum.custReport.getCode()));	 
		if(searchCache==null||searchCache.size()<1){
		list = costreportCustCostFinishDao.list(paramMap);	
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
		for (CostreportCustCostFinishDO data : list) {		
			if(data.getCustomerId() != null && !data.getCustomerId().isEmpty()){
				Map<String, Object> customerInfo = operationsCqkh.get(data.getCustomerId());
				if(customerInfo != null && customerInfo.get("khmc") != null) {
					data.setCustomerName(customerInfo.get("khmc").toString());						
				}
			}
			//菜鸟和京东库取商家id和店铺名称
			if("1".equals(data.getCustomerSourceType())){
				String customerId = data.getCustomerId()+"cn";
				Map<String, Object> sellerCNInfo = operationsCqkh.get(customerId);
				if(sellerCNInfo != null && sellerCNInfo.get("seller_id") != null) {
					data.setSellerId(sellerCNInfo.get("seller_id").toString());
					data.setSellerName(sellerCNInfo.get("seller_name").toString());
				}
			}
			if("4".equals(data.getCustomerSourceType())){
				String customerId = data.getCustomerId()+"jd";
				Map<String, Object> sellerJDInfo = operationsCqkh.get(customerId);
				if(sellerJDInfo != null && sellerJDInfo.get("vendor_code") != null) {
					data.setSellerId(sellerJDInfo.get("vendor_code").toString());
					data.setSellerName(sellerJDInfo.get("vendor_name").toString());
				}
			}
			String resultStart =Constant.province_code_name.get(data.getStartProvinceId()+"")+"";
			String resultEnd =Constant.province_code_name.get(data.getEndProvinceId()+"")+"";
			data.setStartProvinceName(Objects.equal(resultStart, "null")?"-":resultStart);
			data.setEndProvinceName(Objects.equal(resultEnd, "null")?"-":resultEnd);
	    }
		Integer orderSumtotal= 0;
		double weighttotal = 0.00;
		double tsfFeetotal = 0.00;
		double deliveryFeetotal = 0.00;
		double deliveryAdditionalWeightFeetotal = 0.00;
		double deliveryBalanceFeetotal = 0.00;
		double shipmentFeetotal = 0.00;
		double scanFeetotal = 0.00;
		double peopleCosttotal = 0.00;
		double materielCosttotal = 0.00;
		double tsfCosttotal = 0.00;
		double hhCosttotal = 0.00;
		double taxesCosttotal = 0.00;
		double packingChargetotal = 0.00;
		double otherCosttotal = 0.00;
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#0.00");
		for(CostreportCustCostFinishDO data :list ){
			orderSumtotal += data.getOrderSum();
			String weight = data.getWeight() == null ? "0.00" : data.getWeight().toString();
			weighttotal += Double.parseDouble(weight);
			String tsfFee = data.getTsfFee() == null ? "0.00" : data.getTsfFee().toString();
			tsfFeetotal += Double.parseDouble(tsfFee);
			String deliveryFee = data.getDeliveryFee() == null ? "0.00" : data.getDeliveryFee().toString();
			deliveryFeetotal += Double.parseDouble(deliveryFee);
			String deliveryAdditionalWeightFee = data.getDeliveryAdditionalWeightFee() == null ? "0.00" : data.getDeliveryAdditionalWeightFee().toString();
			deliveryAdditionalWeightFeetotal += Double.parseDouble(deliveryAdditionalWeightFee);
			String deliveryBalanceFee = data.getDeliveryBalanceFee()== null ? "0.00" : data.getDeliveryBalanceFee().toString();
			deliveryBalanceFeetotal += Double.parseDouble(deliveryBalanceFee);
			String shipmentFee = data.getShipmentFee()== null ? "0.00" : data.getShipmentFee().toString();
			shipmentFeetotal += Double.parseDouble(shipmentFee);
			String scanFee = data.getScanFee()== null ? "0.00" : data.getScanFee().toString();
			scanFeetotal += Double.parseDouble(scanFee);
			String peopleCost = data.getPeopleCost()== null ? "0.00" : data.getPeopleCost().toString();
			peopleCosttotal += Double.parseDouble(peopleCost);
			String materielCost = data.getMaterielCost()== null ? "0.00" : data.getMaterielCost().toString();
			materielCosttotal += Double.parseDouble(materielCost);
			String tsfCost = data.getTsfCost()== null ? "0.00" : data.getTsfCost().toString();
			tsfCosttotal += Double.parseDouble(tsfCost);
			String hhCost = data.getHhCost()== null ? "0.00" : data.getHhCost().toString();
			hhCosttotal += Double.parseDouble(hhCost);
			String taxesCost = data.getTaxesCost()== null ? "0.00" : data.getTaxesCost().toString();
			taxesCosttotal += Double.parseDouble(taxesCost);
			String packingCharge = data.getPackingCharge()== null ? "0.00" : data.getPackingCharge().toString();
			packingChargetotal += Double.parseDouble(packingCharge);
			String otherCost = data.getPackingCharge()== null ? "0.00" : data.getPackingCharge().toString();
			otherCosttotal += Double.parseDouble(otherCost);
		}
		CostreportCustCostFinishDO custCostFinishDO = new CostreportCustCostFinishDO();
		custCostFinishDO.setOrderSum(orderSumtotal);
		custCostFinishDO.setWeight(new BigDecimal(df.format(weighttotal)));
		custCostFinishDO.setTsfFee(new BigDecimal(df.format(tsfFeetotal)));
		custCostFinishDO.setDeliveryFee(new BigDecimal(df.format(deliveryFeetotal))); 
		custCostFinishDO.setDeliveryAdditionalWeightFee(new BigDecimal(df.format(deliveryAdditionalWeightFeetotal)));
		custCostFinishDO.setDeliveryBalanceFee(new BigDecimal(df.format(deliveryBalanceFeetotal)));
		custCostFinishDO.setShipmentFee(new BigDecimal(df.format(shipmentFeetotal)));
		custCostFinishDO.setScanFee(new BigDecimal(df.format(scanFeetotal)));
		custCostFinishDO.setPeopleCost(new BigDecimal(df.format(peopleCosttotal)));
		custCostFinishDO.setMaterielCost(new BigDecimal(df.format(materielCosttotal)));
		custCostFinishDO.setTsfCost(new BigDecimal(df.format(tsfCosttotal)));
		custCostFinishDO.setHhCost(new BigDecimal(df.format(hhCosttotal)));
		custCostFinishDO.setTaxesCost(new BigDecimal(df.format(taxesCosttotal)));
		custCostFinishDO.setPackingCharge(new BigDecimal(df.format(packingChargetotal)));
		custCostFinishDO.setOtherCost(new BigDecimal(df.format(otherCosttotal)));
		custCostFinishDO.setStartProvinceName("首行合计");
		
		list.add(0, custCostFinishDO);
/*		 //查询到的所有数据守行合计
		 List<CostreportCustCostFinishDO> resultOrderCostTotal = costreportCustCostFinishDao.listTotal(paramMap);
		 if(resultOrderCostTotal.size()>0&&list.size()>0){
			 resultOrderCostTotal.get(0).setStartProvinceName("首行合计");
			 list.addAll(0, resultOrderCostTotal);
			 operations.set(cache.getSeed(Constant.QUERYCUSTREPORT+cacheResult,SuckCacheKeyPerfixEnum.custReport.getCode()), list,86400, TimeUnit.SECONDS);
		 }*/	 
		 return list;
	}
		
		return searchCache;
	}
	
	@Override
	@DataSourceAnnotation
	public int count(Bo_CostreportCustCostFinish_two boCostreportCustCostFinishTwo,String dsId){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("accountDt", boCostreportCustCostFinishTwo.getAccountDt());
		paramMap.put("customerId",boCostreportCustCostFinishTwo.getCustomerId());
		paramMap.put("startProvinceId", boCostreportCustCostFinishTwo.getStartProvinceId());
		paramMap.put("endProvinceId", boCostreportCustCostFinishTwo.getEndProvinceId());
		paramMap.put("accountDtExt", (boCostreportCustCostFinishTwo.getAccountDt().substring(0, 4)+"-"+(boCostreportCustCostFinishTwo.getAccountDt().substring(4)+"-01")));   
		return costreportCustCostFinishDao.count(paramMap);
	}
	
	@Override
	@DataSourceAnnotation
	public int count(Map<String, Object> map,String dsId){
		return costreportCustCostFinishDao.count(map);
	}
	
	@Override
	@DataSourceAnnotation
	public int save(CostreportCustCostFinishDO costreportCustCostFinish,String dsId){
		return costreportCustCostFinishDao.save(costreportCustCostFinish);
	}
	
	@Override
	@DataSourceAnnotation
	public int update(CostreportCustCostFinishDO costreportCustCostFinish,String dsId){
		return costreportCustCostFinishDao.update(costreportCustCostFinish);
	}
	
	@Override
	@DataSourceAnnotation
	public int remove(Integer recordId,String dsId){
		return costreportCustCostFinishDao.remove(recordId);
	}
	
	@Override
	@DataSourceAnnotation
	public int batchRemove(Integer[] recordIds,String dsId){
		return costreportCustCostFinishDao.batchRemove(recordIds);
	}
	
	@Override
	@DataSourceAnnotation
	public List<ExportCostreportCustCostFinishDO> filterCustData(
		List<CostreportCustCostFinishDO> reportTotaldata,String dsId) {
		List<ExportCostreportCustCostFinishDO> totalDate = new ArrayList<ExportCostreportCustCostFinishDO>();
		ExportCostreportCustCostFinishDO newTotal = new ExportCostreportCustCostFinishDO();
		for(CostreportCustCostFinishDO data : reportTotaldata){			
//	      if( data.getCustomerId() !=null&& !"".equals(data.getCustomerId())){	    	  
	    		  newTotal = new ExportCostreportCustCostFinishDO();  
	    		  newTotal.setCustomerId(data.getCustomerId());
	    		  newTotal.setCustomerName(data.getCustomerName());
	    		  newTotal.setStartProvinceName(data.getStartProvinceName());
	    		  newTotal.setEndProvinceName(data.getEndProvinceName());
	    		  newTotal.setOrderSum(data.getOrderSum());
	    		  newTotal.setWeight(data.getWeight());
	    		  newTotal.setTsfFee(data.getTsfFee());
	    		  newTotal.setDeliveryFee(data.getDeliveryFee());
	    		  newTotal.setDeliveryAdditionalWeightFee(data.getDeliveryAdditionalWeightFee());
	    		  newTotal.setDeliveryBalanceFee(data.getDeliveryBalanceFee());
	    		  newTotal.setShipmentFee(data.getShipmentFee());
	    		  newTotal.setScanFee(data.getScanFee());
	    		  newTotal.setPeopleCost(data.getPeopleCost());
	    		  newTotal.setMaterielCost(data.getMaterielCost());
	    		  newTotal.setTsfCost(data.getTsfCost());
	    		  newTotal.setHhCost(data.getHhCost());
	    		  newTotal.setTaxesCost(data.getTaxesCost());
	    		  newTotal.setPackingCharge(data.getPackingCharge());
	    		  newTotal.setOtherCost(data.getOtherCost());	    		  
	    		  totalDate.add(newTotal);	    	  
//	      }		
		}
		return totalDate;
	}

	@Override
	public List<ExportCostreportCustCostFinishDO> filterData(
			List<CostreportCustCostFinishDO> costreportCustCostFinishList) {

		List<ExportCostreportCustCostFinishDO> custCostData = new ArrayList<ExportCostreportCustCostFinishDO>();
		ExportCostreportCustCostFinishDO newCustCost = new ExportCostreportCustCostFinishDO();
		
		for(CostreportCustCostFinishDO data : costreportCustCostFinishList){		
				 newCustCost = new ExportCostreportCustCostFinishDO();
	    		 
				 newCustCost.setCustomerId(data.getCustomerId());
				 newCustCost.setCustomerName(data.getCustomerName());
				 newCustCost.setStartProvinceName(data.getStartProvinceName());
				 newCustCost.setEndProvinceName(data.getEndProvinceName());
				 newCustCost.setOrderSum(data.getOrderSum());
				 newCustCost.setWeight(data.getWeight());
				 newCustCost.setTsfFee(data.getTsfFee());
				 newCustCost.setDeliveryFee(data.getDeliveryFee());
				 newCustCost.setDeliveryAdditionalWeightFee(data.getDeliveryAdditionalWeightFee());
				 newCustCost.setDeliveryBalanceFee(data.getDeliveryBalanceFee());
				 newCustCost.setShipmentFee(data.getShipmentFee());
				 newCustCost.setScanFee(data.getScanFee());
				 newCustCost.setPeopleCost(data.getPeopleCost());
				 newCustCost.setMaterielCost(data.getMaterielCost());
				 newCustCost.setTsfCost(data.getTsfCost());
				 newCustCost.setHhCost(data.getHhCost());
				 newCustCost.setTaxesCost(data.getTaxesCost());
				 newCustCost.setPackingCharge(data.getPackingCharge());
				 newCustCost.setOtherCost(data.getOtherCost());
	    		 
				 custCostData.add(newCustCost);		    		 
		    
		}      
		return custCostData;
	}	
	
}
