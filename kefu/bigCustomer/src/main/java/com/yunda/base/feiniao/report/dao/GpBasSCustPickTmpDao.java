package com.yunda.base.feiniao.report.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.common.multi.annotation.DataSourceAnnotation;
import com.yunda.base.feiniao.report.domain.GpBasSCustPickTmpDO;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-31 10:34:48
 */
@Mapper
public interface GpBasSCustPickTmpDao {

	GpBasSCustPickTmpDO get(Integer pickBrchCd);
	
	List<GpBasSCustPickTmpDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(GpBasSCustPickTmpDO gpBasSCustPickTmp);
	
	int update(GpBasSCustPickTmpDO gpBasSCustPickTmp);
	
	int remove(Integer pick_brch_cd);
	
	int batchRemove(Integer[] pickBrchCds);
	
	/**
	 * 
	 * 查询gp表数据。
	 * 
	 * @param map
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月31日
	 */
	List<Map<String,Object>> queryGpSource(Map<String,Object> map);

	/**
	 * 
	 * gp表汇总客户数据。
	 * 
	 * @param map
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年7月31日
	 */
	List<Map<String,Object>> queryGpCustomerSource(Map<String,Object> map);
	
	
	/**
	 * 
	 * gp表汇总数。
	 * 
	 * @param map
	 * @return
	 * @author bianxiaolong
	 * @since 1.0.0_2018年8月27日
	 */
	int countGpSource();
	
	/**
	 * 获取菜鸟/京东库的商家id和店铺名称
	 * @param cqkh_paramMap
	 * @param string
	 * @return
	 */
	@DataSourceAnnotation
	List<Map<String, Object>> searchSellerCN(
			Map<String, Object> map);
	@DataSourceAnnotation
	List<Map<String, Object>> searchSellerJD(
			HashMap<String, Object> cqkh_paramMap);
}