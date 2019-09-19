<?php

/**
 * 仓内加工单下发单头信息model
 * table: t_store_process_order_info
 */
class StoreProcessCreate extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return StoreProcessCreate the static model class
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
        return '{{store_process_order_info}}';
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
        if (!empty($param['StoreProcessCreate']['process_order_code'])) {
            $criteria->compare('process_order_code', trim($param['StoreProcessCreate']['process_order_code']));
        } 
        if (isset($param['StoreProcessCreate']['customer_id'])) {
            $criteria->compare('customer_id', trim($param['StoreProcessCreate']['customer_id']));
        }
        if (isset($param['StoreProcessCreate']['warehouse_code'])) {
        	$criteria->compare('warehouse_code', trim($param['StoreProcessCreate']['warehouse_code']));
        }
        if (!empty($param['StoreProcessCreate']['start_time']) && !empty($param['StoreProcessCreate']['end_time'])) {
            $criteria->addBetweenCondition('create_time', trim($param['StoreProcessCreate']['start_time']), trim($param['StoreProcessCreate']['end_time']));//between 1 and 4 
        } else {
        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
        }
        $criteria->compare('is_valid', 1);
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $dataProvider = new CActiveDataProvider('StoreProcessCreate', array(
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
     * 仓内加工单单头信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'加工单编码' => 'process_order_code',
	        '客户编码' => 'customer_id',
    	    '仓库编码' => 'warehouse_code',
    	    '单据类型' => 'order_type',
    	    '订单状态' => 'status',
    	    '加工单创建时间' => 'order_create_time',
    	    '计划加工时间' => 'plan_time',
    	    '加工类型' => 'service_type',
    	    '成品计划数量' => 'plan_qty',
    	    '扩展属性' => 'extend_props',
    	    '备注' => 'remark',
    	    '是否有效 1有效 0 无效' => 'is_valid',
    	    '发货单创建时间' => 'create_time'
    	);
    	return $columns;
    }
}