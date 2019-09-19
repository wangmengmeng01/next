package com.yunda.base.feiniao.schedule.suckdata.task.impls;

import java.util.Date;

import com.yunda.base.feiniao.log.domain.LogSuckdataDO;
import com.yunda.base.feiniao.log.enums.EventTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.CachePrefixConformity;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

//抽数动作抽象类，统计记录起止和异常信息
public abstract class SuckDataTaskAbs {

	/**
	 * 执行抽数动作
	 * 
	 * @param logSuckdataService
	 * @param targetDay
	 *            抽数的目标日期， 0为当天，-1为昨天，-2为前天，以此类推
	 */
	public void process(LogSuckdataService logSuckdataService, int targetDay) {
		logSuckdataService.save(new LogSuckdataDO(whichLogType(), "【" + whoareyou() + "】开始抽数"));

		String feedback = "";
		Date target = DateUtils.getDate(new Date(), targetDay);
		try {
			feedback = realProcess(logSuckdataService, target);
		} catch (Exception e) {
			logSuckdataService.save(new LogSuckdataDO(whichLogType(), EventTypeEnum.event_all.getCode(),
					whoareyou() + "抽数异常：" + e.getMessage()));
		}

		logSuckdataService.save(
				new LogSuckdataDO(whichLogType(), "【" + whoareyou() + "】抽数结束:" + DateUtils.format(target)+"=" + feedback));
	}

	/**
	 * 清理抽数成果
	 * 
	 * @param logSuckdataService
	 * @param targetDay
	 *            抽数的目标日期， 0为当天，-1为昨天，-2为前天，以此类推
	 */
	public void clearTable(LogSuckdataService logSuckdataService, int targetDay) {
		logSuckdataService.save(new LogSuckdataDO(whichLogType(), "【" + whoareyou() + "】开始清理表"));

		Date target = DateUtils.getDate(new Date(), targetDay);
		try {
			realClearTable(logSuckdataService, target);
		} catch (Exception e) {
			logSuckdataService.save(new LogSuckdataDO(whichLogType(), EventTypeEnum.event_all.getCode(),
					whoareyou() + "清理表异常：" + e.getMessage()));
		}

		logSuckdataService
				.save(new LogSuckdataDO(whichLogType(), "【" + whoareyou() + "】" + DateUtils.format(target) + "表清理结束"));
	}

	/**
	 * 清理抽数查询缓存
	 * 
	 * @param logSuckdataService
	 * @param targetDay
	 *            抽数的目标日期， 0为当天，-1为昨天，-2为前天，以此类推
	 */
	public void clearRedis(LogSuckdataService logSuckdataService, int targetDay) {

		//logSuckdataService.save(new LogSuckdataDO(whichLogType(), "【" + whoareyou() + "】开始清理缓存"));
		CachePrefixConformity cache = new CachePrefixConformity();

		try {
			cache.resetSeed(cacheKeyPerfix());
		} catch (Exception e) {
			logSuckdataService.save(new LogSuckdataDO(whichLogType(), EventTypeEnum.event_all.getCode(),
					whoareyou() + "清理缓存异常：" + e.getMessage()));
		}

		//logSuckdataService.save(new LogSuckdataDO(whichLogType(), "【" + whoareyou() + "】缓存清理结束"));
	}

	// 排序，越大优先度越高. 根据依赖关系调整优先顺序
	public abstract int index();

	// 预先检查,本task是否需要执行, true为需要执行,false为无需执行task
	public abstract boolean preCheck(LogSuckdataService logSuckdataService, Date targetDay);

	// 正式执行抽数动作
	public abstract String realProcess(LogSuckdataService logSuckdataService, Date targetDay);

	// 正式执行清理动作
	public abstract void realClearTable(LogSuckdataService logSuckdataService, Date targetDay);

	// 正式执行清理redis缓存动作
	public abstract void realClearRedis(LogSuckdataService logSuckdataService, Date targetDay);

	// 对应的logtype是什么
	public abstract int whichLogType();

	// 自我标签
	public abstract String whoareyou();

	// 缓存命名空间
	public abstract String cacheKeyPerfix();

}
