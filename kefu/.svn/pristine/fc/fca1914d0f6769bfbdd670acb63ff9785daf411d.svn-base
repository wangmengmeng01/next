package com.yunda.base.system.startup;

import com.yunda.base.system.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//配置参数从数据库载入
@Component
@Order(value = 1) // 第二个被执行
public class InitSysconfigStartup implements CommandLineRunner {
	@Autowired
	SysConfigService sysConfigService;

	@Override
	public void run(String... args) throws Exception {
		sysConfigService.initConfig();
	}
}
