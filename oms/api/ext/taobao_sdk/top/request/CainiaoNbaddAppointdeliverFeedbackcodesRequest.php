<?php
/**
 * TOP API: cainiao.nbadd.appointdeliver.feedbackcodes request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoNbaddAppointdeliverFeedbackcodesRequest
{
	
	private $apiParas = array();
	
	public function getApiMethodName()
	{
		return "cainiao.nbadd.appointdeliver.feedbackcodes";
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
