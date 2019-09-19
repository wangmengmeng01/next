package com.yunda.base.bigcustomer.service;

import java.util.List;
import java.util.Map;

import com.yunda.base.bigcustomer.domain.UserManageDO;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-21164900
 */
public interface UserManageService {
	
	UserManageDO get(Integer id);
	
	List<UserManageDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UserManageDO userManage);
	
	int update(UserManageDO userManage);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	int stateUpdate(Integer id, String state);
}
