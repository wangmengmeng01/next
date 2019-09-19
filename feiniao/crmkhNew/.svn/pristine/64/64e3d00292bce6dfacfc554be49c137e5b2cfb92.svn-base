package com.yunda.base.feiniao.schedule.suckdata.task.impls.changecooperateordernum;

import com.yunda.base.common.config.Constant;
import com.yunda.base.feiniao.customer.domain.ChangeCooperateOrderNumDO;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
import com.yunda.base.feiniao.report.utils.TaskBeanUtils;
import org.apache.log4j.Logger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 以达成合作的转化合作单量(抽取昨天的数据targetDay为-1)
 * @author beidouxing
 * @create 2019/03/29 13:54
 */
public class ChangeCooperateOrderNum extends ChangeCooperateOrderNumTaskAbs {
    Logger log = Logger.getLogger(getClass());

    @Override
    public int index() {
        return 3;
    }

    @Override
    public boolean preCheck(LogSuckdataService logSuckdataService, Date targetDay) {
        return true;
    }

    @Override
    public String realProcess(LogSuckdataService logSuckdataService, Date targetDay) {
        int result =0;
        //抽取昨天的数据(targetDay为-1)
        //拿着前天的客户编码去数据库里获取昨天的揽件量
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
        //昨天的日期
        String zuoTian = sdf.format(targetDay);
        //获取前天的时间
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-2);
        Date d=cal.getTime();

        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
        String qianTian=sp.format(d);//获取昨天日期
        //获取前天所有以达成合作的客户编码和时间
        //设置前天开始时间和前天结束时间(根据绑定vip账号时间和状态获取信息,并且客户编码不能为空)
        ChangeCooperateOrderNumDO changeCooperateOrderNum = new ChangeCooperateOrderNumDO();
        changeCooperateOrderNum.setUploadTime(qianTian);
        changeCooperateOrderNum.setTime(zuoTian);
        //已达成合作
        changeCooperateOrderNum.setState(Constant.SUCCESS_COOPERATE);
        List<ChangeCooperateOrderNumDO> changeCooperateOrderNumDOList =  TaskBeanUtils.getNotCooperateCustomerDao().getSuccessCooperateByDate(changeCooperateOrderNum);
        //拿到之后遍历查询揽件量
        for (ChangeCooperateOrderNumDO changeCooperateOrderNumDO : changeCooperateOrderNumDOList) {
             result += TaskBeanUtils.getNotCooperateCustomerDao().saveChangeCooperateOrderNumDO(changeCooperateOrderNumDO);
        }
            return "揽件量数据生成"+result!=null?result+"":0+"条";
    }


    @Override
    public void realClearTable(LogSuckdataService logSuckdataService, Date targetDay) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");//小写的mm表示的是分钟
        String dqday = sdf.format(targetDay);
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(dqday)){
            TaskBeanUtils.getNotCooperateCustomerDao().removeBdByDate(dqday);
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
