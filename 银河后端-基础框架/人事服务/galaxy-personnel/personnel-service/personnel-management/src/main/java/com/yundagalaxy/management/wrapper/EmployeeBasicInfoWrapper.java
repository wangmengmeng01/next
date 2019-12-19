package com.yundagalaxy.management.wrapper;

import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import com.yundagalaxy.management.vo.EmployeeBasicInfoVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 一段话简述功能。
 * <p>
 * Created by MiaoYuanMeng on 2019/11/7.
 */
public class EmployeeBasicInfoWrapper extends BaseEntityWrapper<EmployeeBasicInfo, EmployeeBasicInfoVO> {

    public static EmployeeBasicInfoWrapper build() {
        return new EmployeeBasicInfoWrapper();
    }

    @Override
    public EmployeeBasicInfoVO entityVO(EmployeeBasicInfo employeeBasicInfo) {
        EmployeeBasicInfoVO employeeBasicInfoVO = BeanUtil.copy(employeeBasicInfo, EmployeeBasicInfoVO.class);
        assert employeeBasicInfoVO != null;
        return employeeBasicInfoVO;
    }
}
