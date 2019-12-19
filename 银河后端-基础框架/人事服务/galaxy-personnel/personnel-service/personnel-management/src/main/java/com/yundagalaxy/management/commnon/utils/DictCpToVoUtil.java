package com.yundagalaxy.management.commnon.utils;

import com.yundagalaxy.management.entity.*;
import com.yundagalaxy.management.service.IDepartmentInfoService;
import com.yundagalaxy.management.service.IDepartmentJobService;
import com.yundagalaxy.management.vo.EmployeeBasicInfoVO;
import com.yundagalaxy.management.vo.EmployeeDetailInfoVO;
import com.yundagalaxy.management.vo.EmployeeSalaryInfoVO;
import com.yundagalaxy.management.wrapper.EmployeeBasicInfoWrapper;
import com.yundagalaxy.management.wrapper.EmployeeDetailInfoWrapper;
import com.yundagalaxy.management.wrapper.EmployeeSalaryInfoWrapper;
import org.springblade.core.mp.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;


/**
 * 一段话简述功能。
 * <p>
 * Created by MiaoYuanMeng on 2019/11/7.
 */
@Component
public class DictCpToVoUtil {

    @Autowired
    private DictUtil dictUtil;

    @Autowired
    private IDepartmentInfoService departmentInfoService;

    @Autowired
    private IDepartmentJobService departmentJobService;

    /**
     * 人事中心 - 基本信息字典视图信息补全
     * @param employeeBasicInfo
     * @return
     */
    public EmployeeBasicInfoVO entityVO(EmployeeBasicInfo employeeBasicInfo) {
        if (employeeBasicInfo == null) {
            return null;
        }
        EmployeeBasicInfoVO employeeBasicInfoVO = EmployeeBasicInfoWrapper.build().entityVO(employeeBasicInfo);
        //數據字典补全
        employeeBasicInfoVO.setSexDictValue(employeeBasicInfoVO.getSex() == 0?"":dictUtil.getDictValue(DictUtil.DictCode.SEX, employeeBasicInfoVO.getSex()));
        employeeBasicInfoVO.setIdTypeDictValue(employeeBasicInfoVO.getIdType() == 0?"":dictUtil.getDictValue(DictUtil.DictCode.ID_TYPE, employeeBasicInfoVO.getIdType()));
        employeeBasicInfoVO.setNationDictValue(employeeBasicInfoVO.getNation()==0?"":dictUtil.getDictValue(DictUtil.DictCode.NATION, employeeBasicInfoVO.getNation()));
        employeeBasicInfoVO.setEducationDictValue(employeeBasicInfoVO.getEducation()==0?"":dictUtil.getDictValue(DictUtil.DictCode.EDUCATION, employeeBasicInfoVO.getEducation()));
        employeeBasicInfoVO.setPoliticsStatusDictValue(employeeBasicInfoVO.getPoliticsStatus()==0?"":dictUtil.getDictValue(DictUtil.DictCode.POLITICS_STATUS, employeeBasicInfoVO.getPoliticsStatus()));
        employeeBasicInfoVO.setWorkingStateDictValue(dictUtil.getDictValue(DictUtil.DictCode.WORKING_STATE, employeeBasicInfoVO.getWorkingState()));
        LocalDate today = LocalDate.now();
        Period pe = Period.between(employeeBasicInfoVO.getHiredate(), today);
        employeeBasicInfoVO.setWorkYear(pe.getYears());
        employeeBasicInfoVO.setWorkMouth(pe.getMonths());
        DepartmentInfo departmentInfo =  departmentInfoService.getOne(Condition.getQueryWrapper(new DepartmentInfo()).lambda().eq(DepartmentInfo::getDpmentCode ,employeeBasicInfoVO.getDpmentCode()));
        if (departmentInfo !=null){
            employeeBasicInfoVO.setDpmentName(departmentInfo.getDpmentName());
            employeeBasicInfoVO.setBusinessModel(departmentInfo.getBusinessModel());
            employeeBasicInfoVO.setBusinessModelDictValue(departmentInfo.getBusinessModel() == 0?"":dictUtil.getDictValue(DictUtil.DictCode.BUSINESS_MODEL, departmentInfo.getBusinessModel()));
        }
        DepartmentJob departmentJob = departmentJobService.getOne(Condition.getQueryWrapper(new DepartmentJob()).lambda().eq(DepartmentJob::getJobCode, employeeBasicInfoVO.getJobCode()));
        if (departmentJob!=null){
            employeeBasicInfoVO.setJobName(departmentJob.getJobName());
            employeeBasicInfoVO.setJobTypeDictValue(departmentJob.getJobType()==0?"":dictUtil.getDictValue(DictUtil.DictCode.JOB_TYPE, departmentJob.getJobType()));
            employeeBasicInfoVO.setJobType(departmentJob.getJobType());
            employeeBasicInfoVO.setJobLevel(departmentJob.getJobLevel());
            employeeBasicInfoVO.setJobLevelDictValue(departmentJob.getJobLevel()==0?"":dictUtil.getDictValue(DictUtil.DictCode.JOB_LEVEL, departmentJob.getJobLevel()));
        }
        return employeeBasicInfoVO;
    }

    /**
     * 人事中心 - 详细信息字典视图信息补全
     * @param employeeDetailInfo
     * @return
     */
    public EmployeeDetailInfoVO entityVO(EmployeeDetailInfo employeeDetailInfo) {
        if (employeeDetailInfo == null) {
            return null;
        }
        EmployeeDetailInfoVO employeeDetailInfoVO = EmployeeDetailInfoWrapper.build().entityVO(employeeDetailInfo);
        employeeDetailInfoVO.setMaritalStatusDictValue(employeeDetailInfoVO.getMaritalStatus()==0?"":dictUtil.getDictValue(DictUtil.DictCode.MARITAL_STATUS, employeeDetailInfoVO.getMaritalStatus()));
        employeeDetailInfoVO.setHouseholdTypeDictValue(employeeDetailInfoVO.getHouseholdType()==0?"":dictUtil.getDictValue(DictUtil.DictCode.HOUSEHOLD_TYPE, employeeDetailInfoVO.getHouseholdType()));
        employeeDetailInfoVO.setExpCcieLevelDictValue((employeeDetailInfoVO.getExpCcieLevel()==null || employeeDetailInfoVO.getExpCcieLevel()==0)?"":dictUtil.getDictValue(DictUtil.DictCode.EXP_CCIE_LEVEL, employeeDetailInfoVO.getExpCcieLevel()));
        return employeeDetailInfoVO;
    }

    /**
     * 人事中心 - 薪资信息字典视图信息补全
     * @param employeeSalaryInfo
     * @return
     */
    public EmployeeSalaryInfoVO entityVO(EmployeeSalaryInfo employeeSalaryInfo) {
        if (employeeSalaryInfo == null) {
            return null;
        }
        EmployeeSalaryInfoVO employeeSalaryInfoVO = EmployeeSalaryInfoWrapper.build().entityVO(employeeSalaryInfo);
        employeeSalaryInfoVO.setSalarytypeDictValue(employeeSalaryInfoVO.getSalaryType()==0?"":dictUtil.getDictValue(DictUtil.DictCode.SALARY_TYPE, employeeSalaryInfoVO.getSalaryType()));
        employeeSalaryInfoVO.setPayoffModelDictValue(employeeSalaryInfoVO.getPayoffModel()==0?"":dictUtil.getDictValue(DictUtil.DictCode.PAYOFF_MODEL, employeeSalaryInfoVO.getPayoffModel()));
        employeeSalaryInfoVO.setSocialInsurDictValue(employeeSalaryInfoVO.getSocialInsur()==0?"":dictUtil.getDictValue(DictUtil.DictCode.SOCIAL_INSUR, employeeSalaryInfoVO.getSocialInsur()));
        employeeSalaryInfoVO.setAccumFundDictValue(employeeSalaryInfoVO.getAccumFund()==0?"":dictUtil.getDictValue(DictUtil.DictCode.ACCUM_FUND, employeeSalaryInfoVO.getAccumFund()));
        employeeSalaryInfoVO.setTaxDictValue(employeeSalaryInfoVO.getTax()==0?"":dictUtil.getDictValue(DictUtil.DictCode.TAX, employeeSalaryInfoVO.getTax()));
        return employeeSalaryInfoVO;
    }
}
