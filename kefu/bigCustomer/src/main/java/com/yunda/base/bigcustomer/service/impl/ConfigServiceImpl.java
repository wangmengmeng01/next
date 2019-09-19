package com.yunda.base.bigcustomer.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.bigcustomer.dao.ConfigDao;
import com.yunda.base.bigcustomer.domain.ConfigDO;
import com.yunda.base.bigcustomer.service.ConfigService;



@Service
public class ConfigServiceImpl implements ConfigService {
	@Autowired
	private ConfigDao configDao;
	
	@Override
	public ConfigDO get(Integer id){
		return configDao.get(id);
	}
	
	@Override
	public List<ConfigDO> list(Map<String, Object> map){
		return configDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return configDao.count(map);
	}
	
	@Override
	public int save(ConfigDO config){
		return configDao.save(config);
	}
	
	@Override
	public int update(ConfigDO config){
		return configDao.update(config);
	}
	
	@Override
	public int remove(Integer id){
		return configDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return configDao.batchRemove(ids);
	}
	
}
