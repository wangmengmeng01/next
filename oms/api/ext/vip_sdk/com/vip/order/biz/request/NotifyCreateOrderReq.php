<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace com\vip\order\biz\request;

class NotifyCreateOrderReq {
	
	static $_TSPEC;
	public $orderCreateorderDataSyncVOList = null;
	public $orderCategory = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'orderCreateorderDataSyncVOList'
			),
			2 => array(
			'var' => 'orderCategory'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['orderCreateorderDataSyncVOList'])){
				
				$this->orderCreateorderDataSyncVOList = $vals['orderCreateorderDataSyncVOList'];
			}
			
			
			if (isset($vals['orderCategory'])){
				
				$this->orderCategory = $vals['orderCategory'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'NotifyCreateOrderReq';
	}
	
	public function read($input){
		
		$input->readStructBegin();
		while(true){
			
			$schemeField = $input->readFieldBegin();
			if ($schemeField == null) break;
			$needSkip = true;
			
			
			if ("orderCreateorderDataSyncVOList" == $schemeField){
				
				$needSkip = false;
				
				$this->orderCreateorderDataSyncVOList = array();
				$_size1 = 0;
				$input->readListBegin();
				while(true){
					
					try{
						
						$elem1 = null;
						
						$elem1 = new \com\vip\order\common\pojo\order\vo\OrderCreateorderDataSyncVO();
						$elem1->read($input);
						
						$this->orderCreateorderDataSyncVOList[$_size1++] = $elem1;
					}
					catch(\Exception $e){
						
						break;
					}
				}
				
				$input->readListEnd();
				
			}
			
			
			
			
			if ("orderCategory" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->orderCategory); 
				
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
		
		if($this->orderCreateorderDataSyncVOList !== null) {
			
			$xfer += $output->writeFieldBegin('orderCreateorderDataSyncVOList');
			
			if (!is_array($this->orderCreateorderDataSyncVOList)){
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$output->writeListBegin();
			foreach ($this->orderCreateorderDataSyncVOList as $iter0){
				
				
				if (!is_object($iter0)) {
					
					throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
				}
				
				$xfer += $iter0->write($output);
				
			}
			
			$output->writeListEnd();
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		$xfer += $output->writeFieldBegin('orderCategory');
		$xfer += $output->writeI32($this->orderCategory);
		
		$xfer += $output->writeFieldEnd();
		
		$xfer += $output->writeFieldStop();
		$xfer += $output->writeStructEnd();
		return $xfer;
	}
	
}

?>