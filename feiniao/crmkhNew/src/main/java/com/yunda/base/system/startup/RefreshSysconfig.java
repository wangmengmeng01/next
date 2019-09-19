package com.yunda.base.system.startup;

import com.yunda.base.system.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

//刷新配置表数据
@Component
@Order(value = 3) // 第二个被执行
public class RefreshSysconfig implements CommandLineRunner {

	@Autowired
	SysConfigService sysConfigService;

	@Override
	public void run(String... args) throws Exception {

		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				sysConfigService.initConfig();
			}
		};

		Timer timer = new Timer();

		timer.schedule(task, 10000, 600000);

	}

}