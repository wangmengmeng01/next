package com.yundagalaxy.management.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yundagalaxy.management.vo.ExcelEmployeeInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springblade.core.mp.support.Query;

import java.util.List;
import java.util.Map;

/**
 * 一段话简述功能。
 * <p>
 * Created by MiaoYuanMeng on 2019/10/30.
 */
@Mapper
public interface EmployeeInfoAggregationMapper {

    List<ExcelEmployeeInfoVO> empList(@Param("params") Map<String, Object> params,@Param("query") Query query);
}
