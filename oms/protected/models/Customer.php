<?php

/**
 * 货主维护model
 * table: t_base_customer
 */
class Customer extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Customer the static model class
	 */
	public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}

	/**
	 * @return string the associated database table name
	 */
	public function tableName()
	{
		return '{{base_customer}}';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('customer_id,customer_name,branch_code,active_flag,app_secret', 'required'),
			array('is_valid', 'numerical', 'integerOnly'=>true),
			array('contact1,contact1_tel1,contact1_tel2,address1,remark,operator_id,operator_name,create_time', 'safe'),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('customer_id, customer_name, branch_code, active_flag, is_valid,create_time', 'safe', 'on'=>'search'),
		);
	}


	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
				'customer_id' => '货主',
				'customer_name' => '货主名称',
				'branch_code' => '所属网点',
				'app_secret' => '接口秘钥',
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
	 * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
	 */
	public static function search()
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
        //$criteria->addCondition("is_valid=1"); //查询条件，即where is_valid = 1  		
		if ( isset($_POST['Customer']['customer_id']) ) {
			$criteria->compare('customer_id',trim($_POST['Customer']['customer_id']));
		}
		if ( isset($_POST['Customer']['customer_name']) ) {
		      $criteria->compare('customer_name',trim($_POST['Customer']['customer_name']),true);
		}	
		if ( isset($_POST['Customer']['branch_code']) ) {
			$criteria->compare('branch_code',trim($_POST['Customer']['branch_code']),true);
		}
		if ( isset($_POST['Customer']['active_flag']) ) {
			$criteria->compare('active_flag',trim($_POST['Customer']['active_flag']));
		}	
		if ( isset($_POST['Customer']['is_valid']) ) {
		      $criteria->compare('is_valid',trim($_POST['Customer']['is_valid']));
		}	
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		$dataProvider = new CActiveDataProvider('Customer',array(
    		    'sort'=>array(
    		        'defaultOrder'=>'create_time Desc',
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