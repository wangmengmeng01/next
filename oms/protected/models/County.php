<?php

/**
 * This is the model class for table "ydserver.county".
 *
 * The followings are the available columns in table 'ydserver.county':
 * @property string $CountyID
 * @property string $CountyName
 * @property string $CityID
 * @property integer $CLC_ID
 */
class County extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return County the static model class
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
		return 'ydserver.county';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('CountyID', 'required'),
			array('CLC_ID', 'numerical', 'integerOnly'=>true),
			array('CountyID, CityID', 'length', 'max'=>6),
			array('CountyName', 'length', 'max'=>200),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('CountyID, CountyName, CityID, CLC_ID', 'safe', 'on'=>'search'),
		);
	}

	/**
	 * @return array relational rules.
	 */
	public function relations()
	{
		// NOTE: you may need to adjust the relation name and the related
		// class name for the relations automatically generated below.
		return array(
		);
	}

	/**
	 * @return array customized attribute labels (name=>label)
	 */
	public function attributeLabels()
	{
		return array(
			'CountyID' => 'County',
			'CountyName' => 'County Name',
			'CityID' => 'City',
			'CLC_ID' => 'Clc',
		);
	}

	/**
	 * Retrieves a list of models based on the current search/filter conditions.
	 * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
	 */
	public function search()
	{
		// Warning: Please modify the following code to remove attributes that
		// should not be searched.

		$criteria=new CDbCriteria;

		$criteria->compare('CountyID',$this->CountyID,true);
		$criteria->compare('CountyName',$this->CountyName,true);
		$criteria->compare('CityID',$this->CityID,true);
		$criteria->compare('CLC_ID',$this->CLC_ID);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
	}

	public static function getCounty($countyId='')
	{
		//获取省表中所有数据
		$cities = self::model()->findAll();
		$arr = array();
		array_push($arr,array('CountyID'=>0,'CountyName'=>'请选择省份'));
		//判断获取数据，是否为空。非空，将省编码为键，省名为值。
		if ( $cities == null ) {
			return '';
		} else {
			if( !empty($countyId) ) {
				foreach( $cities as $item ) {
					if($item->CountyID == $countyId){
						array_push($arr,array('CountyID'=>$item->CountyID,'CountyName'=>$item->CountyName,'selected'=>true));
					}else{
						array_push($arr,array('CountyID'=>$item->CountyID,'CountyName'=>$item->CountyName));
					}
				}
				return $arr;
			}else{
				return '';
			}
		}
	
	}
}