package com.yunda.base.feiniao.schedule.suckdata.task.impls.khje;

import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.domain.ReportTotaldataDO;
import com.yunda.base.feiniao.report.service.impl.CRMKHReportTotalManageServiceImpl;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;
import com.yunda.base.feiniao.schedule.suckdata.enums.SuckCacheKeyPerfixEnum;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

//抽数 - 客户金额报表 - 按日全国时间段金额合计

public class KhjeByCountry extends KhjeSuckDataTaskAbs {
	
	@Override
	public boolean preCheck(LogSuckdataService logSuckdataService,
			Date targetDay) {
		return true;
	}
	
	
	@Override
	public void realClearTable(LogSuckdataService logSuckdataService,Date targetDay) {

		TaskBeanUtils.getReportTotaldataDao().removeJeByTime(targetDay);
/*		//获取时间段数据是否包含该该跑数日期，有就删除该时间段数据生成的缓存标识
		List<ReportTotaldataDO> datas = TaskBeanUtils.getReportTotaldataDao().getSJDDate(targetDay);
		if(null != datas && datas.size() > 0){
			ValueOperations<String, String> operations = TaskBeanUtils.getRedisTemplate().opsForValue();
			for(ReportTotaldataDO data:datas){
				//String cacheTotal = operations.get(data.getStartDate()+data.getEndDate()+SuckCacheKeyPerfixEnum.zongbiao.getCode());
				TaskBeanUtils.getCRMKHReportTotalManageServiceImpl().setTotalReportStatus(data.getStartDate(), data.getEndDate(), "ruRun");
				//String cachetest = operations.get(data.getStartDate()+data.getEndDate()+SuckCacheKeyPerfixEnum.zongbiao.getCode());
				//System.out.println(cachetest);
			}
			//删除金额时间段数据
			TaskBeanUtils.getReportTotaldataDao().deleteJeByCustomerSJDNew(targetDay);
			TaskBeanUtils.getReportTotaldataDao().deleteJeByWangdianSJD(targetDay);
			TaskBeanUtils.getReportTotaldataDao().deleteJeByCitySJD(targetDay);
			TaskBeanUtils.getReportTotaldataDao().deleteJeByProvinceSJD(targetDay);
			TaskBeanUtils.getReportTotaldataDao().deleteJeByBigAreaSJD(targetDay);
			TaskBeanUtils.getReportTotaldataDao().deleteJeByTimeSJD(targetDay);

		}*/
		
//		TaskBeanUtils.getReportTotaldataDao().removeJeByTimePriceSum(targetDay);
	}

	@Override
	public void realClearRedis(LogSuckdataService logSuckdataService,Date targetDay) {
		// TODO 清理查询产生的redis缓存
	}

	@Override
	public String realProcess(LogSuckdataService logSuckdataService,Date targetDay) {
		TaskBeanUtils.getReportTotaldataDao().saveJeByTime(targetDay);
//		TaskBeanUtils.getReportTotaldataDao().saveJeByTimePriceSum(targetDay);
		return "抽取了时间段汇总金额";
	}

	@Override
	// 排序，越大优先度越高. 根据依赖关系调整优先顺序
	public int index() {
		return 1;
	}

	@Override
	public String whoareyou() {
		return "客户金额报表 - 按日全国时间段金额合计";
	}

	@Override
	public int whichLogType() {
		return LogSuckTypeEnum.khje_country.getCode();
	}


	@Override
	public String cacheKeyPerfix() {
		// TODO Auto-generated method stub
		return null;
	}

}
