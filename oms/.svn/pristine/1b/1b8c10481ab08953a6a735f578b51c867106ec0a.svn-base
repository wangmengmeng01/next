<?php
/**
 * TOP API: cainiao.guoguo.cp.validatecancelbycp request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoCpValidatecancelbycpRequest
{
	/** 
	 * CP公司编号
	 **/
	private $cpCode;
	
	/** 
	 * LP订单号
	 **/
	private $lpCode;
	
	/** 
	 * 任务ID
	 **/
	private $taskId;
	
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

	public function setLpCode($lpCode)
	{
		$this->lpCode = $lpCode;
		$this->apiParas["lp_code"] = $lpCode;
	}

	public function getLpCode()
	{
		return $this->lpCode;
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
		return "cainiao.guoguo.cp.validatecancelbycp";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->cpCode,"cpCode");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
