package com.yunda.base.system.dao;

import com.yunda.base.system.domain.UserBigareaDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户与大区对应关系
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-08-17 11:08:47
 */
@Mapper
public interface UserBigareaDao {

	UserBigareaDO get(Long id);
	
	List<UserBigareaDO> list(Map<String,Object> map);
	
	int count(Map<String,Object> map);
	
	int save(UserBigareaDO userBigarea);
	
	int update(UserBigareaDO userBigarea);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
	
	List<String> listBigareaNameByUserId(Long id);
	
	int removeByUserId(Long userId);
	
	int batchSave(List<UserBigareaDO> list);

	List<String> queryUP(Long userId);
}
