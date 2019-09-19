package com.yunda.base.common.enums;

public enum ExportEnum {
	StandingBy("1", "等待中"), Handing("2", "处理中"), Complete("3", "执行完成"), fail("4", "执行失败"), no_data("5", "无数据");
	private String num;
	private String name;

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	ExportEnum(String num, String name) {
		this.num = num;
		this.name = name;
	}

}
