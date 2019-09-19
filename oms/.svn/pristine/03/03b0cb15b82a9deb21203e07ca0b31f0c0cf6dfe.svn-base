<?php
/**
 * TOP API: cainiao.guoguo.cp.grasptaskbycourier request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoCpGrasptaskbycourierRequest
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
	 * 抢单任务组编号
	 **/
	private $groupTask;
	
	/** 
	 * 任务组内任务数量
	 **/
	private $groupTaskSize;
	
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

	public function setGroupTask($groupTask)
	{
		$this->groupTask = $groupTask;
		$this->apiParas["group_task"] = $groupTask;
	}

	public function getGroupTask()
	{
		return $this->groupTask;
	}

	public function setGroupTaskSize($groupTaskSize)
	{
		$this->groupTaskSize = $groupTaskSize;
		$this->apiParas["group_task_size"] = $groupTaskSize;
	}

	public function getGroupTaskSize()
	{
		return $this->groupTaskSize;
	}

	public function getApiMethodName()
	{
		return "cainiao.guoguo.cp.grasptaskbycourier";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->cpCode,"cpCode");
		RequestCheckUtil::checkNotNull($this->cpUserId,"cpUserId");
		RequestCheckUtil::checkNotNull($this->groupTask,"groupTask");
		RequestCheckUtil::checkNotNull($this->groupTaskSize,"groupTaskSize");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
