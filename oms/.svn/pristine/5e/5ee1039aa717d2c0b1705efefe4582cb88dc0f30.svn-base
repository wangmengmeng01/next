<?php

/**
 * 发货单下发单头信息model
 * table: t_delivery_order_info
 */
class DeliveryOrderCreate extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return DeliveryOrderCreate the static model class
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
        return '{{delivery_order_info}}';
    }

    /**
     * Retrieves a list of models based on the current search/filter conditions.
     * 
     * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
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
        if (!empty($param['DeliveryOrderCreate']['delivery_order_code'])) {
            $criteria->compare('delivery_order_code', trim($param['DeliveryOrderCreate']['delivery_order_code']));
        } else {
	        if (isset($param['DeliveryOrderCreate']['order_type'])) {
	            $criteria->compare('order_type', trim($param['DeliveryOrderCreate']['order_type']));
	        }
	        if (isset($param['DeliveryOrderCreate']['customer_id'])) {
	            $criteria->compare('customer_id', trim($param['DeliveryOrderCreate']['customer_id']));
	        }
            if (isset($param['DeliveryOrderCreate']['fx_flag'])) {
                $criteria->compare('fx_flag', trim($param['DeliveryOrderCreate']['fx_flag']));
            }
	        if (isset($param['DeliveryOrderCreate']['warehouse_code'])) {
	        	$criteria->compare('warehouse_code', trim($param['DeliveryOrderCreate']['warehouse_code']));
	        }
	        if (!empty($param['DeliveryOrderCreate']['start_time']) && !empty($param['DeliveryOrderCreate']['end_time'])) {
	            $criteria->addBetweenCondition('create_time', trim($param['DeliveryOrderCreate']['start_time']), trim($param['DeliveryOrderCreate']['end_time']));//between 1 and 4 
	        } else {
	        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
	        }
        }
        $criteria->compare('is_valid', 1);
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $dataProvider = new CActiveDataProvider('DeliveryOrderCreate', array(
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
     * 发货单单头信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'发货单单号' => 'delivery_order_code',
	        '原出库单号（ERP分配）' => 'pre_delivery_order_code',
    	    '原出库单号（WMS分配）' => 'pre_delivery_order_id',
    	    '出库单类型' => 'order_type',
    	    '货主ID' => 'customer_id',
    	    '仓库编码' => 'warehouse_code',
    	    '订单状态' => 'order_status',
    	    '订单标记' => 'order_flag',
    	    '订单来源平台编码' => 'source_platform_code',
    	    '订单来源平台名称' => 'source_platform_name',
            '分销订单标志 ' => 'fx_flag',
            '客户来源 ' => 'fx_customer',
            '网点编码 ' => 'fx_branch_code',
            '网点名称 ' => 'fx_branch_name',
            '是否押金支付 ' => 'fx_is_depositpay',
            '分拨中心编码 ' => 'fx_distbt_code',
            '分拨中心名称 ' => 'fx_distbt_name',
    	    '发货单创建时间' => 'deliv_create_time',
    	    '前台订单 (店铺订单) 创建时间 (下单时间)' => 'place_order_time',
    	    '订单支付时间' => 'pay_time',
    	    '支付平台交易号' => 'pay_no',
    	    '操作员 (审核员) 编码' => 'operator_code',
    	    '操作员 (审核员) 名称' => 'operator_name',
    	    '操作 (审核) 时间' => 'operate_time',
    	    '店铺名称' => 'shop_nick',
    	    '卖家名称' => 'seller_nick',
    	    '买家昵称' => 'buyer_nick',
    	    '订单总金额 (元)' => 'total_amount',
    	    '商品总金额 (元)' => 'item_amount',
    	    '订单折扣金额 (元)' => 'discount_amount',
    	    '快递费用 (元)' => 'freight',
    	    '应收金额 (元) , 消费者还需要支付多少' => 'ar_amount',
    	    '已收金额 (元) , 消费者已经支付多少' => 'got_amount',
    	    'COD服务费' => 'service_fee',
    	    '物流公司编码' => 'logistics_code',
    	    '物流公司名称' => 'logistics_name',
    	    '运单号' => 'express_code',
    	    '快递区域编码, 大头笔信息' => 'logistics_area_code',
    	    '投递时延要求' => 'schedule_type',
    	    '要求送达日期' => 'schedule_day',
    	    '投递时间范围要求 (开始时间)' => 'schedule_start_time',
    	    '投递时间范围要求 (结束时间)' => 'schedule_end_time',
    	    '发货服务类型' => 'delivery_type',
    	    '发件人--公司名称' => 'sender_company',
    	    '发件人--姓名' => 'sender_name',
    	    '发件人--邮编' => 'sender_zipcode',
    	    '发件人--固定电话' => 'sender_tel',
    	    '发件人--移动电话' => 'sender_mobile',
    	    '发件人--电子邮箱' => 'sender_email',
    	    '发件人--国家二字码' => 'sender_countrycode',
    	    '发件人--省份' => 'sender_province',
    	    '发件人--城市' => 'sender_city',
    	    '发件人--区域' => 'sender_area',
    	    '发件人--村镇' => 'sender_town',
    	    '发件人--详细地址' => 'sender_detail_address',
    	    '收件人--公司名称' => 'receiver_company',
    	    '收件人--姓名' => 'receiver_name',
    	    '收件人--邮编' => 'receiver_zipcode',
    	    '收件人--固定电话' => 'receiver_tel',
    	    '收件人--移动电话' => 'receiver_mobile',
    	    '收件人--电子邮箱' => 'receiver_email',
    	    '收件人--国家二字码' => 'receiver_countrycode',
    	    '收件人--省份' => 'receiver_province',
    	    '收件人--城市' => 'receiver_city',
    	    '收件人--区域' => 'receiver_area',
    	    '收件人--村镇' => 'receiver_town',
    	    '收件人--详细地址' => 'receiver_detail_address',
    	    '是否紧急, Y/N, 默认为N' => 'is_urgency',
    	    '是否需要发票, Y/N, 默认为N' => 'invoice_flag',
    	    '是否需要保险, Y/N, 默认为N' => 'insurance_flag',
    	    '保险类型' => 'insurance_type',
    	    '保险金额' => 'insurance_amount',
    	    '买家留言' => 'buyer_message',
    	    '卖家留言' => 'seller_message',
    	    '是否有效 1有效 0 无效' => 'is_valid',
    	    '备注' => 'remark',
    	    '发货单创建时间' => 'create_time'
    	);
    	return $columns;
    }
}