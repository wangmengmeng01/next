package com.yunda.base.common.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.beanutils.converters.SqlTimestampConverter;

import java.lang.reflect.InvocationTargetException;

/**
 * 使用BeaUtils.copyProperties时，如果源目标中包含Date类型（sql.date，util,date）字段，而且该字段值为空时，
 * 会出现异常，无法赋值
 * 
 * 通过声名Date类型的转换类解决
 * 
 * 取自http://www.blogjava.net/javagrass/archive/2011/10/10/352856.html
 * 
 * @author Grimm
 *
 */

public final class BeanUtilEx extends BeanUtils {
	static {
		// 注册sql.date的转换器，即允许BeanUtils.copyProperties时的源目标的sql类型的值允许为空
		ConvertUtils.register(new SqlDateConverter(null), java.sql.Date.class);
		ConvertUtils.register(new SqlDateConverter(null), java.util.Date.class);
		ConvertUtils.register(new SqlTimestampConverter(null), java.sql.Timestamp.class);
	}

	public static void copyProperties(Object target, Object source)
			throws InvocationTargetException, IllegalAccessException {
		BeanUtils.copyProperties(target, source);

	}
}
