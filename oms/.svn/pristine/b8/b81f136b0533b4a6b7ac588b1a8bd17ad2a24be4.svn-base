<?php

/**
 * 库存model
 * table: t_product_inventory
 */
class QueryINVData extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return QueryINVData the static model class
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
        return '{{product_inventory}}';
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
        if (isset($param['QueryINVData']['customer_id'])) {
            $criteria->compare('customer_id', trim($param['QueryINVData']['customer_id']));
        }
        if (isset($param['QueryINVData']['warehouse_code'])) {
        	$criteria->compare('warehouse_code', trim($param['QueryINVData']['warehouse_code']));
        }
        if (isset($param['QueryINVData']['sku'])) {
            $criteria->compare('sku', trim($param['QueryINVData']['sku']));
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '50' : $param['rows'];
        $dataProvider = new CActiveDataProvider('QueryINVData', array(
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
     * 入库单下发单头信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
    			'货主ID ' => 'customer_id',
    			'仓库编码 ' => 'warehouse_code',
    			'SKU' => 'sku',
    			'可用数量' => 'qty',
    			'占用数 ' => 'occupy_qty',
    			'总数量' => 'qty_total',
    			'库存版本' => 'invver',
    			'入库时间' => 'create_time'
    	);
    	return $columns;
    }
}