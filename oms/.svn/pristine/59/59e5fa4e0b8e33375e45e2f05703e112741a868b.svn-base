<?php
/**
 * TOP API: cainiao.guoguo.cp.nborderfrontr.updateuser request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoCpNborderfrontrUpdateuserRequest
{
	/** 
	 * 支付宝账号
	 **/
	private $alipayAccount;
	
	/** 
	 * 城市行政区域编码
	 **/
	private $cityCode;
	
	/** 
	 * 城市
	 **/
	private $cityName;
	
	/** 
	 * 小件员所在公司编号
	 **/
	private $cpCode;
	
	/** 
	 * 小件员员工编号
	 **/
	private $cpUserId;
	
	/** 
	 * 手机号
	 **/
	private $mobile;
	
	/** 
	 * 姓名
	 **/
	private $name;
	
	/** 
	 * 网点站点编码
	 **/
	private $workStationCode;
	
	/** 
	 * 网点站点信息
	 **/
	private $workStationName;
	
	private $apiParas = array();
	
	public function setAlipayAccount($alipayAccount)
	{
		$this->alipayAccount = $alipayAccount;
		$this->apiParas["alipay_account"] = $alipayAccount;
	}

	public function getAlipayAccount()
	{
		return $this->alipayAccount;
	}

	public function setCityCode($cityCode)
	{
		$this->cityCode = $cityCode;
		$this->apiParas["city_code"] = $cityCode;
	}

	public function getCityCode()
	{
		return $this->cityCode;
	}

	public function setCityName($cityName)
	{
		$this->cityName = $cityName;
		$this->apiParas["city_name"] = $cityName;
	}

	public function getCityName()
	{
		return $this->cityName;
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

	public function setMobile($mobile)
	{
		$this->mobile = $mobile;
		$this->apiParas["mobile"] = $mobile;
	}

	public function getMobile()
	{
		return $this->mobile;
	}

	public function setName($name)
	{
		$this->name = $name;
		$this->apiParas["name"] = $name;
	}

	public function getName()
	{
		return $this->name;
	}

	public function setWorkStationCode($workStationCode)
	{
		$this->workStationCode = $workStationCode;
		$this->apiParas["work_station_code"] = $workStationCode;
	}

	public function getWorkStationCode()
	{
		return $this->workStationCode;
	}

	public function setWorkStationName($workStationName)
	{
		$this->workStationName = $workStationName;
		$this->apiParas["work_station_name"] = $workStationName;
	}

	public function getWorkStationName()
	{
		return $this->workStationName;
	}

	public function getApiMethodName()
	{
		return "cainiao.guoguo.cp.nborderfrontr.updateuser";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->cpCode,"cpCode");
		RequestCheckUtil::checkNotNull($this->cpUserId,"cpUserId");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
