<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace com\vip\order\biz\response;

class GetConsigneeRelatedOrderResp {
	
	static $_TSPEC;
	public $result = null;
	public $canModify = null;
	public $consigneeRelatedOrderList = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'result'
			),
			2 => array(
			'var' => 'canModify'
			),
			3 => array(
			'var' => 'consigneeRelatedOrderList'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['result'])){
				
				$this->result = $vals['result'];
			}
			
			
			if (isset($vals['canModify'])){
				
				$this->canModify = $vals['canModify'];
			}
			
			
			if (isset($vals['consigneeRelatedOrderList'])){
				
				$this->consigneeRelatedOrderList = $vals['consigneeRelatedOrderList'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'GetConsigneeRelatedOrderResp';
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
			
			
			
			
			if ("canModify" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->canModify); 
				
			}
			
			
			
			
			if ("consigneeRelatedOrderList" == $schemeField){
				
				$needSkip = false;
				
				$this->consigneeRelatedOrderList = array();
				$_size1 = 0;
				$input->readListBegin();
				while(true){
					
					try{
						
						$elem1 = null;
						
						$elem1 = new \com\vip\order\biz\response\ConsigneeRelatedOrderVO();
						$elem1->read($input);
						
						$this->consigneeRelatedOrderList[$_size1++] = $elem1;
					}
					catch(\Exception $e){
						
						break;
					}
				}
				
				$input->readListEnd();
				
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
		
		
		if($this->canModify !== null) {
			
			$xfer += $output->writeFieldBegin('canModify');
			$xfer += $output->writeI32($this->canModify);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->consigneeRelatedOrderList !== null) {
			
			$xfer += $output->writeFieldBegin('consigneeRelatedOrderList');
			
			if (!is_array($this->consigneeRelatedOrderList)){
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$output->writeListBegin();
			foreach ($this->consigneeRelatedOrderList as $iter0){
				
				
				if (!is_object($iter0)) {
					
					throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
				}
				
				$xfer += $iter0->write($output);
				
			}
			
			$output->writeListEnd();
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		$xfer += $output->writeFieldStop();
		$xfer += $output->writeStructEnd();
		return $xfer;
	}
	
}

?>