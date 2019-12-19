package com.yundagalaxy.management.commnon.utils;

import com.yundagalaxy.system.entity.Dict;
import com.yundagalaxy.system.feign.IDictClient;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.tool.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import static com.yundagalaxy.common.constant.AppConstant.APPLICATION_PERSONNEL_NAME;

/**
 * 一段话简述功能。
 * <p>
 * Created by MiaoYuanMeng on 2019/11/1.
 */
@Component
@Slf4j
public class DictUtil {

    @Autowired
    private IDictClient dictClient;

    public enum DictCode{

        //是否缴纳个税
        TAX("tax"),
        //是否缴纳公积金
        ACCUM_FUND("accum_fund"),
        //是否缴纳社保
        SOCIAL_INSUR("social_insur"),
        //工资发放方式(现金、银行卡、支付宝、预付款)
        PAYOFF_MODEL("payoff_model"),
        //试用期工资折扣方式(岗位工资扣减/比例)
        SALARY_TYPE("salary_type"),
        //快递资格证等级(高级、中级、初级、初中级、中高级、初中高级、无)
        EXP_CCIE_LEVEL("exp_ccie_level"),
        //政治面貌
        POLITICS_STATUS("politics_status"),
        //民族
        NATION("nation"),
        //证件类型
        ID_TYPE("id_type"),
        //性别
        SEX("sex"),
        //岗位类型
        JOB_TYPE("job_type"),
        //岗位级别
        JOB_LEVEL("job_level"),
        //工作状态
        WORKING_STATE("working_state"),
        //婚姻状态
        MARITAL_STATUS("marital_status"),
        //户口性质
        HOUSEHOLD_TYPE("household_type"),
        //学历
        EDUCATION("education"),
        //部门层级
        DPMENT_LEVEL("dpment_level"),
        //经营模式
        BUSINESS_MODEL("business_model"),
        //账号角色
        ACCOUNT_NO("account_no"),
        //角色类型
        ACCOUNT_TYPE("account_type"),
        //是否交接完毕
        HANDOVER("handover")


        ;
        private String code;
        DictCode(String code){
            this.code=code;
        }
        public String getCode(){return code;}
    }

    /**
     * 获取数据字典值
     * @param dictCode
     * @param dictKey
     * @return
     */
    public String getDictValue(DictCode dictCode,Integer dictKey){
        String val = "";
        try{
            val = dictClient.getValueBySubsystemName(dictCode.getCode(), dictKey,APPLICATION_PERSONNEL_NAME).getData();
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return val;
    }

    /**
     * 获取数据字典值
     * @param dictCode
     * @param dictName
     * @return
     */

    public Integer getDictKey(DictCode dictCode,String dictName){
        List<Dict> ls = null;
        Integer dictKey = null;
        try{
            ls = dictClient.getListBySubsystemName(dictCode.getCode(),APPLICATION_PERSONNEL_NAME).getData();
            if(null!=ls&&ls.size()>0){
                for (Dict v:ls){
                    if(dictName.equals(v.getDictValue())){
                        dictKey = v.getDictKey();
                        continue;
                    };
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return dictKey;
    }





    /**
     * 获取字典装配Excel
     * @param dictCode
     * @return
     */
    public String[] getDictValuesToExcelReplace(DictCode dictCode){
        String[] replaces = new String[]{};
        try {
            List<Dict> list = dictClient.getList(dictCode.getCode()).getData();
            int i = 0;
            if (list != null && list.size()>0){
                for (Dict li:list) {
                    replaces[i] = li.getDictValue()+"_"+li.getDictKey();
                    i++;
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return replaces;
    }

    /**
     *
     * @param
     * @param
     * @return
     */
    public List<Dict> getListBySubsystemName(DictCode dictCode){

        List<Dict> ls = new ArrayList<>();
        try{
            ls = dictClient.getListBySubsystemName(dictCode.getCode(),APPLICATION_PERSONNEL_NAME).getData();

        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());

        }
        return ls;
    }
}
