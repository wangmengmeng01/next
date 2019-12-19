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
package com.yundagalaxy.management.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yundagalaxy.management.entity.TmpEmployeeInfo;
import com.yundagalaxy.management.vo.TmpEmployeeInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 非员工基本信息表 Mapper 接口
 *
 * @author dongfeng
 * @since 2019-10-21
 */
public interface TmpEmployeeInfoMapper extends BaseMapper<TmpEmployeeInfo> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param
	 * @return
	 */
	List<TmpEmployeeInfoVO> selectTmpEmployeeInfoPage(@Param("page")IPage page,@Param("rq") TmpEmployeeInfoVO rq);

	/**
	 *
	 *
	 * @param
	 * @param
	 * @return
	 */
	List<TmpEmployeeInfoVO> selectTmpEmployeeInfoList(@Param("rq") TmpEmployeeInfoVO rq);
	/**
	 * 查询最大编号
	 *
	 * @param
	 * @return
	 */
	String findMaxTmpEmpCode();
}
