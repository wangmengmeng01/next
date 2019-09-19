package com.yunda.base.system.domain;

import com.github.crab2died.annotation.ExcelField;
import com.yunda.ydmbspringbootstarter.common.annotation.ValidCertifId;
import com.yunda.ydmbspringbootstarter.common.annotation.ValidMac;
import com.yunda.ydmbspringbootstarter.common.annotation.ValidMobile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Author: pyj
 * @Date: 2019/1/25 10:53
 */
@ApiModel(value = "用户维护表")
public class LoginUserDO {
    private static final long serialVersionUID = 1L;
    @ExcelField(title = "序列号", order = 1)
    private Long userId;
    // 用户名
    @ExcelField(title = "登录账号", order = 2)
    @ApiModelProperty(value = "账号")
    @NotBlank(message = "账号不能为空")
    private String username;
    // 用户真实姓名
    @ExcelField(title = "登录人姓名", order = 3)
    @ApiModelProperty(value = "姓名")
    @NotBlank(message = "姓名不能为空")
    private String name;
    @ExcelField(title = "所属角色", order = 4)
    private String role;
    @ExcelField(title = "所属机构",order = 5)
    private String institution;
    // 手机号
    @ExcelField(title = "手机号", order = 7)
    @ApiModelProperty(value = "手机号")
    @ValidMobile
    //@NotBlank(message = "手机号不能为空")
    private String mobile;
    // 状态 0:禁用，1:正常
//    @ExcelField(title = "使用状态", order = 5)
    @ApiModelProperty(value = "使用状态")
    private Integer status;
    // 修改时间
    @ExcelField(title = "修改时间", order = 10)
    @ApiModelProperty(value = "修改时间")
    private String gmtModified;
    //身份证
    @ValidCertifId
    @ApiModelProperty(value = "身份证号",required = true)
    //@NotBlank(message = "身份证不能为空")
    @ExcelField(title = "身份证号", order = 6)
    private String idcdNo;
    private String hiddenIdcdNo;
    private String superiorRole;

    public String getHiddenIdcdNo() {
        return hiddenIdcdNo;
    }

    public void setHiddenIdcdNo(String hiddenIdcdNo) {
        this.hiddenIdcdNo = hiddenIdcdNo;
    }

    //mac地址
    @ExcelField(title = "有线Mac地址", order = 8)
    @ApiModelProperty(value = "mac地址",required = true)
//    @NotBlank(message = "mac地址不能为空")
    @ValidMac
    private String macAdress;
    @ApiModelProperty(value = "无线Mac地址",required = true)
//    @NotBlank(message = "mac地址不能为空")
    @ValidMac
    @ExcelField(title = "无线Mac地址", order = 9)

    private String wirelessMacAdress;
    //修改人
    @ExcelField(title = "修改人", order = 11)
    @ApiModelProperty(value = "修改人",required = true)
    private String updateName;
    @ApiModelProperty(value = "机构编码")
    private String organizationCode;
    @ExcelField(title = "使用状态", order = 12)
    private String showState;
    
    private String faceImage;
    //短信类型
    private String smsType;

    /**
	 * @return the faceImage
	 */
	public String getFaceImage() {
		return faceImage;
	}

	/**
	 * @param faceImage the faceImage to set
	 */
	public void setFaceImage(String faceImage) {
		this.faceImage = faceImage;
	}

	public String getShowState() {
        if (status==1){
            showState="使用中";
        }else{
            showState="已停用";
        }
        return showState;
    }

    public void setShowState(String showState) {
        this.showState = showState;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }



    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getIdcdNo() {
        return idcdNo;
    }

    public void setIdcdNo(String idcdNo) {
        this.idcdNo = idcdNo;
    }

    public String getMacAdress() {
        return macAdress;
    }

    public void setMacAdress(String macAdress) {
        this.macAdress = macAdress;
    }

    public String getWirelessMacAdress() {
        return wirelessMacAdress;
    }

    public void setWirelessMacAdress(String wirelessMacAdress) {
        this.wirelessMacAdress = wirelessMacAdress;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "LoginUserDO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", institution='" + institution + '\'' +
                ", mobile='" + mobile + '\'' +
                ", status=" + status +
                ", gmtModified='" + gmtModified + '\'' +
                ", idcdNo='" + idcdNo + '\'' +
                ", hiddenIdcdNo='" + hiddenIdcdNo + '\'' +
                ", superiorRole='" + superiorRole + '\'' +
                ", macAdress='" + macAdress + '\'' +
                ", wirelessMacAdress='" + wirelessMacAdress + '\'' +
                ", updateName='" + updateName + '\'' +
                ", organizationCode='" + organizationCode + '\'' +
                ", showState='" + showState + '\'' +
                ", faceImage='" + faceImage + '\'' +
                ", smsType='" + smsType + '\'' +
                ", organizationName='" + organizationName + '\'' +
                '}';
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    @ApiModelProperty(value = "机构名称")
    private String organizationName;


    public String getSuperiorRole() {
        return superiorRole;
    }

    public void setSuperiorRole(String superiorRole) {
        this.superiorRole = superiorRole;
    }
}
