<?php
/**
 * 货主与ERP和WMS关系绑定
 * @author wp
 *
 */
class CustomerBindController extends Controller
{
	private $_model;
	
	public function actionIndex()
	{
		$this->render('index');
	}
	
	public function actionData()
	{
		$dataProvider = CustomerBind::search($_POST);
		if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
		//记录elk日志
		util::elkLog('基础信息', '货主与ERP和WMS关系绑定查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
		Yii::app()->end();
	}
	
	//新增绑定关系
	public function actionAdd()
	{
		//校验权限
		util::operatePriContr(6.1);
		$model = new CustomerBind();
		if (isset($_POST['CustomerBind'])) {
			//校验货主ID是否存在
			$rsCustomer = Customer::model()->findByPk($_POST['CustomerBind']['customer_id']);
			if (empty($rsCustomer)) {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系绑定新增', 'error', $_POST['CustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主ID不存在,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => '货主ID不存在,保存失败'
				)));
			} elseif ($rsCustomer['customer_id'] != $_POST['CustomerBind']['customer_id']) {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系绑定新增', 'error', $_POST['CustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主ID填写不规范，请注意字母的大小写,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => '货主ID填写不规范，请注意字母的大小写,保存失败'
				)));
			}
			//校验ERP编码是否存在
			$rsErp = Erp::model()->find('erp_code = :erp_code AND is_valid=1', array(
			    
					':erp_code' => $_POST['CustomerBind']['erp_code']
			));
			if (empty($rsErp)) {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系绑定新增', 'error', $_POST['CustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'ERP编码不存在或已失效,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => 'ERP编码不存在或已失效,保存失败'
				)));
			} elseif ($rsErp['erp_code'] != $_POST['CustomerBind']['erp_code']) {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系绑定新增', 'error', $_POST['CustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'ERP编码填写不规范，请注意字母的大小写,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => 'ERP编码填写不规范，请注意字母的大小写,保存失败'
				)));
			}
			//校验WMS编码是否存在
			$rsWms = Wms::model()->find('wms_code = :wms_code AND is_valid=1', array(
					':wms_code' => $_POST['CustomerBind']['wms_code']
			));
			if (empty($rsWms)) {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系绑定新增', 'error', $_POST['CustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'WMS编码不存在或已失效,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => 'WMS编码不存在或已失效,保存失败'
				)));
			} elseif ($rsWms['wms_code'] != $_POST['CustomerBind']['wms_code']) {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系绑定新增', 'error', $_POST['CustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'WMS编码填写不规范，请注意字母的大小写,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => 'WMS编码填写不规范，请注意字母的大小写,保存失败'
				)));
			}	
			//校验数据是否已经存在
			$rs = $model->findByPk($_POST['CustomerBind']['customer_id']);
			if (!empty($rs)) {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系绑定新增', 'error', $_POST['CustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'该货主与ERP和WMS的绑定关系已经存在,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => '该货主与ERP和WMS的绑定关系已经存在,保存失败'
				)));
			}
						
			$_POST['CustomerBind']['operator_id'] = Yii::app()->user->soa_id;
			$_POST['CustomerBind']['operator_name'] = Yii::app()->user->user_title;
			$_POST['CustomerBind']['create_time'] = date("Y-m-d H:i:s");
			$model->attributes = $_POST['CustomerBind'];
			if ($model->save()) {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系绑定新增', 'info', $_POST['CustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'保存成功'), 'Y');
				die(json_encode(array(
						'status' => 'ok',
						'msg' => '保存成功'
				)));
				Yii::app()->end();
			} else {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系绑定新增', 'error', $_POST['CustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'错误操作，请找IT部'), 'N');
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
		util::operatePriContr(6.1);
		$model = CustomerBind::model();
		$customerBindInfo = $model->findByPk($id);
		if (isset($_POST['CustomerBind'])) {
		    //校验ERP编码是否存在
			$rsErp = Erp::model()->find('erp_code = :erp_code AND is_valid=1', array(
					':erp_code' => $_POST['CustomerBind']['erp_code']
			));
			if (empty($rsErp)) {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系绑定修改', 'error', $_POST['CustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'ERP编码不存在或已失效,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => 'ERP编码不存在或已失效,保存失败'
				)));
			} elseif ($rsErp['erp_code'] != $_POST['CustomerBind']['erp_code']) {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系绑定修改', 'error', $_POST['CustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'ERP编码填写不规范，请注意字母的大小写,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => 'ERP编码填写不规范，请注意字母的大小写,保存失败'
				)));
			}
			//校验WMS编码是否存在
			$rsWms = Wms::model()->find('wms_code = :wms_code AND is_valid=1', array(
					':wms_code' => $_POST['CustomerBind']['wms_code']
			));
			if (empty($rsWms)) {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系绑定修改', 'error', $_POST['CustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'WMS编码不存在或已失效,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => 'WMS编码不存在或已失效,保存失败'
				)));
			} elseif ($rsWms['wms_code'] != $_POST['CustomerBind']['wms_code']) {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系绑定修改', 'error', $_POST['CustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'WMS编码填写不规范，请注意字母的大小写,保存失败'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => 'WMS编码填写不规范，请注意字母的大小写,保存失败'
				)));
			}	
			$_POST['CustomerBind']['operator_id'] = Yii::app()->user->soa_id;
			$_POST['CustomerBind']['operator_name'] = Yii::app()->user->user_title;
			$model->attributes = $_POST['CustomerBind'];
			if ($model->save()) {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系绑定修改', 'info', $_POST['CustomerBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'更新成功'), 'Y');
				die(json_encode(array(
						'status' => 'ok',
						'msg' => '更新成功'
				)));
				Yii::app()->end();
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
		util::operatePriContr(6.1, 'json');
		if (!isset($_POST['id'])) {
		    //记录elk日志
		    util::elkLog('基础信息', '货主与ERP和WMS关系绑定删除', 'error', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'error', 'msg' => '非法操作'), 'N');
			die(json_encode(array(
					'status' => 'error',
					'msg' => '非法操作'
			)));
		}
		$delModel = $this->loadModel($_POST['id']);
		$_POST['CustomerBind']['is_valid'] = 0;
		$delModel->attributes = $_POST['CustomerBind'];
		if ($delModel->save(false)) {
		    //记录elk日志
		    util::elkLog('基础信息', '货主与ERP和WMS关系绑定删除', 'info', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok', 'msg' => '删除成功'), 'Y');
			die(json_encode(array(
					'status' => 'ok',
					'msg' => '删除成功'
			)));
		}
	}
	
	//推送数据到ERP
	public function actionPushErp()
	{		
		//校验权限
		util::operatePriContr(6.1, 'json');
		if (!isset($_POST['customer_id'])) {
			$this->render('push');
		} else {
			//校验货主有没有绑定仓库
			$rsWare = CustomerWarehouseBind::model()->findAll("customer_id=:customer_id AND type='WH' AND is_valid=1", array(
					':customer_id' => $_POST['customer_id']
			));
			if (empty($rsWare)) {
				die(json_encode(array(
						'status' => 'error',
						'msg' => '该货主ID：'.$_POST['customer_id'].'还没有维护仓库编码，请先维护仓库编码'
				)));
			}
			//获取货主信息
			$rsCustomer = Customer::model()->findByPk($_POST['customer_id']);
			if (empty($rsCustomer)) {
				die(json_encode(array(
						'status' => 'error',
						'msg' => '该货主ID：'.$_POST['customer_id'].'不存在'
				)));
			}
			$sendArr = array();
			$sendArr[0]['CustomerID'] = $rsCustomer['customer_id'];
			$sendArr[0]['Customer_Type'] = 'OW';
			$sendArr[0]['Descr_C'] = $rsCustomer['descr_c'];
			$sendArr[0]['Contact1'] = $rsCustomer['contact1'];
			$sendArr[0]['Contact1_Tel1'] = $rsCustomer['contact1_tel1'];
			$sendArr[0]['Contact1_Tel2'] = $rsCustomer['contact1_tel2'];
			$sendArr[0]['Address1'] = $rsCustomer['address1'];
			$sendArr[0]['NOTES'] = $rsCustomer['remark'];
			$sendArr[0]['Active_Flag'] = $rsCustomer['active_flag'];
			//获取货主绑定的仓库基础信息
			$wareArr = array();
			foreach ($rsWare as $k)
			{
				$wareArr[] = $k['code'];
			}
			$criteria = new CDbCriteria();
			$criteria->compare('warehouse_code', $wareArr);
			$criteria->compare('is_valid', 1);
			$wareInfo = Warehouse::model()->findAll($criteria);
			if (empty($wareInfo)) {
				die(json_encode(array(
						'status' => 'error',
						'msg' => '该货主ID：'.$_POST['customer_id'].'绑定的仓库不存在或已失效'
				)));
			} else {
				$i = 1;
				$relationArr = CustomerBind::paramRelaation('warehouse');
				foreach ($wareInfo as $k => $v)
				{
					foreach ($relationArr as $a => $b)
					{
						$sendArr[$i][$a] = $v[$b];
					}
					$sendArr[$i]['Customer_Type'] = 'WH';
					$i++;
				}
			}
			$sendArr = array('xmldata'=>array('header'=>$sendArr));
			//发送数据给ERP
			$params = array(
					'inner_service' => 'true',
					'method' => 'putCustData_OmsToErp',
					'customer_id' => $_POST['customer_id'],
					'messageid'=>'CUSTOMER',
					'data' => json_encode($sendArr)
			);

			//10为超时时间
			$response = util::curl(OMS_API_URL, $params, 10);
			if ($response == '') {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系推送数据到ERP', 'error', $_POST['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'error', 'msg' => '数据推送失败，请检查ERP接口是否能正常访问'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => '数据推送失败，请检查ERP接口是否能正常访问'
				)));
			} else {
				$responseArr = json_decode($response, true);
				if ($responseArr['returnFlag'] == 1) {
				    //记录elk日志
				    util::elkLog('基础信息', '货主与ERP和WMS关系推送数据到ERP', 'info', $_POST['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok', 'msg' => '推送成功'), 'Y');
					die(json_encode(array(
							'status' => 'ok',
							'msg' => '数据推送成功'
					)));
				} elseif ($responseArr['returnFlag'] == 2) {
				    //记录elk日志
				    util::elkLog('基础信息', '货主与ERP和WMS关系推送数据到ERP', 'error', $_POST['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'error', 'msg' => '数据推送部分成功部分失败：'.$responseArr['returnDesc']), 'N');
					die(json_encode(array(
							'status' => 'error',
							'msg' => '数据推送部分成功部分失败：'.$responseArr['returnDesc']
					)));
				} else {
				    //记录elk日志
				    util::elkLog('基础信息', '货主与ERP和WMS关系推送数据到ERP', 'error', $_POST['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'error', 'msg' => '数据推送部分成功部分失败：'.$responseArr['returnDesc']), 'N');
					die(json_encode(array(
							'status' => 'error',
							'msg' => '数据推送失败：'.$responseArr['returnDesc']
					)));
				}
			}
		}
	}
	
	//推送数据到WMS
	public function actionPushWms()
	{
		//校验权限
		util::operatePriContr(6.1, 'json');
		if (!isset($_POST['customer_id'])) {
			$this->render('push');
		} else {
			//校验货主有没有绑定仓库
			$rsBasic = CustomerWarehouseBind::model()->findAll("customer_id=:customer_id AND is_valid=1", array(
					':customer_id' => $_POST['customer_id']
			));
			if (empty($rsBasic)) {
				die(json_encode(array(
						'status' => 'error',
						'msg' => '该货主ID：'.$_POST['customer_id'].'没有需要推送的数据'
				)));
			}
			//获取货主信息
			$rsCustomer = Customer::model()->findByPk($_POST['customer_id']);
			if (empty($rsCustomer)) {
				die(json_encode(array(
						'status' => 'error',
						'msg' => '该货主ID：'.$_POST['customer_id'].'不存在'
				)));
			}
			$sendArr = array();
			$sendArr[0]['CustomerID'] = $rsCustomer['customer_id'];
			$sendArr[0]['Customer_Type'] = 'OW';
			$sendArr[0]['Descr_C'] = $rsCustomer['descr_c'];
			$sendArr[0]['Contact1'] = $rsCustomer['contact1'];
			$sendArr[0]['Contact1_Tel1'] = $rsCustomer['contact1_tel1'];
			$sendArr[0]['Contact1_Tel2'] = $rsCustomer['contact1_tel2'];
			$sendArr[0]['Address1'] = $rsCustomer['address1'];
			$sendArr[0]['NOTES'] = $rsCustomer['remark'];
			$sendArr[0]['Active_Flag'] = $rsCustomer['active_flag'];
			
            //获取货主绑定的仓库、供应商和店铺信息			
			$wareArr = array();
			$suppArr = array();
			$shopArr = array();
			foreach ($rsBasic as $k)
			{
				if ($k['type'] == 'WH') {
					$wareArr[] = $k['code'];
				} elseif ($k['type'] == 'VE') {
					$suppArr[] = $k['code'];
				} elseif ($k['type'] == 'OT') {
					$shopArr[] = $k['code'];
				}
				
			}
			//获取仓库基础信息
			$criteria = new CDbCriteria();
			$criteria->compare('warehouse_code', $wareArr);
			$criteria->compare('active_flag', 'Y');
			$criteria->compare('is_valid', 1);
			$wareInfo = Warehouse::model()->findAll($criteria);
			if (!empty($wareInfo)) {				
				$i = 1;
				$relationArr = CustomerBind::paramRelaation('warehouse');
				foreach ($wareInfo as $k => $v)
				{
					foreach ($relationArr as $a => $b)
					{
						$sendArr[$i][$a] = $v[$b];
					}
					$sendArr[$i]['Customer_Type'] = 'WH';
					$i++;
				}
			}
			//获取货主对应的供应商信息
			$criteria = new CDbCriteria();
			$criteria->compare('supplier_code', $wareArr);
			$criteria->compare('active_flag', 'Y');
			$criteria->compare('is_valid', 1);
			$suppInfo = Supplier::model()->findAll($criteria);
			if (!empty($suppInfo)) {
				$relationArr = CustomerBind::paramRelaation('supplier');
				foreach ($suppInfo as $k => $v)
				{
					foreach ($relationArr as $a => $b)
					{
						$sendArr[$i][$a] = $v[$b];
					}
					$sendArr[$i]['Customer_Type'] = 'VE';
					$i++;
				}
			}
			//获取货主对应的店铺信息
			$criteria = new CDbCriteria();
			$criteria->compare('shop_code', $wareArr);
			$criteria->compare('active_flag', 'Y');
			$criteria->compare('is_valid', 1);
			$shopInfo = Shop::model()->findAll($criteria);
			if (!empty($shopInfo)) {
				$relationArr = CustomerBind::paramRelaation('shop');
				foreach ($shopInfo as $k => $v)
				{
					foreach ($relationArr as $a => $b)
					{
						$sendArr[$i][$a] = $v[$b];
					}
					$sendArr[$i]['Customer_Type'] = 'OT';
					$i++;
				}
			}	
					
			$sendArr = array('xmldata'=>array('header'=>$sendArr));
			//发送数据给WMS
			$params = array(
					'inner_service' => 'true',
					'method' => 'putCustData_OmsToWms',
					'customer_id' => $_POST['customer_id'],
					'messageid'=>'CUSTOMER',
					'data' => json_encode($sendArr)
			);

			//10为超时时间
			$response = util::curl(OMS_API_URL, $params, 10);			
			if ($response == '') {
			    //记录elk日志
			    util::elkLog('基础信息', '货主与ERP和WMS关系推送数据到WMS', 'error', $_POST['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'error', 'msg' => '数据推送失败，请检查WMS接口是否能正常访问'), 'N');
				die(json_encode(array(
						'status' => 'error',
						'msg' => '数据推送失败，请检查WMS接口是否能正常访问'
				)));
			} else {
				$responseArr = json_decode($response, true);
				if ($responseArr['returnFlag'] == 1) {
				    //记录elk日志
				    util::elkLog('基础信息', '货主与ERP和WMS关系推送数据到WMS', 'info', $_POST['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'ok', 'msg' => '推送成功'), 'Y');
					die(json_encode(array(
							'status' => 'ok',
							'msg' => '数据推送成功'
					)));
				} elseif ($responseArr['returnFlag'] == 2) {
				    //记录elk日志
				    util::elkLog('基础信息', '货主与ERP和WMS关系推送数据到WMS', 'error', $_POST['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'error', 'msg' => '数据推送失败：'.$responseArr['returnDesc']), 'N');
					die(json_encode(array(
							'status' => 'error',
							'msg' => '数据推送部分成功部分失败：'.$responseArr['returnDesc']
					)));
				} else {
				    //记录elk日志
				    util::elkLog('基础信息', '货主与ERP和WMS关系推送数据到WMS', 'error', $_POST['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status' => 'error', 'msg' => '数据推送失败：'.$responseArr['returnDesc']), 'N');
					die(json_encode(array(
							'status' => 'error',
							'msg' => '数据推送失败：'.$responseArr['returnDesc']
					)));
				}
			}
		}
	}
	
	public function loadModel($id)
	{
		if ($this->_model === null) {
			if (isset($id)) {
				$this->_model = CustomerBind::model()->findByPk($id);
			}
			if ($this->_model === null)
				throw new CHttpException(404, 'The requested page does not exist.');
		}
		return $this->_model;
	}
}