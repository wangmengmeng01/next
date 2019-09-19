<?php

/**
 * 发货地址维护model
 * table: csk_ship_address
 */
class CskShipAddress extends CActiveRecord
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
		return 'csk_ship_address';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('ship_addr_code,ship_address', 'required'),
			array('is_valid', 'numerical', 'integerOnly'=>true),
		    array('oper_id,oper_name,oper_time', 'safe'),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('ship_addr_code,ship_address, is_valid, oper_name, oper_time', 'safe', 'on'=>'search'),
		);
	}


	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
				'ship_addr_code' => '仓库地址编码',
				'ship_address' => '发件地址',
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
		if ( isset($_POST['Addr']['ship_addr_code']) ) {
			$criteria->compare('ship_addr_code',trim($_POST['Addr']['ship_addr_code']));
		}
		if ( isset($_POST['Addr']['ship_address']) ) {
		      $criteria->compare('ship_address',trim($_POST['Addr']['ship_address']));
		}	
		if ( isset($_POST['Addr']['is_valid']) ) {
		      $criteria->compare('is_valid',trim($_POST['Addr']['is_valid']));
		}	
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		$dataProvider = new CActiveDataProvider('CskShipAddress',array(
    		    'sort'=>array(
    		        'defaultOrder'=>'oper_time Desc',
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