<?php
/**
 * TOP API: cainiao.guoguo.cp.backup.assigncourier request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoCpBackupAssigncourierRequest
{
	/** 
	 * 指派/改派原因
	 **/
	private $assignReason;
	
	/** 
	 * 指派/改派原因编码
	 **/
	private $assignReasonCode;
	
	/** 
	 * 小件员所在公司编号
	 **/
	private $cpCode;
	
	/** 
	 * 小件员员工编号
	 **/
	private $cpUserId;
	
	/** 
	 * LP订单号
	 **/
	private $lpCode;
	
	/** 
	 * 小件员手机号
	 **/
	private $mobile;
	
	/** 
	 * 任务ID
	 **/
	private $taskId;
	
	private $apiParas = array();
	
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

	public function setCpUserId($cpUserId)
	{
		$this->cpUserId = $cpUserId;
		$this->apiParas["cp_user_id"] = $cpUserId;
	}

	public function getCpUserId()
	{
		return $this->cpUserId;
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

	public function setMobile($mobile)
	{
		$this->mobile = $mobile;
		$this->apiParas["mobile"] = $mobile;
	}

	public function getMobile()
	{
		return $this->mobile;
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
		return "cainiao.guoguo.cp.backup.assigncourier";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->cpCode,"cpCode");
		RequestCheckUtil::checkNotNull($this->cpUserId,"cpUserId");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
