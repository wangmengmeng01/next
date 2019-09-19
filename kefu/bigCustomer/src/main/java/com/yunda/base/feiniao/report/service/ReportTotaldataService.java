package com.yunda.base.feiniao.report.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunda.base.feiniao.report.bo.Bo_ReportTotaldata;
import com.yunda.base.feiniao.report.domain.ExportCustBranchReportTotaldataDO;
import com.yunda.base.feiniao.report.domain.ExportCustReportTotaldataDO;
import com.yunda.base.feiniao.report.domain.ExportReportTotaldataDO;
import com.yunda.base.feiniao.report.domain.ReportTotaldataDO;
import com.yunda.base.system.domain.UserDO;

/**
 * 客户报表订单统计-总表
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-11 14:54:40
 */
public interface ReportTotaldataService {
	
	ReportTotaldataDO get(String bigarea);
	
	List<ReportTotaldataDO> list(Bo_ReportTotaldata bo_ReportTotaldata,UserDO cipuser);
	
	int count(Map<String, Object> map);
	
	int save(ReportTotaldataDO reportTotaldata);
	
	int update(ReportTotaldataDO reportTotaldata);
	
	int remove(String bigarea);
	
	int batchRemove(String[] bigareas);
	
	/**
	 * 
	 * 地图 总表数据。
	 * 
	 * @param bo_ReportTotaldata
	 * @param cipuser
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
	List<ReportTotaldataDO> queryProvinceMapReport(Bo_ReportTotaldata bo_ReportTotaldata,UserDO cipuser);

	
	/**
	 * 
	 * 网点前100 总表数据。
	 * 
	 * @param bo_ReportTotaldata
	 * @param cipuser
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
	List<ReportTotaldataDO> queryBranchMapReport(Bo_ReportTotaldata bo_ReportTotaldata,UserDO cipuser);

	
	/**
	 * 
	 * 地图 获取总表统计。
	 * 
	 * @param bo_ReportTotaldata
	 * @param cipuser
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
	List<ReportTotaldataDO> queryTotalMapReport(Bo_ReportTotaldata bo_ReportTotaldata,UserDO cipuser);

//	//用户行为权限资源获取
//    public List<CIPResource> getResources(CIPUser user, String resourceId);
	
	List<ReportTotaldataDO> searchData(
            Map<String, Object> crmkh_report_cust_od_sumData);
	
	int countSearchData(
            Map<String, Object> crmkh_report_cust_od_sumData);
	
	List<ExportReportTotaldataDO> filterData(
            List<ReportTotaldataDO> reportTotaldata);
	
	List<ExportCustReportTotaldataDO> filterCustData(
            List<ReportTotaldataDO> reportTotaldata);

	List<Map<String,Object>> queryGpCustomerSource(Map<String, Object> map, String dsId);

	List<Map<String,Object>> queryGpSource(Map<String, Object> map, String dsId);
//获取菜鸟 京东的商家id和店铺名称
List<Map<String, Object>> searchSellerCN(
        Map<String, Object> map, String dsId);
	List<Map<String, Object>> searchSellerJD(
            HashMap<String, Object> cqkh_paramMap, String dsId);

	List<ExportCustBranchReportTotaldataDO> filterCustBranchData(
            List<ReportTotaldataDO> reportTotaldata);

}
