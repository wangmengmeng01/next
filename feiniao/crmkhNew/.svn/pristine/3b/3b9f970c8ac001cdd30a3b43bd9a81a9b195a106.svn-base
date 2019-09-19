package com.yunda.base.common.task;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.yunda.base.common.config.Constant;
import com.yunda.base.common.enums.RoleEnum;
import com.yunda.base.common.utils.SpringUtil;
import com.yunda.base.feiniao.schedule.suckdata.vo.TaskParams;
import com.yunda.base.feiniao.warning.dao.WarningBenchmarkDao;
import com.yunda.base.feiniao.warning.dao.WarningBranchMobileDao;
import com.yunda.base.feiniao.warning.dao.WarningHandleDao;
import com.yunda.base.feiniao.warning.domain.WarningBenchmarkDO;
import com.yunda.base.feiniao.warning.domain.WarningBranchMobileDO;
import com.yunda.base.feiniao.warning.domain.WarningHandleDO;
import com.yunda.base.system.config.SysConfig;
import com.yunda.base.system.domain.LoginUserDO;
import com.yunda.base.system.service.LoginService;
import com.yunda.base.system.service.LoginUserService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
/**
 * 预警反馈表数据--短信提醒
 * @author admin
 *
 */
@DisallowConcurrentExecution
public class WarningMessageSendTask extends TaskAbs{

	Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void run(JobExecutionContext arg0) {
		WarningHandleDao warningHandleDao = SpringUtil.getBean(WarningHandleDao.class);
		WarningBenchmarkDao warningBenchmarkDao = SpringUtil.getBean(WarningBenchmarkDao.class);
		WarningBranchMobileDao warningBranchMobileDao = SpringUtil.getBean(WarningBranchMobileDao.class);
		LoginService loginService = SpringUtil.getBean(LoginService.class);
		LoginUserService loginUserService = SpringUtil.getBean(LoginUserService.class);
		StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
		
		TaskParams tp = TaskParams.newInstance(arg0);
		int from = tp.getTargetDay();
		String date = new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.getDate(from));
		
		Map<String, Object> provinceMap = new HashMap<String, Object>();
		provinceMap.put("warnDate", date);
		//预警基准设置的大区/省的客户类别基准  放缓存
		List<WarningBenchmarkDO> warningBenchmarkList = warningBenchmarkDao.list(provinceMap);
		ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
		if(warningBenchmarkList != null && warningBenchmarkList.size()>0){
			for(WarningBenchmarkDO warningBenchmarkDO :warningBenchmarkList){
				if(StringUtils.isNotEmpty(warningBenchmarkDO.getJgmc())){
					operations.set("crmkh_warningSMS_"+warningBenchmarkDO.getJgmc(), warningBenchmarkDO.getBenchmarkSetting(), 1, TimeUnit.HOURS);	
				}
			}
		}
		
		//provinceList需要的字段: 省名称  大区名
		List<WarningHandleDO> provinceList = warningHandleDao.provinceList(provinceMap);
		if(provinceList!=null && provinceList.size()>0){
			for(WarningHandleDO data : provinceList){
				try {
					if(data==null){continue;}
				if(StringUtils.isNotBlank(data.getProvincename())){
					// 2 %s日  =date
					
					//3 %s类用缓存的方式取
					String sz_lei = "";
					if(StringUtils.isNotEmpty(operations.get("crmkh_warningSMS_"+data.getProvincename()))){
						sz_lei = operations.get("crmkh_warningSMS_"+data.getProvincename());
					}else{
						sz_lei ="e";
					}
					//4  网点合计%s家（5  分别为**公司）     要的字段:该业务省下的所有网点id和名称(去重)
					provinceMap.put("provinceName", data.getProvincename());
					provinceMap.put("priceLevel", sz_lei);
					provinceMap.put("branchCode", "");    //循环遍历下一个省前  将前一次循环网点编码条件重置
					List<WarningHandleDO> sz_branchList = warningHandleDao.branchList(provinceMap);
					int sz_branchCount = sz_branchList.size();
					String sz_branchName ="";
					for (int i = 0; i < sz_branchList.size(); i++) {
						sz_branchName += sz_branchList.get(i).getBranchName()+",";
					}
					
					//6   客户数量%s 
					int sz_customer = warningHandleDao.customerList(provinceMap);
                    //System.out.println("~~~~~~~~~~~省~~~~~~~~~"+data.getProvincename()+sz_lei+sz_branchCount+sz_customer);
					provinceMap.put("institution", data.getProvincename());
					provinceMap.put("role", RoleEnum.SZ.getRolename());
					provinceMap.put("smsType", "2");
					List<LoginUserDO> sz_loginUser = loginUserService.list(provinceMap);//省总:%s   和手机号  
                    if (sz_loginUser != null && sz_loginUser.size() > 0) {
                    	String sz_warning_format = String.format(SysConfig.WARNING_TEMPLATE_SZ, sz_loginUser.get(0).getName(),date,sz_lei,sz_branchCount,sz_branchName,sz_customer);
                        String sz_messageFallback = loginService.sendMsm(sz_warning_format, sz_loginUser.get(0).getMobile());
                        parseMessage(sz_messageFallback, sz_loginUser.get(0).getMobile());
                    }

	                //网点短信   网点:%s，%s日,%s类,有%s个客户
	                for(WarningHandleDO wd : sz_branchList){
	                	try {
	                		if(wd == null){continue;}
	                	if(StringUtils.isNotEmpty(String.valueOf(wd.getBranchCode()))&&StringUtils.isNotEmpty(wd.getBranchName())){
	                		provinceMap.put("branchCode", wd.getBranchCode());
	                		int wd_customer = warningHandleDao.customerList(provinceMap);//该网点的客户数
	                		provinceMap.put("warningStatus", "running");//若网点已停用则不发短信
	                		List<WarningBranchMobileDO> wd_userList = warningBranchMobileDao.list(provinceMap);
	                		if(wd_userList !=null && wd_userList.size()>0){
	                			String wd_warning_format = String.format(SysConfig.WARNING_TEMPLATE_WD, wd_userList.get(0).getUserName(),date,sz_lei,wd_customer);
	                            String wd_messageFallback = loginService.sendMsm(wd_warning_format, wd_userList.get(0).getMobile());
	                            parseMessage(wd_messageFallback, wd_userList.get(0).getMobile());
	                		}else{
	                			log.warn("网点:"+data.getProvincename()+wd.getBranchCode()+",未获取到网点手机号,短信发送失败!");
	                		}
	                	}
	                	} catch (Exception e) {
	    					log.warn("网点:"+data.getProvincename()+wd.getBranchCode()+",网点短信发送失败!"+e);
	    				}
	                }
                
				}
				} catch (Exception e) {
					log.warn("省:"+data.getProvincename()+",省总短信发送失败!"+e);
				}
			}
		}
		
		//区总和区协助人:%s，%s日,%s类,有%s个省份%s个网点%s客户，（分别为%s省**个网点**客户），
		Map<String, Object> daquMap = new HashMap<String, Object>();
		daquMap.put("warnDate", date);
		List<WarningHandleDO> daquList = warningHandleDao.daquList(daquMap);
		if(daquList!=null && daquList.size()>0){
			for(WarningHandleDO daquData: daquList){
				try {
					if(daquData == null){continue;}
					if(StringUtils.isNotEmpty(daquData.getBigarea())){
					
					String daqu_lei = "";
					if(StringUtils.isNotEmpty(operations.get("crmkh_warningSMS_"+daquData.getBigarea()))){
						daqu_lei = operations.get("crmkh_warningSMS_"+daquData.getBigarea());
					}else{
						daqu_lei ="d";
					}
					
					//该大区有%s个省份%s个网点%s客户
					daquMap.put("bigarea", daquData.getBigarea());
					daquMap.put("priceLevel", daqu_lei);
					List<WarningHandleDO> daqu_provinceList = warningHandleDao.provinceList(daquMap);
					int daqu_provinceTotal = daqu_provinceList.size();
					List<WarningHandleDO> daqu_branchTotalList = warningHandleDao.branchList(daquMap);
					int daqu_branchTotal = daqu_branchTotalList.size();
					int daqu_customerTotal = warningHandleDao.customerList(daquMap);
					//System.out.println("~~~~~~~~~~~~~~~~~~~~~~"+daqu_branchTotal+"~~~~~~~~~~~~"+daqu_customerTotal);
					String areaTemplate="";
					for(WarningHandleDO provinceData:daqu_provinceList){
						if(StringUtils.isNotEmpty(provinceData.getProvincename())){
							daquMap.put("provinceName", provinceData.getProvincename());
							//（分别为%s省**个网点**客户）
							List<WarningHandleDO> sz_branchList = warningHandleDao.branchList(daquMap);
							int daqu_pro_branch = sz_branchList.size();
							int daqu_pro_customer = warningHandleDao.customerList(daquMap);
							
							areaTemplate += provinceData.getProvincename()+","+daqu_pro_branch+"个网点"+daqu_pro_customer+"客户;";
							daquMap.put("provinceName", "");//重置条件
						}
					}
					daquMap.put("role", RoleEnum.QZ.getRolename());
					daquMap.put("institution", daquData.getBigarea());
					daquMap.put("smsType","2");
                    List<LoginUserDO> daqu_loginUser = loginUserService.list(daquMap);//区总:%s   和手机号  
                    for(LoginUserDO data :daqu_loginUser){
                    	if(data == null){continue;}
                    	if(StringUtils.isNotBlank(data.getMobile())){
	                    	String daqu_warning_format = String.format(SysConfig.WARNING_TEMPLATE_QZ, data.getName(),date,daqu_lei,daqu_provinceTotal,daqu_branchTotal,daqu_customerTotal,areaTemplate);
	                        String daqu_messageFallback = loginService.sendMsm(daqu_warning_format, data.getMobile());
	                        parseMessage(daqu_messageFallback, data.getMobile());
                    	}
                    }
				}
				} catch (Exception e) {
					log.warn("区总:"+daquData.getBigarea()+",区总短信发送失败!"+e);
				}
			}
		}
		
		//总部:%s,您好，%s日,有%s个区%s个省%s个网点%s个客户，（分别为%s区%s省**个网点**客户）
		Map<String, Object> zbMap = new HashMap<String, Object>();
		zbMap.put("warnDate", date);
		zbMap.put("lastOrderAvg", SysConfig.WARNING_jizhun_ZB_double);
		List<WarningHandleDO> zbList = warningHandleDao.daquList(zbMap);
		int zb_count_daquTotal = zbList.size();
		List<WarningHandleDO> zb_provinceList = warningHandleDao.provinceList(zbMap);
		int zb_count_provinceTotal = zb_provinceList.size();
		List<WarningHandleDO> zb_branchTotalList = warningHandleDao.branchList(zbMap);
		int zb_count_branchTotal = zb_branchTotalList.size();
		int zb_count_customerTotal = warningHandleDao.customerList(zbMap);
		String zb_Template ="";
		
		if(zb_provinceList!=null && zb_provinceList.size()>0){
			for(WarningHandleDO zbProvinceData: zb_provinceList){
				if(StringUtils.isNotEmpty(zbProvinceData.getBigarea())){
					zbMap.put("provinceName", zbProvinceData.getProvincename());
					//（分别为%s省**个网点**客户）
					List<WarningHandleDO> zb_province_branchList = warningHandleDao.branchList(zbMap);
					int zb_pro_branch = zb_province_branchList.size();
					int zb_pro_customer = warningHandleDao.customerList(zbMap);
					
					zb_Template += zbProvinceData.getBigarea()+","+zbProvinceData.getProvincename()+","+zb_pro_branch+"个网点"+zb_pro_customer+"客户;";
					if(zb_Template.length()>=200){
						zb_Template +="......";
						break;
					}
				}
			}
			//总部:%s,您好，%s日,有%s个区%s个省%s个网点%s个客户，（分别为%s区%s省**个网点**客户）
			zbMap.put("role", RoleEnum.ZB.getRolename());
			zbMap.put("smsType","2");
	        List<LoginUserDO> zb_UserList = loginUserService.list(zbMap);
	        for (LoginUserDO zb_loginUserDO : zb_UserList) {
	            String zb_warning_Tem = String.format(SysConfig.WARNING_TEMPLATE_ZB, zb_loginUserDO.getName(), date,SysConfig.WARNING_jizhun_ZB, zb_count_daquTotal,zb_count_provinceTotal,zb_count_branchTotal,zb_count_customerTotal,zb_Template);
	            String zb_warning_Fallback = loginService.sendMsm(zb_warning_Tem, zb_loginUserDO.getMobile());
	            parseMessage(zb_warning_Fallback, zb_loginUserDO.getMobile());
	        }
		}
	}

    private void parseMessage(String message, String phone) {
        try {
            Document document = DocumentHelper.parseText(message);
            Element root = document.getRootElement();
            List<Element> elements = root.elements();
            for (Iterator<Element> it = elements.iterator(); it.hasNext(); ) {
                Element element = it.next();
                List<Attribute> attributes = element.attributes();
                for (int i = 0; i < attributes.size(); i++) {
                    Attribute attribute = attributes.get(i);
                    if (Constant.SMS_CODE.equals(attribute.getText())) {
                        // 1代表成功
                        log.info("手机号码:[{}],预警反馈短信发送成功!!", phone);
                    }
                }
                log.error("短信发送失败:" + phone + ",短信平台返回code:" + attributes.get(attributes.size() - 1).getText());
            }
        } catch (DocumentException e) {
            log.error("xml解析异常");
        }
    }
	
	@Override
	public String whoareyou() {
		return "预警反馈表数据--短信提醒";
	}

}
