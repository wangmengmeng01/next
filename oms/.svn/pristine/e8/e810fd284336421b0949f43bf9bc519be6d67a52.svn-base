<?php
/**
 * 货主与仓库编码绑定
 * @author wp
 *
 */
class CustomerWarehouseBindController extends Controller
{
    private $_model;

    public function actionIndex()
    {
        $this->render('index');
    }

    public function actionData()
    {
        $dataProvider = CustomerWarehouseBind::search($_POST);
        if (!empty($dataProvider)) {
            echo '{"total":' . $dataProvider->getTotalItemCount() . ',"rows":' . CJSON::encode($dataProvider->getData()) . '}';
        }
        //记录elk日志
        util::elkLog('基础信息', '货主与仓库关系维护查询', 'info', '', Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array(), 'Y');
        Yii::app()->end();
    }

    public function actionGetDataByCustomer()
    {
        util::operatePriContr(32.1);
        $model = CustomerWarehouseBind::model();
        $customerId = $_POST['customer_id'];
        $sql = "SELECT relation_id,code FROM　t_customer_relation WHERE customer_id={$customerId}";
        $warehouseListInfo = $model->findBySql($sql)->all();
        $this->render('_form', array(
            'model' => array(
                'customer_id'=>$customerId,
                'warehouse_info'=>$warehouseListInfo
            )
        ));
    }

    //新增货主与仓库绑定关系
    public function actionAdd()
    {
        //校验权限
        util::operatePriContr(5.1);
        $model = new CustomerWarehouseBind();
        if (isset($_POST['CustomerWarehouseBind'])) {
            //校验货主ID是否存在
            $rsCustomer = Customer::model()->findByPk($_POST['CustomerWarehouseBind']['customer_id']);
            if (empty($rsCustomer)) {
                //记录elk日志
                util::elkLog('基础信息', '货主与仓库关系维护新增', 'error', $_POST['CustomerWarehouseBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主ID不存在,保存失败'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '货主ID不存在,保存失败'
                )));
            } elseif ($rsCustomer['customer_id'] != $_POST['CustomerWarehouseBind']['customer_id']) {
                //记录elk日志
                util::elkLog('基础信息', '货主与仓库关系维护新增', 'error', $_POST['CustomerWarehouseBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主ID填写不规范，请注意字母的大小写,保存失败'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '货主ID填写不规范，请注意字母的大小写,保存失败'
                )));
            }
            //校验仓库编码是否存在
            $rsWare = Warehouse::model()->find('warehouse_code = :warehouse_code AND is_valid=1', array(
                ':warehouse_code' => $_POST['CustomerWarehouseBind']['code']
            ));
            if (empty($rsWare)) {
                //记录elk日志
                util::elkLog('基础信息', '货主与仓库关系维护新增', 'error', $_POST['CustomerWarehouseBind']['code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'仓库编码不存在或已失效,保存失败'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '仓库编码不存在或已失效,保存失败'
                )));
            } elseif ($rsWare['warehouse_code'] != $_POST['CustomerWarehouseBind']['code']) {
                //记录elk日志
                util::elkLog('基础信息', '货主与仓库关系维护新增', 'error', $_POST['CustomerWarehouseBind']['code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'仓库编码填写不规范，请注意字母的大小写,保存失败'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '仓库编码填写不规范，请注意字母的大小写,保存失败'
                )));
            }
            //校验数据是否已经存在
            $rs = $model->find("customer_id=:customer_id AND code=:code AND type='WH'", array(
                ':customer_id' => $_POST['CustomerWarehouseBind']['customer_id'],
                ':code' => $_POST['CustomerWarehouseBind']['code']
            ));
            if (!empty($rs)) {
                //记录elk日志
                util::elkLog('基础信息', '货主与仓库关系维护新增', 'error', $_POST['CustomerWarehouseBind']['code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主：' . $_POST['CustomerWarehouseBind']['customer_id'] . '与仓库：' . $_POST['CustomerWarehouseBind']['code'] . '的绑定关系已经存在,保存失败'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '货主：' . $_POST['CustomerWarehouseBind']['customer_id'] . '与仓库：' . $_POST['CustomerWarehouseBind']['code'] . '的绑定关系已经存在,保存失败'
                )));
            }

            $_POST['CustomerWarehouseBind']['type'] = 'WH';
            $_POST['CustomerWarehouseBind']['operator_id'] = Yii::app()->user->soa_id;
            $_POST['CustomerWarehouseBind']['operator_name'] = Yii::app()->user->user_title;
            $_POST['CustomerWarehouseBind']['create_time'] = date("Y-m-d H:i:s");
            $model->attributes = $_POST['CustomerWarehouseBind'];
            if ($model->save()) {
                //记录elk日志
                util::elkLog('基础信息', '货主与仓库关系维护新增', 'info', $_POST['CustomerWarehouseBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'保存成功'), 'Y');
                die(json_encode(array(
                    'status' => 'ok',
                    'msg' => '保存成功'
                )));
                Yii::app()->end();
            } else {
                //记录elk日志
                util::elkLog('基础信息', '货主与仓库关系维护新增', 'error', $_POST['CustomerWarehouseBind']['customer_id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'错误操作，请找IT部'), 'N');
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

    //修改货主与仓库绑定关系
    public function actionUpdate($id)
    {
        //校验权限
        util::operatePriContr(5.1);
        $model = CustomerWarehouseBind::model();
        $customerWarehouseBindInfo = $model->findByPk($id);
        if (isset($_POST['CustomerWarehouseBind'])) {
            //校验仓库编码是否存在
            $rsWare = Warehouse::model()->find('warehouse_code = :warehouse_code AND is_valid=1', array(
                ':warehouse_code' => $_POST['CustomerWarehouseBind']['code']
            ));
            if (empty($rsWare)) {
                //记录elk日志
                util::elkLog('基础信息', '货主与仓库关系维护修改', 'error', $_POST['CustomerWarehouseBind']['code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'仓库编码不存在或已失效,保存失败'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '仓库编码不存在或已失效,保存失败'
                )));
            } elseif ($rsWare['warehouse_code'] != $_POST['CustomerWarehouseBind']['code']) {
                //记录elk日志
                util::elkLog('基础信息', '货主与仓库关系维护修改', 'error', $_POST['CustomerWarehouseBind']['code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'仓库编码填写不规范，请注意字母的大小写,保存失败'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '仓库编码填写不规范，请注意字母的大小写,保存失败'
                )));
            }
            //校验数据是否已经存在
            $rs = $model->find("customer_id=:customer_id AND code=:code AND type='WH' AND relation_id!='$id'", array(
                ':customer_id' => $_POST['CustomerWarehouseBind']['customer_id'],
                ':code' => $_POST['CustomerWarehouseBind']['code']
            ));
            if (!empty($rs)) {
                //记录elk日志
                util::elkLog('基础信息', '货主与仓库关系维护修改', 'error', $_POST['CustomerWarehouseBind']['code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'货主：' . $_POST['CustomerWarehouseBind']['customer_id'] . '与仓库：' . $_POST['CustomerWarehouseBind']['code'] . '的绑定关系已经存在,保存失败'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '货主：' . $_POST['CustomerWarehouseBind']['customer_id'] . '与仓库：' . $_POST['CustomerWarehouseBind']['code'] . '的绑定关系已经存在,保存失败'
                )));
            }

            $_POST['CustomerWarehouseBind']['operator_id'] = Yii::app()->user->soa_id;
            $_POST['CustomerWarehouseBind']['operator_name'] = Yii::app()->user->user_title;
            $customerWarehouseBindInfo->attributes = $_POST['CustomerWarehouseBind'];
            if ($customerWarehouseBindInfo->save()) {
                //记录elk日志
                util::elkLog('基础信息', '货主与仓库关系维护修改', 'info', $_POST['CustomerWarehouseBind']['code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'更新成功'), 'Y');
                die(json_encode(array(
                    'status' => 'ok',
                    'msg' => '更新成功'
                )));
                Yii::app()->end();
            }else{
                //记录elk日志
                util::elkLog('基础信息', '货主与仓库关系维护修改', 'error', $_POST['CustomerWarehouseBind']['code'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'错误操作，请找IT部'), 'N');
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '错误操作，请找IT部'
                )));
            }
        }
        $this->render('_form', array(
            'model' => $customerWarehouseBindInfo
        ));
    }

    //作废货主与仓库的绑定关系
    public function actionDelete()
    {
        //校验权限
        util::operatePriContr(5.1, 'json');
        if (! isset($_POST['id'])) {
            //记录elk日志
            util::elkLog('基础信息', '货主与仓库关系维护删除', 'error', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'error', 'msg'=>'非法操作'), 'N');
            die(json_encode(array(
                'status' => 'error',
                'msg' => '非法操作'
            )));
        }
        $delModel = $this->loadModel($_POST['id']);
        $_POST['CustomerWarehouseBind']['is_valid'] = 0;
        $delModel->attributes = $_POST['CustomerWarehouseBind'];
        if ($delModel->save(false)) {
            //记录elk日志
            util::elkLog('基础信息', '货主与仓库关系维护删除', 'info', $_POST['id'], Yii::app()->user->soa_id, Yii::app()->user->gsbm, util::getRealIP(), $_POST, array('status'=>'ok', 'msg'=>'删除成功'), 'Y');
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
        util::operatePriContr(5.1, 'json');
        if (!isset($_POST['customer_id'])) {
            $this->render('push');
        } else {
            //获取调用接口系统级参数
            $rsCustomer = QimenCustomerBind::model()->find('customer_id=:customer_id',array(':customer_id'=>$_POST['customer_id']));
            if (empty($rsCustomer)) {
                //记录elk日志
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => '未找到该货主对应的客户信息！'
                )));
            }
            $customerInfo = QimenCustomer::model()->findByPk($rsCustomer['qimen_customer_id']);
            if (empty($customerInfo)) {
                //记录elk日志
                die(json_encode(array(
                    'status' => 'error',
                    'msg' => $customerInfo['customer_id']
                )));
            }

            $warehouseCode = $_POST['code'];
            $whInfo = Warehouse::model()->find('warehouse_code=:warehouse_code',array(':warehouse_code'=>$warehouseCode));
            if (isset($whInfo['wms_url']) && !empty($whInfo['wms_url'])) {
                $url = $whInfo['wms_url'];
            } else {
                $url = $customerInfo['wms_api_url'];
            }

            $requestXml = '<?xml version="1.0" encoding="utf-8"?><request><CustomerID>' . $_POST['customer_id'] . '</CustomerID><Customer_Type>OW</Customer_Type><Active_Flag>1</Active_Flag><UDF5>1</UDF5></request>';
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
            $response = $httpObj->post($url, $apiParams);

            if ($response == '') {
                //记录elk日志
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
                    die(json_encode(array(
                        'status' => 'ok',
                        'msg' => '货主同步成功！'
                    )));
                } else {
                    $this->writeSendLog($rsCustomer['customer_id'], 0, $responseArr['code'], $responseArr['message']);
                    //记录elk日志
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
                $this->_model = CustomerWarehouseBind::model()->findByPk($id);
            }
            if ($this->_model === null)
                throw new CHttpException(404, 'The requested page does not exist.');
        }
        return $this->_model;
    }
}