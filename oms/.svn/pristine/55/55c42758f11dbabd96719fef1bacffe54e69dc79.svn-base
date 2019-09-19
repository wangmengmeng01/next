<?php
/**
 * TOP API: cainiao.guoguo.task.gotbydirectsendingcourier request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoTaskGotbydirectsendingcourierRequest
{
	/** 
	 * 揽件码
	 **/
	private $gotCode;
	
	/** 
	 * 任务id
	 **/
	private $taskId;
	
	private $apiParas = array();
	
	public function setGotCode($gotCode)
	{
		$this->gotCode = $gotCode;
		$this->apiParas["got_code"] = $gotCode;
	}

	public function getGotCode()
	{
		return $this->gotCode;
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
		return "cainiao.guoguo.task.gotbydirectsendingcourier";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
