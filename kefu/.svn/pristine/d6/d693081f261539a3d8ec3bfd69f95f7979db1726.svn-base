package com.yunda.base.feiniao.customer.service;

import com.yunda.base.feiniao.customer.bo.Bo_CustomerPotentialPersonNew;
import com.yunda.base.feiniao.customer.domain.CustomerPotentialPersonNewDO;
import com.yunda.base.feiniao.customer.domain.ExportExcelCustomerPotentialPersonNewDO;
import com.yunda.base.system.domain.UserDO;

import java.util.List;
import java.util.Map;

/**
 * 潜在客户新表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-12-14173416
 */
public interface CustomerPotentialPersonNewService {
	
	CustomerPotentialPersonNewDO get(Integer recordId);
	
	List<CustomerPotentialPersonNewDO> list(Bo_CustomerPotentialPersonNew boCustomerPotentialPersonNew, UserDO loginUser);
	
	int count(Bo_CustomerPotentialPersonNew boCustomerPotentialPersonNew, UserDO loginUser);
	
	int countwd(String branchcode);
	
	int save(CustomerPotentialPersonNewDO customerPotentialPersonNew, UserDO loginUser);
	
	int update(CustomerPotentialPersonNewDO customerPotentialPersonNew, UserDO loginUser);
	
	int remove(Integer recordId);
	
	int batchRemove(Integer[] recordIds);

	boolean checkData(CustomerPotentialPersonNewDO row, UserDO loginUser);

	List<ExportExcelCustomerPotentialPersonNewDO> filterData(
			List<CustomerPotentialPersonNewDO> customerPotentialPersonNewlist);

	List<Map<String, Object>> searchCustomerName();

	
}
