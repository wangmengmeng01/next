<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace vipapis\delivery;

class JitXCarrierInfo {
	
	static $_TSPEC;
	public $order_sn = null;
	public $pick_code = null;
	public $cust_code = null;
	public $cust_name = null;
	public $transport_no = null;
	public $carrier_point_code = null;
	public $carrier_point_name = null;
	public $jitx_carriers_info_id = null;
	public $third_cust_code = null;
	public $third_cust_name = null;
	public $origin_code = null;
	public $destination_code = null;
	public $sub_mail_no_model = null;
	public $template_code = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'order_sn'
			),
			2 => array(
			'var' => 'pick_code'
			),
			3 => array(
			'var' => 'cust_code'
			),
			4 => array(
			'var' => 'cust_name'
			),
			5 => array(
			'var' => 'transport_no'
			),
			6 => array(
			'var' => 'carrier_point_code'
			),
			7 => array(
			'var' => 'carrier_point_name'
			),
			8 => array(
			'var' => 'jitx_carriers_info_id'
			),
			9 => array(
			'var' => 'third_cust_code'
			),
			10 => array(
			'var' => 'third_cust_name'
			),
			11 => array(
			'var' => 'origin_code'
			),
			12 => array(
			'var' => 'destination_code'
			),
			13 => array(
			'var' => 'sub_mail_no_model'
			),
			14 => array(
			'var' => 'template_code'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['order_sn'])){
				
				$this->order_sn = $vals['order_sn'];
			}
			
			
			if (isset($vals['pick_code'])){
				
				$this->pick_code = $vals['pick_code'];
			}
			
			
			if (isset($vals['cust_code'])){
				
				$this->cust_code = $vals['cust_code'];
			}
			
			
			if (isset($vals['cust_name'])){
				
				$this->cust_name = $vals['cust_name'];
			}
			
			
			if (isset($vals['transport_no'])){
				
				$this->transport_no = $vals['transport_no'];
			}
			
			
			if (isset($vals['carrier_point_code'])){
				
				$this->carrier_point_code = $vals['carrier_point_code'];
			}
			
			
			if (isset($vals['carrier_point_name'])){
				
				$this->carrier_point_name = $vals['carrier_point_name'];
			}
			
			
			if (isset($vals['jitx_carriers_info_id'])){
				
				$this->jitx_carriers_info_id = $vals['jitx_carriers_info_id'];
			}
			
			
			if (isset($vals['third_cust_code'])){
				
				$this->third_cust_code = $vals['third_cust_code'];
			}
			
			
			if (isset($vals['third_cust_name'])){
				
				$this->third_cust_name = $vals['third_cust_name'];
			}
			
			
			if (isset($vals['origin_code'])){
				
				$this->origin_code = $vals['origin_code'];
			}
			
			
			if (isset($vals['destination_code'])){
				
				$this->destination_code = $vals['destination_code'];
			}
			
			
			if (isset($vals['sub_mail_no_model'])){
				
				$this->sub_mail_no_model = $vals['sub_mail_no_model'];
			}
			
			
			if (isset($vals['template_code'])){
				
				$this->template_code = $vals['template_code'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'JitXCarrierInfo';
	}
	
	public function read($input){
		
		$input->readStructBegin();
		while(true){
			
			$schemeField = $input->readFieldBegin();
			if ($schemeField == null) break;
			$needSkip = true;
			
			
			if ("order_sn" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->order_sn);
				
			}
			
			
			
			
			if ("pick_code" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->pick_code);
				
			}
			
			
			
			
			if ("cust_code" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->cust_code);
				
			}
			
			
			
			
			if ("cust_name" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->cust_name);
				
			}
			
			
			
			
			if ("transport_no" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->transport_no);
				
			}
			
			
			
			
			if ("carrier_point_code" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->carrier_point_code);
				
			}
			
			
			
			
			if ("carrier_point_name" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->carrier_point_name);
				
			}
			
			
			
			
			if ("jitx_carriers_info_id" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->jitx_carriers_info_id);
				
			}
			
			
			
			
			if ("third_cust_code" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->third_cust_code);
				
			}
			
			
			
			
			if ("third_cust_name" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->third_cust_name);
				
			}
			
			
			
			
			if ("origin_code" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->origin_code);
				
			}
			
			
			
			
			if ("destination_code" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->destination_code);
				
			}
			
			
			
			
			if ("sub_mail_no_model" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->sub_mail_no_model);
				
			}
			
			
			
			
			if ("template_code" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->template_code);
				
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
		
		$xfer += $output->writeFieldBegin('order_sn');
		$xfer += $output->writeString($this->order_sn);
		
		$xfer += $output->writeFieldEnd();
		
		$xfer += $output->writeFieldBegin('pick_code');
		$xfer += $output->writeString($this->pick_code);
		
		$xfer += $output->writeFieldEnd();
		
		$xfer += $output->writeFieldBegin('cust_code');
		$xfer += $output->writeString($this->cust_code);
		
		$xfer += $output->writeFieldEnd();
		
		$xfer += $output->writeFieldBegin('cust_name');
		$xfer += $output->writeString($this->cust_name);
		
		$xfer += $output->writeFieldEnd();
		
		$xfer += $output->writeFieldBegin('transport_no');
		$xfer += $output->writeString($this->transport_no);
		
		$xfer += $output->writeFieldEnd();
		
		$xfer += $output->writeFieldBegin('carrier_point_code');
		$xfer += $output->writeString($this->carrier_point_code);
		
		$xfer += $output->writeFieldEnd();
		
		$xfer += $output->writeFieldBegin('carrier_point_name');
		$xfer += $output->writeString($this->carrier_point_name);
		
		$xfer += $output->writeFieldEnd();
		
		$xfer += $output->writeFieldBegin('jitx_carriers_info_id');
		$xfer += $output->writeString($this->jitx_carriers_info_id);
		
		$xfer += $output->writeFieldEnd();
		
		if($this->third_cust_code !== null) {
			
			$xfer += $output->writeFieldBegin('third_cust_code');
			$xfer += $output->writeString($this->third_cust_code);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->third_cust_name !== null) {
			
			$xfer += $output->writeFieldBegin('third_cust_name');
			$xfer += $output->writeString($this->third_cust_name);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->origin_code !== null) {
			
			$xfer += $output->writeFieldBegin('origin_code');
			$xfer += $output->writeString($this->origin_code);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->destination_code !== null) {
			
			$xfer += $output->writeFieldBegin('destination_code');
			$xfer += $output->writeString($this->destination_code);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->sub_mail_no_model !== null) {
			
			$xfer += $output->writeFieldBegin('sub_mail_no_model');
			$xfer += $output->writeString($this->sub_mail_no_model);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->template_code !== null) {
			
			$xfer += $output->writeFieldBegin('template_code');
			$xfer += $output->writeString($this->template_code);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		$xfer += $output->writeFieldStop();
		$xfer += $output->writeStructEnd();
		return $xfer;
	}
	
}

?>