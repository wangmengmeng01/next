<?php

/**
 * 发货单缺货通知明细model
 * table: t_item_lack_product_record
 */
class DeliveryItemLackReportDetail extends CActiveRecord
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
        return '{{item_lack_product_record}}';
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
        if (isset($param['deliv_lack_id'])) {
            $criteria->compare('deliv_lack_id', trim($param['deliv_lack_id']));
        }
        $dataProvider = new CActiveDataProvider('DeliveryItemLackReportDetail', array(
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
     * 发货单缺货通知明细信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'商品编码' => 'item_code',
			'仓储系统商品ID' => 'item_id',
    	    '库存类型' => 'inventory_type',
    	    '批次编码' => 'batch_code',
    	    '商品生产日期' => 'product_date',
    	    '商品过期日期' => 'expire_date',
    	    '生产批号' => 'produce_code',
    	    '应发商品数量' => 'plan_aty',
    	    '缺货商品数量' => 'lack_aty',
    	    '缺货原因 (系统报缺, 实物报缺)' => 'reason',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}