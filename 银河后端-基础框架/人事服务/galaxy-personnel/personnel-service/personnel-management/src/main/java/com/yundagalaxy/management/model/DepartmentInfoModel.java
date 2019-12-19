package com.yundagalaxy.management.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;
@Data
public class DepartmentInfoModel extends BaseRowModel {
    /**
     * 部门名称
     */
    @ExcelProperty(value = "部门名称", index = 0)
    private String dpmentName;
    /**
     * 上级部门编号
     */
    @ExcelProperty(value = "上级部门编号", index = 1)
    private String parentDpmentCode;
    /**
     * 上级部门
     */
    @ExcelProperty(value = "上级部门", index = 2)
    private String parentDpmentName;
    /**
     * 经营模式
     */
    @ExcelProperty(value = "经营模式", index = 3)
    private String businessModelName;
    /**
     * 部门负责人（员工工号）
     */
    @ExcelProperty(value = "部门负责人（员工工号）", index = 4)
    private String empCode;

}
