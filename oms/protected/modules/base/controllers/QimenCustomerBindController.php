<?php
/**
 * 奇门货主绑定Controller
 * @author Renee
 *
 */
class QimenCustomerBindController extends Controller
{
	private $_model;
	
	public function actionIndex()
	{
		$this->render('index');
	}
	
	public function actionData()
	{
		$dataProvider = QimenCustomerBind::search($_POST);
		if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
		//记录elk日志
		util::elkLog('基础信息', '奇门货主绑定维护查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
		Yii::app()->end();
	}
	
	//新增绑定关系
	public function actionAdd()
	{
		//校验权限
		util::operatePriContr(23.1);
		$model = new QimenCustomerBind();
		if (isset($_POST['QimenCustomerBind'])) {
		    
			//校验奇门客户ID是否存在
			$rsCustomer = QimenCustomer::model()->findByPk($_POST['QimenCustomerBind']['qimen_customer_id']);
			
			if (empty($rsCustomer)) {
				//记录elk日志
				util::elkLog('基础信息', '奇门货主绑定维护新增', 'error', $_POST['QimenCustomerBind']['qimen_customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'该奇门客户ID不存在,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => '该奇门客户ID不存在,保存失败'
				)));
			} elseif ($rsCustomer['customer_id'] != $_POST['QimenCustomerBind']['qimen_customer_id']) {
				//记录elk日志
				util::elkLog('基础信息', '奇门货主绑定维护新增', 'error', $_POST['QimenCustomerBind']['qimen_customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主ID填写不规范，请注意字母的大小写,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => '货主ID填写不规范，请注意字母的大小写,保存失败'
				)));
			}
			
			//校验数据是否已经存在
			$rs = $model->find('customer_id = :customer_id AND is_valid = 1',array(':customer_id'=>$_POST['QimenCustomerBind']['customer_id']));
			if (!empty($rs)) {
				//记录elk日志
				util::elkLog('基础信息', '奇门货主绑定维护新增', 'error', $_POST['QimenCustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'该货主已经存在，保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => '该货主已经存在，保存失败'
				)));
			}
			
			$_POST['QimenCustomerBind']['operator_id'] = Yii::app()->user->soa_id;
			$_POST['QimenCustomerBind']['operator_name'] = Yii::app()->user->user_title;
			$_POST['QimenCustomerBind']['create_time'] = date("Y-m-d H:i:s");
			$model->attributes = $_POST['QimenCustomerBind'];
			if ($model->save()) {
				//记录elk日志
				util::elkLog('基础信息', '奇门货主绑定维护新增', 'info', $_POST['QimenCustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'保存成功'), 'Y');
				die(json_encode(array(
						'status' => 'ok',
						'msg' => '保存成功'
				)));
				Yii::app()->end();
			} else {
				//记录elk日志
				util::elkLog('基础信息', '奇门货主绑定维护新增', 'error', $_POST['QimenCustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'错误操作，请找IT部'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => '错误操作，请找IT部'
				)));
			}
		}
		$this->renderPartial('_form', array(
				'model' => $model
		));
	}
	
	//修改绑定关系
	public function actionUpdate($id)
	{
		//校验权限
		util::operatePriContr(23.1);
		$model = QimenCustomerBind::model();
		$customerBindInfo = $model->find('customer_id = :customer_id',array(':customer_id'=>$id));
		
		if (isset($_POST['QimenCustomerBind'])) {
		    //校验奇门客户ID是否存在
			$qimenInfo = QimenCustomer::model()->find('customer_id = :customer_id AND is_valid=1', array(
					':customer_id' => $_POST['QimenCustomerBind']['qimen_customer_id']
			));
			if (empty($qimenInfo['customer_id'])) {
				//记录elk日志
				util::elkLog('基础信息', '奇门货主绑定维护修改', 'error', $_POST['QimenCustomerBind']['qimen_customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'奇门客户ID不存在或已失效,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => '奇门客户ID不存在或已失效,保存失败'
				)));
			} 
			
			//校验货主ID是否存在
			$cusInfo = QimenCustomerBind::model()->find('customer_id = :customer_id AND bind_id != :bind_id AND is_valid=1', array(
					':customer_id' => $_POST['QimenCustomerBind']['customer_id'],
			        ':bind_id' => $customerBindInfo['bind_id']
			));
			if (!empty($cusInfo)) {
				//记录elk日志
				util::elkLog('基础信息', '奇门货主绑定维护修改', 'error', $_POST['QimenCustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主ID已存在,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => '货主ID已存在,保存失败'
				)));
			}
			$_POST['QimenCustomerBind']['operator_id'] = Yii::app()->user->soa_id;
			$_POST['QimenCustomerBind']['operator_name'] = Yii::app()->user->user_title;
			
			if (!isset($_POST['QimenCustomerBind']['warehouse_code'])) {
			    $_POST['QimenCustomerBind']['warehouse_code'] = '';
			}
			
			$customerBindInfo->attributes = $_POST['QimenCustomerBind'];
			
			if ($customerBindInfo->save()) {
				//记录elk日志
				util::elkLog('基础信息', '奇门货主绑定维护修改', 'info', $_POST['QimenCustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'更新成功'), 'Y');
				die(json_encode(array(
						'status' => 'ok',
						'msg' => '更新成功'
				)));
				Yii::app()->end();
			}else{
				//记录elk日志
				util::elkLog('基础信息', '奇门货主绑定维护修改', 'error', $_POST['QimenCustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'错误操作，请找IT部'), 'N');
				die(json_encode(array(
                    'status' => 'error',
                    'msg' => '错误操作，请找IT部'
                )));
			}
		}
		$this->render('_form', array(
				'model' => $customerBindInfo
		));
	}
	
	//作废货主与ERP和WMS的绑定关系
	public function actionDelete()
	{		
		//校验权限
		util::operatePriContr(23.1, 'json');
		
		if (!isset($_POST['id'])) {
			//记录elk日志
			util::elkLog('基础信息', '奇门货主绑定维护删除', 'error', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'非法操作'), 'N');
			die(json_encode(array(
					'status' => 'error',
					'msg' => '非法操作'
			)));
		}
		$delModel = $this->loadModel($_POST['id']);
		$_POST['QimenCustomerBind']['is_valid'] = 0;
		$delModel->attributes = $_POST['QimenCustomerBind'];
		if ($delModel->save(false)) {
			//记录elk日志
			util::elkLog('基础信息', '奇门货主绑定维护删除', 'info', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'删除成功'), 'Y');
			die(json_encode(array(
					'status' => 'ok',
					'msg' => '删除成功'
			)));
		}
	}
    
	//推送数据到WMS
	public function actionPushWms()
	{
	    //校验权限
	    util::operatePriContr(23.1, 'json');
	    if (!isset($_POST['customer_id'])) {
	        $this->render('push');
	    } else {
	        $rsCustomer = QimenCustomerBind::model()->find('customer_id=:customer_id',array(':customer_id'=>$_POST['customer_id']));
	        if (empty($rsCustomer)) {
				//记录elk日志
				util::elkLog('基础信息', '奇门货主绑定维护推送数据到WMS', 'error', $_POST['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'未找到该货主对应的客户信息'), 'N');
	            die(json_encode(array(
	                'status' => 'error',
	                'msg' => '未找到该货主对应的客户信息！'
	            )));
	        }
	        
	        $customerInfo = QimenCustomer::model()->findByPk($rsCustomer['qimen_customer_id']);
	        if (empty($customerInfo)) {
				//记录elk日志
				util::elkLog('基础信息', '奇门货主绑定维护推送数据到WMS', 'error', $rsCustomer['qimen_customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>$customerInfo['customer_id']), 'N');
	            die(json_encode(array(
	                'status' => 'error',
	                'msg' => $customerInfo['customer_id']
	            )));
	        }



	        //获取货主信息
	        $customerType = empty($_POST['warehouse_code']) ? '1' : '3' ;//1普通货主，3菜鸟仓储货主
	        $requestXml = '<?xml version="1.0" encoding="utf-8"?><request><CustomerID>' . $_POST['customer_id'] . '</CustomerID><Customer_Type>OW</Customer_Type><Active_Flag>1</Active_Flag><UDF5>'. $customerType .'</UDF5></request>';
	        //直接访问wms那边接口
	        $apiParams = array(
	            'method' => 'putCustData4QM',
	            'customerid' => $rsCustomer['customer_id'],
	            'appkey' => $customerInfo['wms_app_key'],
	            'sign' => strtoupper(base64_encode(md5($customerInfo['wms_secret'] . $requestXml . $customerInfo['wms_secret']))),
	            'timestamp' => date('Y-m-d H:i:s'),
	            'data' => $requestXml
	        );

	        include_once Yii::app()->basePath . '/ext/httpclient.php';
	        include_once Yii::app()->basePath . '/ext/xml.php';
	        $httpObj = new httpclient();
	        $response = $httpObj->post($customerInfo['wms_api_url'], $apiParams);
	        if ($response == '') {
				//记录elk日志
				util::elkLog('基础信息', '奇门货主绑定维护推送数据到WMS', 'error', $_POST['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $apiParams, array('status'=>'error', 'msg'=>'数据推送失败，请检查YWMS接口是否能正常访问'), 'N');
	            die(json_encode(array(
	                'status' => 'error',
	                'msg' => '数据推送失败，请检查YWMS接口是否能正常访问'
	            )));
	        } else {
	            $xmlObj = new xml();
	            $responseArr = $xmlObj->xmlStr2array($response);
	            if ($responseArr['flag'] == 'success') {
	                $this->writeSendLog($rsCustomer['customer_id'], 1, $responseArr['code'], $responseArr['message']);
					//记录elk日志
					util::elkLog('基础信息', '奇门货主绑定维护推送数据到WMS', 'info', $_POST['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $apiParams, array('status'=>'ok', 'msg'=>'删除成功'), 'Y');
	                die(json_encode(array(
	                    'status' => 'ok',
	                    'msg' => '货主同步成功！'
	                )));
	            } else {
	                $this->writeSendLog($rsCustomer['customer_id'], 0, $responseArr['code'], $responseArr['message']);
					//记录elk日志
					util::elkLog('基础信息', '奇门货主绑定维护推送数据到WMS', 'error', $_POST['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $apiParams, array('status'=>'error', 'msg'=>$responseArr['message']), 'N');
	                die(json_encode(array(
	                    'status' => 'error',
	                    'msg' => $responseArr['message']
	                )));
	            }
	        }
	    }
	}
	
	//记录货主推送记录
	public function writeSendLog($customerId, $status, $msgcode, $msg) {
	    if (!empty($customerId)) {
	        $sql = 'SELECT customer_id FROM t_customer_send_log WHERE customer_id=:customer_id AND customer_type=:customer_type';
	        $db = Yii::app()->db;
	        $model = $db->createCommand($sql);
	        $model->bindValue(':customer_id', $customerId);
	        $model->bindValue(':customer_type', 'OW');
	        $sendInfo = $model->execute();
	        if (empty($sendInfo)) {
	            $insertSql = "INSERT INTO t_customer_send_log(customer_id,customer_type,send_wms_status,send_wms_num,wms_error_code,wms_error_msg,create_time) VALUES(:customer_id,:customer_type,:send_wms_status,:send_wms_num,:wms_error_code,:wms_error_msg,now());";
	            $insertModel= $db->createCommand($insertSql);
	            $insertModel->bindValue(':customer_id', $customerId);
	            $insertModel->bindValue(':customer_type', 'OW');
	            $insertModel->bindValue(':send_wms_status', $status);
	            $insertModel->bindValue(':send_wms_num', 1);
	            $insertModel->bindValue(':wms_error_code', $msgcode);
	            $insertModel->bindValue(':wms_error_msg', $msg);
	            $insertModel->execute();
	        } else {
	            $updateSql = "UPDATE t_customer_send_log SET send_wms_num=send_wms_num+1,send_wms_status=:send_wms_status,wms_error_code=:wms_error_code,wms_error_msg=:wms_error_msg WHERE customer_id=:customer_id AND customer_type=:customer_type";
	            $updateModel = $db->createCommand($updateSql);
	            $updateModel->bindValue(':send_wms_status', $status);
	            $updateModel->bindValue(':wms_error_code', $msgcode);
	            $updateModel->bindValue(':wms_error_msg', $msg);
	            $updateModel->bindValue(':customer_id', $customerId);
	            $updateModel->bindValue(':customer_type', 'OW');
	            $updateModel->execute();
	        }
	    }
	}
	
	public function loadModel($id)
	{
		if ($this->_model === null) {
			if (isset($id)) {
				$this->_model = QimenCustomerBind::model()->find('customer_id = :customer_id',array(':customer_id' => $id));
			}
			if ($this->_model === null)
				throw new CHttpException(404, 'The requested page does not exist.');
		}
		return $this->_model;
	}
}