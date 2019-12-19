package com.yundagalaxy.management.vo;

import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 一段话简述功能。
 * <p>
 * Created by MiaoYuanMeng on 2019/10/30.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExcelEmployeeInfoVO对象", description = "员工信息表")
public class ExcelEmployeeInfoVO extends EmployeeBasicInfo {
    private static final long serialVersionUID = 1L;

    /**
     * 网点名称
     */
    @ApiModelProperty(value = "网点名称")
    private String cpName;

    /**
     * 1：直营和2：承包
     */
    @ApiModelProperty(value = "1：直营和2：承包")
    private Integer businessModel;

    private String businessModelDictValue;

    /**
     * 人事行政后勤、业务部、操作部、车队、财务部、客服部、经理室
     */
    @ApiModelProperty(value = "人事行政后勤、业务部、操作部、车队、财务部、客服部、经理室")
    private String dpmentName;

    /**
     * 岗位名称
     */
    @ApiModelProperty(value = "岗位名称")
    private String jobName;

    /**
     * 1级员工、2级员工、3级主管、4级副经理、5级经理
     */
    @ApiModelProperty(value = "1级员工、2级员工、3级主管、4级副经理、5级经理")
    private Integer jobLevel;

    private String jobLevelDictValue;

    /**
     * 法人、经理及法人、经理、客服、财务、司机、操作员、业务员、
     人事行政后勤类
     */
    @ApiModelProperty(value = "法人、经理及法人、经理、客服、财务、司机、操作员、业务员、 人事行政后勤类")
    private Integer jobType;

    private String jobTypeDictValue;

    /**
     * 婚姻状态（未婚、已婚、丧偶、离婚、其他）
     */
    @ApiModelProperty(value = "婚姻状态（未婚、已婚、丧偶、离婚、其他）")
    private Integer maritalStatus;

    private String maritalStatusDictValue;

    /**
     * 户口性质(本地城镇、本地非城镇、外地城镇、外地非城镇)
     */
    @ApiModelProperty(value = "户口性质(本地城镇、本地非城镇、外地城镇、外地非城镇)")
    private Integer householdType;

    private String householdTypeDictValue;

    /**
     * 户籍详细地址
     */
    @ApiModelProperty(value = "户籍详细地址")
    private String householdStreet;
    /**
     * 现详细地址
     */
    @ApiModelProperty(value = "户籍详细地址")
    private String currentStreet;
    /**
     * 紧急联系人
     */
    @ApiModelProperty(value = "紧急联系人")
    private String emgContact;
    /**
     * 紧急联系人关系
     */
    @ApiModelProperty(value = "紧急联系人关系")
    private String emgContactRlp;
    /**
     * 紧急联系人电话
     */
    @ApiModelProperty(value = "紧急联系人电话")
    private String emgContactMobile;

    private String sexDictValue;

    private String workingStateDictValue;
}
