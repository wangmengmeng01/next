package com.yunda.base.feiniao.schedule.suckdata.enums;

public enum DelFlagEnum {
	flag_ok(0, "正常"), flag_del(-11, "删除");

	// 成员变量
	private int code;
	private String name;

	DelFlagEnum(int code, String name) {
		this.name = name;
		this.code = code;
	}

	// 显示枚举中的有效code，用于validta中显示
	public static String showCodes() {
		StringBuilder stb = new StringBuilder();
		for (DelFlagEnum p : DelFlagEnum.values()) {
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
		for (DelFlagEnum p : DelFlagEnum.values()) {
			if (p.getCode() == code) {
				return true;
			}
		}
		return false;
	}

	// 普通方法
	public static String getName(int code) {
		for (DelFlagEnum p : DelFlagEnum.values()) {
			if (p.getCode() == code) {
				return p.name;
			}
		}
		return null;
	}

	// 普通方法
	public static DelFlagEnum getEnum(int code) {
		for (DelFlagEnum p : DelFlagEnum.values()) {
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
