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
import com.yundagalaxy.management.entity.SoaEmp;
import com.yundagalaxy.management.vo.SoaEmpVO;
import com.yundagalaxy.management.vo.TmpEmployeeInfoVO;
import org.springblade.core.secure.BladeUser;

/**
 * soa账号表 服务类
 *
 * @author dongfeng
 * @since 2019-11-08
 */
public interface ISoaEmpService extends IService<SoaEmp> {

	/**
	 * 自定义分页
	 *
	 * @param page
	 * @param soaEmp
	 * @return
	 */
	IPage<SoaEmpVO> selectSoaEmpPage(IPage<SoaEmpVO> page, SoaEmpVO soaEmp);
	/**
	 * 非员工入职登记
	 *
	 * @param
	 * @param tmpEmployeeInfoVO
	 * @return
	 */
	Boolean updateSoaToTmpEmp(TmpEmployeeInfoVO tmpEmployeeInfoVO, BladeUser bladeUser);

	void deleteSoaInfo(String soaCode);

    void updateJoinStatus(String soaCode, int status);
}
