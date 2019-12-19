package com.yundagalaxy.management.commnon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yundagalaxy.common.constant.CodeConstant;
import com.yundagalaxy.management.commnon.utils.RedisManUtil;
import com.yundagalaxy.management.entity.DepartmentInfo;
import com.yundagalaxy.management.entity.DepartmentJob;
import com.yundagalaxy.management.entity.EmployeeBasicInfo;
import com.yundagalaxy.management.entity.TmpEmployeeInfo;
import com.yundagalaxy.management.service.IDepartmentInfoService;
import com.yundagalaxy.management.service.IDepartmentJobService;
import com.yundagalaxy.management.service.IEmployeeBasicInfoService;
import com.yundagalaxy.management.service.ITmpEmployeeInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springblade.core.log.logger.BladeLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.yundagalaxy.common.constant.CodeConstant.*;

@Service
public class DistributedLock {
    @Autowired
    private BladeLogger bladeLogger;
    /**
     * redis 服务
     * demo 项目没有加redis相关，若有需要请参考，redis的博客
     */
    @Autowired
    private RedisManUtil redisManUtil;
    @Autowired
    private IDepartmentInfoService departmentInfoService;
    @Autowired
    private IDepartmentJobService departmentJobService;
    @Autowired
    private IEmployeeBasicInfoService employeeBasicInfoService;
    @Autowired
    private ITmpEmployeeInfoService tmpEmployeeInfoService;


    /**
     *
     *系统初始化code值到redis
     * JD:部门编码(示例:JD000000001)
     * JJ:岗位编码(示例:JJ00000001)
     * JE:员工编码(示例:JE00000001)
     * LE:非员工编码(示例:LE00000001)
     * @param
     * @return
     */
    @PostConstruct
    public void initCurrent(){

        String[] strArray={
                PRE_GROUP_DPMENT_CODE,
                PRE_GROUP_JOB_CODE,
                PRE_GROUP_EMP_CODE,
                PRE_GROUP_TMP_EMP_CODE
        };
        for (String s : strArray) {
            switch (s){
                case PRE_GROUP_DPMENT_CODE:
                    initCurrentCode(s,CodeConstant.INIT_NUMBER,CodeConstant.REDIS_MAX_DPMENT_CODE_KEY);
                    break;
                case PRE_GROUP_JOB_CODE:
                    initCurrentCode(s,CodeConstant.INIT_NUMBER,CodeConstant.REDIS_MAX_JOB_CODE_KEY);
                    break;
                case PRE_GROUP_EMP_CODE:
                    initCurrentCode(s,CodeConstant.INIT_NUMBER,CodeConstant.REDIS_MAX_EMP_CODE_KEY);
                    break;
                case PRE_GROUP_TMP_EMP_CODE:
                    initCurrentCode(s,CodeConstant.INIT_NUMBER,CodeConstant.REDIS_MAX_TMP_EMP_CODE_KEY);
                    break;
                default:
                    break;
            }
        }

    }
    /**
     * 自定义分页
     *
     * @param prefix 编号前缀
     * @param initCode 初始化值
     * @param redisKey redis key值
     * @return
     */


    public synchronized void initCurrentCode(String prefix,String initCode,String redisKey ){

        TimeUnit timeUnit = TimeUnit.SECONDS;
        long start = System.nanoTime();
        do{
//            String lockValue = String.valueOf(new Date().getTime());
            String lockValue = String.valueOf(new Date().getTime()+5*1000);
            boolean lockFlag = redisManUtil.lock("initCurrentCode", lockValue);
            //获取锁
            if(lockFlag){
                //1、设置有效期，防止当前锁异常或崩溃导致锁释放失败
                redisManUtil.expire("initCurrentCode", 5);
                String currentMaxCode ="";
                switch (prefix){
                    case PRE_GROUP_DPMENT_CODE:
                        //部门表最大编码查询
                        currentMaxCode = departmentInfoService.getMaxDpmentCode();
                        break;
                    case PRE_GROUP_JOB_CODE:
                        //岗位表最大编码查询
                        currentMaxCode = departmentJobService.getMaxJobCode();
                        break;
                    case PRE_GROUP_EMP_CODE:
                        //员工主表最大编码查询
                        currentMaxCode = employeeBasicInfoService.getMaxEmpCode();
                        break;
                    case PRE_GROUP_TMP_EMP_CODE:
                        //非员工主表最大编码查询
                        currentMaxCode = tmpEmployeeInfoService.getMaxTmpEmpCode();
                        break;
                    default:
                        break;

                }

                //如果为空，则设置为初始值
                if(StringUtils.isBlank(currentMaxCode)){
                    currentMaxCode = prefix+initCode;
                }
                redisManUtil.set(redisKey, currentMaxCode,0);
                //5、释放锁，redis执行删除方法
                redisManUtil.unlock("initCurrentCode",lockValue);
                break;
                //未获取锁
            }else if(!lockFlag){
                System.out.println(Thread.currentThread().getName()+"=====未获取锁,未超时将进入循环");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    bladeLogger.error("initCurrentCode()=>方法未获得锁：",e.getMessage());
                }
            }
            //如果未超时，则循环获取锁
        }while(System.nanoTime()-start<timeUnit.toNanos(5));


    }

    /**
     * @Description :获取最大编码值，当前服务被部署多套，采用：synchronized+redis分布式锁 形式共同完成
     * @param timeOut 循环获取最大值超时时长
     * @param lockKey 锁key
     * @param lockTime 锁时间
     * @param redisKey redis key
     * @param timeUnit 超时单位
     * @return String
     */
    public synchronized String getNewCodeMax(String prefix,String lockKey,int lockTime,String redisKey,long timeOut,TimeUnit timeUnit){
        String newMaxValue = null;
        if(timeUnit == null){
            timeUnit = TimeUnit.SECONDS;
        }
        long start = System.nanoTime();
        do{
//            String lockValue = String.valueOf(new Date().getTime());
            String lockValue = String.valueOf(new Date().getTime()+5*1000);
            boolean lockFlag = redisManUtil.lock(lockKey, lockValue);
            //获取锁
            if(lockFlag){
                //1、设置有效期，防止当前锁异常或崩溃导致锁释放失败
                redisManUtil.expire(lockKey, lockTime);
                //2、获取当前最大编码值
                String currentMaxValue = (String) redisManUtil.get(redisKey);
                //如果redis中该值丢失，重新执行初始化
                if(StringUtils.isBlank(currentMaxValue)){
                    initCurrentCode(prefix,CodeConstant.INIT_NUMBER,redisKey);
                    currentMaxValue = (String)redisManUtil.get(redisKey);
                }
                //3、将最大值加1，获取新的最大值
                String cc = currentMaxValue.substring(2);
                Long currentMaxNum = Long.parseLong (cc);
                Long nn = currentMaxNum + 1 ;
                //不足9位左边补0
                newMaxValue = prefix + String.format("%09d", nn);
                //生成的值重新查询数据库判断是否存在
                Boolean res = findExistValue(prefix,newMaxValue);
                if(res){
                    currentMaxValue = (String)redisManUtil.get(redisKey);
                    //3、将最大值加1，获取新的最大值
                    cc = currentMaxValue.substring(2);
                    currentMaxNum = Long.parseLong (cc);
                    nn = currentMaxNum + 1 ;
                    //不足9位左边补0
                    newMaxValue = prefix + String.format("%09d", nn);
                }
                //4、将新的最大值同步到redis缓存
                redisManUtil.set(redisKey, newMaxValue,0);
                //5、释放锁，redis执行删除方法
                redisManUtil.unlock(lockKey,lockValue);
                break;
                //未获取锁
            }else if(!lockFlag){
                System.out.println(Thread.currentThread().getName()+"=====未获取锁,未超时将进入循环");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    bladeLogger.error("getNewCodeMax()=>方法未获得锁：",e.getMessage());
                }
            }
            //如果未超时，则循环获取锁
        }while(System.nanoTime()-start<timeUnit.toNanos(timeOut));

        return newMaxValue;
    }

    /**
     * @Description : 查询重复如果重复初始化最大值
     * @param prefix 前缀
     * @param currentMaxCode 最大值
     * @return Boolean
     */
    public Boolean findExistValue(String prefix,String currentMaxCode){

        //初始化获取数据库中最大编码值
        Integer num = null;
        switch (prefix){
            case PRE_GROUP_DPMENT_CODE:
                //部门表
                num = departmentInfoService.count(new QueryWrapper<DepartmentInfo>().eq("dpment_code",currentMaxCode));
                break;
            case PRE_GROUP_JOB_CODE:
                //岗位表
                num = departmentJobService.count(new QueryWrapper<DepartmentJob>().eq("job_code",currentMaxCode));
                break;
            case PRE_GROUP_EMP_CODE:
                //员工主表
                num = employeeBasicInfoService.count(new QueryWrapper<EmployeeBasicInfo>().eq("emp_code",currentMaxCode));
                break;
            case PRE_GROUP_TMP_EMP_CODE:
                //非员工主表
                num = tmpEmployeeInfoService.count(new QueryWrapper<TmpEmployeeInfo>().eq("tmp_emp_code",currentMaxCode));
                break;
            default:
                break;

        }
        if(num!=null&&num!=0){
            initCurrent();
            return true;
        }else{
            return false;
        }
    }


}