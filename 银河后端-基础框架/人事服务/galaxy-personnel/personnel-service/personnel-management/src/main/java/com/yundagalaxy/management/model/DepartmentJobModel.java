package com.yundagalaxy.management.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DepartmentJobModel extends BaseRowModel {
    /**
     * 岗位名称
     */
    @ExcelProperty(value = "岗位名称", index = 0)
    private String jobName;
    /**
     * 岗位类型
     */
    @ExcelProperty(value = "岗位类型", index = 1)
    private String jobTypeValue;
    /**
     * 岗位级别
     */
    @ExcelProperty(value = "岗位级别", index = 2)
    private String jobLevelValue;
    /**
     * 部门编号
     */
    @ExcelProperty(value = "部门编号", index = 3)
    private String dpmentCode;
    /**
     * 所属部门
     */
    @ExcelProperty(value = "所属部门", index = 4)
    private String dpmentName;

}
