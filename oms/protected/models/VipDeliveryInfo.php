<?php

/**
 * 唯品会JIT出仓单model
 * table: t_vip_po_list
 */
class VipDeliveryInfo extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return StoreProcessCreate the static model class
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
        return '{{vip_delivery_info}}';
    }


}