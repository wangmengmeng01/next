package com.yunda.base.feiniao.log.enums;

public enum LogSuckTypeEnum {
	khje_wd(1, "客户金额报表按网点"), khje_province(2, "客户金额报表按省份"), khje_area(3, "客户金额报表按大区"), khje_country(4,
			"客户金额报表按全国时间段"), khje_city(5, "客户金额报表按城市"),khgp_source(6, "客户gp数据汇总"),khgp_customersource(7, "客户gp客户数据汇总"),
    khje_customer(8, "客户金额汇总"),
	khlj_city(11, "客户揽件报表按城市"), khlj_customer(11, "客户揽件报表按客户"), khlj_wd(1, "客户揽件报表按网点"), khlj_province(1,
			"客户揽件报表按省份"), khlj_area(1, "客户揽件报表按大区"), khlj_country(1, "客户揽件报表按全国时间段")
	,khyj_warningsum(1,"客户预警报表"),khbd_cutomer(1,"按日生成客户波动基础数据"),khyj_warningday(1, "预警反馈表")		

	;

	// 成员变量
	private int code;
	private String name;

	LogSuckTypeEnum(int code, String name) {
		this.name = name;
		this.code = code;
	}

	// 显示枚举中的有效code，用于validta中显示
	public static String showCodes() {
		StringBuilder stb = new StringBuilder();
		for (LogSuckTypeEnum p : LogSuckTypeEnum.values()) {
			if (stb.length() > 0) {
				stb.append(",");
			}
			stb.append(p.getCode());
		}
		return stb.toString();
	}

	// 是否包含xxcode
	public static boolean hitCode(String code) {
		return hitCode(Integer.valueOf(code));
	}

	// 是否包含xxcode
	public static boolean hitCode(Integer code) {
		for (LogSuckTypeEnum p : LogSuckTypeEnum.values()) {
			if (p.getCode() == code) {
				return true;
			}
		}
		return false;
	}

	// 普通方法
	public static String getName(int code) {
		for (LogSuckTypeEnum p : LogSuckTypeEnum.values()) {
			if (p.getCode() == code) {
				return p.name;
			}
		}
		return null;
	}

	// 普通方法
	public static LogSuckTypeEnum getEnum(int code) {
		for (LogSuckTypeEnum p : LogSuckTypeEnum.values()) {
			if (p.getCode() == code) {
				return p;
			}
		}
		return null;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
