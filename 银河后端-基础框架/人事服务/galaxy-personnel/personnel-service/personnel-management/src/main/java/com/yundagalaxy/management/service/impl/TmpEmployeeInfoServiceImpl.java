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
import com.yundagalaxy.management.commnon.utils.DictUtil;
import com.yundagalaxy.management.entity.TmpEmployeeInfo;
import com.yundagalaxy.management.mapper.TmpEmployeeInfoMapper;
import com.yundagalaxy.management.service.IDepartmentInfoService;
import com.yundagalaxy.management.service.ISoaEmpService;
import com.yundagalaxy.management.service.ITmpEmployeeInfoService;
import com.yundagalaxy.management.vo.TmpEmployeeInfoVO;
import com.yundagalaxy.management.vo.YunDaSoaEmpVO;
import org.apache.commons.lang.StringUtils;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.yundagalaxy.common.constant.CodeConstant.*;

/**
 * 非员工基本信息表 服务实现类
 *
 * @author dongfeng
 * @since 2019-10-21
 */
@Service
public class TmpEmployeeInfoServiceImpl extends ServiceImpl<TmpEmployeeInfoMapper, TmpEmployeeInfo> implements ITmpEmployeeInfoService {

	@Autowired
	private DistributedLock distributedLock;

	@Autowired
	private IDepartmentInfoService departmentInfoService;
	@Autowired
	private ISoaEmpService soaEmpService;
    @Autowired
	private DictUtil dictUtil;




	@Override
	public IPage<TmpEmployeeInfoVO> selectTmpEmployeeInfoPage(IPage<TmpEmployeeInfoVO> page, TmpEmployeeInfoVO tmpEmployeeInfoVO) {

        List<TmpEmployeeInfoVO> list =  baseMapper.selectTmpEmployeeInfoPage(page, tmpEmployeeInfoVO);
		String cpName = departmentInfoService.getYdserverCpName(tmpEmployeeInfoVO.getCpCode());
        if(null!=list&&list.size()>0){
            for (TmpEmployeeInfoVO vo:list) {
                String accountNoValue = dictUtil.getDictValue(DictUtil.DictCode.ACCOUNT_NO,vo.getAccountNo());
                vo.setAccountNoValue(accountNoValue);
                String accountTypeValue = dictUtil.getDictValue(DictUtil.DictCode.ACCOUNT_TYPE,vo.getAccountType());
                vo.setAccountTypeValue(accountTypeValue);
                String idTypeValue = dictUtil.getDictValue(DictUtil.DictCode.ID_TYPE,vo.getIdType());
                vo.setIdTypeValue(idTypeValue);
                String workingStateValue = dictUtil.getDictValue(DictUtil.DictCode.WORKING_STATE,vo.getWorkingState());
                vo.setWorkingStateValue(workingStateValue);
                String handoverValue = dictUtil.getDictValue(DictUtil.DictCode.HANDOVER,vo.getHandover());
                vo.setHandoverValue(handoverValue);
                vo.setCpName(cpName);
            }
        }
	    return page.setRecords(list);
	}

	/**
	 *
	 * @param tmpEmployeeInfoVO
	 * @return
	 */
	@Override
	public List<TmpEmployeeInfoVO> selectTmpEmployeeInfoList(TmpEmployeeInfoVO tmpEmployeeInfoVO) {

        List<TmpEmployeeInfoVO> list =  baseMapper.selectTmpEmployeeInfoList(tmpEmployeeInfoVO);
		String cpName = departmentInfoService.getYdserverCpName(tmpEmployeeInfoVO.getCpCode());
        if(null!=list&&list.size()>0){
            for (TmpEmployeeInfoVO vo:list) {
                String accountNoValue = dictUtil.getDictValue(DictUtil.DictCode.ACCOUNT_NO,vo.getAccountNo());
                vo.setAccountNoValue(accountNoValue);
                String accountTypeValue = dictUtil.getDictValue(DictUtil.DictCode.ACCOUNT_TYPE,vo.getAccountType());
                vo.setAccountTypeValue(accountTypeValue);
                String idTypeValue = dictUtil.getDictValue(DictUtil.DictCode.ID_TYPE,vo.getIdType());
                vo.setIdTypeValue(idTypeValue);
                String workingStateValue = dictUtil.getDictValue(DictUtil.DictCode.WORKING_STATE,vo.getWorkingState());
                vo.setWorkingStateValue(workingStateValue);
                String handoverValue = dictUtil.getDictValue(DictUtil.DictCode.HANDOVER,vo.getHandover());
                vo.setHandoverValue(handoverValue);
                vo.setCpName(cpName);
            }
        }
	    return list;
	}

	@Override
	public boolean saveTmpEmployeeInfo(TmpEmployeeInfo tmpEmployeeInfo, BladeUser bladeUser){

		String tmpEmpCode =  distributedLock.getNewCodeMax(
				PRE_GROUP_TMP_EMP_CODE,
				TMP_EMP_CODE_LOCK_KEY,
				LOCK_TIME,
				REDIS_MAX_TMP_EMP_CODE_KEY,
				5,
				TimeUnit.SECONDS);
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		tmpEmployeeInfo.setTmpEmpCode(tmpEmpCode);
		tmpEmployeeInfo.setDelFlag(0);
		tmpEmployeeInfo.setCreateTime(LocalDateTime.now());
		tmpEmployeeInfo.setLastUpdate(LocalDateTime.now());
		tmpEmployeeInfo.setFinalOperator(bladeUser.getUserName());
		tmpEmployeeInfo.setCpCode(cpCode);
		tmpEmployeeInfo.setCreateBy(bladeUser.getNickName());
		try {
			baseMapper.insert(tmpEmployeeInfo);
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw new ApiException("新增非员工失败");
		}
		return true;
	}

	@Override
	public boolean updateTmpEmployeeInfo(TmpEmployeeInfo tmpEmployeeInfo, BladeUser bladeUser){
		if(null==tmpEmployeeInfo.getTmpEmpId()){
			throw new ApiException("非员工tmpEmpId参数不能为空");
		}
		tmpEmployeeInfo.setLastUpdate(LocalDateTime.now());
		tmpEmployeeInfo.setFinalOperator(bladeUser.getUserName());
		//在职状态：2-离职，0-试用，1-正式 ，3-在职
		if(null!=tmpEmployeeInfo.getLeaveingDate()){
			tmpEmployeeInfo.setWorkingState(2);
		}
		try {
			baseMapper.updateById(tmpEmployeeInfo);
		}catch (Exception e){
			log.error(e.getMessage());
			e.printStackTrace();
			throw new ApiException("修改非员工失败");
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
	public String getMaxTmpEmpCode(){
		String tmpEmpCode = "";
		try {
			tmpEmpCode = baseMapper.findMaxTmpEmpCode();
		}catch (Exception e){
			log.error(e.getMessage());
		}
		return tmpEmpCode;
	}

	@Override
	public boolean deleteTmpEmployeeInfo(List<Long> tmpEmpIds,BladeUser bladeUser){

		tmpEmpIds.forEach(tmpEmpId -> {
			TmpEmployeeInfo tmpEmployeeInfo = baseMapper.selectOne(new QueryWrapper<TmpEmployeeInfo>().eq("tmp_emp_id",tmpEmpId).eq("del_flag",0));
			if(null==tmpEmployeeInfo){
				throw new ApiException("未查询到要修改的数据");
			}
			tmpEmployeeInfo.setTmpEmpId(tmpEmpId);
			tmpEmployeeInfo.setLastUpdate(LocalDateTime.now());
			tmpEmployeeInfo.setFinalOperator(bladeUser.getUserName());
			if(null==tmpEmployeeInfo.getDelFlag()||tmpEmployeeInfo.getDelFlag()==0){
				tmpEmployeeInfo.setDelFlag(1);
			}
			try {
				baseMapper.updateById(tmpEmployeeInfo);
			}catch (Exception e){
				log.error(e.getMessage());
				throw new ApiException("岗位jobId：（"+tmpEmpId+"）删除失败");
			}
		});
		return true;
	}



}
