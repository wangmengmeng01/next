<?php
/**
 * 货主与ERP和WMS维护model
 * table: t_bind_relation
 * @author wp
 *
 */

class CustomerBind extends CActiveRecord
{
    public static function model($className=__CLASS__)
	{
		return parent::model($className);
	}
	
	public function tableName()
	{
		return '{{bind_relation}}';
	}
	
	public function rules()
	{
		return array(
				array('customer_id,erp_code,erp_api_url,erp_api_ver,wms_code,wms_api_url,wms_api_ver', 'required'),
				array('is_valid', 'numerical', 'integerOnly'=>true),
				array('operator_id,operator_name,create_time', 'safe'),
				// The following rule is used by search().
				// Please remove those attributes that should not be searched.
				array('customer_id,erp_code,wms_code,is_valid', 'safe', 'on'=>'search'),
		);
	}
	
	public function attributeLabels()
	{
		return array(
				'customer_id' => '货主ID',
				'erp_code' => 'ERP编码',
				'erp_api_url' => 'ERP接口地址',
				'erp_api_ver' => 'ERP版本号',
				'wms_code' => 'WMS编码',
				'wms_api_url' => 'WMS接口地址',
				'wms_api_ver' => 'WMS版本号',				
				'is_valid' => '有效性'
		);
	}
	
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
		if ( isset($_POST['CustomerBind']['customer_id']) ) {
			$criteria->compare('customer_id',trim($_POST['CustomerBind']['customer_id']));
		}
		if ( isset($_POST['CustomerBind']['erp_code']) ) {
			$criteria->compare('erp_code',trim($_POST['CustomerBind']['erp_code']));
		}
		if ( isset($_POST['CustomerBind']['wms_code']) ) {
			$criteria->compare('wms_code',trim($_POST['CustomerBind']['wms_code']));
		}
		if ( isset($_POST['CustomerBind']['is_valid']) ) {
			$criteria->compare('is_valid',trim($_POST['CustomerBind']['is_valid']));
		}
		$_POST['page'] = empty($_POST['page']) ? '1' : $_POST['page'];
		$_POST['rows'] = empty($_POST['rows']) ? '50' : $_POST['rows'];
		$dataProvider = new CActiveDataProvider('CustomerBind',array(
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
	
	//定义数据库字段和接口参数的对应关系
	public static function paramRelaation($type)
	{
		$returnArr = array();
		if ($type == 'warehouse') {					
			$returnArr['CustomerID'] = 'warehouse_code';
			$returnArr['Descr_C'] = 'descr_c';
			$returnArr['Descr_E'] = 'descr_e';
			$returnArr['Address1'] = 'address1';
			$returnArr['Address2'] = 'address2';
			$returnArr['Address3'] = 'address3';
			$returnArr['Country'] = 'country';
			$returnArr['Province'] = 'province';
			$returnArr['City'] = 'city';
			$returnArr['Zip'] = 'zip';
			$returnArr['Contact1'] = 'contact1';
			$returnArr['Contact1_Tel1'] = 'contact1_tel1';
			$returnArr['Contact1_Tel2'] = 'contact1_tel2';
			$returnArr['Contact1_Fax'] = 'contact1_fax';
			$returnArr['Contact1_Title'] = 'contact1_title';
			$returnArr['Contact1_Email'] = 'contact1_email';
			$returnArr['Contact2'] = 'contact2';
			$returnArr['Contact2_Tel1'] = 'contact2_tel1';
			$returnArr['Contact2_Tel2'] = 'contact2_tel2';
			$returnArr['Contact2_Fax'] = 'contact2_fax';
			$returnArr['Contact2_Title'] = 'contact2_title';
			$returnArr['Contact3'] = 'contact3';
			$returnArr['Contact3_Tel1'] = 'contact3_tel1';
			$returnArr['Contact3_Tel2'] = 'contact3_tel2';
			$returnArr['Contact3_Fax'] = 'contact3_fax';
			$returnArr['Contact3_Title'] = 'contact3_title';
			$returnArr['Currency'] = 'currency';
			$returnArr['RouteCode'] = 'route_code';
			$returnArr['Stop'] = 'stop';
			$returnArr['R_Owner'] = 'r_owner';
			$returnArr['UDF1'] = 'udf1';
			$returnArr['UDF2'] = 'udf2';
			$returnArr['UDF3'] = 'udf3';
			$returnArr['UDF4'] = 'udf4';
			$returnArr['UDF5'] = 'udf5';
			$returnArr['NOTES'] = 'remark';
			$returnArr['BankAccount'] = 'bank_account';
			$returnArr['easycode'] = 'easy_code';
			$returnArr['Active_Flag'] = 'active_flag';
		} elseif ($type == 'customer') {
    		$returnArr['CustomerID'] = 'customer_code';
    		$returnArr['Descr_C'] = 'descr_c';
    		$returnArr['Descr_E'] = 'descr_e';
    		$returnArr['Address1'] = 'address1';
    		$returnArr['Address2'] = 'address2';
    		$returnArr['Address3'] = 'address3';
    		$returnArr['Country'] = 'country';
    		$returnArr['Province'] = 'province';
    		$returnArr['City'] = 'city';
    		$returnArr['Zip'] = 'zip';
    		$returnArr['Contact1'] = 'contact1';
    		$returnArr['Contact1_Tel1'] = 'contact1_tel1';
    		$returnArr['Contact1_Tel2'] = 'contact1_tel2';
    		$returnArr['Contact1_Fax'] = 'contact1_fax';
    		$returnArr['Contact1_Title'] = 'contact1_title';
    		$returnArr['Contact1_Email'] = 'contact1_email';
    		$returnArr['Contact2'] = 'contact2';
    		$returnArr['Contact2_Tel1'] = 'contact2_tel1';
    		$returnArr['Contact2_Tel2'] = 'contact2_tel2';
    		$returnArr['Contact2_Fax'] = 'contact2_fax';
    		$returnArr['Contact2_Title'] = 'contact2_title';
    		$returnArr['Contact3'] = 'contact3';
    		$returnArr['Contact3_Tel1'] = 'contact3_tel1';
    		$returnArr['Contact3_Tel2'] = 'contact3_tel2';
    		$returnArr['Contact3_Fax'] = 'contact3_fax';
    		$returnArr['Contact3_Title'] = 'contact3_title';
    		$returnArr['Currency'] = 'currency';
    		$returnArr['RouteCode'] = 'route_code';
    		$returnArr['Stop'] = 'stop';
    		$returnArr['R_Owner'] = 'r_owner';
    		$returnArr['UDF1'] = 'udf1';
    		$returnArr['UDF2'] = 'udf2';
    		$returnArr['UDF3'] = 'udf3';
    		$returnArr['UDF4'] = 'udf4';
    		$returnArr['UDF5'] = 'udf5';
    		$returnArr['NOTES'] = 'remark';
    		$returnArr['BankAccount'] = 'bank_account';
    		$returnArr['easycode'] = 'easy_code';
    		$returnArr['Active_Flag'] = 'active_flag';
    	} elseif ($type == 'supplier') {
    		$returnArr['CustomerID'] = 'supplier_code';
    		$returnArr['Descr_C'] = 'descr_c';
    		$returnArr['Descr_E'] = 'descr_e';
    		$returnArr['Address1'] = 'address1';
    		$returnArr['Address2'] = 'address2';
    		$returnArr['Address3'] = 'address3';
    		$returnArr['Country'] = 'country';
    		$returnArr['Province'] = 'province';
    		$returnArr['City'] = 'city';
    		$returnArr['Zip'] = 'zip';
    		$returnArr['Contact1'] = 'contact1';
    		$returnArr['Contact1_Tel1'] = 'contact1_tel1';
    		$returnArr['Contact1_Tel2'] = 'contact1_tel2';
    		$returnArr['Contact1_Fax'] = 'contact1_fax';
    		$returnArr['Contact1_Title'] = 'contact1_title';
    		$returnArr['Contact1_Email'] = 'contact1_email';
    		$returnArr['Contact2'] = 'contact2';
    		$returnArr['Contact2_Tel1'] = 'contact2_tel1';
    		$returnArr['Contact2_Tel2'] = 'contact2_tel2';
    		$returnArr['Contact2_Fax'] = 'contact2_fax';
    		$returnArr['Contact2_Title'] = 'contact2_title';
    		$returnArr['Contact3'] = 'contact3';
    		$returnArr['Contact3_Tel1'] = 'contact3_tel1';
    		$returnArr['Contact3_Tel2'] = 'contact3_tel2';
    		$returnArr['Contact3_Fax'] = 'contact3_fax';
    		$returnArr['Contact3_Title'] = 'contact3_title';
    		$returnArr['Currency'] = 'currency';
    		$returnArr['RouteCode'] = 'route_code';
    		$returnArr['Stop'] = 'stop';
    		$returnArr['R_Owner'] = 'r_owner';
    		$returnArr['UDF1'] = 'udf1';
    		$returnArr['UDF2'] = 'udf2';
    		$returnArr['UDF3'] = 'udf3';
    		$returnArr['UDF4'] = 'udf4';
    		$returnArr['UDF5'] = 'udf5';
    		$returnArr['NOTES'] = 'remark';
    		$returnArr['BankAccount'] = 'bank_account';
    		$returnArr['easycode'] = 'easy_code';
    		$returnArr['Active_Flag'] = 'active_flag';
    	} elseif ($type == 'shop') {
    		$returnArr['CustomerID'] = 'shop_code';
    		$returnArr['Descr_C'] = 'descr_c';
    		$returnArr['Descr_E'] = 'descr_e';
    		$returnArr['Address1'] = 'address1';
    		$returnArr['Address2'] = 'address2';
    		$returnArr['Address3'] = 'address3';
    		$returnArr['Country'] = 'country';
    		$returnArr['Province'] = 'province';
    		$returnArr['City'] = 'city';
    		$returnArr['Zip'] = 'zip';
    		$returnArr['Contact1'] = 'contact1';
    		$returnArr['Contact1_Tel1'] = 'contact1_tel1';
    		$returnArr['Contact1_Tel2'] = 'contact1_tel2';
    		$returnArr['Contact1_Fax'] = 'contact1_fax';
    		$returnArr['Contact1_Title'] = 'contact1_title';
    		$returnArr['Contact1_Email'] = 'contact1_email';
    		$returnArr['Contact2'] = 'contact2';
    		$returnArr['Contact2_Tel1'] = 'contact2_tel1';
    		$returnArr['Contact2_Tel2'] = 'contact2_tel2';
    		$returnArr['Contact2_Fax'] = 'contact2_fax';
    		$returnArr['Contact2_Title'] = 'contact2_title';
    		$returnArr['Contact3'] = 'contact3';
    		$returnArr['Contact3_Tel1'] = 'contact3_tel1';
    		$returnArr['Contact3_Tel2'] = 'contact3_tel2';
    		$returnArr['Contact3_Fax'] = 'contact3_fax';
    		$returnArr['Contact3_Title'] = 'contact3_title';
    		$returnArr['Currency'] = 'currency';
    		$returnArr['RouteCode'] = 'route_code';
    		$returnArr['Stop'] = 'stop';
    		$returnArr['R_Owner'] = 'r_owner';
    		$returnArr['UDF1'] = 'udf1';
    		$returnArr['UDF2'] = 'udf2';
    		$returnArr['UDF3'] = 'udf3';
    		$returnArr['UDF4'] = 'udf4';
    		$returnArr['UDF5'] = 'udf5';
    		$returnArr['NOTES'] = 'remark';
    		$returnArr['BankAccount'] = 'bank_account';
    		$returnArr['easycode'] = 'easy_code';
    		$returnArr['Active_Flag'] = 'active_flag';
    	}
		return $returnArr;
	}
}