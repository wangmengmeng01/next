package com.yunda.base.common.task;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunda.base.common.utils.PushWebsocketMsgUtils;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.service.SysConfigService;

//定时重新加载配置表 0 0/10 * * * ?
public class RefreshSysconfigTask extends TaskAbs {
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	SysConfigService sysConfigService;

	@Override
	public void run(JobExecutionContext arg0) {
		sysConfigService.initConfig();

		(new PushWebsocketMsgUtils()).pushMsg("定时重新加载配置表" + SysConfig.uploadLocal);
		log.info("定时重新加载配置表--->");
	}

	@Override
	public String whoareyou() {
		return "重载配置表";
	}
}