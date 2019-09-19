package com.yunda.base.system.vo;

import java.io.Serializable;

import com.yunda.base.common.enums.RespEnum;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "封装返回数据")
public final class RspBean<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "状态码", example = "200：表示成功；其他都不成功")
	private int code = 200;
	@ApiModelProperty(value = "错误透传消息", example = "message信息")
	private String message = "";
	@ApiModelProperty(value = "返回数据", example = "请求成功返回的数据类型T", dataType = "T")
	private T data = null;

	@Override
	public String toString() {
		return "code=" + code + ",message=" + message + ",data=" + data;
	}

	public RspBean() {
		this.code = RespEnum.SUCCESS.getCode();
	}

	public RspBean(T data) {
		this.code = RespEnum.SUCCESS.getCode();
		this.data = data;
	}

	public RspBean(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public RspBean(RespEnum res) {
		this.code = res.getCode();
		this.message = res.getMessage();
	}

	// 根据状态码返回
	public RspBean<T> feedback(int code) {
		this.code = code;
		this.message = RespEnum.getMessage(code);
		return this;
	}

	public RspBean<T> failure(int code) {
		return feedback(code);
	}

	public RspBean<T> failureWithData(int code, T data) {
		return feedbackWithData(code, data);
	}

	private RspBean<T> feedbackWithData(int Code, T Data) {
		this.code = Code;
		this.message = RespEnum.getMessage(Code);
		this.data = Data;
		return this;
	}

	public RspBean<T> failure(RespEnum res) {
		return feedback(res.getCode());
	}

	public RspBean<T> success() {
		return feedback(RespEnum.SUCCESS.getCode());
	}

	public int getCode() {
		return code;
	}

	public RspBean<T> setCode(int code) {
		this.code = code;
		return this;
	}

	public RspBean<T> setRespEnum(RespEnum re) {
		this.message = re.getMessage();
		this.code = re.getCode();
		return this;
	}

	public String getMessage() {
		return message;
	}

	public RspBean<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public T getData() {
		return data;
	}

	public RspBean<T> setData(T data) {
		this.data = data;
		return this;
	}
}
