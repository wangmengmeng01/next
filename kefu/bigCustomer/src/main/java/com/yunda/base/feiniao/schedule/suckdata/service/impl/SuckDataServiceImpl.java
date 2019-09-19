package com.yunda.base.feiniao.schedule.suckdata.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunda.base.common.multi.annotation.DataSourceAnnotation;
import com.yunda.base.feiniao.log.domain.LogSuckdataDO;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.dao.GpBasSCustPickTmpDao;
import com.yunda.base.feiniao.schedule.suckdata.domain.RecordSuckDO;
import com.yunda.base.feiniao.schedule.suckdata.enums.DelFlagEnum;
import com.yunda.base.feiniao.schedule.suckdata.service.RecordSuckService;
import com.yunda.base.feiniao.schedule.suckdata.service.SuckDataService;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.SuckDataTaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.changecooperateordernum.ChangeCooperateOrderNum;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.changecooperateordernum.ChangeCooperateOrderNumTaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khgp.KhGpCustomerSource;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khgp.KhGpSource;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khgp.KhgpSuckDataTaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khje.KhjeByArea;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khje.KhjeByCity;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khje.KhjeByCountry;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khje.KhjeByCustomer;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khje.KhjeByProvince;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khje.KhjeByWangdian;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khje.KhjeSuckDataTaskAbs;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khlj.KhljByArea;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khlj.KhljByCity;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khlj.KhljByCountry;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khlj.KhljByCustomer;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khlj.KhljByGs;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khlj.KhljByProvince;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khlj.KhljByWangdian;
import com.yunda.base.feiniao.schedule.suckdata.task.impls.khlj.KhljSuckDataTaskAbs;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

@Service
public class SuckDataServiceImpl implements SuckDataService {
	private static final int TYPE_SUCK = 1; // 抽数
	private static final int TYPE_CLEAR_TABLE = 2;// 清理数据库表
	private static final int TYPE_CLEAR_REDIS = 3;// 清理redis
	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	LogSuckdataService logSuckdataService;
	@Autowired
	RecordSuckService recordSuckService;
	@Autowired
	SuckDataService suckDataService;
	@Autowired
	private GpBasSCustPickTmpDao gpBasSCustPickTmpDao;

	/**
	 * 针对指定日期进行抽数，如果当日已经抽取过，那么需要先清理然后重建数据
	 * 
	 * @param targetDay抽数的目标日期，
	 *            0为当天，-1为昨天，-2为前天，以此类推
	 */
	@Override
    public void processSuck(int targetDay) {
		int day = DateUtils.convertDate2Int4Day(DateUtils.getDate(targetDay));

		// 查询源头表的行数
		long gpNums = suckDataService.countGpSource("db2");

		// 检测目标是否已经抽取过
		boolean already = recordSuckService.alreadySuck(targetDay);
		if (already) {
			logSuckdataService.save(new LogSuckdataDO(targetDay + "存在已抽数，清理所有过程表"));
			// 清理所有过程表
			realProcess(targetDay, TYPE_CLEAR_TABLE);

			logSuckdataService.save(new LogSuckdataDO(targetDay + "存在已抽数，清理所有缓存数据"));
			// 清理所有缓存数据
			realProcess(targetDay, TYPE_CLEAR_REDIS);

			logSuckdataService.save(new LogSuckdataDO(targetDay + "存在已抽数，将历史抽数记录设置为无效状态"));
			// 将历史抽数记录设置为无效状态
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("suckDate", day + "");
			recordSuckService.delMark(param);
		}

		// 执行抽数动作
		realProcess(targetDay, TYPE_SUCK);

		// 记录并读取工作记录表
		RecordSuckDO record = new RecordSuckDO();
		record.setSuckDate(day);
		record.setGpNums(gpNums);
		record.setDelFlag(DelFlagEnum.flag_ok.getCode());
		recordSuckService.save(record);
	}

	// 执行抽数动作
	private void realProcess(int targetDay, int type) {
		// 执行所有gp汇总
		logSuckdataService.save(new LogSuckdataDO(Thread.currentThread().getName() + "执行所有gp汇总" + showType(type)
				+ "任务,targetDay=" + showTargetDay(targetDay)));
		try {
			processAllSubClass(KhgpSuckDataTaskAbs.class, targetDay, type);
		} catch (Exception e) {
			logSuckdataService.save(new LogSuckdataDO("执行所有gp汇总异常" + e.getMessage()));
		}

		// 执行所有客户揽件
		logSuckdataService
				.save(new LogSuckdataDO("执行所有客户揽件报表的" + showType(type) + "任务,targetDay=" + showTargetDay(targetDay)));
		try {
			processAllSubClass(KhljSuckDataTaskAbs.class, targetDay, type);
		} catch (Exception e) {
			logSuckdataService.save(new LogSuckdataDO("执行所有客户揽件异常" + e.getMessage()));
		}

		// 执行所有客户金额
		logSuckdataService
				.save(new LogSuckdataDO("执行所有客户金额报表的" + showType(type) + "任务,targetDay=" + showTargetDay(targetDay)));
		try {
			processAllSubClass(KhjeSuckDataTaskAbs.class, targetDay, type);
		} catch (Exception e) {
			logSuckdataService.save(new LogSuckdataDO("执行所有客户金额异常" + e.getMessage()));
		}

		// 执行所有预警报表
		/*logSuckdataService
				.save(new LogSuckdataDO("执行所有预警报表的" + showType(type) + "任务,targetDay=" + showTargetDay(targetDay)));
		try {
			processAllSubClass(KhyjSuckDataTaskAbs.class, targetDay, type);
		} catch (Exception e) {
			logSuckdataService.save(new LogSuckdataDO("执行所有预警报表异常" + e.getMessage()));
		}*/

		// 执行所有波动报表
		/*logSuckdataService
				.save(new LogSuckdataDO("执行所有波动报表的" + showType(type) + "任务,targetDay=" + showTargetDay(targetDay)));
		try {
			processAllSubClass(KhbdSuckDataTaskAbs.class, targetDay, type);
		} catch (Exception e) {
			logSuckdataService.save(new LogSuckdataDO("执行所有波动报表异常" + e.getMessage()));
		}*/
		// 执行未合作客户表中的客户揽件量
		logSuckdataService
				.save(new LogSuckdataDO("执行未合作客户表中的客户揽件量" + showType(type) + "任务,targetDay=" + showTargetDay(targetDay)));
		try {
			processAllSubClass(ChangeCooperateOrderNumTaskAbs.class, targetDay, type);
		} catch (Exception e) {
			logSuckdataService.save(new LogSuckdataDO("执行未合作客户表中的客户揽件量异常" + e.getMessage()));
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
		if (csabs == KhgpSuckDataTaskAbs.class) {
			list.add(KhGpCustomerSource.class);
			list.add(KhGpSource.class);
		} /*else if (csabs == KhbdSuckDataTaskAbs.class) {
			list.add(KhbdByCustomer.class);
			list.add(KhbdByCustomerBefore20180801.class);
		}*/ else if (csabs == KhjeSuckDataTaskAbs.class) {
			list.add(KhjeByArea.class);
			list.add(KhjeByCity.class);
			list.add(KhjeByCountry.class);
			list.add(KhjeByCustomer.class);
			list.add(KhjeByProvince.class);
			list.add(KhjeByWangdian.class);

		} else if (csabs == KhljSuckDataTaskAbs.class) {
			list.add(KhljByArea.class);
			list.add(KhljByCity.class);
			list.add(KhljByCountry.class);
			list.add(KhljByCustomer.class);
			list.add(KhljByGs.class);
			list.add(KhljByProvince.class);
			list.add(KhljByWangdian.class);

		} /*else if (csabs == KhyjSuckDataTaskAbs.class) {
			list.add(KhyjByWarningSum.class);
		}*/
		else if (csabs == ChangeCooperateOrderNumTaskAbs.class) {
			list.add(ChangeCooperateOrderNum.class);
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

	// 全表统计Gp源头表crmkh_gp_bas_s_cust_pick_tmp的条数【该表的引擎必须设定为MyISAM】
	@Override
	@DataSourceAnnotation
	public int countGpSource(String dsId) {
		return gpBasSCustPickTmpDao.countGpSource();

	}

}
