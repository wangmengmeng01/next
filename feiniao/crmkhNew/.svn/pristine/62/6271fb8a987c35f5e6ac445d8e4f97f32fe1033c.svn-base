package com.yunda.base.system.service;

import com.yunda.base.system.domain.AlarmDO;
import com.yunda.base.system.domain.LoginUserDO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-02-21151731
 */
public interface AlarmService {
	
	AlarmDO get(String sessionId);
	
	List<AlarmDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(AlarmDO alarm);
	
	int update(AlarmDO alarm);
	
	int remove(String sessionId);
	
	int batchRemove(String[] sessionIds);

	/**
	 * 根据用户的userId获取角色名字
	 * @param userId
	 * @return
	 */
	String queryRoleNameByUserId(Long userId);

	/**
	 * 根据用户的userId获取对应的区名
	 * @param userId
	 * @return
	 */
	String queryBigareaNameByUserId(Long userId);

	/**
	 * 根据大区的名字获取区总信息
	 * @param bigareaName
	 * @return
	 */
	LoginUserDO queryBigareaInfoByBigareaName(String bigareaName);

	/**
	 * 查询勾选了短信类型的总部人员信息
	 * @return
	 */
    List<LoginUserDO> getZongBuData();

	/**
	 * 根据登录人账号获取当前登录人安全维护的信息
	 * @param username
	 * @return
	 */
	LoginUserDO getLoginUserByUserName(String username);
}
