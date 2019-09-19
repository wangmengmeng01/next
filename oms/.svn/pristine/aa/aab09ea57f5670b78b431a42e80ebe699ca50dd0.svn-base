<?php

/**
 * WMS软件维护model
 * table: t_base_wms
 */
class Wms extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Wms the static model class
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
		return '{{base_wms}}';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('wms_code,wms_name,cilent_customerid,app_token,app_key,app_secret', 'required'),
			array('is_valid', 'numerical', 'integerOnly'=>true),
			array('wms_id,cilent_customerid,app_token,app_key,app_secret,contact,contact_phone,contact_tel,address,remark,operator_id,operator_name,create_time', 'safe'),			
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('wms_code, wms_name, is_valid', 'safe', 'on'=>'search')
		);
	}


	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
				'wms_code' => 'ERP编码',
				'wms_name' => 'ERP名称',
				'contact' => '联系人',
				'contact_phone' => '联系人手机号',
				'contact_tel' => '联系人固话',				
				'address' => '联系人地址',
				'remark' => '备注',
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
		//校验是否有查看所有WMS的权限
		$viewFlag = util::hasViewAll();
		if ($viewFlag == 0) {
			$existsArr = util::getCustomer('WMS');
			if (!empty($existsArr)) {
				$criteria->addInCondition('wms_code', $existsArr);
			} else {
				return ;
			}
		}		
		if ( isset($_POST['Wms']['wms_name']) ) {
		      $criteria->compare('wms_name',trim($_POST['Wms']['wms_name']),true);
		}
		if ( isset($_POST['Wms']['wms_code']) ) {
		      $criteria->compare('wms_code',trim($_POST['Wms']['wms_code']));
		}
		if ( isset($_POST['Wms']['is_valid']) ) {
		      $criteria->compare('is_valid',trim($_POST['Wms']['is_valid']));
		}
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		$dataProvider = new CActiveDataProvider('Wms',array(
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