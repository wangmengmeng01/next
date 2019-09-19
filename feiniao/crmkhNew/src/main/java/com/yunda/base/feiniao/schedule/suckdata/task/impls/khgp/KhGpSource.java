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
//抽数 - 汇总gp报表 - 按日gp汇总合计
public class KhGpSource extends KhgpSuckDataTaskAbs {
	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		return true;
	}
	
	@Override
	public String realProcess(LogSuckdataService logSuckdataService, Date targetDay) {
		int sum = 0;
		int skip = 0;
		long l = System.currentTimeMillis();

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("pickDt", DateUtils.format(targetDay));
		param.put("limit", SysConfig.suckLimitInt);
		// ReportTotaldataDO a =reportTotaldataDao.get("重庆大区");
		// ApplicationContextRegister.getApplicationContext().getBean(GpBasSCustPickTmpDao.class);
		
		while (true) {
			param.put("skip", skip);
			List<Map<String, Object>> gpSource = TaskBeanUtils.getReportTotaldataServiceImpl().queryGpSource(param,
					"db2");
			if (gpSource ==null ||gpSource.size() == 0)
				break;
			
			TaskBeanUtils.getReportTotaldataDao().saveGpSource(gpSource);
			sum += gpSource.size();
			skip +=  SysConfig.suckLimitInt;
		}
		
		return "抽取了gp汇总(所有面单)数据" + sum + "条，耗时" + ((System.currentTimeMillis() - l) / 1000) + "秒";
	}

	@Override
	// 排序，越大优先度越高. 根据依赖关系调整优先顺序
	public int index() {
		return 1;
	}

	@Override
	public void realClearTable(LogSuckdataService logSuckdataService, Date targetDay) {

		TaskBeanUtils.getReportTotaldataDao().removeGpSource(targetDay);

	}

	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService, Date targetDay) {
		// TODO 清理查询产生的redis缓存
	}

	@Override
	public String whoareyou() {
		return "汇总gp报表 - 按日gp汇总(所有面单)合计";
	}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khgp_source.getCode();
	}

	@Override
	public String cacheKeyPerfix() {
		// TODO Auto-generated method stub
		return null;
	}

}
