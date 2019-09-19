package com.yunda.base.feiniao.log.service.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.yunda.base.common.utils.R;
import com.yunda.base.feiniao.log.dao.LogSuckdataDao;
import com.yunda.base.feiniao.log.domain.LogSuckdataDO;
import com.yunda.base.feiniao.log.enums.EventTypeEnum;
import com.yunda.base.feiniao.log.service.LogSuckdataService;
@Service
public class LogSuckdataServiceImpl implements LogSuckdataService {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private LogSuckdataDao logSuckdataDao;
	@Autowired
	SimpMessagingTemplate messagingTemplate;

	@Override
	public LogSuckdataDO get(Integer id) {
		return logSuckdataDao.get(id);
	}

	@Override
	public List<LogSuckdataDO> list(Map<String, Object> map) {
		return logSuckdataDao.list(map);
	}

	@Override
	public int count(Map<String, Object> map) {
		return logSuckdataDao.count(map);
	}

	@Override
	public int save(LogSuckdataDO logSuckdata) {
		log.info(logSuckdata.getLogInfo());

		// 抽数日志信息触发的其他事件
		if (logSuckdata.getEventType() == EventTypeEnum.event_sms.getCode()) {
			// TODO
		} else if (logSuckdata.getEventType() == EventTypeEnum.event_mail.getCode()) {
			// TODO 邮件通知

		} else if (logSuckdata.getEventType() == EventTypeEnum.event_websocket.getCode()) {
			// websocket通知
			messagingTemplate.convertAndSend("/topic/getResponse", R.ok(logSuckdata.getLogInfo()));

		} else if (logSuckdata.getEventType() == EventTypeEnum.event_all.getCode()) {
			// TODO 邮件通知

			// websocket通知
			messagingTemplate.convertAndSend("/topic/getResponse", R.ok(logSuckdata.getLogInfo()));
		}

		return logSuckdataDao.save(logSuckdata);
	}

	@Override
	public int update(LogSuckdataDO logSuckdata) {
		return logSuckdataDao.update(logSuckdata);
	}

	@Override
	public int remove(Integer id) {
		return logSuckdataDao.remove(id);
	}

	@Override
	public int batchRemove(Integer[] ids) {
		return logSuckdataDao.batchRemove(ids);
	}

}
