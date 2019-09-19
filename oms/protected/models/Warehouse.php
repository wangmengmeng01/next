<?php

/**
 * 仓库维护model
 * table: t_base_warehouse
 */
class Warehouse extends CActiveRecord
{

    /**
     * Returns the static model of the specified AR class.
     * 
     * @param string $className
     *            active record class name.
     * @return Warehouse the static model class
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
        return '{{base_warehouse}}';
    }

    /**
     * @return array validation rules for model attributes.
     */
    public function rules()
    {
    	// NOTE: you should only define rules for those attributes that
    	// will receive user inputs.
    	return array(
    			array('warehouse_code,descr_c,branch_code,active_flag,is_valid', 'required'),
    			array('is_valid', 'numerical', 'integerOnly'=>true),
    			array('warehouse_code,wms_url,contact1,contact1_tel1,contact1_tel2,address1,remark,operator_id,operator_name,create_time', 'safe'),
    			// The following rule is used by search().
    			// Please remove those attributes that should not be searched.
    			array('warehouse_code, descr_c, wms_url, branch_code, active_flag, is_valid,create_time', 'safe', 'on'=>'search'),
    	);
    }
       
    /**
     * @return array customized attribute labels (name=>label)
     */
    public function attributeLabels()
    {
    	return array(
    			'warehouse_code' => '仓库编码',
    			'descr_c' => '仓库名称',
                'wms_url' => '仓库接口地址',
    			'branch_code' => '所属网点',
    			'contact1' => '联系人',
    			'contact1_tel1' => '联系人手机号码',
    			'contact1_tel2' => '联系人固话',
    			'address1' => '联系人地址',
    			'active_flag' => '激活标志',
    			'is_valid' => '有效性',
    			'remark' => '备注'
    	);
    }
    
    /**
     * Retrieves a list of models based on the current search/filter conditions.
     * 
     * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
     */
    public static function search()
    {
        $criteria = new CDbCriteria();
        //校验是否有查看所仓库的权限
        $viewFlag = util::hasViewAll();
        if ($viewFlag == 0) {
        	$existsArr = util::getCustomer('WH');
        	if (!empty($existsArr)) {
        		$criteria->addInCondition('warehouse_code', $existsArr);
        	} else {
        		return ;
        	}
        }        
        if (isset($_POST['Warehouse']['warehouse_code'])) {
        	$criteria->compare('warehouse_code', trim($_POST['Warehouse']['warehouse_code']));
        }
        if (isset($_POST['Warehouse']['descr_c'])) {
            $criteria->compare('descr_c', trim($_POST['Warehouse']['descr_c']), true);
        }
        if (isset($_POST['Warehouse']['branch_code'])) {
        	$criteria->compare('branch_code', trim($_POST['Warehouse']['branch_code']), true);
        }
        if (isset($_POST['Warehouse']['active_flag'])) {
        	$criteria->compare('active_flag', trim($_POST['Warehouse']['active_flag']));
        }
        if (isset($_POST['Warehouse']['is_valid'])) {
            $criteria->compare('is_valid', trim($_POST['Warehouse']['is_valid']));
        }
        $_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
        $_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
        $dataProvider = new CActiveDataProvider('Warehouse', array(
            'sort' => array(
                'defaultOrder' => 'create_time Desc'
            ),
            'pagination' => array(
                'pageSize' => $_POST['rows'],
                'currentPage' => $_POST['page'] - 1
            ),
            'criteria' => $criteria
        ));
        return $dataProvider;
    }
}