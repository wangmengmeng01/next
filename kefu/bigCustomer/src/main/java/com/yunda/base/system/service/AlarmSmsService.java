package com.yunda.base.system.service;

import java.util.List;
import java.util.Map;

import com.yunda.base.system.domain.AlarmSmsDO;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-08154910
 */
public interface AlarmSmsService {
	
	AlarmSmsDO get(String sessionId);
	
	List<AlarmSmsDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(AlarmSmsDO alarmSms);
	
	int update(AlarmSmsDO alarmSms);
	
	int remove(String sessionId);
	
	int batchRemove(String[] sessionIds);

    void saveOrUpdate(AlarmSmsDO alarmSmsDO);

    void selectProvinceIsnotNull(String date);
}
