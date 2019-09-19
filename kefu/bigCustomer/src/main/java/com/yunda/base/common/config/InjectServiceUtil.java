package com.yunda.base.common.config;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yunda.base.system.service.OutLogService;

/**
 * 加这个是为了让session失效监控中可以引用outLogService进行存放日志记录
 * 不然在BDSessionListener中引用outLogService会是null
 */
@Component
public class InjectServiceUtil {

    @Autowired
    private OutLogService outLogService;

    @PostConstruct
    public void init(){
        InjectServiceUtil.getInstance().outLogService = this.outLogService;
    }

    /**

     *  实现单例 start

     */
    private static class SingletonHolder {
        private static final InjectServiceUtil INSTANCE = new InjectServiceUtil();
    }

    private InjectServiceUtil (){}

    public static final InjectServiceUtil getInstance() {

        return SingletonHolder.INSTANCE;

    }

    /**

     *  实现单例 end

     */
    public OutLogService getOutLogService(){

        return InjectServiceUtil.getInstance().outLogService;

    }

}
