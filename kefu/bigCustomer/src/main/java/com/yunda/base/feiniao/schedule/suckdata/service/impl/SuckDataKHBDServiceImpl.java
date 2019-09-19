package com.yunda.base.feiniao.schedule.suckdata.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.feiniao.log.domain.LogSuckdataDO;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.dao.GpBasSCustPickTmpDao;
import com.yunda.base.feiniao.schedule.suckdata.service.RecordSuckService;
import com.yunda.base.feiniao.schedule.suckdata.service.SuckDataKHBDService;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.SuckDataTaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khbd.KhbdByCustomer;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khbd.KhbdByCustomerBefore20180801;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khbd.KhbdSuckDataTaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khyj.KhyjByWarningDay;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

@Service
public class SuckDataKHBDServiceImpl implements SuckDataKHBDService {
	private static final int TYPE_SUCK = 1; // 抽数
	private static final int TYPE_CLEAR_TABLE = 2;// 清理数据库表
	private static final int TYPE_CLEAR_REDIS = 3;// 清理redis
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	LogSuckdataService logSuckdataService;
	@Autowired
	RecordSuckService recordSuckService;
	@Autowired
	SuckDataKHBDService suckDataKHBDService;
	@Autowired
	private GpBasSCustPickTmpDao gpBasSCustPickTmpDao;

	/**
	 * 针对指定日期进行抽数，如果当日已经抽取过，那么需要先清理然后重建数据
	 * 
	 * @param targetDay抽数的目标日期，
	 *            0为当天，-1为昨天，-2为前天，以此类推
	 */
	public void processSuck(int targetDay) {
		int day = DateUtils.convertDate2Int4Day(DateUtils.getDate(targetDay));

		/** 波动表跑数抽取出来  task跑不跑不能再跟进gp数据是否更新来决定是否跑数
		 * 而应该根据tmpv2_cust_fluctuate_sum表中  目标日期targetDay是否有数据
		 * 这里流程简单化  不检测  直接删表数据/清缓存/重建数据
		 * */
			//logSuckdataService.save(new LogSuckdataDO(targetDay + "存在已抽数，清理所有过程表"));
			// 清理所有过程表
			realProcess(targetDay, TYPE_CLEAR_TABLE);

			//logSuckdataService.save(new LogSuckdataDO(targetDay + "存在已抽数，清理所有缓存数据"));
			// 清理所有缓存数据
			realProcess(targetDay, TYPE_CLEAR_REDIS);

		// 执行抽数动作
		logSuckdataService.save(new LogSuckdataDO("日期targetDay="+targetDay + "波动表重建数据"));
		realProcess(targetDay, TYPE_SUCK);
		
	}

	// 执行抽数动作
	private void realProcess(int targetDay, int type) {
		
		// 执行所有波动报表
		logSuckdataService
				.save(new LogSuckdataDO(Thread.currentThread().getName() +"执行所有波动报表的" + showType(type) + "任务,targetDay=" + showTargetDay(targetDay)));
		try {
			processAllSubClass(KhbdSuckDataTaskAbs.class, targetDay, type);
		} catch (Exception e) {
			logSuckdataService.save(new LogSuckdataDO(Thread.currentThread().getName() +"执行所有波动报表异常" + e.getMessage()));
		}

	}

	// 显示目标日期
	private String showTargetDay(int targetDay) {
		Date date = DateUtils.getDate(targetDay);
		return DateUtils.format(date, "yyyy-MM-dd");
	}

	private String showType(int type) {
		switch (type) {
		case TYPE_SUCK:
			return "抽数";
		case TYPE_CLEAR_TABLE:
			return "清理数据库表";
		case TYPE_CLEAR_REDIS:
			return "清理redis";
		default:
			return "";
		}
	}

	/**
	 * 
	 * @param csabs
	 *            实现类对应的抽象类
	 * @param targetDay
	 *            抽数的目标日期， 0为当天，-1为昨天，-2为前天，以此类推
	 * @param type
	 *            1抽数 2清理
	 */
	private void processAllSubClass(Class csabs, int targetDay, int type) {
		List<Class> list = new ArrayList<Class>();
		if (csabs == KhbdSuckDataTaskAbs.class) {
			list.add(KhbdByCustomer.class);
			list.add(KhbdByCustomerBefore20180801.class);
			//为预警反馈表提供基础数据
			list.add(KhyjByWarningDay.class);
		} 

		List<SuckDataTaskAbs> objs = new ArrayList<SuckDataTaskAbs>();
		for (Class cs : list) {
			try {
				SuckDataTaskAbs task = (SuckDataTaskAbs) cs.newInstance();
				objs.add(task);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}
		}

		// 按优先度排序
		Collections.sort(objs, new Comparator<SuckDataTaskAbs>() {
			@Override
			public int compare(SuckDataTaskAbs o1, SuckDataTaskAbs o2) {
				return Integer.valueOf(o2.index()).compareTo(o1.index());
			}
		});

		for (SuckDataTaskAbs task : objs) {
			switch (type) {
			case TYPE_SUCK:
				// 预检测是否应该执行task
				if (task.preCheck(logSuckdataService, DateUtils.getDate(new Date(), targetDay))) {
					task.process(logSuckdataService, targetDay);
				} else {
					logSuckdataService.save(new LogSuckdataDO(task.getClass().getName() + "preCheck判断无需执行该任务"));
				}
				break;
			case TYPE_CLEAR_TABLE:
				task.clearTable(logSuckdataService, targetDay);
				break;
			case TYPE_CLEAR_REDIS:
				task.clearRedis(logSuckdataService, targetDay);
				break;
			}
		}
	}

}
