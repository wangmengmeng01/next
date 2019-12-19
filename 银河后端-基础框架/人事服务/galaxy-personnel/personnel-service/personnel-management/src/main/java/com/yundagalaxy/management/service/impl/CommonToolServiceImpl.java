package com.yundagalaxy.management.service.impl;

import com.yundagalaxy.management.mapper.CommonToolMapper;
import com.yundagalaxy.management.service.CommonToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 一段话简述功能。
 * <p>
 * Created by MiaoYuanMeng on 2019/10/29.
 */
@Service
public class CommonToolServiceImpl implements CommonToolService {

    @Autowired
    private CommonToolMapper commonToolMapper;

    @Override
    public List<Map<String, Object>> getProvinces() {
        return commonToolMapper.getProvinces();
    }

    @Override
    public List<Map<String, Object>> getCitys(String provinceID) {
        return commonToolMapper.getCitys(provinceID);
    }

    @Override
    public List<Map<String, Object>> getCountys(String cityID) {
        return commonToolMapper.getCountys(cityID);
    }

    @Override
    public List<Map<String, Object>> getLowerOrgCode(String deptId) {
        return commonToolMapper.getLowerOrgCode(deptId);
    }
}
