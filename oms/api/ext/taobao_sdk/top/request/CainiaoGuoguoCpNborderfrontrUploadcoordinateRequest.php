<?php
/**
 * TOP API: cainiao.guoguo.cp.nborderfrontr.uploadcoordinate request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoCpNborderfrontrUploadcoordinateRequest
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
	 * 0 安卓定位，     1 苹果定位，  2 其他系统定位，   10 高德定位，  11 百度定位，  12 google定位     13 其他
	 **/
	private $gpsType;
	
	/** 
	 * 纬度
	 **/
	private $lat;
	
	/** 
	 * 经度
	 **/
	private $lng;
	
	/** 
	 * 来源：1.小件员app sdk 2.驿站 3. 裹裹 10001.圆通行者
	 **/
	private $source;
	
	/** 
	 * 上报时间，格式：yyyy-MM-dd HH:mm:ss
	 **/
	private $timeStamp;
	
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

	public function setGpsType($gpsType)
	{
		$this->gpsType = $gpsType;
		$this->apiParas["gps_type"] = $gpsType;
	}

	public function getGpsType()
	{
		return $this->gpsType;
	}

	public function setLat($lat)
	{
		$this->lat = $lat;
		$this->apiParas["lat"] = $lat;
	}

	public function getLat()
	{
		return $this->lat;
	}

	public function setLng($lng)
	{
		$this->lng = $lng;
		$this->apiParas["lng"] = $lng;
	}

	public function getLng()
	{
		return $this->lng;
	}

	public function setSource($source)
	{
		$this->source = $source;
		$this->apiParas["source"] = $source;
	}

	public function getSource()
	{
		return $this->source;
	}

	public function setTimeStamp($timeStamp)
	{
		$this->timeStamp = $timeStamp;
		$this->apiParas["time_stamp"] = $timeStamp;
	}

	public function getTimeStamp()
	{
		return $this->timeStamp;
	}

	public function getApiMethodName()
	{
		return "cainiao.guoguo.cp.nborderfrontr.uploadcoordinate";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->cpCode,"cpCode");
		RequestCheckUtil::checkNotNull($this->cpUserId,"cpUserId");
		RequestCheckUtil::checkNotNull($this->gpsType,"gpsType");
		RequestCheckUtil::checkNotNull($this->lat,"lat");
		RequestCheckUtil::checkNotNull($this->lng,"lng");
		RequestCheckUtil::checkNotNull($this->source,"source");
		RequestCheckUtil::checkNotNull($this->timeStamp,"timeStamp");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
