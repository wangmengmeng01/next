package com.yundagalaxy.management.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 一段话简述功能。
 * <p>
 * Created by MiaoYuanMeng on 2019/10/29.
 */
@Mapper
public interface CommonToolMapper{

    List<Map<String, Object>> getProvinces();

    List<Map<String, Object>> getCitys(@Param("ProvinceID") String ProvinceID);

    List<Map<String, Object>> getCountys(@Param("CityID") String CityID);

    List<Map<String, Object>> getLowerOrgCode(@Param("deptId") String deptId);
}
