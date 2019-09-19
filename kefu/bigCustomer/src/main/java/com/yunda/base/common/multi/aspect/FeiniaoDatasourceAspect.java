package com.yunda.base.common.multi.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.yunda.base.common.multi.dataSource.DynamicDataSourceContextHolder;

@Aspect
@Order(-1)
@Component
public class FeiniaoDatasourceAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeiniaoDatasourceAspect.class);

	@Before("@annotation(com.yunda.base.common.multi.annotation.DataSourceAnnotation)")
	public void changeDateSource(JoinPoint join){
		Object[] object = join.getArgs();
		String ids = object[join.getArgs().length-1].toString();
		if(!DynamicDataSourceContextHolder.existDateSoure(ids)){
			LOGGER.error("no datasource found "+"ids");
			return;
		}else{
			DynamicDataSourceContextHolder.setDateSoureType(ids);
			
		}
	}
	
    @After("@annotation(com.yunda.base.common.multi.annotation.DataSourceAnnotation)")
    public void destroyDataSource(JoinPoint point){
        DynamicDataSourceContextHolder.clearDateSoureType();
    }
	
}
