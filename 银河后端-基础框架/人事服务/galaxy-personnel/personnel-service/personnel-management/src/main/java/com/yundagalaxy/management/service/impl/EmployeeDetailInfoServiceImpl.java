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
package com.yundagalaxy.management.service.impl;

import com.yundagalaxy.management.entity.EmployeeDetailInfo;
import com.yundagalaxy.management.vo.EmployeeDetailInfoVO;
import com.yundagalaxy.management.mapper.EmployeeDetailInfoMapper;
import com.yundagalaxy.management.service.IEmployeeDetailInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 员工详细信息表 服务实现类
 *
 * @author BladeX
 * @since 2019-10-18
 */
@Service
public class EmployeeDetailInfoServiceImpl extends ServiceImpl<EmployeeDetailInfoMapper, EmployeeDetailInfo> implements IEmployeeDetailInfoService {

	@Override
	public IPage<EmployeeDetailInfoVO> selectEmployeeDetailInfoPage(IPage<EmployeeDetailInfoVO> page, EmployeeDetailInfoVO employeeDetailInfo) {
		return page.setRecords(baseMapper.selectEmployeeDetailInfoPage(page, employeeDetailInfo));
	}

}
