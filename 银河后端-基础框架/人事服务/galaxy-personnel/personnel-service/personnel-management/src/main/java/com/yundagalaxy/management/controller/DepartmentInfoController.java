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

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.view.MiniAbstractExcelView;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yundagalaxy.common.utils.EasyExcelUtil;
import com.yundagalaxy.management.commnon.utils.DictUtil;
import com.yundagalaxy.management.entity.DepartmentInfo;
import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import com.yundagalaxy.management.model.DepartmentInfoModel;
import com.yundagalaxy.management.service.IDepartmentInfoService;
import com.yundagalaxy.management.service.IEmployeeBasicInfoService;
import com.yundagalaxy.management.vo.DepartmentInfoVO;
import com.yundagalaxy.management.vo.EmployeeBasicInfoVO;
import com.yundagalaxy.management.vo.IdCommonVO;
import com.yundagalaxy.management.wrapper.DeptWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门岗位表 控制器
 *
 * @author dongfeng
 * @since 2019-10-16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/departmentInfo")
@Api(value = "部门管理接口", tags = "部门管理接口")
@Slf4j
public class DepartmentInfoController extends BladeController {


	private IDepartmentInfoService departmentInfoService;

    private MiniAbstractExcelView miniAbstractExcelView;

    private IEmployeeBasicInfoService employeeBasicInfoService;


	private DictUtil dictUtil;



	/**
	 * 查询所属部门 - 下拉框
	 * @param
	 * @return
	 */
	@GetMapping("/getDpmentName")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "查询所属部门-下拉框", notes = "下拉框")
	public R<List<Map<String,Object>>> getDpmentName(BladeUser bladeUser) {
		List<Map<String,Object>>  lsMap = new ArrayList<>();
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		List<DepartmentInfo> list = departmentInfoService.list(Condition.getQueryWrapper(new DepartmentInfo()).lambda().eq(DepartmentInfo::getCpCode,cpCode ));
		if(null!=list && list.size()>0){
			for (DepartmentInfo vv:list) {
				Map<String,Object> map = new HashMap<>();
				map.put("dpmentCode", vv.getDpmentCode());
				map.put("dpmentName",vv.getDpmentName());
				map.put("dpmentLevel",vv.getDpmentLevel());
				lsMap.add(map);
			}
		}
		return R.data(lsMap);
	}

	/**
	 * 获取部门树形结构
	 *
	 * @return
	 */
	@GetMapping("/tree")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "树形结构", notes = "树形结构")
	public R<List<DepartmentInfoVO>> tree(Integer dpmentLevel,BladeUser bladeUser) {
		Map map = new HashMap();
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		map.put("dpmentLevel",dpmentLevel);
		map.put("cpCode",cpCode);
		List<DepartmentInfoVO> tree = departmentInfoService.tree(map);
		return R.data(tree);
	}

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入departmentInfo")
	public R<DepartmentInfoVO> detail(DepartmentInfo departmentInfo) {
		DepartmentInfo detail = departmentInfoService.getOne(Condition.getQueryWrapper(departmentInfo));
		DepartmentInfoVO vo = BeanUtil.copy(detail,DepartmentInfoVO.class);
		return R.data(vo);
	}

	/**
	 * 分页 部门岗位表
	 */
	@GetMapping("/treeList")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "部门管理树列表", notes = "传入departmentInfo")
	public R treeList(@Valid DepartmentInfo departmentInfo,BladeUser bladeUser) {

		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		departmentInfo.setCpCode(cpCode);
		List<DepartmentInfoVO> list = departmentInfoService.getLockList(departmentInfo);
        for (DepartmentInfoVO vo:list){
			DepartmentInfo dep = departmentInfoService.getOne(new QueryWrapper<DepartmentInfo>().eq("dpment_code",vo.getParentDpmentCode()));
			if(null!=dep){
				vo.setParentDpmentName(dep.getDpmentName());
			}
			String dpLevelName = dictUtil.getDictValue(DictUtil.DictCode.DPMENT_LEVEL,vo.getDpmentLevel());
			vo.setDpmentLevelName(dpLevelName);
			String busModelName = dictUtil.getDictValue(DictUtil.DictCode.BUSINESS_MODEL,vo.getBusinessModel());
			vo.setBusinessModelName(busModelName);
		}
		//		List<INode> ch = DeptWrapper.build().listNodeVO(list);
		return R.data(DeptWrapper.build().listNodeVO(list));

	}


	/**
	 * 自定义分页 部门岗位表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入departmentInfo")
	public R<IPage<DepartmentInfoVO>> page(DepartmentInfoVO departmentInfo, Query query, BladeUser bladeUser) {
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		departmentInfo.setCpCode(cpCode);
		IPage<DepartmentInfoVO> pages = departmentInfoService.selectDepartmentInfoPage(Condition.getPage(query), departmentInfo);
		return R.data(pages);
	}

	/**
	 * 新增 部门岗位表
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增部门", notes = "传入departmentInfo")
	public R save(@Valid @RequestBody DepartmentInfo departmentInfo, BladeUser bladeUser) {

		return R.status(departmentInfoService.saveDepartmentInfo(departmentInfo,bladeUser));
	}

	/**
	 * 修改 部门岗位表
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改部门", notes = "传入departmentInfo")
	public R update(@Valid @RequestBody DepartmentInfo departmentInfo, BladeUser bladeUser) {
		return R.status(departmentInfoService.updateDepartmentInfo(departmentInfo,bladeUser));
	}

	/**
	 * 新增或修改 部门岗位表
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入departmentInfo")
	public R submit(@Valid @RequestBody DepartmentInfo departmentInfo) {
		return R.status(departmentInfoService.saveOrUpdate(departmentInfo));
	}


	/**
	 * 删除 部门岗位表
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "逻辑删除", notes = "传入dpmentIds")
	public R remove(@RequestBody IdCommonVO idCommonVO, BladeUser bladeUser) {

		if(StringUtils.isEmpty(idCommonVO.getDpmentIds())){
			return R.fail("参数dpmentIds不能为空");
		}
		return R.status(departmentInfoService.deleteDepartmentInfo(Func.toLongList(idCommonVO.getDpmentIds()),bladeUser));
	}
	/**
	 * 组织架构管理
	 */
	@GetMapping("/structure")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "组织架构管理", notes = "传入cpCode加token")
	public R structure(@RequestParam("cpCode") Integer cpCode, BladeUser bladeUser) {
        if(null==cpCode){
        	cpCode=Integer.parseInt(bladeUser.getDeptId());
		}
		Map map = departmentInfoService.getStructure(cpCode,bladeUser);
		return R.data(map);
	}

	/**
	 * 选择上级部门
	 */
	@GetMapping("/chooseSuperior")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "选择上级部门", notes = "选择上级部门")
	public R chooseSuperior(Integer dpmentLevel,BladeUser bladeUser) {
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		DepartmentInfo departmentInfo = new DepartmentInfo();
        //选择二级
		if(null!=dpmentLevel&&dpmentLevel>2){
			return R.fail("部门层级不能大于2");
		}
		departmentInfo.setDpmentLevel(dpmentLevel);
		departmentInfo.setCpCode(cpCode);
		List<DepartmentInfoVO> list = departmentInfoService.getLockList(departmentInfo);
		List<Map<String,Object>> resData = new ArrayList<>();
		if(null!=list&&list.size()>0){
			for (DepartmentInfo rp:list){
				Map mm = new HashMap(2);
				mm.put("dpmentCode",rp.getDpmentCode());
				mm.put("dpmentName",rp.getDpmentName());
				Integer num = 0;
				//传2选择部门不能大于3
                if(null!=dpmentLevel&&dpmentLevel==2){
					num = rp.getDpmentLevel()+1;
				}else {
					num = rp.getDpmentLevel();
				}

				mm.put("dpmentLevel",num);
				String dpLevelName = dictUtil.getDictValue(DictUtil.DictCode.DPMENT_LEVEL,num);
				mm.put("dpmentLevelName",dpLevelName);
				mm.put("businessModel",rp.getBusinessModel());
				mm.put("businessModelDictValue",rp.getBusinessModel()==0?"":dictUtil.getDictValue(DictUtil.DictCode.BUSINESS_MODEL, rp.getBusinessModel()));

               resData.add(mm);
			}
		}
		return R.data(resData);
	}

	/**
	 * 选择上级部门
	 */
	@GetMapping("/exportDepartmentInfo")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "导出部门", notes = "导出部门")
	public R exportDepartmentInfo(HttpServletRequest request, HttpServletResponse response){

		//获得营业明细(含收支)信息
		List<Map<String,Object>> list = new ArrayList<>();
		Map<String,Object> mm = new HashMap<>();
		mm.put("id",111);
		mm.put("class","班级");
		list.add(mm);

		// 创建参数对象（用来设定excel得sheet得内容等信息）
		ExportParams params = new ExportParams() ;
		// 设置sheet得名称
		params.setSheetName("营业收支明细");


		List<ExcelExportEntity> columnList = new ArrayList<ExcelExportEntity>();
		ExcelExportEntity colEntity1 = new ExcelExportEntity("序号", "id");
		colEntity1.setNeedMerge(true);
		columnList.add(colEntity1);

		ExcelExportEntity colEntity2 = new ExcelExportEntity("班级", "class");
		colEntity2.setNeedMerge(true);
		columnList.add(colEntity2);

		// 执行方法
		ExportParams exportParams = new ExportParams("班级信息", "人员数据");
		Workbook workbook = ExcelExportUtil.exportExcel(exportParams, columnList, list);

		try{
			miniAbstractExcelView.out(workbook,"测试",request,response);
		}catch (Exception e){
        	e.printStackTrace();
        	log.error(e.getMessage());
		}
		return R.success("成功");
	}


	/**
	 * 组织架构管理-获取员工(正式)
	 */
	@GetMapping("/getEmpPage")
	@ApiOperationSupport(order = 11)
	@ApiOperation(value = "分页", notes = "传入employeeBasicInfo")
	public R<IPage<EmployeeBasicInfoVO>> page(EmployeeBasicInfoVO employeeBasicInfo, Query query,BladeUser bladeUser) {
		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		if(null==employeeBasicInfo.getCpCode()){
			employeeBasicInfo.setCpCode(cpCode);
		}
        employeeBasicInfo.setEndWorkingState(1);
		IPage<EmployeeBasicInfoVO> pages = employeeBasicInfoService.selectEmpBasicInfoPage(Condition.getPage(query), employeeBasicInfo);
		return R.data(pages);
	}

	/**
	 * 下载部门模板
	 */
//	@GetMapping("/downLoadDpInfoMoBan")
//	@ApiOperationSupport(order = 12)
//	@ApiOperation(value = "下载部门模板", notes = "下载部门模板")
//	public R downLoadDpInfoMoBan(HttpServletRequest request, HttpServletResponse response){
//
//		//获得营业明细(含收支)信息
//		List<Map<String,Object>> dpList = new ArrayList<>();
//		Map<String,Object> one = new HashMap<>();
//		one.put("dpmentName","韵达客服部1");
//		one.put("parentDpmentCode","");
//		one.put("parentDpmentName","无上级");
//		one.put("businessModel","承包");
//		one.put("empCode","JE00000001");
//		dpList.add(one);
//		Map<String,Object> two = new HashMap<>();
//		two.put("dpmentName","韵达客服部2");
//		two.put("parentDpmentCode","JD00000001");
//		two.put("parentDpmentName","客服部");
//		two.put("businessModel","直营");
//		two.put("empCode","JE00000002");
//		dpList.add(two);
//
//		// 创建参数对象（用来设定excel得sheet得内容等信息）
//		ExportParams exportParams = new ExportParams() ;
//		// 设置sheet得名称
//		exportParams.setSheetName("导入模板");
////		params.setTitle("");
//
//		List<ExcelExportEntity> columnList = new ArrayList<ExcelExportEntity>();
//		ExcelExportEntity dpmentName = new ExcelExportEntity("部门名称", "dpmentName");
//		dpmentName.setNeedMerge(true);
//		columnList.add(dpmentName);
//
//		ExcelExportEntity parentDpmentCode = new ExcelExportEntity("上级部门编号", "parentDpmentCode");
//		parentDpmentCode.setNeedMerge(true);
//		columnList.add(parentDpmentCode);
//
//
//		ExcelExportEntity parentDpmentName = new ExcelExportEntity("上级部门", "parentDpmentName");
//		parentDpmentName.setNeedMerge(true);
//		columnList.add(parentDpmentName);
//
//		ExcelExportEntity businessModel = new ExcelExportEntity("经营模式", "businessModel");
//		businessModel.setNeedMerge(true);
//		columnList.add(businessModel);
//
//		ExcelExportEntity empCode = new ExcelExportEntity("部门负责人（员工工号）", "empCode");
//		empCode.setNeedMerge(true);
//		columnList.add(empCode);
//
//		// 执行方法
//		Workbook workbook = ExcelExportUtil.exportExcel(exportParams, columnList, dpList);
//
//		try{
//			miniAbstractExcelView.out(workbook,"bumenmoban",request,response);
//		}catch (Exception e){
//			e.printStackTrace();
//			log.error(e.getMessage());
//
//		}
//		return R.success("下载成功");
//	}

	/**
	 * 导入部门数据
	 */
	@PostMapping("/exportDpInfoDataAll")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "导入部门数据", notes = "导入部门数据")
	public R exportDpInfoDataAll(@RequestParam("file") MultipartFile file,HttpServletRequest request,BladeUser bladeUser){
        Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		Map<String,Object> result = null;
		try {
			result = EasyExcelUtil.readExcel(file,new DepartmentInfoModel(),1);

		}catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}
		boolean flag = (boolean) result.get("flag");
		List<Map<String,Object>> lo = new ArrayList<>();
		Map resData = new HashMap();
		Integer failNum=0;
		Integer successNum=0;
		Integer allNum=0;
		if(flag){
			List<Object> dd = (List<Object>) result.get("data");
			if(dd != null && dd.size() > 0){
				allNum = dd.size();
				for(Object o : dd){
					DepartmentInfo departmentInfo = new DepartmentInfo();
					Map<String,Object>  mp = new HashMap<>();
					DepartmentInfoModel dm = (DepartmentInfoModel) o;
					mp.put("dpmentName",dm.getDpmentName());
					mp.put("parentDpmentCode",dm.getParentDpmentCode());
					mp.put("parentDpmentName",dm.getParentDpmentName());
					mp.put("businessModelName",dm.getBusinessModelName());
					mp.put("empCode",dm.getEmpCode());
					if(null!=dm&&StringUtils.isEmpty(dm.getDpmentName())){
						failNum++;
						mp.put("msg","部门名称不能为空");
						mp.put("success",false);
						mp.put("code",500);
						lo.add(mp);
						continue;
					}
					departmentInfo.setDpmentName(dm.getParentDpmentName());
					Integer count =  departmentInfoService.count(new QueryWrapper<DepartmentInfo>().eq("cp_code",cpCode).eq("dpment_name",dm.getDpmentName()));
					if(null!=count&&count>0){
						failNum++;
						mp.put("msg","部门名称已存在");
						mp.put("success",false);
						mp.put("code",500);
						lo.add(mp);
						continue;
					}

					if(!StringUtils.isEmpty(dm.getParentDpmentCode())){
						DepartmentInfo pd =  departmentInfoService.getOne(new QueryWrapper<DepartmentInfo>().eq("cp_code",cpCode).eq("dpment_code",dm.getParentDpmentCode()));
						if(null==pd){
							failNum++;
							mp.put("msg","上级部门不存在");
							mp.put("success",false);
							mp.put("code",500);
							lo.add(mp);
							continue;
						}
						departmentInfo.setParentDpmentCode(dm.getParentDpmentCode());
                        if(null!=pd.getDpmentLevel()&&pd.getDpmentLevel()>=2){
							failNum++;
							mp.put("msg","不能建立四级部门");
							mp.put("success",false);
							mp.put("code",500);
							lo.add(mp);
							continue;
						}
                        departmentInfo.setDpmentLevel(pd.getDpmentLevel()+1);
					}else{
						departmentInfo.setDpmentLevel(1);
					}

					if(StringUtils.isEmpty(dm.getBusinessModelName())){
						failNum++;
						mp.put("msg","经营模式不能为空");
						mp.put("success",false);
						mp.put("code",500);
						lo.add(mp);
						continue;
					}
					Integer busModel = dictUtil.getDictKey(DictUtil.DictCode.BUSINESS_MODEL,dm.getBusinessModelName());
					if(null!=busModel){
						departmentInfo.setBusinessModel(busModel);
					}else{
						failNum++;
						mp.put("msg","经营模式不对");
						mp.put("success",false);
						mp.put("code",500);
						lo.add(mp);
						continue;
					}
					if(!StringUtils.isEmpty(dm.getEmpCode())){
						EmployeeBasicInfo emp = employeeBasicInfoService.getOne(new QueryWrapper<EmployeeBasicInfo>().eq("emp_code",dm.getEmpCode()).eq("cp_code",cpCode));
					    if(null!=emp&&!StringUtils.isEmpty(emp.getName())){
					    	departmentInfo.setDpmentHead(emp.getName());
						}else{
							failNum++;
							mp.put("msg","部门负责人不存在");
							mp.put("success",false);
							mp.put("code",500);
							lo.add(mp);
							continue;
						}
					}
					try{
						departmentInfoService.saveDepartmentInfo(departmentInfo,bladeUser);
					}catch (Exception e){
						e.printStackTrace();
						failNum++;
						mp.put("msg","导入失败");
						mp.put("success",false);
						mp.put("code",500);
						lo.add(mp);
						continue;
					}
					mp.put("msg","成功");
					mp.put("success",true);
					mp.put("code",200);
					lo.add(mp);
					successNum++;
				}

			}
		}else{
			log.error("表头格式错误");
		}
		resData.put("data",lo);
		resData.put("failNum",failNum);
		resData.put("successNum",successNum);
		resData.put("allNum",allNum);
		return R.data(resData);
	}
	/**
	 * 导入部门数据
	 */
	@PostMapping("/exportDpInfoTest")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "导入部门数据测试", notes = "导入部门数据测试")
	public R exportDpInfoTest(@RequestParam("file") MultipartFile file,HttpServletRequest request){



		return R.success("导入成功");
	}


	/**
	 * 导入部门数据
	 */
	@PostMapping("/exportDpInfoExcel")
	@ApiOperationSupport(order = 14)
	@ApiOperation(value = "导入部门Excel返回数据", notes = "导入部门Excel返回数据")
	public R exportDpInfoExcel(@RequestParam("file") MultipartFile file){

		Map<String,Object> result = null;
		try {
			result = EasyExcelUtil.readExcel(file,new DepartmentInfoModel(),1);

		}catch (Exception e){
			log.error(e.getMessage());
		}
		boolean flag = (boolean) result.get("flag");
		List<Object> resultData = new ArrayList<>();
		if(flag) {
			resultData = (List<Object>) result.get("data");
		}else {
			return R.data(400,resultData,"导入文件格式有误，请重新选择");
		}
		return R.data(resultData);
	}
	/**
	 * 导入部门数据
	 */
	@PostMapping("/exportDpInfoData")
	@ApiOperationSupport(order = 15)
	@ApiOperation(value = "导入部门数据", notes = "导入部门数据")
	public R exportDpInfoData(@RequestBody DepartmentInfoModel dm,BladeUser bladeUser){

		Integer cpCode = Integer.parseInt(bladeUser.getDeptId());
		DepartmentInfo departmentInfo = new DepartmentInfo();
        Map<String,Object> data = new HashMap<>(3);

		if(null!=dm&&StringUtils.isEmpty(dm.getDpmentName())){
            data.put("success",false);
            data.put("code",400);
            data.put("msg","部门名称不能为空");
			return R.data(data);
		}
		departmentInfo.setDpmentName(dm.getDpmentName());
		Integer count =  departmentInfoService.count(new QueryWrapper<DepartmentInfo>().eq("cp_code",cpCode).eq("dpment_name",dm.getDpmentName()).eq("del_flag",0));
		if(null!=count&&count>0){
            data.put("success",false);
            data.put("code",400);
            data.put("msg","部门名称已存在");
			return R.data(data);
		}
		if(!StringUtils.isEmpty(dm.getParentDpmentCode())){
			DepartmentInfo pd =  departmentInfoService.getOne(new QueryWrapper<DepartmentInfo>().eq("cp_code",cpCode).eq("dpment_code",dm.getParentDpmentCode()).eq("del_flag",0));
			if(null==pd){
                data.put("success",false);
                data.put("code",400);
                data.put("msg","上级部门不存在");
                return R.data(data);

			}
			departmentInfo.setParentDpmentCode(dm.getParentDpmentCode());
			if(null!=pd.getDpmentLevel()&&pd.getDpmentLevel()>=2){
                data.put("success",false);
                data.put("code",400);
                data.put("msg","不能建立四级部门");
                return R.data(data);

			}
			departmentInfo.setDpmentLevel(pd.getDpmentLevel()+1);
		}else{
			departmentInfo.setDpmentLevel(1);
		}
		if(StringUtils.isEmpty(dm.getBusinessModelName())){
            data.put("success",false);
            data.put("code",400);
            data.put("msg","经营模式不能为空");
            return R.data(data);
		}
		Integer busModel = dictUtil.getDictKey(DictUtil.DictCode.BUSINESS_MODEL,dm.getBusinessModelName());
		if(null!=busModel){
			departmentInfo.setBusinessModel(busModel);
		}else{
            data.put("success",false);
            data.put("code",400);
            data.put("msg","经营模式不对");
            return R.data(data);
		}
		if(!StringUtils.isEmpty(dm.getEmpCode())){
			EmployeeBasicInfo emp = employeeBasicInfoService.getOne(new QueryWrapper<EmployeeBasicInfo>().eq("emp_code",dm.getEmpCode()).eq("cp_code",cpCode));
			if(null!=emp&&!StringUtils.isEmpty(emp.getName())){
				departmentInfo.setDpmentHead(emp.getName());
			}else{
                data.put("success",false);
                data.put("code",400);
                data.put("msg","部门负责人不存在");
                return R.data(data);

			}
		}
		try{
			departmentInfoService.saveDepartmentInfo(departmentInfo,bladeUser);
		}catch (Exception e){
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
	@GetMapping("/downLoadDpInfoMoBan")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "下载部门模板", notes = "下载部门模板")
	public R downLoadDpInfoMoBan() {
	   return R.data(departmentInfoService.getDpInfoUrl());
	}

}
