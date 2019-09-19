<?php
/**
 * Notes:拼多多每天凌晨刷新商家授权信息
 * Date: 2019/4/12
 * Time: 16:26
 */
class PddRefreshTokenCommand extends CConsoleCommand
{
    public function actionRefresh() {

        //只有每天早上4点之后执行一次
        $searchDate = date("Y-m-d");
        if (date('H:i') < '04:00') {
            die('time error');
        } else {
            $logFileName = ROOT_DIR . 'protected/runtime/uploadFiles/refresh.log';
            if (file_exists($logFileName)) {
                $lastTime = file_get_contents($logFileName);
                if ($lastTime != $searchDate) {
                    file_put_contents($logFileName, $searchDate);
                } else {
                    die('date is exists');
                }
            } else {
                file_put_contents($logFileName, $searchDate);
            }
        }
        $db = Yii::app()->db;
        //查询请求报文
        $sql = "SELECT refresh_token FROM csk_seller_access_token where  is_valid=1 and platform_elec = 'PDD'";
        $model = $db->createCommand($sql);
        $tokenInfo = $model->queryAll();
        foreach ($tokenInfo as $k=>$v) {
            $parmars = array(
                'client_id' => CLIENT_ID,
                'client_secret' => CLIENT_SECRET,
                'grant_type' => 'refresh_token',
                'refresh_token' => $v['refresh_token']
            );
            $ch = curl_init();
            $defaults = array(
                CURLOPT_POST => 1,
                CURLOPT_HEADER => 0,
                CURLOPT_HTTPHEADER => array('Content-Type:application/json'),
                CURLOPT_URL => 'http://open-api.pinduoduo.com/oauth/token',
                CURLOPT_FRESH_CONNECT => 1,
                CURLOPT_RETURNTRANSFER => 1,
                CURLOPT_FORBID_REUSE => 1,
                CURLOPT_TIMEOUT => 30,
                CURLOPT_POSTFIELDS => json_encode($parmars)
            );
            curl_setopt_array($ch, ($defaults));
            $result = curl_exec($ch);
            $logName = date("Ymd") . '_pdd_refresh.log';
            if($error = curl_error($ch)) {
                error_log(print_r($error, 1) . PHP_EOL, 3, LOG_PATH . $logName);
                echo $error;exit();
            }
            curl_close($ch);
            $result = json_decode($result,true);
            if (isset($result['access_token']) && isset($result['refresh_token'])) {
                $sql = "update `csk_seller_access_token` set `access_token`='".$result['access_token']."' , `refresh_token`='".$result['refresh_token']."',`time`= Now() where seller_id=".$result['owner_id'];
                $model = $db->createCommand($sql);
                $model->execute();
            } else {
                error_log(print_r($result, 1) . PHP_EOL, 3, LOG_PATH . $logName);
            }
        }
        exit();
    }
}