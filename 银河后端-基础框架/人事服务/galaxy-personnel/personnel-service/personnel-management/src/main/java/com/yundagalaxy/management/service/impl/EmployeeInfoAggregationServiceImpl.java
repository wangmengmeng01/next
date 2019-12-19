package com.yundagalaxy.management.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yundagalaxy.management.commnon.utils.WebUtils;
import com.yundagalaxy.management.dto.SoaEmpDTO;
import com.yundagalaxy.management.entity.DepartmentJob;
import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import com.yundagalaxy.management.entity.EmployeeDetailInfo;
import com.yundagalaxy.management.entity.EmployeeSalaryInfo;
import com.yundagalaxy.management.mapper.EmployeeInfoAggregationMapper;
import com.yundagalaxy.management.service.*;
import com.yundagalaxy.management.vo.ExcelEmployeeInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springblade.core.log.logger.BladeLogger;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 聚合员工信息。
 * <p>
 * Created by MiaoYuanMeng on 2019/10/18.
 */
@Service
public class EmployeeInfoAggregationServiceImpl implements IEmployeeInfoAggregationService {

    @Autowired
    private IEmployeeBasicInfoService employeeBasicInfoService;

    @Autowired
    private IEmployeeDetailInfoService employeeDetailInfoService;

    @Autowired
    private IEmployeeSalaryInfoService employeeSalaryInfoService;

    @Autowired
    private EmployeeInfoAggregationMapper employeeInfoAggregationMapper;

    @Autowired
    private IDepartmentJobService departmentJobService;

    @Autowired
    private BladeLogger bladeLogger;

    @Value("${yd.soa.add.url}")
    private String addUrl;

    @Value("${yd.soa.update.url}")
    private String updateUrl;

    /**
     * 聚合插入员工信息
     * @param employeeBasicInfo
     * @param employeeDetailInfo
     * @param employeeSalaryInfo
     * @return
     */
    @Override
    public boolean save(EmployeeBasicInfo employeeBasicInfo, EmployeeDetailInfo employeeDetailInfo, EmployeeSalaryInfo employeeSalaryInfo) {
        if (employeeBasicInfoService.save(employeeBasicInfo) && employeeDetailInfoService.save(employeeDetailInfo)
                && employeeSalaryInfoService.save(employeeSalaryInfo)){
            return true;
        }
        return false;
    }

    @Override
    public List<ExcelEmployeeInfoVO> empList(Map<String, Object> params, Query query) {
        return employeeInfoAggregationMapper.empList(params,query);
    }

    /**
     * 添加到SOA
     * @param employeeBasicInfo
     * @return
     */
    @Override
    public R addToSoa(EmployeeBasicInfo employeeBasicInfo, BladeUser userfo) {
        JSONObject Data = new JSONObject();
        JSONArray totalData = new JSONArray();
        JSONObject params = new JSONObject();
//        Map<String,String> params = new HashMap<>();
        String sign = Func.md5Hex(
                "@"+userfo.getDeptId()
                        +"@"+employeeBasicInfo.getIdCode()
                        +"@"+employeeBasicInfo.getPhoneNo()
                        +"@soa&sz2019");
        params.put("SIGN", sign);
        params.put("WCODE", userfo.getDeptId());
        params.put("GENDER", employeeBasicInfo.getSex()==1?"m":"f");
        params.put("PHONENO", employeeBasicInfo.getPhoneNo());
        params.put("EMPNAME", employeeBasicInfo.getName());
        params.put("CARDNO", employeeBasicInfo.getIdCode());
        //岗位
        DepartmentJob departmentJob = departmentJobService.getOne(Condition.getQueryWrapper(new DepartmentJob()).lambda().eq(DepartmentJob::getJobCode, employeeBasicInfo.getJobCode()));
        params.put("POST", departmentJob.getJobType());
        params.put("STATUS", employeeBasicInfo.getWorkingState()==0?"10"
                :(employeeBasicInfo.getWorkingState()==1?"11"
                :(employeeBasicInfo.getWorkingState()==2?"15"
                :(employeeBasicInfo.getWorkingState()==3?"18":"0"))));
        params.put("SERIALNUM", String.valueOf(System.currentTimeMillis()));
        String data="";
        JSONObject result = new JSONObject();
        JSONObject resultData = new JSONObject();
        try {
            totalData.add(params);
            Data.put("totalData", totalData);
//            data = OkHttpUtil.postJson(addUrl, Data.toString());
            data = WebUtils.doPostBodyWithHeader(addUrl, Data.toString(), 3000, 5000, null);
            if (StringUtils.isEmpty(data)){
                return R.data(1,"操作失败！(NULL)","操作失败！(NULL)");
            }
            result = JSONArray.parseObject(data);
            if (result!=null){
                resultData = result.getJSONArray("result").getJSONObject(0);
            }
        } catch (IOException e) {
            bladeLogger.error("添加员工", "请求SOA超时！");
            return R.data(1,"操作失败！(IOException)","操作失败！(IOException)");
        }catch (Exception e){
            bladeLogger.error("添加员工",e.getMessage());
            return R.data(1,"操作失败！(Exception)","操作失败！(Exception)");
        }
        String CODE = resultData.getString("CODE");
        if ("0".equals(CODE)){
            return R.data(0,resultData.getString("EMPID"),"成功！");
        }else{
            return R.data(1
                    ,"E03".equals(resultData.getString("MSG"))?"手机号重复！(E03)":"操作失败！("+resultData.getString("MSG")+")"
                    ,"E03".equals(resultData.getString("MSG"))?"手机号重复！(E03)":"操作失败！("+resultData.getString("MSG")+")");
        }
    }

    /**
     *      * E01：工号不存在！
     *      * E02：网点不存在！
     *      * E03：手机号重复！
     *      * E04：岗位异常！
     *      * E05：状态异常！
     *      * E06：签名验证失败！
     *      * E07：单次请求限制100条！
     *      * E08：签名为空！
     *      * E09：网点编码为空！
     *      * E10：员工工号为空！
     *      * E11：性别为空！
     *      * E12：手机号码为空！
     *      * E13：密码为空！
     *      * E14：姓名为空！
     *      * E15：身份证号为空！
     *      * E16：岗位为空！
     *      * E17：人员状态为空！
     *      * E18：SOA插入数据失败！
     *      * E19：流水号为空！
     */

    /**
     * 更新到SOA
     * @param soaEmpDTO
     * @param userfo
     * @return
     */
    @Override
    public R updateToSoa(SoaEmpDTO soaEmpDTO, BladeUser userfo) {
        JSONObject Data = new JSONObject();
        JSONArray totalData = new JSONArray();
        JSONObject params =  soaEmpDTO.getSoaParms();
        String data="";
        JSONObject result = new JSONObject();
        JSONObject resultData = new JSONObject();
        totalData.add(params);
        Data.put("totalData", totalData);
        try {
//            data = OkHttpUtil.postJson(updateUrl, Data.toString());
            data = WebUtils.doPostBodyWithHeader(updateUrl, Data.toString(), 3000, 5000, null);
            if (StringUtils.isEmpty(data)){
                return R.data(1,"操作失败！(NULL)","操作失败！(NULL)");
            }
            result = JSONArray.parseObject(data);
            if (result!=null){
                resultData = result.getJSONArray("result").getJSONObject(0);
            }
        } catch (IOException e) {
            bladeLogger.error("addToSoa", "请求SOA超时！");
            return R.data(1,"操作失败！(IOException)","操作失败！(IOException)");
        }catch (Exception e){
            bladeLogger.error("addToSoa",e.getMessage());
            return R.data(1,"操作失败！(Exception)","操作失败！(Exception)");
        }
        String CODE = resultData.getString("CODE");
        if ("0".equals(CODE)){
            return R.data(0,resultData.getString("EMPID"),"成功！");
        }else{
            return R.data(1
                    ,"E03".equals(resultData.getString("MSG"))?"手机号重复！(E03)":"操作失败！("+resultData.getString("MSG")+")"
                    ,"E03".equals(resultData.getString("MSG"))?"手机号重复！(E03)":"操作失败！("+resultData.getString("MSG")+")");
        }
    }

//
//    /**
//     * SOA添加用户状态码
//     * @param msg
//     * @return
//     */
//    public String getMsg(String msg){
//        return   "E01".equals(msg)?"工号不存在！"
//                :("E02".equals(msg)?"网点不存在！"
//                :("E03".equals(msg)?"手机号重复！"
//                :("E04".equals(msg)?"岗位异常！"
//                :("E05".equals(msg)?"状态异常！"
//                :("E06".equals(msg)?"签名验证失败！"
//                :("E07".equals(msg)?"单次请求限制100条！"
//                :("E08".equals(msg)?"签名为空！"
//                :("E09".equals(msg)?"网点编码为空！"
//                :("E10".equals(msg)?"员工工号为空！"
//                :("E11".equals(msg)?"性别为空！"
//                :("E12".equals(msg)?"手机号码为空！"
//                :("E13".equals(msg)?"密码为空！"
//                :("E14".equals(msg)?"姓名为空！"
//                :("E15".equals(msg)?"身份证号为空！"
//                :("E16".equals(msg)?"岗位为空！"
//                :("E17".equals(msg)?"人员状态为空！"
//                :("E18".equals(msg)?"SOA插入数据失败！"
//                :("E19".equals(msg)?"流水号为空！"
//                :msg))))))))))))))))));
//    }
}
