/**   
 * Copyright © 2019 eSunny Info. Tech Ltd. All rights reserved.
 * 
 * @Package: com.yunda.base.common.enums 
 * @author: 22374   
 * @date: 2019年2月22日 下午2:54:46 
 */
package com.yunda.base.common.enums;

/** 
 * @ClassName: resourceSafeEnum 
 * @Description: TODO
 * @author: 22374
 * @date: 2019年2月22日 下午2:54:46  
 */
public enum ResourceSafeEnum {
	TOGE_AUTH("0", "统一授权登录"), MAC("1", "Mac校验"), ID_CARD("2", "输入身份证号码"), MESSAGE("3", "短信验证码 "), FACE("4", "人脸识别"), VIDEO("5", "全程录像");
	private String num;
	private String name;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	ResourceSafeEnum(String num, String name) {
		this.num = num;
		this.name = name;
	}
}
