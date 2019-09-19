package com.yunda.base.feiniao.workorder.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.feiniao.workorder.dao.ServiceWorkorderDao;
import com.yunda.base.feiniao.workorder.domain.ServiceWorkorderDO;
import com.yunda.base.feiniao.workorder.service.ServiceWorkorderService;



@Service
public class ServiceWorkorderServiceImpl implements ServiceWorkorderService {
	@Autowired
	private ServiceWorkorderDao serviceWorkorderDao;
	
	@Override
	public ServiceWorkorderDO get(Integer workOrderId){
		return serviceWorkorderDao.get(workOrderId);
	}
	
	@Override
	public List<ServiceWorkorderDO> list(Map<String, Object> map){
		return serviceWorkorderDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return serviceWorkorderDao.count(map);
	}
	
	@Override
	public int save(ServiceWorkorderDO serviceWorkorder){
		return serviceWorkorderDao.save(serviceWorkorder);
	}
	
	@Override
	public int update(ServiceWorkorderDO serviceWorkorder){
		return serviceWorkorderDao.update(serviceWorkorder);
	}
	
	@Override
	public int remove(Integer workOrderId){
		return serviceWorkorderDao.remove(workOrderId);
	}
	
	@Override
	public int batchRemove(Integer[] workOrderIds){
		return serviceWorkorderDao.batchRemove(workOrderIds);
	}
	
}
