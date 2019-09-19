package com.yunda.base.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yunda.base.system.domain.AlarmDO;
import com.yunda.base.system.domain.LoginUserDO;

/**
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-02-21151731
 */
@Mapper
public interface AlarmDao {

	AlarmDO get(String sessionId);
	
	List<AlarmDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(AlarmDO alarm);
	
	int update(AlarmDO alarm);
	
	int remove(String session_id);
	
	int batchRemove(String[] sessionIds);

	/**
	 * 根据用户的userId来获取角色名字
	 * @param userId
	 * @return
	 */
	String queryRoleNameByUserId(Long userId);

	/**
	 * 根据用户的userId来获取区名
	 * @param userId
	 * @return
	 */
	String queryBigareaNameByUserId(Long userId);

	/**
	 * 根据大区名字获取区总信息
	 * @param bigareaName
	 * @return
	 */
	LoginUserDO queryBigareaInfoByBigareaName(String bigareaName);

	/**
	 * 获取勾选了短信类型的总部人信息
	 * @return
	 */
    List<LoginUserDO> getZongBuData();

	/**
	 * 根据账号获取安全维护信息
	 * @param username
	 * @return
	 */
	LoginUserDO getLoginUserByUserName(@Param("username") String username);
}
