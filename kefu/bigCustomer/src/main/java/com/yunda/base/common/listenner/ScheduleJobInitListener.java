package com.yunda.base.common.listenner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.yunda.base.common.quartz.utils.QuartzManager;
import com.yunda.base.common.service.JobService;

@Component
@Order(value = 1)
public class ScheduleJobInitListener implements CommandLineRunner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleJobInitListener.class);
	
	@Autowired
	JobService scheduleJobService;

	@Autowired
	QuartzManager quartzManager;

	@Override
	public void run(String... arg0) throws Exception {
		try {
			scheduleJobService.initSchedule();
		} catch (Exception e) {
			LOGGER.error("ScheduleJobInitListener Error",e);
		}

	}

}