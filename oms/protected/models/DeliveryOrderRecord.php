<?php

/**
 * 发货单回传model
 * table: t_outbound_info
 */
class DeliveryOrderRecord extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return PutSOData the static model class
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
        return '{{delivery_order_info_record}}';
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
        if (!empty($param['DeliveryOrderRecord']['delivery_order_code'])) {
            $criteria->compare('delivery_order_code', trim($param['DeliveryOrderRecord']['delivery_order_code']));
        } else {
	        if (isset($param['DeliveryOrderRecord']['order_type'])) {
	            $criteria->compare('order_type', trim($param['DeliveryOrderRecord']['order_type']));
	        }
	        if (isset($param['DeliveryOrderRecord']['customer_id'])) {
	            $criteria->compare('customer_id', trim($param['DeliveryOrderRecord']['customer_id']));
	        }
	        if (isset($param['DeliveryOrderRecord']['warehouse_code'])) {
	        	$criteria->compare('warehouse_code', trim($param['DeliveryOrderRecord']['warehouse_code']));
	        }
	        if (!empty($param['DeliveryOrderRecord']['start_time']) && !empty($param['DeliveryOrderRecord']['end_time'])) {
	            $criteria->addBetweenCondition('create_time', trim($param['DeliveryOrderRecord']['start_time']), trim($param['DeliveryOrderRecord']['end_time']));//between 1 and 4 
	        } else {
	        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
	        }
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $dataProvider = new CActiveDataProvider('DeliveryOrderRecord', array(
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
     * 发货单回传信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    			'出库单号' => 'delivery_order_code',
    			'仓储系统出库单号' => 'delivery_order_id',
    			'仓库编码' => 'warehouse_code',
    			'出库单类型' => 'order_type',
    			'货主ID ' => 'customer_id',
    			'出库单状态' => 'order_status',
    			'外部业务编码' => 'out_biz_code',
        	    '支持出库单多次发货' => 'confirm_type',
    			'订单完成时间' => 'order_confirm_time',
        	    '当前状态操作员编码' => 'operator_code',
        	    '当前状态操作员姓名' => 'operator_name',
        	    '当前状态操作时间' => 'operate_time',
        	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}