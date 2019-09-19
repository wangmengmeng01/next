<?php

/**
 * 出库单状态回传model
 * table: t_outbound_status_record
 */
class ConfirmSOStatus extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return ConfirmSOStatus the static model class
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
        return '{{outbound_status_record}}';
    }

    /**
     * Retrieves a list of models based on the current search/filter conditions.
     * 
     * @return CActiveStatusProvider the data provider that can return the models based on the search/filter conditions.
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
        if (!empty($param['ConfirmSOStatus']['order_no'])) {
            $criteria->compare('order_no', trim($param['ConfirmSOStatus']['order_no']));
        } else {
	        if (isset($param['ConfirmSOStatus']['order_type'])) {
	            $criteria->compare('order_type', trim($param['ConfirmSOStatus']['order_type']));
	        }
	        if (isset($param['ConfirmSOStatus']['customer_id'])) {
	            $criteria->compare('customer_id', trim($param['ConfirmSOStatus']['customer_id']));
	        }
	        if (!empty($param['ConfirmSOStatus']['start_time']) && !empty($param['ConfirmSOStatus']['end_time'])) {
	            $criteria->addBetweenCondition('create_time', trim($param['ConfirmSOStatus']['start_time']), trim($param['ConfirmSOStatus']['end_time']));//between 1 and 4 
	        } else {
	        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
	        }
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '50' : $param['rows'];
        $dataProvider = new CActiveDataProvider('ConfirmSOStatus', array(
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
     * 出库单状态回传信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    			'订单号' => 'order_no',
    			'订单类型' => 'order_type',
    			'货主ID ' => 'customer_id',
    			'仓库编码' => 'warehouse_code',
    			'订单状态' => 'order_status',
    			'状态描述' => 'order_desc',
    			'操作时间' => 'operator_time',
    			'入库时间' => 'create_time'
    	);
        return $columns;	
    }
}