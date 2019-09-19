package com.yunda.base.common.service;

import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;

import com.yunda.base.common.domain.TaskDO;
import com.yunda.base.common.utils.Query;
/**
 * 
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-26 20:53:48
 */
public interface JobService {
	
	TaskDO get(Long id);
	
	List<TaskDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(TaskDO taskScheduleJob);
	
	int update(TaskDO taskScheduleJob);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);

	void initSchedule() throws SchedulerException;

	void changeStatus(Long jobId, String cmd) throws SchedulerException;

	void updateCron(Long jobId) throws SchedulerException;

	List<TaskDO> listJob(Query query);
}
