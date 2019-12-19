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
import com.yundagalaxy.common.utils.DateUtils;
import com.yundagalaxy.management.commnon.utils.DictUtil;
import com.yundagalaxy.management.entity.DepartmentJob;
import com.yundagalaxy.management.service.IDepartmentInfoService;
import com.yundagalaxy.management.service.IDepartmentJobService;
import com.yundagalaxy.management.service.IEmployeeBasicInfoService;
import com.yundagalaxy.management.service.IEmployeeKanbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 员工基本信息表 服务实现类
 *
 * @author BladeX
 * @since 2019-10-18
 */
@Service("IEmployeeKanbanService")
public class EmployeeKanbanServiceImpl implements IEmployeeKanbanService {

	@Autowired
	private IEmployeeBasicInfoService employeeBasicInfoService;
	@Autowired
	private IDepartmentJobService departmentJobService;
	@Autowired
	private IDepartmentInfoService departmentInfoService;

	@Autowired
	private DictUtil dictUtil;


	@Override
	public Map<String,Long> getProfile(Map<String,Object> map){
        //员工概况
		Map<String,Long> totalCnt =  getTotalCnt(map);
		Long totalInCnt = totalCnt.get("totalInCnt");
		Long totalLeaveCnt = totalCnt.get("totalLeaveCnt");

		Date nowTime = new Date();
		Map<String,Long> monthCnt =  getMonthCnt(map,nowTime,-1,true);
		Long monthInCnt = monthCnt.get("monthInCnt");
		Long monthLeaveCnt = monthCnt.get("monthLeaveCnt");

		Map<String,Long> threeMonthCnt =  getMonthCnt(map,nowTime,-3,false);
		Long threeMonthInCnt = threeMonthCnt.get("monthInCnt");
		Long threeMonthLeaveCnt = threeMonthCnt.get("monthLeaveCnt");

		Map<String,Long> sixMonthCnt =  getMonthCnt(map,nowTime,-6,false);
		Long sixMonthInCnt = sixMonthCnt.get("monthInCnt");
		Long sixMonthLeaveCnt = sixMonthCnt.get("monthLeaveCnt");

        Map<String,Long> mp = new HashMap(8);
        mp.put("monthInCnt",monthInCnt);
        mp.put("monthLeaveCnt",monthLeaveCnt);
        mp.put("sixMonthInCnt",sixMonthInCnt);
        mp.put("sixMonthLeaveCnt",sixMonthLeaveCnt);
        mp.put("threeMonthInCnt",threeMonthInCnt);
        mp.put("threeMonthLeaveCnt",threeMonthLeaveCnt);
        mp.put("totalInCnt",totalInCnt);
        mp.put("totalLeaveCnt",totalLeaveCnt);
		return mp;
	}




	public Map<String,Long> getTotalCnt(Map<String,Object> tc){
		Long allCount = employeeBasicInfoService.getEmployeeCount(tc);
		//在职状态（2-离职，0-试用，1-正式 ，3-在职）
		tc.put("startWorkingState",2L);
		Long totalLeaveCnt = employeeBasicInfoService.getEmployeeCount(tc);
		tc.remove("startWorkingState");
		Long totalInCnt = allCount-totalLeaveCnt;
		Map totalCnt = new HashMap(2);
		totalCnt.put("totalInCnt",totalInCnt);
		totalCnt.put("totalLeaveCnt",totalLeaveCnt);
		return totalCnt;
	}

	/**
	 * 根据时间统计最近入职/离职员工
	 * @param mc
	 * @param nowTime
	 * @param mouth
	 * @param thisMonth
	 * @return
	 */
	public Map<String,Long> getMonthCnt(Map<String,Object> mc,Date nowTime,int mouth,boolean thisMonth){
		Date startTime = null;
		//本月
		if(thisMonth){
			startTime = DateUtils.getMonthFirstDay();
			nowTime = DateUtils.getMonthLastDay();
		}else{
			startTime =  DateUtils.addMonths(nowTime,mouth);
		}


		mc.put("inStartTime",startTime);
		mc.put("inEndTime",nowTime);
		mc.put("endWorkingState",1L);
		Long monthInCount = employeeBasicInfoService.getEmployeeCount(mc);
		mc.remove("inStartTime");
		mc.remove("inEndTime");
		mc.remove("endWorkingState");
		//在职状态（2-离职，0-试用，1-正式）
		mc.put("startWorkingState",2L);
		mc.put("leaveStartTime",startTime);
		mc.put("leaveEndTime",nowTime);
		Long monthLeaveCnt = employeeBasicInfoService.getEmployeeCount(mc);
		mc.remove("startWorkingState");
		mc.remove("leaveStartTime");
		mc.remove("leaveEndTime");
        Long monthInCnt = monthInCount;
//        Long monthInCnt = monthCount-monthLeaveCnt;
		Map monthTotalCnt = new HashMap(2);
		monthTotalCnt.put("monthInCnt",monthInCnt);
		monthTotalCnt.put("monthLeaveCnt",monthLeaveCnt);
		return monthTotalCnt;
	}
	@Override
	public Map<String,Object> getAnalysis(Map<String,Object> map){


		//在职性别统计
		Map<String,Object> employ = employeeSexSurveyResult(map);

		//在职年龄分析
		List<JSONObject> ageSurveyList = ageSurveyList(map);

		//在职工龄分析
		List<JSONObject> workAgeSurvey = workAgeSurveyList(map);

		//在职岗位统计
		List<JSONObject> positionTypeSurveyList = positionTypeSurveyList(map);


		Map<String,Object> result = new HashMap<>();
		result.put("employeeSexSurveyResult",employ);
		result.put("ageSurveyList",ageSurveyList);
		result.put("workAgeSurvey",workAgeSurvey);
		result.put("positionTypeSurveyList",positionTypeSurveyList);
		return result;
	}
	/**
	 *
	 *
	 * @param er
	 * @return
	 */
	public Map<String,Object> employeeSexSurveyResult(Map<String,Object> er){
        //女性
        er.put("sex",2);
		Long femaleCnt = employeeBasicInfoService.getEmployeeCount(er);
		//男性
        er.put("sex",1);
		Long maleCnt = employeeBasicInfoService.getEmployeeCount(er);
		//未知
        er.put("sex",0);
		Long otherCnt = employeeBasicInfoService.getEmployeeCount(er);
        Long allCnt = femaleCnt+maleCnt+otherCnt;
		double femalePercent = 0.00;
		double malePercent = 0.00;
		double otherPercent = 0.00;
        if(0!=allCnt){
			femalePercent = new BigDecimal((float)femaleCnt/allCnt).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
			malePercent = new BigDecimal((float)maleCnt/allCnt).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
			otherPercent = new BigDecimal((float)otherCnt/allCnt).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		Map employSex = new HashMap(6);
		employSex.put("femaleCnt",femaleCnt);
		employSex.put("femalePercent",femalePercent);
		employSex.put("maleCnt",maleCnt);
		employSex.put("malePercent",malePercent);
		employSex.put("otherCnt",otherCnt);
		employSex.put("otherPercent",otherPercent);
        er.remove("sex");
		return employSex;
	}

	public List<JSONObject> ageSurveyList(Map<String,Object> as){
		//年纪区间
		int[][] arrAge = {{0,17},{18,24},{25,29},{30,39},{40,59},{60,120}};
		List<JSONObject> list = new ArrayList<>();
		for(int i=0;i<arrAge.length;i++){
			int startAge = arrAge[i][0];
			int endAge = arrAge[i][1];
			JSONObject ageSurvey = ageSurvey(as,startAge,endAge);
			list.add(ageSurvey);
		}
		return list;
	}
	public List<JSONObject> workAgeSurveyList(Map<String,Object> wa){
		//年纪区间
		int[][] workAge = {{0,1},{1,3},{3,6},{6,12},{12,480}};
		List<JSONObject> list = new ArrayList<>();
		for(int i=0;i<workAge.length;i++){
			int startAge = workAge[i][0];
			JSONObject workAgeSurvey = workAgeSurvey(wa,startAge);
			list.add(workAgeSurvey);
		}
		return list;
	}
	/**
	 *在职岗位统计
	 *
	 * @param
	 * @return
	 */
	@Override
	public List<JSONObject> positionTypeSurveyList(Map<String,Object> pt){
	    Object cpCodeStr = pt.get("cpCode");
	    Long cpCode = Long.parseLong(cpCodeStr.toString());
        List<JSONObject> list = new ArrayList<>();
        List<DepartmentJob> ls = departmentJobService.list((new QueryWrapper<DepartmentJob>().eq("cp_code",cpCode).eq("del_flag",0)));
		Long allCnt = employeeBasicInfoService.getEmployeeCount(pt);
		if(0!=allCnt) {
			for (DepartmentJob rp : ls) {
				pt.put("cpCode", rp.getCpCode());
				pt.put("dpmentCode", rp.getDpmentCode());
				String jobTypeValue = dictUtil.getDictValue(DictUtil.DictCode.JOB_TYPE,rp.getJobType());;
				pt.put("dpmentName",jobTypeValue);
				pt.put("jobType",rp.getJobType());
				pt.put("jobTypeValue",jobTypeValue);
				pt.put("jobCode",rp.getJobCode());
				JSONObject jsonObject = positionTypeSurvey(pt);
				list.add(jsonObject);
			}
		}
		return getPositionTypeJobType(list);
	}

    /**
     *
     * @return
     */
	public List<JSONObject> getPositionTypeJobType(List<JSONObject> ls){
		List<JSONObject> resData =  new ArrayList<>();
		for(int i = 0; i < ls.size(); i++){
			String tmp = ls.get(i).getString("jobType");
			Integer num  = ls.get(i).getInteger("count");
			if(null!=num&&num!=0){
				JSONObject rd = new JSONObject();
				for(int j = i+1; j < ls.size();j++){
					if(tmp.equals(ls.get(j).getString("jobType"))){
						num += ls.get(j).getInteger("count");
						//遍历完成后，要删除User的name相同的数据
						ls.remove(ls.get(j));
						//remove一个元素时，要把遍历的指针减一
						j--;
					}
				}
				rd.put("count",num);
				rd.put("typeName",ls.get(i).getString("jobTypeValue"));
				resData.add(rd);
			}
		}
	    return resData;
    }
	/**
	 *
	 *
	 * @param
	 * @return
	 */
	public JSONObject ageSurvey(Map<String,Object> as,int startAge,int endAge){
        as.put("startAge",startAge);
        as.put("endAge",endAge);
		Long count = employeeBasicInfoService.getEmployeeCount(as);
		JSONObject jb = new JSONObject();
		String scope ="";
		if(startAge>=0 && endAge<18){
			scope = "18岁以下";
		}else if(endAge>=60){
			scope = startAge+"岁以上";
		}else {
			scope = startAge+"-"+endAge+"岁";
		}
		as.remove("startAge");
		as.remove("endAge");
		jb.put("scope",scope);
		jb.put("count",count);
		return jb;
	}
	public JSONObject workAgeSurvey(Map<String,Object> wa,int startAge){
        Date nowTime = new Date();
		Date inStartTime = nowTime;
		Date inNowTime = nowTime;
		JSONObject jb = new JSONObject();
		String scope ="";
        if(startAge>=0 && startAge<1){
			scope = "[0,1月)";
			inStartTime= DateUtils.addMonths(nowTime,-1);
			wa.put("inStartTime",inStartTime);
			wa.put("inEndTime",inNowTime);
		}else if(startAge>=1 && startAge<3){
			scope = "[1,3月)";
			inNowTime= DateUtils.addMonths(nowTime,-1);
			inStartTime= DateUtils.addMonths(nowTime,-3);
			wa.put("inStartTime",inStartTime);
			wa.put("inEndTime",inNowTime);
		}else if(startAge>=3 && startAge<6){
			scope = "[3,6月)";
			inNowTime= DateUtils.addMonths(nowTime,-3);
			inStartTime= DateUtils.addMonths(nowTime,-6);
			wa.put("inStartTime",inStartTime);
			wa.put("inEndTime",inNowTime);
		}else if(startAge>=6 && startAge<12){
			scope = "[6,12月)";
			inNowTime= DateUtils.addMonths(nowTime,-6);
			inStartTime= DateUtils.addMonths(nowTime,-11);
			wa.put("inStartTime",inStartTime);
			wa.put("inEndTime",inNowTime);
		}else {
			scope = "[12,∞)";
			inStartTime= DateUtils.addMonths(nowTime,-12);

			wa.put("inEndTime",inStartTime);

		}
		Long count = employeeBasicInfoService.getEmployeeCount(wa);
        wa.remove("inStartTime");
        wa.remove("inEndTime");
		jb.put("scope",scope);
		jb.put("count",count);
		return jb;
	}

    @Override
    public  JSONObject positionTypeSurvey(Map<String,Object> pt){
        JSONObject jd = new JSONObject();
        Long count = employeeBasicInfoService.getEmployeeCount(pt);
        String typeName = pt.get("dpmentName").toString();
        String typeCode = pt.get("dpmentCode").toString();
        String jobType = pt.get("jobType").toString();
        String jobTypeValue = pt.get("jobTypeValue").toString();
        pt.remove("cpCode");
        pt.remove("dpmentCode");
        pt.remove("dpmentName");
        pt.remove("jobCode");
        jd.put("typeName",typeName);
        jd.put("typeCode",typeCode);
        jd.put("jobType",jobType);
        jd.put("jobTypeValue",jobTypeValue);
        jd.put("count",count);
        return jd;
    }
    @Override
    public  JSONObject strPositionTypeSurvey(Map<String,Object> pt){
        JSONObject jb = new JSONObject();
        Long count = employeeBasicInfoService.getEmployeeCount(pt);
        String typeName = pt.get("dpmentName").toString();
        String typeCode = pt.get("dpmentCode").toString();
        pt.remove("cpCode");
        pt.remove("dpmentCode");
        pt.remove("dpmentName");
        jb.put("typeName",typeName);
        jb.put("typeCode",typeCode);
        jb.put("count",count);
        return jb;
    }

}
