package com.yunda.base.feiniao.schedule.suckdata.task;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yunda.base.common.task.TaskAbs;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.schedule.suckdata.service.RecordSuckService;
import com.yunda.base.feiniao.schedule.suckdata.service.SuckDataService;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.SuckDataTaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.vo.TaskParams;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

/**
 * 运行单个的抽数task
 * 
 * 通过启动task的扩展参数喂参， 例如 
 * { "targetDay": -11 ,"targetClass":"com.bootdo.portrait.task.suck.SuckDataDemoTask" }
 * 
 * @author Administrator
 *
 */
public class RunOneSuckTask extends TaskAbs {
	private static final Logger log = LoggerFactory.getLogger(RunOneSuckTask.class);
	@Autowired
	SuckDataService suckDataService;
	@Autowired
	LogSuckdataService logSuckdataService;
	@Autowired
	RecordSuckService recordSuckService;

	@Override
	public void run(JobExecutionContext arg0) {
		TaskParams tp = TaskParams.newInstance(arg0);

		if (StringUtils.isBlank(tp.getTargetClass())) {
			log.error("运行单个的抽数task失败，未提供目标类");
			return;
		}

		Object obj = null;
		try {
			obj = Class.forName(tp.getTargetClass()).newInstance();

			if (!(obj instanceof SuckDataTaskAbs)) {
				log.error("目标类" + tp.getTargetClass() + "不是抽数实现task");
				return;
			}
		} catch (Exception e) {
			log.error("目标类" + tp.getTargetClass() + "不能实例");
			log.error(e.getMessage(), e);
			return;
		}

		SuckDataTaskAbs task = (SuckDataTaskAbs) obj;

		// 预检测是否应该执行task
		if (tp.isForce() || task.preCheck(logSuckdataService, DateUtils.getDate(new Date(), tp.getTargetDay()))) {
			// 清理所有过程表
			task.clearTable(logSuckdataService, tp.getTargetDay());
			// 清理所有缓存数据
			task.clearRedis(logSuckdataService, tp.getTargetDay());
			// 执行抽数动作
			task.process(logSuckdataService, tp.getTargetDay());
		} else {
			log.error("目标类" + tp.getTargetClass() + "preCheck不用运行，如需强制运行请提供force参数");
		}
	}

	@Override
	public String whoareyou() {
		return "手动调度单个的抽数task";
	}
}
