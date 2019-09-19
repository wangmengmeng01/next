<?php
/**
 * TOP API: cainiao.nbadd.appointdeliver.modifyappointtime request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoNbaddAppointdeliverModifyappointtimeRequest
{
	/** 
	 * 预约的日期，格式如：2017-04-16
	 **/
	private $appointDay;
	
	/** 
	 * 预约的时间段，格式如：09:00-11:00
	 **/
	private $appointTimeInterval;
	
	/** 
	 * lpCode
	 **/
	private $lpCode;
	
	/** 
	 * 运单号
	 **/
	private $mailNo;
	
	/** 
	 * 订单号
	 **/
	private $orderId;
	
	/** 
	 * 小件员工号
	 **/
	private $userId;
	
	private $apiParas = array();
	
	public function setAppointDay($appointDay)
	{
		$this->appointDay = $appointDay;
		$this->apiParas["appoint_day"] = $appointDay;
	}

	public function getAppointDay()
	{
		return $this->appointDay;
	}

	public function setAppointTimeInterval($appointTimeInterval)
	{
		$this->appointTimeInterval = $appointTimeInterval;
		$this->apiParas["appoint_time_interval"] = $appointTimeInterval;
	}

	public function getAppointTimeInterval()
	{
		return $this->appointTimeInterval;
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

	public function setMailNo($mailNo)
	{
		$this->mailNo = $mailNo;
		$this->apiParas["mail_no"] = $mailNo;
	}

	public function getMailNo()
	{
		return $this->mailNo;
	}

	public function setOrderId($orderId)
	{
		$this->orderId = $orderId;
		$this->apiParas["order_id"] = $orderId;
	}

	public function getOrderId()
	{
		return $this->orderId;
	}

	public function setUserId($userId)
	{
		$this->userId = $userId;
		$this->apiParas["user_id"] = $userId;
	}

	public function getUserId()
	{
		return $this->userId;
	}

	public function getApiMethodName()
	{
		return "cainiao.nbadd.appointdeliver.modifyappointtime";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->appointTimeInterval,"appointTimeInterval");
		RequestCheckUtil::checkNotNull($this->lpCode,"lpCode");
		RequestCheckUtil::checkNotNull($this->mailNo,"mailNo");
		RequestCheckUtil::checkNotNull($this->orderId,"orderId");
		RequestCheckUtil::checkNotNull($this->userId,"userId");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
