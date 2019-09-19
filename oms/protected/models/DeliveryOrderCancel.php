<?php

/**
 * 发货单取消信息model
 * table: t_delivery_order_cancel
 */
class DeliveryOrderCancel extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return DeliveryOrderCancel the static model class
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
        return '{{delivery_order_cancel}}';
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
        if (!empty($param['DeliveryOrderCancel']['order_no'])) {
            $criteria->compare('order_no', trim($param['DeliveryOrderCancel']['order_no']));
        } else {
	        if (isset($param['DeliveryOrderCancel']['order_type'])) {
	            $criteria->compare('order_type', trim($param['DeliveryOrderCancel']['order_type']));
	        }
	        if (isset($param['DeliveryOrderCancel']['customer_id'])) {
	            $criteria->compare('customer_id', trim($param['DeliveryOrderCancel']['customer_id']));
	        }
	        if (isset($param['DeliveryOrderCancel']['warehouse_code'])) {
	        	$criteria->compare('warehouse_code', trim($param['DeliveryOrderCancel']['warehouse_code']));
	        }
	        if (!empty($param['DeliveryOrderCancel']['start_time']) && !empty($param['DeliveryOrderCancel']['end_time'])) {
	            $criteria->addBetweenCondition('create_time', trim($param['DeliveryOrderCancel']['start_time']), trim($param['DeliveryOrderCancel']['end_time']));//between 1 and 4 
	        } else {
	        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
	        }
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $dataProvider = new CActiveDataProvider('DeliveryOrderCancel', array(
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
     * 发货单取消信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'发货单号' => 'order_no',
	        '仓储系统单据编码' => 'wms_order_id',
    	    '出库单类型' => 'order_type',
    	    '货主ID' => 'customer_id',
    	    '仓库编码' => 'warehouse_code',
    	    '取消原因' => 'reason',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}