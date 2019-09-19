<?php

/**
 * 仓内加工单确认单头信息model
 * table: t_store_process_order_record
 */
class StoreProcessConfirm extends CActiveRecord
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
        return '{{store_process_order_record}}';
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
        if (!empty($param['StoreProcessConfirm']['process_order_code'])) {
            $criteria->compare('process_order_code', trim($param['StoreProcessConfirm']['process_order_code']));
        } 
        if (!empty($param['StoreProcessConfirm']['start_time']) && !empty($param['StoreProcessConfirm']['end_time'])) {
            $criteria->addBetweenCondition('create_time', trim($param['StoreProcessConfirm']['start_time']), trim($param['StoreProcessConfirm']['end_time']));//between 1 and 4 
        } else {
        	$criteria->addBetweenCondition('create_time', date("Y-m-d").' 00:00:00', date("Y-m-d").' 23:59:59');
        }

        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        $dataProvider = new CActiveDataProvider('StoreProcessConfirm', array(
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
     * 仓内加工单确认单头信息导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'加工单编码' => 'process_order_code',
    	    '仓储系统加工单ID' => 'process_order_id',
    	    '外部业务编码' => 'out_biz_code',
    	    '单据类型' => 'order_type',
    	    '加工单完成时间' => 'order_complete_time',
    	    '实际作业总数量' => 'actual_qty',
    	    '扩展属性' => 'extend_props',
    	    '备注' => 'remark',
    	    '创建时间' => 'create_time'
    	);
    	return $columns;
    }
}