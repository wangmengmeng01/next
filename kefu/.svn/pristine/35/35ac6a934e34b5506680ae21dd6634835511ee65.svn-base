package com.yunda.base.feiniao.report.service;

import com.yunda.base.feiniao.report.bo.Bo_ReportFluctuate;
import com.yunda.base.feiniao.report.domain.ExportReportFluctuateCustDO;
import com.yunda.base.feiniao.report.domain.ExportReportFluctuateDataDO;
import com.yunda.base.feiniao.report.domain.ReportFluctuateDO;
import com.yunda.base.system.domain.UserDO;

import java.text.ParseException;
import java.util.List;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-18 13:31:19
 */
public interface ReportFluctuateService {

	
	List<ReportFluctuateDO> custNumReport(Bo_ReportFluctuate bo_ReportFluctuate, UserDO loginUser) throws ParseException;
	
	List<ExportReportFluctuateDataDO> filterData(
            List<ReportFluctuateDO> reportWarningData);
	
	int custBDcount(Bo_ReportFluctuate boReportFluctuate,UserDO loginUser)throws ParseException;
	
	List<ExportReportFluctuateCustDO> filterDataCust(
            List<ReportFluctuateDO> reportWarningData);
}
