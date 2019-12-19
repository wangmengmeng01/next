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

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundagalaxy.management.commnon.service.impl.DistributedLock;
import com.yundagalaxy.management.commnon.utils.RedisManUtil;
import com.yundagalaxy.management.entity.DepartmentInfo;
import com.yundagalaxy.management.entity.DepartmentJob;
import com.yundagalaxy.management.mapper.DepartmentInfoMapper;
import com.yundagalaxy.management.node.ForestNodeMerger;
import com.yundagalaxy.management.node.INode;
import com.yundagalaxy.management.service.IDepartmentInfoService;
import com.yundagalaxy.management.service.IDepartmentJobService;
import com.yundagalaxy.management.service.IEmployeeBasicInfoService;
import com.yundagalaxy.management.service.IEmployeeKanbanService;
import com.yundagalaxy.management.vo.DepartmentInfoVO;
import com.yundagalaxy.management.vo.StructureVO;
import com.yundagalaxy.management.wrapper.DeptWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.log.logger.BladeLogger;
import org.springblade.core.secure.BladeUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.yundagalaxy.common.constant.CodeConstant.*;

/**
 * 部门岗位表 服务实现类
 *
 * @author dongfeng
 * @since 2019-10-16
 */
@Service
public class DepartmentInfoServiceImpl extends ServiceImpl<DepartmentInfoMapper, DepartmentInfo> implements IDepartmentInfoService {


	@Autowired
	private DistributedLock distributedLock;

	@Autowired
	private IDepartmentInfoService departmentInfoService;
	@Autowired
	private IDepartmentJobService departmentJobService;
	@Autowired
	private IEmployeeBasicInfoService employeeBasicInfoService;
	@Autowired
	private IEmployeeKanbanService employeeKanbanService;

    @Autowired
    private RedisManUtil redisManUtil;
    @Autowired
	private BladeLogger bladeLogger;




	@Value("${personnel.downLoadDpInfoMoBan.url}")
	private String dpInfoMoBanUrl;



	@Override
	public IPage<DepartmentInfoVO> selectDepartmentInfoPage(IPage<DepartmentInfoVO> page, DepartmentInfoVO departmentInfo) {
		List<DepartmentInfoVO> resList = new ArrayList<>();
		try{
			resList = baseMapper.selectDepartmentInfoPage(page, departmentInfo);
		}catch (Exception e){
			bladeLogger.error("selectDepartmentInfoPage()方法报错：",e.getMessage());
			throw new ApiException("部门数据查询异常");
		}
		return page.setRecords(resList);
	}

	@Override
	public List<DepartmentInfoVO> tree(Map map) {

		return ForestNodeMerger.merge(baseMapper.tree(map));
	}
	@Override
	public boolean saveDepartmentInfo(DepartmentInfo departmentInfo, BladeUser bladeUser){

		if(StringUtils.isEmpty(departmentInfo.getParentDpmentCode())){
			departmentInfo.setParentDpmentCode("0");
		}
		//公司编码
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		DepartmentInfo dpInfo = baseMapper.selectOne(new QueryWrapper<DepartmentInfo>().eq("cp_code",cpCode).eq("dpment_name",departmentInfo.getDpmentName()).eq("del_flag",0));
		//系统默认部门：1-默认，2-非默认
		if(null!=dpInfo&&departmentInfo.getDpmentName().equals(dpInfo.getDpmentName())&&dpInfo.getIsDefault()==1){
			throw new ApiException("不能使用默认部门名称");
		}
        if(null!=dpInfo){
			throw new ApiException("部门名称被占用");
		}
        if(departmentInfo.getDpmentLevel()==1){
			Integer dpCnt = baseMapper.selectCount(new QueryWrapper<DepartmentInfo>().eq("cp_code",cpCode).eq("dpment_level",1).eq("del_flag",0));
			if(dpCnt>=10){
				throw new ApiException("新增部门不能大于10个");
			}
		}

		//删除标志:
		departmentInfo.setDelFlag(0);
		//系统默认部门：1-默认，2-非默认
		departmentInfo.setIsDefault(2);
		//分布式主键生成
		String dpmentCode = distributedLock.getNewCodeMax(
				PRE_GROUP_DPMENT_CODE,
				DPMENT_CODE_LOCK_KEY,
				LOCK_TIME,REDIS_MAX_DPMENT_CODE_KEY,
				5,
				TimeUnit.SECONDS
		);

		//
		departmentInfo.setDpmentCode(dpmentCode);
		//更新时间
		departmentInfo.setLastUpdate(LocalDateTime.now());
		//创建时间
		departmentInfo.setCreateTime(LocalDateTime.now());

		departmentInfo.setCpCode(cpCode);
		//最后修改人
		departmentInfo.setFinalBy(bladeUser.getNickName());
		//创建人
		departmentInfo.setCreateBy(bladeUser.getNickName());
		try {
			baseMapper.saveDepartmentInfo(departmentInfo);
		}catch (Exception e){
			bladeLogger.error("saveDepartmentInfo()方法报错：",e.getMessage());
			throw new ApiException("新增部门失败");
		}
		return true;
	}
	@Override
	public boolean updateDepartmentInfo(DepartmentInfo departmentInfo, BladeUser bladeUser){
		if(null==departmentInfo.getDpmentId()){
			throw new ApiException("部门dpmentId不能为空");
		}
		DepartmentInfo di = baseMapper.selectOne(new QueryWrapper<DepartmentInfo>().eq("dpment_id",departmentInfo.getDpmentId()).eq("del_flag",0));
		if(null==di){
			throw new ApiException("未查询到修改信息");
		}
		//系统默认部门：1-默认，2-非默认
		if(null!=di&&!departmentInfo.getDpmentName().equals(di.getDpmentName())&&di.getIsDefault()==1){
			throw new ApiException("默认部门不能修改部门名称");
		}
		//判断部门下面是否有员工有员工的不能删除
//		Integer eCount = employeeBasicInfoService.count(new QueryWrapper<EmployeeBasicInfo>().eq("dpment_code",departmentInfo.getDpmentCode()).eq("del_flag",0));
        Map<String,Object> eParam = new HashMap<>(3);
        eParam.put("dpmentCode",di.getDpmentCode());
		//1-试用,2-正式,3-离职
        eParam.put("endWorkingState",1);
        Long eCount = employeeBasicInfoService.getEmployeeCount(eParam);
		if(null!=eCount && eCount > 0){
			throw new ApiException("该部门下已有员工，请解绑后更改部门层级");
		}

		if(null!=di&&di.getIsDefault()==1&&!StringUtils.isEmpty(departmentInfo.getParentDpmentCode())){
			throw new ApiException("默认部门不能修改上级部门");
		}
		DepartmentInfo parentSelf = baseMapper.selectOne(new QueryWrapper<DepartmentInfo>().eq("dpment_code",departmentInfo.getParentDpmentCode()).eq("del_flag",0));
		if(StringUtils.isEmpty(departmentInfo.getParentDpmentCode())){
			//一级
			departmentInfo.setParentDpmentCode("0");
		}
		if(di.getDpmentCode().equals(departmentInfo.getParentDpmentCode())||(null!=parentSelf&&parentSelf.getParentDpmentCode().equals(di.getDpmentCode()))){
			throw new ApiException("父部门不能是自己或自己的子孙部门");
		}

		//更新时间
		departmentInfo.setLastUpdate(LocalDateTime.now());
		//
		departmentInfo.setFinalBy(bladeUser.getNickName());
		try {
			baseMapper.updateDepartmentInfo(departmentInfo);
		}catch (Exception e){
			bladeLogger.error("updateDepartmentInfo()方法报错：",e.getMessage());
			throw new ApiException("修改部门失败");
		}
		return true;
	}
	@Override
	public boolean deleteDepartmentInfo(List<Long> dpmentIds, BladeUser bladeUser){

		dpmentIds.forEach(dpmentId -> {
			DepartmentInfo departmentInfo = baseMapper.selectOne(new QueryWrapper<DepartmentInfo>().eq("dpment_id",dpmentId).eq("del_flag",0));
			if(null==departmentInfo){
				throw new ApiException("您删除的数据不存在");
			}

			//系统默认部门：1-默认，2-非默认
			if(null!=departmentInfo&&departmentInfo.getIsDefault()==1){
				throw new ApiException("默认部门不能删除");
			}
			//判断部门下面是否有员工有员工的不能删除
//			Integer eCount = employeeBasicInfoService.count(new QueryWrapper<EmployeeBasicInfo>().eq("dpment_code",departmentInfo.getDpmentCode()).eq("del_flag",0));
            Map<String,Object> eParam = new HashMap<>(3);
            eParam.put("dpmentCode",departmentInfo.getDpmentCode());
            //1-试用,2-正式,3-离职
            eParam.put("endWorkingState",1);
            Long eCount = employeeBasicInfoService.getEmployeeCount(eParam);
			if(null!=eCount && eCount > 0){
				throw new ApiException("该部门下有已绑定的员工,请先进行解绑!");
			}
			Integer cnt = baseMapper.selectCount((new QueryWrapper<DepartmentInfo>().eq("parent_dpment_code",departmentInfo.getDpmentCode()).eq("del_flag",0)));
			if (null==cnt || cnt > 0) {
				throw new ApiException("请先删除子节点!");
			}
			//删除岗位
			List<DepartmentJob> jobs = departmentJobService.list(new QueryWrapper<DepartmentJob>().eq("dpment_code",departmentInfo.getDpmentCode()).eq("del_flag",0));
            if(null!=jobs&&jobs.size()>0){
            	for(DepartmentJob rp:jobs){
					DepartmentJob updateDpJob = new DepartmentJob();
					updateDpJob.setDelFlag(1);
					updateDpJob.setJobId(rp.getJobId());
					try {
						departmentJobService.updateDepartmentJob(updateDpJob,bladeUser);
					}catch (Exception e){
						e.printStackTrace();
						log.error(e.getMessage());
						throw new ApiException("岗位jobId：（"+rp.getJobId()+"）删除失败");
					}
				}
			}
			departmentInfo.setDpmentId(dpmentId);
			departmentInfo.setFinalBy(bladeUser.getNickName());
			departmentInfo.setLastUpdate(LocalDateTime.now());
			if(null==departmentInfo.getDelFlag()||departmentInfo.getDelFlag()==0){
				departmentInfo.setDelFlag(1);
			}
			try {
				baseMapper.updateDepartmentInfo(departmentInfo);
			}catch (Exception e){
				bladeLogger.error("deleteDepartmentInfo()方法报错：",e.getMessage());
				throw new ApiException("部门dpmentId：（"+dpmentId+"）删除失败");
			}
		});
		return true;
	}
	/**
	 * 查询最大编号
	 *
	 * @param
	 * @return
	 */
	@Override
	public String getMaxDpmentCode(){
		String dpmentCode = "";
		try {
			dpmentCode = baseMapper.findMaxDpmentCode();
		}catch (Exception e){
			log.error(e.getMessage());
		}
		return dpmentCode;
	}

	/**
	 *
	 * @param departmentInfo
	 * @return
	 */
	@Override
	public  List<DepartmentInfoVO> getList(DepartmentInfo departmentInfo){
		List<DepartmentInfoVO>  list = new ArrayList<>();
		try{
			list = baseMapper.selectDepartmentInfoList(departmentInfo);
		}catch (Exception e){
			bladeLogger.error("getList()方法报错：",e.getMessage());
			throw new ApiException("部门管理数据查询失败");
		}
		return list;
	}

	/**
	 *
	 * @param departmentInfo
	 * @return
	 */
	@Override
	public  List<DepartmentInfoVO> getLockList(DepartmentInfo departmentInfo){
		List<DepartmentInfoVO>  list = new ArrayList<>();
		Integer cpCode = departmentInfo.getCpCode();
		try{
			list = baseMapper.selectDepartmentInfoList(departmentInfo);
			if(StringUtils.isEmpty(departmentInfo.getDpmentCode())&&list.size()<7) {
                boolean result = defaultDepartment(cpCode);
                if (result) {
                    list = baseMapper.selectDepartmentInfoList(departmentInfo);
                } else {
                    throw new ApiException("初始化失败，请重新刷新页面初始化");
                }
		    }
		}catch (Exception e){
			bladeLogger.error("getLockList()方法异常：",e.getMessage());
		}
		return list;
	}


	/**
	 * 查询网点名称
	 *
	 * @param
	 * @return
	 */
	@Override
	public String getYdserverCpName(Integer cpCode){
		String cpName = "";
		try{
			cpName = baseMapper.findYdserverCpName(cpCode);
		}catch (Exception e){
			bladeLogger.error("getYdserverCpName()方法报错：",e.getMessage());
		}
		return cpName;
	}

	@Override
	public Map<String,Object> getStructure(Integer cpCode, BladeUser bladeUser){
		Map<String,Object> mm = new HashMap<>();
		String cpName = getYdserverCpName(cpCode);
		Map md = new HashMap();
		md.put("cpCode",cpCode);
		md.put("endWorkingState",1);
		Long inCnt = employeeBasicInfoService.getEmployeeCount(md);
		List<StructureVO> ls = getDepartList(cpCode);
		List<INode> nodes = DeptWrapper.build().listNodeTwoVO(ls);
		mm.put("cpName",cpName);
		mm.put("inCnt",inCnt);
		mm.put("children",nodes);
		return mm;
	}
	/**
	 *
	 * @param cpCode
	 * @return
	 */
	public List<StructureVO> getDepartList(Integer cpCode){
		DepartmentInfo departmentInfo = new DepartmentInfo();
		departmentInfo.setCpCode(cpCode);
		List<DepartmentInfoVO> ls = departmentInfoService.getLockList(departmentInfo);
		List<StructureVO> list =new ArrayList<>();
		for (DepartmentInfo rp:ls) {
			Map<String,Object> pt= new HashMap<>(3);
			pt.put("cpCode",rp.getCpCode());
			pt.put("dpmentCode",rp.getDpmentCode());
			pt.put("dpmentName",rp.getDpmentName());
			pt.put("endWorkingState",1);
			JSONObject jsonObject = employeeKanbanService.strPositionTypeSurvey(pt);
			Long cnt = jsonObject.getLong("count");
			StructureVO structureVO = new StructureVO();
			structureVO.setEmpNum(cnt);
			structureVO.setDpmentCode(rp.getDpmentCode());
			structureVO.setParentDpmentCode(rp.getParentDpmentCode());
			structureVO.setDpmentId(rp.getDpmentId());
			structureVO.setDpmentName(rp.getDpmentName());
			list.add(structureVO);
		}
		return list;
	}

	/**
	 * 默认7条数据
	 * @param cpCode
	 * @return
	 */
	public  boolean defaultDepartment(Integer cpCode) {

		String[] strArray={"人事行政后勤","业务部","操作部","车队","财务部","客服部","经理室"};
        long start = System.nanoTime();
        TimeUnit timeUnit = TimeUnit.SECONDS;
        do{
            String lockValue = String.valueOf(new Date().getTime()+5*1000);
            boolean lockFlag = redisManUtil.lock(IS_DEFAULT_LOCK_KEY,lockValue);
            if(lockFlag){
                redisManUtil.expire(IS_DEFAULT_LOCK_KEY, 5);
                for (String s:strArray){
                    DepartmentInfo dpInfo = baseMapper.selectOne(new QueryWrapper<DepartmentInfo>().eq("cp_code",cpCode).eq("dpment_name",s).eq("is_default",1));
                    if(null!=dpInfo){
                        continue;
                    }
                    DepartmentInfo enDpInfo = new DepartmentInfo();
                    //分布式主键生成
                    String dpCode = distributedLock.getNewCodeMax(
                            PRE_GROUP_DPMENT_CODE,
                            DPMENT_CODE_LOCK_KEY,
                            LOCK_TIME,REDIS_MAX_DPMENT_CODE_KEY,
                            5,
                            TimeUnit.SECONDS
                    );
                    enDpInfo.setCpCode(cpCode);
                    enDpInfo.setDelFlag(0);
                    enDpInfo.setDpmentCode(dpCode);
                    enDpInfo.setIsDefault(1);
                    enDpInfo.setDpmentName(s);
                    enDpInfo.setCreateTime(LocalDateTime.now());
                    enDpInfo.setCreateBy("admin");
                    enDpInfo.setLastUpdate(LocalDateTime.now());
                    enDpInfo.setFinalBy("");
                    //等级
                    enDpInfo.setDpmentLevel(1);
                    //1-直营，2-承包
                    enDpInfo.setBusinessModel(1);
                    try {
                        baseMapper.insert(enDpInfo);
                    }catch (Exception e){
                        bladeLogger.error("defaultDepartment()=>插入数据失败:","data{"+enDpInfo.toString()+"}error:"+e.getMessage());
                        return false;
                    }

                }
                redisManUtil.unlock(IS_DEFAULT_LOCK_KEY, lockValue);
                break;
            }else if(!lockFlag){
                bladeLogger.info("getLockList():",Thread.currentThread().getName()+"======未获取锁,未超时将进入循环");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    bladeLogger.error("InterruptedException",e.getMessage());
                }
            }
            //如果未超时，则循环获取锁
        }while (System.nanoTime()-start<timeUnit.toNanos(5));
		return true;
	}
	@Override
	public String getDpInfoUrl(){
		//部门地址
		return dpInfoMoBanUrl;
	}

}
