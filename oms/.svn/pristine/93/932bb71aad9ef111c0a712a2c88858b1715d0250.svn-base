<?php
/**
 * TOP API: cainiao.member.courier.userstatus.get request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoMemberCourierUserstatusGetRequest
{
	/** 
	 * cpcode
	 **/
	private $cpCode;
	
	/** 
	 * cpuserid
	 **/
	private $cpUserid;
	
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

	public function setCpUserid($cpUserid)
	{
		$this->cpUserid = $cpUserid;
		$this->apiParas["cp_userid"] = $cpUserid;
	}

	public function getCpUserid()
	{
		return $this->cpUserid;
	}

	public function getApiMethodName()
	{
		return "cainiao.member.courier.userstatus.get";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
