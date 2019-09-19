package com.yunda.base.bigcustomer.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yunda.base.bigcustomer.domain.PermissionsManageDO;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-05-22111446
 */
@Mapper
public interface PermissionsManageDao {

	PermissionsManageDO get(Integer id);
	
	List<PermissionsManageDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(PermissionsManageDO permissionsManage);
	
	int update(PermissionsManageDO permissionsManage);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

    int stateUpdate(@Param("id") Integer id,@Param("state") String state);
}
