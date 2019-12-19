package com.yundagalaxy.management.feign;

import com.yundagalaxy.management.entity.DepartmentInfo;
import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import com.yundagalaxy.management.service.IDepartmentInfoService;
import com.yundagalaxy.management.service.IEmployeeBasicInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限系统查询人事中心。
 * <p>
 * Created by MiaoYuanMeng on 2019/10/26.
 */
@Slf4j
@RestController
@RequestMapping("/management")
@AllArgsConstructor
public class ManagementClient{

    @Autowired
    private IEmployeeBasicInfoService employeeBasicInfoService;

    @Autowired
    private IDepartmentInfoService departmentInfoService;

    /**
     * Galaxy权限系统调用人事中心网点下员工信息 - 网点
     * @warning  - 未经许可 不要轻易改动
     * @param deptId
     * @return
     */
    @GetMapping("/orgPerson")
    public R<List<EmployeeBasicInfo>> orgPerson(String deptId) {
        List<EmployeeBasicInfo> list = employeeBasicInfoService.list(Condition.getQueryWrapper(new EmployeeBasicInfo()).lambda()
                .eq(EmployeeBasicInfo::getCpCode, deptId)
                .ne(EmployeeBasicInfo::getWorkingState, 2));
        return R.data(list);
    }

    /**
     * Galaxy权限系统调用人事中心网点下员工信息 - 根据IDs
     * @param ids
     * @param deptId
     * @return
     */
    @GetMapping("/getUserListByIds")
    public R<List<EmployeeBasicInfo>> getUserListByIds(String ids, String deptId) {
        List<EmployeeBasicInfo> list = employeeBasicInfoService.list(Condition.getQueryWrapper(new EmployeeBasicInfo()).lambda().
                in(StringUtils.isNotEmpty(ids),EmployeeBasicInfo::getSoaCode, Func.toIntArray(ids))
                .eq(StringUtils.isNotEmpty(deptId),EmployeeBasicInfo::getCpCode, deptId));
        return R.data(list);
    }

    /**
     * Galaxy权限系统调用人事中心网点下员工信息 - 根据name
     * @param name
     * @param deptId
     * @return
     */
    @GetMapping("/getUserListByName")
    public R<List<EmployeeBasicInfo>> getUserListByName(String name, String deptId) {
        List<EmployeeBasicInfo> list = employeeBasicInfoService.list(Condition.getQueryWrapper(new EmployeeBasicInfo()).lambda().eq(EmployeeBasicInfo::getName, name)
                .eq(EmployeeBasicInfo::getCpCode, deptId));
        return R.data(list);
    }

    /**
     * Galaxy权限系统调用人事中心网点下员工信息 - 根据soaCode
     * @param soaCode soa工号/业务员工号
     * @return
     */
    @GetMapping("/getUserBySoaCode")
    public R<Map<String,Object>> getUserBySoaCode(String soaCode) {
        Map<String,Object> map = new HashMap<>();
        EmployeeBasicInfo employeeBasicInfo = employeeBasicInfoService.getOne(Condition.getQueryWrapper(new EmployeeBasicInfo())
            .lambda().eq(EmployeeBasicInfo::getSoaCode, soaCode));
        if (employeeBasicInfo !=null){
            DepartmentInfo departmentInfo =  departmentInfoService.getOne(Condition.getQueryWrapper(new DepartmentInfo()).lambda().eq(DepartmentInfo::getDpmentCode ,employeeBasicInfo.getDpmentCode()));
            map.put("cpName", departmentInfoService.getYdserverCpName(employeeBasicInfo.getCpCode()));
            map.put("empCode", employeeBasicInfo.getEmpCode());
            map.put("name", employeeBasicInfo.getName());
            map.put("dpmentCode", employeeBasicInfo.getDpmentCode());
            map.put("dpmentName", departmentInfo.getDpmentName());
        }
        return R.data(map);
    }


}
