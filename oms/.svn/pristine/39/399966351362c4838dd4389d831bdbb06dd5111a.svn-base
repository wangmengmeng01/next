<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace com\vip\order\biz\request;

class BatchModifyOrderInvoiceReqV2 {
	
	static $_TSPEC;
	public $modifyOrderInvoiceReqVOList = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'modifyOrderInvoiceReqVOList'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['modifyOrderInvoiceReqVOList'])){
				
				$this->modifyOrderInvoiceReqVOList = $vals['modifyOrderInvoiceReqVOList'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'BatchModifyOrderInvoiceReqV2';
	}
	
	public function read($input){
		
		$input->readStructBegin();
		while(true){
			
			$schemeField = $input->readFieldBegin();
			if ($schemeField == null) break;
			$needSkip = true;
			
			
			if ("modifyOrderInvoiceReqVOList" == $schemeField){
				
				$needSkip = false;
				
				$this->modifyOrderInvoiceReqVOList = array();
				$_size0 = 0;
				$input->readListBegin();
				while(true){
					
					try{
						
						$elem0 = null;
						
						$elem0 = new \com\vip\order\common\pojo\order\vo\ModifyOrderInvoiceReqVO();
						$elem0->read($input);
						
						$this->modifyOrderInvoiceReqVOList[$_size0++] = $elem0;
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
		
		if($this->modifyOrderInvoiceReqVOList !== null) {
			
			$xfer += $output->writeFieldBegin('modifyOrderInvoiceReqVOList');
			
			if (!is_array($this->modifyOrderInvoiceReqVOList)){
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$output->writeListBegin();
			foreach ($this->modifyOrderInvoiceReqVOList as $iter0){
				
				
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