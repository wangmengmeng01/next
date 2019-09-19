<?php
/**
 * 手动下发脚本fule
 * @author Renee
 *
 */
class xfFuleOrderDataCommand extends CConsoleCommand {
    public function actionCreate() {
        echo 'start...';
        $db = Yii::app()->db;
        //查询请求报文
        $sql = "SELECT a.*,b.app_secret as app_secret from 
                (
                SELECT
                	*
                FROM
                	t_order_interface_log
                WHERE
                	create_time >= '2017-04-01 14:00:00' and create_time <= '2017-04-02 17:39:00'
                AND method ='putSOData'
                AND return_status = 1
                GROUP BY order_no,customer_id
                ) a
                left JOIN t_base_customer b
                on a.customer_id=b.customer_id";
        $model = $db->createCommand($sql);
        $requestParam = $model->queryAll();
        error_log(print_r($requestParam,1).PHP_EOL,3,'/tmp/20170402_fule_querydata.log');
        foreach ($requestParam as $r_v) {
            $str_xml= $r_v['request_param'];
            //秘钥
            $appSecret=$r_v['app_secret'];
            //接口地址
            $url='http://oms.yundasys.com:2124/oms/api.php';
            
            //参数
            $sysData = array(
                'method' => 'putSOData',
        		'customerid' => $r_v['customer_id'],
        		'warehouseid' => 'WH01',
        		'messageid' => 'SO',
        		'apptoken' => 'KI8J7-NC01H-8JZ84-BA5JE-PAC4FUO',
        		'appkey' => 'mSeGBJv',
        		'sign' => strtoupper(base64_encode(md5($appSecret.$str_xml.$appSecret))),
        		'timestamp' => date("Y-m-d H:i:s"),
        		'data' => $str_xml
            );
            error_log(print_r($sysData,1).PHP_EOL,3,'/tmp/20170402_fule_req.log');
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL,$url);      // set url to post to
            curl_setopt($ch, CURLOPT_RETURNTRANSFER,1); // return into a variable
            curl_setopt($ch, CURLOPT_TIMEOUT, 30);      // times out
            curl_setopt($ch, CURLOPT_POSTFIELDS, $sysData);     // add POST fields
            $result = curl_exec($ch);
            if($error = curl_error($ch))
            {
            	echo $error;
            }
            error_log(print_r($result,1).PHP_EOL,3,'/tmp/20170402_fule_rs.log');
        }
        echo "succ";
    }
}
?>