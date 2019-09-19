package com.yunda.base.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 查询参数
 */
public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	//
	private int offset = 0;
	// 每页条数
	private int limit = 10;

	public Query(Map<String, Object> params) {
		this.putAll(params);
		// 分页参数
		if (params.containsKey("offset")) {
			this.offset = Integer.parseInt(params.get("offset").toString());
		}
		if (params.containsKey("limit")) {
			this.limit = Integer.parseInt(params.get("limit").toString());
		}
		this.put("offset", offset);
		this.put("page", offset / limit + 1);
		this.put("limit", limit);
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.put("offset", offset);
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
