package com.yundagalaxy.management.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.view.MiniAbstractExcelView;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yundagalaxy.common.constant.CommonResultConstant;
import com.yundagalaxy.management.commnon.service.impl.DistributedLock;
import com.yundagalaxy.management.commnon.utils.DictCpToVoUtil;
import com.yundagalaxy.management.commnon.utils.DictUtil;
import com.yundagalaxy.management.dto.SoaEmpDTO;
import com.yundagalaxy.management.entity.*;
import com.yundagalaxy.management.service.*;
import com.yundagalaxy.management.vo.EmployeeInfoAggregationVO;
import com.yundagalaxy.management.vo.ExcelEmployeeInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.yundagalaxy.common.constant.CodeConstant.*;

/**
 * 员工信息聚合处理。
 * <p>
 * Created by MiaoYuanMeng on 2019/10/18.
 */
@RestController
@AllArgsConstructor
@RequestMapping("/employeeinfoaggregation")
@Api(value = "员工信息聚合", tags = "员工信息聚合处理接口")
public class EmployeeInfoAggregationController extends BladeController {

    private IEmployeeInfoAggregationService employeeInfoAggregationService;

    private IEmployeeBasicInfoService employeeBasicInfoService;

    private IEmployeeDetailInfoService employeeDetailInfoService;

    private IEmployeeSalaryInfoService employeeSalaryInfoService;

    private IDepartmentInfoService departmentInfoService;

    private MiniAbstractExcelView miniAbstractExcelView;

    private IDepartmentJobService departmentJobService;

    private ISoaEmpService soaEmpService;

    @Autowired
    private DistributedLock distributedLock;

    @Autowired
    private DictUtil dictUtil;

    @Autowired
    private DictCpToVoUtil dictCpToVoUtil;

    /**
     * 聚合 - 详情 员工信息
     *
     * @param empCode
     * @return
     */
    @GetMapping("/getPositives")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "详情", notes = "员工信息")
    public R<Map<String, Object>> getPositives(@RequestParam("empCode") String empCode) {
        if (StringUtils.isEmpty(empCode)){
            return R.fail("未找到该员工！");
        }
        Map<String, Object> mapResult = new HashMap<>();
        EmployeeBasicInfo employeeBasicInfo = employeeBasicInfoService.getOne(Condition.getQueryWrapper(new EmployeeBasicInfo()).lambda()
                .eq(EmployeeBasicInfo::getEmpCode, empCode));
        EmployeeDetailInfo employeeDetailInfo = employeeDetailInfoService.getOne(Condition.getQueryWrapper(new EmployeeDetailInfo()).lambda()
                .eq(EmployeeDetailInfo::getEmpCode, empCode));
        EmployeeSalaryInfo employeeSalaryInfo = employeeSalaryInfoService.getOne(Condition.getQueryWrapper(new EmployeeSalaryInfo()).lambda()
                .eq(EmployeeSalaryInfo::getEmpCode, empCode));
        mapResult.put("employeeBasicInfo", dictCpToVoUtil.entityVO(employeeBasicInfo));
        mapResult.put("employeeDetailInfo", dictCpToVoUtil.entityVO(employeeDetailInfo));
        mapResult.put("employeeSalaryInfo", dictCpToVoUtil.entityVO(employeeSalaryInfo));
        return R.data(mapResult);
    }


    /**
     * 修改 员工基本信息表
     */
    @PostMapping("/updateBasic")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改员工基本信息表", notes = "传入employeeBasicInfo")
    public R updateBasic(@Valid @RequestBody EmployeeBasicInfo employeeBasicInfo, BladeUser user) {
        employeeBasicInfo.setFinalBy(user.getNickName());
        employeeBasicInfo.setLastUpdate(LocalDateTime.now());

        SoaEmpDTO soaEmpDTO = new SoaEmpDTO();
        soaEmpDTO.setIdCard(employeeBasicInfo.getIdCode());
        soaEmpDTO.setCpCode(Integer.valueOf(user.getDeptId()));
        soaEmpDTO.setPhone(employeeBasicInfo.getPhoneNo());
        soaEmpDTO.setSoaCode(employeeBasicInfo.getSoaCode());
        soaEmpDTO.setSex(employeeBasicInfo.getSex());
        soaEmpDTO.setName(employeeBasicInfo.getName());
        DepartmentJob departmentJob = departmentJobService.getOne(Condition.getQueryWrapper(new DepartmentJob()).lambda().eq(DepartmentJob::getJobCode, employeeBasicInfo.getJobCode()));
        soaEmpDTO.setJob(departmentJob.getJobType());
        soaEmpDTO.setStringStatus(employeeBasicInfo.getWorkingState()==0?"10"
                :(employeeBasicInfo.getWorkingState()==1?"11"
                :(employeeBasicInfo.getWorkingState()==2?"15"
                :(employeeBasicInfo.getWorkingState()==3?"18":"0"))));
        R r =  employeeInfoAggregationService.updateToSoa(soaEmpDTO, user);
        if (r.getCode()==0){
            employeeBasicInfoService.updateById(employeeBasicInfo);
            return R.status(true);
        }else {
            return R.fail(r.getMsg());
        }
    }

    /**
     * 修改 员工详细信息表
     */
    @PostMapping("/updateDetail")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改员工详细信息表", notes = "传入employeeDetailInfoDTO")
    @Transactional
    public R updateDetail(@RequestBody EmployeeDetailInfo employeeDetailInfo, BladeUser user) {
        employeeBasicInfoService.updateDateTimeByEmpCode(employeeDetailInfo.getEmpCode(), user.getUserName());
        return R.status(employeeDetailInfoService.updateById(employeeDetailInfo));
    }

    /**
     * 修改 员工薪酬信息表
     */
    @PostMapping("/updateSalary")
    @ApiOperationSupport(order = 5)
    @ApiOperation(value = "修改员工薪酬信息表", notes = "传入employeeSalaryInfo")
    @Transactional
    public R updateSalary(@Valid @RequestBody EmployeeSalaryInfo employeeSalaryInfo, BladeUser user) {
        employeeBasicInfoService.updateDateTimeByEmpCode(employeeSalaryInfo.getEmpCode(), user.getUserName());
        return R.status(employeeSalaryInfoService.updateById(employeeSalaryInfo));
    }

    /**
     * 删除 员工信息表
     */
    @PostMapping("/remove")
    @Transactional
    @ApiOperationSupport(order = 8)
    @ApiOperation(value = "删除员工信息表", notes = "传入empCode")
    public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam("empCode") String empCode) {
        if (!StringUtil.isEmpty(empCode)) {
            employeeBasicInfoService.remove(Condition.getQueryWrapper(new EmployeeBasicInfo()).lambda()
                    .eq(EmployeeBasicInfo::getEmpCode, empCode));
            employeeDetailInfoService.remove(Condition.getQueryWrapper(new EmployeeDetailInfo()).lambda()
                    .eq(EmployeeDetailInfo::getEmpCode, empCode));
            employeeSalaryInfoService.remove(Condition.getQueryWrapper(new EmployeeSalaryInfo()).lambda()
                    .eq(EmployeeSalaryInfo::getEmpCode, empCode));
        }
        return R.status(true);
    }

    /**
     * 注销SOA
     */
    @GetMapping("/cancellationSoa")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "注销SOA", notes = "注销SOA")
    public R cancellationSoa(@RequestParam("soaCode") String soaCode, BladeUser bladeUser) {
        SoaEmp soaEmp = soaEmpService.getOne(Condition.getQueryWrapper(new SoaEmp()).lambda()
                .eq(SoaEmp::getSoaCode, soaCode));
        SoaEmpDTO soaEmpDTO = new SoaEmpDTO();
        soaEmpDTO.setCpCode(soaEmp.getCpCode());
        soaEmpDTO.setIdCard(soaEmp.getIdCard());
        soaEmpDTO.setPhone(soaEmp.getPhone());
        soaEmpDTO.setSoaCode(soaEmp.getSoaCode());
        soaEmpDTO.setSex(soaEmp.getSex());
        soaEmpDTO.setName(soaEmp.getName());
        soaEmpDTO.setJob(soaEmp.getJob());
        //离职
        soaEmpDTO.setStringStatus(SoaEmpDTO.DictCode.LZ.getCode());
        if (soaEmp!=null){
            R r =  employeeInfoAggregationService.updateToSoa(soaEmpDTO, bladeUser);
            if (r.getCode()==0){
                //刪除
//                soaEmpService.(soaCode);
                soaEmpService.updateJoinStatus(soaCode,1);
                return R.status(true);
            }else {
                return R.fail(r.getMsg());
            }
        }
        return fail("没有找到该员工！");
    }

    /**
     * 聚合 - 新增 员工信息
     *
     * @param employeeInfoAggregationVO
     * @param user
     * @return
     */
    @PostMapping("/save")
    @Transactional
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "新增", notes = "传入{employeeBasicData,employeeDetailData,employeeSalaryData}")
    public R save(@Valid @RequestBody EmployeeInfoAggregationVO employeeInfoAggregationVO, BladeUser user) {
        try {
            if (user == null) {
                return R.fail("缺少用户信息！");
            }
            //基本员工信息
            EmployeeBasicInfo employeeBasicInfo = employeeInfoAggregationVO.getEmployeeBasicInfo();

            //员工入住
            if (StringUtils.isEmpty(employeeBasicInfo.getSoaCode())){
                R r = employeeInfoAggregationService.addToSoa(employeeBasicInfo,user);
                if (r.getCode()==0){
                    employeeBasicInfo.setSoaCode(r.getData().toString());
                }else {
                    return R.fail(r.getMsg());
                }
            //账号入职维护
            }else {
                if (soaEmpService.getOne(Condition.getQueryWrapper(new SoaEmp()).lambda().eq(SoaEmp::getSoaCode, employeeBasicInfo.getSoaCode())).getStatus()!=0){
                    return R.fail("该员工已经入职！");
                }
                if (employeeBasicInfoService.count(Condition.getQueryWrapper(new EmployeeBasicInfo()).lambda()
                        .eq(EmployeeBasicInfo::getSoaCode, employeeInfoAggregationVO.getEmployeeBasicInfo().getSoaCode()))>0){
                    return R.fail("员工账号已存在！");
                }
                SoaEmpDTO soaEmpDTO = new SoaEmpDTO();
                soaEmpDTO.setIdCard(employeeBasicInfo.getIdCode());
                soaEmpDTO.setCpCode(Integer.valueOf(user.getDeptId()));
                soaEmpDTO.setPhone(employeeBasicInfo.getPhoneNo());
                soaEmpDTO.setSoaCode(employeeBasicInfo.getSoaCode());
                soaEmpDTO.setSex(employeeBasicInfo.getSex());
                soaEmpDTO.setName(employeeBasicInfo.getName());
                DepartmentJob departmentJob = departmentJobService.getOne(Condition.getQueryWrapper(new DepartmentJob()).lambda().eq(DepartmentJob::getJobCode, employeeBasicInfo.getJobCode()));
                soaEmpDTO.setJob(departmentJob.getJobType());
                soaEmpDTO.setStringStatus(employeeBasicInfo.getWorkingState()==0?"10"
                        :(employeeBasicInfo.getWorkingState()==1?"11"
                        :(employeeBasicInfo.getWorkingState()==2?"15"
                        :(employeeBasicInfo.getWorkingState()==3?"18":"0"))));
                R r =  employeeInfoAggregationService.updateToSoa(soaEmpDTO, user);
                if (r.getCode()==0){
                    employeeBasicInfoService.updateById(employeeBasicInfo);
                    soaEmpService.updateJoinStatus(employeeBasicInfo.getSoaCode(),1);
//                    return R.status(true);
                }else {
                    return R.fail(r.getMsg());
                }
            }
            employeeBasicInfo.setCreateTime(LocalDateTime.now());
            employeeBasicInfo.setEmpCode(distributedLock.getNewCodeMax(
                    PRE_GROUP_EMP_CODE,
                    EMP_CODE_LOCK_KEY,
                    LOCK_TIME,
                    REDIS_MAX_EMP_CODE_KEY,
                    5,
                    TimeUnit.SECONDS));
            employeeBasicInfo.setDelFlag(0);
            employeeBasicInfo.setCreateTime(LocalDateTime.now());
            employeeBasicInfo.setCreateBy(user.getNickName());
            employeeBasicInfo.setCpCode(Integer.valueOf(user.getDeptId()));
            employeeBasicInfo.setFinalBy(user.getNickName());
            employeeBasicInfo.setLastUpdate(LocalDateTime.now());
            //员工详细信息
            EmployeeDetailInfo employeeDetailInfo = employeeInfoAggregationVO.getEmployeeDetailInfo();
            employeeDetailInfo.setEmpCode(employeeBasicInfo.getEmpCode());
            //员工薪酬信息
            EmployeeSalaryInfo employeeSalaryInfo = employeeInfoAggregationVO.getEmployeeSalaryInfo();
            employeeSalaryInfo.setEmpCode(employeeBasicInfo.getEmpCode());
            return R.status(employeeInfoAggregationService.save(employeeBasicInfo, employeeDetailInfo, employeeSalaryInfo));
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(CommonResultConstant.DATA_IN_WRONG_FORMAT);
        }
    }

    /**
     * 分页 人员信息查询
     */
    @GetMapping("/empList")
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "人员信息查询", notes = "传入params")
    public R<IPage<ExcelEmployeeInfoVO>> empList(@RequestParam Map<String, Object> params, Query query, BladeUser bladeUser) {
//        if ((params == null || params.size() == 0)){
//            return fail("未传参！");
//        }
        if (params == null || params.get("cpCode") == null || "".equals(params.get("cpCode"))) {
            params.put("cpCode", Integer.valueOf(bladeUser.getDeptId()));
        }
        if (query.getCurrent()!=null && query.getCurrent()!=0){
            query.setCurrent((query.getCurrent()-1)*query.getSize());
        }
        List<ExcelEmployeeInfoVO> list = new ArrayList<>();
        if (params.get("cpCode") != null) {
            list = employeeInfoAggregationService.empList(params,query);
            for (ExcelEmployeeInfoVO vo : list) {
                vo.setCpName(departmentInfoService.getYdserverCpName(Integer.valueOf(params.get("cpCode").toString())));
                vo.setSexDictValue(vo.getSex() == 0 ? "" : dictUtil.getDictValue(DictUtil.DictCode.SEX, vo.getSex()));
                vo.setWorkingStateDictValue(dictUtil.getDictValue(DictUtil.DictCode.WORKING_STATE, vo.getWorkingState()));
                vo.setJobTypeDictValue(vo.getJobType() == 0 ? "" : dictUtil.getDictValue(DictUtil.DictCode.JOB_TYPE, vo.getJobType()));
                vo.setJobLevelDictValue(vo.getJobLevel() == 0 ? "" : dictUtil.getDictValue(DictUtil.DictCode.JOB_LEVEL, vo.getJobLevel()));
                vo.setHouseholdTypeDictValue(vo.getHouseholdType() == 0 ? "" : dictUtil.getDictValue(DictUtil.DictCode.HOUSEHOLD_TYPE, vo.getHouseholdType()));
                vo.setMaritalStatusDictValue(vo.getMaritalStatus() == 0 ? "" : dictUtil.getDictValue(DictUtil.DictCode.MARITAL_STATUS, vo.getMaritalStatus()));
                vo.setBusinessModelDictValue(vo.getBusinessModel() == 0 ? "" : dictUtil.getDictValue(DictUtil.DictCode.BUSINESS_MODEL, vo.getBusinessModel()));
            }
        }
        IPage<ExcelEmployeeInfoVO> page = new Page<>();
        page.setRecords(list);
        page.setTotal(list.size());
        return R.data(page);
    }

    /**
     * 导出员工信息表
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportEmpInfo", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperationSupport(order = 2)
    @ApiOperation(value = "导出员工信息表", notes = "导出员工信息表")
    public R exportEmpInfo(@RequestParam Map<String, Object> params, Query query, HttpServletRequest request, HttpServletResponse response, BladeUser bladeUser) throws Exception {

        List<ExcelEmployeeInfoVO> list = new ArrayList<>();
        List<Map<String,Object>> mapList = new ArrayList<>();
        if ((params == null)) {
            return R.fail("没有对应的数据！");
        }
//        params.put("cpCode", Integer.valueOf(bladeUser.getDeptId()));
        params.put("cpCode", 1001);

        if (params.get("cpCode") != null) {
            list = employeeInfoAggregationService.empList(params,query);
            for (ExcelEmployeeInfoVO vo : list) {
                vo.setCpName(departmentInfoService.getYdserverCpName(Integer.valueOf(params.get("cpCode").toString())));
                Map<String,Object> map = new HashMap<>();
                map.put("cpCode", vo.getCpCode());
                map.put("cpName", vo.getCpName());
                map.put("name", vo.getName());
                map.put("empCode", vo.getEmpCode());
                map.put("soaCode", vo.getSoaCode());
                map.put("sex", dictUtil.getDictValue(DictUtil.DictCode.SEX, vo.getSex()));
                map.put("businessModel", dictUtil.getDictValue(DictUtil.DictCode.BUSINESS_MODEL, vo.getBusinessModel()));
                map.put("dpmentName", vo.getDpmentName());
                map.put("jobName", vo.getJobName());
                map.put("jobType", dictUtil.getDictValue(DictUtil.DictCode.JOB_TYPE, vo.getJobType()));
                map.put("jobLevel", dictUtil.getDictValue(DictUtil.DictCode.JOB_LEVEL, vo.getJobLevel()));
                map.put("hiredate", vo.getHiredate());
                map.put("workingState", dictUtil.getDictValue(DictUtil.DictCode.WORKING_STATE, vo.getWorkingState()));
                map.put("phoneNo", vo.getPhoneNo());
                map.put("nativePlace", vo.getHandover());
                map.put("idCode", vo.getIdCode());
                map.put("age", vo.getAge());
                map.put("education", dictUtil.getDictValue(DictUtil.DictCode.EDUCATION, vo.getEducation()));
                map.put("maritalStatus", dictUtil.getDictValue(DictUtil.DictCode.MARITAL_STATUS, vo.getMaritalStatus()));
                map.put("householdType", dictUtil.getDictValue(DictUtil.DictCode.HOUSEHOLD_TYPE, vo.getHouseholdType()));
                map.put("householdStreet", vo.getHouseholdStreet());
                map.put("currentStreet", vo.getCurrentStreet());
                map.put("emgContact", vo.getEmpCode());
                map.put("emgContactRlp", vo.getEmgContactRlp());
                map.put("emgContactMobile", vo.getEmgContactMobile());
                map.put("createTime", vo.getCreateTime());
                map.put("createBy", vo.getCreateBy());
                mapList.add(map);
            }
        }

        List<ExcelExportEntity> columnList = new ArrayList<ExcelExportEntity>();
        ExcelExportEntity colEntity1 = new ExcelExportEntity("网点编码", "cpCode");
        colEntity1.setNeedMerge(true);
        columnList.add(colEntity1);

        ExcelExportEntity colEntity2 = new ExcelExportEntity("网点名称", "cpName");
        colEntity2.setNeedMerge(true);
        columnList.add(colEntity2);

        ExcelExportEntity colEntity3 = new ExcelExportEntity("真实姓名", "name");
        colEntity3.setNeedMerge(true);
        columnList.add(colEntity3);

        ExcelExportEntity colEntity4 = new ExcelExportEntity("员工工号", "empCode");
        colEntity4.setNeedMerge(true);
        columnList.add(colEntity4);

        ExcelExportEntity colEntity5 = new ExcelExportEntity("账号", "soaCode");
        colEntity5.setNeedMerge(true);
        columnList.add(colEntity5);

        ExcelExportEntity colEntity6 = new ExcelExportEntity("性别", "sex");
//        colEntity6.setReplace(dictUtil.getDictValuesToExcelReplace(DictUtil.DictCode.SEX));
        colEntity6.setNeedMerge(true);
        columnList.add(colEntity6);

        ExcelExportEntity colEntity7 = new ExcelExportEntity("经营模式", "businessModel");
//        colEntity7.setReplace(dictUtil.getDictValuesToExcelReplace(DictUtil.DictCode.BUSINESS_MODEL));
        colEntity7.setNeedMerge(true);
        columnList.add(colEntity7);

        ExcelExportEntity colEntity8 = new ExcelExportEntity("部门", "dpmentName");
        colEntity8.setNeedMerge(true);
        columnList.add(colEntity8);

        ExcelExportEntity colEntity9 = new ExcelExportEntity("岗位", "jobName");
        colEntity9.setNeedMerge(true);
        columnList.add(colEntity9);

        ExcelExportEntity colEntity10 = new ExcelExportEntity("岗位类型", "jobType");
//        colEntity10.setReplace(dictUtil.getDictValuesToExcelReplace(DictUtil.DictCode.JOB_TYPE));
        colEntity10.setNeedMerge(true);
        columnList.add(colEntity10);

        ExcelExportEntity colEntity11 = new ExcelExportEntity("岗位级别", "jobLevel");
//        colEntity11.setReplace(dictUtil.getDictValuesToExcelReplace(DictUtil.DictCode.JOB_LEVEL));
        colEntity11.setNeedMerge(true);
        columnList.add(colEntity11);

        ExcelExportEntity colEntity12 = new ExcelExportEntity("入职日期", "hiredate");
        colEntity12.setNeedMerge(true);
        columnList.add(colEntity12);

        ExcelExportEntity colEntity13 = new ExcelExportEntity("在职状态", "workingState");
//        colEntity13.setReplace(dictUtil.getDictValuesToExcelReplace(DictUtil.DictCode.WORKING_STATE));
        colEntity13.setNeedMerge(true);
        columnList.add(colEntity13);

        ExcelExportEntity colEntity14 = new ExcelExportEntity("电话", "phoneNo");
        colEntity14.setNeedMerge(true);
        columnList.add(colEntity14);

        ExcelExportEntity colEntity15 = new ExcelExportEntity("籍贯", "nativePlace");
        colEntity15.setNeedMerge(true);
        columnList.add(colEntity15);

        ExcelExportEntity colEntity16 = new ExcelExportEntity("证件号码", "idCode");
        colEntity16.setNeedMerge(true);
        columnList.add(colEntity16);

        ExcelExportEntity colEntity17 = new ExcelExportEntity("年龄", "age");
        colEntity17.setNeedMerge(true);
        columnList.add(colEntity17);

        ExcelExportEntity colEntity18 = new ExcelExportEntity("学历", "education");
//        colEntity18.setReplace(dictUtil.getDictValuesToExcelReplace(DictUtil.DictCode.EDUCATION));
        colEntity18.setNeedMerge(true);
        columnList.add(colEntity18);

        ExcelExportEntity colEntity19 = new ExcelExportEntity("婚姻状态", "maritalStatus");
//        colEntity19.setReplace(dictUtil.getDictValuesToExcelReplace(DictUtil.DictCode.MARITAL_STATUS));
        colEntity19.setNeedMerge(true);
        columnList.add(colEntity19);

        ExcelExportEntity colEntity20 = new ExcelExportEntity("户口性质", "householdType");
//        colEntity20.setReplace(dictUtil.getDictValuesToExcelReplace(DictUtil.DictCode.HOUSEHOLD_TYPE));
        colEntity20.setNeedMerge(true);
        columnList.add(colEntity20);

        ExcelExportEntity colEntity21 = new ExcelExportEntity("户籍地址", "householdStreet");
        colEntity21.setNeedMerge(true);
        columnList.add(colEntity21);

        ExcelExportEntity colEntity22 = new ExcelExportEntity("现住地址", "currentStreet");
        colEntity22.setNeedMerge(true);
        columnList.add(colEntity22);

        ExcelExportEntity colEntity23 = new ExcelExportEntity("紧急联系人", "emgContact");
        colEntity23.setNeedMerge(true);
        columnList.add(colEntity23);

        ExcelExportEntity colEntity24 = new ExcelExportEntity("紧急联系人关系", "emgContactRlp");
        colEntity24.setNeedMerge(true);
        columnList.add(colEntity24);

        ExcelExportEntity colEntity25 = new ExcelExportEntity("紧急联系人电话", "emgContactMobile");
        colEntity25.setNeedMerge(true);
        columnList.add(colEntity25);

        ExcelExportEntity colEntity26 = new ExcelExportEntity("创建时间", "createTime");
        colEntity26.setNeedMerge(true);
        columnList.add(colEntity26);

        ExcelExportEntity colEntity27 = new ExcelExportEntity("创建人", "createBy");
        colEntity27.setNeedMerge(true);
        columnList.add(colEntity27);

        // 执行方法
        ExportParams exportParams = new ExportParams("员工信息表", "员工信息表");
        try{
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, columnList, mapList);
            miniAbstractExcelView.out(workbook,"员工信息表",request,response);
        }catch (Exception e){
            e.printStackTrace();
        }
        return R.status(true);
    }

    /**
     * 选择部门 带出岗位名称 - 下拉框
     *
     * @param dpmentCode
     * @return
     */
    @GetMapping("/getJobTypeByDepartment")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "部门带出岗位", notes = "下拉框")
    public R<List<Map<String, Object>>> getJobTypeByDepartment(@RequestParam("dpmentCode") String dpmentCode,BladeUser bladeUser) {
        List<Map<String, Object>> listMaps = new ArrayList<>();
        List<DepartmentJob> list = departmentJobService.list(Condition.getQueryWrapper(new DepartmentJob()).lambda()
                .eq(StringUtils.isNotEmpty(dpmentCode), DepartmentJob::getDpmentCode, dpmentCode)
                .eq(DepartmentJob::getCpCode, bladeUser.getDeptId())
//                .eq(StringUtils.isNotEmpty(jobType), DepartmentJob::getJobType, jobType)
                .groupBy(DepartmentJob::getJobType));
        for (DepartmentJob departmentJob : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("jobCode", departmentJob.getJobCode());
            map.put("jobName", departmentJob.getJobName());
            map.put("jobType", departmentJob.getJobType());
            map.put("dictKey", departmentJob.getJobType());
            map.put("dictValue", departmentJob.getJobType() == 0 ? "" : dictUtil.getDictValue(DictUtil.DictCode.JOB_TYPE, departmentJob.getJobType()));
            map.put("jobLevel", departmentJob.getJobLevel());
            map.put("jobLevelDictValue", departmentJob.getJobLevel() == 0 ? "" : dictUtil.getDictValue(DictUtil.DictCode.JOB_LEVEL, departmentJob.getJobLevel()));
            listMaps.add(map);
        }
        return R.data(listMaps);
    }

    /**
     * 选择部门 带出岗位名称 - 下拉框
     *
     * @param dpmentCode
     * @return
     */
    @GetMapping("/getJobNameByDepartment")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "部门带出岗位", notes = "下拉框")
    public R<List<Map<String, Object>>> getJobNameByDepartment(@RequestParam("dpmentCode") String dpmentCode, @RequestParam("jobType") String jobType,BladeUser bladeUser) {
        List<Map<String, Object>> listMaps = new ArrayList<>();
        List<DepartmentJob> list = departmentJobService.list(Condition.getQueryWrapper(new DepartmentJob()).lambda()
                .eq(StringUtils.isNotEmpty(dpmentCode), DepartmentJob::getDpmentCode, dpmentCode)
                .eq(StringUtils.isNotEmpty(jobType), DepartmentJob::getJobType, jobType)
                .eq(DepartmentJob::getCpCode, bladeUser.getDeptId())
                .groupBy(DepartmentJob::getJobName));
        for (DepartmentJob departmentJob : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("jobCode", departmentJob.getJobCode());
            map.put("jobName", departmentJob.getJobName());
//            map.put("jobType", departmentJob.getJobType());
//            map.put("jobTypeDictValue", departmentJob.getJobType()==0?"":dictUtil.getDictValue(DictUtil.DictCode.JOB_TYPE, departmentJob.getJobType()));
//            map.put("jobLevel", departmentJob.getJobLevel());
//            map.put("jobLevelDictValue", departmentJob.getJobLevel()==0?"":dictUtil.getDictValue(DictUtil.DictCode.JOB_LEVEL, departmentJob.getJobLevel()));
            listMaps.add(map);
        }
        return R.data(listMaps);
    }

    /**
     * 选择岗位名称 带出级别 类型 等- 下拉框
     *
     * @param jobCode
     * @return
     */
    @GetMapping("/getJobs")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "部门带出岗位", notes = "下拉框")
    public R<Map<String, Object>> getJobs(@RequestParam("jobCode") String jobCode, BladeUser bladeUser) {
        Map<String,Object> map = new HashMap<>();
        DepartmentJob departmentJob = departmentJobService.getOne(Condition.getQueryWrapper(new DepartmentJob())
            .lambda().eq(DepartmentJob::getJobCode , jobCode)
                     .eq(DepartmentJob::getCpCode, bladeUser.getDeptId()));
        map.put("jobType", departmentJob.getJobType());
        map.put("jobTypeDictValue", dictUtil.getDictValue(DictUtil.DictCode.JOB_TYPE, departmentJob.getJobType()));
        map.put("jobLevel", departmentJob.getJobLevel());
        map.put("jobLevelDictValue", dictUtil.getDictValue(DictUtil.DictCode.JOB_LEVEL, departmentJob.getJobLevel()));
        DepartmentInfo departmentInfo = departmentInfoService.getOne(Condition.getQueryWrapper(new DepartmentInfo()).lambda()
                .eq(DepartmentInfo::getDpmentCode, departmentJob.getDpmentCode()));
        map.put("businessModel", departmentInfo.getBusinessModel());
        map.put("businessModelDictValue", dictUtil.getDictValue(DictUtil.DictCode.BUSINESS_MODEL, departmentInfo.getBusinessModel()));
        map.put("dpmentCode", departmentJob.getDpmentCode());
        map.put("dpmentName", departmentInfo.getDpmentName());
        return R.data(map);
    }

    /**
     * 选择部门带出经营模式 崗位
     *
     * @param dpmentCode
     * @return
     */
    @GetMapping("/getBMAndJob")
    @ApiOperationSupport(order = 1)
    @ApiOperation(value = "部门带出岗位", notes = "下拉框")
    public R<Map<String, Object>> getBMAndJob(@RequestParam("dpmentCode") String dpmentCode, BladeUser bladeUser) {
        List<Map<String, Object>> listMaps = new ArrayList<>();
        List<DepartmentJob> list = departmentJobService.list(Condition.getQueryWrapper(new DepartmentJob()).lambda()
                .eq(StringUtils.isNotEmpty(dpmentCode), DepartmentJob::getDpmentCode, dpmentCode)
                .eq(DepartmentJob::getCpCode, bladeUser.getDeptId())
                .groupBy(DepartmentJob::getJobName));
        for (DepartmentJob departmentJob : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("jobCode", departmentJob.getJobCode());
            map.put("jobName", departmentJob.getJobName());
            listMaps.add(map);
        }
        Map<String, Object> mapBm = new HashMap<>();
        DepartmentInfo departmentInfo = departmentInfoService.getOne(Condition.getQueryWrapper(new DepartmentInfo()).lambda()
                .eq(DepartmentInfo::getDpmentCode, dpmentCode)
                .eq(DepartmentInfo::getCpCode, bladeUser.getDeptId()));
        mapBm.put("businessModel", departmentInfo.getBusinessModel());
        mapBm.put("businessModelDictValue", dictUtil.getDictValue(DictUtil.DictCode.BUSINESS_MODEL, departmentInfo.getBusinessModel()));
        mapBm.put("job", listMaps);
        return R.data(mapBm);

    }
}