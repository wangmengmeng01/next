<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace com\vip\order\common\pojo\order\vo;

class OrderGoodsAndDescribeVO {
	
	static $_TSPEC;
	public $orderGoodsDescribeList = null;
	public $orderGoods = null;
	public $orderGoodsExtAttr = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'orderGoodsDescribeList'
			),
			2 => array(
			'var' => 'orderGoods'
			),
			3 => array(
			'var' => 'orderGoodsExtAttr'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['orderGoodsDescribeList'])){
				
				$this->orderGoodsDescribeList = $vals['orderGoodsDescribeList'];
			}
			
			
			if (isset($vals['orderGoods'])){
				
				$this->orderGoods = $vals['orderGoods'];
			}
			
			
			if (isset($vals['orderGoodsExtAttr'])){
				
				$this->orderGoodsExtAttr = $vals['orderGoodsExtAttr'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'OrderGoodsAndDescribeVO';
	}
	
	public function read($input){
		
		$input->readStructBegin();
		while(true){
			
			$schemeField = $input->readFieldBegin();
			if ($schemeField == null) break;
			$needSkip = true;
			
			
			if ("orderGoodsDescribeList" == $schemeField){
				
				$needSkip = false;
				
				$this->orderGoodsDescribeList = array();
				$_size1 = 0;
				$input->readListBegin();
				while(true){
					
					try{
						
						$elem1 = null;
						
						$elem1 = new \com\vip\order\common\pojo\order\vo\OrderGoodsDescribeVO();
						$elem1->read($input);
						
						$this->orderGoodsDescribeList[$_size1++] = $elem1;
					}
					catch(\Exception $e){
						
						break;
					}
				}
				
				$input->readListEnd();
				
			}
			
			
			
			
			if ("orderGoods" == $schemeField){
				
				$needSkip = false;
				
				$this->orderGoods = new \com\vip\order\common\pojo\order\vo\OrderGoodsVO();
				$this->orderGoods->read($input);
				
			}
			
			
			
			
			if ("orderGoodsExtAttr" == $schemeField){
				
				$needSkip = false;
				
				$this->orderGoodsExtAttr = new \com\vip\order\common\pojo\order\vo\OrderGoodsExtAttrVO();
				$this->orderGoodsExtAttr->read($input);
				
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
		
		if($this->orderGoodsDescribeList !== null) {
			
			$xfer += $output->writeFieldBegin('orderGoodsDescribeList');
			
			if (!is_array($this->orderGoodsDescribeList)){
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$output->writeListBegin();
			foreach ($this->orderGoodsDescribeList as $iter0){
				
				
				if (!is_object($iter0)) {
					
					throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
				}
				
				$xfer += $iter0->write($output);
				
			}
			
			$output->writeListEnd();
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->orderGoods !== null) {
			
			$xfer += $output->writeFieldBegin('orderGoods');
			
			if (!is_object($this->orderGoods)) {
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$xfer += $this->orderGoods->write($output);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->orderGoodsExtAttr !== null) {
			
			$xfer += $output->writeFieldBegin('orderGoodsExtAttr');
			
			if (!is_object($this->orderGoodsExtAttr)) {
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$xfer += $this->orderGoodsExtAttr->write($output);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		$xfer += $output->writeFieldStop();
		$xfer += $output->writeStructEnd();
		return $xfer;
	}
	
}

?>