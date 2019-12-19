package com.yundagalaxy.management.wrapper;

import com.yundagalaxy.management.entity.EmployeeSalaryInfo;
import com.yundagalaxy.management.vo.EmployeeSalaryInfoVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 一段话简述功能。
 * <p>
 * Created by MiaoYuanMeng on 2019/11/7.
 */
public class EmployeeSalaryInfoWrapper  extends BaseEntityWrapper<EmployeeSalaryInfo, EmployeeSalaryInfoVO> {

    public static EmployeeSalaryInfoWrapper build() {
        return new EmployeeSalaryInfoWrapper();
    }

    @Override
    public EmployeeSalaryInfoVO entityVO(EmployeeSalaryInfo employeeSalaryInfo) {
        EmployeeSalaryInfoVO employeeDetailInfoVO = BeanUtil.copy(employeeSalaryInfo, EmployeeSalaryInfoVO.class);
        assert employeeDetailInfoVO != null;
        return employeeDetailInfoVO;
    }
}
