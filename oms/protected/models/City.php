<?php

/**
 * This is the model class for table "ydserver.city".
 *
 * The followings are the available columns in table 'ydserver.city':
 * @property string $CityID
 * @property string $CityName
 * @property string $CityID
 * @property string $AreaCode
 */
class City extends CActiveRecord
{
	/**
	 * Returns the static model of the specified AR class.
	 * @param string $className active record class name.
	 * @return City the static model class
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
		return 'ydserver.city';
	}

	/**
	 * @return array validation rules for model attributes.
	 */
	public function rules()
	{
		// NOTE: you should only define rules for those attributes that
		// will receive user inputs.
		return array(
			array('CityID', 'required'),
			array('CityID, CityID, AreaCode', 'length', 'max'=>6),
			array('CityName', 'length', 'max'=>100),
			// The following rule is used by search().
			// Please remove those attributes that should not be searched.
			array('CityID, CityName, CityID, AreaCode', 'safe', 'on'=>'search'),
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
			'CityID' => 'City',
			'CityName' => 'City Name',
			'CityID' => 'Province',
			'AreaCode' => 'Area Code',
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

		$criteria->compare('CityID',$this->CityID,true);
		$criteria->compare('CityName',$this->CityName,true);
		$criteria->compare('CityID',$this->CityID,true);
		$criteria->compare('AreaCode',$this->AreaCode,true);

		return new CActiveDataProvider($this, array(
			'criteria'=>$criteria,
		));
	}

	public static function getCity($cityId)
	{
		//获取省表中所有数据
		$cities = self::model()->findAll();
		$arr = array();
		array_push($arr,array('CityID'=>0,'CityName'=>'请选择省份'));
		//判断获取数据，是否为空。非空，将省编码为键，省名为值。
		if ( $cities == null ) {
			return '';
		} else {
			if( !empty($cityId) ) {
				foreach( $cities as $item ) {
					if($item->CityID == $cityId){
						array_push($arr,array('CityID'=>$item->CityID,'CityName'=>$item->CityName,'selected'=>true));
					}else{
						array_push($arr,array('CityID'=>$item->CityID,'CityName'=>$item->CityName));
					}
				}
			}else{
				return '';
			}
			return $arr;
		}
	
	}
}