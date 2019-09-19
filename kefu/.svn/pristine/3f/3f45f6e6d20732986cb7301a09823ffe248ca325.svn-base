package com.yunda.base.feiniao.report.dao;

import com.yunda.base.feiniao.report.domain.ReportWarningDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-16 10:45:24
 */
@Mapper
public interface ReportWarningDao {

	ReportWarningDO get(String bigarea);
	
	List<ReportWarningDO> list(Map<String,Object> map);
	
	int count(ReportWarningDO reportWarningDO);
	
	int save(ReportWarningDO reportWarning);
	
	int update(ReportWarningDO reportWarning);
	
	int remove(String bigarea);
	
	int batchRemove(String[] bigareas);

//doWithProvince()	
//查询省份数据
	List<ReportWarningDO> queryprovince(Map<String, Object> custMap);
//查询集团数据
	List<ReportWarningDO> queryallcount(Map<String, Object> custMap);
//查询大区数据
	List<ReportWarningDO> querybigarea(Map<String, Object> custMap);
	
	
//doWithCity()方法的dao
	List<ReportWarningDO> doWithCityprovince(Map<String, Object> custMap);
	
	List<ReportWarningDO> doWithCitycity(Map<String, Object> custMap);
	
//doWithBranch()方法
	List<ReportWarningDO> doWithBranchshi(Map<String, Object> custMap);
	
	List<ReportWarningDO> doWithBranchgs(Map<String, Object> custMap);
	
	List<ReportWarningDO> doWithCustomercust(Map<String, Object> custMap);
//
	List<ReportWarningDO> doWithCustPronumbercust(Map<String, Object> custMap);
	
	
	/**
	 * 
	 * 抽数前判断是否存在这个时间段的数据。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int countTask(
            Map<String, Object> param);
	/**
	 * 
	 * 抽数-时间段汇总明细。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int saveYjByAll(
            Map<String, Object> param);
	
	/**
	 * 抽数-时间段汇总明细。
	 * 
	 * //查询日期为2019.1.1日前的，按照老逻辑客户分段，2019.1.1日后的，按照新逻辑客户分段
	 * @param param
	 * @return
	 */
    int saveYjByAllNew(
            Map<String, Object> param);
	
	/**
	 * 
	 * 抽数-删除时间段汇总明细。
	 * 
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月18日
	 */
    int removeYjByAll(
            String search_month);
	
	/**
	 * 
	 * 抽数前判断是否存在这个时间段的数据。
	 */
    int countYJWeekTask(String searchWeek);
	/** 
	 * 抽数-预警周基础表。
	 */
    int saveYjByWeek(Map<String, Object> param);
	/**
	 *  抽数-删除预警周基础数据。
	 */
    int removeYjByWeek(String searchWeek);
	
	//判断选选时间段是否有数据    如果count =0 没有数据    就生成要查询的预警临时数据
	int findIfHasDate(String pricePrefix);
	//生成要查询的预警临时数据
	List<ReportWarningDO> reportWarning(Map<String, Object> custMap);
//网点权限
	List<ReportWarningDO> queryWangdian(Map<String, Object> custMap);

	


	
}
