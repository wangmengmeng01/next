package com.yundagalaxy.management.service;

import com.yundagalaxy.management.dto.SoaEmpDTO;
import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import com.yundagalaxy.management.entity.EmployeeDetailInfo;
import com.yundagalaxy.management.entity.EmployeeSalaryInfo;
import com.yundagalaxy.management.entity.SoaEmp;
import com.yundagalaxy.management.vo.ExcelEmployeeInfoVO;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;

import java.util.List;
import java.util.Map;

/**
 * 一段话简述功能。
 * <p>
 * Created by MiaoYuanMeng on 2019/10/18.
 */
public interface IEmployeeInfoAggregationService {
    boolean save(EmployeeBasicInfo employeeBasicInfo, EmployeeDetailInfo employeeDetailInfo, EmployeeSalaryInfo employeeSalaryInfo);

    List<ExcelEmployeeInfoVO> empList(Map<String, Object> params, Query query);

    R addToSoa(EmployeeBasicInfo employeeBasicIn, BladeUser userfo);

    R updateToSoa(SoaEmpDTO soaEmpDTO, BladeUser userfo);
}
