package com.yunda.base.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.common.config.Constant;
import com.yunda.base.common.dao.TaskDao;
import com.yunda.base.common.domain.ScheduleJob;
import com.yunda.base.common.domain.TaskDO;
import com.yunda.base.common.quartz.utils.QuartzManager;
import com.yunda.base.common.service.JobService;
import com.yunda.base.common.utils.Query;
@Service
public class JobServiceImpl implements JobService {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobServiceImpl.class);

	@Autowired
	private TaskDao taskScheduleJobMapper;

	@Autowired
	QuartzManager quartzManager;

	@Override
	public TaskDO get(Long id) {
		return taskScheduleJobMapper.get(id);
	}

	@Override
	public List<TaskDO> list(Map<String, Object> map) {
		return taskScheduleJobMapper.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return taskScheduleJobMapper.count(map);
	}

	@Override
	public int save(TaskDO taskScheduleJob) {
		return taskScheduleJobMapper.save(taskScheduleJob);
	}

	@Override
	public int update(TaskDO taskScheduleJob) {
		return taskScheduleJobMapper.update(taskScheduleJob);
	}

	@Override
	public int remove(Long id) {
		try {
			TaskDO scheduleJob = get(id);
			quartzManager.deleteJob(new ScheduleJob(scheduleJob));
			return taskScheduleJobMapper.remove(id);
		} catch (SchedulerException e) {
			LOGGER.error("remove scheduleJob Error --> JobServiceImpl", e);
			return 0;
		}

	}

	@Override
	public int batchRemove(Long[] ids) {
		for (Long id : ids) {
			try {
				TaskDO scheduleJob = get(id);
				quartzManager.deleteJob(new ScheduleJob(scheduleJob));
			} catch (SchedulerException e) {
				LOGGER.error("batchRemove scheduleJob Error --> JobServiceImpl", e);
				return 0;
			}
		}
		return taskScheduleJobMapper.batchRemove(ids);
	}

	@Override
	public void initSchedule() throws SchedulerException {
		// 这里获取任务信息数据
		List<TaskDO> jobList = taskScheduleJobMapper.list(new HashMap<String, Object>(16));
		for (TaskDO scheduleJob : jobList) {
			if (ScheduleJob.STATUS_RUNNING.equals(scheduleJob.getJobStatus())) {
				ScheduleJob job = new ScheduleJob(scheduleJob);
				quartzManager.addJob(job);
			}
		}
	}

	@Override
	public void changeStatus(Long jobId, String cmd) throws SchedulerException {
		TaskDO scheduleJob = get(jobId);
		if (scheduleJob == null) {
			return;
		}
		if (Constant.STATUS_RUNNING_STOP.equals(cmd)) {
			quartzManager.deleteJob(new ScheduleJob(scheduleJob));
			scheduleJob.setJobStatus(ScheduleJob.STATUS_NOT_RUNNING);
		} else {
			if (!Constant.STATUS_RUNNING_START.equals(cmd)) {
			} else {
				scheduleJob.setJobStatus(ScheduleJob.STATUS_RUNNING);
				quartzManager.addJob(new ScheduleJob(scheduleJob));
			}
		}
		update(scheduleJob);
	}

	@Override
	public void updateCron(Long jobId) throws SchedulerException {
		TaskDO scheduleJob = get(jobId);
		if (scheduleJob == null) {
			return;
		}
		if (ScheduleJob.STATUS_RUNNING.equals(scheduleJob.getJobStatus())) {
			quartzManager.updateJobCron(new ScheduleJob(scheduleJob));
		}
		update(scheduleJob);
	}

	@Override
	public List<TaskDO> listJob(Query query) {
		return taskScheduleJobMapper.listJob(query);
	}

}
