package com.yunda.base.feiniao.costreport.enums;

public enum SoaOrgType {
	/**
	 * 总部
	 * */
	HEAD_OFFICE(1, "总部"),
	
	/**
	 * 分拨中心
	 */
	DISTRIBUTION_CENTER(2,"分拨中心"),
	
	/**
	 * 区域
	 */
	REGION(3,"区域"),
	
	
	/**
	 * 网点
	 * */
	BRANCH(4, "网点"),
	
	/**
	 * 省级中心
	 * */
	PROVINCIAL_CENTER(5, "省级中心"),
	
	/**
	 * 省级中心
	 * */
	PROVINCIAL_CENTERNEW(6, "省级中心");

	// 成员变量
	private int code;
	private String name;

	SoaOrgType(int code, String name) {
		this.name = name;
		this.code = code;
	}

	// 显示枚举中的有效code，用于validta中显示
	public static String showCodes() {
		StringBuilder stb = new StringBuilder();
		for (SoaOrgType p : SoaOrgType.values()) {
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
		for (SoaOrgType p : SoaOrgType.values()) {
			if (p.getCode() == code) {
				return true;
			}
		}
		return false;
	}

	// 普通方法
	public static String getName(int code) {
		for (SoaOrgType p : SoaOrgType.values()) {
			if (p.getCode() == code) {
				return p.name;
			}
		}
		return null;
	}

	// 普通方法
	public static SoaOrgType getEnum(int code) {
		for (SoaOrgType p : SoaOrgType.values()) {
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
