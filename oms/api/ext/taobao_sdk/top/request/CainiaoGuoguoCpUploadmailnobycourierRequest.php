<?php
/**
 * TOP API: cainiao.guoguo.cp.uploadmailnobycourier request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoCpUploadmailnobycourierRequest
{
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
	 * 运单号所属的CP公司编号
	 **/
	private $mailCpCode;
	
	/** 
	 * 运单号
	 **/
	private $mailno;
	
	/** 
	 * 任务ID
	 **/
	private $taskId;
	
	/** 
	 * 电子面单编码
	 **/
	private $waybillCode;
	
	/** 
	 * 电子面单地址
	 **/
	private $waybillShortAddress;
	
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

	public function setLpCode($lpCode)
	{
		$this->lpCode = $lpCode;
		$this->apiParas["lp_code"] = $lpCode;
	}

	public function getLpCode()
	{
		return $this->lpCode;
	}

	public function setMailCpCode($mailCpCode)
	{
		$this->mailCpCode = $mailCpCode;
		$this->apiParas["mail_cp_code"] = $mailCpCode;
	}

	public function getMailCpCode()
	{
		return $this->mailCpCode;
	}

	public function setMailno($mailno)
	{
		$this->mailno = $mailno;
		$this->apiParas["mailno"] = $mailno;
	}

	public function getMailno()
	{
		return $this->mailno;
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

	public function setWaybillCode($waybillCode)
	{
		$this->waybillCode = $waybillCode;
		$this->apiParas["waybill_code"] = $waybillCode;
	}

	public function getWaybillCode()
	{
		return $this->waybillCode;
	}

	public function setWaybillShortAddress($waybillShortAddress)
	{
		$this->waybillShortAddress = $waybillShortAddress;
		$this->apiParas["waybill_short_address"] = $waybillShortAddress;
	}

	public function getWaybillShortAddress()
	{
		return $this->waybillShortAddress;
	}

	public function getApiMethodName()
	{
		return "cainiao.guoguo.cp.uploadmailnobycourier";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->cpCode,"cpCode");
		RequestCheckUtil::checkNotNull($this->cpUserId,"cpUserId");
		RequestCheckUtil::checkNotNull($this->mailno,"mailno");
		RequestCheckUtil::checkNotNull($this->taskId,"taskId");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
