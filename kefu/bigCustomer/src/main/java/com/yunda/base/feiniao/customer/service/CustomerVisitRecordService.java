package com.yunda.base.feiniao.customer.service;

import java.util.List;
import java.util.Map;

import com.yunda.base.feiniao.customer.domain.CustomerVisitRecordDO;

/**
 * 客户拜访记录表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-10-31144058
 */
public interface CustomerVisitRecordService {
	
	CustomerVisitRecordDO get(Integer recordId);
	
	List<CustomerVisitRecordDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CustomerVisitRecordDO customerVisitRecord);
	
	int update(CustomerVisitRecordDO customerVisitRecord);
	
	int remove(Integer recordId);
	
	int batchRemove(Integer[] recordIds);
}
