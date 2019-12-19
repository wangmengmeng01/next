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
import com.yundagalaxy.management.commnon.service.impl.DistributedLock;
import com.yundagalaxy.management.entity.DepartmentInfo;
import com.yundagalaxy.management.entity.DepartmentJob;
import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import com.yundagalaxy.management.mapper.DepartmentJobMapper;
import com.yundagalaxy.management.service.IDepartmentJobService;
import com.yundagalaxy.management.service.IEmployeeBasicInfoService;
import com.yundagalaxy.management.vo.DepartmentJobVO;
import org.springblade.core.secure.BladeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.yundagalaxy.common.constant.CodeConstant.*;


/**
 * 部门岗位表 服务实现类
 *
 * @author dongfeng
 * @since 2019-10-19
 */
@Service
public class DepartmentJobServiceImpl extends ServiceImpl<DepartmentJobMapper, DepartmentJob> implements IDepartmentJobService {
	@Autowired
	private DistributedLock distributedLock;
	@Autowired
	private IEmployeeBasicInfoService employeeBasicInfoService;
	@Value("${personnel.downLoadDpJobMoBan.url}")
	private String dpJobMoBanUrl;
	@Override
	public IPage<DepartmentJobVO> selectDepartmentJobPage(IPage<DepartmentJobVO> page, DepartmentJobVO departmentJob) {
		List<DepartmentJobVO> resList = new ArrayList<>();
		try {
			resList = baseMapper.selectDepartmentJobPage(page, departmentJob);
		}catch (Exception e){
			log.error(e.getMessage());
			throw new ApiException("岗位数据查询异常");
		}
		return page.setRecords(resList);
	}
	@Override
	public List<DepartmentJobVO> selectDepartmentJobList(DepartmentJob departmentJob) {
		List<DepartmentJobVO> resList = new ArrayList<>();
		try {
			resList = baseMapper.selectDepartmentJobList(departmentJob);
		}catch (Exception e){
			log.error(e.getMessage());
			throw new ApiException("岗位数据查询异常");
		}
		return resList;
	}

	@Override
	public boolean saveDepartmentJob(DepartmentJob departmentJob, BladeUser bladeUser){

		String jobCode =  distributedLock.getNewCodeMax(
				PRE_GROUP_JOB_CODE,
				JOB_CODE_LOCK_KEY,
				LOCK_TIME,
				REDIS_MAX_JOB_CODE_KEY,
				5,
				TimeUnit.SECONDS);
		departmentJob.setDelFlag(0);
		departmentJob.setCreateTime(LocalDateTime.now());
		departmentJob.setLastUpdate(LocalDateTime.now());
		departmentJob.setJobCode(jobCode);
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());

		departmentJob.setCpCode(cpCode);
		departmentJob.setFinalBy(bladeUser.getNickName());
		departmentJob.setCreateBy(bladeUser.getNickName());
		try {
			baseMapper.insert(departmentJob);
		}catch (Exception e){
			log.error(e.getMessage());
			throw new ApiException("新增岗位失败");
		}
		return true;
	}

	@Override
	public boolean deleteDepartmentJob(List<Long> jobIds,BladeUser bladeUser){

		jobIds.forEach(jobId -> {
			DepartmentJob departmentJob = baseMapper.selectOne(new QueryWrapper<DepartmentJob>().eq("job_id",jobId).eq("del_flag",0));
			if(null==departmentJob){
				throw new ApiException("未查询到要修改的数据");
			}
			//判断部门下面是否有员工有员工的不能删除
//			Integer eCount = employeeBasicInfoService.count(new QueryWrapper<EmployeeBasicInfo>().eq("job_code",departmentJob.getJobCode()).eq("del_flag",0));

			Map<String,Object> eParam = new HashMap<>(3);
			eParam.put("jobCode",departmentJob.getJobCode());
			//1-试用,2-正式,3-离职
			eParam.put("endWorkingState",1);
			Long eCount = employeeBasicInfoService.getEmployeeCount(eParam);

			if(null!=eCount && eCount > 0){
				throw new ApiException("该岗位下有已绑定的员工,请先进行解绑");
			}
			departmentJob.setJobId(jobId);
			departmentJob.setLastUpdate(LocalDateTime.now());
			departmentJob.setFinalBy(bladeUser.getNickName());
			if(null==departmentJob.getDelFlag()||departmentJob.getDelFlag()==0){
				departmentJob.setDelFlag(1);
			}
			try {
				baseMapper.updateById(departmentJob);
			}catch (Exception e){
				log.error(e.getMessage());
				throw new ApiException("岗位jobId：（"+jobId+"）删除失败");
			}
		});
		return true;
	}
	@Override
	public boolean updateDepartmentJob(DepartmentJob departmentJob, BladeUser bladeUser){
		if(null==departmentJob.getJobId()){
			throw new ApiException("岗位jobId不能为空");
		}
		departmentJob.setFinalBy(bladeUser.getNickName());
		departmentJob.setLastUpdate(LocalDateTime.now());
		try {
			baseMapper.updateById(departmentJob);
		}catch (Exception e){
			log.error(e.getMessage());
			throw new ApiException(e.getMessage());
		}
		return true;
	}

	/**
	 * 查询最大编号
	 *
	 * @param
	 * @return
	 */
	@Override
	public String getMaxJobCode(){
		String jobCode = "";
		try {
			jobCode = baseMapper.findMaxJobCode();
		}catch (Exception e){
			log.error(e.getMessage());
		}
		return jobCode;
	}

	@Override
	public String getDpJobUrl(){
		return dpJobMoBanUrl;
	}

}
