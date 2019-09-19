package com.yunda.base.feiniao.schedule.suckdata.task;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunda.base.common.task.TaskAbs;
import com.yunda.base.feiniao.report.service.CqkhCustomerService;

@Component
/**
 * 抽数，cqkh表
 * 
 * @author Administrator
 *
 */
public class CustomerDataJob extends TaskAbs {
	@Autowired
	CqkhCustomerService cqkhCustomerService;

	@Override
	public void run(JobExecutionContext arg0) {
		//从cqkh库拿用户信息
		cqkhCustomerService.synCqkhCustomer();
		//从菜鸟京东库拿商家id和店铺名称
		cqkhCustomerService.synCNAndJDSeller("db3");
	}

	@Override
	public String whoareyou() {
		return "从cqkh表抽数";
	}
}