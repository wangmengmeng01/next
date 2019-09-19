<?php

/**
 * 商家维护model
 * table: csk_pdd_access_token
 */
class CskPddAccessToken extends CActiveRecord
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
		return 'csk_pdd_access_token';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return [];
	}


	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
				'shop_id' => '店铺id',
				'seller_id' => '商家id',
				'seller_name' => '商家名称',
				'platform' => '电子面单平台',
				'is_valid' => '有效性',
				'creater' => '创建人',
		        'auth_time' => '授权时间'
		);
	}
	
	/**
	 * Retrieves a list of models based on the current search/filter conditions.
	 * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
	 */
	public static function search()
	{		
		$criteria = new CDbCriteria();
		
		if ( isset($_POST['Seller']['seller_id']) ) {
			$criteria->compare('seller_id',trim($_POST['Seller']['seller_id']));
		}
		if ( isset($_POST['Seller']['is_valid']) ) {
		      $criteria->compare('is_valid',trim($_POST['Seller']['is_valid']));
		}	
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		$dataProvider = new CActiveDataProvider('CskPddAccessToken',array(
    		    'sort'=>array(
    		        'defaultOrder'=>'shop_id Desc',
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