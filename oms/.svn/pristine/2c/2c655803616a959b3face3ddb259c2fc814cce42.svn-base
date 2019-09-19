<?php
/**
 * TOP API: cainiao.guoguo.task.uploadbizexception request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoTaskUploadbizexceptionRequest
{
	/** 
	 * 异常信息code
	 **/
	private $exceptionCode;
	
	/** 
	 * 任务id
	 **/
	private $taskId;
	
	private $apiParas = array();
	
	public function setExceptionCode($exceptionCode)
	{
		$this->exceptionCode = $exceptionCode;
		$this->apiParas["exception_code"] = $exceptionCode;
	}

	public function getExceptionCode()
	{
		return $this->exceptionCode;
	}

	public function setTaskId($taskId)
	{
		$this->taskId = $taskId;
		$this->apiParas["task_id"] = $taskId;
	}

	public function getTaskId()
	{
		return $this->taskId;
	}

	public function getApiMethodName()
	{
		return "cainiao.guoguo.task.uploadbizexception";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->exceptionCode,"exceptionCode");
		RequestCheckUtil::checkNotNull($this->taskId,"taskId");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
