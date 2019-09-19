<?php
/**
 * 手动下发脚本
 * @author Renee
 *
 */
class xfOrderDataCommand extends CConsoleCommand {
    public function actionCreate() {
        $db = Yii::app()->db;
        //查询请求报文
        /*
        $sql = "SELECT
                	b.request_param,b.method,q.customer_id,q.erp_api_ver,q.wms_api_url
                FROM (
                	SELECT 
                	 a.*
                	FROM
                	(
                		SELECT
                			*
                		FROM
                			t_order_interface_log
                		WHERE
                			create_time >= '2017-04-01 15:30:00'
                		AND create_time <= '2017-04-01 18:54:00'
                		AND method in('deliveryorder.create','deliveryorder.batchcreate')
                		AND return_status=1
                		ORDER BY order_no DESC
                	) a
                GROUP BY a.order_no,a.customer_id) b 
                left join t_qimen_customer_bind p
                on b.customer_id=p.customer_id
                LEFT join t_qimen_customer q
                on p.qimen_customer_id=q.customer_id limit 1";
        */
        $sql = "
SELECT * FROM t_order_interface_log
WHERE
method = 'returnorder.create' 
and order_no in ('180311189',
'180311183',
'180311181',
'180311181',
'180311181',
'180311179',
'180311177',
'180311175',
'180311173',
'180311171',
'180311018',
'180311018',
'180311018',
'180311018',
'180310590',
'180310541',
'180310418',
'180310384',
'180310370',
'180310368',
'180310326',
'180310318',
'180310316',
'180310286',
'180310248',
'180310231',
'180310229',
'180310229',
'180310225',
'180310218',
'180310217',
'180310198',
'180310191',
'180310190',
'180310190',
'180310190',
'180310181',
'180310177',
'180310173',
'180310172',
'180310169',
'180310165',
'180310162',
'180310160',
'180310156',
'180310156',
'180310085',
'180309162',
'180309162',
'180309162',
'180309069',
'180309069',
'180309070',
'180309068',
'180309068',
'180309068',
'180309067',
'180309067',
'180309064',
'180308196',
'180308191',
'180308138',
'180308057',
'180308057',
'180308089',
'180307256',
'180307256',
'180307243',
'180307243',
'180307243',
'180307243',
'180307243',
'180307243',
'180307283',
'180307176',
'180307176',
'180307176',
'180306626',
'180306543',
'180305150',
'180305150',
'180305150',
'180305150',
'180305016',
'180305016',
'180305016',
'171229181',
'171229181',
'171219128',
'171218077',
'171216056',
'171216056',
'171216056',
'171216056',
'171216056',
'171216056',
'171216056',
'171214067',
'171214067',
'171214067',
'171214067',
'171214067',
'171214067',
'171214067',
'171214067',
'171214067'
) group by order_no,customer_id;";
        $model = $db->createCommand($sql);
        $requestParam = $model->queryAll();
        
        foreach ($requestParam as $r_k=>$r_v) {
            $str_xml= $r_v['request_param'];
            //秘钥
            $appSecret='97e05c770e25bb970a1ffd3d4f411e1a';
            //接口地址
            $url='http://oms.yundasys.com:2124/oms/api.php';
            
            //参数
            $sysData = array(
                'method' => $r_v['method'],
        		'timestamp' => date("Y-m-d H:i:s"),
        		'format' => 'xml',
                'app_key' => '23229135',
        		'v' => '2.4',
        		'sign_method' => 'md5',
        		'customerId' => $r_v['customer_id'] 
            );
            
            ksort($sysData);
            $signStr = $appSecret;
            foreach ($sysData as $key => $val)
            {
            	$signStr .= $key . $val;
            }
            $signStr .= $str_xml . $appSecret;
            $sign = strtoupper(md5($signStr));
            
            $sendUrl = $url . '?method=' . $sysData['method'] . '&timestamp=' . urlencode($sysData['timestamp']) . '&format=' . $sysData['format'] . '&app_key=' . $sysData['app_key'] . '&v=' . $sysData['v'] . '&sign=' . $sign . '&sign_method=' . $sysData['sign_method'] . '&customerId=' . $sysData['customerId'];
            error_log(print_r($str_xml.PHP_EOL,1),3,'D:/log/20180312_qimen_thrk_xml.log');
            error_log(print_r($sendUrl.PHP_EOL,1),3,'D:/log/20180312_qimen_thrk_url.log');
            
            //发送数据
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, $sendUrl);      // set url to post to
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1); // return into a variable
            curl_setopt($ch, CURLOPT_TIMEOUT, 30);      // times out
            curl_setopt($ch, CURLOPT_POSTFIELDS, $str_xml);     // add POST fields
            $result = curl_exec($ch);

            if ($error = curl_error($ch)) {
                echo $error;
            }
            error_log(print_r($result.PHP_EOL,1),3,'D:/log/20180312_qimen_thrk_rs.log');
            echo $r_k.PHP_EOL;
        }
        echo "succ";
    }
}
?>