package com.yundagalaxy.management.vo;

import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import com.yundagalaxy.management.entity.EmployeeDetailInfo;
import com.yundagalaxy.management.entity.EmployeeSalaryInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 一段话简述功能。
 * <p>
 * Created by MiaoYuanMeng on 2019/11/6.
 */
@Data
public class EmployeeInfoAggregationVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "employeeBasicInfo实体")
    private EmployeeBasicInfo employeeBasicInfo;

    @ApiModelProperty(value = "employeeDetailInfo实体")
    private EmployeeDetailInfo employeeDetailInfo;

    @ApiModelProperty(value = "employeeSalaryInfo实体")
    private EmployeeSalaryInfo employeeSalaryInfo;

}
