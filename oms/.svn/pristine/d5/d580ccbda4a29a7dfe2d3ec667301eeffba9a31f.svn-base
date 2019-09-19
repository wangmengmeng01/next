<?php
/**
 * TOP API: cainiao.guoguo.gongyi.data.upload request
 * 
 * @author auto create
 * @since 1.0, 2018.07.25
 */
class CainiaoGuoguoGongyiDataUploadRequest
{
	/** 
	 * 请求参数实体
	 **/
	private $ngoDataUploadRequest;
	
	private $apiParas = array();
	
	public function setNgoDataUploadRequest($ngoDataUploadRequest)
	{
		$this->ngoDataUploadRequest = $ngoDataUploadRequest;
		$this->apiParas["ngo_data_upload_request"] = $ngoDataUploadRequest;
	}

	public function getNgoDataUploadRequest()
	{
		return $this->ngoDataUploadRequest;
	}

	public function getApiMethodName()
	{
		return "cainiao.guoguo.gongyi.data.upload";
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
