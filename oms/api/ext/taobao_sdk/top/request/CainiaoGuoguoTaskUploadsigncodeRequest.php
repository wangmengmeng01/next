<?php
/**
 * TOP API: cainiao.guoguo.task.uploadsigncode request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoTaskUploadsigncodeRequest
{
	/** 
	 * 签收码
	 **/
	private $deliveryCode;
	
	/** 
	 * 任务id
	 **/
	private $taskId;
	
	private $apiParas = array();
	
	public function setDeliveryCode($deliveryCode)
	{
		$this->deliveryCode = $deliveryCode;
		$this->apiParas["delivery_code"] = $deliveryCode;
	}

	public function getDeliveryCode()
	{
		return $this->deliveryCode;
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

	public function getApiMethodName()
	{
		return "cainiao.guoguo.task.uploadsigncode";
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
