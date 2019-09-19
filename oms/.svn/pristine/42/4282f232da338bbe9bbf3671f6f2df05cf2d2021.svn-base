<?php
/**
 * Created by PhpStorm.
 * User: 20171012
 * Date: 2018/5/22
 * Time: 16:28
 */
class KlRequestController extends Controller
{
    public function actionIndex()
    {
        $this->render("index");
    }

    public function actionSub()
    {

        $apiList = $_POST['ApiList'];
        $params = array(
            'notify_type'=>$apiList['method'],
            'notify_id'=>rand(5,10),
            'notify_time'=>date("Y-m-d H:i:s"),
            'wms_id'=>'yunda',
            'stock_id'=>'412810',
            'owner_id'=>'163',
            'data'=>$apiList['json']
        );

        $params['sign'] = $this->makeSign($apiList['json']);

        require_once APP_ROOT.'/protected/components/util.php';
        $utilObj = new util();

        $rsp = $utilObj->curl('http://localhost/oms/1.0.1/kaola_api.php',$params);

        die(json_encode(array('status'=>'ok', 'msg'=>$rsp)));
    }

    public function makeSign ($data)
    {
        $priContent = file_get_contents("E:/xampp/htdocs/oms_test/ssl/rsa_private_key.pem");

        $prikeyid = openssl_pkey_get_private($priContent);

        openssl_sign($data,$sign,$prikeyid,OPENSSL_ALGO_SHA1);

        openssl_free_key($prikeyid);

        return bin2hex($sign);
    }
}