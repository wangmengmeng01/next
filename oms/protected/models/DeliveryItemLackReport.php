<?php

/**
 * 发货单缺货通知信息model
 * table: t_delivery_order_info
 */
class DeliveryItemLackReport extends CActiveRecord
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
        return '{{item_lack_record}}';
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
        if (!empty($param['DeliveryItemLackReport']['delivery_order_code'])) {
            $criteria->compare('delivery_order_code', trim($param['DeliveryItemLackReport']['delivery_order_code']));
        } else {
	        if (isset($param['DeliveryItemLackReport']['customer_id'])) {
	            $criteria->compare('customer_id', trim($param['DeliveryItemLackReport']['customer_id']));
	        }
	        if (isset($param['DeliveryItemLackReport']['warehouse_code'])) {
	        	$criteria->compare('warehouse_code', trim($param['DeliveryItemLackReport']['warehouse_code']));
	        }
	        if (!empty($param['DeliveryItemLackReport']['start_time']) && !empty($param['DeliveryItemLackReport']['end_time'])) {
	            $criteria->addBetweenCondition('create_time', trim($param['DeliveryItemLackReport']['start_time']), trim($param['DeliveryItemLackReport']['end_time']));//between 1 and 4 
	        } else {
	        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
	        }
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $dataProvider = new CActiveDataProvider('DeliveryItemLackReport', array(
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
     * 发货单缺货通知信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    	    '货主ID' => 'customer_id',
    	    '仓库编码' => 'warehouse_code',
    	    'ERP的发货单编码' => 'delivery_order_code',
    	    '仓库系统的发货单编码' => 'delivery_order_id',
    	    '缺货回告创建时间' => 'lack_create_time',
    	    '外部业务编码' => 'out_biz_code',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}