<?php

/**
 * 入库单单头信息model
 * table: t_inbound_info
 */
class PutASNData extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return PutASNData the static model class
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
        return '{{inbound_info}}';
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
        if (!empty($param['PutASNData']['order_no'])) {
            $criteria->compare('order_no', trim($param['PutASNData']['order_no']), true);
        } else {
	        if (isset($param['PutASNData']['order_type'])) {
	            $criteria->compare('order_type', trim($param['PutASNData']['order_type']));
	        }
	        if (isset($param['PutASNData']['customer_id'])) {
	            $criteria->compare('customer_id', trim($param['PutASNData']['customer_id']));
	        }
	        if (isset($param['PutASNData']['warehouse_code'])) {
	        	$criteria->compare('warehouse_code', trim($param['PutASNData']['warehouse_code']));
	        }
	        if (!empty($param['PutASNData']['start_time']) && !empty($param['PutASNData']['end_time'])) {
	            $criteria->addBetweenCondition('create_time', trim($param['PutASNData']['start_time']), trim($param['PutASNData']['end_time']));//between 1 and 4 
	        } else {
	        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
	        }
        }
        $criteria->compare('is_valid', 1);
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $dataProvider = new CActiveDataProvider('PutASNData', array(
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
     * 入库单下发单头信息导出字段
     */
    public static function getColumns()
    {
        $columns = array(
            '订单号' => 'order_no',
            '订单类型' => 'order_type',
            '货主ID' => 'customer_id',
            '仓库编码' => 'warehouse_code',
            '订单状态' => 'order_status',
            '预期到货时间' => 'expected_arrive_time1',
            '快递单号' => 'asn_reference2',
            '平台单号或交易号' => 'asn_reference3',
            '店铺名称' => 'asn_reference4',
            '手机号码' => 'asn_reference5',
            '原出库单号' => 'pono',
            '退货联系人' => 'i_contact',
            '平台旺旺号' => 'issue_party_name',
            '原产国' => 'country_of_origin',
            '目的国' => 'country_of_destination',
            '装货地' => 'place_of_loading',
            '卸货地' => 'place_of_discharge',
            '交货地' => 'placeof_delivery',
            '退货快递公司' => 'user_define1',
            '退货快递单号' => 'user_define2',
            '退货原因' => 'user_define3',
            '订单下发方' => 'user_define4',
            '退换货标识' => 'user_define5',
            '供应商编码' => 'supplier_code',
            '供应商名称' => 'supplier_name',
            '承运人ID' => 'carrier_id',
            '承运人名称' => 'carrier_name',
            '优先级' => 'priority',
            '业务担当' => 'follow_up',
            '备注' => 'remark',
            '入库时间' => 'create_time'
        );
        return $columns;
    }
}