/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.yundagalaxy.management.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yundagalaxy.management.entity.TmpEmployeeInfo;
import com.yundagalaxy.management.vo.TmpEmployeeInfoVO;
import org.springblade.core.secure.BladeUser;

import java.util.List;

/**
 * 非员工基本信息表 服务类
 *
 * @author dongfeng
 * @since 2019-10-21
 */
public interface ITmpEmployeeInfoService extends IService<TmpEmployeeInfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param tmpEmployeeInfo
	 * @return
	 */
	IPage<TmpEmployeeInfoVO> selectTmpEmployeeInfoPage(IPage<TmpEmployeeInfoVO> page, TmpEmployeeInfoVO tmpEmployeeInfo);

	List<TmpEmployeeInfoVO> selectTmpEmployeeInfoList(TmpEmployeeInfoVO tmpEmployeeInfo);
	/**
	 * 非员工基本信息表获取最大编号
	 *
	 * @param
	 * @return
	 */
	String getMaxTmpEmpCode();
	/**
	 * 新增
	 *
	 * @param
	 * @return
	 */
	boolean saveTmpEmployeeInfo(TmpEmployeeInfo tmpEmployeeInfo, BladeUser bladeUser);
	/**
	 * 修改
	 *
	 * @param
	 * @return
	 */
	boolean updateTmpEmployeeInfo(TmpEmployeeInfo tmpEmployeeInfo, BladeUser bladeUser);

	/**
	 * 批量删除
	 *
	 * @param tmpEmpIds
	 * @return
	 */
	boolean deleteTmpEmployeeInfo(List<Long> tmpEmpIds, BladeUser bladeUser);
}
