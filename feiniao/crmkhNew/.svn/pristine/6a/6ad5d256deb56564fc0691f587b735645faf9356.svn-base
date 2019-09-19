package com.yunda.base.system.service.impl;

import com.yunda.base.system.dao.LoginLogDao;
import com.yunda.base.system.domain.AlarmDO;
import com.yunda.base.system.domain.LoginLogDO;
import com.yunda.base.system.service.AlarmService;
import com.yunda.base.system.service.LoginLogService;
import com.yunda.ydmbspringbootstarter.common.utils.DateUtils;
import com.yunda.ydmbspringbootstarter.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class LoginLogServiceImpl implements LoginLogService {
	@Autowired
	private LoginLogDao loginLogDao;

	@Autowired
	private AlarmService alarmService;
	
	@Override
	public LoginLogDO get(Long id){
		return loginLogDao.get(id);
	}
	
	@Override
	public List<LoginLogDO> list(Map<String, Object> map){
		List<LoginLogDO> listLoginLogDO = loginLogDao.list(map);
		for (LoginLogDO loginLogDO : listLoginLogDO) {
			if (StringUtils.isNotEmpty(loginLogDO.getIdcdNo())) {
				StringBuilder sb = new StringBuilder(loginLogDO.getIdcdNo());
				sb.replace(6, 14, "********");
				loginLogDO.setIdcdNo(sb.toString());
			}
			String startTime = loginLogDO.getCreateTime();
			String endTime = loginLogDO.getOutTime();
			//登陆时长
			if(StringUtils.isNotEmpty(startTime)&&StringUtils.isNotEmpty(endTime)){
				String time = DateUtils.dateDiff(startTime, endTime, "yyyy-MM-dd hh:mm:ss");
				loginLogDO.setTime(time);
			}
			//根据查询出的对象的sessionId获取是否报警和报警类型,有就设置为是,null就设置为否
			AlarmDO alarmDO = alarmService.get(loginLogDO.getSessionId());
		}
		return listLoginLogDO;
	}
	
	@Override
	public int count(Map<String, Object> map){
		return loginLogDao.count(map);
	}
	
	@Override
	public int save(LoginLogDO loginLog){
		return loginLogDao.save(loginLog);
	}
	
	@Override
	public int update(LoginLogDO loginLog){
		return loginLogDao.update(loginLog);
	}
	
	@Override
	public int remove(Long id){
		return loginLogDao.remove(id);
	}
	
	@Override
	public int batchRemove(Long[] ids){
		return loginLogDao.batchRemove(ids);
	}

	@Override
	public String queryPictureBySessionId(String sessionId) {
		return loginLogDao.queryPictureBySessionId(sessionId);
	}

}
