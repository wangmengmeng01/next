<?php

class CskSellerWaybillInfoController extends Controller
{
    private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        $dataProvider = CskSellerWaybillInfo::search($_POST);
        if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
        //记录elk日志
        util::elkLog('基础信息', '商家开通电子面单服务信息查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }
    
    public function actionUpdate($id)
    {
        //校验权限
        util::operatePriContr(39.1);
        $model = CskSellerWaybillInfo::model();
        $waybillInfo = $model->findByPk($id);
        if (isset($_POST['WaybillInfo'])) {
        	if (!isset($_POST['WaybillInfo']['ship_addr_code'])) {
        		$_POST['WaybillInfo']['ship_addr_code'] = '';
        	}
            $model->attributes = $_POST['WaybillInfo'];
            if ($model->save()) {
                //记录elk日志
                util::elkLog('基础信息', '商家开通电子面单服务信息更新', 'info', $_POST['WaybillInfo']['seller_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok','msg' => '更新成功'), 'Y');
                die(json_encode(array(
                    'status' => 'ok',
                    'msg' => '更新成功'
                )));
            }
        } 
        $this->render('_form', array(
            'model' => $waybillInfo
        ));
    }

    /**
     * 获取拼多多商家面单订购及使用详情
     * @param $id   快递公司编码
     */
    public function actionReload($id)
    {

        $params = array(
            'platform_elec' => 'PDD',
            'v' => '1.0',
            'timestamp' => date("Y-m-d H:i:s"),
            'method' => 'pdd.waybill.search',
            'data' => '{"seller_id":"' . $id .'"}',
            'appkey' => YWMS_WAYBILL_APP_KEY
        );

        $params['sign'] = strtoupper(base64_encode(md5(YWMS_WAYBILL_APP_SECRET . $params['data'] . YWMS_WAYBILL_APP_SECRET)));

        require_once APP_ROOT . '/api/ext/util.php';

        $util = new util();

        $res = $util->postForm(PDD_API_URL, $params, 30);

        $res = json_decode($res, true);

        $response = array(
            'status' => 1,
            'msg' => '刷新成功'
        );

        if (isset($res['error_response'])) {

            $response = array(
                'status' => 0,
                'msg' => $res['error_response']['error_msg']
            );
        }

        exit(json_encode($response));

    }

    public function loadModel($id)
    {
        if ($this->_model === null) {
            if (isset($id)) {
                $this->_model = CskSellerWaybillInfo::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
    }


}