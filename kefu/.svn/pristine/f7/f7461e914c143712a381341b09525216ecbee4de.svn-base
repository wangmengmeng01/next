package com.yunda.base.feiniao.schedule.suckdata.task;

import com.yunda.base.common.task.TaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.domain.RecordSuckDO;
import com.yunda.base.feiniao.schedule.suckdata.service.RecordSuckService;
import com.yunda.base.feiniao.schedule.suckdata.service.SuckDataService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 抽数监视狗<br>
 * 1.观测当日是否执行了抽数，若没则报警。若执行了就判断源头表是否变化了<br>
 * 2.若源头数据发生了变化，停顿1分钟再试，直到变化稳定后，调用抽数job重建数据
 * 
 * @author Administrator
 *
 */
@Component
public class WatchDogJob extends TaskAbs {
	private static final Logger log = LoggerFactory.getLogger(WatchDogJob.class);
	private static long sleepTime = 60 * 1000;

	@Autowired
	SimpMessagingTemplate messagingTemplate;
	@Autowired
	RecordSuckService recordSuckService;
	@Autowired
	SuckDataService suckDataService;

	@Override
	public void run(JobExecutionContext arg0) {
		int day = DateUtils.convertDate2Int4Day(DateUtils.getDate(0));

		// 1.观测当日是否执行了抽数，若没则报警
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("suckDate", day + "");
		param.put("delFlag", "0");
		List<RecordSuckDO> list = recordSuckService.list(param);
		if (list == null || list.size() < 1) {
			return;
		}

		RecordSuckDO record = list.get(0);
		long dataEnd = record.getGpNums();

		// 查询源头表的行数
		long gpNums = suckDataService.countGpSource("db2");
		if (gpNums == dataEnd) {
			log.debug("抽数正常，结束本次检测");
			return;
		}

		// 发现源头数据变化了，进一步捕捉稳定状态
		int tryTimes = 0;
		long _gpNums = 0;
		while (true) {
			tryTimes++;

			if (gpNums > _gpNums) {
				_gpNums = gpNums;
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					//e.printStackTrace();
					log.error(e.getMessage(), e);
				}
			} else {
				break;
			}

			gpNums = suckDataService.countGpSource("db2");

			// 防死保护
			if (tryTimes > 1000) {
				// 重试次数太多，放弃
				return;
			}
		}

		// 走到这一步，说明源头数据变化了，且处于一个稳定状态了
		log.info("源头数据变化了，且处于一个稳定状态了开始重建数据");
		suckDataService.processSuck(0);
	}

	@Override
	public String whoareyou() {
		return "抽数监视狗";
	}
}