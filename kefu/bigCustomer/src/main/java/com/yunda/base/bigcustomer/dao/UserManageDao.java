package com.yunda.base.bigcustomer.dao;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.bigcustomer.domain.UserManageDO;


/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-21164900
 */
@Mapper
public interface UserManageDao {

	UserManageDO get(Integer id);
	
	List<UserManageDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UserManageDO userManage);
	
	int update(UserManageDO userManage);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	int stateUpdate(Integer id, String state);
}
