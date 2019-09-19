<?php

/**
 * 订单流水通知信息model
 * table: t_order_process_record
 */
class OrderProcessReport extends CActiveRecord
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
        return '{{order_process_record}}';
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
        if (!empty($param['OrderProcessReport']['order_code'])) {
            $criteria->compare('order_code', trim($param['OrderProcessReport']['order_code']));
        } 
        if (!empty($param['OrderProcessReport']['order_type'])) {
            $criteria->compare('order_type', trim($param['OrderProcessReport']['order_type']));
        }
        if (!empty($param['OrderProcessReport']['process_status'])) {
            $criteria->compare('process_status', trim($param['OrderProcessReport']['process_status']));
        }
        if (isset($param['OrderProcessReport']['customer_id'])) {
            $criteria->compare('customer_id', trim($param['OrderProcessReport']['customer_id']));
        }
        if (isset($param['OrderProcessReport']['warehouse_code'])) {
        	$criteria->compare('warehouse_code', trim($param['OrderProcessReport']['warehouse_code']));
        }
        if (!empty($param['OrderProcessReport']['start_time']) && !empty($param['OrderProcessReport']['end_time'])) {
            $criteria->addBetweenCondition('create_time', trim($param['OrderProcessReport']['start_time']), trim($param['OrderProcessReport']['end_time']));//between 1 and 4 
        } else {
        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $dataProvider = new CActiveDataProvider('OrderProcessReport', array(
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
     * 订单流水通知信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'单据编码' => 'order_code',
    	    '仓储系统单据号' => 'order_id',
    	    '仓库编码' => 'warehouse_code',
    	    '单据类型' => 'order_type',
    	    '单据状态' => 'process_status',
    	    '当前状态操作员编码' => 'operator_code',
    	    '当前状态操作员姓名' => 'operator_name',
    	    '当前状态操作时间' => 'operate_time',
    	    '操作内容' => 'operate_info',
    	    '扩展属性' => 'extend_props',
    	    '备注' => 'remark',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}