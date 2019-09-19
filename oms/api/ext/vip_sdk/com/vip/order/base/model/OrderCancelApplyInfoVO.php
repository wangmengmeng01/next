<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace com\vip\order\base\model;

class OrderCancelApplyInfoVO {
	
	static $_TSPEC;
	public $orderCancelApplyVO = null;
	public $orderCancelProgressVOs = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'orderCancelApplyVO'
			),
			2 => array(
			'var' => 'orderCancelProgressVOs'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['orderCancelApplyVO'])){
				
				$this->orderCancelApplyVO = $vals['orderCancelApplyVO'];
			}
			
			
			if (isset($vals['orderCancelProgressVOs'])){
				
				$this->orderCancelProgressVOs = $vals['orderCancelProgressVOs'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'OrderCancelApplyInfoVO';
	}
	
	public function read($input){
		
		$input->readStructBegin();
		while(true){
			
			$schemeField = $input->readFieldBegin();
			if ($schemeField == null) break;
			$needSkip = true;
			
			
			if ("orderCancelApplyVO" == $schemeField){
				
				$needSkip = false;
				
				$this->orderCancelApplyVO = new \com\vip\order\common\pojo\order\vo\OrderCancelApplyVO();
				$this->orderCancelApplyVO->read($input);
				
			}
			
			
			
			
			if ("orderCancelProgressVOs" == $schemeField){
				
				$needSkip = false;
				
				$this->orderCancelProgressVOs = array();
				$_size0 = 0;
				$input->readListBegin();
				while(true){
					
					try{
						
						$elem0 = null;
						
						$elem0 = new \com\vip\order\common\pojo\order\vo\OrderCancelProgressVO();
						$elem0->read($input);
						
						$this->orderCancelProgressVOs[$_size0++] = $elem0;
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
		
		if($this->orderCancelApplyVO !== null) {
			
			$xfer += $output->writeFieldBegin('orderCancelApplyVO');
			
			if (!is_object($this->orderCancelApplyVO)) {
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$xfer += $this->orderCancelApplyVO->write($output);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->orderCancelProgressVOs !== null) {
			
			$xfer += $output->writeFieldBegin('orderCancelProgressVOs');
			
			if (!is_array($this->orderCancelProgressVOs)){
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$output->writeListBegin();
			foreach ($this->orderCancelProgressVOs as $iter0){
				
				
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