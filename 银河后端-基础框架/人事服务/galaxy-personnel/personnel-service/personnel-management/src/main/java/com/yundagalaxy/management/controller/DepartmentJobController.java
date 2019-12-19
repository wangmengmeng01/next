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
package com.yundagalaxy.management.controller;

import cn.afterturn.easypoi.view.MiniAbstractExcelView;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yundagalaxy.common.utils.EasyExcelUtil;
import com.yundagalaxy.management.commnon.utils.DictUtil;
import com.yundagalaxy.management.entity.DepartmentInfo;
import com.yundagalaxy.management.entity.DepartmentJob;
import com.yundagalaxy.management.model.DepartmentJobModel;
import com.yundagalaxy.management.service.IDepartmentInfoService;
import com.yundagalaxy.management.service.IDepartmentJobService;
import com.yundagalaxy.management.vo.DepartmentJobVO;
import com.yundagalaxy.management.vo.IdCommonVO;
import com.yundagalaxy.system.feign.IDictClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门岗位表 控制器
 *
 * @author dongfeng
 * @since 2019-10-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/departmentJob")
@Api(value = "部门岗位表", tags = "部门岗位表接口")
@Slf4j
public class DepartmentJobController extends BladeController {

	private IDepartmentJobService departmentJobService;

	private IDepartmentInfoService departmentInfoService;

	private MiniAbstractExcelView miniAbstractExcelView;

	private DictUtil dictUtil;

	private IDictClient dictClient;



	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入departmentJob")
	public R<DepartmentJob> detail(DepartmentJob departmentJob) {
		DepartmentJob detail = departmentJobService.getOne(Condition.getQueryWrapper(departmentJob));
		return R.data(detail);
	}

	@GetMapping("/getOneInfo")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "获取单独一条数据", notes = "传入departmentJob")
	public R getOneInfo(DepartmentJob departmentJob) {
		DepartmentJob detail = departmentJobService.getOne(Condition.getQueryWrapper(departmentJob));
		DepartmentJobVO departmentJobVO = BeanUtil.copy(detail, DepartmentJobVO.class);

		return R.data(departmentJobVO);
	}

	/**
	 * 分页 部门岗位表
	 */
//	@GetMapping("/list")
//	@ApiOperationSupport(order = 2)
//	@ApiOperation(value = "分页", notes = "传入departmentJob")
//	public R<IPage<DepartmentJob>> list(DepartmentJob departmentJob, Query query) {
//
//		IPage<DepartmentJob> pages = departmentJobService.page(Condition.getPage(query), Condition.getQueryWrapper(departmentJob));
//		return R.data(pages);
//	}

	/**
	 * 不分页 部门岗位表
	 */
	@GetMapping("/getList")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "不分页（list）", notes = "传入departmentJob")
	public R getList(DepartmentJob departmentJob, BladeUser bladeUser) {
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		departmentJob.setCpCode(cpCode);
		List<DepartmentJobVO> list = departmentJobService.selectDepartmentJobList(departmentJob);
		List<Map<String, Object>> ls = new ArrayList<>();
		if (null != list && list.size() > 0) {
			for (DepartmentJobVO job : list) {
				Map mm = new HashMap(7);
				mm.put("jobName", job.getJobName());
				mm.put("jobType", job.getJobType());
				String jobTypeValue = dictUtil.getDictValue(DictUtil.DictCode.JOB_TYPE, job.getJobType().intValue());
				String jobLevelValue = dictUtil.getDictValue(DictUtil.DictCode.JOB_LEVEL, job.getJobLevel().intValue());
				mm.put("jobTypeValue", jobTypeValue);
				mm.put("jobLevelValue", jobLevelValue);
				mm.put("jobLevel", job.getJobLevel());
				mm.put("jobId", job.getJobId().toString());
				mm.put("jobCode", job.getJobCode());
				mm.put("dpmentCode", job.getDpmentCode());
				DepartmentInfo departmentInfo = departmentInfoService.getOne(new QueryWrapper<DepartmentInfo>().eq("dpment_code", job.getDpmentCode()).eq("del_flag", 0));
				String dpName = "";
				if (null != departmentInfo) {
					dpName = departmentInfo.getDpmentName();
				}
				mm.put("dpmentName", dpName);
				ls.add(mm);
			}
		}
		return R.data(ls);
	}

	/**
	 * 自定义分页 部门岗位表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入departmentJob")
	public R<IPage<DepartmentJobVO>> page(DepartmentJobVO departmentJob, Query query, BladeUser bladeUser) {
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		departmentJob.setCpCode(cpCode);
		IPage<DepartmentJobVO> pages = departmentJobService.selectDepartmentJobPage(Condition.getPage(query), departmentJob);
		return R.data(pages);
	}

	/**
	 * 新增 部门岗位表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增岗位", notes = "传入departmentJob")
	public R save(@Valid @RequestBody DepartmentJob departmentJob, BladeUser bladeUser) {

		return R.status(departmentJobService.saveDepartmentJob(departmentJob, bladeUser));
	}

	/**
	 * 修改 部门岗位表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改岗位", notes = "传入departmentJob")
	public R update(@Valid @RequestBody DepartmentJob departmentJob, BladeUser bladeUser) {

		return R.status(departmentJobService.updateDepartmentJob(departmentJob, bladeUser));
	}

	/**
	 * 新增或修改 部门岗位表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入departmentJob")
	public R submit(@Valid @RequestBody DepartmentJob departmentJob) {
		return R.status(departmentJobService.saveOrUpdate(departmentJob));
	}


	/**
	 * 删除 部门岗位表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除岗位（批量）", notes = "传入jobIds")
	public R remove(@RequestBody IdCommonVO idCommonVO, BladeUser bladeUser) {

		if (StringUtils.isEmpty(idCommonVO.getJobIds())) {
			return R.fail("参数jobIds不能为空");
		}

		return R.status(departmentJobService.deleteDepartmentJob(Func.toLongList(idCommonVO.getJobIds()), bladeUser));
	}


	/**
	 * 查询岗位类型- 下拉框
	 *
	 * @param
	 * @return
	 */
	@GetMapping("/getJobType")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "查询岗位类型- 下拉框", notes = "下拉框")
	public R getJobType(BladeUser bladeUser) {
		List<Map<String, Object>> lsMap = new ArrayList<>();
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		List<DepartmentJob> ld = departmentJobService.list(Condition.getQueryWrapper(new DepartmentJob()).lambda().eq(DepartmentJob::getCpCode, cpCode));
		if (null != ld && ld.size() > 0) {
			for (DepartmentJob vo : ld) {
				Map<String, Object> map = new HashMap<>();
				map.put("jobType", vo.getJobType());
				String jobName = dictUtil.getDictValue(DictUtil.DictCode.JOB_TYPE, vo.getJobType().intValue());
				map.put("jobName", jobName);
				lsMap.add(map);
			}
		}
		return R.data(lsMap);
	}

	/**
	 * 查询岗位级别- 下拉框
	 *
	 * @param
	 * @return
	 */
	@GetMapping("/getJobLevel")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "查询岗位级别- 下拉框", notes = "下拉框")
	public R getJobLevel(BladeUser bladeUser) {
		List<Map<String, Object>> lMap = new ArrayList<>();
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		List<DepartmentJob> dl = departmentJobService.list(Condition.getQueryWrapper(new DepartmentJob()).lambda().eq(DepartmentJob::getCpCode, cpCode));
		if (null != dl && dl.size() > 0) {
			for (DepartmentJob vo : dl) {
				Map<String, Object> map = new HashMap<>();
				map.put("jobLevel", vo.getJobLevel());
				String jobLevelName = dictUtil.getDictValue(DictUtil.DictCode.JOB_LEVEL, vo.getJobLevel().intValue());
				map.put("jobLevelName", jobLevelName);
				lMap.add(map);
			}
		}
		return R.data(lMap);
	}

	/**
	 * 根据岗位类型查询所属部门
	 *
	 * @param
	 * @return
	 */
	@GetMapping("/getDepartmentName")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "根据岗位类型查询所属部门", notes = "根据岗位类型查询所属部门")
	public R getDepartmentName(@RequestParam Map<String, Object> map, BladeUser bladeUser) {
		Map mm = new HashMap(2);
		mm.put("cp_code", Integer.parseInt(bladeUser.getDeptId()));
		mm.put("job_type", Integer.parseInt(map.get("jobType").toString()));
		DepartmentJob job = departmentJobService.getOne(new QueryWrapper<DepartmentJob>().allEq(mm));
		DepartmentInfo departmentInfo = departmentInfoService.getOne(new QueryWrapper<DepartmentInfo>().eq("dpment_code", job.getDpmentCode()));
		Map dd = new HashMap(2);
		dd.put("dpmentName", departmentInfo.getDpmentName());
		dd.put("dpmentCode", departmentInfo.getDpmentCode());
		return R.data(dd);

	}

	/**
	 * 测试
	 *
	 * @param
	 * @return
	 */
	@GetMapping("/test")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "测试", notes = "测试")
	public R test(@RequestParam Map<String, Object> map) {
		String code = map.get("code").toString();
		String dictKey = map.get("dictKey").toString();
		R<String> ss = dictClient.getValue(code, Integer.parseInt(dictKey));
		return R.data(ss.getData());

	}


//	/**
//	 * 下载部门模板
//	 */
//	@GetMapping("/downLoadDpJobMoBan")
//	@ApiOperationSupport(order = 6)
//	@ApiOperation(value = "下载岗位模板", notes = "下载岗位模板")
//	public R downLoadDpJobMoBan(HttpServletRequest request, HttpServletResponse response) {
//
//		//获得营业明细(含收支)信息
//		List<Map<String, Object>> dpList = new ArrayList<>();
//		Map<String, Object> one = new HashMap<>();
//		one.put("jobName", "客服1");
//		one.put("jobTypeValue", "司机");
//		one.put("jobLevelValue", "一级员工");
//		one.put("dpmentCode", "JM00245652");
//		one.put("dpmentName", "客服部");
//		dpList.add(one);
//		Map<String, Object> two = new HashMap<>();
//		two.put("jobName", "客服2");
//		two.put("jobTypeValue", "人事行政后勤类");
//		two.put("jobLevelValue", "一级员工");
//		two.put("dpmentCode", "JM00245652");
//		two.put("dpmentName", "客服部");
//		dpList.add(two);
//
//		// 创建参数对象（用来设定excel得sheet得内容等信息）
//		ExportParams exportParams = new ExportParams();
//		// 设置sheet得名称
//		exportParams.setSheetName("导入模板");
////		params.setTitle("");
//
//		List<ExcelExportEntity> columnList = new ArrayList<ExcelExportEntity>();
//		ExcelExportEntity jobName = new ExcelExportEntity("岗位名称", "jobName");
//		jobName.setNeedMerge(true);
//		columnList.add(jobName);
//
//		ExcelExportEntity jobTypeValue = new ExcelExportEntity("岗位类型", "jobTypeValue");
//		jobTypeValue.setNeedMerge(true);
//		columnList.add(jobTypeValue);
//
//
//		ExcelExportEntity jobLevelValue = new ExcelExportEntity("岗位级别", "jobLevelValue");
//		jobLevelValue.setNeedMerge(true);
//		columnList.add(jobLevelValue);
//
//		ExcelExportEntity dpmentCode = new ExcelExportEntity("部门编号", "dpmentCode");
//		dpmentCode.setNeedMerge(true);
//		columnList.add(dpmentCode);
//
//		ExcelExportEntity dpmentName = new ExcelExportEntity("所属部门", "dpmentName");
//		dpmentName.setNeedMerge(true);
//		columnList.add(dpmentName);
//
//		// 执行方法
//		Workbook workbook = ExcelExportUtil.exportExcel(exportParams, columnList, dpList);
//
//		try {
//			miniAbstractExcelView.out(workbook, "gangweimoban", request, response);
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		return R.success("下载成功");
//	}

	/**
	 * 导入岗位excel返回数据
	 */
	@PostMapping("/exportDpJobExcel")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "导入岗位Excel返回数据", notes = "导入岗位Excel返回数据")
	public R exportDpJobExcel(@RequestParam("file") MultipartFile file) {
		Map<String, Object> result = null;
		try {
			result = EasyExcelUtil.readExcel(file, new DepartmentJobModel(), 1);

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		boolean flag = (boolean) result.get("flag");
		List<Object> dataList = new ArrayList<>();
		if (flag) {
			dataList = (List<Object>) result.get("data");
		} else {
			return R.data(400,dataList,"导入文件格式有误，请重新选择");
		}
		return R.data(dataList);
	}

	/**
	 * 导入岗位数据
	 */
	@PostMapping("/exportDpJobData")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "导入岗位数据", notes = "导入岗位数据")
	public R exportDpJobData(@Valid @RequestBody DepartmentJobModel dm, BladeUser bladeUser) {
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		DepartmentJob departmentJob = new DepartmentJob();
		Map<String,Object> data = new HashMap<>(3);
		if (null != dm && StringUtils.isEmpty(dm.getJobName())) {
			data.put("success",false);
			data.put("code",400);
			data.put("msg","岗位名称不能为空");
			return R.data(data);

		}
		departmentJob.setJobName(dm.getJobName());

		if (null != dm && StringUtils.isEmpty(dm.getJobTypeValue())) {
			data.put("success",false);
			data.put("code",400);
			data.put("msg","岗位类型不能为空");
			return R.data(data);

		}
		Integer jobType = dictUtil.getDictKey(DictUtil.DictCode.JOB_TYPE, dm.getJobTypeValue());
		if (null != jobType) {
			departmentJob.setJobType(jobType);
		} else {
			data.put("success",false);
			data.put("code",400);
			data.put("msg","岗位类型不对");
			return R.data(data);

		}
		if (null != dm && StringUtils.isEmpty(dm.getJobLevelValue())) {
			data.put("success",false);
			data.put("code",400);
			data.put("msg","岗位等级不能为空");
			return R.data(data);
		}
		Integer jobLevel = dictUtil.getDictKey(DictUtil.DictCode.JOB_LEVEL, dm.getJobLevelValue());
		if (null != jobType) {
			departmentJob.setJobLevel(jobLevel);
		} else {
			data.put("success",false);
			data.put("code",400);
			data.put("msg","岗位等级不对");
			return R.data(data);

		}
		if (null != dm && StringUtils.isEmpty(dm.getDpmentCode())) {
			data.put("success",false);
			data.put("code",400);
			data.put("msg","所属部门不能为空");
			return R.data(data);
		}

        Integer dpInfoCnt = departmentInfoService.count(new QueryWrapper<DepartmentInfo>().eq("cp_code", cpCode).eq("dpment_code", dm.getDpmentCode()).eq("del_flag",0));
        if (null == dpInfoCnt || dpInfoCnt==0) {
            data.put("success",false);
            data.put("code",400);
            data.put("msg","部门编号不存在");
            return R.data(data);

        }

		departmentJob.setDpmentCode(dm.getDpmentCode());
		Integer count = departmentJobService.count(new QueryWrapper<DepartmentJob>().eq("cp_code", cpCode).eq("job_name", dm.getJobName()).eq("del_flag",0));
		if (null != count && count > 0) {
			data.put("success",false);
			data.put("code",400);
			data.put("msg","岗位名称已存在");
			return R.data(data);

		}
		try {
			departmentJobService.saveDepartmentJob(departmentJob, bladeUser);
		} catch (Exception e) {
			e.printStackTrace();
			data.put("success",false);
			data.put("code",400);
			data.put("msg",e.getMessage());
			return R.data(data);

		}
		data.put("success",true);
		data.put("code",200);
		data.put("msg","导入成功");
		return R.data(data);
	}

	/**
	 * 下载部门模板
	 */
	@GetMapping("/downLoadDpJobMoBan")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "下载岗位模板", notes = "下载岗位模板")
	public R downLoadDpJobMoBan() {
		return R.data(departmentJobService.getDpJobUrl());
	}
}