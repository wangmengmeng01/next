<?php

/**
 * 发货单明细model
 * table: t_delivery_order_info
 */
class DeliveryOrderDetail extends CActiveRecord
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
        return '{{delivery_order_detail}}';
    }

    /**
     * Retrieves a list of models based on the current search/filter conditions.
     * 
     * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
     */
    public static function search($param)
    {
        $criteria = new CDbCriteria();
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        if (isset($param['delivery_id'])) {
            $criteria->compare('delivery_id', trim($param['delivery_id']));
        }
        $dataProvider = new CActiveDataProvider('DeliveryOrderDetail', array(
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
     * 出库单单头信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'单据行号' => 'order_line_no',
			'交易平台订单' => 'source_order_code',
    	    '交易平台子订单编码' => 'sub_source_order_code',
    	    '货主编码' => 'customer_id',
    	    '商品编码' => 'item_code',
    	    '仓储系统商品编码' => 'item_id',
    	    '库存类型' => 'inventory_type',
    	    '商品名称' => 'item_name',
    	    '交易平台商品编码' => 'ext_code',
    	    '应发商品数量' => 'plan_qty',
    	    '零售价' => 'retail_price',
    	    '实际成交价' => 'actual_price',
    	    '单件商品折扣金额' => 'discount_amount',
    	    '批次编码' => 'batch_code',
    	    '生产日期' => 'product_date',
    	    '过期日期' => 'expire_date',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}