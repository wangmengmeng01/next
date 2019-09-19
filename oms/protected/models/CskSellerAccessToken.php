<?php

/**
 * 商家维护model
 * table: csk_seller_access_token
 */
class CskSellerAccessToken extends CActiveRecord
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
		return 'csk_seller_access_token';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('seller_id,access_token,platform_elec,product_type', 'required'),
			array('is_valid', 'numerical', 'integerOnly'=>true),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('seller_id, access_token, platform_elec, product_type, is_valid, time', 'safe', 'on'=>'search'),
		);
	}


	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
				'seller_id' => '商家id',
				'access_token' => '授权口令',
				'platform_elec' => '电子面单平台',
				'product_type' => '物流商产品类型',
				'is_valid' => '有效性',
		        'time' => '授权时间'
		);
	}
	
	/**
	 * Retrieves a list of models based on the current search/filter conditions.
	 * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
	 */
	public static function search()
	{		
		$criteria = new CDbCriteria();
		
        //$criteria->addCondition("is_valid=1"); //查询条件，即where is_valid = 1  		
		if ( isset($_POST['Seller']['seller_id']) ) {
			$criteria->compare('seller_id',trim($_POST['Seller']['seller_id']));
		}
		if ( isset($_POST['Seller']['access_token']) ) {
		      $criteria->compare('access_token',trim($_POST['Seller']['access_token']),true);
		}	
		if ( isset($_POST['Seller']['platform_elec']) ) {
			$criteria->compare('platform_elec',trim($_POST['Seller']['platform_elec']),true);
		}
		if ( isset($_POST['Seller']['is_valid']) ) {
		      $criteria->compare('is_valid',trim($_POST['Seller']['is_valid']));
		}	
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		$dataProvider = new CActiveDataProvider('CskSellerAccessToken',array(
    		    'sort'=>array(
    		        'defaultOrder'=>'time Desc',
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