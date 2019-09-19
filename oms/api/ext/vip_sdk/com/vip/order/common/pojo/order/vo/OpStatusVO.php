<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace com\vip\order\common\pojo\order\vo;

class OpStatusVO {
	
	static $_TSPEC;
	public $op = null;
	public $opFlag = null;
	public $message = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'op'
			),
			2 => array(
			'var' => 'opFlag'
			),
			3 => array(
			'var' => 'message'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['op'])){
				
				$this->op = $vals['op'];
			}
			
			
			if (isset($vals['opFlag'])){
				
				$this->opFlag = $vals['opFlag'];
			}
			
			
			if (isset($vals['message'])){
				
				$this->message = $vals['message'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'OpStatusVO';
	}
	
	public function read($input){
		
		$input->readStructBegin();
		while(true){
			
			$schemeField = $input->readFieldBegin();
			if ($schemeField == null) break;
			$needSkip = true;
			
			
			if ("op" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->op);
				
			}
			
			
			
			
			if ("opFlag" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->opFlag); 
				
			}
			
			
			
			
			if ("message" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->message);
				
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
		
		if($this->op !== null) {
			
			$xfer += $output->writeFieldBegin('op');
			$xfer += $output->writeString($this->op);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->opFlag !== null) {
			
			$xfer += $output->writeFieldBegin('opFlag');
			$xfer += $output->writeI32($this->opFlag);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->message !== null) {
			
			$xfer += $output->writeFieldBegin('message');
			$xfer += $output->writeString($this->message);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		$xfer += $output->writeFieldStop();
		$xfer += $output->writeStructEnd();
		return $xfer;
	}
	
}

?>