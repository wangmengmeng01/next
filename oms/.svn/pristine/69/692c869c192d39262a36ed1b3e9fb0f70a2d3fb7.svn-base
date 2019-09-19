<?php

/**
 * 出库单取消model
 * table: t_outbound_cancel
 */
class CancelSOData extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return CancelSOData the static model class
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
        return '{{outbound_cancel}}';
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
        if (!empty($param['CancelSOData']['order_no'])) {
            $criteria->compare('order_no', trim($param['CancelSOData']['order_no']));
        } else {
	        if (isset($param['CancelSOData']['order_type'])) {
	            $criteria->compare('order_type', trim($param['CancelSOData']['order_type']));
	        }
	        if (isset($param['CancelSOData']['customer_id'])) {
	            $criteria->compare('customer_id', trim($param['CancelSOData']['customer_id']));
	        }
	        if (isset($param['CancelSOData']['warehouse_code'])) {
	        	$criteria->compare('warehouse_code', trim($param['CancelSOData']['warehouse_code']));
	        }
	        if (!empty($param['CancelSOData']['start_time']) && !empty($param['CancelSOData']['end_time'])) {
	            $criteria->addBetweenCondition('create_time', trim($param['CancelSOData']['start_time']), trim($param['CancelSOData']['end_time']));//between 1 and 4 
	        } else {
	        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
	        }
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '50' : $param['rows'];
        $dataProvider = new CActiveDataProvider('CancelSOData', array(
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
     * 出库单取消信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    			'订单号' => 'order_no',
    			'订单类型' => 'order_type',
    			'货主ID' => 'customer_id',
    			'仓库编码 ' => 'warehouse_code',
    			'取消原因' => 'reason',
    			'入库时间' => 'create_time'
    	);
        return $columns;	
    }
}