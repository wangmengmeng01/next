package com.yunda.base.common.config;

import com.yunda.base.system.config.SysConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
class WebConfigurer extends WebMvcConfigurerAdapter {
	@Resource(name = "thymeleafViewResolver")
	private ThymeleafViewResolver thymeleafViewResolver;

//	@Value("${info.app.version}")
//	private String appVer;

	@Value("${info.app.name}")
	private String appName;

	@Value("${info.app.description}")
	private String description;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/files/**").addResourceLocations("file:///" + SysConfig.uploadPath);
	}

	@Override
	//参看 https://www.jianshu.com/p/9214d792c5e3
	public void configureViewResolvers(ViewResolverRegistry registry) {
		if (thymeleafViewResolver != null) {
			Map<String, Object> vars = new HashMap<>(8);
//			vars.put("appVer", appVer);
			vars.put("appName", appName);
			vars.put("description", description);
			thymeleafViewResolver.setStaticVariables(vars);
		}
		super.configureViewResolvers(registry);
	}
}
