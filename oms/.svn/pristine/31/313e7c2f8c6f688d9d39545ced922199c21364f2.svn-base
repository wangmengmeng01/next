<?php
/**
 * 手动回传脚本
 * @author Renee
 *
 */
class hcDataCommand extends CConsoleCommand {
    public function actionDeliverConfirm() {
        $db = Yii::app()->db;
        //查询请求报文
        $sql = "SELECT request_param FROM t_order_interface_log where  customer_id='MEITUAN' AND create_time>='2018-04-03 17:30:00' and create_time<'2018-04-04 00:00:00' AND method='taobao.qimen.deliveryorder.shortage'";
        $model = $db->createCommand($sql);
        $requestParam = $model->queryAll();
        foreach ($requestParam as $r_k=>$r_v) {
            $str_xml= $r_v['request_param'];
            //秘钥
            $appSecret='97e05c770e25bb970a1ffd3d4f411e1a';
            //接口地址
            $url='http://oms.yundasys.com:2124/oms/api.php';
            
            //参数
            $params = array(
                'method' => 'taobao.qimen.deliveryorder.shortage',
                'customerid' => 'MEITUAN',
                'appkey' => '23229135',
                'sign'=> strtoupper(base64_encode(md5($appSecret.$str_xml.$appSecret))),
                'timestamp'=> date("Y-m-d H:i:s"),
                'data' => $str_xml
            );
            
            //echo "<hr/>";
            
            //发送数据
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL,$url);      // set url to post to
            curl_setopt($ch, CURLOPT_RETURNTRANSFER,1); // return into a variable
            curl_setopt($ch, CURLOPT_TIMEOUT, 30);      // times out
            curl_setopt($ch, CURLOPT_POSTFIELDS, $params);     // add POST fields
            $result = curl_exec($ch);
            if($error = curl_error($ch)) {
                echo $error;
            }
            error_log(print_r($r_k.$result.PHP_EOL,1),3,'E:/log/shortage_04031730_hou.log');
        }
    }
}
?>