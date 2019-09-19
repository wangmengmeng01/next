package com.yunda.base.system.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yunda.base.system.domain.LoginUserDO;

/**
 * @Author: pyj
 * @Date: 2019/1/25 10:45
 */
@Service
public interface LoginUserService {
    LoginUserDO get(Long id);
    List<LoginUserDO> list(Map<String, Object> map);

    List<LoginUserDO> listUsers(Map<String, Object> map);

    int count(Map<String, Object> map);

    int save(LoginUserDO user);

    int update(LoginUserDO user);

    int remove(Long userId,int status);

    /**
     * @param userName
     * 获取脱敏身份证号码
     * @return
     */
    LoginUserDO getUserByName(String userName);

    /**
     * @param userName
     * 获取未脱敏身份证号码
     * @return
     */
    LoginUserDO getUserDOByName(String userName);
	/**
	* @Title: getFaceimageByUsername 
	* @Description: TODO 
	* @param username
	* @return String
	* @author 22374
	* @date 2019年2月18日下午2:20:22
	*/
	String getFaceimageByUsername(String username);

    List<String> getBigareaData();

    List<String> getProvinceData();
}
