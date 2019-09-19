package com.yunda.base.feiniao.costreport.listener;

import com.yunda.base.common.config.Constant;
import com.yunda.base.feiniao.costreport.service.CostreportOrderCostFinishService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.Map;

@WebListener
public class ProvinceListener implements ServletContextListener{

	Logger logger =LoggerFactory.getLogger(ProvinceListener.class);
	@Autowired
	private CostreportOrderCostFinishService costreportOrderCostFinishService;
	
	private void getProvinceCodeNameMap(){
		try{
			logger.info("初始化省信息");
		    List<Map<String, Object>> province_code_name =  costreportOrderCostFinishService.getProvinceCodeNameMap();			
			for(Map<String, Object> end :province_code_name){				       
			    Constant.province_code_name.put(end.get("code")+"", end.get("name")+"");	             			
			}
			for(Map.Entry<String, Object> map :Constant.province_code_name.entrySet()){
				Constant.province_name_code.put(map.getValue()+"", map.getKey());			
			}
		}catch(Exception ex){		
			logger.error("获取所有省信息失败", ex);
			logger.error(ex.getMessage(), ex);
		}
	}
	
	private void getCityCodeNameMap(){
		try{
			logger.info("初始化省信息");
			List<Map<String, Object>> city_code_name =  costreportOrderCostFinishService.getCityCodeNameMap();						
			for(Map<String, Object> end :city_code_name){					              
			 	Constant.city_code_name.put(end.get("code")+"",end.get("name")+"");			
			}		
			for(Map.Entry<String, Object> map :Constant.city_code_name.entrySet()){
				Constant.city_name_code.put(map.getValue()+"", map.getKey());
			
			}
		}catch(Exception ex){		
			logger.error("获取所有城市信息失败", ex);
			logger.error(ex.getMessage(), ex);
		}
	}
	
	public void contextDestroyedClearAreaMapData(){
		if (Constant.city_code_name != null) {
			Constant.city_code_name.clear();
			Constant.city_code_name = null;
		}
		if (Constant.city_name_code != null) {
			Constant.city_name_code.clear();
			Constant.city_name_code = null;
		}
		if (Constant.province_code_name != null) {
			Constant.province_code_name.clear();
			Constant.province_code_name = null;
		}
		if (Constant.province_name_code != null) {
			Constant.province_name_code.clear();
			Constant.province_name_code = null;
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		/**
		 * autowired的类在启动的时候，由于相应的beanfactory还没有加载，所以会出现空指针问题
		 */
		WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext())  
        .getAutowireCapableBeanFactory().autowireBean(this); 
		try{
			getProvinceCodeNameMap();
			getCityCodeNameMap();	
			logger.info("初始化数据完成");
		} catch (Exception e) {
			logger.error("初始化数据存入缓存失败！", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		contextDestroyedClearAreaMapData();
		logger.info("销毁初始化数据");
	}

}