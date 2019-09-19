<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace com\vip\order\biz\response;

class CancelOrderResp {
	
	static $_TSPEC;
	public $result = null;
	public $returnBrandCouponStatus = null;
	public $returnType = null;
	public $previewInfo = null;
	public $linkageCancelOrderInfoList = null;
	public $createCancelApplyFlag = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'result'
			),
			2 => array(
			'var' => 'returnBrandCouponStatus'
			),
			3 => array(
			'var' => 'returnType'
			),
			4 => array(
			'var' => 'previewInfo'
			),
			5 => array(
			'var' => 'linkageCancelOrderInfoList'
			),
			6 => array(
			'var' => 'createCancelApplyFlag'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['result'])){
				
				$this->result = $vals['result'];
			}
			
			
			if (isset($vals['returnBrandCouponStatus'])){
				
				$this->returnBrandCouponStatus = $vals['returnBrandCouponStatus'];
			}
			
			
			if (isset($vals['returnType'])){
				
				$this->returnType = $vals['returnType'];
			}
			
			
			if (isset($vals['previewInfo'])){
				
				$this->previewInfo = $vals['previewInfo'];
			}
			
			
			if (isset($vals['linkageCancelOrderInfoList'])){
				
				$this->linkageCancelOrderInfoList = $vals['linkageCancelOrderInfoList'];
			}
			
			
			if (isset($vals['createCancelApplyFlag'])){
				
				$this->createCancelApplyFlag = $vals['createCancelApplyFlag'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'CancelOrderResp';
	}
	
	public function read($input){
		
		$input->readStructBegin();
		while(true){
			
			$schemeField = $input->readFieldBegin();
			if ($schemeField == null) break;
			$needSkip = true;
			
			
			if ("result" == $schemeField){
				
				$needSkip = false;
				
				$this->result = new \com\vip\order\common\pojo\order\result\Result();
				$this->result->read($input);
				
			}
			
			
			
			
			if ("returnBrandCouponStatus" == $schemeField){
				
				$needSkip = false;
				$input->readByte($this->returnBrandCouponStatus); 
				
			}
			
			
			
			
			if ("returnType" == $schemeField){
				
				$needSkip = false;
				$input->readByte($this->returnType); 
				
			}
			
			
			
			
			if ("previewInfo" == $schemeField){
				
				$needSkip = false;
				
				$this->previewInfo = new \com\vip\order\biz\response\PreviewInfoResp();
				$this->previewInfo->read($input);
				
			}
			
			
			
			
			if ("linkageCancelOrderInfoList" == $schemeField){
				
				$needSkip = false;
				
				$this->linkageCancelOrderInfoList = array();
				$_size0 = 0;
				$input->readListBegin();
				while(true){
					
					try{
						
						$elem0 = null;
						
						$elem0 = new \com\vip\order\biz\response\LinkageCancelOrderInfo();
						$elem0->read($input);
						
						$this->linkageCancelOrderInfoList[$_size0++] = $elem0;
					}
					catch(\Exception $e){
						
						break;
					}
				}
				
				$input->readListEnd();
				
			}
			
			
			
			
			if ("createCancelApplyFlag" == $schemeField){
				
				$needSkip = false;
				$input->readByte($this->createCancelApplyFlag); 
				
			}
			
			
			
			if($needSkip){
				
				\Osp\Protocol\ProtocolUtil::skip($input);
			}
			
			$input->readFieldEnd();
		}
		
		$input->readStructEnd();
		
		
		
	}
	
	public function write($output){
		
		$xfer = 0;
		$xfer += $output->writeStructBegin();
		
		if($this->result !== null) {
			
			$xfer += $output->writeFieldBegin('result');
			
			if (!is_object($this->result)) {
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$xfer += $this->result->write($output);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->returnBrandCouponStatus !== null) {
			
			$xfer += $output->writeFieldBegin('returnBrandCouponStatus');
			$xfer += $output->writeByte($this->returnBrandCouponStatus);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->returnType !== null) {
			
			$xfer += $output->writeFieldBegin('returnType');
			$xfer += $output->writeByte($this->returnType);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->previewInfo !== null) {
			
			$xfer += $output->writeFieldBegin('previewInfo');
			
			if (!is_object($this->previewInfo)) {
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$xfer += $this->previewInfo->write($output);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->linkageCancelOrderInfoList !== null) {
			
			$xfer += $output->writeFieldBegin('linkageCancelOrderInfoList');
			
			if (!is_array($this->linkageCancelOrderInfoList)){
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$output->writeListBegin();
			foreach ($this->linkageCancelOrderInfoList as $iter0){
				
				
				if (!is_object($iter0)) {
					
					throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
				}
				
				$xfer += $iter0->write($output);
				
			}
			
			$output->writeListEnd();
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->createCancelApplyFlag !== null) {
			
			$xfer += $output->writeFieldBegin('createCancelApplyFlag');
			$xfer += $output->writeByte($this->createCancelApplyFlag);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		$xfer += $output->writeFieldStop();
		$xfer += $output->writeStructEnd();
		return $xfer;
	}
	
}

?>