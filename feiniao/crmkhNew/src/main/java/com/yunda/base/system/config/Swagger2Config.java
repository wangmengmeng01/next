package com.yunda.base.system.config;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * ${DESCRIPTION}
 *
 * @author edison
 * @create 2017-01-02 23:53
 */
@EnableSwagger2
@Configuration
public class Swagger2Config {
//	@Value("${info.app.version}")
//	private String appVer;

	@Value("${info.app.name}")
	private String appName;
	
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //只有加了api注释的才会被swagger显示
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    //构建 api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("功能接口")
                //创建人
                //.contact(new Contact("Edison", "xxx@qq.com", "xxx@qq.com"))
                //版本号
//                .version(appVer)
                //描述
                .description(appName)
                .build();
    }
}