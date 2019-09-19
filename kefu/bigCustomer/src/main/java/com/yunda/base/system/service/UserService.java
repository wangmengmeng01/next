package com.yunda.base.system.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.yunda.base.common.domain.Tree;
import com.yunda.base.system.domain.DeptDO;
import com.yunda.base.system.domain.UserDO;
import com.yunda.base.system.vo.UserVO;

@Service
public interface UserService {
	UserDO get(Long userId);

	List<UserDO> list(Map<String, Object> map);
	
	List<UserDO> listUsers(Map<String, Object> map);

	int count(Map<String, Object> map);

	int save(UserDO user);

	int update(UserDO user);

	int remove(Long userId);

	int batchremove(Long[] userIds);

	boolean exit(Map<String, Object> params);

	Set<String> listRoles(Long userId);

	int resetPwd(UserVO userVO, UserDO userDO) throws Exception;
	int adminResetPwd(UserVO userVO) throws Exception;
	Tree<DeptDO> getTree();

	/**
	 * 更新个人信息
	 * @param userDO
	 * @return
	 */
	int updatePersonal(UserDO userDO);

	/**
	 * 更新个人图片
	 * @param file 图片
	 * @param avatar_data 裁剪信息
	 * @param userId 用户ID
	 * @throws Exception
	 */
    Map<String, Object> updatePersonalImg(MultipartFile file, String avatar_data, Long userId) throws Exception;
    
    /**
     * 维护用户省份管辖范围
     * @param user
     * @return
     */
    int updateProvince(UserDO user);
    
    int updateBigarea(UserDO user);
    
  //登录时根据用户名查询用户信息  并存入到session
  	List<UserDO> queryLoginInfo(String username);
  	
//统一授权  	
  	String getRoleId(String userRole1);
  	
  	Long getUserId(String username);

	List<String> getUserRole(Long long1);

	boolean isExist(String username);
	
	void insertPasswordAndUser(String userId, String passwordOrgin, String name, String status, String companycode);

	void updatePasswordAndUser(String userId, String passwordOrgin1, String name, String status, String companycode);



	String selectUserByBase64(String username);


	void updateBase64(UserDO user);

	/** 
	* @Title: getAllUserRole 
	* @Description: TODO 
	* @return List<String>
	* @author 22374
	* @date 2019年2月24日下午2:39:47
	*/
	List<String> getAllUserRole();

	/**
	 * 根据网点编码获取网点名称
	 * @return
	 */
	String getMcByBranchCode(String branchCode);

	/**
	 * 改变状态是否启用或禁止
	 * @param userId
	 * @param status
	 * @return
	 */
    int stateUpdate(Long userId, int status);

    //boolean checkData(UserDO row,UserDO loginUser);
}
