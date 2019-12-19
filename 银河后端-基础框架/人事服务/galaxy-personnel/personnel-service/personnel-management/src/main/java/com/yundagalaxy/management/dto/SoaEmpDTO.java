/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.yundagalaxy.management.dto;

import com.alibaba.fastjson.JSONObject;
import com.yundagalaxy.management.entity.SoaEmp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.tool.utils.Func;

/**
 * soa账号表数据传输对象实体类
 *
 * @author dongfeng
 * @since 2019-11-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SoaEmpDTO extends SoaEmp {
	private static final long serialVersionUID = 1L;

	//自付串状态
	private String StringStatus;

	/**
	 * SOA 人员状态码
	 */
	public enum DictCode {

		//是否缴纳个税
		SY("10","试用"),
		ZS("11","正式"),
		LS("12","临时"),
		SYYQ("13","试用延期"),
		JP("14","解聘"),
		LZ("15","离职"),
		TX("16","退休"),
		WX("17","无效"),
		ZZ("18","在职")
		;
		private String code;
		private String name;
		DictCode(String code,String name){
			this.code=code;
			this.name=name;
		}
		public String getCode(){return code;}
		public String getName(){return name;}
	}

	public JSONObject getSoaParms(){
		JSONObject params = new JSONObject();
		String sign = Func.md5Hex(
				"@"+this.getCpCode()
						+"@"+this.getIdCard()
						+"@"+this.getPhone()
						+"@soa&sz2019");
		params.put("SIGN", sign);
		params.put("EMPID", this.getSoaCode());
		params.put("WCODE", this.getCpCode());
		params.put("GENDER", this.getSex()==1?"m":"f");
		params.put("PHONENO", this.getPhone());
		params.put("EMPNAME", this.getName());
		params.put("CARDNO", this.getIdCard());
		//岗位
		params.put("POST", this.getJob());
		params.put("STATUS", this.getStringStatus());
		params.put("SERIALNUM", String.valueOf(System.currentTimeMillis()));
		return params;
	}
}
