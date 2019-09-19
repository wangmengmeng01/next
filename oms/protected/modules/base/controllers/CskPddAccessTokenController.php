<?php

/**
 * 拼多多授权列表
 * Class CskPddrAccessTokenController
 */
class CskPddAccessTokenController extends Controller
{

    private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        $dataProvider = CskPddAccessToken::search($_POST);

        if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
        //记录elk日志
        util::elkLog('基础信息', '拼多多授权列表查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }

    /**
     * 添加客户信息
     */
    public function actionAdd()
    {
    	//校验权限
    	util::operatePriContr(86.1);

        if (isset($_POST['operate']) && $_POST['operate'] == 'add') {

            $model = new CskPddAccessToken();

            $model->create_time = '2019-02-26 15:48:21';
            $model->creater = Yii::app()->user->getState('soa_id');

            if ($model->save()) {

                exit(json_encode(['status' => 1, 'msg' => '添加成功']));
            }

            exit(json_encode(['status' => 0, 'msg' => '添加失败，请重试']));
        }
    }



    /**
     * 设置客户有效性
     */
    public function actionSetCustomer()
    {
    	//校验权限
    	util::operatePriContr(86.1, 'json');

        if (isset($_POST['shop_id'])) {

            $model = new CskPddAccessToken();

            if ($model->updateByPk($_POST['shop_id'], ['is_valid' => $_POST['is_valid']])) {

                exit(json_encode(['status' => 1, 'msg' => '设置成功']));
            }

            exit(json_encode(['status' => 0, 'msg' => '设置失败请重试']));
        }

    }
}