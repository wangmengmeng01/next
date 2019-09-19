<?php

/**
 * 奇门货主配置维护model
 * table: t_qimen_customer
 */
class QimenCustomer extends CActiveRecord
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
		return '{{qimen_customer}}';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('customer_id,customer_name,wms_app_key,wms_secret,erp_code,erp_api_ver,erp_api_url,wms_code,wms_api_ver,wms_api_url', 'required'),
			array('is_valid', 'numerical', 'integerOnly'=>true),
			array('descr_c,descr_e,contact,contact_mobile,contact_phone,remark,operator_id,operator_name,create_time', 'safe')
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			//array('customer_id, customer_name, branch_code, active_flag, is_valid,create_time', 'safe', 'on'=>'search'),
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
		        'wms_app_key' => '奇门WMS的app_key',
		        'wms_secret' => '奇门WMS密码',
		        'descr_c' => '中文描述',
		        'descr_e' => '英文描述',
		        'contact' => '联系人',
    		    'contact_mobile' => '联系人手机号',
    		    'contact_phone' => '联系人固话',
    		    'erp_code' => 'ERP编码',
		        'erp_api_ver' => 'erp接口版本',
		        'erp_api_url' => 'ERP接口地址',
				'wms_code' => 'wms编码',
				'wms_api_ver' => 'wms接口版本',
				'wms_api_url' => 'WMS接口地址',
				'remark' => '备注',
				'operator_id' => '操作人ID',				
				'operator_name' => '操作人姓名',				
				'is_valid' => '有效性'
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
		if ( isset($_POST['QIMEN_Customer']['customer_id']) ) {
			$criteria->compare('customer_id',trim($_POST['QIMEN_Customer']['customer_id']));
		}
		if ( isset($_POST['QIMEN_Customer']['customer_name']) ) {
		      $criteria->compare('customer_name',trim($_POST['QIMEN_Customer']['customer_name']),true);
		}	
		if ( isset($_POST['QIMEN_Customer']['is_valid']) ) {
		      $criteria->compare('is_valid',trim($_POST['QIMEN_Customer']['is_valid']));
		}	
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		$dataProvider = new CActiveDataProvider('QimenCustomer',array(
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