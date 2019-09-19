package com.yunda.base.bigcustomer.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yunda.base.bigcustomer.domain.CustomerManageDO;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-20111513
 */
@Mapper
public interface CustomerManageDao {

	CustomerManageDO get(Integer id);
	
	List<CustomerManageDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CustomerManageDO customerManage);
	
	int update(CustomerManageDO customerManage);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	List getOrganizationInfo();

	int stateUpdate(@Param("id") Integer id,@Param("state") String state);

	String getBranchName(@Param("branch") String branch);

	int checkVipNum(@Param("vipNum") String vipNum);

	String getVipNameByVipNum(String vipNum);

    List<CustomerManageDO> getCustomerAll();

	int countByCustomerName(String customerName);
}
