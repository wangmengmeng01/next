<?php
/**
 * oms请求基类
 */
class omsRequest
{

    public static $customerId = '';//客户ID
    public  $msgObj = null;
    public  $utilObj = null;

    public function __construct()
    {
        $this->msgObj = new msg();
        $this->utilObj = new util();
    }   

    /**
     * 接口参数与数据库字段对应关系
     * @param  $type 类型（wareHouse:仓库  customer:货主  goods:商品  supplier:供应商  shop:店铺 ）
     * @return array
     */
    public function get_dataBase_relation($type)
    {
    	$return_arr = array();
    	if ($type == 'wareHouse') {
    		$return_arr['CustomerID'] = 'warehouse_code';
    		$return_arr['Descr_C'] = 'descr_c';
    		$return_arr['Descr_E'] = 'descr_e';
    		$return_arr['Address1'] = 'address1';
    		$return_arr['Address2'] = 'address2';
    		$return_arr['Address3'] = 'address3';
    		$return_arr['Country'] = 'country';
    		$return_arr['Province'] = 'province';
    		$return_arr['City'] = 'city';
    		$return_arr['Zip'] = 'zip';
    		$return_arr['Contact1'] = 'contact1';
    		$return_arr['Contact1_Tel1'] = 'contact1_tel1';
    		$return_arr['Contact1_Tel2'] = 'contact1_tel2';
    		$return_arr['Contact1_Fax'] = 'contact1_fax';
    		$return_arr['Contact1_Title'] = 'contact1_title';
    		$return_arr['Contact1_Email'] = 'contact1_email';
    		$return_arr['Contact2'] = 'contact2';
    		$return_arr['Contact2_Tel1'] = 'contact2_tel1';
    		$return_arr['Contact2_Tel2'] = 'contact2_tel2';
    		$return_arr['Contact2_Fax'] = 'contact2_fax';
    		$return_arr['Contact2_Title'] = 'contact2_title';
    		$return_arr['Contact3'] = 'contact3';
    		$return_arr['Contact3_Tel1'] = 'contact3_tel1';
    		$return_arr['Contact3_Tel2'] = 'contact3_tel2';
    		$return_arr['Contact3_Fax'] = 'contact3_fax';
    		$return_arr['Contact3_Title'] = 'contact3_title';
    		$return_arr['Currency'] = 'currency';
    		$return_arr['RouteCode'] = 'route_code';
    		$return_arr['Stop'] = 'stop';
    		$return_arr['R_Owner'] = 'r_owner';
    		$return_arr['UDF1'] = 'udf1';
    		$return_arr['UDF2'] = 'udf2';
    		$return_arr['UDF3'] = 'udf3';
    		$return_arr['UDF4'] = 'udf4';
    		$return_arr['UDF5'] = 'udf5';
    		$return_arr['NOTES'] = 'remark';
    		$return_arr['BankAccount'] = 'bank_account';
    		$return_arr['easycode'] = 'easy_code';
    		$return_arr['Active_Flag'] = 'active_flag';
    	} elseif ($type == 'customer') {
    		$return_arr['CustomerID'] = 'customer_code';
    		$return_arr['Descr_C'] = 'descr_c';
    		$return_arr['Descr_E'] = 'descr_e';
    		$return_arr['Address1'] = 'address1';
    		$return_arr['Address2'] = 'address2';
    		$return_arr['Address3'] = 'address3';
    		$return_arr['Country'] = 'country';
    		$return_arr['Province'] = 'province';
    		$return_arr['City'] = 'city';
    		$return_arr['Zip'] = 'zip';
    		$return_arr['Contact1'] = 'contact1';
    		$return_arr['Contact1_Tel1'] = 'contact1_tel1';
    		$return_arr['Contact1_Tel2'] = 'contact1_tel2';
    		$return_arr['Contact1_Fax'] = 'contact1_fax';
    		$return_arr['Contact1_Title'] = 'contact1_title';
    		$return_arr['Contact1_Email'] = 'contact1_email';
    		$return_arr['Contact2'] = 'contact2';
    		$return_arr['Contact2_Tel1'] = 'contact2_tel1';
    		$return_arr['Contact2_Tel2'] = 'contact2_tel2';
    		$return_arr['Contact2_Fax'] = 'contact2_fax';
    		$return_arr['Contact2_Title'] = 'contact2_title';
    		$return_arr['Contact3'] = 'contact3';
    		$return_arr['Contact3_Tel1'] = 'contact3_tel1';
    		$return_arr['Contact3_Tel2'] = 'contact3_tel2';
    		$return_arr['Contact3_Fax'] = 'contact3_fax';
    		$return_arr['Contact3_Title'] = 'contact3_title';
    		$return_arr['Currency'] = 'currency';
    		$return_arr['RouteCode'] = 'route_code';
    		$return_arr['Stop'] = 'stop';
    		$return_arr['R_Owner'] = 'r_owner';
    		$return_arr['UDF1'] = 'udf1';
    		$return_arr['UDF2'] = 'udf2';
    		$return_arr['UDF3'] = 'udf3';
    		$return_arr['UDF4'] = 'udf4';
    		$return_arr['UDF5'] = 'udf5';
    		$return_arr['NOTES'] = 'remark';
    		$return_arr['BankAccount'] = 'bank_account';
    		$return_arr['easycode'] = 'easy_code';
    		$return_arr['Active_Flag'] = 'active_flag';
    	} elseif ($type == 'supplier') {
    		$return_arr['CustomerID'] = 'supplier_code';
    		$return_arr['Descr_C'] = 'descr_c';
    		$return_arr['Descr_E'] = 'descr_e';
    		$return_arr['Address1'] = 'address1';
    		$return_arr['Address2'] = 'address2';
    		$return_arr['Address3'] = 'address3';
    		$return_arr['Country'] = 'country';
    		$return_arr['Province'] = 'province';
    		$return_arr['City'] = 'city';
    		$return_arr['Zip'] = 'zip';
    		$return_arr['Contact1'] = 'contact1';
    		$return_arr['Contact1_Tel1'] = 'contact1_tel1';
    		$return_arr['Contact1_Tel2'] = 'contact1_tel2';
    		$return_arr['Contact1_Fax'] = 'contact1_fax';
    		$return_arr['Contact1_Title'] = 'contact1_title';
    		$return_arr['Contact1_Email'] = 'contact1_email';
    		$return_arr['Contact2'] = 'contact2';
    		$return_arr['Contact2_Tel1'] = 'contact2_tel1';
    		$return_arr['Contact2_Tel2'] = 'contact2_tel2';
    		$return_arr['Contact2_Fax'] = 'contact2_fax';
    		$return_arr['Contact2_Title'] = 'contact2_title';
    		$return_arr['Contact3'] = 'contact3';
    		$return_arr['Contact3_Tel1'] = 'contact3_tel1';
    		$return_arr['Contact3_Tel2'] = 'contact3_tel2';
    		$return_arr['Contact3_Fax'] = 'contact3_fax';
    		$return_arr['Contact3_Title'] = 'contact3_title';
    		$return_arr['Currency'] = 'currency';
    		$return_arr['RouteCode'] = 'route_code';
    		$return_arr['Stop'] = 'stop';
    		$return_arr['R_Owner'] = 'r_owner';
    		$return_arr['UDF1'] = 'udf1';
    		$return_arr['UDF2'] = 'udf2';
    		$return_arr['UDF3'] = 'udf3';
    		$return_arr['UDF4'] = 'udf4';
    		$return_arr['UDF5'] = 'udf5';
    		$return_arr['NOTES'] = 'remark';
    		$return_arr['BankAccount'] = 'bank_account';
    		$return_arr['easycode'] = 'easy_code';
    		$return_arr['Active_Flag'] = 'active_flag';
    	} elseif ($type == 'shop') {
    		$return_arr['CustomerID'] = 'shop_code';
    		$return_arr['Descr_C'] = 'descr_c';
    		$return_arr['Descr_E'] = 'descr_e';
    		$return_arr['Address1'] = 'address1';
    		$return_arr['Address2'] = 'address2';
    		$return_arr['Address3'] = 'address3';
    		$return_arr['Country'] = 'country';
    		$return_arr['Province'] = 'province';
    		$return_arr['City'] = 'city';
    		$return_arr['Zip'] = 'zip';
    		$return_arr['Contact1'] = 'contact1';
    		$return_arr['Contact1_Tel1'] = 'contact1_tel1';
    		$return_arr['Contact1_Tel2'] = 'contact1_tel2';
    		$return_arr['Contact1_Fax'] = 'contact1_fax';
    		$return_arr['Contact1_Title'] = 'contact1_title';
    		$return_arr['Contact1_Email'] = 'contact1_email';
    		$return_arr['Contact2'] = 'contact2';
    		$return_arr['Contact2_Tel1'] = 'contact2_tel1';
    		$return_arr['Contact2_Tel2'] = 'contact2_tel2';
    		$return_arr['Contact2_Fax'] = 'contact2_fax';
    		$return_arr['Contact2_Title'] = 'contact2_title';
    		$return_arr['Contact3'] = 'contact3';
    		$return_arr['Contact3_Tel1'] = 'contact3_tel1';
    		$return_arr['Contact3_Tel2'] = 'contact3_tel2';
    		$return_arr['Contact3_Fax'] = 'contact3_fax';
    		$return_arr['Contact3_Title'] = 'contact3_title';
    		$return_arr['Currency'] = 'currency';
    		$return_arr['RouteCode'] = 'route_code';
    		$return_arr['Stop'] = 'stop';
    		$return_arr['R_Owner'] = 'r_owner';
    		$return_arr['UDF1'] = 'udf1';
    		$return_arr['UDF2'] = 'udf2';
    		$return_arr['UDF3'] = 'udf3';
    		$return_arr['UDF4'] = 'udf4';
    		$return_arr['UDF5'] = 'udf5';
    		$return_arr['NOTES'] = 'remark';
    		$return_arr['BankAccount'] = 'bank_account';
    		$return_arr['easycode'] = 'easy_code';
    		$return_arr['Active_Flag'] = 'active_flag';
    	}
    	return $return_arr;
    }
}