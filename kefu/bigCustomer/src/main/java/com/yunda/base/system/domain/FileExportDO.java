package com.yunda.base.system.domain;

import java.io.Serializable;
import java.util.Date;

import com.github.crab2died.annotation.ExcelField;


/**
 * 
 * 
 * @author yunda
 * @email zhanghan813@163.com
 * @date 2018-12-28193801
 */
public class FileExportDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//序号
	@ExcelField(title = "序号", order = 1)
	private Long id;
	//用户编号
	@ExcelField(title = "用户编号", order = 2)
	private String userId;
	//创建时间
	@ExcelField(title = "创建时间", order = 3)
	private Date createTime;
	//start_time
	@ExcelField(title = "start_time", order = 4)
	private Date startTime;
	//结束时间
	@ExcelField(title = "结束时间", order = 5)
	private Date endTime;
	//hand_count
	@ExcelField(title = "hand_count", order = 6)
	private String handCount;
	//执行类
	@ExcelField(title = "执行类", order = 7)
	private String executeClass;
	//执行方法
	@ExcelField(title = "执行方法", order = 8)
	private String executeMethod;
	//喂参参数
	@ExcelField(title = "喂参参数", order = 9)
	private String executeParam;
	//domain对象
	@ExcelField(title = "domain对象", order = 10)
	private String specClass;
	//标题
	@ExcelField(title = "标题", order = 11)
	private String title;
	//"1":"等待中","2":"处理中","3":"执行完成","4":"执行失败","5":"无数据";
	@ExcelField(title = "状态", order = 12)
	private String state;
	//文件路径
	@ExcelField(title = "文件路径", order = 13)
	private String filePath;

 

	/**
	 * 设置：序号
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：序号
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：用户编号
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户编号
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：start_time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：start_time
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：结束时间
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * 设置：hand_count
	 */
	public void setHandCount(String handCount) {
		this.handCount = handCount;
	}
	/**
	 * 获取：hand_count
	 */
	public String getHandCount() {
		return handCount;
	}
	/**
	 * 设置：执行类
	 */
	public void setExecuteClass(String executeClass) {
		this.executeClass = executeClass;
	}
	/**
	 * 获取：执行类
	 */
	public String getExecuteClass() {
		return executeClass;
	}
	/**
	 * 设置：执行方法
	 */
	public void setExecuteMethod(String executeMethod) {
		this.executeMethod = executeMethod;
	}
	/**
	 * 获取：执行方法
	 */
	public String getExecuteMethod() {
		return executeMethod;
	}
	/**
	 * 设置：喂参参数
	 */
	public void setExecuteParam(String executeParam) {
		this.executeParam = executeParam;
	}
	/**
	 * 获取：喂参参数
	 */
	public String getExecuteParam() {
		return executeParam;
	}
	/**
	 * 设置：domain对象
	 */
	public void setSpecClass(String specClass) {
		this.specClass = specClass;
	}
	/**
	 * 获取：domain对象
	 */
	public String getSpecClass() {
		return specClass;
	}
	/**
	 * 设置：标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * 获取：标题
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * 设置："1":"等待中","2":"处理中","3":"执行完成","4":"执行失败","5":"无数据";
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * 获取："1":"等待中","2":"处理中","3":"执行完成","4":"执行失败","5":"无数据";
	 */
	public String getState() {
		return state;
	}
	/**
	 * 设置：文件路径
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * 获取：文件路径
	 */
	public String getFilePath() {
		return filePath;
	}
}
