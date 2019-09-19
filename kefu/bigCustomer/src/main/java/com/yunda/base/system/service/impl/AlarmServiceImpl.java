package com.yunda.base.system.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.system.dao.AlarmDao;
import com.yunda.base.system.domain.AlarmDO;
import com.yunda.base.system.domain.LoginUserDO;
import com.yunda.base.system.service.AlarmService;



@Service
public class AlarmServiceImpl implements AlarmService {

	@Autowired
	private AlarmDao alarmDao;
	
	@Override
	public AlarmDO get(String sessionId){
		return alarmDao.get(sessionId);
	}
	
	@Override
	public List<AlarmDO> list(Map<String, Object> map){
		return alarmDao.list(map);
	}
	
	@Override
	public int count(Map<String, Object> map){
		return alarmDao.count(map);
	}
	
	@Override
	public int save(AlarmDO alarm){
		return alarmDao.save(alarm);
	}
	
	@Override
	public int update(AlarmDO alarm){
		return alarmDao.update(alarm);
	}
	
	@Override
	public int remove(String sessionId){
		return alarmDao.remove(sessionId);
	}
	
	@Override
	public int batchRemove(String[] sessionIds){
		return alarmDao.batchRemove(sessionIds);
	}

	@Override
	public String queryRoleNameByUserId(Long userId) {
		return alarmDao.queryRoleNameByUserId(userId);
	}

	@Override
	public String queryBigareaNameByUserId(Long userId) {
		return alarmDao.queryBigareaNameByUserId(userId);
	}

	@Override
	public LoginUserDO queryBigareaInfoByBigareaName(String bigareaName) {
		return alarmDao.queryBigareaInfoByBigareaName(bigareaName);
	}

	@Override
	public List<LoginUserDO> getZongBuData() {
		return alarmDao.getZongBuData();
	}

	@Override
	public LoginUserDO getLoginUserByUserName(String username) {
		return alarmDao.getLoginUserByUserName(username);
	}

}
