package com.yunda.base.feiniao.workorder.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.feiniao.workorder.domain.ServiceWorkorderDO;

/**
 * 客户咨询单管理/客户咨询单
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-06-29 09:20:45
 */
@Mapper
public interface ServiceWorkorderDao {

	ServiceWorkorderDO get(Integer workOrderId);
	
	List<ServiceWorkorderDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ServiceWorkorderDO serviceWorkorder);
	
	int update(ServiceWorkorderDO serviceWorkorder);
	
	int remove(Integer work_order_id);
	
	int batchRemove(Integer[] workOrderIds);
}
