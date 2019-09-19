<?php

/**
 * 仓内加工单取消信息model
 * table: t_store_process_order_cancel
 */
class StoreProcessCancel extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return StoreProcessCancel the static model class
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
        return '{{store_process_order_cancel}}';
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
        if (!empty($param['StoreProcessCancel']['order_no'])) {
            $criteria->compare('order_no', trim($param['StoreProcessCancel']['order_no']));
        } else {
	        if (isset($param['StoreProcessCancel']['customer_id'])) {
	            $criteria->compare('customer_id', trim($param['StoreProcessCancel']['customer_id']));
	        }
	        if (isset($param['StoreProcessCancel']['warehouse_code'])) {
	        	$criteria->compare('warehouse_code', trim($param['StoreProcessCancel']['warehouse_code']));
	        }
	        if (!empty($param['StoreProcessCancel']['start_time']) && !empty($param['StoreProcessCancel']['end_time'])) {
	            $criteria->addBetweenCondition('create_time', trim($param['StoreProcessCancel']['start_time']), trim($param['StoreProcessCancel']['end_time']));//between 1 and 4 
	        } else {
	        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
	        }
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $dataProvider = new CActiveDataProvider('StoreProcessCancel', array(
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
     * 仓内加工单取消信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'仓内加工单号' => 'order_no',
	        '仓储系统单据编码' => 'wms_order_id',
    	    '单据类型' => 'order_type',
    	    '货主ID' => 'customer_id',
    	    '仓库编码' => 'warehouse_code',
    	    '取消原因' => 'reason',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}