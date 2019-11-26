package com.yunda.base.common.utils;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @Author bootdo 1992lcg@163.com
 */
@Data
public class ImportUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	private int code;
	private String message;
	private List<?> errorList;

	public ImportUtils() {
	}

	public ImportUtils(int code,String message,List<?> list) {
		this.errorList = list;
		this.code = code;
		this.message = message;
	}



}