<?php
/**
 * TOP API: cainiao.guoguo.cp.accepttask request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoCpAccepttaskRequest
{
	/** 
	 * cp统一编码
	 **/
	private $cpCode;
	
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
		return "cainiao.guoguo.cp.accepttask";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->cpCode,"cpCode");
		RequestCheckUtil::checkNotNull($this->taskGroup,"taskGroup");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
