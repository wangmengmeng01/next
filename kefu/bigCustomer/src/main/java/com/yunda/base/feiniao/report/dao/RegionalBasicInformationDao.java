package com.yunda.base.feiniao.report.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.yunda.base.feiniao.report.domain.RegionalBasicInformationDO;

/**
 * 区县市省大区关系表
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2019-03-11112155
 */
@Mapper
public interface RegionalBasicInformationDao {

	//获取每月基础区县市省大区基础信息
	List<RegionalBasicInformationDO> getBasicInformationList();
	//删除每月基础区县市省大区基础信息
	int deleteDateByDate(String targetDate);
	
	//保存每月基础信息
	int saveBasicalInfo(List<RegionalBasicInformationDO> infoList);
	
}
