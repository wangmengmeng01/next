<?php
/**
 * 菜鸟电子面单接口日志控制器
 * @author Renee
 *
 */
class WaybillLogController extends Controller
{

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        //插入查询日志记录
	    $ip            = $_SERVER["REMOTE_ADDR"];
	    $emp_id        = Yii::app()->user->soa_id;
	    $log_type      = 'cnwaybill';
	    $order_no      = '';
	    if (!empty($_POST['WaybillLog']['order_list'])) {
	        $order_no  = $_POST['WaybillLog']['order_list'];
	    }   
	    $express_no    = '';
	    $conds         = '';
	    if (!empty($_POST['WaybillLog'])) {
	        $conds     = json_encode($_POST['WaybillLog']);
	    }
	    
	    $connection = new QueryLogRecord();
	    $connection->ip            = $ip;
	    $connection->emp_id        = $emp_id;
	    $connection->log_type      = $log_type;
	    $connection->order_no      = $order_no;
	    $connection->express_no    = $express_no;
	    $connection->conds         = $conds;
	    $connection->create_time   = date("Y-m-d H:i:s");
	    $connection->save();
        
	    //查询数据
        $dataProvider = WaybillLog::search($_POST);
        if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
		//记录elk日志
		util::elkLog('日志管理', '菜鸟电子面单接口日志查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }
}
?>