<?php

/**
 * 唯品会JIT采购单列表model
 * table: t_vip_po_list
 */
class VipJitPoList extends CActiveRecord
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
        return '{{vip_po_list}}';
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
//        $viewFlag = util::hasViewAll();
//        if ($viewFlag == 0) {
//            $existsArr = util::getCustomer();
//            if (!empty($existsArr)) {
//                $criteria->addInCondition('customer_id', $existsArr);
//            } else {
//                return ;
//            }
//        }
        if (!empty($param['VipJitPoList']['po_no'])) {
            $criteria->compare('po_no', trim($param['VipJitPoList']['po_no']));
        }
        if (!empty($param['VipJitPoList']['vendor_name'])) {
            $criteria->compare('vendor_name', trim($param['VipJitPoList']['vendor_name']));
        }
        if (!empty($param['VipJitPoList']['brand_name'])) {
            $criteria->compare('brand_name', trim($param['VipJitPoList']['brand_name']));
        }
        if (!empty($param['VipJitPoList']['schedule_name'])) {
            $criteria->compare('schedule_name', trim($param['VipJitPoList']['schedule_name']));
        }
        if (!empty($param['VipJitPoList']['sell_site'])) {
            $criteria->compare('sell_site', trim($param['VipJitPoList']['sell_site']));
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        //excel导出时pagination参数做特殊处理
        if (isset($param['VipJitPoList']['operate_type']) && $param['VipJitPoList']['operate_type'] == 'excel_export') {
            $pagination = false;
        } else {
            $pagination = array(
                'pageSize' => $param['rows'],
                'currentPage' => $param['page'] - 1
            );
        }
        $dataProvider = new CActiveDataProvider('VipJitPoList', array(
            'sort' => array(
                'defaultOrder' => 'create_time Desc'
            ),
            'pagination' => $pagination,
            'criteria' => $criteria
        ));
        return $dataProvider;
    }
    
    /**
     * 唯品会JIT采购单列表导出字段
     */
    public static function getColumns()
    {
    	$columns = array(
			'po编号' => 'po_no',
			'合作模式' => 'co_mode',
			'档期开始销售时间' => 'sell_st_time',
			'档期结束销售时间' => 'sell_et_time',
			'虚拟总库存' => 'stock',
			'销售数' => 'sales_volume',
			'未拣货数' => 'not_pick',
			'海淘档期新增贸易模式' => 'trade_mode',
			'档期号' => 'schedule_id',
			'供应商' => 'vendor_name',
			'品牌' => 'brand_name',
			'唯品会仓库' => 'sell_site',
			'档期名称' => 'schedule_name',
			'po开始时间' => 'po_start_time',
			'常态合作编码' => 'cooperation_no',
    	);
    	return $columns;
    }
}