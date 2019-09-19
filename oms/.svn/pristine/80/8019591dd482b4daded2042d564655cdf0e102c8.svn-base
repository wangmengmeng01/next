<?php

/**
 * 发货单SN通知信息model
 * table: t_sn_record
 */
class DeliverySnReport extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return DeliverySnReport the static model class
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
        return '{{sn_record}}';
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
        if (!empty($param['DeliverySnReport']['delivery_order_code'])) {
            $criteria->compare('delivery_order_code', trim($param['DeliverySnReport']['delivery_order_code']));
        } else {
	        if (isset($param['DeliverySnReport']['order_type'])) {
	            $criteria->compare('order_type', trim($param['DeliverySnReport']['order_type']));
	        }
	        if (isset($param['DeliverySnReport']['customer_id'])) {
	            $criteria->compare('customer_id', trim($param['DeliverySnReport']['customer_id']));
	        }
	        if (isset($param['DeliverySnReport']['warehouse_code'])) {
	        	$criteria->compare('warehouse_code', trim($param['DeliverySnReport']['warehouse_code']));
	        }
	        if (!empty($param['DeliverySnReport']['start_time']) && !empty($param['DeliverySnReport']['end_time'])) {
	            $criteria->addBetweenCondition('create_time', trim($param['DeliverySnReport']['start_time']), trim($param['DeliverySnReport']['end_time']));//between 1 and 4 
	        } else {
	        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
	        }
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $dataProvider = new CActiveDataProvider('DeliverySnReport', array(
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
     * 发货单SN通知信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'出库单号' => 'delivery_order_code',
	        '仓储系统出库单号' => 'delivery_order_id',
    	    '货主ID' => 'customer_id',
    	    '仓库编码' => 'warehouse_code',
    	    '出库单类型' => 'order_type',
    	    '外部业务编码' => 'out_biz_code',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}