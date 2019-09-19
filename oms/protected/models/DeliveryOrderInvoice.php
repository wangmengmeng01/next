<?php

/**
 * 发货单发票model
 * table: t_delivery_order_info
 */
class DeliveryOrderInvoice extends CActiveRecord
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
        return '{{delivery_order_bill_info}}';
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
        $dataProvider = new CActiveDataProvider('DeliveryOrderInvoice', array(
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
     * 发货单发票信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'发票类型' => 'type',
    	    '发票抬头' => 'header',
    	    '发票总金额' => 'amount',
    	    '发票内容' => 'content',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}