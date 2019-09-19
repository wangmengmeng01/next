<?php

/**
 * 商家开通电子面单服务列表model
 * table: csk_seller_waybill_info   
 */
class CskSellerWaybillInfo extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return Product the static model class
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
        return 'csk_seller_waybill_info';
    }
    
    /**
     * @return array validation rules for model attributes.
     */
    public function rules()
    {
        // NOTE: you should only define rules for those attributes that
        // will receive user inputs.
        return array(
            array('seller_id,ship_detail_address', 'required'),
            array('info_id,ship_addr_code,ship_prov,ship_city,ship_county,ship_town,waybill_address_id,branch_code,branch_name,print_quantity,cancel_quantity,allocated_quantity,quantity,cp_type,cp_code', 'safe'),
            // The following rule is used by search().
            // Please remove those attributes that should not be searched.
            array('seller_id, ship_addr_code, ship_detail_address, ship_prov, ship_city, ship_county, ship_town, waybill_address_id, branch_code, branch_name, print_quantity, cancel_quantity, allocated_quantity, quantity, cp_type, cp_code, create_time', 'safe', 'on'=>'search'),
        );	
    }
    
    /**
     * @return array customized attribute labels (name=>label)
     */
    public function attributeLabels()
    {
        return array(
            'seller_id' => '商家ID',
            'ship_addr_code' => '仓库地址编码',
            'ship_prov' => '发件省',
            'ship_city' => '发件城市',
            'ship_county' => '发件县区',
            'ship_town' => '发件乡镇',
            'ship_detail_address' => '发件详细地址',
            'waybill_address_id' => '淘宝定义的发件地址ID',
            'branch_code' => '发件网点编码',
            'branch_name' => '发件网点名称',
            'print_quantity' => '面单打印数',
            'cancel_quantity' => '单号取消数',
            'allocated_quantity' => '总分配单号数',
            'quantity' => '当前余量',
            'cp_type' => '快递公司类型',
            'cp_code' => '快递公司编码',
            'create_time' => '操作时间'
        );
    }

    /**
     * Retrieves a list of models based on the current search/filter conditions.
     * 
     * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
     */
    public static function search($param)
    {
        $criteria = new CDbCriteria();
        //校验是否有查看所有商品的权限
        $viewFlag = util::hasViewAll();
        if ($viewFlag == 0) {
        	$existsArr = util::getCustomer();
        	if (!empty($existsArr)) {
        		$criteria->addInCondition('customer_id', $existsArr);
        	} else {
        		return ;
        	}
        }
        $param['page'] = empty($param['page']) ? '1' : $param['page'];
        $param['rows'] = empty($param['rows']) ? '20' : $param['rows'];
        if (isset($param['WaybillInfo']['seller_id'])) {
            $criteria->compare('seller_id', trim($param['WaybillInfo']['seller_id']));
        }
        if (isset($param['WaybillInfo']['ship_addr_code'])) {
            $criteria->compare('ship_addr_code', trim($param['WaybillInfo']['ship_addr_code']));
        }
        if (isset($param['WaybillInfo']['address_detail'])) {
        	$criteria->compare('address_detail', trim($param['WaybillInfo']['address_detail']));
        }
        if (isset($param['WaybillInfo']['cp_code'])) {
        	$criteria->compare('cp_code', trim($param['WaybillInfo']['cp_code']));
        }
        if (isset($param['WaybillInfo']['branch_code'])) {
            $criteria->compare('branch_code', trim($param['WaybillInfo']['branch_code']));
        }
        if (isset($param['WaybillInfo']['branch_name'])) {
            $criteria->compare('branch_name', trim($param['WaybillInfo']['branch_name']));
        }

        # 因为平台为菜鸟时，类型之为空，条件拼接需手动
        if (isset($param['WaybillInfo']['is_jd']) && $param['WaybillInfo']['is_jd'] != 'all') {

            $criteria->addCondition('is_jd=:ycpJd', 'AND');
            $criteria->params[':ycpJd']=$param['WaybillInfo']['is_jd'];
        }

        $dataProvider = new CActiveDataProvider('CskSellerWaybillInfo', array(
            'sort'=>array(
    		        'defaultOrder'=>'create_time Desc',
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