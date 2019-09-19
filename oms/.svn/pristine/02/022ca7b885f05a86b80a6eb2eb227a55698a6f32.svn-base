<?php


/*
* Copyright (c) 2008-2016 vip.com, All Rights Reserved.
*
* Powered by com.vip.osp.osp-idlc-2.5.11.
*
*/

namespace vipapis\delivery;

class OrderPackage {
	
	static $_TSPEC;
	public $order_sn = null;
	public $weight = null;
	public $weight_um = null;
	public $length = null;
	public $width = null;
	public $height = null;
	public $dimension_um = null;
	public $volume = null;
	public $volume_um = null;
	public $box_id = null;
	public $pj_transport_no = null;
	public $collection_way = null;
	public $box_num = null;
	public $oqc_date = null;
	public $warehouse = null;
	public $cmd_type = null;
	public $is_bind = null;
	public $pick_code = null;
	public $cust_code = null;
	public $transport_no = null;
	public $carrier_point_code = null;
	public $carrier_point_name = null;
	public $is_last_box = null;
	public $box_barcode = null;
	public $details = null;
	
	public function __construct($vals=null){
		
		if (!isset(self::$_TSPEC)){
			
			self::$_TSPEC = array(
			1 => array(
			'var' => 'order_sn'
			),
			2 => array(
			'var' => 'weight'
			),
			3 => array(
			'var' => 'weight_um'
			),
			4 => array(
			'var' => 'length'
			),
			5 => array(
			'var' => 'width'
			),
			6 => array(
			'var' => 'height'
			),
			7 => array(
			'var' => 'dimension_um'
			),
			8 => array(
			'var' => 'volume'
			),
			9 => array(
			'var' => 'volume_um'
			),
			10 => array(
			'var' => 'box_id'
			),
			11 => array(
			'var' => 'pj_transport_no'
			),
			12 => array(
			'var' => 'collection_way'
			),
			13 => array(
			'var' => 'box_num'
			),
			14 => array(
			'var' => 'oqc_date'
			),
			15 => array(
			'var' => 'warehouse'
			),
			16 => array(
			'var' => 'cmd_type'
			),
			17 => array(
			'var' => 'is_bind'
			),
			18 => array(
			'var' => 'pick_code'
			),
			19 => array(
			'var' => 'cust_code'
			),
			20 => array(
			'var' => 'transport_no'
			),
			21 => array(
			'var' => 'carrier_point_code'
			),
			22 => array(
			'var' => 'carrier_point_name'
			),
			23 => array(
			'var' => 'is_last_box'
			),
			24 => array(
			'var' => 'box_barcode'
			),
			25 => array(
			'var' => 'details'
			),
			
			);
			
		}
		
		if (is_array($vals)){
			
			
			if (isset($vals['order_sn'])){
				
				$this->order_sn = $vals['order_sn'];
			}
			
			
			if (isset($vals['weight'])){
				
				$this->weight = $vals['weight'];
			}
			
			
			if (isset($vals['weight_um'])){
				
				$this->weight_um = $vals['weight_um'];
			}
			
			
			if (isset($vals['length'])){
				
				$this->length = $vals['length'];
			}
			
			
			if (isset($vals['width'])){
				
				$this->width = $vals['width'];
			}
			
			
			if (isset($vals['height'])){
				
				$this->height = $vals['height'];
			}
			
			
			if (isset($vals['dimension_um'])){
				
				$this->dimension_um = $vals['dimension_um'];
			}
			
			
			if (isset($vals['volume'])){
				
				$this->volume = $vals['volume'];
			}
			
			
			if (isset($vals['volume_um'])){
				
				$this->volume_um = $vals['volume_um'];
			}
			
			
			if (isset($vals['box_id'])){
				
				$this->box_id = $vals['box_id'];
			}
			
			
			if (isset($vals['pj_transport_no'])){
				
				$this->pj_transport_no = $vals['pj_transport_no'];
			}
			
			
			if (isset($vals['collection_way'])){
				
				$this->collection_way = $vals['collection_way'];
			}
			
			
			if (isset($vals['box_num'])){
				
				$this->box_num = $vals['box_num'];
			}
			
			
			if (isset($vals['oqc_date'])){
				
				$this->oqc_date = $vals['oqc_date'];
			}
			
			
			if (isset($vals['warehouse'])){
				
				$this->warehouse = $vals['warehouse'];
			}
			
			
			if (isset($vals['cmd_type'])){
				
				$this->cmd_type = $vals['cmd_type'];
			}
			
			
			if (isset($vals['is_bind'])){
				
				$this->is_bind = $vals['is_bind'];
			}
			
			
			if (isset($vals['pick_code'])){
				
				$this->pick_code = $vals['pick_code'];
			}
			
			
			if (isset($vals['cust_code'])){
				
				$this->cust_code = $vals['cust_code'];
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
			
			
			if (isset($vals['is_last_box'])){
				
				$this->is_last_box = $vals['is_last_box'];
			}
			
			
			if (isset($vals['box_barcode'])){
				
				$this->box_barcode = $vals['box_barcode'];
			}
			
			
			if (isset($vals['details'])){
				
				$this->details = $vals['details'];
			}
			
			
		}
		
	}
	
	
	public function getName(){
		
		return 'OrderPackage';
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
			
			
			
			
			if ("weight" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->weight);
				
			}
			
			
			
			
			if ("weight_um" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->weight_um);
				
			}
			
			
			
			
			if ("length" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->length);
				
			}
			
			
			
			
			if ("width" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->width);
				
			}
			
			
			
			
			if ("height" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->height);
				
			}
			
			
			
			
			if ("dimension_um" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->dimension_um);
				
			}
			
			
			
			
			if ("volume" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->volume);
				
			}
			
			
			
			
			if ("volume_um" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->volume_um);
				
			}
			
			
			
			
			if ("box_id" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->box_id);
				
			}
			
			
			
			
			if ("pj_transport_no" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->pj_transport_no);
				
			}
			
			
			
			
			if ("collection_way" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->collection_way); 
				
			}
			
			
			
			
			if ("box_num" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->box_num); 
				
			}
			
			
			
			
			if ("oqc_date" == $schemeField){
				
				$needSkip = false;
				$input->readI64($this->oqc_date);
				
			}
			
			
			
			
			if ("warehouse" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->warehouse);
				
			}
			
			
			
			
			if ("cmd_type" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->cmd_type);
				
			}
			
			
			
			
			if ("is_bind" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->is_bind); 
				
			}
			
			
			
			
			if ("pick_code" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->pick_code);
				
			}
			
			
			
			
			if ("cust_code" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->cust_code);
				
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
			
			
			
			
			if ("is_last_box" == $schemeField){
				
				$needSkip = false;
				$input->readI32($this->is_last_box); 
				
			}
			
			
			
			
			if ("box_barcode" == $schemeField){
				
				$needSkip = false;
				$input->readString($this->box_barcode);
				
			}
			
			
			
			
			if ("details" == $schemeField){
				
				$needSkip = false;
				
				$this->details = array();
				$_size1 = 0;
				$input->readListBegin();
				while(true){
					
					try{
						
						$elem1 = null;
						
						$elem1 = new \vipapis\delivery\OrderPackageDetail();
						$elem1->read($input);
						
						$this->details[$_size1++] = $elem1;
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
		
		if($this->order_sn !== null) {
			
			$xfer += $output->writeFieldBegin('order_sn');
			$xfer += $output->writeString($this->order_sn);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->weight !== null) {
			
			$xfer += $output->writeFieldBegin('weight');
			$xfer += $output->writeString($this->weight);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->weight_um !== null) {
			
			$xfer += $output->writeFieldBegin('weight_um');
			$xfer += $output->writeString($this->weight_um);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->length !== null) {
			
			$xfer += $output->writeFieldBegin('length');
			$xfer += $output->writeString($this->length);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->width !== null) {
			
			$xfer += $output->writeFieldBegin('width');
			$xfer += $output->writeString($this->width);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->height !== null) {
			
			$xfer += $output->writeFieldBegin('height');
			$xfer += $output->writeString($this->height);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->dimension_um !== null) {
			
			$xfer += $output->writeFieldBegin('dimension_um');
			$xfer += $output->writeString($this->dimension_um);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->volume !== null) {
			
			$xfer += $output->writeFieldBegin('volume');
			$xfer += $output->writeString($this->volume);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->volume_um !== null) {
			
			$xfer += $output->writeFieldBegin('volume_um');
			$xfer += $output->writeString($this->volume_um);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->box_id !== null) {
			
			$xfer += $output->writeFieldBegin('box_id');
			$xfer += $output->writeString($this->box_id);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->pj_transport_no !== null) {
			
			$xfer += $output->writeFieldBegin('pj_transport_no');
			$xfer += $output->writeString($this->pj_transport_no);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->collection_way !== null) {
			
			$xfer += $output->writeFieldBegin('collection_way');
			$xfer += $output->writeI32($this->collection_way);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->box_num !== null) {
			
			$xfer += $output->writeFieldBegin('box_num');
			$xfer += $output->writeI32($this->box_num);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->oqc_date !== null) {
			
			$xfer += $output->writeFieldBegin('oqc_date');
			$xfer += $output->writeI64($this->oqc_date);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->warehouse !== null) {
			
			$xfer += $output->writeFieldBegin('warehouse');
			$xfer += $output->writeString($this->warehouse);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->cmd_type !== null) {
			
			$xfer += $output->writeFieldBegin('cmd_type');
			$xfer += $output->writeString($this->cmd_type);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		$xfer += $output->writeFieldBegin('is_bind');
		$xfer += $output->writeI32($this->is_bind);
		
		$xfer += $output->writeFieldEnd();
		
		if($this->pick_code !== null) {
			
			$xfer += $output->writeFieldBegin('pick_code');
			$xfer += $output->writeString($this->pick_code);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->cust_code !== null) {
			
			$xfer += $output->writeFieldBegin('cust_code');
			$xfer += $output->writeString($this->cust_code);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->transport_no !== null) {
			
			$xfer += $output->writeFieldBegin('transport_no');
			$xfer += $output->writeString($this->transport_no);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->carrier_point_code !== null) {
			
			$xfer += $output->writeFieldBegin('carrier_point_code');
			$xfer += $output->writeString($this->carrier_point_code);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->carrier_point_name !== null) {
			
			$xfer += $output->writeFieldBegin('carrier_point_name');
			$xfer += $output->writeString($this->carrier_point_name);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		$xfer += $output->writeFieldBegin('is_last_box');
		$xfer += $output->writeI32($this->is_last_box);
		
		$xfer += $output->writeFieldEnd();
		
		if($this->box_barcode !== null) {
			
			$xfer += $output->writeFieldBegin('box_barcode');
			$xfer += $output->writeString($this->box_barcode);
			
			$xfer += $output->writeFieldEnd();
		}
		
		
		if($this->details !== null) {
			
			$xfer += $output->writeFieldBegin('details');
			
			if (!is_array($this->details)){
				
				throw new \Osp\Exception\OspException('Bad type in structure.', \Osp\Exception\OspException::INVALID_DATA);
			}
			
			$output->writeListBegin();
			foreach ($this->details as $iter0){
				
				
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