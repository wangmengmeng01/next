<?php

/**
 * data
 * @author auto create
 */
class UserStatusDto
{
	
	/** 
	 * NORMAL(0, "正常"), WARN(1, "警告中"), FREEZE(2, "冻结中"), DO_BLACK(3, "拉黑中");
	 **/
	public $account_status;
	
	/** 
	 * 是否实名认证
	 **/
	public $alipay_auth;
	
	/** 
	 * 是否有手机号
	 **/
	public $have_mobile;
	
	/** 
	 * 实收实人认证
	 **/
	public $real_person_auth;
	
	/** 
	 * 未离职 0 离职中 1 已离职 2
	 **/
	public $resign_status;	
}
?>