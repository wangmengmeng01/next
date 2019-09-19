/*
 * Filename	CRM_constants.java
 * Company	上海东普信息科技有限公司。
 * @author	zhangFeng
 * @version	1.0.0
 */
package com.yunda.base.feiniao.report.core;

import java.util.HashSet;
import java.util.Set;

import com.yunda.base.system.config.SysConfig;

/**
 * 常量配置。
 * 
 * @author bxl
 * @since 1.0.0_07.20, 2018
 */
public class CRM_constants {
	public static final String CACHE_IN_LOCAL = "CACHE_IN_LOCAL";
	public static final String CACHE_IN_REDIS = "CRMKHGL_CACHE_IN_REDIS";
	public static final String TEMP_IN_REDIS = "TEMP_IN_REDIS";
	public static final String CAINIAO_IN_REDIS = "CAINIAO_IN_REDIS";
	public static final String ERWEIMA_IN_REDIS = "ERWEIMA_IN_REDIS";
	public static final String INDEX_IN_REDIS = "INDEX_IN_REDIS";
	public static final String REPORT_CUST_IN_REDIS = "REPORT_CUST_IN_REDIS";//总部对接客户表
	public static final String TEMP_HOUNIAO_IN_REDIS = "TEMP_HOUNIAO_IN_REDIS";
	public static final String AUTH_USER = "AUTH_USER";
	
	public static String STATUS = "1";//用户启用状态
	public static final Set<String> USER_ROLE = new HashSet<String>();
	public static String ZB = "zongbu";//总部权限

	public static String DQ = "daqu";//大区角色

	public static String SHENG = "sheng";//省总角色

	public static String WD = "wangdian";//网点角色

	static {
		USER_ROLE.add(SysConfig.ROLE_ADMIN_id);
		USER_ROLE.add(SysConfig.ROLE_ZB_id);
		USER_ROLE.add(SysConfig.ROLE_KFZG_id);
		USER_ROLE.add(SysConfig.ROLE_KF_id);
	}

}
