package com.yunda.base.feiniao.report.service;

import com.yunda.base.feiniao.report.bo.Bo_ReportWarning;
import com.yunda.base.feiniao.report.domain.ExportReportWarningBranchDO;
import com.yunda.base.feiniao.report.domain.ExportReportWarningdataDO;
import com.yunda.base.feiniao.report.domain.ReportWarningDO;
import com.yunda.base.system.domain.UserDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-16 10:45:24
 */
public interface ReportWarningService {
	
	ReportWarningDO get(String bigarea);
	
	List<ReportWarningDO> list(Map<String, Object> map);
	
	int count(ReportWarningDO reportWarningDO);
	
	int save(ReportWarningDO reportWarning);
	
	int update(ReportWarningDO reportWarning);
	
	int remove(String bigarea);
	
	int batchRemove(String[] bigareas);

	List<ExportReportWarningdataDO> filterData(
            List<ReportWarningDO> reportWarningData);
	List<ExportReportWarningBranchDO> filterBranchData(
            List<ReportWarningDO> reportWarningData);
	List<ReportWarningDO> list(Bo_ReportWarning boReportWarning, UserDO loginUser);
}
