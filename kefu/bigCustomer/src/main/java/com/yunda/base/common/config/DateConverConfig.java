package com.yunda.base.common.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class DateConverConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateConverConfig.class);

	@Bean
	public Converter<String, Date> stringDateConvert() {
		return new Converter<String, Date>() {
			@Override
			public Date convert(String source) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = null;
				try {
					date = sdf.parse(source);
				} catch (Exception e) {
					SimpleDateFormat sdfday = new SimpleDateFormat("yyyy-MM-dd");
					try {
						date = sdfday.parse(source);
					} catch (ParseException e1) {
						LOGGER.error("date convert Error", e1);
					}
				}
				return date;
			}
		};
	}

}
