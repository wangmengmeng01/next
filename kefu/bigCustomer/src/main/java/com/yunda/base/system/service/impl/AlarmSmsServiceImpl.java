package com.yunda.base.system.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.system.dao.AlarmSmsDao;
import com.yunda.base.system.domain.AlarmSmsDO;
import com.yunda.base.system.service.AlarmSmsService;



@Service
public class AlarmSmsServiceImpl implements AlarmSmsService {
	@Autowired
	private AlarmSmsDao alarmSmsDao;
	
	@Override
	public AlarmSmsDO get(String sessionId){
		return alarmSmsDao.get(sessionId);
	}
	
	@Override
	public List<AlarmSmsDO> list(Map<String, Object> map){
		return alarmSmsDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return alarmSmsDao.count(map);
	}
	
	@Override
	public int save(AlarmSmsDO alarmSms){
		return alarmSmsDao.save(alarmSms);
	}
	
	@Override
	public int update(AlarmSmsDO alarmSms){
		return alarmSmsDao.update(alarmSms);
	}
	
	@Override
	public int remove(String sessionId){
		return alarmSmsDao.remove(sessionId);
	}
	
	@Override
	public int batchRemove(String[] sessionIds){
		return alarmSmsDao.batchRemove(sessionIds);
	}

	@Override
	public void saveOrUpdate(AlarmSmsDO alarmSmsDO) {
		alarmSmsDao.saveOrUpdate(alarmSmsDO);
	}

	@Override
	public void selectProvinceIsnotNull(String date) {

	}
}
