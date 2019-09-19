<?php

/**
 * 发货单缺货转仓信息model
 * table: t_delivery_order_shortage
 */
class DeliveryOrderShortage extends CActiveRecord
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
        return '{{delivery_order_shortage}}';
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
        if(Yii::app()->user->getState('login_type') != 'OMS' && Yii::app()->user->getState('login_type') != 'ADMIN'){
            $viewFlag = util::hasViewAll();
            if ($viewFlag == 0) {
                $existsArr = util::getCustomer();
                if (!empty($existsArr)) {
                    $criteria->addInCondition('customer_id', $existsArr);
                } else {
                    return ;
                }
            }   
        }
        
        
        if (!empty($param['DeliveryOrderShortage']['delivery_order_code'])) {
            $criteria->compare('delivery_order_code', trim($param['DeliveryOrderShortage']['delivery_order_code']));
        } else {
	        if (isset($param['DeliveryOrderShortage']['customer_id'])) {
	            $criteria->compare('customer_id', trim($param['DeliveryOrderShortage']['customer_id']));
	        }
	        if (isset($param['DeliveryOrderShortage']['warehouse_code'])) {
	        	$criteria->compare('warehouse_code', trim($param['DeliveryOrderShortage']['warehouse_code']));
	        }
	        if (isset($param['DeliveryOrderShortage']['mid_warehouse_code'])) {
	            $criteria->compare('mid_warehouse_code', trim($param['DeliveryOrderShortage']['mid_warehouse_code']));
	        }
	        if (!empty($param['DeliveryOrderShortage']['erp_create_start_time']) && !empty($param['DeliveryOrderShortage']['erp_create_end_time'])) {
	            $criteria->addBetweenCondition('erp_create_time', trim($param['DeliveryOrderShortage']['erp_create_start_time']), trim($param['DeliveryOrderShortage']['erp_create_end_time']));
	        }
	        if (!empty($param['DeliveryOrderShortage']['create_start_time']) && !empty($param['DeliveryOrderShortage']['create_end_time'])) {
	            $criteria->addBetweenCondition('delivery_create_time', trim($param['DeliveryOrderShortage']['create_start_time']), trim($param['DeliveryOrderShortage']['create_end_time']));
	        } else {
	        	$criteria->addBetweenCondition('delivery_create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
	        }
        }

        if (Yii::app()->user->getState('login_type') == 'OMS'){
            $customer = explode(',', Yii::app()->user->getState('customer_permission'));
            $criteria->addInCondition('customer_id', $customer);
        }
        
        $criteria->compare('done_flag', 0);
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $dataProvider = new CActiveDataProvider('DeliveryOrderShortage', array(
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
     * 找出原先仓库的备选仓库的优先级
     * 查找仓库备选仓库，并在controller里面调用接口去查找这个单子里面所包含商品在这个仓库的库存，以json格式返回。并将
     */
    public function allocateWarehouse($param){
        //连接数据库
        $db = Yii::app()->db;
        
        $warehouseCode = $param['warehouse_code'];
        $customerId = $param['customer_id'];
        $notWarehouseCode = $param['not_warehouse_code'];
        $sql = "SELECT alternative_wh,level FROM t_alternative_warehouse_relation WHERE warehouse_code='$warehouseCode' AND customer_id='$customerId' AND alternative_wh NOT IN (".$notWarehouseCode.") AND is_valid=1";
        $model = $db->createCommand($sql);
        $rs = $model->queryAll();
        return  $rs;
    }
    
    /**
     * 发货单缺货转仓信息导出字段
     */ 
    public static function getColumns()
    {
    	$columns = array(
    	    '发货单号' => 'delivery_order_code',
    	    '货主编码' => 'customer_id',
    	    '仓库编码' => 'warehouse_code',
    	    '缺货商品' => 'short_sku',
    	    '所有商品' => 'all_sku',
    	    '订单创建时间' => 'erp_create_time',
    	    '发货单创建时间 ' => 'delivery_create_time',
    	    '缺货通知时间' => 'create_time'
    	);
    	return $columns;
    }
}