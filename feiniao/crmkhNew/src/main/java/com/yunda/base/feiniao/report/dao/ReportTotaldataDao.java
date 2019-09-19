package com.yunda.base.feiniao.report.dao;

import com.yunda.base.feiniao.report.bo.Bo_ReportTotaldata;
import com.yunda.base.feiniao.report.domain.ReportTotaldataDO;
import com.yunda.base.system.domain.UserDO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户报表订单统计-总表
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-11 14:54:40
 */
@Mapper
public interface ReportTotaldataDao {

	ReportTotaldataDO get(String bigarea);
	
	List<ReportTotaldataDO> list(Map<String,Object> map);
	
	int cityCount(Map<String,Object> map);
	
	int branchCount(Map<String,Object> map);
	
	int branchCountSJD(Map<String,Object> map);
	
	int cityCountSJD(Map<String,Object> map);
	
	int custCount(Map<String,Object> map);
	
	int branchCustCount(Map<String,Object> map);
	
	int custCountSJD(Map<String,Object> map);
	
	int custCountSJDNew(Map<String,Object> map);
	
	int branchCustCountSJD(Map<String,Object> map);
	
	int branchCustCountSJDNew(Map<String,Object> map);
	
	//总表-市表 省份名称查询获取省id
	String getProvinceIdByPorvinceName(String provinceName);
	
	int save(ReportTotaldataDO reportTotaldata);
	
	int update(ReportTotaldataDO reportTotaldata);
	
	int remove(String bigarea);
	
	int batchRemove(String[] bigareas);
	
	/**
	 * 
	 * 查询查询条件的日期数据是否存在。
	 * 
	 * @param endDate
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月16日
	 */
	int findIfHasDate(@Param(value="endDate") String endDate);

	/**
	 * 
	 * 省级 查询日期下的总表数据。
	 * 
	 * @param provinceId
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月17日
	 */
	List<ReportTotaldataDO> queryProvinceTotalInfo(Bo_ReportTotaldata crmkh_total_tableVO);

	/**
	 * 
	 * 省级 查询日期下的总表数据。
	 * 
	 * @param provinceId
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月17日
	 */
	List<ReportTotaldataDO> queryProvinceTotalInfo(Map<String,Object> map);
	
	
	/**
	 * 总表-城市列表查询
	 * 省级 查询日期下的总表数据。
	 * 
	 * @param provinceId
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月17日
	 */
	List<ReportTotaldataDO> queryCityProvinceTotalInfo(Map<String,Object> map);
	/**
	 * 
	 * 省级 查询日期下的总表数据-时间段。
	 * 
	 * @param provinceId
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月10日
	 */
	List<ReportTotaldataDO> queryProvinceTotalInfoSJD(Bo_ReportTotaldata crmkh_total_tableVO);
	
	/**
	 * 
	 * 省级 查询日期下的总表数据-时间段。
	 * 
	 * @param provinceId
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月10日
	 */
	List<ReportTotaldataDO> queryProvinceTotalInfoSJD(Map<String,Object> map);
	
	
	/**
	 *总表-城市省份
	 * 省级 查询日期下的总表数据-时间段。
	 * 
	 * @param provinceId
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月10日
	 */
	List<ReportTotaldataDO> queryCityProvinceTotalInfoSJD(Map<String,Object> map);

	/**
	 * 
	 * 市级 查询日期下的总表数据。
	 * 
	 * @param provinceId
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月17日
	 */
	List<ReportTotaldataDO> queryCityTotalInfo(@Param(value="provinceId") String provinceId,@Param(value="startDate") String startDate,@Param(value="endDate") String endDate,@Param(value="cityId") String cityId);
	
	
	/**
	 * 
	 * 市级 查询日期下的总表数据。
	 */
	List<ReportTotaldataDO> queryCityTotalInfo(Map<String,Object> map);
	
	/**
	 * 总表-网点页面 城市合计
	 * 市级 查询日期下的总表数据。
	 */
	List<ReportTotaldataDO> queryBranchCityIds(Map<String,Object> map);
	
	/**
	 * 
	 * 市级 查询日期下的总表数据-时间段。
	 * 
	 */
	List<ReportTotaldataDO> queryCityTotalInfoSJD(@Param(value="provinceId") String provinceId,@Param(value="startDate") String startDate,@Param(value="endDate") String endDate,@Param(value="cityId") String cityId);


	/**
	 * 
	 * 市级 查询日期下的总表数据-时间段。
	 * 
	 */
	List<ReportTotaldataDO> queryCityTotalInfoSJD(Map<String,Object> map);
	
	/**
	 * 总表-网点页面 城市合计 时间段
	 * 市级 查询日期下的总表数据-时间段。
	 */
	List<ReportTotaldataDO> queryBranchCityIdsSJD(Map<String,Object> map);
	
	/**
	 * 总表-客户
	 * 
	 */
	List<ReportTotaldataDO> queryCustTotalInfo(Map<String,Object> map);
	
	/**
	 * 总表-客户
	 * 
	 */
	List<ReportTotaldataDO> queryBranchCustTotalInfo(Map<String,Object> map);
	
	/**
	 * 总表-客户-时间段
	 * 
	 */
	List<ReportTotaldataDO> queryCustTotalInfoSJD(Map<String,Object> map);
	
	/**
	 * 总表-客户-时间段
	 * 
	 */
	List<ReportTotaldataDO> queryCustTotalInfoSJDNew(Map<String,Object> map);
	
	/**
	 * 总表-客户-时间段-网点
	 * 
	 */
	List<ReportTotaldataDO> queryBranchCustTotalInfoSJD(Map<String,Object> map);
	
	/**
	 * 总表-客户-时间段-网点
	 * 
	 */
	List<ReportTotaldataDO> queryBranchCustTotalInfoSJDNew(Map<String,Object> map);

	/**
	 * 
	 * 网点级 查询日期下的总表数据。
	 */
	List<ReportTotaldataDO> queryBranchTotalInfo(@Param(value="startDate") String startDate,@Param(value="endDate") String endDate,@Param(value="cityId")String cityId);

	/**
	 * 总表-网点
	 * 网点级 查询日期下的总表数据。
	 */
	List<ReportTotaldataDO> queryBranchTotalInfo(Map<String,Object> map);

	/**
	 * 
	 * 网点级 查询日期下的总表数据-时间段。
	 */
	List<ReportTotaldataDO> queryBranchTotalInfoSJD(@Param(value="startDate") String startDate,@Param(value="endDate") String endDate,@Param(value="cityId")String cityId,@Param(value="startDay")String startDay);

	
	/**
	 * 总表-网点级 时间段
	 * 网点级 查询日期下的总表数据-时间段。
	 */
	List<ReportTotaldataDO> queryBranchTotalInfoSJD(Map<String,Object> map);

	/**
	 * 
	 * 集团合计 查询日期下的合计数据。
	 */
	List<ReportTotaldataDO> queryCompanyTotalInfo(@Param(value="startDate") String startData,@Param(value="endDate") String endData);
	
	
	
	/**
	 * 
	 * 集团合计 查询日期下的合计数据。
	 */
	List<ReportTotaldataDO> queryCompanyTotalInfo(Map<String,Object> map);
	
	/**
	 * 
	 * 集团合计 查询日期下的合计数据-时间段。
	 * 
	 * @param dataTime
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月10日
	 */
	List<ReportTotaldataDO> queryCompanyTotalInfoSJD(@Param(value="startDate") String startData,@Param(value="endDate") String endData);
	
	/**
	 * 
	 * 集团合计 查询日期下的合计数据-时间段。
	 * 
	 * @param dataTime
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月10日
	 */
	List<ReportTotaldataDO> queryCompanyTotalInfoSJD(Map<String,Object> map);
	
	/**
	 * 
	 * 大区合计 查询日期下的合计数据。
	 * 
	 * @param dataTime
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月17日
	 */
	List<ReportTotaldataDO> queryBigareaTotalInfo(Bo_ReportTotaldata crmkh_total_tableVO);
	/**
	 * 
	 * 大区合计 查询日期下的合计数据。
	 * 
	 * @param dataTime
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月17日
	 */
	List<ReportTotaldataDO> queryBigareaTotalInfo(Map<String,Object> map);
	
	/**
	 * 
	 * 大区合计 查询日期下的合计数据-时间段。
	 * 
	 * @param dataTime
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月10日
	 */
	List<ReportTotaldataDO> queryBigareaTotalInfoSJD(Bo_ReportTotaldata crmkh_total_tableVO);
	
	/**
	 * 
	 * 大区合计 查询日期下的合计数据-时间段。
	 * 
	 * @param dataTime
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月10日
	 */
	List<ReportTotaldataDO> queryBigareaTotalInfoSJD(Map<String,Object> map);
	
	
	/**
	 * 
	 * 多省统计 查询日期下的总表数据。
	 * 
	 * @param provinceId
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月17日
	 */
	List<ReportTotaldataDO> queryMultiProvinceTotalInfo(Map<String,Object> map);
	
	/**
	 * 
	 * 多省统计 查询日期下的总表数据。
	 * 
	 * @param provinceId
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月17日
	 */
	List<ReportTotaldataDO> queryMultiProvinceTotalInfo(Bo_ReportTotaldata crmkh_total_tableVO);

	/**
	 * 
	 * 多省统计 查询日期下的总表数据-时间段。
	 * 
	 * @param provinceId
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月10日
	 */
	List<ReportTotaldataDO> queryMultiProvinceTotalInfoSJD(Map<String,Object> map);
	
	
	/**
	 * 
	 * 多省统计 查询日期下的总表数据-时间段。
	 * 
	 * @param provinceId
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月10日
	 */
	List<ReportTotaldataDO> queryMultiProvinceTotalInfoSJD(Bo_ReportTotaldata crmkh_total_tableVO);

	/**
	 * 
	 * 网点统计统计前一百网点数据。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
	List<ReportTotaldataDO> queryBranchMapReport(Bo_ReportTotaldata crmkh_total_tableVO);

	/**
	 * 
	 * 地图 汇总总表数据。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
	List<ReportTotaldataDO> queryTotalMapReport(Bo_ReportTotaldata crmkh_total_tableVO);

	/**
	 * 
	 * 网点客户数据。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    List<ReportTotaldataDO> searchData(
            Map<String, Object> crmkh_report_cust_od_sumData);
	
	/**
	 * 
	 * 网点客户数据-时间段。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月10日
	 */
    List<ReportTotaldataDO> searchDataSJD(
            Map<String, Object> crmkh_report_cust_od_sumData);
	
	/**
	 * 
	 * cqkh客户数据。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
	List<Map<String, Object>> searchCqkhCustomerData(
			HashMap<String, Object> cqkh_paramMap);
	
	
	/**
	 * 
	 * 网点客户数量。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int countSearchData(
            Map<String, Object> crmkh_report_cust_od_sumData);
	
	/**
	 * 
	 * 网点客户数量-时间段。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年10月10日
	 */
    int countSearchDataSJD(
            Map<String, Object> crmkh_report_cust_od_sumData);
	
	/**
	 * 
	 * 抽数-客户汇总。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int saveLjByCustomer(
            Date targetDay);
	/**
	 * 抽数-客户汇总。
	 * 
	 * 查询日期为2019.1.1日前的，按照老逻辑客户分段，2019.1.1日后的，按照新逻辑客户分段
	 * 
	 * @param targetDay
	 * @return
	 */
    int saveLjByCustomerNew(
            Date targetDay);
	/**
	 * 
	 * 抽数-删除客户汇总。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int removeLjByCustomer(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-公司汇总。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int saveLjByGs(
            Date targetDay);
	/**
	 * 
	 * 抽数-删除公司汇总。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int removeLjByGs(
            Date targetDay);
	
	
	
	/**
	 * 
	 * 抽数-公司汇总明细。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int saveLjByWangdian(
            Date targetDay);
	
	/**
	 * 抽数-公司汇总明细。
	 * 
	 * 查询日期为2019.1.1日前的，按照老逻辑客户分段，2019.1.1日后的，按照新逻辑客户分段
	 * 
	 * @param targetDay
	 * @return
	 */
    int saveLjByWangdianNew(
            Date targetDay);
	/**
	 * 
	 * 抽数-删除公司汇总明细。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int removeLjByWangdian(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-城市汇总明细。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    /*int saveLjByCity(
            Date targetDay);*/
    int saveLjByCity(
    		HashMap<String, Object> targetDay);
	/**
	 * 
	 * 抽数-删除城市汇总明细。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int removeLjByCity(
            Date targetDay);
	
	
	/**
	 * 
	 * 抽数-省份汇总明细。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int saveLjByProvince(
            Date targetDay);
	/**
	 * 
	 * 抽数-删除城市汇总明细。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int removeLjByProvince(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-大区汇总明细。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int saveLjByBigArea(
            Date targetDay);
	/**
	 * 
	 * 抽数-删除大区汇总明细。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int removeLjByBigArea(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-时间段汇总明细。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int saveLjByAll(
            Date targetDay);
	/**
	 * 
	 * 抽数-删除时间段汇总明细。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int removeLjByAll(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-各类客户金额。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int saveJeByCustomer(
            HashMap<String, Object> targetDay);
	
	/**
	 * 抽数-各类客户金额。
	 * 查询日期为2019.1.1日前的，按照老逻辑客户分段，2019.1.1日后的，按照新逻辑客户分段
	 * @param targetDay
	 * @return
	 */
    int saveJeByCustomerNew(
            HashMap<String, Object> targetDay);
	
	/**
	 * 
	 * 抽数-删除各类客户金额。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int removeJeByCustomer(
            Date targetDay);
    
	/**
	 * 
	 * 抽数-删除各类客户金额-时间段。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int removeJeByCustomerSJD(
            Date targetDay);
    
	/**
	 * 
	 * 判断当前跑数日期在客户金额时间段表里是否包含
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int countJeByCustomerSJD(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-网点金额。
	 */
    int saveJeByWangdian(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-删除网点金额。
	 * 
	 */
    int removeJeByWangdian(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-城市金额。
	 * 
	 */
    int saveJeByCity(
    		HashMap<String, Object> targetDay); 
	
	/**
	 * 
	 * 抽数-删除城市金额。
	 * 
	 */
    int removeJeByCity(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-省份金额。
	 * 
	 */
    int saveJeByProvince(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-删除省份金额。
	 * 
	 */
    int removeJeByProvince(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-大区金额。
	 * 
	 */
    int saveJeByBigArea(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-删除大区金额。
	 * 
	 */
    int removeJeByBigArea(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-按时间汇总金额。
	 */
    int saveJeByTime(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-删除按时间金额。
	 */
    int removeJeByTime(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-按客户汇总金额。
	 * 
	 */
    int saveJeByCustomerStats(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-删除按客户汇总金额。
	 * 
	 */
    int removeJeByCustomerStats(
            Date targetDay);
	
	
	/**
	 * 
	 * 抽数-按公司汇总金额。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int saveJeByGsPriceSum(
            List<HashMap<String, Object>> targetDay);
	
	/**
	 * 
	 * 抽数-删除按公司汇总金额。
	 * 
	 */
    int removeJeByGsPriceSum(
            HashMap<String, Object> targetDay);
	
	/**
	 * 
	 * 抽数-按城市汇总金额。
	 */
    int saveJeByCityPriceSum(
            List<HashMap<String, Object>> targetDay);
	
	/**
	 * 
	 * 抽数-删除按城市汇总金额。
	 * 
	 */
    int removeJeByCityPriceSum(
            HashMap<String, Object> targetDay);
	
	/**
	 * 
	 * 抽数-按省份汇总金额。
	 * 
	 */
    int saveJeByProvincePriceSum(
            List<HashMap<String, Object>> targetDay);
	
	/**
	 * 
	 * 抽数-删除按省份汇总金额。
	 * 
	 */
    int removeJeByProvincePriceSum(
            HashMap<String, Object> targetDay);
	
	/**
	 * 
	 * 抽数-按大区汇总金额。
	 * 
	 */
    int saveJeByBigAreaPriceSum(
            List<HashMap<String, Object>> targetDay);
	
	/**
	 * 
	 * 抽数-删除按大区汇总金额。
	 * 
	 */
    int removeJeByBigAreaPriceSum(
            HashMap<String, Object> targetDay);
	
	/**
	 * 
	 * 抽数-按时间汇总金额。
	 */
    int saveJeByTimePriceSum(
            List<HashMap<String, Object>> targetDay);
	
	/**
	 * 
	 * 抽数-删除按时间汇总金额。
	 * 
	 */
    int removeJeByTimePriceSum(
            HashMap<String, Object> targetDay);
	
	
	/**
	 * 
	 * 生成gp汇总数据。
	 * 
	 * @param map
	 */
	int saveGpSource(List<Map<String,Object>> map);
	
	/**
	 * 
	 * 生成gp客户汇总数据。
	 * 
	 * @param map
	 */
	int saveGpCustomerSource(List<Map<String,Object>> map);
	
	/**
	 * 查询所属大区省编码。
	 */
    List<HashMap<String,Object>> searchBigAreaCode(@Param(value = "startDay") String startDay,@Param(value = "bigAreaName") String[] bigAreaName);
    
	/**
	 * 查询所属大区城市编码。
	 */
    List<HashMap<String,Object>> getCityIdsByBigarea(@Param(value = "endDate") String endDate,@Param(value = "bigAreaName") String[] bigAreaName);
    
    
	/**
	 * 
	 * 查询所属省份城市编码。
	 * 
	 */
    List<HashMap<String,Object>> getCityIdsByProvinces(@Param(value = "endDate") String endDate,@Param(value = "provinceIds") List<Long> provinceIds);
    
	/**
	 * 
	 * 查询所属省份公司编码。
	 * 
	 * @param bigAreaName
	 */
    List<HashMap<String,Object>> getGSByProvinces(@Param(value = "endDate") String endDate,@Param(value = "provinceIds") String[] provinceIds);

	/**
	 * 
	 * 抽数-删除gp客户汇总。
	 * 
	 */
    int removeGpCustomerSource(
            Date targetDay);
	
	/**
	 * 
	 * 抽数-删除gp汇总。
	 * 
	 */
    int removeGpSource(
            Date targetDay);
		
	/**
	 * 
	 * 抽数-按客户时间段汇总金额。
	 * 
	 */
    int saveJeByCustomerSJD(
            Map<String, Object> targetDay);
	
	/**
	 * 抽数-按客户时间段汇总金额。
	 * 2019-01-01开始执行新的返利逻辑
	 * 
	 */
    int saveJeByCustomerSJDNew(
            Map<String, Object> targetDay);
    
	/**
	 * 删除-按客户时间段汇总金额。
	 * 2019-01-01开始执行新的返利逻辑
	 * 
	 */
    int deleteJeByCustomerSJDNew(
    		String targetDay);
    
	/**
	 * 删除-按客户时间段汇总揽件。
	 * 2019-01-01开始执行新的返利逻辑
	 * 
	 */
    int deleteLJByCustomerSJDNew(
    		String targetDay);
	
	/**
	 * 
	 * 抽数-删除-按客户时间段汇总金额。
	 * 
	 */
/*    int removeJeByCustomerSJD(
            HashMap<String, Object> targetDay);*/
	
	/**
	 * 
	 * 抽数-按网点时间段汇总金额。
	 * 
	 */
    int saveJeByWangdianSJD(
            Map<String, Object> targetDay);
    
	/**
	 * 
	 * 删除-按网点时间段汇总金额。
	 * 
	 */
    int deleteJeByWangdianSJD(
            String targetDay);
    
	/**
	 * 
	 * 删除-按网点时间段汇总揽件。
	 * 
	 */
    int deleteLJByWangdianSJD(
            String targetDay);
	
	/**
	 * 
	 * 抽数-删除-按网点时间段汇总金额。
	 * 
	 */
    int removeJeByWangdianSJD(
            Map<String, Object> targetDay);
	
	/**
	 * 
	 * 抽数-按城市时间段汇总金额。
	 * 
	 */
    int saveJeByCitySJD(
            Map<String, Object> targetDay);
    
    
	/**
	 * 
	 * 删除-按城市时间段汇总金额。
	 * 
	 */
    int deleteJeByCitySJD(
            String targetDay);
    
    
	/**
	 * 
	 * 删除-按城市时间段汇总揽件。
	 * 
	 */
    int deleteLJByCitySJD(
            String targetDay);
	
	/**
	 * 
	 * 抽数-删除-按城市时间段汇总金额。
	 * 
	 */
    int removeJeByCitySJD(
            HashMap<String, Object> targetDay);
	
	/**
	 * 
	 * 抽数-按省份时间段汇总金额。
	 * 
	 */
    int saveJeByProvinceSJD(
            Map<String, Object> targetDay);
    
	/**
	 * 
	 * 删除-按省份时间段汇总金额。
	 * 
	 */
    int deleteJeByProvinceSJD(
            String targetDay);
    
    
	/**
	 * 
	 * 删除-按省份时间段汇总揽件。
	 * 
	 */
    int deleteLJByProvinceSJD(
            String targetDay);
	
	/**
	 * 
	 * 抽数-删除-按省份时间段汇总金额。
	 * 
	 */
    int removeJeByProvinceSJD(
            HashMap<String, Object> targetDay);
	
	/**
	 * 
	 * 抽数-按大区时间段汇总金额。
	 * 
	 */
    int saveJeByBigAreaSJD(
            Map<String, Object> targetDay);
    
	/**
	 * 
	 * 删除-按大区时间段汇总金额。
	 * 
	 */
    int deleteJeByBigAreaSJD(
           String targetDay);
    
    
	/**
	 * 
	 * 删除-按大区时间段汇总揽件。
	 * 
	 */
    int deleteLJByBigAreaSJD(
           String targetDay);
	
	/**
	 * 
	 * 抽数-删除-按大区时间段汇总金额。
	 */
    int removeJeByBigAreaSJD(
            HashMap<String, Object> targetDay);
	
	/**
	 * 
	 * 抽数-按时间段汇总金额。
	 * 
	 */
    int saveJeByTimeSJD(
            Map<String, Object> targetDay);
	
	/**
	 * 
	 * 删除-按时间段汇总金额。
	 * 
	 */
    int deleteJeByTimeSJD(
            String targetDay);
    
	/**
	 * 
	 * 删除-按时间段汇总揽件。
	 * 
	 */
    int deleteLJByTimeSJD(
            String targetDay);
	/**
	 * 
	 * 抽数-删除-按时间段汇总金额。
	 */
    int removeJeByTimeSJD(
            HashMap<String, Object> targetDay);
	
	/**
	 * 
	 * 抽数-删除-时间段统计表。
	 * 
	 */
    int removeTotalPriceConfig(
            HashMap<String, Object> targetDay);
	
	/**
	 * 
	 * 抽数-按客户时间段汇总金额。
	 * 
	 */
    int saveJeByCustomerStatsSJD2(
            List<HashMap<String, Object>> targetDay);
	
	/**
	 * 
	 * 抽数-删除-按客户时间段汇总金额。
	 * 
	 */
    int removeJeByCustomerStatsSJD2(
            HashMap<String, Object> targetDay);

	/**
	 * 
	 * 查询-客户时间段汇总数据是否存在。
	 * 
	 */
    int countJeByCustomerSJD(
    		Map<String, Object> map);
    
	/**
	 * 
	 * 查询-客户时间段汇总数据是否存在。
	 * 
	 */
    int countJeByCustomerSJD(
            HashMap<String, String> targetDay);
	
	
	/**
	 * 
	 * 插入-日期数据。
	 * 
	 */
    int insertTotalPriceConfig(
            HashMap<String, Object> targetDay);
	
	/**
	 * 
	 * 查询-日期数据。
	 */
    List<HashMap<String,Object>>  searchTotalPriceConfig(
            HashMap<String, Object> targetDay);
    
	/**
	 * 2019-01-01以前  新逻辑
	 * 生成具体时间段客户揽件
	 * 
	 */
    int saveLjByCustomerSJD(
            Map<String, Object> zbljMap);
	
	/**
	 * 2019-01-01以前 旧逻辑
	 * 删除具体时间段客户揽件
	 * 
	 */
    int removeLjByCustomerSJD(
            Map<String, Object> zbljMap);
    
	/**
	 * 2019-01-01以前  新逻辑
	 * 生成具体时间段客户揽件
	 * 
	 */
    int saveLjByCustomerSJDNew(
            Map<String, Object> zbljMap);
	
	/**
	 * 2019-01-01以前 旧逻辑
	 * 删除具体时间段客户揽件
	 * 
	 */
    int removeLjByCustomerSJDNew(
            Map<String, Object> zbljMap);
    
	/**
	 * 2019-01-01以前  新逻辑
	 * 生成具体时间段网点揽件
	 * 
	 */
    int saveLjByWangdianSJD(
            Map<String, Object> zbljMap);
	
	/**
	 * 2019-01-01以前 旧逻辑
	 * 删除具体时间段网点揽件
	 * 
	 */
    int removeLjByWangdianSJD(
            Map<String, Object> zbljMap);
    
	/**
	 * 2019-01-01及以后 新逻辑
	 * 生成具体时间段网点揽件
	 * 
	 */
    int saveLjByWangdianSJDNew(
            Map<String, Object> zbljMap);
	
	/**
	 * 2019-01-01及以后 新逻辑
	 * 删除具体时间段网点揽件
	 * 
	 */
    int removeLjByWangdianSJDNew(
            Map<String, Object> zbljMap);
    
	/**
	 * 抽数-时间段-城市汇总明细。
	 */
    int saveLjByCitySJD(
    		Map<String, Object> zbljMap);
    
	/**
	 * 删除-时间段-城市汇总明细。
	 */
    int removeLjByCitySJD(
    		Map<String, Object> zbljMap);
    
	/**
	 * 抽数-时间段-省份汇总明细。
	 */
    int saveLjByProvinceSJD(
    		Map<String, Object> zbljMap);
    
	/**
	 * 删除-时间段-省份汇总明细。
	 */
    int removeLjByProvinceSJD(
    		Map<String, Object> zbljMap);
	/**
	 * 抽数-时间段-大区汇总明细。
	 */
    int saveLjByBigAreaSJD(
    		Map<String, Object> zbljMap);
    
	/**
	 * 删除-时间段-大区汇总明细。
	 */
    int removeLjByBigAreaSJD(
    		Map<String, Object> zbljMap);
    
	/**
	 * 抽数-时间段-全国汇总明细。
	 */
    int saveLjByAllSJD(
    		Map<String, Object> zbljMap);
    
	/**
	 * 删除-时间段-全国汇总明细。
	 */
    int removeLjByAllSJD(
    		Map<String, Object> zbljMap);
    
    List<ReportTotaldataDO> getSJDDate(Date targetDay);
}
