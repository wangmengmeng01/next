package com.yunda.base.feiniao.log.enums;

public enum EventTypeEnum {
	event_null(0, "无事件"), event_sms(1, "消息报警"), event_mail(2, "邮件报警"), event_websocket(3, "websocket"), event_all(4,
			"所有可用途径产生报警");

	// 成员变量
	private int code;
	private String name;

	EventTypeEnum(int code, String name) {
		this.name = name;
		this.code = code;
	}

	// 显示枚举中的有效code，用于validta中显示
	public static String showCodes() {
		StringBuilder stb = new StringBuilder();
		for (EventTypeEnum p : EventTypeEnum.values()) {
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
		for (EventTypeEnum p : EventTypeEnum.values()) {
			if (p.getCode() == code) {
				return true;
			}
		}
		return false;
	}

	// 普通方法
	public static String getName(int code) {
		for (EventTypeEnum p : EventTypeEnum.values()) {
			if (p.getCode() == code) {
				return p.name;
			}
		}
		return null;
	}

	// 普通方法
	public static EventTypeEnum getEnum(int code) {
		for (EventTypeEnum p : EventTypeEnum.values()) {
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
