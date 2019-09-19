<?php

/**
 * 异常出库单单头信息model
 * table: t_outbound_info_exception
 */
class PutSOEcpt extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return PutSOEcpt the static model class
     */
    public static function model($className = __CLASS__)
    {
        return parent::model($className);
    }

    /**
     *
     * @return string the associated database table name
     */
    public function tableName()
    {
        return '{{outbound_info_exception}}';
    }

    /**
     * Retrieves a list of models based on the current search/filter conditions.
     * 
     * @return CActiveEcptProvider the data provider that can return the models based on the search/filter conditions.
     */
    public static function search($param)
    {
        $criteria = new CDbCriteria();
        //校验是否有查看所有货主的权限
        $viewFlag = util::hasViewAll();
        if ($viewFlag == 0) {
        	$existsArr = util::getCustomer();
        	if (!empty($existsArr)) {
        		$criteria->addInCondition('customer_id', $existsArr);
        	} else {
        		return ;
        	}
        }        
        if (!empty($param['PutSOEcpt']['order_no'])) {
            $criteria->compare('order_no', trim($param['PutSOEcpt']['order_no']));
        } else {
	        if (isset($param['PutSOEcpt']['order_type'])) {
	            $criteria->compare('order_type', trim($param['PutSOEcpt']['order_type']));
	        }
	        if (isset($param['PutSOEcpt']['customer_id'])) {
	            $criteria->compare('customer_id', trim($param['PutSOEcpt']['customer_id']));
	        }
	        if (isset($param['PutSOEcpt']['warehouse_code'])) {
	        	$criteria->compare('warehouse_code', trim($param['PutSOEcpt']['warehouse_code']));
	        }
	        if (!empty($param['PutSOEcpt']['start_time']) && !empty($param['PutSOEcpt']['end_time'])) {
	            $criteria->addBetweenCondition('create_time', trim($param['PutSOEcpt']['start_time']), trim($param['PutSOEcpt']['end_time']));//between 1 and 4 
	        } else {
	        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
	        }
        }
        $criteria->compare('is_valid', 1);
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $dataProvider = new CActiveDataProvider('PutSOEcpt', array(
            'sort' => array(
                'defaultOrder' => 'create_time Desc'
            ),
            'pagination' => array(
                'pageSize' => $param['rows'],
                'currentPage' => $param['page'] - 1
            ),
            'criteria' => $criteria
        ));
        return $dataProvider;
    }
    
    /**
     * 异常出库单单头信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    			'订单号' => 'order_no',
    			'订单类型 ' => 'order_type',
    			'货主ID' => 'customer_id',
    			'仓库编码 ' => 'warehouse_code',
    			'订单创建时间' => 'order_time',
    			'预期发货时间' => 'expected_shipment_time1',
    			'要求交货时间' => 'required_delivery_time',
    			'平台订单号 ' => 'so_reference2',
    			'店铺名称' => 'so_reference3',
    			'快递单号 ' => 'delivery_no',
    			'下单平台' => 'consignee_id',
    			'收货人名称 ' => 'consignee_name',
    			'国家代码 ' => 'c_country',
    			'省' => 'c_province',
    			'市' => 'c_city',
    			'手机号' => 'c_tel1',
    			'固话' => 'c_tel2',
    			'邮编' => 'c_zip',
    			'邮箱 ' => 'c_mail',
    			'地址' => 'c_address1',
    			'退货原入库单号' => 'user_define2',
    			'平台发货仓库' => 'user_define3',
    			'订单下发方' => 'user_define4',
    			'客服备注' => 'user_define5',
    			'是否打印发票' => 'invoice_print_flag',
    			'备注（顾客留言）' => 'remark',
    			'支付方式' => 'h_edi_01',
    			'订单总价 ' => 'h_edi_02',
    			'优惠金额' => 'h_edi_03',
    			'已付金额' => 'h_edi_04',
    			'是否货到付款 ' => 'h_edi_05',
    			'应付金额' => 'h_edi_06',
    			'支付宝交易号 ' => 'h_edi_07',
    			'是否保价' => 'h_edi_08',
    			'保价金额' => 'h_edi_09',
    			'运费 ' => 'h_edi_10',
    			'渠道' => 'channel',
    			'承运商编码' => 'carrier_id',
    			'承运商名称 ' => 'carrier_name',
    			'是否有效 ' => 'is_valid',
    			'入库时间' => 'create_time'
    	);
    	return $columns;
    }
}