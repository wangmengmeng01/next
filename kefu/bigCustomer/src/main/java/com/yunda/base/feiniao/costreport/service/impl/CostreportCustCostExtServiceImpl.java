package com.yunda.base.feiniao.costreport.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.yunda.base.common.config.Constant;
import com.yunda.base.common.multi.annotation.DataSourceAnnotation;
import com.yunda.base.feiniao.costreport.bo.Bo_costreportCustCostExt;
import com.yunda.base.feiniao.costreport.dao.CostreportCustCostExtDao;
import com.yunda.base.feiniao.costreport.domain.CostreportCustCostExtDO;
import com.yunda.base.feiniao.costreport.domain.CostreportOrderCostDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportCustCostExtDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportOrderCostDO;
import com.yunda.base.feiniao.costreport.service.CostreportCustCostExtService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;


@Service
public class CostreportCustCostExtServiceImpl implements CostreportCustCostExtService {
	
	private final CostreportCustCostExtDao costreportCustCostExtDao;		
	private final RedisTemplate redisTemplate;		
	@Autowired
	public CostreportCustCostExtServiceImpl(
			CostreportCustCostExtDao costreportCustCostExtDao,
			RedisTemplate redisTemplate) {
		super();
		this.costreportCustCostExtDao = costreportCustCostExtDao;
		this.redisTemplate = redisTemplate;
	}

	@Override
	@DataSourceAnnotation
	public CostreportCustCostExtDO get(Integer recordId,String dsId){
		return costreportCustCostExtDao.get(recordId);
	}
	
	@Override
	@DataSourceAnnotation
	public List<CostreportOrderCostDO> list(Map<String, Object> map,String dsId){
		List<CostreportOrderCostDO> list = costreportCustCostExtDao.listDetail(map);
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
		for (CostreportOrderCostDO data : list) {		
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
		 		
			String result =Constant.province_code_name.get(data.getStartProvinceId()+"")+"";
			String result1 =Constant.province_code_name.get(data.getEndProvinceId()+"")+"";
			data.setStartProvinceName(result);
			data.setEndProvinceName(result1);
	    }
		return list;
	}
	
	@Override
	@DataSourceAnnotation
	public List<CostreportOrderCostDO> list(
			Bo_costreportCustCostExt boCostreportCustCostExt, String dsId) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("accountDt", boCostreportCustCostExt.getAccountDt());
		paramMap.put("customerId",boCostreportCustCostExt.getCustomerId());
		paramMap.put("startProvinceId", boCostreportCustCostExt.getStartProvinceId());
		paramMap.put("endProvinceId", boCostreportCustCostExt.getEndProvinceId());
		paramMap.put("shipmentNo", boCostreportCustCostExt.getShipmentNo());
		paramMap.put("accountDtStart", (boCostreportCustCostExt.getAccountDt().substring(0, 4)+"-"+boCostreportCustCostExt.getAccountDt().substring(4)+"-01"));       
		paramMap.put("accountDtEnd", DateUtils.getMonthEnd(paramMap.get("accountDtStart")+""));  
		paramMap.put("offset",boCostreportCustCostExt.getOffset());
		paramMap.put("limit", boCostreportCustCostExt.getLimit());
		paramMap.put("test1","test3");
		List<CostreportOrderCostDO> list = costreportCustCostExtDao.listDetail(paramMap);
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
		for (CostreportOrderCostDO data : list) {		
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
			String result =Constant.province_code_name.get(data.getStartProvinceId()+"")+"";
			String result1 =Constant.province_code_name.get(data.getEndProvinceId()+"")+"";
			data.setStartProvinceName(result);
			data.setEndProvinceName(result1);
	    }
		return list;
	}
	
	@Override
	@DataSourceAnnotation
	public int count(Bo_costreportCustCostExt boCostreportCustCostExt,String dsId){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("accountDt", boCostreportCustCostExt.getAccountDt());
		paramMap.put("customerId",boCostreportCustCostExt.getCustomerId());
		paramMap.put("startProvinceId", boCostreportCustCostExt.getStartProvinceId());
		paramMap.put("endProvinceId", boCostreportCustCostExt.getEndProvinceId());
		paramMap.put("shipmentNo", boCostreportCustCostExt.getShipmentNo());
		paramMap.put("accountDtStart", (boCostreportCustCostExt.getAccountDt().substring(0, 4)+"-"+boCostreportCustCostExt.getAccountDt().substring(4)+"-01"));       
		paramMap.put("accountDtEnd", DateUtils.getMonthEnd(paramMap.get("accountDtStart")+""));  
		return costreportCustCostExtDao.count(paramMap);
	}
	
	@Override
	@DataSourceAnnotation
	public int count(Map<String, Object> map,String dsId){
		return costreportCustCostExtDao.count(map);
	}
	
	@Override
	@DataSourceAnnotation
	public int save(CostreportCustCostExtDO costreportCustCostExt,String dsId){
		return costreportCustCostExtDao.save(costreportCustCostExt);
	}
	
	@Override
	@DataSourceAnnotation
	public int update(CostreportCustCostExtDO costreportCustCostExt,String dsId){
		return costreportCustCostExtDao.update(costreportCustCostExt);
	}
	
	@Override
	@DataSourceAnnotation
	public int remove(Integer recordId,String dsId){
		return costreportCustCostExtDao.remove(recordId);
	}
	
	@Override
	@DataSourceAnnotation
	public int batchRemove(Integer[] recordIds,String dsId){
		return costreportCustCostExtDao.batchRemove(recordIds);
	}
	
	@Override
	@DataSourceAnnotation
	public void groupCostExtData(Date date,String dsId){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01");
		costreportCustCostExtDao.groupCostExtData(sdf.format(date));
	}

	@Override
	@DataSourceAnnotation
	public List<ExportCostreportOrderCostDO> filterCustData(
			List<CostreportOrderCostDO> reportTotaldata,String dsId) {
		List<ExportCostreportOrderCostDO> totalDate = new ArrayList<ExportCostreportOrderCostDO>();
		ExportCostreportOrderCostDO newTotal = new ExportCostreportOrderCostDO();
		for(CostreportOrderCostDO data : reportTotaldata){			
//	      if( data.getCustomerId() !=null&& !"".equals(data.getCustomerId())){	    	  
	    		  newTotal = new ExportCostreportOrderCostDO();  
	    		  newTotal.setSenderAccountDt(data.getSenderAccountDt());
	    		  newTotal.setTdAccountDt(data.getTdAccountDt());
	    		  newTotal.setAccountDt(data.getAccountDt());
	    		  newTotal.setCustomerId(data.getCustomerId());
	    		  newTotal.setCustomerName(data.getCustomerName());
	    		  newTotal.setShipmentNo(data.getShipmentNo());
	    		  newTotal.setStartProvinceName(data.getStartProvinceName());
	    		  newTotal.setEndProvinceName(data.getEndProvinceName());
	    		  newTotal.setWeight(data.getWeight());
	    		  newTotal.setTsfFee(data.getTsfFee());
	    		  newTotal.setDeliveryFee(data.getDeliveryFee());
	    		  newTotal.setDeliveryAdditionalWeightFee(data.getDeliveryAdditionalWeightFee());
	    		  newTotal.setDeliveryBalanceFee(data.getDeliveryBalanceFee());
	    		  newTotal.setShipmentFee(data.getShipmentFee());
	    		  newTotal.setScanFee(data.getScanFee());
	    		  totalDate.add(newTotal);	    	  
//	      }		
		}
		return totalDate;
	}

	@Override
	public List<ExportCostreportCustCostExtDO> filterData(
			List<CostreportOrderCostDO> costreportCustCostExtList) {

		List<ExportCostreportCustCostExtDO> custCostExtList = new ArrayList<ExportCostreportCustCostExtDO>();
		ExportCostreportCustCostExtDO newCustCostExt = new ExportCostreportCustCostExtDO();
		
		for(CostreportOrderCostDO data : costreportCustCostExtList){		
			newCustCostExt = new ExportCostreportCustCostExtDO();
			newCustCostExt.setSenderAccountDt(data.getSenderAccountDt());
			newCustCostExt.setTdAccountDt(data.getTdAccountDt());
			newCustCostExt.setAccountDt(data.getAccountDt());
			newCustCostExt.setCustomerId(data.getCustomerId());
			newCustCostExt.setCustomerName(data.getCustomerName());
			newCustCostExt.setShipmentNo(data.getShipmentNo());
			newCustCostExt.setStartProvinceName(data.getStartProvinceName());
			newCustCostExt.setEndProvinceName(data.getEndProvinceName());
			newCustCostExt.setWeight(data.getWeight());
			newCustCostExt.setTsfFee(data.getTsfFee());
			newCustCostExt.setDeliveryFee(data.getDeliveryFee());
			newCustCostExt.setDeliveryAdditionalWeightFee(data.getDeliveryAdditionalWeightFee());
			newCustCostExt.setDeliveryBalanceFee(data.getDeliveryBalanceFee());
			newCustCostExt.setShipmentFee(data.getShipmentFee());
			newCustCostExt.setScanFee(data.getScanFee());
			
			
	    		 
	
			custCostExtList.add(newCustCostExt);		    		 
		    
		}      
		return custCostExtList;
	}


	
}
