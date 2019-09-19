package com.yunda.base.common.enums;

//渠道枚举
public enum ChannelEnum {
	wechat(1, "wechat", "微信"), alipay(2, "alipay", "支付宝");

	// 成员变量
	private int index;
	private String code;
	private String name;

	ChannelEnum(int index, String code, String name) {
		this.index = index;
		this.name = name;
		this.code = code;
	}

	// 是否包含xxcode
	public static boolean hitCode(String code) {
		for (ChannelEnum p : ChannelEnum.values()) {
			if (p.getCode().equals(code)) {
				return true;
			}
		}
		return false;
	}

	public static String showAllCode() {
		StringBuilder stb = new StringBuilder();
		for (ChannelEnum p : ChannelEnum.values()) {
			if (stb.length() > 0) {
				stb.append(",");
			}
			stb.append(p.getCode());
		}
		return stb.toString();
	}

	// 普通方法
	public static String getName(int index) {
		for (ChannelEnum p : ChannelEnum.values()) {
			if (p.getIndex() == index) {
				return p.name;
			}
		}
		return null;
	}

	// 普通方法
	public static String getCode(int index) {
		for (ChannelEnum p : ChannelEnum.values()) {
			if (p.getIndex() == index) {
				return p.code;
			}
		}
		return null;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
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