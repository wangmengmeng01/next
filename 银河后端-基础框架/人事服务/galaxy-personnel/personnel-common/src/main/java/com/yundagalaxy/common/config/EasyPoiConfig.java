/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package com.yundagalaxy.common.config;


import cn.afterturn.easypoi.view.MiniAbstractExcelView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Demo配置
 *
 * @author feng.dong
 */
@Configuration
@Slf4j
public  class EasyPoiConfig{

	@Bean
	public  MiniAbstractExcelView miniAbstractExcelView() {
        MiniAbstractExcelView miniAbstractExcelView = new MiniAbstractExcelView() {
            @Override
            protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
                log.info("初始化easyPoi======>>>成功！");
            }
        };
        return miniAbstractExcelView;
	}

}
