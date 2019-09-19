package com.yunda.base.bigcustomer.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.bigcustomer.dao.ConsultConfigDao;
import com.yunda.base.bigcustomer.domain.ConsultConfigDO;
import com.yunda.base.bigcustomer.service.ConsultConfigService;


@Service
public class ConsultConfigServiceImpl implements ConsultConfigService {
	@Autowired
	private ConsultConfigDao consultConfigDao;
	
	@Override
	public ConsultConfigDO get(Integer id){
		return consultConfigDao.get(id);
	}
	
	@Override
	public List<ConsultConfigDO> list(Map<String, Object> map){
		return consultConfigDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return consultConfigDao.count(map);
	}
	
	@Override
	public int save(ConsultConfigDO consultConfig){
		return consultConfigDao.save(consultConfig);
	}
	
	@Override
	public int update(ConsultConfigDO consultConfig){
		return consultConfigDao.update(consultConfig);
	}
	
	@Override
	public int remove(Integer id){
		return consultConfigDao.remove(id);
	}
	
	@Override
	public int batchRemove(Integer[] ids){
		return consultConfigDao.batchRemove(ids);
	}

	@Override
	public int stateUpdate(Integer id, String state) {
		return consultConfigDao.stateUpdate(id,state);
	}
	//
	@Override
	public List<String> searchConsultType(){
		return consultConfigDao.searchConsultType();
	}

}
