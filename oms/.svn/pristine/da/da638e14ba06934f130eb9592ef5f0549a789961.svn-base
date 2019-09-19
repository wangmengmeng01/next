<?php
/**
 * Created by PhpStorm.
 * User: Renee
 * Date: 2018/4/13
 * Time: 11:14
 */
class JdWaybillInfoCommand extends CConsoleCommand
{
    /***
     * Notes:根据商家编码查询商家所有审核成功的签约信息
     * Date: 2019/6/25
     * Time: 13:48
     * @throws Exception
     */
    public function actionGet() {
        $lastTime = file_get_contents(ROOT_DIR . 'protected/runtime/uploadFiles/getJdWaybillInfo.log');

        $periodTime = (time() - strtotime($lastTime))%3600;//时间差
        $periodTime = intval($periodTime/60);

        if ($periodTime < 15) {
            exit('time error');
        }
        //one day
        if ($periodTime > 1440) {
            exit('time difference too long!');
        }

        $eTime = date("Y-m-d H:i:s");
        $sql = "SELECT pop_id FROM csk_seller_access_token WHERE time>'{$lastTime}' AND time<'{$eTime}' AND platform_elec='JD' AND is_valid=1;";
        $db = Yii::app()->db;
        $model = $db->createCommand($sql);
        $result = $model->queryAll();

        if (empty($result)) {
            exit("data error");
        }

        $sysParams = array(
            'method'    => 'jingdong.ldop.alpha.provider.sign.success.info.get',
            'v'         => '3.0',
            'timestamp' => date("Y-m-d H:i:s"),
            'appkey'    => YWMS_WAYBILL_APP_KEY,
            'platform_elec' => 'JD'
        );
        foreach ($result as $v) {
            $jsonData = '{"venderCode":"'.$v['pop_id'].'"}';
            $sysParams['data'] = $jsonData;
            $sysParams['sign'] = strtoupper(base64_encode(md5(YWMS_WAYBILL_APP_SECRET.$jsonData.YWMS_WAYBILL_APP_SECRET)));

            util::curl(YWMS_WAYBILL_OMS_URL,$sysParams);
        }
        exit();
    }
}