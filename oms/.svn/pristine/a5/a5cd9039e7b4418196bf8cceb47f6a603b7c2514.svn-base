<?php
/**
 * TOP API: cainiao.guoguo.cp.graborder.cporderreturnsettleservice request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoCpGraborderCporderreturnsettleserviceRequest
{
	/** 
	 * 重量
	 **/
	private $goodsWeight;
	
	/** 
	 * 外部订单号
	 **/
	private $lpCode;
	
	/** 
	 * 运输cp编码
	 **/
	private $mailCpCode;
	
	/** 
	 * cp价格
	 **/
	private $mailCpPrice;
	
	/** 
	 * 运单号
	 **/
	private $mailNo;
	
	/** 
	 * 回单时间
	 **/
	private $mailNoDate;
	
	/** 
	 * 收件人省份
	 **/
	private $receiveProvice;
	
	/** 
	 * 寄件人市
	 **/
	private $sendCity;
	
	/** 
	 * 揽件cp编码
	 **/
	private $takePackgeCpCode;
	
	private $apiParas = array();
	
	public function setGoodsWeight($goodsWeight)
	{
		$this->goodsWeight = $goodsWeight;
		$this->apiParas["goods_weight"] = $goodsWeight;
	}

	public function getGoodsWeight()
	{
		return $this->goodsWeight;
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

	public function setMailCpPrice($mailCpPrice)
	{
		$this->mailCpPrice = $mailCpPrice;
		$this->apiParas["mail_cp_price"] = $mailCpPrice;
	}

	public function getMailCpPrice()
	{
		return $this->mailCpPrice;
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

	public function setMailNoDate($mailNoDate)
	{
		$this->mailNoDate = $mailNoDate;
		$this->apiParas["mail_no_date"] = $mailNoDate;
	}

	public function getMailNoDate()
	{
		return $this->mailNoDate;
	}

	public function setReceiveProvice($receiveProvice)
	{
		$this->receiveProvice = $receiveProvice;
		$this->apiParas["receive_provice"] = $receiveProvice;
	}

	public function getReceiveProvice()
	{
		return $this->receiveProvice;
	}

	public function setSendCity($sendCity)
	{
		$this->sendCity = $sendCity;
		$this->apiParas["send_city"] = $sendCity;
	}

	public function getSendCity()
	{
		return $this->sendCity;
	}

	public function setTakePackgeCpCode($takePackgeCpCode)
	{
		$this->takePackgeCpCode = $takePackgeCpCode;
		$this->apiParas["take_packge_cp_code"] = $takePackgeCpCode;
	}

	public function getTakePackgeCpCode()
	{
		return $this->takePackgeCpCode;
	}

	public function getApiMethodName()
	{
		return "cainiao.guoguo.cp.graborder.cporderreturnsettleservice";
	}
	
	public function getApiParas()
	{
		return $this->apiParas;
	}
	
	public function check()
	{
		
		RequestCheckUtil::checkNotNull($this->goodsWeight,"goodsWeight");
		RequestCheckUtil::checkNotNull($this->lpCode,"lpCode");
		RequestCheckUtil::checkNotNull($this->mailCpCode,"mailCpCode");
		RequestCheckUtil::checkNotNull($this->mailCpPrice,"mailCpPrice");
		RequestCheckUtil::checkNotNull($this->mailNo,"mailNo");
		RequestCheckUtil::checkNotNull($this->mailNoDate,"mailNoDate");
		RequestCheckUtil::checkNotNull($this->receiveProvice,"receiveProvice");
		RequestCheckUtil::checkNotNull($this->sendCity,"sendCity");
		RequestCheckUtil::checkNotNull($this->takePackgeCpCode,"takePackgeCpCode");
	}
	
	public function putOtherTextParam($key, $value) {
		$this->apiParas[$key] = $value;
		$this->$key = $value;
	}
}
