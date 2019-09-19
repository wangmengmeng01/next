package com.yunda.base.common.bo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.yunda.base.common.enums.ChannelEnum;
import com.yunda.ydmbspringbootstarter.common.annotation.ValidEnum;
import com.yunda.ydmbspringbootstarter.common.annotation.ValidSameAB;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "测试用BO")
@ValidSameAB(field = "password", verifyField = "confirmPassword", message = "两次输入密码不一致")
public class Bo_Test implements Bo_Interface {
	@ApiModelProperty(value = "cron表达式")
	@NotNull()
	@ValidEnum(enumClass = ChannelEnum.class)
	private String cron;

	@NotEmpty(message = "不能为空")
	@Length(min = 6, message = "不能少于{min}个字符")
	private String password;

	private String confirmPassword;

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

}
