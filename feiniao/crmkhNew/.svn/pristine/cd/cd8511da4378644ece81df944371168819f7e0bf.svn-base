package com.yunda.base.system.dao;

import com.yunda.base.system.domain.UserProvinceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户与省对应关系
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-08-15 17:28:17
 */
@Mapper
public interface UserProvinceDao {

	UserProvinceDO get(Long id);
	
	List<UserProvinceDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(UserProvinceDO userProvince);
	
	int update(UserProvinceDO userProvince);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	int removeByUserId(Long userId);

	int batchSave(List<UserProvinceDO> list);
	
	List<Long> listProvinceIdByUserId(Long id);

	List<Long> queryUP(Long userId);

	List<Map<String, Object>> getProvinceAndBigarea(@Param("userId") Long userId);
}
