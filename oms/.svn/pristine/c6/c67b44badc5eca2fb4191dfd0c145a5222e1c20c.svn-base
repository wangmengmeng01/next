<?php
/**
 * 商品接口日志控制器
 * @author wp
 *
 */
class CustomerLogController extends Controller
{

	public function actionIndex()
	{
		$this->render('index');
	}

	public function actionData()
	{
		$dataProvider = CustomerLog::search($_POST);
		if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
		//记录elk日志
		util::elkLog('日志管理', '客商档案日志查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
		Yii::app()->end();
	}


}