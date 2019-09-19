<?php

/**
 * 物流公司维护model
 * table: t_base_logistics
 */
class Logistics extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return Logistics the static model class
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
		return '{{base_logistics}}';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('logistics_code,logistics_name', 'required'),
			array('is_valid', 'numerical', 'integerOnly'=>true),
			array('logistics_id,contact,contact_tel,address,remark,operator_id,operator_name,create_time', 'safe'),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('logistics_code, logistics_name, is_valid', 'safe', 'on'=>'search'),
		);
	}


	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
				'logistics_code' => '物流公司编码',
				'logistics_name' => '物流公司名称',
				'contact_phone' => '联系电话',
				'contact_tel' => '联系人固话',				
				'address' => '联系地址',
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
		if ( isset($_POST['Logistics']['logistics_name']) ) {
		      $criteria->compare('logistics_name',trim($_POST['Logistics']['logistics_name']),true);
		}
		if ( isset($_POST['Logistics']['logistics_code']) ) {
		      $criteria->compare('logistics_code',trim($_POST['Logistics']['logistics_code']));
		}
		if ( isset($_POST['Logistics']['is_valid']) ) {
		      $criteria->compare('is_valid',trim($_POST['Logistics']['is_valid']));
		}
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		$dataProvider = new CActiveDataProvider('Logistics',array(
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