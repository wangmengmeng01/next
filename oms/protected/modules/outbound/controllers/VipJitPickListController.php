<?php

class VipJitPickListController extends Controller
{

    private $_model;

    public function actionIndex()
    {
        $status = 2; //默认关闭，ToDo 待获取
        $this->render('index',array('status'=>$status));
    }

    public function actionData()
    {
        $dataProvider = VipJitPickList::search($_POST);
        if (!empty($dataProvider)) {
            echo $dataProvider;
//            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
		//记录elk日志
		util::elkLog('出库管理', '唯品会JIT拣货单列表查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }

    public function loadModel($id)
    {
        if ($this->_model === null) {
            if (isset($id)) {
                $this->_model = VipJitPickList::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
    }

    public function actionManualDownload()
    {
        $status = $_REQUEST['status']==1 ? 2 : 1; //1：开启状态，2：关闭状态
        //ToDo setStatus
        $response = array(
            'code' => 0,
            'status' => $status,
            'msg' => '切换成功'
        );
        echo CJSON::encode($response);

    }


}