<?php
/**
 * Notes:
 * Date: 2019/4/29
 * Time: 15:34
 */

class CskMerchantDeployController extends Controller
{

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        $dataProvider = CskMerchantDeploy::search($_POST);
        if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
        //记录elk日志
        util::elkLog('基础信息', '承运商配置列表查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }


    public function actionUpdate($id)
    {
        //校验权限
        util::operatePriContr(87.1);
        $model = CskMerchantDeploy::model();
        $merchantInfo = $model->findByPk($id);
        if (isset($_POST['Merchant'])) {
            $model->attributes = $_POST['Merchant'];
            if ($model->save()) {
                //记录elk日志
                util::elkLog('基础信息', '承运商配置列表修改', 'info', $id, Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok','msg' => '更新成功'), 'Y');
                die(json_encode(array(
                    'status' => 'ok',
                    'msg' => '更新成功'
                )));
                Yii::app()->end();
            }
        }
        $this->render('_form', array(
            'model' => $merchantInfo
        ));
    }

}