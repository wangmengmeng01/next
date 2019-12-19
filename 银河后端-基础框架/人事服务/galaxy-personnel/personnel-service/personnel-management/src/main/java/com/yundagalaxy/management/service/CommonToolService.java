package com.yundagalaxy.management.service;

import java.util.List;
import java.util.Map;

/**
 * 一段话简述功能。
 * <p>
 * Created by MiaoYuanMeng on 2019/10/29.
 */
public interface CommonToolService {

    List<Map<String, Object>> getProvinces();

    List<Map<String, Object>> getCitys(String provinceID);

    List<Map<String, Object>> getCountys(String cityID);

    List<Map<String, Object>> getLowerOrgCode(String deptId);
}
