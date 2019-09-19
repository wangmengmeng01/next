<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace com\vip\order\common\pojo\order\vo;

class CalculateSplitOrderMoneyGrayVO {
	
	static $_TSPEC;
	public $jitOxoMixSelect = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'jitOxoMixSelect'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['jitOxoMixSelect'])){
				
				$this->jitOxoMixSelect = $vals['jitOxoMixSelect'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'CalculateSplitOrderMoneyGrayVO';
	}
	
	public function read($input){
		
		$input->readStructBegin();
		while(true){
			
			$schemeField = $input->readFieldBegin();
			if ($schemeField == null) break;
			$needSkip = true;
			
			
			if ("jitOxoMixSelect" == $schemeField){
				
				$needSkip = false;
				$input->readBool($this->jitOxoMixSelect);
				
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
		
		if($this->jitOxoMixSelect !== null) {
			
			$xfer += $output->writeFieldBegin('jitOxoMixSelect');
			$xfer += $output->writeBool($this->jitOxoMixSelect);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		$xfer += $output->writeFieldStop();
		$xfer += $output->writeStructEnd();
		return $xfer;
	}
	
}

?>