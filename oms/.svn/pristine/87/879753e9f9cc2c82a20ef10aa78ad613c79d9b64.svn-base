<?php
/**
 * TOP API: cainiao.member.courier.cpresign request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoMemberCourierCpresignRequest
{
	/** 
	 * 菜鸟用户id
	 **/
	private $accountId;
	
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

	public function getApiMethodName()
	{
		return "cainiao.member.courier.cpresign";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->accountId,"accountId");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
