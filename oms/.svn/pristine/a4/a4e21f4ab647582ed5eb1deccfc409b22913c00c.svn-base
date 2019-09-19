<?php
/**
 * TOP API: cainiao.guoguo.cp.signtaskbycourier request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoCpSigntaskbycourierRequest
{
	/** 
	 * CP统一编码
	 **/
	private $cpCode;
	
	/** 
	 * 小件员员工号
	 **/
	private $cpUserId;
	
	/** 
	 * 签收码
	 **/
	private $signCode;
	
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

	public function setCpUserId($cpUserId)
	{
		$this->cpUserId = $cpUserId;
		$this->apiParas["cp_user_id"] = $cpUserId;
	}

	public function getCpUserId()
	{
		return $this->cpUserId;
	}

	public function setSignCode($signCode)
	{
		$this->signCode = $signCode;
		$this->apiParas["sign_code"] = $signCode;
	}

	public function getSignCode()
	{
		return $this->signCode;
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
		return "cainiao.guoguo.cp.signtaskbycourier";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->cpCode,"cpCode");
		RequestCheckUtil::checkNotNull($this->cpUserId,"cpUserId");
		RequestCheckUtil::checkNotNull($this->signCode,"signCode");
		RequestCheckUtil::checkNotNull($this->taskId,"taskId");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
