<?php
/**
 * TOP API: cainiao.guoguo.cp.cancelbycourier request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoCpCancelbycourierRequest
{
	/** 
	 * 取消原因
	 **/
	private $cancelReason;
	
	/** 
	 * 取消原因编码
	 **/
	private $cancelReasonCode;
	
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
	 * 任务ID
	 **/
	private $taskId;
	
	private $apiParas = array();
	
	public function setCancelReason($cancelReason)
	{
		$this->cancelReason = $cancelReason;
		$this->apiParas["cancel_reason"] = $cancelReason;
	}

	public function getCancelReason()
	{
		return $this->cancelReason;
	}

	public function setCancelReasonCode($cancelReasonCode)
	{
		$this->cancelReasonCode = $cancelReasonCode;
		$this->apiParas["cancel_reason_code"] = $cancelReasonCode;
	}

	public function getCancelReasonCode()
	{
		return $this->cancelReasonCode;
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
		return "cainiao.guoguo.cp.cancelbycourier";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->cancelReason,"cancelReason");
		RequestCheckUtil::checkNotNull($this->cpCode,"cpCode");
		RequestCheckUtil::checkNotNull($this->cpUserId,"cpUserId");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
