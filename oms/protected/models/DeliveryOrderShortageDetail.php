<?php

/**
 * 发货单缺货明细model
 * table: t_delivery_order_info
 */
class DeliveryOrderShortageDetail extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *        active record class name.
     * @return DeliveryOrderShortageDetail the static model class
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
        return '{{delivery_order_shortage_detail}}';
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
        if (isset($param['order_id'])) {
            $criteria->compare('order_id', trim($param['order_id']));
        }
        $dataProvider = new CActiveDataProvider('DeliveryOrderShortageDetail', array(
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
}