package com.yunda.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ServletComponentScan
@MapperScan("com.yunda.base.**.dao")
@SpringBootApplication
@EnableAsync //开启异步调用
public class CrmkhApplication {
	public static void main(String[] args) {
		SpringApplication.run(CrmkhApplication.class, args);
		System.out.println("ヾ(◍°∇°◍)ﾉﾞ   【研二-客服-新飞鸟】启动成功      ヾ(◍°∇°◍)ﾉﾞ");
}
}
