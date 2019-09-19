<?php

/**
 * 库存盘点通知model
 * table: t_inventory_check_record
 */
class InventoryReport extends CActiveRecord
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
        return '{{inventory_check_record}}';
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
        if (!empty($param['InventoryReport']['check_order_code'])) {
            $criteria->compare('check_order_code', trim($param['InventoryReport']['check_order_code']));
        } else {
	        if (isset($param['InventoryReport']['customer_id'])) {
	            $criteria->compare('customer_id', trim($param['InventoryReport']['customer_id']));
	        }
	        if (isset($param['InventoryReport']['warehouse_code'])) {
	        	$criteria->compare('warehouse_code', trim($param['InventoryReport']['warehouse_code']));
	        }
	        if (!empty($param['InventoryReport']['start_time']) && !empty($param['InventoryReport']['end_time'])) {
	            $criteria->addBetweenCondition('create_time', trim($param['InventoryReport']['start_time']), trim($param['InventoryReport']['end_time']));//between 1 and 4 
	        } else {
	        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
	        }
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $dataProvider = new CActiveDataProvider('InventoryReport', array(
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
     * 库存盘点通知信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    			'货主编码' => 'customer_id',
    			'仓库编码' => 'warehouse_code',
    			'盘点单编码' => 'check_order_code',
    	        '仓储系统的盘点单编码' => 'check_order_id',
    			'外部业务编码' => 'out_biz_code',
        	    '盘点时间' => 'check_time',
    	        '备注' => 'remark',
        	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}