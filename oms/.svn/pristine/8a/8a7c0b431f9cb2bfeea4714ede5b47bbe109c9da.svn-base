<?php

/**
 * 仓内加工单确认商品明细model
 * table: t_store_process_order_product_record
 */
class StoreProcessProductConfirm extends CActiveRecord
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
        return '{{store_process_order_product_record}}';
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
        if (isset($param['process_id'])) {
            $criteria->compare('process_id', trim($param['process_id']));
        }
        $dataProvider = new CActiveDataProvider('StoreProcessProductConfirm', array(
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
     * 仓内加工单确认商品明细信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'加工单编码' => 'process_order_code',
			'系统商品编码' => 'item_code',
    	    '仓储系统商品ID' => 'item_id',
    	    '库存类型' => 'inventory_type',
    	    '数量' => 'quantity',
    	    '商品生产日期' => 'product_date',
    	    '商品过期日期' => 'expire_date',
    	    '生产批号' => 'produce_code',
    	    '批次编码' => 'batch_code',
    	    '备注' => 'remark',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}