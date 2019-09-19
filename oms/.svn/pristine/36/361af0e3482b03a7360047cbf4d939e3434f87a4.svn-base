<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace com\vip\order\biz\request;

class CancelOrderApplyingReq {
	
	static $_TSPEC;
	public $applySn = null;
	public $cancelOrderReq = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'applySn'
			),
			2 => array(
			'var' => 'cancelOrderReq'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['applySn'])){
				
				$this->applySn = $vals['applySn'];
			}
			
			
			if (isset($vals['cancelOrderReq'])){
				
				$this->cancelOrderReq = $vals['cancelOrderReq'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'CancelOrderApplyingReq';
	}
	
	public function read($input){
		
		$input->readStructBegin();
		while(true){
			
			$schemeField = $input->readFieldBegin();
			if ($schemeField == null) break;
			$needSkip = true;
			
			
			if ("applySn" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->applySn);
				
			}
			
			
			
			
			if ("cancelOrderReq" == $schemeField){
				
				$needSkip = false;
				
				$this->cancelOrderReq = new \com\vip\order\biz\request\CancelOrderReq();
				$this->cancelOrderReq->read($input);
				
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
		
		if($this->applySn !== null) {
			
			$xfer += $output->writeFieldBegin('applySn');
			$xfer += $output->writeString($this->applySn);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->cancelOrderReq !== null) {
			
			$xfer += $output->writeFieldBegin('cancelOrderReq');
			
			if (!is_object($this->cancelOrderReq)) {
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$xfer += $this->cancelOrderReq->write($output);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		$xfer += $output->writeFieldStop();
		$xfer += $output->writeStructEnd();
		return $xfer;
	}
	
}

?>