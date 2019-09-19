package com.yunda.base.feiniao.report.dao;

import com.yunda.base.feiniao.report.domain.ReportJurisdictionTableDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 权限表
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-18 17:02:50
 */
@Mapper
public interface ReportJurisdictionTableDao {

	ReportJurisdictionTableDO get(Integer jobNumber);
	
	List<ReportJurisdictionTableDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(ReportJurisdictionTableDO reportJurisdictionTable);
	
	int update(ReportJurisdictionTableDO reportJurisdictionTable);
	
	int remove(Integer job_number);
	
	int batchRemove(Integer[] jobNumbers);
	
	List<ReportJurisdictionTableDO> getReportJurisdictionTableDO(Map<String,Object> map);
}
