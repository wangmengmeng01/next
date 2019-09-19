<?php
/**
 * 订单接口日志控制器
 * @author wp
 *
 */
class OrderLogController extends Controller
{

	public function actionIndex()
	{
		$this->render('index');
	}

	public function actionData()
	{
	    //插入查询日志记录
	    $ip         = $_SERVER["REMOTE_ADDR"];
	    $emp_id     = Yii::app()->user->soa_id;
	    $log_type   = 'order';
	    $order_no   = '';
	    if (!empty($_POST['OrderLog']['order_no'])) {
	        $order_no = $_POST['OrderLog']['order_no'];
	    }   
	    $express_no = '';
	    $conds      = '';
	    if (!empty($_POST['OrderLog'])) {
	        $conds  = json_encode($_POST['OrderLog']);
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
		$dataProvider = OrderLog::search($_POST);
        
		if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
		//记录elk日志
		util::elkLog('日志管理', '订单日志查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
		Yii::app()->end();
	}

	/**
	 * 手工推送
	 */
	public function actionPush()
	{

		//手工推送权限校验
		util::operatePriContr(21.1);

		$param = $_POST['value'];

		$e_order = array();	//手动推送失败入库单
		$t_order = "";		//手工推送仓库未绑定货主的单号
		$d_order = "";		//手工推送货主、仓库编码均为空的单号
		$e = 0;

		foreach ($param as $v) {

			//货主id是否为空
			if(empty($v['customer_id'])) {

				//仓库编码为空
				if(empty($v['warehouse_code'])) {

					$d_order .= "{$v['order_no']},";
					$e++;
					continue;

				} else {

					$connection = Yii::app()->db;

					//根据仓库编码获取货主id
					$sql = "SELECT customer_id FROM t_customer_relation WHERE `code` = '{$v['warehouse_code']}'";

					$customer_id = $connection->createCommand($sql)->queryRow();
					if($customer_id)
					{
						$v['customer_id'] = $customer_id['customer_id'];

					} else {

						$t_order .= "{$v['order_no']},";
						$e++;
						continue;

					}
				}
			}

			//请求格式
			$queryParams = array(
				'method' 	 => $v['method'],
				'customerid' => $v['customer_id'],
				'appkey' 	 => SRD_APP_KEY,
				'sign'		 => strtoupper(base64_encode(md5(SRD_APP_SECRET.$v['request_param'].SRD_APP_SECRET))),
				'timestamp'	 => date("Y-m-d H:i:s"),
				'data' 		 => $v['request_param']
			);

			//推送
			include_once Yii::app()->basePath . '/ext/httpclient.php';
			$httpObject = new httpclient();
			
			$result = $httpObject->post(OMS_API_URL,$queryParams);

			//xml转换为数组
			$result = json_decode(json_encode(simplexml_load_string($result)), true);

			//结果判定
			if($result['flag']=='success') {

				//记录操作日志
				$this->operateLog(1,$v);

			} else {

				//记录操作日志
				$this->operateLog(0,$v);

				//手工推送失败单号
				$e_order[] = array(
					'order_no'=> $v['order_no'],
					'error'	  => $result['message'],
				);

				$e++;

			}
		}

		//提示消息拼接
		$total = count($param);
		$msg = "手工推送成功".($total-$e)."条数据，失败".$e."条".PHP_EOL;

		if($t_order != "") {
			$t_order = rtrim($t_order,',');
			$msg .= "仓库未绑定货主导致未推送成功的单号：".$t_order.PHP_EOL;
		}
		if($d_order != "") {
			$d_order = rtrim($d_order,',');
			$msg .= "货主、仓库编码均为空导致未推送成功的单号".$d_order.PHP_EOL;
		}
		if(!empty($e_order)) {
			foreach ($e_order as $item) {
				$msg .= $item['order_no']." 推送失败，原因:".$item['error'].PHP_EOL;
			}
		}

		//记录elk日志
		util::elkLog('日志管理', '订单日志推送数据到ERP', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>$msg), 'Y');
		die(
		json_encode(
			array(
				'status'=>1,
				'msg'	=>$msg
			)
		)
		);
	}

	/**
	 * 记录操作日志
	 * @param $status
	 * @param $param
	 */
	protected function operateLog($status,$param)
	{


		$operation = new InterfaceOperationLog();

		$operation->method 			= $param['method'];
		$operation->order_no 		= $param['order_no'];
		$operation->order_type 		= $param['order_type'];
		$operation->customer_id 	= $param['customer_id'];
		$operation->warehouse_code 	= $param['warehouse_code'];
		$operation->operate_status 	= $status;
		$operation->create_time 	= date("Y-m-d H:i:s");
		$operation->operator_id 	= Yii::app()->user->soa_id;
		$operation->operator_name 	= Yii::app()->user->user_title;

		//插入操作日志
		$res = $operation->save();

		if($res) {
			return;
		} else {

			//记录文本操作日志
			$error_msg  = PHP_EOL."#########################################################".PHP_EOL;
			$error_msg .= "订单号：{$param['order_no']}".PHP_EOL;
			$error_msg .= "货主编码：{$param['customer_id']}".PHP_EOL;
			$error_msg .= "仓库编码：{$param['warehouse_code']}".PHP_EOL;
			$error_msg .= "订单类型：{$param['order_type']}".PHP_EOL;
			$error_msg .= "接口名称：{$param['method']}".PHP_EOL;
			$error_msg .= "操作状态：$status".PHP_EOL;
			$error_msg .= "操作时间：".date("Y-m-d H:i:s").PHP_EOL;
			$error_msg .= "操作人：".Yii::app()->user->user_title."(".Yii::app()->user->soa_id.")".PHP_EOL;
			$error_msg .= "#########################################################";


			error_log(print_r($error_msg,1).PHP_EOL,3,LOG_PATH.'interface_operation_error_log.txt');
		}

		return ;

	}
}