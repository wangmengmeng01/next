package com.yundagalaxy.management.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * PersonnelProperties
 *
 * @author feng.dong
 */
@Data
@ConfigurationProperties(prefix = "personnel")
public class PersonnelProperties {
	/**
	 * 名称
	 */
	private String name;
}
