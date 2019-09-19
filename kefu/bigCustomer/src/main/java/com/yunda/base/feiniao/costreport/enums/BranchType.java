package com.yunda.base.feiniao.costreport.enums;

public enum BranchType {
	
	/**
	 * 总部
	 * */
	HEAD_OFFICE(50, "总部"),
	
	/**
	 * 分拨中心
	 */
	DISTRIBUTION_CENTER(3,"分拨中心"),
	
	/**
	 * 网点
	 * */
	BRANCH(2, "网点"),
	
	/**
	 * 分部
	 * */
	SEGMENT(21, "分部"),
	
	/**
	 * 服务部
	 * */
	SERVICE_BUREAU(22, "服务部");

	// 成员变量
	private int code;
	private String name;

	BranchType(int code, String name) {
		this.name = name;
		this.code = code;
	}

	// 显示枚举中的有效code，用于validta中显示
	public static String showCodes() {
		StringBuilder stb = new StringBuilder();
		for (BranchType p : BranchType.values()) {
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
		for (BranchType p : BranchType.values()) {
			if (p.getCode() == code) {
				return true;
			}
		}
		return false;
	}

	// 普通方法
	public static String getName(int code) {
		for (BranchType p : BranchType.values()) {
			if (p.getCode() == code) {
				return p.name;
			}
		}
		return null;
	}

	// 普通方法
	public static BranchType getEnum(int code) {
		for (BranchType p : BranchType.values()) {
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
