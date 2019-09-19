<?php

/**
 * 发货单回传明细model
 * table: t_delivery_order_info
 */
class DeliveryRecordDetail extends CActiveRecord
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
        return '{{delivery_order_detail_record}}';
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
        $dataProvider = new CActiveDataProvider('DeliveryRecordDetail', array(
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
     * 发货单明细回传信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'单据行号' => 'order_line_no',
			'交易平台订单编码' => 'order_source_code',
    	    '交易平台子订单编码' => 'sub_source_order_code',
    	    '货主编码' => 'customer_id',
    	    '商品编码' => 'item_code',
    	    '仓储系统商品编码' => 'item_id',
    	    '库存类型' => 'inventory_type',
    	    '商品名称' => 'item_name',
    	    '交易平台商品编码' => 'ext_code',
    	    '应发商品数量' => 'plan_qty',
    	    '实发商品数量' => 'actual_qty',
    	    '批次编码' => 'batch_code',
    	    '商品生产日期' => 'product_date',
    	    '商品生产批号' => 'produce_code',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}