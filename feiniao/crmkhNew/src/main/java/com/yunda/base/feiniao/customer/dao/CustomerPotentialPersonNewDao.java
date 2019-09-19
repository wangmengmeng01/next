package com.yunda.base.feiniao.customer.dao;

import com.yunda.base.feiniao.customer.domain.CustomerPotentialPersonNewDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 潜在客户新表
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-12-14173416
 */
@Mapper
public interface CustomerPotentialPersonNewDao {

	CustomerPotentialPersonNewDO get(Integer recordId);
	
	List<CustomerPotentialPersonNewDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int countwd(String branchcode);
	
	int save(CustomerPotentialPersonNewDO customerPotentialPersonNew);
	
	int update(CustomerPotentialPersonNewDO customerPotentialPersonNew);
	
	int remove(Integer record_id);
	
	int batchRemove(Integer[] recordIds);

	List<CustomerPotentialPersonNewDO> searchYdserver(String branchcode);

	List<Map<String, Object>> searchCustomerName();

}
