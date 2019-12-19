package com.yundagalaxy.management.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 一段话简述功能。
 * <p>
 * Created by MiaoYuanMeng on 2019/10/18.
 */
@Data
@ApiModel(value = "EmployeeInfoAggregationInfo对象", description = "员工信息表")
public class EmployeeInfoAggregationInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private EmployeeBasicInfo employeeBasicInfo;


}

