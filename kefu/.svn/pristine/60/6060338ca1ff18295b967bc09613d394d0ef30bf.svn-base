package com.yunda.base.feiniao.schedule.suckdata.service;

public interface SuckDataService {

	/**
	 * 针对指定日期进行抽数，如果当日已经抽取过，那么需要先清理然后重建数据
	 * 
	 * @param targetDay抽数的目标日期，
	 *            0为当天，-1为昨天，-2为前天，以此类推
	 */
    void processSuck(int targetDay);

	// 全表统计Gp源头表crmkh_gp_bas_s_cust_pick_tmp的条数【该表的引擎必须设定为MyISAM】
    int countGpSource(String dsId);

	// 指定日期获取GP推送的客户揽件总表数据的起始id

	// 指定日期获取GP推送的客户揽件总表数据的最后一条

}
