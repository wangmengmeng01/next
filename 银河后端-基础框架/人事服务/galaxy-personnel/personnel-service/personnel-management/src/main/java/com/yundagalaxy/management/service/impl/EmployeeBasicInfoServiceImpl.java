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
import com.yundagalaxy.management.commnon.utils.DictUtil;
import com.yundagalaxy.management.entity.DepartmentInfo;
import com.yundagalaxy.management.entity.DepartmentJob;
import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import com.yundagalaxy.management.mapper.DepartmentInfoMapper;
import com.yundagalaxy.management.mapper.DepartmentJobMapper;
import com.yundagalaxy.management.vo.EmployeeBasicInfoVO;
import com.yundagalaxy.management.mapper.EmployeeBasicInfoMapper;
import com.yundagalaxy.management.service.IEmployeeBasicInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 员工基本信息表 服务实现类
 *
 * @author BladeX
 * @since 2019-10-18
 */
@Service
public class EmployeeBasicInfoServiceImpl extends ServiceImpl<EmployeeBasicInfoMapper, EmployeeBasicInfo> implements IEmployeeBasicInfoService {

	@Autowired
	private DepartmentInfoMapper departmentInfoMapper;
    @Autowired
	private DepartmentJobMapper departmentJobMapper;
    @Autowired
	private DictUtil dictUtil;
	/**
	 *
	 *
	 * @param
	 * @return
	 */
	@Override
	public IPage<EmployeeBasicInfoVO> selectEmployeeBasicInfoPage(IPage<EmployeeBasicInfoVO> page, EmployeeBasicInfoVO employeeBasicInfo) {

		return page.setRecords(baseMapper.selectEmployeeBasicInfoPage(page, employeeBasicInfo));
	}
	/**
	 *
	 *
	 * @param
	 * @return
	 */
	@Override
	public IPage<EmployeeBasicInfoVO> selectEmpBasicInfoPage(IPage<EmployeeBasicInfoVO> page, EmployeeBasicInfoVO employeeBasicInfo) {
		List<EmployeeBasicInfoVO> ls = baseMapper.selectEmpBasicInfoPage(page, employeeBasicInfo);
        if(null!=ls&&ls.size()>0){
			for(EmployeeBasicInfoVO vo:ls){
				DepartmentInfo dp = new DepartmentInfo();
				try{
                    dp = departmentInfoMapper.selectOne(new QueryWrapper<DepartmentInfo>().eq("dpment_code",vo.getDpmentCode()));
                }catch (Exception e){
				    e.printStackTrace();
                }
				if(null!=dp){
					vo.setDpmentName(dp.getDpmentName());
				}
				DepartmentJob job = new DepartmentJob();
				try{
                    job = departmentJobMapper.selectOne(new QueryWrapper<DepartmentJob>().eq("job_code",vo.getJobCode()));
                }catch (Exception e){
				    e.printStackTrace();
                }
				if(null!=job){
					String jobTypeValue = dictUtil.getDictValue(DictUtil.DictCode.JOB_TYPE,job.getJobType());
					vo.setJobName(job.getJobName());
					vo.setJobTypeDictValue(jobTypeValue);
				}
			}
        }
		return page.setRecords(ls);
	}
	/**
	 *
	 *
	 * @param
	 * @return
	 */
	@Override
	public boolean updateDateTimeByEmpCode(String empCode,String updateBy) {
		LocalDateTime localDateTime = LocalDateTime.now();
		baseMapper.updateDateTimeByEmpCode(empCode,localDateTime,updateBy);
		return true;
	}
	/**
	 *
	 *
	 * @param
	 * @return
	 */
	@Override
	public String getMaxEmpCode(){
		String empCode = "";
		try {
			empCode = baseMapper.findMaxEmpCode();
		}catch (Exception e){
			e.printStackTrace();
		}
		return empCode;
	}
	/**
	 * 统计员工数量
	 *
	 * @param
	 * @return
	 */
	@Override
	public Long getEmployeeCount(Map<String,Object> map){
		Long empCount = 0L;
		try{
			empCount = baseMapper.selectEmployeeCount(map);
		}catch (Exception e){
			log.error(e.getMessage());
		}
		return empCount;
	}
}
