package com.yunda.base.feiniao.schedule.suckdata.task;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunda.base.common.task.TaskAbs;
import com.yunda.base.feiniao.costreport.service.CostreportCustCostExtService;
import com.yunda.base.feiniao.schedule.suckdata.vo.TaskParams;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
@Component
/**
 * 抽数，crmkh_costreport_cust_cost_ext
 * 
 * @author Administrator
 *
 */
public class CostReportDataJob extends TaskAbs {
	
	private final CostreportCustCostExtService costreportCustCostExtService;
	
	@Autowired
	public CostReportDataJob(
			CostreportCustCostExtService costreportCustCostExtService) {
		super();
		this.costreportCustCostExtService = costreportCustCostExtService;
	}

	@Override
	public void run(JobExecutionContext arg0) {
		TaskParams tp = TaskParams.newInstance(arg0);
        int resultDate = tp.getTargetDay();
		costreportCustCostExtService.groupCostExtData(DateUtils.getDate(new Date(), resultDate),"db2");
	}

	@Override
	public String whoareyou() {
		return "从crmkh_costreport_cust_cost_ext表抽数";
	}
}