package com.yunda.base.common.multi.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.yunda.base.common.config.Constant;
import com.yunda.base.common.multi.dataSource.DynamicDataSource;
import com.yunda.base.common.multi.dataSource.DynamicDataSourceContextHolder;

@Configuration
@EnableTransactionManagement
public class DruidConfig implements EnvironmentAware {

	private List<String> customDataSourceNames = new ArrayList<String>();
	
    private Logger logger = LogManager.getLogger(DruidConfig.class);
	
    private ConversionService conversionService = new DefaultConversionService();
 
    private Environment environment;
    
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
    
    @Bean(name = "dataSource")
    @Primary
    public AbstractRoutingDataSource dataSource(){
    	DynamicDataSource dynamic = new DynamicDataSource();
    	LinkedHashMap<Object, Object> map = new LinkedHashMap<Object, Object>();
    	initCustomDataSources(map);
    	dynamic.setDefaultTargetDataSource(map.get(customDataSourceNames.get(0)));
    	dynamic.setTargetDataSources(map);
    	dynamic.afterPropertiesSet();
    	return dynamic;
    }
    
    private void initCustomDataSources(LinkedHashMap<Object, Object> targetDataResources){
      RelaxedPropertyResolver relaxed = new RelaxedPropertyResolver(environment,Constant.DATA_SOURCE_PREfIX_CUSTOM);
      
      String dataSourceNames = relaxed.getProperty(Constant.DATA_SOURCE_CUSTOM_NAME);
      if(StringUtils.isEmpty(dataSourceNames)){
          logger.error("The multiple data source list are empty.");

      }else{
          RelaxedPropertyResolver springDataSourceProperty =
                  new RelaxedPropertyResolver(environment, "spring.datasource.");

          Map<String, Object> druidPropertiesMaps = springDataSourceProperty.getSubProperties("druid.");
          Map<String,Object>  druidValuesMaps = new HashMap<>();
          for(String key :druidPropertiesMaps.keySet()){
        	  String druid = Constant.DRUID_SOURCE_PREFIX+key;
        	  druidValuesMaps.put(druid, druidPropertiesMaps.get(key));
          }
          MutablePropertyValues values = new MutablePropertyValues(druidValuesMaps);
          for (String dataSourceName : dataSourceNames.split(Constant.SEP)) {
              Map<String, Object> dsMaps = relaxed.getSubProperties(dataSourceName+".");

              for(String dsKey : dsMaps.keySet())
              {
                  if(dsKey.equals("type"))
                  {
                	  values.addPropertyValue("spring.datasource.type", dsMaps.get(dsKey));
                  }
                  else
                  {
                      String druidKey = Constant.DRUID_SOURCE_PREFIX + dsKey;
                      values.addPropertyValue(druidKey, dsMaps.get(dsKey));
                  }
              }
              System.out.println(values.get("spring.datasource.type").toString());

              DataSource ds = dataSourcebuild(values);
              if(null != ds){
            	  if(ds instanceof DruidDataSource){
                      DruidDataSource druidDataSource = (DruidDataSource)ds;
                      druidDataSource.setName(dataSourceName);
                      initDruidFilters(druidDataSource);

            	  }
            	  customDataSourceNames.add(dataSourceName);
            	  DynamicDataSourceContextHolder.datasourceId.add(dataSourceName);
                  targetDataResources.put(dataSourceName,ds);

            	  
              }
              logger.info("Data source initialization 【"+dataSourceName+"】 successfully ...");

          }
      }   
    }

    public DataSource dataSourcebuild(MutablePropertyValues values){
    	
    	DataSource ds =null;
    	if(values.isEmpty()){
    		return ds;
    	}
    	String type = values.get("spring.datasource.type").toString();
        if(StringUtils.isNotEmpty(type)){
    		ds= new DruidDataSource();
    		RelaxedDataBinder db=new RelaxedDataBinder(ds,Constant.DRUID_SOURCE_PREFIX);
    		db.setConversionService(conversionService);
    		db.setIgnoreInvalidFields(false);
    		db.setIgnoreNestedProperties(false);
    		db.setIgnoreUnknownFields(true);
    		db.bind(values);
    	}
    	
    	return ds;
    	
    }
    
    @Bean
    public ServletRegistrationBean statViewServlet(){

        RelaxedPropertyResolver property =
                new RelaxedPropertyResolver(environment, "spring.datasource.druid.");

        Map<String, Object> druidPropertiesMaps = property.getSubProperties("stat-view-servlet.");


        boolean statViewServletEnabled = false;
        String statViewServletEnabledKey = Constant.ENABLED_ATTRIBUTE_NAME;
        ServletRegistrationBean registrationBean = null;

        if(druidPropertiesMaps.containsKey(statViewServletEnabledKey))
        {
            String statViewServletEnabledValue =
                    druidPropertiesMaps.get(statViewServletEnabledKey).toString();
            statViewServletEnabled = Boolean.parseBoolean(statViewServletEnabledValue);
        }
        if(statViewServletEnabled){
            registrationBean = new ServletRegistrationBean();
            StatViewServlet statViewServlet = new StatViewServlet();

            registrationBean.setServlet(statViewServlet);

            String urlPatternKey= "url-pattern";
            String allowKey= "allow";
            String denyKey= "deny";
            String usernameKey= "login-username";
            String secretKey = "login-password";
            String resetEnableKey= "reset-enable";

            if(druidPropertiesMaps.containsKey(urlPatternKey)){
                String urlPatternValue =
                        druidPropertiesMaps.get(urlPatternKey).toString();
                registrationBean.addUrlMappings(urlPatternValue);
            }
            else
            {
                registrationBean.addUrlMappings("/druid/*");
            }

            addBeanParameter(druidPropertiesMaps,registrationBean, "allow",allowKey);
            addBeanParameter(druidPropertiesMaps,registrationBean, "deny",denyKey);
            addBeanParameter(druidPropertiesMaps,registrationBean, "loginUsername",usernameKey);
            addBeanParameter(druidPropertiesMaps,registrationBean, "loginPassword",secretKey);
            addBeanParameter(druidPropertiesMaps,registrationBean, "resetEnable",resetEnableKey);
        }

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        RelaxedPropertyResolver property =
                new RelaxedPropertyResolver(environment, "spring.datasource.druid.");

        Map<String, Object> druidPropertiesMaps = property.getSubProperties("web-stat-filter.");


        boolean webStatFilterEnabled = false;
        String webStatFilterEnabledKey = Constant.ENABLED_ATTRIBUTE_NAME;
        FilterRegistrationBean registrationBean = null;
        if(druidPropertiesMaps.containsKey(webStatFilterEnabledKey))
        {
            String webStatFilterEnabledValue =
                    druidPropertiesMaps.get(webStatFilterEnabledKey).toString();
            webStatFilterEnabled = Boolean.parseBoolean(webStatFilterEnabledValue);
        }
        if(webStatFilterEnabled){
            registrationBean = new FilterRegistrationBean();
            WebStatFilter filter = new WebStatFilter();
            registrationBean.setFilter(filter);

            String urlPatternKey = "url-pattern";
            String exclusionsKey = "exclusions";
            String sessionStatEnabledKey = "session-stat-enable";
            String profileEnabledKey = "profile-enable";
            String principalCookieNameKey = "principal-cookie-name";
            String principalSessionNameKey = "principal-session-name";
            String sessionStateMaxCountKey = "session-stat-max-count";

            if(druidPropertiesMaps.containsKey(urlPatternKey)){
                String urlPatternValue =
                        druidPropertiesMaps.get(urlPatternKey).toString();
                registrationBean.addUrlPatterns(urlPatternValue);
            }
            else{
                registrationBean.addUrlPatterns("/*");
            }

            if(druidPropertiesMaps.containsKey(exclusionsKey)){
                String exclusionsValue =
                        druidPropertiesMaps.get(exclusionsKey).toString();
                registrationBean.addInitParameter("exclusions",exclusionsValue);
            }
            else{
                registrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
            }

            addBeanParameter(druidPropertiesMaps,registrationBean, "sessionStatEnable",sessionStatEnabledKey);
            addBeanParameter(druidPropertiesMaps,registrationBean, "profileEnable",profileEnabledKey);
            addBeanParameter(druidPropertiesMaps,registrationBean, "principalCookieName",principalCookieNameKey);
            addBeanParameter(druidPropertiesMaps,registrationBean, "sessionStatMaxCount",sessionStateMaxCountKey);
            addBeanParameter(druidPropertiesMaps,registrationBean, "principalSessionName",principalSessionNameKey);
        }
        return registrationBean;
    }

    private void addBeanParameter(Map<String,Object> druidPropertyMap, RegistrationBean registrationBean, String paramName, String propertyKey){
        if(druidPropertyMap.containsKey(propertyKey)){
            String propertyValue =
                    druidPropertyMap.get(propertyKey).toString();
            registrationBean.addInitParameter(paramName, propertyValue);
        }
    }
    
    
    private void initDruidFilters(DruidDataSource druidDataSource){
    	 List<Filter> filters =	druidDataSource.getProxyFilters();
    	 RelaxedPropertyResolver filterProperty =
                 new RelaxedPropertyResolver(environment, "spring.datasource.druid.filter.");

         String filterNames= environment.getProperty("spring.datasource.druid.filters");
         String[] filterNameArray = filterNames.split("\\,");
         for(int i=0; i<filterNameArray.length;i++){
             String filterName = filterNameArray[i];
             Filter filter = filters.get(i);

             Map<String, Object> filterValueMap = filterProperty.getSubProperties(filterName + ".");

             String statFilterEnabled = filterValueMap.get(Constant.ENABLED_ATTRIBUTE_NAME).toString();
             if(statFilterEnabled.equals("true")){
                  MutablePropertyValues values = new MutablePropertyValues(filterValueMap);
                  RelaxedDataBinder dataBinder = new RelaxedDataBinder(filter);
                  dataBinder.bind(values);
             }             
         }
    	 
    	 
    }
    
}
