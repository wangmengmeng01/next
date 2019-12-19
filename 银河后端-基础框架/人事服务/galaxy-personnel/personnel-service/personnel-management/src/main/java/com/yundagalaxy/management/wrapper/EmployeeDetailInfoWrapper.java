package com.yundagalaxy.management.wrapper;

import com.yundagalaxy.management.entity.EmployeeDetailInfo;
import com.yundagalaxy.management.vo.EmployeeDetailInfoVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

/**
 * 一段话简述功能。
 * <p>
 * Created by MiaoYuanMeng on 2019/11/7.
 */
public class EmployeeDetailInfoWrapper extends BaseEntityWrapper<EmployeeDetailInfo, EmployeeDetailInfoVO> {

    public static EmployeeDetailInfoWrapper build() {
        return new EmployeeDetailInfoWrapper();
    }

    @Override
    public EmployeeDetailInfoVO entityVO(EmployeeDetailInfo employeeDetailInfo) {
        EmployeeDetailInfoVO employeeDetailInfoVO = BeanUtil.copy(employeeDetailInfo, EmployeeDetailInfoVO.class);
        assert employeeDetailInfoVO != null;
        return employeeDetailInfoVO;
    }
}
