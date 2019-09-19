<?php
/**
 * TOP API: cainiao.guoguo.cp.rejecttask request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoCpRejecttaskRequest
{
	/** 
	 * cp编号
	 **/
	private $cpCode;
	
	/** 
	 * 拒单原因
	 **/
	private $reason;
	
	/** 
	 * 任务组编号
	 **/
	private $taskGroup;
	
	private $apiParas = array();
	
	public function setCpCode($cpCode)
	{
		$this->cpCode = $cpCode;
		$this->apiParas["cp_code"] = $cpCode;
	}

	public function getCpCode()
	{
		return $this->cpCode;
	}

	public function setReason($reason)
	{
		$this->reason = $reason;
		$this->apiParas["reason"] = $reason;
	}

	public function getReason()
	{
		return $this->reason;
	}

	public function setTaskGroup($taskGroup)
	{
		$this->taskGroup = $taskGroup;
		$this->apiParas["task_group"] = $taskGroup;
	}

	public function getTaskGroup()
	{
		return $this->taskGroup;
	}

	public function getApiMethodName()
	{
		return "cainiao.guoguo.cp.rejecttask";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->cpCode,"cpCode");
		RequestCheckUtil::checkNotNull($this->reason,"reason");
		RequestCheckUtil::checkNotNull($this->taskGroup,"taskGroup");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
