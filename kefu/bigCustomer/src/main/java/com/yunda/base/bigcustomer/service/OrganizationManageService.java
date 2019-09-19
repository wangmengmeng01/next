package com.yunda.base.bigcustomer.service;

import java.util.List;
import java.util.Map;

import com.yunda.base.bigcustomer.domain.OrganizationManageDO;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-21110249
 */
public interface OrganizationManageService {
	
	OrganizationManageDO get(Integer id);
	
	List<OrganizationManageDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(OrganizationManageDO organizationManage);
	
	int update(OrganizationManageDO organizationManage);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

    int stateUpdate(Integer id, String state);

    List listOrganization();

    List listOrganizationName();

    int countByOrganizationNum(String organizationNum);

    String listOrgName(String orgCode);

}
