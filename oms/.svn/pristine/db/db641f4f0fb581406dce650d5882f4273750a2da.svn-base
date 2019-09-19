<?php
/**
 * TOP API: cainiao.guoguo.task.updatemobile request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoTaskUpdatemobileRequest
{
	/** 
	 * cp编码
	 **/
	private $cpCode;
	
	/** 
	 * cp用户id
	 **/
	private $cpUserId;
	
	/** 
	 * 更新的手机号
	 **/
	private $newPhone;
	
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

	public function setNewPhone($newPhone)
	{
		$this->newPhone = $newPhone;
		$this->apiParas["new_phone"] = $newPhone;
	}

	public function getNewPhone()
	{
		return $this->newPhone;
	}

	public function getApiMethodName()
	{
		return "cainiao.guoguo.task.updatemobile";
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
