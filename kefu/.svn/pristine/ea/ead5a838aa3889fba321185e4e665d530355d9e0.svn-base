package com.yunda.base.feiniao.customer.dao;

import com.yunda.base.feiniao.customer.domain.CooperatePeerDO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-14094844
 */
@Mapper
public interface CooperatePeerDao {

	List<CooperatePeerDO> get(@Param("branchCode")String branchCode,@Param("productType") String productType);
	
	List<CooperatePeerDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(CooperatePeerDO cooperatePeer);
	
	int update(CooperatePeerDO cooperatePeer);
	
	int remove(@Param("branchCode") String branchCode,@Param("productType") String productType);
	
	int batchRemove(String[] ids);
}
