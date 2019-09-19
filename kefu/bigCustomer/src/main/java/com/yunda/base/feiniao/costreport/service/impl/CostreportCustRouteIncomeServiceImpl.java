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
import com.yunda.base.common.config.Constant;
import com.yunda.base.common.multi.annotation.DataSourceAnnotation;
import com.yunda.base.feiniao.costreport.bo.Bo_costreportCustRouteIncome_list;
import com.yunda.base.feiniao.costreport.dao.CostreportCustRouteIncomeDao;
import com.yunda.base.feiniao.costreport.domain.CostreportCustRouteIncomeDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportCRIncomeDetailDO;
import com.yunda.base.feiniao.costreport.domain.ExportCostreportCustRouteIncomeDO;
import com.yunda.base.feiniao.costreport.service.CostreportCustRouteIncomeService;
import com.yunda.base.feiniao.report.utils.CachePrefixConformity;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;
import com.yunda.base.system.dao.ProvinceDao;
import com.yunda.base.system.domain.ProvinceDO;
import com.yunda.ydmbspringbootstarter.common.utils.MD5Utils;


@Service
public class CostreportCustRouteIncomeServiceImpl implements CostreportCustRouteIncomeService {

	private final CostreportCustRouteIncomeDao costreportCustRouteIncomeDao;
	
	@Autowired
	private ProvinceDao provinceDao;
	
	private final RedisTemplate redisTemplate;
	
	
	@Autowired
	public CostreportCustRouteIncomeServiceImpl(
			CostreportCustRouteIncomeDao costreportCustRouteIncomeDao,
			RedisTemplate redisTemplate) {
		super();
		this.costreportCustRouteIncomeDao = costreportCustRouteIncomeDao;
		this.redisTemplate = redisTemplate;
	}

	@Override
	@DataSourceAnnotation
	public List<CostreportCustRouteIncomeDO> getIncomelist(
			Bo_costreportCustRouteIncome_list boCostreportCustRouteIncomeList,String dsId ) throws Exception {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("accountDt", boCostreportCustRouteIncomeList.getAccountDt());
		paramMap.put("customerId",boCostreportCustRouteIncomeList.getCustomerId());
		paramMap.put("startProvinceId", boCostreportCustRouteIncomeList.getStartProvinceId());
		paramMap.put("endProvinceId", boCostreportCustRouteIncomeList.getEndProvinceId());
		paramMap.put("offset",boCostreportCustRouteIncomeList.getOffset());
		paramMap.put("limit", boCostreportCustRouteIncomeList.getLimit());
		paramMap.put("test5","test4");
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<CostreportCustRouteIncomeDO>> operations = redisTemplate.opsForValue();
		String paramMapStrin  = JSON.toJSONString(paramMap);
	    String paramMapprefix = MD5Utils.encrypt(paramMapStrin);
	    List<CostreportCustRouteIncomeDO> dataList = operations.get(cache.getSeed(Constant.QUERYLBLSRBB+paramMapprefix,SuckCacheKeyPerfixEnum.costReportIncome.getCode()));
	    if(dataList == null || dataList.size() < 1){
	    	dataList = costreportCustRouteIncomeDao.getIncomelist(paramMap);
	    	operations.set(cache.getSeed(Constant.QUERYLBLSRBB+paramMapprefix,SuckCacheKeyPerfixEnum.costReportIncome.getCode()), dataList,86400, TimeUnit.SECONDS);
	    }
	    //客户名称 
		ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
		for(CostreportCustRouteIncomeDO data : dataList){
			if(data.getCustomerId() != null && !data.getCustomerId().isEmpty()){
				Map<String, Object> customerInfo = operationsCqkh.get(data.getCustomerId());
				if (customerInfo != null && customerInfo.get("khmc") != null) {
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
		CostreportCustRouteIncomeDO dataDO = new CostreportCustRouteIncomeDO();
		Integer orderSumTotal = 0;
		double weightTotal = 0.00;
		double feeTotal = 0.00;
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#0.00");
		for(CostreportCustRouteIncomeDO data :dataList ){
			Integer orderSum  = data.getOrderSum() == null ? 0 : data.getOrderSum();
			orderSumTotal +=orderSum;
			String weight = data.getWeight() == null ? "0.00" : data.getWeight().toString();
			weightTotal += Double.parseDouble(weight);
			String fee = data.getFee() == null ? "0.00" : data.getFee().toString();
			feeTotal += Double.parseDouble(fee);
		}
		
		dataDO.setOrderSum(orderSumTotal);
		dataDO.setWeight(new BigDecimal(df.format(weightTotal)));
		dataDO.setFee(new BigDecimal(df.format(feeTotal)));
		dataDO.setStartProvinceName("合计");
		dataList.add(0, dataDO);
		
		return dataList;
	}

	@Override
	@DataSourceAnnotation
	public int getIncomeCount(
			Bo_costreportCustRouteIncome_list boCostreportCustRouteIncomeList,String dsId) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("accountDt", boCostreportCustRouteIncomeList.getAccountDt());
		paramMap.put("customerId",boCostreportCustRouteIncomeList.getCustomerId());
		paramMap.put("branchCode", boCostreportCustRouteIncomeList.getBranchCode());
		paramMap.put("customerSourceType", boCostreportCustRouteIncomeList.getCustomerSourceType());
		paramMap.put("startProvinceId", boCostreportCustRouteIncomeList.getStartProvinceId());
		paramMap.put("endProvinceId", boCostreportCustRouteIncomeList.getEndProvinceId());
		return costreportCustRouteIncomeDao.getIncomeCount(paramMap);
	}

	@Override
	public List<ProvinceDO> getAllProvinces()
			throws Exception {
		List<ProvinceDO> provinceList = provinceDao.getAllProvinces();
		return provinceList;
	}

	@Override
	@DataSourceAnnotation
	public List<CostreportCustRouteIncomeDO> getIncomeDetailList(
			Bo_costreportCustRouteIncome_list boCostreportCustRouteIncomeList,String dsId)
			throws Exception {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("accountDt", boCostreportCustRouteIncomeList.getAccountDt());
		paramMap.put("customerId",boCostreportCustRouteIncomeList.getCustomerId());
		paramMap.put("branchCode", boCostreportCustRouteIncomeList.getBranchCode());
		paramMap.put("shipmentNo", boCostreportCustRouteIncomeList.getShipmentNo());
		paramMap.put("startProvinceId", boCostreportCustRouteIncomeList.getStartProvinceId());
		paramMap.put("endProvinceId", boCostreportCustRouteIncomeList.getEndProvinceId());
		paramMap.put("offset",boCostreportCustRouteIncomeList.getOffset());
		paramMap.put("limit", boCostreportCustRouteIncomeList.getLimit());
		paramMap.put("accountDtStart", boCostreportCustRouteIncomeList.getAccountDtStart());
		paramMap.put("accountDtEnd", boCostreportCustRouteIncomeList.getAccountDtEnd());
		paramMap.put("test5","test3");
		CachePrefixConformity cache = new CachePrefixConformity();
		ValueOperations<String, List<CostreportCustRouteIncomeDO>> operations = redisTemplate.opsForValue();
		String paramMapStrin  = JSON.toJSONString(paramMap);
	    String paramMapprefix = MD5Utils.encrypt(paramMapStrin);
	    List<CostreportCustRouteIncomeDO> datailList = operations.get(cache.getSeed(Constant.QUERYLBLSRMX+paramMapprefix,SuckCacheKeyPerfixEnum.costReportIncomeDetail.getCode()));
	    if(datailList == null || datailList.size() < 1){
	    	datailList = costreportCustRouteIncomeDao.getIncomeDetailList(paramMap);
	    	operations.set(cache.getSeed(Constant.QUERYLBLSRMX+paramMapprefix,SuckCacheKeyPerfixEnum.costReportIncomeDetail.getCode()), datailList,86400, TimeUnit.SECONDS);
	    }
	    //客户名称 
	  	ValueOperations<String, Map<String, Object>> operationsCqkh = redisTemplate.opsForValue();
		for(CostreportCustRouteIncomeDO data : datailList){
			if(data.getCustomerId() != null && !data.getCustomerId().isEmpty()){
				Map<String, Object> customerInfo = operationsCqkh.get(data.getCustomerId());
				if (customerInfo != null && customerInfo.get("khmc") != null) {
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
		return datailList;
	}

	@Override
	public List<ExportCostreportCustRouteIncomeDO> filterData(
			List<CostreportCustRouteIncomeDO> costreportCustRouteIncomeData) {

		List<ExportCostreportCustRouteIncomeDO> incomeData = new ArrayList<ExportCostreportCustRouteIncomeDO>();
		ExportCostreportCustRouteIncomeDO newIncome = new ExportCostreportCustRouteIncomeDO();
		
		for(CostreportCustRouteIncomeDO data : costreportCustRouteIncomeData){		
		    // if( data.getCustomerName() !=null&& !"".equals(data.getCustomerName())){
	    		 newIncome = new ExportCostreportCustRouteIncomeDO();
	    		 
	    		 newIncome.setCustomerId(data.getCustomerId());
	    		 newIncome.setCustomerName(data.getCustomerName());
	    		 newIncome.setStartProvinceName(data.getStartProvinceName());
	    		 newIncome.setEndProvinceName(data.getEndProvinceName());
	    		 newIncome.setOrderSum(data.getOrderSum());
	    		 newIncome.setWeight(data.getWeight());
	    		 newIncome.setFee(data.getFee());
	    		 
	    		 incomeData.add(newIncome);		    		 
		    
		     //}	 
		}      
		return incomeData;
	}

	@Override
	public List<ExportCostreportCRIncomeDetailDO> filterData2(
			List<CostreportCustRouteIncomeDO> costreportCustRouteIncomeData) {
		List<ExportCostreportCRIncomeDetailDO> detailData = new ArrayList<ExportCostreportCRIncomeDetailDO>();
		ExportCostreportCRIncomeDetailDO newDetail = new ExportCostreportCRIncomeDetailDO();
		
		for(CostreportCustRouteIncomeDO data : costreportCustRouteIncomeData){		
			
			newDetail = new ExportCostreportCRIncomeDetailDO();
			
			newDetail.setSenderAccountDt(data.getSenderAccountDt());
			newDetail.setOrderAccountDt(data.getOrderAccountDt());
			newDetail.setShipmentNo(data.getShipmentNo());
			newDetail.setStartProvinceName(data.getStartProvinceName());
			newDetail.setEndProvinceName(data.getEndProvinceName());
			newDetail.setWeight(data.getWeight());
			newDetail.setFee(data.getFee());
	    		 
			detailData.add(newDetail);		    		 
		}      
		return detailData;
	}

	@Override
	@DataSourceAnnotation
	public int getIncomeDetailCount(
			Bo_costreportCustRouteIncome_list boCostreportCustRouteIncomeList,String dsId) {
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("accountDt", boCostreportCustRouteIncomeList.getAccountDt());
		paramMap.put("customerId",boCostreportCustRouteIncomeList.getCustomerId());
		paramMap.put("branchCode", boCostreportCustRouteIncomeList.getBranchCode());
		//paramMap.put("customerSourceType", boCostreportCustRouteIncomeList.getCustomerSourceType());
		paramMap.put("shipmentNo", boCostreportCustRouteIncomeList.getShipmentNo());
		paramMap.put("startProvinceId", boCostreportCustRouteIncomeList.getStartProvinceId());
		paramMap.put("endProvinceId", boCostreportCustRouteIncomeList.getEndProvinceId());
		paramMap.put("accountDtStart", boCostreportCustRouteIncomeList.getAccountDtStart());
		paramMap.put("accountDtEnd", boCostreportCustRouteIncomeList.getAccountDtEnd());
		return costreportCustRouteIncomeDao.getIncomeDetailCount(paramMap);
	}

	
	
}
