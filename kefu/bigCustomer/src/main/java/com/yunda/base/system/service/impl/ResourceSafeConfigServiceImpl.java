package com.yunda.base.system.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.system.dao.ResourceSafeConfigDao;
import com.yunda.base.system.domain.ResourceSafeConfigDO;
import com.yunda.base.system.service.ResourceSafeConfigService;



@Service
public class ResourceSafeConfigServiceImpl implements ResourceSafeConfigService {
	@Autowired
	private ResourceSafeConfigDao resourceSafeConfigDao;
	
	@Override
	public ResourceSafeConfigDO get(Integer id){
		return resourceSafeConfigDao.get(id);
	}
	
	@Override
	public List<ResourceSafeConfigDO> list(Map<String, Object> map){
		return resourceSafeConfigDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return resourceSafeConfigDao.count(map);
	}
	
	@Override
	public int save(ResourceSafeConfigDO resourceSafeConfig){
		return resourceSafeConfigDao.save(resourceSafeConfig);
	}
	
	@Override
	public int update(ResourceSafeConfigDO resourceSafeConfig){
		return resourceSafeConfigDao.update(resourceSafeConfig);
	}
	
	@Override
	public int remove(Integer id){
		return resourceSafeConfigDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return resourceSafeConfigDao.batchRemove(ids);
	}

	@Override
	public String getSafeLevel(String url) {
		return resourceSafeConfigDao.getSafeLevel(url);
	}
	
}
