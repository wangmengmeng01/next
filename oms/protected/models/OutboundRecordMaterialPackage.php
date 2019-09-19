<?php

/**
 * 出库单回传包裹材料明细信息model
 * table: t_outbound_package_material_record
 */
class OutboundRecordMaterialPackage extends CActiveRecord
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
        return '{{outbound_package_material_record}}';
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
        if (isset($param['package_id'])) {
            $criteria->compare('package_id', trim($param['package_id']));
        }
        $dataProvider = new CActiveDataProvider('OutboundRecordMaterialPackage', array(
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
     * 出库单回传包裹材料明细信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    	    '包材型号' => 'type',
    	    '包材的数量' => 'quantity',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}