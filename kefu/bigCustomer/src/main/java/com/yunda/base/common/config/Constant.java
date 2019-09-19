package com.yunda.base.common.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//【注意】：此处只放常量，不变的。若随环境发生变化的放到SysConfig，SysConfig会通过数据库热加载环境值
public class Constant {
	// 自动去除表前缀
	public static String AUTO_REOMVE_PRE = "true";
	// 停止计划任务
	public static String STATUS_RUNNING_STOP = "stop";
	// 开启计划任务
	public static String STATUS_RUNNING_START = "start";
	// 通知公告阅读状态-未读
	public static String OA_NOTIFY_READ_NO = "0";
	// 通知公告阅读状态-已读
	public static int OA_NOTIFY_READ_YES = 1;
	// 部门根节点id
	public static Long DEPT_ROOT_ID = 0L;
	// 缓存方式
	public static String CACHE_TYPE_REDIS = "redis";
	// 服务器唯一编号，集群环境中认识自己
	public static String SERVER_ID = UUID.randomUUID().toString();
	// 服务器名字缓存中的key
	public static final String SERVER_ID_CACHENAME = "SERVER_ID_CACHENAME_";
	// 服务器失活时间，超过多久没打卡就认为该服务器挂了,单位毫秒
	public static int SERVER_ID_DEADTIME = 90000;
	// 上传文件的临时目录
	public static String UPLOAD_PATH = "d:\\test";
	// 集团合计
	public static String QUERYCOMPANYTOTALINFO = "queryCompanyTotalInfo";
	// 大区合计
	public static String QUERYBIGAREATOTALINFO = "queryBigareaTotalInfo";
	// 省份合计
	public static String QUERYPROVINCETOTALINFO = "queryProvinceTotalInfo";
	// 大区所属省份合计
	public static String QUERYMULTIPROVINCETOTALINFO = "queryMultiProvinceTotalInfo";
	// 城市合计
	public static String QUERYCITYTOTALINFO = "queryCityTotalInfo";
	// 网点合计
	public static String QUERYBRANCHTOTALINFO = "queryBranchTotalInfo";
	// 客户合计
	public static String QUERYCUSTOMERTOTALINFO = "queryCustomerTotalInfo";

	// 缓存头
	public static String SEED = "seed";
	// 预警集团合计
	public static String QUERYALLCOUNT = "queryallcount";

	public static final String DATA_SOURCE_PREfIX_CUSTOM = "spring.custom.datasource.";

	public static final String DATA_SOURCE_CUSTOM_NAME = "name";

	public static final String SEP = ",";
	public static final String DRUID_SOURCE_PREFIX = "spring.datasource.druid.";

	public static final String ENABLED_ATTRIBUTE_NAME = "enabled";
	// 配置超级管理员权限
	public static final String REPORT_ADMIN_PERMS = "report:admin:allperms";
	// 配置导出页面超级管理员权限
	public static final String EXPORT_ADMIN = "system:fileExport:adminFileExport";
	// 网点权限
	public static final String QUERYYJWDINFO = "queryWangdian";
	// 波动集团合计
	public static String QUERYBDCOMPANYINFO = "queryBDCompanyInfo";

	// 波动所有大区
	public static String QUERYBDBIGAREAINFO = "queryBDBigareaInfo";

	// 波动所有省
	public static String QUERYBDPROVINCEINFO = "queryBDProvinceInfo";

	// 波动某个省合计
	public static String QUERYBDSINGLEPROVINCEINFO = "queryBDSingleProvinceInfo";

	// 波动某个省的所有市
	public static String QUERYBDCITYINFO = "queryBDCityInfo";

	// 波动某个市合计
	public static String QUERYBDSINGLECITYINFO = "queryBDSingleCityInfo";

	// 波动网点1
	public static String QUERYBDGS1INFO = "queryBDGs1Info";

	// 波动网点2
	public static String QUERYBDGS2INFO = "queryBDGs2Info";

	// 集团统计客户波动
	public static String QUERYCUSTFLUCTUATEINFO = "queryBDCustFluctuateInfo";

	// 预警表 集团合计
	public static String QUERYYJCOMPANYINFO = "queryYJCompanyInfo";

	// 所有大区
	public static String QUERYYJBIGAREAINFO = "queryYJBigareaInfo";
	// 所有省
	public static String QUERYYJPROVINCEINFO = "queryYJProvinceInfo";
	// 省下的市doWithCity
	public static String YJDOCITYPROVINCE = "doWithCityprovince";

	public static String YJDOCITYCITY = "doWithCitycity";

	// doWithCustPronumber
	public static String YJCUSTPRONUMBERCUST = "doWithCustPronumbercust";
	// doWithBranch
	public static String YJBRANCHSHI = "doWithBranchshi";

	public static String YJBRANCHGS = "doWithBranchgs";

	// doWithCustomer
	public static String YJCUSTOMERCUST = "doWithCustomercust";

	// 量本利合计
	public static String QUERYCOSTREPORT = "queryCostReport";

	// 量本利合计导出
	public static String QUERYCOSTREPORTEXPORT = "queryCostReportExport";

	// 量本利下钻合计
	public static String QUERYCUSTREPORT = "queryCustReport";

	// 城市集合(key为编号,value为名称)
	public static Map<String, Object> city_code_name = new HashMap<String, Object>();

	// 城市集合(key为名称,value为编号)
	public static Map<String, Object> city_name_code = new HashMap<String, Object>();

	// 省集合(key为编号,value为名称)
	public static Map<String, Object> province_code_name = new HashMap<String, Object>();

	// 省集合(key为名称,value为编号)
	public static Map<String, Object> province_name_code = new HashMap<String, Object>();

	// 集团统计客户波动
	public static String QUERYKHBDINFO = "queryKHBDInfo";

	// 客户奖励明细
	public static String QUERYKHJLMXINFO = "queryKHJLMXInfo";
	
	// 客户奖励明细数量
	public static String QUERYKHJLMXINFOCOUNT = "queryKHJLMXInfoCount";
		
	// 奖励金额x的月份
	public static String TOTALPRICETYPEXMONTH = "02,03,04,05,06,07,08,";

	// 探测redis生死
	public static String REDISTC = "redistc";

	// 量本利收入报表
	public static String QUERYLBLSRBB = "querylblsrbb";

	// 量本利收入明细
	public static String QUERYLBLSRMX = "querylblsrmx";

	// 省同行间市场份额分析对比表
	public static String STHJSCFEFXDBB = "sthjscfefxdbb";

	// 重点城市同行间市场份额对比表
	public static String ZDCSTHJSCFEDBB = "zdcsthjscfedbb";

	// 标题
	public static final String custRewardTitle = "客户奖励明细表";
	
	public static final String branchCustRewardTitle = "网点客户奖励明细表";

	public static final String FluctuateTitle = "波动表";

	public static final String TotalDataTitle = "客户所属信息表";

	public static final String WarningTitle = "预警表";
	
	public static final String warningHandleTitle = "预警反馈处理表";
	
	// 方法名
	public static final String exportQueryMethodName = "exportQuery";
	
	// 方法名(网点权限)
	public static final String exportBranchQueryMethodName = "exportBranchQuery";

	// websocket通知用户的队列名
	public static final String wsQueueName = "/queue/notifications";
	// 自定义时间校验注解,时区区分
	public static final String CHINA = "China";

	// 短信平台成功标志
	public static final String SMS_CODE = "1";

	// 新返利逻辑开始时间，包括基础数据和查询数据在2019-01-01及以后的日期
	public static final String NEWBEGINRUNTIME = "2019-01-01";
	// 百度成功码
	public static final int BAIDU_ERRORCODE = 0;
	// 百度无人码
	public static final int BAIDU_NOPERSONCODE = 222202;
	// 百度错误信息
	public static final String BAIDU_ERRORMSG = "SUCCESS";
	//2019-01-01以前的一类省份（业务用）生效时间
	public static final String FIRST_PROVINCEIDSBEFORE2019_WORK = "2019-01-01";
	//2019-01-01以前的一类省份（业务用）
	public static final String FIRST_PROVINCEIDSBEFORE2019 = "'440000','330002','330001','320002','320001','310000','350000','370000','110000'";
	//2019-01-01至现在的一类省份（业务用）
	public static final String FIRST_PROVINCEIDS = "'440001','440002','440003','330002','330001','320002','320001','310000','350000','370000','110001'";

	//----------------------未合作客户的常量(Start)---------------------------
	//总部待处理
	public static final String ZONGBU_WAIT_DEAL = "总部待处理";
	//省公司待处理状态
	public static final String PROVINCE_WAIT_DEAL = "省公司待处理";
	//已达成合作状态
	public static final String SUCCESS_COOPERATE = "已达成合作";
	//未达成合作
	public static final String NOT_COOPERATE = "未达成合作";
	//处理失败
	public static final String DEAL_FAIL = "处理失败";
	//洽谈中
	public static final String DISCUSSE = "洽谈中";


	//----------------------未合作客户的常量(End)---------------------------
}
