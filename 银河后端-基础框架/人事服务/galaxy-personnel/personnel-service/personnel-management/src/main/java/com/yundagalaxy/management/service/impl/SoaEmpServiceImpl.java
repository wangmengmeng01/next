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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import com.yundagalaxy.management.entity.SoaEmp;
import com.yundagalaxy.management.entity.TmpEmployeeInfo;
import com.yundagalaxy.management.mapper.SoaEmpMapper;
import com.yundagalaxy.management.service.IEmployeeBasicInfoService;
import com.yundagalaxy.management.service.ISoaEmpService;
import com.yundagalaxy.management.service.ITmpEmployeeInfoService;
import com.yundagalaxy.management.vo.SoaEmpVO;
import com.yundagalaxy.management.vo.TmpEmployeeInfoVO;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * soa账号表 服务实现类
 *
 * @author dongfeng
 * @since 2019-11-08
 */
@Service
public class SoaEmpServiceImpl extends ServiceImpl<SoaEmpMapper, SoaEmp> implements ISoaEmpService {





	@Autowired
	private IEmployeeBasicInfoService employeeBasicInfoService;

	@Autowired
	private ITmpEmployeeInfoService tmpEmployeeInfoService;

	@Override
	public IPage<SoaEmpVO> selectSoaEmpPage(IPage<SoaEmpVO> page, SoaEmpVO soaEmp) {
		List<SoaEmpVO> ls =  baseMapper.selectSoaEmpPage(page, soaEmp);
		if(null!=soaEmp&&soaEmp.getJoinStatus()==1){
			if(null!=ls&&ls.size()>0){
				for(SoaEmpVO vo:ls){
					EmployeeBasicInfo employeeBasicInfo = employeeBasicInfoService.getOne(new QueryWrapper<EmployeeBasicInfo>().eq("soa_code",vo.getSoaCode()).eq("cp_code",vo.getCpCode()));
					if(null!=employeeBasicInfo){
						vo.setEmpCode(employeeBasicInfo.getEmpCode());
					}
				}
			}
		}
		return page.setRecords(ls);
	}
	
	@Override
	public void deleteSoaInfo(String soaCode) {
		baseMapper.deleteSoaInfo(soaCode);
	}

	@Override
	public void updateJoinStatus(String soaCode, int status) {
		baseMapper.updateJoinStatus(soaCode,status);
	}

	@Override
    @Transactional
	public Boolean updateSoaToTmpEmp(TmpEmployeeInfoVO tmpEmployeeInfoVO, BladeUser bladeUser){
		TmpEmployeeInfo tmpEmployeeInfo = BeanUtil.copy(tmpEmployeeInfoVO,TmpEmployeeInfo.class);

        if(StringUtils.isEmpty(tmpEmployeeInfoVO.getSoaCode())){
        	throw new ApiException("soaCode不能为空");
		}
		SoaEmp findOne = baseMapper.selectOne(new QueryWrapper<SoaEmp>().eq("soa_code",tmpEmployeeInfoVO.getSoaCode()));
		if(null!=findOne&&0!=findOne.getJoinStatus()){
			throw new ApiException("soa账号已处理，请刷新页面再试");
		}
		SoaEmp soaEmp = new SoaEmp();
		//员工维护状态：0-未处理 1-已处理员工入职 2-已处理非员工维护
        soaEmp.setJoinStatus(2);
        soaEmp.setLastUpdate(LocalDateTime.now());
		boolean temRes = tmpEmployeeInfoService.saveTmpEmployeeInfo(tmpEmployeeInfo,bladeUser);
		if(temRes){
			try{
        		baseMapper.update(soaEmp,new QueryWrapper<SoaEmp>().eq("soa_code",tmpEmployeeInfoVO.getSoaCode()));
			}catch (Exception e){
				e.printStackTrace();
				return false;
			}
		}else {
			return false;
		}
		return true;
	}

}
