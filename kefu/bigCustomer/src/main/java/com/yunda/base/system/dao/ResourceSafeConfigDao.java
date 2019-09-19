package com.yunda.base.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.system.domain.ResourceSafeConfigDO;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-02-18091552
 */
@Mapper
public interface ResourceSafeConfigDao {

	ResourceSafeConfigDO get(Integer id);
	
	List<ResourceSafeConfigDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ResourceSafeConfigDO resourceSafeConfig);
	
	int update(ResourceSafeConfigDO resourceSafeConfig);
	
	int remove(Integer id);
	
	int batchRemove(Integer[] ids);

	/** 
	* @Title: getSafeLevel 
	* @Description: TODO 
	* @param url
	* @return String
	* @author 22374
	* @date 2019年2月18日上午10:20:53
	*/
	String getSafeLevel(String url);
}
