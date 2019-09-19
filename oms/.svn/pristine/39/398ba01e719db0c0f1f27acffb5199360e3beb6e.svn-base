<?php
/**
 * TOP API: cainiao.guoguo.cp.backup.assigncourierbyid request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoCpBackupAssigncourierbyidRequest
{
	/** 
	 * 小件员菜鸟账号ID
	 **/
	private $accountId;
	
	/** 
	 * 指派/改派原因
	 **/
	private $assignReason;
	
	/** 
	 * 指派/改派原因编码
	 **/
	private $assignReasonCode;
	
	/** 
	 * CP公司编号
	 **/
	private $cpCode;
	
	/** 
	 * 任务编号
	 **/
	private $taskId;
	
	private $apiParas = array();
	
	public function setAccountId($accountId)
	{
		$this->accountId = $accountId;
		$this->apiParas["account_id"] = $accountId;
	}

	public function getAccountId()
	{
		return $this->accountId;
	}

	public function setAssignReason($assignReason)
	{
		$this->assignReason = $assignReason;
		$this->apiParas["assign_reason"] = $assignReason;
	}

	public function getAssignReason()
	{
		return $this->assignReason;
	}

	public function setAssignReasonCode($assignReasonCode)
	{
		$this->assignReasonCode = $assignReasonCode;
		$this->apiParas["assign_reason_code"] = $assignReasonCode;
	}

	public function getAssignReasonCode()
	{
		return $this->assignReasonCode;
	}

	public function setCpCode($cpCode)
	{
		$this->cpCode = $cpCode;
		$this->apiParas["cp_code"] = $cpCode;
	}

	public function getCpCode()
	{
		return $this->cpCode;
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
		return "cainiao.guoguo.cp.backup.assigncourierbyid";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->accountId,"accountId");
		RequestCheckUtil::checkNotNull($this->cpCode,"cpCode");
		RequestCheckUtil::checkNotNull($this->taskId,"taskId");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
