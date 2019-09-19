<?php

/**
 * 供应商类别model
 * table: t_base_supplier
 */
class Supplier extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Supplier the static model class
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
		return '{{base_supplier}}';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
// 			array('supplier_code,descr_c', 'required'),
// 			array('contact1_tel1,is_valid', 'numerical', 'integerOnly'=>true),
// 			array('descr_c,supplier_code', 'length', 'max'=>20),
// 			array('contact1', 'length', 'max'=>20),
// 			array('contact1_tel1,contact1_tel2', 'length', 'max'=>100),
// 			array('country, province, city, county', 'length', 'max'=>6),
// 			array('address1, remark', 'length', 'max'=>255),
// 			array('operator_id', 'length', 'max'=>13),
// 			array('operator_name', 'length', 'max'=>30),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('supplier_code, descr_c, is_valid, active_flag', 'safe', 'on'=>'search'),
		);
	}


	/**
	 * @return array customized attribute labels (name=>label)
	 */
// 	public function attributeLabels()
// 	{
// 		return array(
// 				'supplier_id' => 'Supplier',
// 				'supplier_code' => '设备类别',
// 				'descr_c' => '供应商名称',
// 				'province' => '省',
// 				'city' => '市',
// 				'county' => '区县',
// 				'address1' => '联系人地址',
// 				'contact1' => '联系人',
// 				'contact1_tel1' => '联系人手机号',
// 				'contact1_tel2' => '联系人固话',
// 				'remark' => '备注',
// 				'operator_id' => '操作人ID',
// 				'operator_name' => '操作人姓名',
// 				'create_time' => '创建时间'
// 		);
// 	}
	
	/**
	 * Retrieves a list of models based on the current search/filter conditions.
	 * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
	 */
	public static function search()
	{
		$criteria = new CDbCriteria();
		//校验是否有查看所有供应商的权限
		$viewFlag = util::hasViewAll();
		if ($viewFlag == 0) {
			$existsArr = util::getCustomer('VE');
			if (!empty($existsArr)) {
				$criteria->addInCondition('supplier_code', $existsArr);
			} else {
				return ;
			}
		}			
		if (isset($_POST['Supplier']['supplier_code'])) {
		    $criteria->compare('supplier_code',trim($_POST['Supplier']['supplier_code']));
		}
		if (isset($_POST['Supplier']['descr_c'])) {
			$criteria->compare('descr_c',trim($_POST['Supplier']['descr_c']),true);
		}
		if (isset($_POST['Supplier']['active_flag'])) {
			$criteria->compare('active_flag',trim($_POST['Supplier']['active_flag']));
		}		
		$criteria->compare('is_valid', 1);
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '20' : $_POST['rows'];
		$dataProvider = new CActiveDataProvider('Supplier',array(
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