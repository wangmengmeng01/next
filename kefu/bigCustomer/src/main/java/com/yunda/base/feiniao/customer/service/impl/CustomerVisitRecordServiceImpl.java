package com.yunda.base.feiniao.customer.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.feiniao.customer.dao.CustomerVisitRecordDao;
import com.yunda.base.feiniao.customer.domain.CustomerVisitRecordDO;
import com.yunda.base.feiniao.customer.service.CustomerVisitRecordService;



@Service
public class CustomerVisitRecordServiceImpl implements CustomerVisitRecordService {
	@Autowired
	private CustomerVisitRecordDao customerVisitRecordDao;
	
	@Override
	public CustomerVisitRecordDO get(Integer recordId){
		return customerVisitRecordDao.get(recordId);
	}
	
	@Override
	public List<CustomerVisitRecordDO> list(Map<String, Object> map){
		return customerVisitRecordDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return customerVisitRecordDao.count(map);
	}
	
	@Override
	public int save(CustomerVisitRecordDO customerVisitRecord){
		return customerVisitRecordDao.save(customerVisitRecord);
	}
	
	@Override
	public int update(CustomerVisitRecordDO customerVisitRecord){
		return customerVisitRecordDao.update(customerVisitRecord);
	}
	
	@Override
	public int remove(Integer recordId){
		return customerVisitRecordDao.remove(recordId);
	}
	
	@Override
	public int batchRemove(Integer[] recordIds){
		return customerVisitRecordDao.batchRemove(recordIds);
	}
	
}
