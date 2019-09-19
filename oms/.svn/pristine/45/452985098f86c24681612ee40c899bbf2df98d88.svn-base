<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace com\vip\order\biz\request;

class SearchOrderListByUserIdReq {
	
	static $_TSPEC;
	public $userId = null;
	public $keyword = null;
	public $vipclubList = null;
	public $saleType = null;
	public $typeRange = null;
	public $orderModelList = null;
	public $statusRange = null;
	public $subStatusRange = null;
	public $payStatus = null;
	public $isDisplay = null;
	public $orderSourceTypeRange = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'userId'
			),
			2 => array(
			'var' => 'keyword'
			),
			3 => array(
			'var' => 'vipclubList'
			),
			4 => array(
			'var' => 'saleType'
			),
			5 => array(
			'var' => 'typeRange'
			),
			6 => array(
			'var' => 'orderModelList'
			),
			7 => array(
			'var' => 'statusRange'
			),
			8 => array(
			'var' => 'subStatusRange'
			),
			9 => array(
			'var' => 'payStatus'
			),
			10 => array(
			'var' => 'isDisplay'
			),
			11 => array(
			'var' => 'orderSourceTypeRange'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['userId'])){
				
				$this->userId = $vals['userId'];
			}
			
			
			if (isset($vals['keyword'])){
				
				$this->keyword = $vals['keyword'];
			}
			
			
			if (isset($vals['vipclubList'])){
				
				$this->vipclubList = $vals['vipclubList'];
			}
			
			
			if (isset($vals['saleType'])){
				
				$this->saleType = $vals['saleType'];
			}
			
			
			if (isset($vals['typeRange'])){
				
				$this->typeRange = $vals['typeRange'];
			}
			
			
			if (isset($vals['orderModelList'])){
				
				$this->orderModelList = $vals['orderModelList'];
			}
			
			
			if (isset($vals['statusRange'])){
				
				$this->statusRange = $vals['statusRange'];
			}
			
			
			if (isset($vals['subStatusRange'])){
				
				$this->subStatusRange = $vals['subStatusRange'];
			}
			
			
			if (isset($vals['payStatus'])){
				
				$this->payStatus = $vals['payStatus'];
			}
			
			
			if (isset($vals['isDisplay'])){
				
				$this->isDisplay = $vals['isDisplay'];
			}
			
			
			if (isset($vals['orderSourceTypeRange'])){
				
				$this->orderSourceTypeRange = $vals['orderSourceTypeRange'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'SearchOrderListByUserIdReq';
	}
	
	public function read($input){
		
		$input->readStructBegin();
		while(true){
			
			$schemeField = $input->readFieldBegin();
			if ($schemeField == null) break;
			$needSkip = true;
			
			
			if ("userId" == $schemeField){
				
				$needSkip = false;
				$input->readI64($this->userId); 
				
			}
			
			
			
			
			if ("keyword" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->keyword);
				
			}
			
			
			
			
			if ("vipclubList" == $schemeField){
				
				$needSkip = false;
				
				$this->vipclubList = array();
				$_size0 = 0;
				$input->readListBegin();
				while(true){
					
					try{
						
						$elem0 = null;
						$input->readString($elem0);
						
						$this->vipclubList[$_size0++] = $elem0;
					}
					catch(\Exception $e){
						
						break;
					}
				}
				
				$input->readListEnd();
				
			}
			
			
			
			
			if ("saleType" == $schemeField){
				
				$needSkip = false;
				
				$this->saleType = array();
				$_size1 = 0;
				$input->readListBegin();
				while(true){
					
					try{
						
						$elem1 = null;
						$input->readI32($elem1); 
						
						$this->saleType[$_size1++] = $elem1;
					}
					catch(\Exception $e){
						
						break;
					}
				}
				
				$input->readListEnd();
				
			}
			
			
			
			
			if ("typeRange" == $schemeField){
				
				$needSkip = false;
				
				$this->typeRange = new \com\vip\order\common\pojo\order\request\RangeParam();
				$this->typeRange->read($input);
				
			}
			
			
			
			
			if ("orderModelList" == $schemeField){
				
				$needSkip = false;
				
				$this->orderModelList = array();
				$_size2 = 0;
				$input->readListBegin();
				while(true){
					
					try{
						
						$elem2 = null;
						$input->readI32($elem2); 
						
						$this->orderModelList[$_size2++] = $elem2;
					}
					catch(\Exception $e){
						
						break;
					}
				}
				
				$input->readListEnd();
				
			}
			
			
			
			
			if ("statusRange" == $schemeField){
				
				$needSkip = false;
				
				$this->statusRange = new \com\vip\order\common\pojo\order\request\RangeParam();
				$this->statusRange->read($input);
				
			}
			
			
			
			
			if ("subStatusRange" == $schemeField){
				
				$needSkip = false;
				
				$this->subStatusRange = new \com\vip\order\common\pojo\order\request\RangeParam();
				$this->subStatusRange->read($input);
				
			}
			
			
			
			
			if ("payStatus" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->payStatus); 
				
			}
			
			
			
			
			if ("isDisplay" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->isDisplay); 
				
			}
			
			
			
			
			if ("orderSourceTypeRange" == $schemeField){
				
				$needSkip = false;
				
				$this->orderSourceTypeRange = new \com\vip\order\common\pojo\order\request\RangeParam();
				$this->orderSourceTypeRange->read($input);
				
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
		
		if($this->userId !== null) {
			
			$xfer += $output->writeFieldBegin('userId');
			$xfer += $output->writeI64($this->userId);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->keyword !== null) {
			
			$xfer += $output->writeFieldBegin('keyword');
			$xfer += $output->writeString($this->keyword);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->vipclubList !== null) {
			
			$xfer += $output->writeFieldBegin('vipclubList');
			
			if (!is_array($this->vipclubList)){
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$output->writeListBegin();
			foreach ($this->vipclubList as $iter0){
				
				$xfer += $output->writeString($iter0);
				
			}
			
			$output->writeListEnd();
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->saleType !== null) {
			
			$xfer += $output->writeFieldBegin('saleType');
			
			if (!is_array($this->saleType)){
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$output->writeListBegin();
			foreach ($this->saleType as $iter0){
				
				$xfer += $output->writeI32($iter0);
				
			}
			
			$output->writeListEnd();
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->typeRange !== null) {
			
			$xfer += $output->writeFieldBegin('typeRange');
			
			if (!is_object($this->typeRange)) {
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$xfer += $this->typeRange->write($output);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->orderModelList !== null) {
			
			$xfer += $output->writeFieldBegin('orderModelList');
			
			if (!is_array($this->orderModelList)){
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$output->writeListBegin();
			foreach ($this->orderModelList as $iter0){
				
				$xfer += $output->writeI32($iter0);
				
			}
			
			$output->writeListEnd();
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->statusRange !== null) {
			
			$xfer += $output->writeFieldBegin('statusRange');
			
			if (!is_object($this->statusRange)) {
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$xfer += $this->statusRange->write($output);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->subStatusRange !== null) {
			
			$xfer += $output->writeFieldBegin('subStatusRange');
			
			if (!is_object($this->subStatusRange)) {
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$xfer += $this->subStatusRange->write($output);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->payStatus !== null) {
			
			$xfer += $output->writeFieldBegin('payStatus');
			$xfer += $output->writeI32($this->payStatus);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->isDisplay !== null) {
			
			$xfer += $output->writeFieldBegin('isDisplay');
			$xfer += $output->writeI32($this->isDisplay);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->orderSourceTypeRange !== null) {
			
			$xfer += $output->writeFieldBegin('orderSourceTypeRange');
			
			if (!is_object($this->orderSourceTypeRange)) {
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$xfer += $this->orderSourceTypeRange->write($output);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		$xfer += $output->writeFieldStop();
		$xfer += $output->writeStructEnd();
		return $xfer;
	}
	
}

?>