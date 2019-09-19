<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace vipapis\delivery;

class JitXOrder {
	
	static $_TSPEC;
	public $id = null;
	public $order_sn = null;
	public $delivery_warehouse = null;
	public $add_time = null;
	public $vis_add_time = null;
	public $buyer = null;
	public $buyer_address = null;
	public $buyer_mobile = null;
	public $buyer_tel = null;
	public $buyer_postcode = null;
	public $buyer_city = null;
	public $buyer_province = null;
	public $buyer_country = null;
	public $buyer_country_id = null;
	public $pay_type = null;
	public $cod_type = null;
	public $money = null;
	public $remark = null;
	public $transport_time = null;
	public $transport_day = null;
	public $vendor_id = null;
	public $vendor_name = null;
	public $invoice = null;
	public $invoice_amount = null;
	public $invoice_type = null;
	public $tax_pay_no = null;
	public $operation_type = null;
	public $print_list = null;
	public $goods = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'id'
			),
			2 => array(
			'var' => 'order_sn'
			),
			3 => array(
			'var' => 'delivery_warehouse'
			),
			4 => array(
			'var' => 'add_time'
			),
			5 => array(
			'var' => 'vis_add_time'
			),
			6 => array(
			'var' => 'buyer'
			),
			7 => array(
			'var' => 'buyer_address'
			),
			8 => array(
			'var' => 'buyer_mobile'
			),
			9 => array(
			'var' => 'buyer_tel'
			),
			10 => array(
			'var' => 'buyer_postcode'
			),
			11 => array(
			'var' => 'buyer_city'
			),
			12 => array(
			'var' => 'buyer_province'
			),
			13 => array(
			'var' => 'buyer_country'
			),
			14 => array(
			'var' => 'buyer_country_id'
			),
			15 => array(
			'var' => 'pay_type'
			),
			16 => array(
			'var' => 'cod_type'
			),
			17 => array(
			'var' => 'money'
			),
			18 => array(
			'var' => 'remark'
			),
			19 => array(
			'var' => 'transport_time'
			),
			20 => array(
			'var' => 'transport_day'
			),
			21 => array(
			'var' => 'vendor_id'
			),
			22 => array(
			'var' => 'vendor_name'
			),
			23 => array(
			'var' => 'invoice'
			),
			24 => array(
			'var' => 'invoice_amount'
			),
			25 => array(
			'var' => 'invoice_type'
			),
			26 => array(
			'var' => 'tax_pay_no'
			),
			27 => array(
			'var' => 'operation_type'
			),
			28 => array(
			'var' => 'print_list'
			),
			29 => array(
			'var' => 'goods'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['id'])){
				
				$this->id = $vals['id'];
			}
			
			
			if (isset($vals['order_sn'])){
				
				$this->order_sn = $vals['order_sn'];
			}
			
			
			if (isset($vals['delivery_warehouse'])){
				
				$this->delivery_warehouse = $vals['delivery_warehouse'];
			}
			
			
			if (isset($vals['add_time'])){
				
				$this->add_time = $vals['add_time'];
			}
			
			
			if (isset($vals['vis_add_time'])){
				
				$this->vis_add_time = $vals['vis_add_time'];
			}
			
			
			if (isset($vals['buyer'])){
				
				$this->buyer = $vals['buyer'];
			}
			
			
			if (isset($vals['buyer_address'])){
				
				$this->buyer_address = $vals['buyer_address'];
			}
			
			
			if (isset($vals['buyer_mobile'])){
				
				$this->buyer_mobile = $vals['buyer_mobile'];
			}
			
			
			if (isset($vals['buyer_tel'])){
				
				$this->buyer_tel = $vals['buyer_tel'];
			}
			
			
			if (isset($vals['buyer_postcode'])){
				
				$this->buyer_postcode = $vals['buyer_postcode'];
			}
			
			
			if (isset($vals['buyer_city'])){
				
				$this->buyer_city = $vals['buyer_city'];
			}
			
			
			if (isset($vals['buyer_province'])){
				
				$this->buyer_province = $vals['buyer_province'];
			}
			
			
			if (isset($vals['buyer_country'])){
				
				$this->buyer_country = $vals['buyer_country'];
			}
			
			
			if (isset($vals['buyer_country_id'])){
				
				$this->buyer_country_id = $vals['buyer_country_id'];
			}
			
			
			if (isset($vals['pay_type'])){
				
				$this->pay_type = $vals['pay_type'];
			}
			
			
			if (isset($vals['cod_type'])){
				
				$this->cod_type = $vals['cod_type'];
			}
			
			
			if (isset($vals['money'])){
				
				$this->money = $vals['money'];
			}
			
			
			if (isset($vals['remark'])){
				
				$this->remark = $vals['remark'];
			}
			
			
			if (isset($vals['transport_time'])){
				
				$this->transport_time = $vals['transport_time'];
			}
			
			
			if (isset($vals['transport_day'])){
				
				$this->transport_day = $vals['transport_day'];
			}
			
			
			if (isset($vals['vendor_id'])){
				
				$this->vendor_id = $vals['vendor_id'];
			}
			
			
			if (isset($vals['vendor_name'])){
				
				$this->vendor_name = $vals['vendor_name'];
			}
			
			
			if (isset($vals['invoice'])){
				
				$this->invoice = $vals['invoice'];
			}
			
			
			if (isset($vals['invoice_amount'])){
				
				$this->invoice_amount = $vals['invoice_amount'];
			}
			
			
			if (isset($vals['invoice_type'])){
				
				$this->invoice_type = $vals['invoice_type'];
			}
			
			
			if (isset($vals['tax_pay_no'])){
				
				$this->tax_pay_no = $vals['tax_pay_no'];
			}
			
			
			if (isset($vals['operation_type'])){
				
				$this->operation_type = $vals['operation_type'];
			}
			
			
			if (isset($vals['print_list'])){
				
				$this->print_list = $vals['print_list'];
			}
			
			
			if (isset($vals['goods'])){
				
				$this->goods = $vals['goods'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'JitXOrder';
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
			
			
			
			
			if ("order_sn" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->order_sn);
				
			}
			
			
			
			
			if ("delivery_warehouse" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->delivery_warehouse);
				
			}
			
			
			
			
			if ("add_time" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->add_time);
				
			}
			
			
			
			
			if ("vis_add_time" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->vis_add_time);
				
			}
			
			
			
			
			if ("buyer" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->buyer);
				
			}
			
			
			
			
			if ("buyer_address" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->buyer_address);
				
			}
			
			
			
			
			if ("buyer_mobile" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->buyer_mobile);
				
			}
			
			
			
			
			if ("buyer_tel" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->buyer_tel);
				
			}
			
			
			
			
			if ("buyer_postcode" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->buyer_postcode);
				
			}
			
			
			
			
			if ("buyer_city" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->buyer_city);
				
			}
			
			
			
			
			if ("buyer_province" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->buyer_province);
				
			}
			
			
			
			
			if ("buyer_country" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->buyer_country);
				
			}
			
			
			
			
			if ("buyer_country_id" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->buyer_country_id);
				
			}
			
			
			
			
			if ("pay_type" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->pay_type);
				
			}
			
			
			
			
			if ("cod_type" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->cod_type);
				
			}
			
			
			
			
			if ("money" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->money);
				
			}
			
			
			
			
			if ("remark" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->remark);
				
			}
			
			
			
			
			if ("transport_time" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->transport_time);
				
			}
			
			
			
			
			if ("transport_day" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->transport_day);
				
			}
			
			
			
			
			if ("vendor_id" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->vendor_id); 
				
			}
			
			
			
			
			if ("vendor_name" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->vendor_name);
				
			}
			
			
			
			
			if ("invoice" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->invoice);
				
			}
			
			
			
			
			if ("invoice_amount" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->invoice_amount);
				
			}
			
			
			
			
			if ("invoice_type" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->invoice_type);
				
			}
			
			
			
			
			if ("tax_pay_no" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->tax_pay_no);
				
			}
			
			
			
			
			if ("operation_type" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->operation_type);
				
			}
			
			
			
			
			if ("print_list" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->print_list);
				
			}
			
			
			
			
			if ("goods" == $schemeField){
				
				$needSkip = false;
				
				$this->goods = array();
				$_size0 = 0;
				$input->readListBegin();
				while(true){
					
					try{
						
						$elem0 = null;
						
						$elem0 = new \vipapis\delivery\JitXOrderGood();
						$elem0->read($input);
						
						$this->goods[$_size0++] = $elem0;
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
		
		if($this->id !== null) {
			
			$xfer += $output->writeFieldBegin('id');
			$xfer += $output->writeI64($this->id);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->order_sn !== null) {
			
			$xfer += $output->writeFieldBegin('order_sn');
			$xfer += $output->writeString($this->order_sn);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->delivery_warehouse !== null) {
			
			$xfer += $output->writeFieldBegin('delivery_warehouse');
			$xfer += $output->writeString($this->delivery_warehouse);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->add_time !== null) {
			
			$xfer += $output->writeFieldBegin('add_time');
			$xfer += $output->writeString($this->add_time);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->vis_add_time !== null) {
			
			$xfer += $output->writeFieldBegin('vis_add_time');
			$xfer += $output->writeString($this->vis_add_time);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->buyer !== null) {
			
			$xfer += $output->writeFieldBegin('buyer');
			$xfer += $output->writeString($this->buyer);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->buyer_address !== null) {
			
			$xfer += $output->writeFieldBegin('buyer_address');
			$xfer += $output->writeString($this->buyer_address);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->buyer_mobile !== null) {
			
			$xfer += $output->writeFieldBegin('buyer_mobile');
			$xfer += $output->writeString($this->buyer_mobile);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->buyer_tel !== null) {
			
			$xfer += $output->writeFieldBegin('buyer_tel');
			$xfer += $output->writeString($this->buyer_tel);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->buyer_postcode !== null) {
			
			$xfer += $output->writeFieldBegin('buyer_postcode');
			$xfer += $output->writeString($this->buyer_postcode);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->buyer_city !== null) {
			
			$xfer += $output->writeFieldBegin('buyer_city');
			$xfer += $output->writeString($this->buyer_city);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->buyer_province !== null) {
			
			$xfer += $output->writeFieldBegin('buyer_province');
			$xfer += $output->writeString($this->buyer_province);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->buyer_country !== null) {
			
			$xfer += $output->writeFieldBegin('buyer_country');
			$xfer += $output->writeString($this->buyer_country);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->buyer_country_id !== null) {
			
			$xfer += $output->writeFieldBegin('buyer_country_id');
			$xfer += $output->writeString($this->buyer_country_id);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->pay_type !== null) {
			
			$xfer += $output->writeFieldBegin('pay_type');
			$xfer += $output->writeString($this->pay_type);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->cod_type !== null) {
			
			$xfer += $output->writeFieldBegin('cod_type');
			$xfer += $output->writeString($this->cod_type);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->money !== null) {
			
			$xfer += $output->writeFieldBegin('money');
			$xfer += $output->writeString($this->money);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->remark !== null) {
			
			$xfer += $output->writeFieldBegin('remark');
			$xfer += $output->writeString($this->remark);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->transport_time !== null) {
			
			$xfer += $output->writeFieldBegin('transport_time');
			$xfer += $output->writeString($this->transport_time);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->transport_day !== null) {
			
			$xfer += $output->writeFieldBegin('transport_day');
			$xfer += $output->writeString($this->transport_day);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->vendor_id !== null) {
			
			$xfer += $output->writeFieldBegin('vendor_id');
			$xfer += $output->writeI32($this->vendor_id);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->vendor_name !== null) {
			
			$xfer += $output->writeFieldBegin('vendor_name');
			$xfer += $output->writeString($this->vendor_name);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->invoice !== null) {
			
			$xfer += $output->writeFieldBegin('invoice');
			$xfer += $output->writeString($this->invoice);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->invoice_amount !== null) {
			
			$xfer += $output->writeFieldBegin('invoice_amount');
			$xfer += $output->writeString($this->invoice_amount);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->invoice_type !== null) {
			
			$xfer += $output->writeFieldBegin('invoice_type');
			$xfer += $output->writeString($this->invoice_type);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->tax_pay_no !== null) {
			
			$xfer += $output->writeFieldBegin('tax_pay_no');
			$xfer += $output->writeString($this->tax_pay_no);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->operation_type !== null) {
			
			$xfer += $output->writeFieldBegin('operation_type');
			$xfer += $output->writeString($this->operation_type);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->print_list !== null) {
			
			$xfer += $output->writeFieldBegin('print_list');
			$xfer += $output->writeString($this->print_list);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->goods !== null) {
			
			$xfer += $output->writeFieldBegin('goods');
			
			if (!is_array($this->goods)){
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$output->writeListBegin();
			foreach ($this->goods as $iter0){
				
				
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