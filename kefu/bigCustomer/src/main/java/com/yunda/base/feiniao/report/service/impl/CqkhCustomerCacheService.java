package com.yunda.base.feiniao.report.service.impl;

import java.util.Map;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.yunda.base.feiniao.report.core.CRM_constants;

/**
 * cqkh信息缓存。
 * 
 * @author bxl
 * @since 3.3.7_0720, 2018
 */
@Service
public class CqkhCustomerCacheService {
	
	/**
	 * 
	 * 根据公司+编码的ID在缓存中查询用户信息。
	 * 
	 * @param customerId gs+bm
	 * @return
	 * @author bxl
	 * @since 1.1.0_0720, 2018
	 */
	@Cacheable(value=CRM_constants.CACHE_IN_REDIS, key="'CustomerInfo@'.concat(#customerId!=null?#customerId:'')")
	public Map<String, Object> getCustomerInfo(String customerId) {
		return null;
	}
	
	/**
	 * 
	 * 缓存中放置用户信息。
	 * 
	 * @param customerId gs+bm
	 * @param customerInfo
	 * @return
	 * @author bxl
	 * @since 1.1.0_0720, 2018
	 */
	@CachePut(value=CRM_constants.CACHE_IN_REDIS, key="'CustomerInfo@'.concat(#customerId!=null?#customerId:'')")
	public Map<String, Object> setTotalReportStatus(String customerId, Map<String, Object> customerInfo) {
		return customerInfo;
	}
}
