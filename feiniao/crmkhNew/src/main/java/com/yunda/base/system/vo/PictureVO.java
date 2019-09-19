package com.yunda.base.system.vo;

import java.io.Serializable;
import java.util.List;

public class PictureVO implements Serializable {
	/*相册标题*/
	private String title="";
	/*相册id*/
	private int id;
	/*初始显示的图片序号，默认0*/
	private int start;
	/*相册包含的图片，数组格式*/
	private List<Data> data;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "PictureVO{" +
				"title='" + title + '\'' +
				", id='" + id + '\'' +
				", start='" + start + '\'' +
				", data=" + data +
				'}';
	}
}


