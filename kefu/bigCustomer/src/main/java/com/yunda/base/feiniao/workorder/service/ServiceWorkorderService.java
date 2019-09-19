package com.yunda.base.feiniao.workorder.service;

import java.util.List;
import java.util.Map;

import com.yunda.base.feiniao.workorder.domain.ServiceWorkorderDO;

/**
 * 客户咨询单管理/客户咨询单
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-06-29 09:20:45
 */
public interface ServiceWorkorderService {
	
	ServiceWorkorderDO get(Integer workOrderId);
	
	List<ServiceWorkorderDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(ServiceWorkorderDO serviceWorkorder);
	
	int update(ServiceWorkorderDO serviceWorkorder);
	
	int remove(Integer workOrderId);
	
	int batchRemove(Integer[] workOrderIds);
}
