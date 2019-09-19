package com.yunda.base.feiniao.schedule.suckdata.enums;

//抽数缓存的前缀
public enum SuckCacheKeyPerfixEnum {
	bodong("bodongcache", "波动缓存"), yujin("yujincache", "预警缓存"), zongbiao("zongbiaocache", "总表缓存"),jianglimingxi("jianglimingxicache","奖励明细缓存")
	,costReport("costreportcache", "量本利缓存"),costReportExport("costreportexportcache", "量本利导出缓存"),custReport("custreportcache", "量本利下钻缓存"),redisTC("redistance","探测redis生死"),marketReport("marketReportcache", "占有率缓存"),
	costReportIncome("costReportIncome","量本利收入报表缓存"),costReportIncomeDetail("costReportIncomeDetail","量本利收入报表明细缓存"),marketKeyProvince("marketKeyProvince","省同行间市场份额分析对比表缓存"),
	marketKeyCity("marketKeyCity","重点城市同行间市场份额对比表"), potentialNew("potentialNew","潜在客户新表查询缓存头"),
	yujinweek("yujinweekcache", "预警周基础表缓存"),yujinday("yujindaycache", "预警反馈表缓存");

	// 成员变量
	private String code;
	private String name;

	SuckCacheKeyPerfixEnum(String code, String name) {
		this.name = name;
		this.code = code;
	}

	// 显示枚举中的有效code，用于validta中显示
	public static String showCodes() {
		StringBuilder stb = new StringBuilder();
		for (SuckCacheKeyPerfixEnum p : SuckCacheKeyPerfixEnum.values()) {
			if (stb.length() > 0) {
				stb.append(",");
			}
			stb.append(p.getCode());
		}
		return stb.toString();
	}

	// 普通方法
	public static String getName(String code) {
		for (SuckCacheKeyPerfixEnum p : SuckCacheKeyPerfixEnum.values()) {
			if (p.getCode().equals(code)) {
				return p.name;
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}