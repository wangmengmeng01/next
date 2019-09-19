<?php
/**
 * TOP API: cainiao.guoguo.task.courierinfo.grasptask request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoTaskCourierinfoGrasptaskRequest
{
	/** 
	 * cp公司名称
	 **/
	private $courierCompanyName;
	
	/** 
	 * 小件员姓名
	 **/
	private $courierName;
	
	/** 
	 * 小件员电话
	 **/
	private $courierPhone;
	
	/** 
	 * cp编码
	 **/
	private $cpCode;
	
	/** 
	 * cp小件员id
	 **/
	private $cpUserId;
	
	/** 
	 * 纬度
	 **/
	private $latitude;
	
	/** 
	 * 经度
	 **/
	private $longitude;
	
	/** 
	 * 任务组id
	 **/
	private $taskGroup;
	
	private $apiParas = array();
	
	public function setCourierCompanyName($courierCompanyName)
	{
		$this->courierCompanyName = $courierCompanyName;
		$this->apiParas["courier_company_name"] = $courierCompanyName;
	}

	public function getCourierCompanyName()
	{
		return $this->courierCompanyName;
	}

	public function setCourierName($courierName)
	{
		$this->courierName = $courierName;
		$this->apiParas["courier_name"] = $courierName;
	}

	public function getCourierName()
	{
		return $this->courierName;
	}

	public function setCourierPhone($courierPhone)
	{
		$this->courierPhone = $courierPhone;
		$this->apiParas["courier_phone"] = $courierPhone;
	}

	public function getCourierPhone()
	{
		return $this->courierPhone;
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

	public function setLatitude($latitude)
	{
		$this->latitude = $latitude;
		$this->apiParas["latitude"] = $latitude;
	}

	public function getLatitude()
	{
		return $this->latitude;
	}

	public function setLongitude($longitude)
	{
		$this->longitude = $longitude;
		$this->apiParas["longitude"] = $longitude;
	}

	public function getLongitude()
	{
		return $this->longitude;
	}

	public function setTaskGroup($taskGroup)
	{
		$this->taskGroup = $taskGroup;
		$this->apiParas["task_group"] = $taskGroup;
	}

	public function getTaskGroup()
	{
		return $this->taskGroup;
	}

	public function getApiMethodName()
	{
		return "cainiao.guoguo.task.courierinfo.grasptask";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->courierCompanyName,"courierCompanyName");
		RequestCheckUtil::checkNotNull($this->courierName,"courierName");
		RequestCheckUtil::checkNotNull($this->courierPhone,"courierPhone");
		RequestCheckUtil::checkNotNull($this->cpCode,"cpCode");
		RequestCheckUtil::checkNotNull($this->cpUserId,"cpUserId");
		RequestCheckUtil::checkNotNull($this->latitude,"latitude");
		RequestCheckUtil::checkNotNull($this->longitude,"longitude");
		RequestCheckUtil::checkNotNull($this->taskGroup,"taskGroup");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
