package com.yunda.base.feiniao.log.domain;

import com.github.crab2died.annotation.ExcelField;
import com.yunda.base.feiniao.log.enums.EventTypeEnum;
import com.yunda.base.feiniao.log.enums.LogSuckTypeEnum;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-07-15 21:56:56
 */
public class LogSuckdataDO implements Serializable {
	private static final long serialVersionUID = 1L;

	// id
	@ExcelField(title = "id", order = 1)
	private Integer id;
	// creat_time
	@ExcelField(title = "creat_time", order = 2)
	private Date creatTime = new Timestamp(System.currentTimeMillis());
	// log_info
	@ExcelField(title = "log_info", order = 3)
	private String logInfo;
	// log_type 抽数类型 参看LogSuckTypeEnum
	@ExcelField(title = "log_type", order = 4)
	private Integer logType = 0;
	// event_type 事件类型，参看EventTypeEnum
	@ExcelField(title = "event_type", order = 5)
	private Integer eventType = 0;

	public String getShowEventType() {
		return EventTypeEnum.getName(eventType);
	}

	public String getShowLogType() {
		return LogSuckTypeEnum.getName(logType);
	}

	public LogSuckdataDO() {

	}

	public LogSuckdataDO(int logType, String logInfo) {
		this.logType = logType;
		this.logInfo = logInfo;
	}

	public LogSuckdataDO(int logType, int eventType, String logInfo) {
		this.logType = logType;
		this.logInfo = logInfo;
		this.eventType = eventType;
	}

	public LogSuckdataDO(String logInfo) {
		this.logInfo = logInfo;
	}

	/**
	 * 设置：id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取：id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置：creat_time
	 */
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	/**
	 * 获取：creat_time
	 */
	public Date getCreatTime() {
		return creatTime;
	}

	/**
	 * 设置：log_info
	 */
	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	/**
	 * 获取：log_info
	 */
	public String getLogInfo() {
		return logInfo;
	}

	/**
	 * 设置：log_type
	 */
	public void setLogType(Integer logType) {
		this.logType = logType;
	}

	/**
	 * 获取：log_type
	 */
	public Integer getLogType() {
		return logType;
	}

	/**
	 * 设置：event_type
	 */
	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	/**
	 * 获取：event_type
	 */
	public Integer getEventType() {
		return eventType;
	}
}
