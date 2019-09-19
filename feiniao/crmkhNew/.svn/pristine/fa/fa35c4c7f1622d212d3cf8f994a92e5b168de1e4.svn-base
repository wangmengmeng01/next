package com.yunda.base.feiniao.schedule.suckdata.task.impls.khgp;

import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;
import com.yunda.base.system.config.SysConfig;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**抽数 - 客户gp报表 - 按日gp客户合计
 * 1菜鸟2二维码3普通 4 京东 5 拼多多
 * GP推数是推了1 2 3 4 4 四种的，但是由于3没有客户编码，所以，到了客户层级的数据就只统计1 2 4 5 这4种
 * 总表的合计就是5种都有，客户奖励明细表、预警表、波动表就都只有4种
 * 只汇总1  2  4  5三种客户
 * @author admin
 *
 */

public class KhGpCustomerSource extends KhgpSuckDataTaskAbs {

	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService, Date targetDay) {
		return true;
	}

	@Override
	public String realProcess(LogSuckdataService logSuckdataService, Date targetDay) {
		int sum = 0;
		int skip = 0;
		long l = System.currentTimeMillis();

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("pickDt", DateUtils.format(targetDay));
		param.put("custSrc", 1);
		param.put("limit", SysConfig.suckLimitInt);
		while (true) {
			param.put("skip", skip);
			List<Map<String, Object>> gpCustomerSource = TaskBeanUtils.getReportTotaldataServiceImpl()
					.queryGpCustomerSource(param, "db2");
			if (gpCustomerSource.size() == 0)
				break;
			
			TaskBeanUtils.getReportTotaldataDao().saveGpCustomerSource(gpCustomerSource);
			sum += gpCustomerSource.size();
			skip += SysConfig.suckLimitInt;
		}

		skip = 0;
		param.put("custSrc", 2);
		while (true) {
			param.put("skip", skip);
			List<Map<String, Object>> gpCustomerSource2 = TaskBeanUtils.getReportTotaldataServiceImpl()
					.queryGpCustomerSource(param, "db2");
			if (gpCustomerSource2.size() == 0)
				break;
			
			TaskBeanUtils.getReportTotaldataDao().saveGpCustomerSource(gpCustomerSource2);
			sum += gpCustomerSource2.size();
			skip += SysConfig.suckLimitInt;
		}
		
		skip = 0;
		param.put("custSrc", 4);
		while (true) {
			param.put("skip", skip);
			List<Map<String, Object>> gpCustomerSource3 = TaskBeanUtils.getReportTotaldataServiceImpl()
					.queryGpCustomerSource(param, "db2");
			if (gpCustomerSource3.size() == 0)
				break;
			
			TaskBeanUtils.getReportTotaldataDao().saveGpCustomerSource(gpCustomerSource3);
			sum += gpCustomerSource3.size();
			skip += SysConfig.suckLimitInt;
		}
		
		/**
		 * 从GP推数源头表（crmkh_gp_bas_s_cust_pick_tmp）抽数(客户来源5 拼多多)到 crmkhv2_report_order_stats_all
		 */
		skip = 0;
		param.put("custSrc", 5);
		while (true) {
			param.put("skip", skip);
			List<Map<String, Object>> gpCustomerSource3 = TaskBeanUtils.getReportTotaldataServiceImpl()
					.queryGpCustomerSource(param, "db2");
			if (gpCustomerSource3.size() == 0)
				break;
			
			TaskBeanUtils.getReportTotaldataDao().saveGpCustomerSource(gpCustomerSource3);
			sum += gpCustomerSource3.size();
			skip += SysConfig.suckLimitInt;
		}
		return "按步长" + SysConfig.suckLimitInt + "抽取了gp电子面单客户数据" + sum + "条，耗时" + ((System.currentTimeMillis() - l) / 1000) + "秒";
	}

	@Override
	// 排序，越大优先度越高. 根据依赖关系调整优先顺序
	public int index() {
		return 2;
	}

	@Override
	public void realClearTable(LogSuckdataService logSuckdataService, Date targetDay) {
		TaskBeanUtils.getReportTotaldataDao().removeGpCustomerSource(targetDay);

	}

	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService, Date targetDay) {

	}

	@Override
	public String whoareyou() {
		return "客户gp报表 - 按日gp电子面单客户合计";
	}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khgp_customersource.getCode();
	}

	@Override
	public String cacheKeyPerfix() {
		// TODO Auto-generated method stub
		return null;
	}

}
