package com.yunda.base.common.multi.config;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = {"com.yunda.base.feiniao.report.dao","com.yunda.base.feiniao.costreport.dao",
		"com.yunda.base.feiniao.schedule.suckdata.dao", "com.yunda.base.feiniao.log.dao",
		"com.yunda.base.feiniao.workorder.dao","com.yunda.base.system.dao","com.yunda.base.common.dao" }, sqlSessionFactoryRef = "sqlSessionFactory")
public class SessionFactoryConfig {
    private static Logger logger = LogManager.getLogger(SessionFactoryConfig.class);

    @Autowired
    private DataSource dataSource;
    
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory createSqlSessionFactoryBean() {
        logger.info("createSqlSessionFactoryBean method");

        try{
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);            
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:mybatis/**/*Mapper.xml"));
            sqlSessionFactoryBean.setTypeAliasesPackage("com.yunda.base.**.domain");
    		try {
				sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
	            return sqlSessionFactoryBean.getObject();

    		} catch (Exception e) {
    			logger.error(e.getMessage(), e);
				//e.printStackTrace();
			}

        }
        catch(IOException ex){
            logger.error("Error happens when getting config files." + ExceptionUtils.getMessage(ex));
        }
        return null;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
