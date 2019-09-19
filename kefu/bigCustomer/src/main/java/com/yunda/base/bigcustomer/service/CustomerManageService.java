package com.yunda.base.bigcustomer.service;

import java.util.List;
import java.util.Map;

import com.yunda.base.bigcustomer.domain.CustomerManageDO;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-20111513
 */
public interface CustomerManageService {
	
	CustomerManageDO get(Integer id);
	
	List<CustomerManageDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CustomerManageDO customerManage);
	
	int update(CustomerManageDO customerManage);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	List getOrganizationInfo();

    int stateUpdate(Integer id, String state);

    String getBranchName(String branch);

	int checkVipNum(String vipNum);

	String getVipNameByVipNum(String vipNum);

    List<CustomerManageDO> getCustomerAll();

	int countByCustomerName(String customerName);
}
