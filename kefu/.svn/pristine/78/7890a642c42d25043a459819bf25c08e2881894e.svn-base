package com.yunda.base.system.service;

import com.yunda.base.system.domain.ResourceSafeConfigDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-02-18091552
 */
public interface ResourceSafeConfigService {
	
	ResourceSafeConfigDO get(Integer id);
	
	List<ResourceSafeConfigDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
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
	* @date 2019年2月18日上午10:19:53
	*/
	String getSafeLevel(String url);
}
