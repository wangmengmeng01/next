package com.yunda.base.feiniao.report.utils;

import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

@SuppressWarnings("all")
public class JsonFilterUtils {
	public static String filterJson(Object object) {

		JsonConfig jsonConfig = new JsonConfig();
		PropertyFilter filter = new PropertyFilter() {
			@Override
			public boolean apply(Object object, String fieldName, Object fieldValue) {
				if (fieldValue instanceof List) {
					List<Object> list = (List<Object>) fieldValue;
					if (list.size() == 0) {
						return true;
					}
				}
				if (fieldValue instanceof Date) {
						return true;
				}
				return null == fieldValue || "".equals(fieldValue);
			}
		};// 添加过滤器,将属性值不为null和""的晰出
		jsonConfig.setJsonPropertyFilter(filter);
		return JSONObject.fromObject(object, jsonConfig).toString();
	}
}
