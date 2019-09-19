package com.yunda.base.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.system.domain.AlarmSmsDO;

/**
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-08154910
 */
@Mapper
public interface AlarmSmsDao {

    AlarmSmsDO get(String sessionId);

    List<AlarmSmsDO> list(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(AlarmSmsDO alarmSms);

    int update(AlarmSmsDO alarmSms);

    int remove(String session_id);

    int batchRemove(String[] sessionIds);

    void saveOrUpdate(AlarmSmsDO alarmSmsDO);
}