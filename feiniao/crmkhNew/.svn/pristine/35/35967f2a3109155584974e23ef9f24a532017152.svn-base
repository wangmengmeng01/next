package com.yunda.base.system.dao;

import com.yunda.base.system.domain.LoginUserDO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: pyj
 * @Date: 2019/1/25 10:50
 */
@Mapper
public interface LoginUserDao {
    LoginUserDO  get(Long userId);
    
    List<LoginUserDO> list(Map<String,Object> map);

    List<LoginUserDO> listUsers(Map<String,Object> map);

    int count(Map<String,Object> map);

    int countByUserName(@Param("username") String username);

    int save(LoginUserDO user);

    int update(LoginUserDO user);

    int remove(@Param("userName") String userName);
    //int remove(@Param("userId") Long userId, @Param("status") int status);
    LoginUserDO getUserByName(@Param("userName") String userName);

	/** 
	* @Title: getFaceimageByUsername 
	* @Description: TODO 
	* @param username
	* @return String
	* @author 22374
	* @date 2019年2月18日下午2:24:27
	*/
	String getFaceimageByUsername(String username);

    List<String> getBigareaData();

    List<String> getProvinceData();

}
