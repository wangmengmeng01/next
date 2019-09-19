<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace com\vip\order\common\pojo\order\vo;

class OrderCancelApplyVO {
	
	static $_TSPEC;
	public $id = null;
	public $applySn = null;
	public $orderId = null;
	public $orderSn = null;
	public $userId = null;
	public $applyStatus = null;
	public $auditStatus = null;
	public $operatorRole = null;
	public $supplierAudit = null;
	public $failReason = null;
	public $createTime = null;
	public $updateTime = null;
	public $isDeleted = null;
	public $reasonChoice = null;
	public $reasonRemark = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'id'
			),
			2 => array(
			'var' => 'applySn'
			),
			3 => array(
			'var' => 'orderId'
			),
			4 => array(
			'var' => 'orderSn'
			),
			5 => array(
			'var' => 'userId'
			),
			6 => array(
			'var' => 'applyStatus'
			),
			7 => array(
			'var' => 'auditStatus'
			),
			8 => array(
			'var' => 'operatorRole'
			),
			9 => array(
			'var' => 'supplierAudit'
			),
			10 => array(
			'var' => 'failReason'
			),
			11 => array(
			'var' => 'createTime'
			),
			12 => array(
			'var' => 'updateTime'
			),
			13 => array(
			'var' => 'isDeleted'
			),
			14 => array(
			'var' => 'reasonChoice'
			),
			15 => array(
			'var' => 'reasonRemark'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['id'])){
				
				$this->id = $vals['id'];
			}
			
			
			if (isset($vals['applySn'])){
				
				$this->applySn = $vals['applySn'];
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
			
			
			if (isset($vals['applyStatus'])){
				
				$this->applyStatus = $vals['applyStatus'];
			}
			
			
			if (isset($vals['auditStatus'])){
				
				$this->auditStatus = $vals['auditStatus'];
			}
			
			
			if (isset($vals['operatorRole'])){
				
				$this->operatorRole = $vals['operatorRole'];
			}
			
			
			if (isset($vals['supplierAudit'])){
				
				$this->supplierAudit = $vals['supplierAudit'];
			}
			
			
			if (isset($vals['failReason'])){
				
				$this->failReason = $vals['failReason'];
			}
			
			
			if (isset($vals['createTime'])){
				
				$this->createTime = $vals['createTime'];
			}
			
			
			if (isset($vals['updateTime'])){
				
				$this->updateTime = $vals['updateTime'];
			}
			
			
			if (isset($vals['isDeleted'])){
				
				$this->isDeleted = $vals['isDeleted'];
			}
			
			
			if (isset($vals['reasonChoice'])){
				
				$this->reasonChoice = $vals['reasonChoice'];
			}
			
			
			if (isset($vals['reasonRemark'])){
				
				$this->reasonRemark = $vals['reasonRemark'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'OrderCancelApplyVO';
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
			
			
			
			
			if ("applySn" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->applySn);
				
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
			
			
			
			
			if ("applyStatus" == $schemeField){
				
				$needSkip = false;
				$input->readByte($this->applyStatus); 
				
			}
			
			
			
			
			if ("auditStatus" == $schemeField){
				
				$needSkip = false;
				$input->readByte($this->auditStatus); 
				
			}
			
			
			
			
			if ("operatorRole" == $schemeField){
				
				$needSkip = false;
				$input->readByte($this->operatorRole); 
				
			}
			
			
			
			
			if ("supplierAudit" == $schemeField){
				
				$needSkip = false;
				$input->readByte($this->supplierAudit); 
				
			}
			
			
			
			
			if ("failReason" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->failReason);
				
			}
			
			
			
			
			if ("createTime" == $schemeField){
				
				$needSkip = false;
				$input->readI64($this->createTime); 
				
			}
			
			
			
			
			if ("updateTime" == $schemeField){
				
				$needSkip = false;
				$input->readI64($this->updateTime); 
				
			}
			
			
			
			
			if ("isDeleted" == $schemeField){
				
				$needSkip = false;
				$input->readByte($this->isDeleted); 
				
			}
			
			
			
			
			if ("reasonChoice" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->reasonChoice); 
				
			}
			
			
			
			
			if ("reasonRemark" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->reasonRemark);
				
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
		
		
		if($this->applySn !== null) {
			
			$xfer += $output->writeFieldBegin('applySn');
			$xfer += $output->writeString($this->applySn);
			
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
		
		
		if($this->applyStatus !== null) {
			
			$xfer += $output->writeFieldBegin('applyStatus');
			$xfer += $output->writeByte($this->applyStatus);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->auditStatus !== null) {
			
			$xfer += $output->writeFieldBegin('auditStatus');
			$xfer += $output->writeByte($this->auditStatus);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->operatorRole !== null) {
			
			$xfer += $output->writeFieldBegin('operatorRole');
			$xfer += $output->writeByte($this->operatorRole);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->supplierAudit !== null) {
			
			$xfer += $output->writeFieldBegin('supplierAudit');
			$xfer += $output->writeByte($this->supplierAudit);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->failReason !== null) {
			
			$xfer += $output->writeFieldBegin('failReason');
			$xfer += $output->writeString($this->failReason);
			
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
		
		
		if($this->isDeleted !== null) {
			
			$xfer += $output->writeFieldBegin('isDeleted');
			$xfer += $output->writeByte($this->isDeleted);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->reasonChoice !== null) {
			
			$xfer += $output->writeFieldBegin('reasonChoice');
			$xfer += $output->writeI32($this->reasonChoice);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->reasonRemark !== null) {
			
			$xfer += $output->writeFieldBegin('reasonRemark');
			$xfer += $output->writeString($this->reasonRemark);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		$xfer += $output->writeFieldStop();
		$xfer += $output->writeStructEnd();
		return $xfer;
	}
	
}

?>