<?php
/**
 * TOP API: cainiao.guoguo.cp.gottaskbycouriermobile request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoCpGottaskbycouriermobileRequest
{
	/** 
	 * 根据价格方案计算出的价格
	 **/
	private $calPrice;
	
	/** 
	 * 小件员所在公司编号
	 **/
	private $cpCode;
	
	/** 
	 * 揽件码
	 **/
	private $gotCode;
	
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
	
	/** 
	 * 货物重量，单位：公斤
	 **/
	private $weight;
	
	private $apiParas = array();
	
	public function setCalPrice($calPrice)
	{
		$this->calPrice = $calPrice;
		$this->apiParas["cal_price"] = $calPrice;
	}

	public function getCalPrice()
	{
		return $this->calPrice;
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

	public function setGotCode($gotCode)
	{
		$this->gotCode = $gotCode;
		$this->apiParas["got_code"] = $gotCode;
	}

	public function getGotCode()
	{
		return $this->gotCode;
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

	public function setWeight($weight)
	{
		$this->weight = $weight;
		$this->apiParas["weight"] = $weight;
	}

	public function getWeight()
	{
		return $this->weight;
	}

	public function getApiMethodName()
	{
		return "cainiao.guoguo.cp.gottaskbycouriermobile";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->cpCode,"cpCode");
		RequestCheckUtil::checkNotNull($this->gotCode,"gotCode");
		RequestCheckUtil::checkNotNull($this->mobile,"mobile");
		RequestCheckUtil::checkNotNull($this->taskId,"taskId");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
