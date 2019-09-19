<?php
/**
 * Notes:承运商配置表model
 * table: csk_merchant_deploy
 * Date: 2019/4/29
 * Time: 15:38
 */
class CskMerchantDeploy extends CActiveRecord
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
        return 'csk_merchant_deploy';
    }

    /**
     * @return array validation rules for model attributes.
     */
    public function rules()
    {
        // NOTE: you should only define rules for those attributes that
        // will receive user inputs.
        return array(
            array('id,vendor_code, provider_code, wms_provider_code,', 'safe'),
        );
    }

    /**
     * @return array customized attribute labels (name=>label)
     */
    public function attributeLabels()
    {
        return array(
            'id' => '自增ID',
            'vendor_code' => '商家编码',
            'provider_code' => '承运商编码',
            'provider_id' => '承运商ID',
            'provider_name' => '承运商名称',
            'wms_provider_code' => 'wms承运商编码',
            'wms_provider_name' => 'wms承运商名称',
            'type' => '平台类型',
            'update_time' => '更新时间',
            'create_time' => '创建时间',
        );
    }

    /**
     * Retrieves a list of models based on the current search/filter conditions.
     * @return CActiveDataProvider the data provider that can return the models based on the search/filter conditions.
     */
    public static function search()
    {
        $criteria = new CDbCriteria();

        if ( isset($_POST['vendor_code']) ) {
            $criteria->compare('vendor_code',trim($_POST['vendor_code']));
        }
        if ( isset($_POST['provider_code']) ) {
            $criteria->compare('provider_code',trim($_POST['provider_code']));
        }
        if ( isset($_POST['wms_provider_code']) ) {
            $criteria->compare('wms_provider_code',trim($_POST['wms_provider_code']));
        }
        $criteria->compare('type',1);
        $_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
        $_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
        $dataProvider = new CActiveDataProvider('CskMerchantDeploy',array(
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