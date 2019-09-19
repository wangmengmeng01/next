<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace com\vip\order\common\pojo\order\vo;

class OrderCreateorderDataSyncVO {
	
	static $_TSPEC;
	public $id = null;
	public $orderId = null;
	public $orderSn = null;
	public $userId = null;
	public $syncVopFlag = null;
	public $syncBuylimitFlag = null;
	public $syncUnpaidFlag = null;
	public $procFlag = null;
	public $procTimes = null;
	public $isDeleted = null;
	public $nextProcTime = null;
	public $createTime = null;
	public $updateTime = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'id'
			),
			2 => array(
			'var' => 'orderId'
			),
			3 => array(
			'var' => 'orderSn'
			),
			4 => array(
			'var' => 'userId'
			),
			5 => array(
			'var' => 'syncVopFlag'
			),
			6 => array(
			'var' => 'syncBuylimitFlag'
			),
			7 => array(
			'var' => 'syncUnpaidFlag'
			),
			8 => array(
			'var' => 'procFlag'
			),
			9 => array(
			'var' => 'procTimes'
			),
			10 => array(
			'var' => 'isDeleted'
			),
			11 => array(
			'var' => 'nextProcTime'
			),
			12 => array(
			'var' => 'createTime'
			),
			13 => array(
			'var' => 'updateTime'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['id'])){
				
				$this->id = $vals['id'];
			}
			
			
			if (isset($vals['orderId'])){
				
				$this->orderId = $vals['orderId'];
			}
			
			
			if (isset($vals['orderSn'])){
				
				$this->orderSn = $vals['orderSn'];
			}
			
			
			if (isset($vals['userId'])){
				
				$this->userId = $vals['userId'];
			}
			
			
			if (isset($vals['syncVopFlag'])){
				
				$this->syncVopFlag = $vals['syncVopFlag'];
			}
			
			
			if (isset($vals['syncBuylimitFlag'])){
				
				$this->syncBuylimitFlag = $vals['syncBuylimitFlag'];
			}
			
			
			if (isset($vals['syncUnpaidFlag'])){
				
				$this->syncUnpaidFlag = $vals['syncUnpaidFlag'];
			}
			
			
			if (isset($vals['procFlag'])){
				
				$this->procFlag = $vals['procFlag'];
			}
			
			
			if (isset($vals['procTimes'])){
				
				$this->procTimes = $vals['procTimes'];
			}
			
			
			if (isset($vals['isDeleted'])){
				
				$this->isDeleted = $vals['isDeleted'];
			}
			
			
			if (isset($vals['nextProcTime'])){
				
				$this->nextProcTime = $vals['nextProcTime'];
			}
			
			
			if (isset($vals['createTime'])){
				
				$this->createTime = $vals['createTime'];
			}
			
			
			if (isset($vals['updateTime'])){
				
				$this->updateTime = $vals['updateTime'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'OrderCreateorderDataSyncVO';
	}
	
	public function read($input){
		
		$input->readStructBegin();
		while(true){
			
			$schemeField = $input->readFieldBegin();
			if ($schemeField == null) break;
			$needSkip = true;
			
			
			if ("id" == $schemeField){
				
				$needSkip = false;
				$input->readI64($this->id); 
				
			}
			
			
			
			
			if ("orderId" == $schemeField){
				
				$needSkip = false;
				$input->readI64($this->orderId); 
				
			}
			
			
			
			
			if ("orderSn" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->orderSn);
				
			}
			
			
			
			
			if ("userId" == $schemeField){
				
				$needSkip = false;
				$input->readI64($this->userId); 
				
			}
			
			
			
			
			if ("syncVopFlag" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->syncVopFlag); 
				
			}
			
			
			
			
			if ("syncBuylimitFlag" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->syncBuylimitFlag); 
				
			}
			
			
			
			
			if ("syncUnpaidFlag" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->syncUnpaidFlag); 
				
			}
			
			
			
			
			if ("procFlag" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->procFlag); 
				
			}
			
			
			
			
			if ("procTimes" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->procTimes); 
				
			}
			
			
			
			
			if ("isDeleted" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->isDeleted); 
				
			}
			
			
			
			
			if ("nextProcTime" == $schemeField){
				
				$needSkip = false;
				$input->readI64($this->nextProcTime); 
				
			}
			
			
			
			
			if ("createTime" == $schemeField){
				
				$needSkip = false;
				$input->readI64($this->createTime); 
				
			}
			
			
			
			
			if ("updateTime" == $schemeField){
				
				$needSkip = false;
				$input->readI64($this->updateTime); 
				
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
		
		if($this->id !== null) {
			
			$xfer += $output->writeFieldBegin('id');
			$xfer += $output->writeI64($this->id);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->orderId !== null) {
			
			$xfer += $output->writeFieldBegin('orderId');
			$xfer += $output->writeI64($this->orderId);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->orderSn !== null) {
			
			$xfer += $output->writeFieldBegin('orderSn');
			$xfer += $output->writeString($this->orderSn);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->userId !== null) {
			
			$xfer += $output->writeFieldBegin('userId');
			$xfer += $output->writeI64($this->userId);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->syncVopFlag !== null) {
			
			$xfer += $output->writeFieldBegin('syncVopFlag');
			$xfer += $output->writeI32($this->syncVopFlag);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->syncBuylimitFlag !== null) {
			
			$xfer += $output->writeFieldBegin('syncBuylimitFlag');
			$xfer += $output->writeI32($this->syncBuylimitFlag);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->syncUnpaidFlag !== null) {
			
			$xfer += $output->writeFieldBegin('syncUnpaidFlag');
			$xfer += $output->writeI32($this->syncUnpaidFlag);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->procFlag !== null) {
			
			$xfer += $output->writeFieldBegin('procFlag');
			$xfer += $output->writeI32($this->procFlag);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->procTimes !== null) {
			
			$xfer += $output->writeFieldBegin('procTimes');
			$xfer += $output->writeI32($this->procTimes);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->isDeleted !== null) {
			
			$xfer += $output->writeFieldBegin('isDeleted');
			$xfer += $output->writeI32($this->isDeleted);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->nextProcTime !== null) {
			
			$xfer += $output->writeFieldBegin('nextProcTime');
			$xfer += $output->writeI64($this->nextProcTime);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->createTime !== null) {
			
			$xfer += $output->writeFieldBegin('createTime');
			$xfer += $output->writeI64($this->createTime);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->updateTime !== null) {
			
			$xfer += $output->writeFieldBegin('updateTime');
			$xfer += $output->writeI64($this->updateTime);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		$xfer += $output->writeFieldStop();
		$xfer += $output->writeStructEnd();
		return $xfer;
	}
	
}

?>