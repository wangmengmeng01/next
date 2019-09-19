<?php

/**
 * 库存异动通知model
 * table: t_stock_change_record
 */
class StockChangeReport extends CActiveRecord
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
        return '{{stock_change_record}}';
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
        if (!empty($param['StockChangeReport']['order_code'])) {
            $criteria->compare('order_code', trim($param['StockChangeReport']['order_code']));
        } else {
	        if (isset($param['StockChangeReport']['order_type'])) {
	            $criteria->compare('order_type', trim($param['StockChangeReport']['order_type']));
	        }
	        if (isset($param['StockChangeReport']['customer_id'])) {
	            $criteria->compare('customer_id', trim($param['StockChangeReport']['customer_id']));
	        }
	        if (isset($param['StockChangeReport']['warehouse_code'])) {
	        	$criteria->compare('warehouse_code', trim($param['StockChangeReport']['warehouse_code']));
	        }
	        if (!empty($param['StockChangeReport']['start_time']) && !empty($param['StockChangeReport']['end_time'])) {
	            $criteria->addBetweenCondition('create_time', trim($param['StockChangeReport']['start_time']), trim($param['StockChangeReport']['end_time']));//between 1 and 4 
	        } else {
	        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
	        }
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $dataProvider = new CActiveDataProvider('StockChangeReport', array(
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
     * 库存异动通知信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    			'货主编码' => 'customer_id',
    			'仓库编码' => 'warehouse_code',
    			'引起异动的单据编码' => 'order_code',
    	        '单据类型' => 'order_type',
    			'外部业务编码' => 'out_biz_code',
        	    '商品编码' => 'item_code',
    			'仓储系统商品ID' => 'item_id',
        	    '库存类型' => 'inventory_type',
        	    '盘盈盘亏商品变化量' => 'quantity',
        	    '批次编码' => 'batch_code',
    	        '商品生产日期' => 'product_date',
    	        '商品过期日期' => 'expire_date',
    	        '生产批号' => 'produce_code',
    	        '备注' => 'remark',
        	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}