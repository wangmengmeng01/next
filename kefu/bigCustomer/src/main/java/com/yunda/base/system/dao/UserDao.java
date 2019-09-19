package com.yunda.base.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.yunda.base.system.domain.UserDO;

/**
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-10-03 09:45:11
 */
@Mapper
public interface UserDao {

	UserDO get(Long userId);
	
	List<UserDO> list(Map<String, Object> map);
	
	List<UserDO> listUsers(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(UserDO user);
	
	int update(UserDO user);
	
	int remove(Long userId);
	
	int batchRemove(Long[] userIds);
	
	Long[] listAllDept();

	List<UserDO> queryLoginInfo(String username);
//统一授权
	Long getUserId(@Param("userId") String userId);

	String getRoleId(@Param("userRole1") String userRole1);
	
	List<String> getUserRole(@Param("long1") Long long1);
	
	boolean isExist(@Param("userId") String userId);
	
	void insertPasswordAndUser(@Param("userId") String userId, @Param("passwordOrgin") String passwordOrgin, @Param("name") String name, @Param("status") String status, @Param("companycode") String companycode);

	void updatePasswordAndUser(@Param("userId") String userId, @Param("passwordOrgin") String passwordOrgin, @Param("name") String name, @Param("status") String status, @Param("companycode") String companycode);

	String getName(@Param("username") String username);

	String selectUserByBase64(@Param("username") String username);

	void updateBase64(UserDO user);

	/** 
	* @Title: getAllUserRole 
	* @Description: TODO 
	* @return List<String>
	* @author 22374
	* @date 2019年2月24日下午2:42:04
	*/
	List<String> getAllUserRole();

	List<String> getRoleIds(@Param("userId") String userId);
	/**
	 * 根据网点编码获取网点名称
	 * @return
	 */
	String getMcByBranchCode(@Param("branchCode") String branchCode);

	/**
	 * 修改状态
	 * @param userId
	 * @param status
	 * @return
	 */
    int stateUpdate(@Param("userId") Long userId,@Param("status") int status);
}
