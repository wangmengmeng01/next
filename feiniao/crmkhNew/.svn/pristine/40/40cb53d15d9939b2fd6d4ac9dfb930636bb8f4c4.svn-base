package com.yunda.base.feiniao.schedule.suckdata.task.impls.market;

import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.market.domain.MarketOccupancyTaoxiDO;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 淘系
 */
public class MarketTaoxiSum extends MarketTaoxiTaskAbs {
    Logger log = Logger.getLogger(getClass());

    @Override
    public int index() {
        return 1;
    }

    @Override
    public boolean preCheck(LogSuckdataService logSuckdataService, Date targetDay) {
        return true;
    }

    @Override
    public String realProcess(LogSuckdataService logSuckdataService, Date targetDay) {
        int result =0;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String quDate = sdf.format(targetDay);
        MarketOccupancyTaoxiDO marketOccupancyTaoxi = new MarketOccupancyTaoxiDO();
        if(quDate!=null && !"".equals(quDate)){
        	marketOccupancyTaoxi.setShowQuDate(quDate);
        	//result =marketOccupancyTaoxiDao.save(marketOccupancyTaoxi);
        	result = TaskBeanUtils.getMarketOccupancyTaoxiDao().save(marketOccupancyTaoxi);
        }
        return "淘系市场占有率上报数据生成"+result!=null?result+"":0+"条";
    }


    @Override
    public void realClearTable(LogSuckdataService logSuckdataService, Date targetDay) {
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String quDate = sdf.format(targetDay);
        
    	if(org.apache.commons.lang3.StringUtils.isNotEmpty(quDate)){
    		//marketOccupancyTaoxiDao.removeByDate(quDate);//这样有问题   不加载
    		TaskBeanUtils.getMarketOccupancyTaoxiDao().removeByDate(quDate);
    	}
    }

    @Override
    public void realClearRedis(LogSuckdataService logSuckdataService, Date targetDay) {

    }

    @Override
    public int whichLogType() {
        return 0;
    }

    @Override
    public String whoareyou() {
        return null;
    }

    @Override
    public String cacheKeyPerfix() {
        return null;
    }
}
