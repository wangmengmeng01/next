<?php

/**
 * @author 敬
 * 功能： Excel导出
 */
class ExportController extends Controller
{
    public function actionIndex()
    {
        $exportType = Yii::app()->request->getParam('exportType');
        switch ($exportType) {
            case 'asnData':
                $this->asnDataExport($_GET);
                break;
            case 'inboundDetail':
                $this->inboundDetailExport($_GET);
                break;
            case 'confirmAsnData':
            	$this->confirmAsnDataExport($_GET);
            	break;
            case 'inboundRecordDetail':
            	$this->inboundRecordDetailExport($_GET);
            	break;
            case 'cancelAsnData':
            	$this->cancelAsnDataExport($_GET);
            	break;
            case 'putSoData':
            	$this->putSoDataExport($_GET);
            	break;
            case 'outboundDetail':
            	$this->outboundDetailExport($_GET);
            	break;
            case 'confirmSoData':
            	$this->confirmSoDataExport($_GET);
            	break;
            case 'outboundRecodeDetail':
            	$this->outboundRecodeDetailExport($_GET);
            	break;
            case 'confirmSoStatus':
            	$this->confirmSoStatusExport($_GET);
            	break;
            case 'cancelSoData':
            	$this->cancelSoDataExport($_GET);
            	break;
            case 'putSoEcpt':
            	$this->putSoEcptExport($_GET);
            	break;
            case 'outboundEcptDetail':
            	$this->outboundEcptDetailExport($_GET);
            	break;
            case 'queryInvData':
            	$this->queryInvDataExport($_GET);
            	break;
            case 'product':
            	$this->productExport($_GET);
            	break;
        	case 'customerShipments':
        	    $this->customerShipmentsExport($_GET);
        	    break;
    	    case 'warehouseShipments':
    	        $this->warehouseShipmentsExport($_GET);
    	        break;
	        case 'deliveryOrderCreate':
	            $this->deliveryOrderCreateExport($_GET);
	            break;
            case 'deliveryOrderDetail':
                $this->deliveryOrderDetailExport($_GET);
                break;
            case 'deliveryOrderInvoice':
                $this->deliveryOrderInvoiceExport($_GET);
                break;
            case 'deliveryInvoiceDetail':
                $this->deliveryInvoiceDetailExport($_GET);
                break;
            case 'deliveryOrderRecord':
                $this->deliveryOrderRecordExport($_GET);
                break;
            case 'deliveryRecordDetail':
                $this->deliveryRecordDetailExport($_GET);
                break;
            case 'deliveryDetailBatchRecord':
                $this->deliveryDetailBatchRecordExport($_GET);
                break;
            case 'deliveryRecordPackage':
                $this->deliveryRecordPackageExport($_GET);
                break;
            case 'deliveryRecordMaterialPackage':
                $this->deliveryRecordMaterialPackageExport($_GET);
                break;
            case 'deliveryRecordProductPackage':
                $this->deliveryRecordProductPackageExport($_GET);
                break;
            case 'deliveryRecordInvoice':
                $this->deliveryRecordInvoiceExport($_GET);
                break;
            case 'deliveryRecordInvoiceDetail':
                $this->deliveryRecordInvoiceDetailExport($_GET);
                break;
            case 'deliveryOrderCancel':
                $this->deliveryOrderCancelExport($_GET);
                break;
            case 'deliverySnReport':
                $this->deliverySnReportExport($_GET);
                break;
            case 'deliverySnReportDetail':
                $this->deliverySnReportDetailExport($_GET);
                break;
            case 'deliveryItemLackReport':
                $this->deliveryItemLackReportExport($_GET);
                break;
            case 'deliveryItemLackReportDetail':
                $this->deliveryItemLackReportDetailExport($_GET);
                break;
            case 'combineProduct':
                $this->combineProductExport($_GET);
                break;
            case 'combineProductDetail':
                $this->combineProductDetailExport($_GET);
                break;
            case 'inboundRecordBatchDetail':
                $this->inboundRecordBatchDetailExport($_GET);
                break;
            case 'outboundRecordPackage':
                $this->outboundRecordPackageExport($_GET);
                break;
            case 'outboundRecordMaterialPackage':
                $this->outboundRecordMaterialPackageExport($_GET);
                break;
            case 'outboundRecordProductPackage':
                $this->outboundRecordProductPackageExport($_GET);
                break;
            case 'outboundRecordBatchDetail':
                $this->outboundRecordBatchDetailExport($_GET);
                break;
            case 'stockChangeReport':
                $this->stockChangeReportExport($_GET);
                break;
            case 'stockChangeBatchReport':
                $this->stockChangeBatchReportExport($_GET);
                break;
            case 'inventoryReport':
                $this->inventoryReportExport($_GET);
                break;
            case 'inventoryProductReport':
                $this->inventoryProductReportExport($_GET);
                break;
            case 'inventorySumAvailable':
                $this->inventorySumAvailableExport($_GET);
                break;
            case 'storeProcessCreate':
                $this->storeProcessCreateExport($_GET);
                break;
            case 'storeProcessMaterialInfo':
                $this->storeProcessMaterialInfoExport($_GET);
                break;
            case 'storeProcessProductInfo':
                $this->storeProcessProductInfoExport($_GET);
                break;
            case 'storeProcessConfirm':
                $this->storeProcessConfirmExport($_GET);
                break;
            case 'storeProcessMaterialConfirm':
                $this->storeProcessMaterialConfirmExport($_GET);
                break;
            case 'storeProcessProductConfirm':
                $this->storeProcessProductConfirmExport($_GET);
                break;
            case 'storeProcessCancel':
                $this->storeProcessCancelExport($_GET);
                break;
            case 'orderProcessReport':
                $this->orderProcessReportExport($_GET);
                break;
            case 'deliveryOrderShortage':
                $this->deliveryOrderShortageExport($_GET);
                break;
            case 'vipJitPickList':
                $this->vipJitPickListExport($_GET);
                break;
            case 'vipJitPickDetail':
                $this->vipJitPickDetailExport($_GET);
                break;
            case 'vipJitPoList':
                $this->vipJitPoListExport($_GET);
                break;
        }
    }
    
    //导出入库单单头信息
    public function asnDataExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(12.1, 'text');
        $values = array();
        //检索条件    
        !empty($param['orderNo'])? $values['PutASNData']['order_no'] = util::checkInput($param['orderNo']):'';//订单号
        !empty($param['orderType'])? $values['PutASNData']['order_type'] = util::checkInput($param['orderType']):'';//订单类型
        !empty($param['customerId'])? $values['PutASNData']['customer_id'] = util::checkInput($param['customerId']):'';//客户ID
        !empty($param['warehouseCode'])? $values['PutASNData']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';//仓库编码
        !empty($param['startTime'])? $values['PutASNData']['start_time'] = util::checkInput($param['startTime']):'';//开始时间
        !empty($param['endTime'])? $values['PutASNData']['end_time'] = util::checkInput($param['endTime']):'';//结束时间
        //获取数据
		$dataProvider = PutASNData::search($values);       
	    //输出
	    util::export($filename='入库单下发报表', $columns = PutASNData::getColumns(), $dataProvider);
	    Yii::app()->end();
    }
    
    //导出入库单明细信息
    public function inboundDetailExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(12.1, 'text');
    	$values = array();
    	//检索条件
    	!empty($param['order_id'])? $values['order_id'] = util::checkInput($param['order_id']):'';
    	!empty($param['order_no'])? $orderNo = util::checkInput($param['order_no']):'';
    	//获取数据
    	$dataProvider = InboundDetail::search($values);    
    	//输出
    	util::export($filename='入库单' . $orderNo . '下发明细报表', $columns = InboundDetail::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出入库单状态明细回传单头信息
    public function confirmAsnDataExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(13.1, 'text');
    	$values = array();
    	//检索条件
    	!empty($param['omsOrderNo'])? $values['ConfirmASNData']['oms_order_no'] = util::checkInput($param['omsOrderNo']):'';
    	!empty($param['wmsOrderNo'])? $values['ConfirmASNData']['wms_order_no'] = util::checkInput($param['wmsOrderNo']):'';
    	!empty($param['orderType'])? $values['ConfirmASNData']['order_type'] = util::checkInput($param['orderType']):'';
    	!empty($param['customerId'])? $values['ConfirmASNData']['customer_id'] = util::checkInput($param['customerId']):'';
    	!empty($param['warehouseCode'])? $values['ConfirmASNData']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
    	!empty($param['startTime'])? $values['ConfirmASNData']['start_time'] = util::checkInput($param['startTime']):'';
    	!empty($param['endTime'])? $values['ConfirmASNData']['end_time'] = util::checkInput($param['endTime']):'';
    	//获取数据
    	$dataProvider = ConfirmASNData::search($values);   	
    	//输出
    	util::export($filename='入库单状态明细回传报表', $columns = ConfirmASNData::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出入库单状态明细回传明细信息
    public function inboundRecordDetailExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(13.1, 'text');
    	$values = array();
    	//检索条件
    	!empty($param['order_id'])? $values['order_id'] = util::checkInput($param['order_id']):'';
    	!empty($param['order_no'])? $orderNo = util::checkInput($param['order_no']):'';
    	!empty($param['record_id'])? $values['record_id'] = util::checkInput($param['record_id']):'';
    	!empty($param['create_time'])? $values['create_time'] = util::checkInput($param['create_time']):'';
    	//获取数据
    	$dataProvider = InboundRecodeDetail::search($values);   
    	//输出
    	util::export($filename='入库单' . $orderNo . '状态明细回传明细报表', $columns = InboundRecodeDetail::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出入库单取消报表
    public function cancelAsnDataExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(14.1, 'text');
    	$values = array();
    	//检索条件
    	!empty($param['orderNo'])? $values['CancelASNData']['order_no'] = util::checkInput($param['orderNo']):'';
        !empty($param['orderType'])? $values['CancelASNData']['order_type'] = util::checkInput($param['orderType']):'';
        !empty($param['customerId'])? $values['CancelASNData']['customer_id'] = util::checkInput($param['customerId']):'';
        !empty($param['warehouseCode'])? $values['CancelASNData']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        !empty($param['startTime'])? $values['CancelASNData']['start_time'] = util::checkInput($param['startTime']):'';
        !empty($param['endTime'])? $values['CancelASNData']['end_time'] = util::checkInput($param['endTime']):'';
    	//获取数据
    	$dataProvider = CancelASNData::search($values);    	 
    	//输出
    	util::export($filename='入库单取消报表', $columns = CancelASNData::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出出库单下发单头信息
    public function putSoDataExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(15.1, 'text');
    	$values = array();
    	//检索条件
    	!empty($param['orderNo'])? $values['PutSOData']['order_no'] = util::checkInput($param['orderNo']):'';
    	!empty($param['orderType'])? $values['PutSOData']['order_type'] = util::checkInput($param['orderType']):'';
    	!empty($param['customerId'])? $values['PutSOData']['customer_id'] = util::checkInput($param['customerId']):'';
    	!empty($param['warehouseCode'])? $values['PutSOData']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        isset($param['fxFlag'])? $values['PutSOData']['fx_flag'] = util::checkInput($param['fxFlag']):'';
    	!empty($param['startTime'])? $values['PutSOData']['start_time'] = util::checkInput($param['startTime']):'';
    	!empty($param['endTime'])? $values['PutSOData']['end_time'] = util::checkInput($param['endTime']):'';
    	//获取数据
    	$dataProvider = PutSOData::search($values);
    	//输出
    	util::export($filename='出库单下单报表', $columns = PutSOData::getColumns(), $dataProvider);
    	Yii::app()->end();
    }    
    
    //导出出库单下发明细信息
    public function outboundDetailExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(15.1, 'text');
    	$values = array();
    	//检索条件
    	!empty($param['order_id'])? $values['order_id'] = util::checkInput($param['order_id']):'';
    	!empty($param['order_no'])? $orderNo = util::checkInput($param['order_no']):'';
    	//获取数据
    	$dataProvider = OutboundDetail::search($values);
    	//输出
    	util::export($filename='出库单' . $orderNo . '下发明细报表', $columns = OutboundDetail::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出出库单明细回传单头信息
    public function confirmSoDataExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(16.1, 'text');
    	$values = array();
    	//检索条件
    	!empty($param['omsOrderNo'])? $values['ConfirmSOData']['oms_order_no'] = util::checkInput($param['omsOrderNo']):'';
    	!empty($param['wmsOrderNo'])? $values['ConfirmSOData']['wms_order_no'] = util::checkInput($param['wmsOrderNo']):'';
    	!empty($param['orderType'])? $values['ConfirmSOData']['order_type'] = util::checkInput($param['orderType']):'';
    	!empty($param['customerId'])? $values['ConfirmSOData']['customer_id'] = util::checkInput($param['customerId']):'';
    	!empty($param['warehouseCode'])? $values['ConfirmSOData']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
    	!empty($param['startTime'])? $values['ConfirmSOData']['start_time'] = util::checkInput($param['startTime']):'';
    	!empty($param['endTime'])? $values['ConfirmSOData']['end_time'] = util::checkInput($param['endTime']):'';
    	//获取数据
    	$dataProvider = ConfirmSOData::search($values);
    	//输出
    	util::export($filename='出库单明细回传报表', $columns = ConfirmSOData::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出出库单明细回传明细信息
    public function outboundRecodeDetailExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(16.1, 'text');
    	$values = array();
    	//检索条件
    	!empty($param['order_id'])? $values['order_id'] = util::checkInput($param['order_id']):'';
    	!empty($param['order_no'])? $orderNo = util::checkInput($param['order_no']):'';
    	!empty($param['record_id'])? $values['record_id'] = util::checkInput($param['record_id']):'';
    	!empty($param['create_time'])? $values['create_time'] = util::checkInput($param['create_time']):'';
    	//获取数据
    	$dataProvider = OutboundRecodeDetail::search($values);
    	//输出
    	util::export($filename='出库单' . $orderNo . '明细回传明细报表', $columns = OutboundRecodeDetail::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出出库单状态回传信息
    public function confirmSoStatusExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(17.1, 'text');
    	$values = array();
    	//检索条件
    	!empty($param['orderNo'])? $values['ConfirmSOStatus']['order_no'] = util::checkInput($param['orderNo']):'';
    	!empty($param['orderType'])? $values['ConfirmSOStatus']['order_type'] = util::checkInput($param['orderType']):'';
    	!empty($param['customerId'])? $values['ConfirmSOStatus']['customer_id'] = util::checkInput($param['customerId']):'';
    	!empty($param['startTime'])? $values['ConfirmSOStatus']['start_time'] = util::checkInput($param['startTime']):'';
    	!empty($param['endTime'])? $values['ConfirmSOStatus']['end_time'] = util::checkInput($param['endTime']):'';
    	//获取数据
    	$dataProvider = ConfirmSOStatus::search($values);
    	//输出
    	util::export($filename='出库单状态回传报表', $columns = ConfirmSOStatus::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出出库单取消报表
    public function cancelSoDataExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(18.1, 'text');
    	$values = array();
    	//检索条件
    	!empty($param['orderNo'])? $values['CancelSOData']['order_no'] = util::checkInput($param['orderNo']):'';
    	!empty($param['orderType'])? $values['CancelSOData']['order_type'] = util::checkInput($param['orderType']):'';
    	!empty($param['customerId'])? $values['CancelSOData']['customer_id'] = util::checkInput($param['customerId']):'';
    	!empty($param['warehouseCode'])? $values['CancelSOData']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
    	!empty($param['startTime'])? $values['CancelSOData']['start_time'] = util::checkInput($param['startTime']):'';
    	!empty($param['endTime'])? $values['CancelSOData']['end_time'] = util::checkInput($param['endTime']):'';
    	//获取数据
    	$dataProvider = CancelSOData::search($values);
    	//输出
    	util::export($filename='出库单取消报表', $columns = CancelSOData::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出异常出库单单头信息
    public function putSoEcptExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(19.1, 'text');
    	$values = array();
    	//检索条件
    	!empty($param['orderNo'])? $values['PutSOEcpt']['order_no'] = util::checkInput($param['orderNo']):'';
    	!empty($param['orderType'])? $values['PutSOEcpt']['order_type'] = util::checkInput($param['orderType']):'';
    	!empty($param['customerId'])? $values['PutSOEcpt']['customer_id'] = util::checkInput($param['customerId']):'';
    	!empty($param['warehouseCode'])? $values['PutSOEcpt']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
    	!empty($param['startTime'])? $values['PutSOEcpt']['start_time'] = util::checkInput($param['startTime']):'';
    	!empty($param['endTime'])? $values['PutSOEcpt']['end_time'] = util::checkInput($param['endTime']):'';
    	//获取数据
    	$dataProvider = PutSOEcpt::search($values);
    	//输出
    	util::export($filename='出库单异常报表', $columns = PutSOEcpt::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出异常出库单明细信息
    public function outboundEcptDetailExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(19.1, 'text');
    	$values = array();
    	//检索条件
    	!empty($param['order_id'])? $values['order_id'] = util::checkInput($param['order_id']):'';
    	!empty($param['order_no'])? $orderNo = util::checkInput($param['order_no']):'';
    	//获取数据
    	$dataProvider = OutboundEcptDetail::search($values);
    	//输出
    	util::export($filename='出库单' . $orderNo . '异常明细报表', $columns = OutboundEcptDetail::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出库存信息
    public function queryInvDataExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(20.1, 'text');
    	$values = array();
    	//检索条件
    	!empty($param['sku'])? $values['QueryINVData']['sku'] = util::checkInput($param['sku']):'';
    	!empty($param['customerId'])? $values['QueryINVData']['customer_id'] = util::checkInput($param['customerId']):'';
    	!empty($param['warehouseCode'])? $values['QueryINVData']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
    	//获取数据
    	$dataProvider = QueryINVData::search($values);
    	//输出
    	util::export($filename='库存查询报表', $columns = QueryINVData::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出商品信息
    public function productExport($param)
    {
    	//校验导出权限
    	util::operatePriContr(10.1, 'text');
    	$values = array();
    	//检索条件
    	!empty($param['sku'])? $values['Product']['sku'] = util::checkInput($param['sku']):'';
    	!empty($param['descr_c'])? $values['Product']['descr_c'] = util::checkInput($param['descr_c']):'';
    	!empty($param['customerId'])? $values['Product']['customer_id'] = util::checkInput($param['customerId']):'';    	
    	!empty($param['activeFlag'])? $values['Product']['active_flag'] = util::checkInput($param['activeFlag']):'';
    	//获取数据
    	$dataProvider = Product::search($values);
    	//输出
    	util::export($filename='商品报表', $columns = Product::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出仓库发货量报表
    public function warehouseShipmentsExport($param)
    {
        //校验导出权限
        util::operatePriContr(34.1, 'text');
        $values = array();
        //检索条件
        !empty($param['warehouseName'])? $values['WarehouseShipments']['warehouse_name'] = util::checkInput($param['warehouseName']):'';
        !empty($param['warehouseCode'])? $values['WarehouseShipments']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        !empty($param['gsName'])? $values['WarehouseShipments']['gs_name'] = util::checkInput($param['gsName']):'';
        !empty($param['gsCode'])? $values['WarehouseShipments']['gs_code'] = util::checkInput($param['gsCode']):'';
        !empty($param['province'])? $values['WarehouseShipments']['province'] = util::checkInput($param['province']):'';
        !empty($param['wmsName'])? $values['WarehouseShipments']['wms_name'] = util::checkInput($param['wmsName']):'';
        !empty($param['startTime'])? $values['WarehouseShipments']['start_time'] = util::checkInput($param['startTime']):'';
    	!empty($param['endTime'])? $values['WarehouseShipments']['end_time'] = util::checkInput($param['endTime']):'';
    	$values['WarehouseShipments']['operate_type'] = 'excel_export';
        //获取数据
        $dataProvider = WarehouseShipments::search($values);
        //输出
        util::export($filename='仓库发货量报表', $columns = WarehouseShipments::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出货主发货量报表
    public function customerShipmentsExport($param)
    {
        //校验导出权限
        util::operatePriContr(33.1, 'text');
        $values = array();
        //检索条件
        !empty($param['customerName'])? $values['CustomerShipments']['customer_name'] = util::checkInput($param['customerName']):'';
        !empty($param['customerId'])? $values['CustomerShipments']['customer_id'] = util::checkInput($param['customerId']):'';
        !empty($param['gsName'])? $values['CustomerShipments']['gs_name'] = util::checkInput($param['gsName']):'';
        !empty($param['gsCode'])? $values['CustomerShipments']['gs_code'] = util::checkInput($param['gsCode']):'';
        !empty($param['warehouseName'])? $values['CustomerShipments']['warehouse_name'] = util::checkInput($param['warehouseName']):'';
        !empty($param['warehouseCode'])? $values['CustomerShipments']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        !empty($param['province'])? $values['CustomerShipments']['province'] = util::checkInput($param['province']):'';
        !empty($param['startTime'])? $values['CustomerShipments']['start_time'] = util::checkInput($param['startTime']):'';
        !empty($param['endTime'])? $values['CustomerShipments']['end_time'] = util::checkInput($param['endTime']):'';
        $values['CustomerShipments']['operate_type'] = 'excel_export';
        //获取数据
        $dataProvider = CustomerShipments::search($values);
        //输出
        util::export($filename='货主仓库发货量报表', $columns = CustomerShipments::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出发货单下发信息
    public function deliveryOrderCreateExport($param)
    {
        //校验导出权限
        util::operatePriContr(28.1, 'text');
        $values = array();
        //检索条件
        !empty($param['deliveryOrderCode'])? $values['DeliveryOrderCreate']['delivery_order_code'] = util::checkInput($param['deliveryOrderCode']):'';
        !empty($param['customerId'])? $values['DeliveryOrderCreate']['customer_id'] = util::checkInput($param['customerId']):'';
        !empty($param['orderType'])? $values['DeliveryOrderCreate']['order_type'] = util::checkInput($param['orderType']):'';
        !empty($param['warehouseCode'])? $values['DeliveryOrderCreate']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        isset($param['fxFlag'])? $values['DeliveryOrderCreate']['fx_flag'] = util::checkInput($param['fxFlag']):'';
        !empty($param['startTime'])? $values['DeliveryOrderCreate']['start_time'] = util::checkInput($param['startTime']):'';
        !empty($param['endTime'])? $values['DeliveryOrderCreate']['end_time'] = util::checkInput($param['endTime']):'';
        //获取数据
        $dataProvider = DeliveryOrderCreate::search($values);
        //输出
        util::export($filename='发货单下发报表', $columns = DeliveryOrderCreate::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出发货单下发明细
    public function deliveryOrderDetailExport($param)
    {
        //校验导出权限
        util::operatePriContr(28.1, 'text');
        $values = array();
    	//检索条件
    	!empty($param['delivery_id'])? $values['delivery_id'] = util::checkInput($param['delivery_id']):'';
    	!empty($param['delivery_order_code'])? $deliveryOrderCode = util::checkInput($param['delivery_order_code']):'';
    	//获取数据
    	$dataProvider = DeliveryOrderDetail::search($values);
    	//输出
    	util::export($filename='发货单' . $deliveryOrderCode . '下发明细报表', $columns = DeliveryOrderDetail::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出发货单发票报表
    public function deliveryOrderInvoiceExport($param)
    {
        //校验导出权限
        util::operatePriContr(28.1, 'text');
        $values = array();
    	//检索条件
    	!empty($param['delivery_id'])? $values['delivery_id'] = util::checkInput($param['delivery_id']):'';
    	!empty($param['delivery_order_code'])? $deliveryOrderCode = util::checkInput($param['delivery_order_code']):'';
    	//获取数据
    	$dataProvider = DeliveryOrderInvoice::search($values);
    	//输出
    	util::export($filename='发货单' . $deliveryOrderCode . '下发发票报表', $columns = DeliveryOrderInvoice::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出发货单发票明细报表
    public function deliveryInvoiceDetailExport($param)
    {
        //校验导出权限
        util::operatePriContr(28.1, 'text');
        $values = array();
        //检索条件
        !empty($param['bill_id'])? $values['bill_id'] = util::checkInput($param['bill_id']):'';
        
        //获取数据
        $dataProvider = DeliveryInvoiceDetail::search($values);
        //输出
        util::export($filename='发货单发票商品明细报表', $columns = DeliveryInvoiceDetail::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出发货单回传单头报表
    public function deliveryOrderRecordExport($param)
    {
        //校验导出权限
        util::operatePriContr(29.1, 'text');
        $values = array();
        //检索条件
        !empty($param['deliveryOrderCode'])? $values['DeliveryOrderRecord']['delivery_order_code'] = util::checkInput($param['deliveryOrderCode']):'';
        !empty($param['customerId'])? $values['DeliveryOrderRecord']['customer_id'] = util::checkInput($param['customerId']):'';
        !empty($param['orderType'])? $values['DeliveryOrderRecord']['order_type'] = util::checkInput($param['orderType']):'';
        !empty($param['warehouseCode'])? $values['DeliveryOrderRecord']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        !empty($param['startTime'])? $values['DeliveryOrderRecord']['start_time'] = util::checkInput($param['startTime']):'';
        !empty($param['endTime'])? $values['DeliveryOrderRecord']['end_time'] = util::checkInput($param['endTime']):'';
        //获取数据
        $dataProvider = DeliveryOrderRecord::search($values);
        //输出
        util::export($filename='发货单回传报表', $columns = DeliveryOrderRecord::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出发货单回传明细报表
    public function deliveryRecordDetailExport($param)
    {
        //校验导出权限
        util::operatePriContr(29.1, 'text');
        $values = array();
    	//检索条件
    	!empty($param['delivery_id'])? $values['delivery_id'] = util::checkInput($param['delivery_id']):'';
    	!empty($param['delivery_order_code'])? $deliveryOrderCode = util::checkInput($param['delivery_order_code']):'';
    	//获取数据
    	$dataProvider = DeliveryRecordDetail::search($values);
    	//输出
    	util::export($filename='发货单' . $deliveryOrderCode . '回传明细报表', $columns = DeliveryRecordDetail::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出发货单回传明细批次报表
    public function deliveryDetailBatchRecordExport($param)
    {
        //校验导出权限
        util::operatePriContr(29.1, 'text');
        $values = array();
        //检索条件
        !empty($param['detail_id'])? $values['detail_id'] = util::checkInput($param['detail_id']):'';
        //获取数据
        $dataProvider = DeliveryDetailBatchRecord::search($values);
        //输出
        util::export($filename='发货单回传明细批次报表', $columns = DeliveryDetailBatchRecord::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出发货单包裹明细报表
    public function deliveryRecordPackageExport($param)
    {
        //校验导出权限
        util::operatePriContr(29.1, 'text');
        $values = array();
    	//检索条件
    	!empty($param['delivery_id'])? $values['delivery_id'] = util::checkInput($param['delivery_id']):'';
    	!empty($param['delivery_order_code'])? $deliveryOrderCode = util::checkInput($param['delivery_order_code']):'';
    	//获取数据
    	$dataProvider = DeliveryRecordPackage::search($values);
    	//输出
    	util::export($filename='发货单' . $deliveryOrderCode . '包裹明细报表', $columns = DeliveryRecordPackage::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出发货单回传包裹材料明细报表
    public function deliveryRecordMaterialPackageExport($param)
    {
        //校验导出权限
        util::operatePriContr(29.1, 'text');
        $values = array();
        //检索条件
        !empty($param['package_id'])? $values['package_id'] = util::checkInput($param['package_id']):'';
        //获取数据
        $dataProvider = DeliveryRecordMaterialPackage::search($values);
        //输出
        util::export($filename='发货单回传包裹材料明细报表', $columns = DeliveryRecordMaterialPackage::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出发货单回传包裹商品明细报表
    public function deliveryRecordProductPackageExport($param)
    {
        //校验导出权限
        util::operatePriContr(29.1, 'text');
        $values = array();
        //检索条件
        !empty($param['package_id'])? $values['package_id'] = util::checkInput($param['package_id']):'';
        //获取数据
        $dataProvider = DeliveryRecordProductPackage::search($values);
        //输出
        util::export($filename='发货单回传包裹商品明细报表', $columns = DeliveryRecordProductPackage::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    
    //导出发货单回传发票报表
    public function deliveryRecordInvoiceExport($param)
    {
        //校验导出权限
        util::operatePriContr(29.1, 'text');
        $values = array();
    	//检索条件
    	!empty($param['delivery_id'])? $values['delivery_id'] = util::checkInput($param['delivery_id']):'';
    	!empty($param['delivery_order_code'])? $deliveryOrderCode = util::checkInput($param['delivery_order_code']):'';
    	//获取数据
    	$dataProvider = DeliveryRecordInvoice::search($values);
    	//输出
    	util::export($filename='发货单' . $deliveryOrderCode . '回传发票报表', $columns = DeliveryRecordInvoice::getColumns(), $dataProvider);
    	Yii::app()->end();
    }
    
    //导出发货单回传发票明细报表
    public function deliveryRecordInvoiceDetailExport($param)
    {
        //校验导出权限
        util::operatePriContr(29.1, 'text');
        $values = array();
        //检索条件
        !empty($param['bill_id'])? $values['bill_id'] = util::checkInput($param['bill_id']):'';
    
        //获取数据
        $dataProvider = DeliveryRecordInvoiceDetail::search($values);
        //输出
        util::export($filename='发货单回传发票商品明细报表', $columns = DeliveryRecordInvoiceDetail::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出发货单取消报表
    public function deliveryOrderCancelExport($param)
    {
        //校验导出权限
        util::operatePriContr(30.1, 'text');
        $values = array();
        //检索条件
        !empty($param['orderNo'])? $values['DeliveryOrderCancel']['order_no'] = util::checkInput($param['orderNo']):'';
        !empty($param['customerId'])? $values['DeliveryOrderCancel']['customer_id'] = util::checkInput($param['customerId']):'';
        !empty($param['orderType'])? $values['DeliveryOrderCancel']['order_type'] = util::checkInput($param['orderType']):'';
        !empty($param['warehouseCode'])? $values['DeliveryOrderCancel']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        !empty($param['startTime'])? $values['DeliveryOrderCancel']['start_time'] = util::checkInput($param['startTime']):'';
        !empty($param['endTime'])? $values['DeliveryOrderCancel']['end_time'] = util::checkInput($param['endTime']):'';
        //获取数据
        $dataProvider = DeliveryOrderCancel::search($values);
        //输出
        util::export($filename='发货单取消报表', $columns = DeliveryOrderCancel::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出发货单SN通知报表
    public function deliverySnReportExport($param)
    {
        //校验导出权限
        util::operatePriContr(31.1, 'text');
        $values = array();
        //检索条件
        !empty($param['deliveryOrderCode'])? $values['DeliverySnReport']['delivery_order_code'] = util::checkInput($param['deliveryOrderCode']):'';
        !empty($param['customerId'])? $values['DeliverySnReport']['customer_id'] = util::checkInput($param['customerId']):'';
        !empty($param['orderType'])? $values['DeliverySnReport']['order_type'] = util::checkInput($param['orderType']):'';
        !empty($param['warehouseCode'])? $values['DeliverySnReport']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        !empty($param['startTime'])? $values['DeliverySnReport']['start_time'] = util::checkInput($param['startTime']):'';
        !empty($param['endTime'])? $values['DeliverySnReport']['end_time'] = util::checkInput($param['endTime']):'';
        //获取数据
        $dataProvider = DeliverySnReport::search($values);
        //输出
        util::export($filename='发货单SN通知报表', $columns = DeliverySnReport::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出发货单SN通知明细
    public function deliverySnReportDetailExport($param)
    {
        //校验导出权限
        util::operatePriContr(31.1, 'text');
        $values = array();
        //检索条件
        !empty($param['sn_id'])? $values['sn_id'] = util::checkInput($param['sn_id']):'';
        !empty($param['delivery_order_code'])? $deliveryOrderCode = util::checkInput($param['delivery_order_code']):'';
        //获取数据
        $dataProvider = DeliverySnReportDetail::search($values);
        //输出
        util::export($filename='发货单' . $deliveryOrderCode . ' SN通知明细报表', $columns = DeliverySnReportDetail::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出发货单缺货通知
    public function deliveryItemLackReportExport($param)
    {
        //校验导出权限
        util::operatePriContr(32.1, 'text');
        $values = array();
        //检索条件
        !empty($param['deliveryOrderCode'])? $values['DeliveryItemLackReport']['delivery_order_code'] = util::checkInput($param['deliveryOrderCode']):'';
        !empty($param['customerId'])? $values['DeliveryItemLackReport']['customer_id'] = util::checkInput($param['customerId']):'';
        !empty($param['warehouseCode'])? $values['DeliveryItemLackReport']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        !empty($param['startTime'])? $values['DeliveryItemLackReport']['start_time'] = util::checkInput($param['startTime']):'';
        !empty($param['endTime'])? $values['DeliveryItemLackReport']['end_time'] = util::checkInput($param['endTime']):'';
        //获取数据
        $dataProvider = DeliveryItemLackReport::search($values);
        //输出
        util::export($filename='发货单缺货通知报表', $columns = DeliveryItemLackReport::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出发货单缺货通知明细
    public function deliveryItemLackReportDetailExport($param)
    {
        //校验导出权限
        util::operatePriContr(32.1, 'text');
        $values = array();
        //检索条件
        !empty($param['deliv_lack_id'])? $values['deliv_lack_id'] = util::checkInput($param['deliv_lack_id']):'';
        !empty($param['delivery_order_code'])? $deliveryOrderCode = util::checkInput($param['delivery_order_code']):'';
        //获取数据
        $dataProvider = DeliveryItemLackReportDetail::search($values);
        //输出
        util::export($filename='发货单' . $deliveryOrderCode . '缺货通知明细报表', $columns = DeliveryItemLackReportDetail::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出组合商品列表
    public function combineProductExport($param)
    {
        //校验导出权限
        util::operatePriContr(24.1, 'text');
        $values = array();
        //检索条件
        !empty($param['itemCode'])? $values['CombineProduct']['item_code'] = util::checkInput($param['itemCode']):'';
        !empty($param['customerId'])? $values['CombineProduct']['customer_id'] = util::checkInput($param['customerId']):'';
        !empty($param['warehouseCode'])? $values['CombineProduct']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        //获取数据
        $dataProvider = CombineProduct::search($values);
        //输出
        util::export($filename='组合商品报表', $columns = CombineProduct::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出组合商品明细列表
    public function combineProductDetailExport($param)
    {
        //校验导出权限
        util::operatePriContr(24.1, 'text');
        $values = array();
        //检索条件
        !empty($param['combine_id'])? $values['combine_id'] = util::checkInput($param['combine_id']):'';
        !empty($param['combine_item_code'])? $combineItemCode = util::checkInput($param['combine_item_code']):'';
        //获取数据
        $dataProvider = CombineProductDetail::search($values);
        //输出
        util::export($filename='组合商品' . $combineItemCode . '明细报表', $columns = CombineProductDetail::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出入库单回传明细批次明细列表
    public function inboundRecordBatchDetailExport($param)
    {
        //校验导出权限
        util::operatePriContr(13.1, 'text');
        $values = array();
        //检索条件
        !empty($param['detail_id'])? $values['detail_id'] = util::checkInput($param['detail_id']):'';
        //获取数据
        $dataProvider = InboundRecordBatchDetail::search($values);
        //输出
        util::export($filename='入库单回传明细批次明细报表', $columns = InboundRecordBatchDetail::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出出库单包裹明细报表
    public function outboundRecordPackageExport($param)
    {
        //校验导出权限
        util::operatePriContr(16.1, 'text');
        $values = array();
        //检索条件
        !empty($param['order_id'])? $values['order_id'] = util::checkInput($param['order_id']):'';
        !empty($param['order_no'])? $orderNo = util::checkInput($param['order_no']):'';
        !empty($param['record_id'])? $values['record_id'] = util::checkInput($param['record_id']):'';
        !empty($param['create_time'])? $values['create_time'] = util::checkInput($param['create_time']):'';
        //获取数据
        $dataProvider = OutboundRecordPackage::search($values);
        //输出
        util::export($filename='出库单' . $orderNo . '包裹明细报表', $columns = OutboundRecordPackage::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出出库单回传包裹材料明细报表
    public function outboundRecordMaterialPackageExport($param)
    {
        //校验导出权限
        util::operatePriContr(16.1, 'text');
        $values = array();
        //检索条件
        !empty($param['package_id'])? $values['package_id'] = util::checkInput($param['package_id']):'';
        //获取数据
        $dataProvider = OutboundRecordMaterialPackage::search($values);
        //输出
        util::export($filename='出库单回传包裹材料明细报表', $columns = OutboundRecordMaterialPackage::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出出库单回传包裹商品明细报表
    public function outboundRecordProductPackageExport($param)
    {
        //校验导出权限
        util::operatePriContr(16.1, 'text');
        $values = array();
        //检索条件
        !empty($param['package_id'])? $values['package_id'] = util::checkInput($param['package_id']):'';
        //获取数据
        $dataProvider = OutboundRecordProductPackage::search($values);
        //输出
        util::export($filename='出库单回传包裹商品明细报表', $columns = OutboundRecordProductPackage::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出出库单回传明细批次报表
    public function outboundRecordBatchDetailExport($param)
    {
        //校验导出权限
        util::operatePriContr(16.1, 'text');
        $values = array();
        //检索条件
        !empty($param['detail_id'])? $values['detail_id'] = util::checkInput($param['detail_id']):'';
        //获取数据
        $dataProvider = OutboundRecordBatchDetail::search($values);
        //输出
        util::export($filename='出库单回传明细批次报表', $columns = OutboundRecordBatchDetail::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出库存异动通知报表
    public function stockChangeReportExport($param) 
    {
        //校验导出权限
        util::operatePriContr(36.1, 'text');
        $values = array();
        //检索条件
        !empty($param['orderCode'])? $values['StockChangeReport']['order_code'] = util::checkInput($param['orderCode']):'';
        !empty($param['orderType'])? $values['StockChangeReport']['order_type'] = util::checkInput($param['orderType']):'';
        !empty($param['customerId'])? $values['StockChangeReport']['customer_id'] = util::checkInput($param['customerId']):'';
        !empty($param['warehouseCode'])? $values['StockChangeReport']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        !empty($param['startTime'])? $values['StockChangeReport']['start_time'] = util::checkInput($param['startTime']):'';
        !empty($param['endTime'])? $values['StockChangeReport']['end_time'] = util::checkInput($param['endTime']):'';
        //获取数据
        $dataProvider = StockChangeReport::search($values);
        //输出
        util::export($filename='库存异动通知报表', $columns = StockChangeReport::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出库存异动通知明细批次报表
    public function stockChangeBatchReportExport($param)
    {
        //校验导出权限
        util::operatePriContr(36.1, 'text');
        $values = array();
        //检索条件
        !empty($param['change_id'])? $values['change_id'] = util::checkInput($param['change_id']):'';
        //获取数据
        $dataProvider = StockChangeBatchReport::search($values);
        //输出
        util::export($filename='库存异动通知批次报表', $columns = StockChangeBatchReport::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出库存盘点通知报表
    public function inventoryReportExport($param)
    {
        //校验导出权限
        util::operatePriContr(37.1, 'text');
        $values = array();
        //检索条件
        !empty($param['checkOrderCode'])? $values['InventoryReport']['check_order_code'] = util::checkInput($param['checkOrderCode']):'';
        !empty($param['customerId'])? $values['InventoryReport']['customer_id'] = util::checkInput($param['customerId']):'';
        !empty($param['warehouseCode'])? $values['InventoryReport']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        !empty($param['startTime'])? $values['InventoryReport']['start_time'] = util::checkInput($param['startTime']):'';
        !empty($param['endTime'])? $values['InventoryReport']['end_time'] = util::checkInput($param['endTime']):'';
        //获取数据
        $dataProvider = InventoryReport::search($values);
        //输出
        util::export($filename='库存盘点通知报表', $columns = InventoryReport::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出库存盘点通知明细报表
    public function inventoryProductReportExport($param)
    {
        //校验导出权限
        util::operatePriContr(37.1, 'text');
        $values = array();
        //检索条件
        !empty($param['inventory_id'])? $values['inventory_id'] = util::checkInput($param['inventory_id']):'';
        !empty($param['check_order_code'])? $checkOrderCode = util::checkInput($param['check_order_code']):'';
        //获取数据
        $dataProvider = InventoryProductReport::search($values);
        //输出
        util::export($filename='库存盘点单'. $checkOrderCode . '通知明细报表', $columns = InventoryProductReport::getColumns(), $dataProvider);
        Yii::app()->end();
    }

    //导出库存汇总可用量报表
    public function inventorySumAvailableExport($param)
    {
        //校验导出权限
        util::operatePriContr(43.1, 'text');
        //获取数据
        $dataProvider = InventorySumAvailable::search($param);
        //输出
        util::export($filename='库存汇总可用量明细报表', $columns = InventorySumAvailable::getColumns(), $dataProvider);
        Yii::app()->end();
    }

    
    //导出仓内加工单创建报表
    public function storeProcessCreateExport($param)
    {
        //校验导出权限
        util::operatePriContr(25.1, 'text');
        $values = array();
        //检索条件
        !empty($param['processOrderCode'])? $values['StoreProcessCreate']['process_order_code'] = util::checkInput($param['processOrderCode']):'';
        !empty($param['customerId'])? $values['StoreProcessCreate']['customer_id'] = util::checkInput($param['customerId']):'';
        !empty($param['warehouseCode'])? $values['StoreProcessCreate']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        !empty($param['startTime'])? $values['StoreProcessCreate']['start_time'] = util::checkInput($param['startTime']):'';
        !empty($param['endTime'])? $values['StoreProcessCreate']['end_time'] = util::checkInput($param['endTime']):'';
        //获取数据
        $dataProvider = StoreProcessCreate::search($values);
        //输出
        util::export($filename='仓内加工单创建报表', $columns = StoreProcessCreate::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出仓内加工单材料明细报表
    public function storeProcessMaterialInfoExport($param)
    {
        //校验导出权限
        util::operatePriContr(25.1, 'text');
        $values = array();
        //检索条件
        !empty($param['process_id'])? $values['process_id'] = util::checkInput($param['process_id']):'';
        !empty($param['process_order_code'])? $processOrderCode = util::checkInput($param['process_order_code']):'';
        //获取数据
        $dataProvider = StoreProcessMaterialInfo::search($values);
        //输出
        util::export($filename='仓内加工单'. $processOrderCode . '材料明细报表', $columns = StoreProcessMaterialInfo::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出仓内加工单商品明细报表
    public function storeProcessProductInfoExport($param)
    {
        //校验导出权限
        util::operatePriContr(25.1, 'text');
        $values = array();
        //检索条件
        !empty($param['process_id'])? $values['process_id'] = util::checkInput($param['process_id']):'';
        !empty($param['process_order_code'])? $processOrderCode = util::checkInput($param['process_order_code']):'';
        //获取数据
        $dataProvider = StoreProcessProductInfo::search($values);
        //输出
        util::export($filename='仓内加工单'. $processOrderCode . '商品明细报表', $columns = StoreProcessProductInfo::getColumns(), $dataProvider);
        Yii::app()->end();
    }

    //导出仓内加工单确认报表
    public function storeProcessConfirmExport($param)
    {
        //校验导出权限
        util::operatePriContr(26.1, 'text');
        $values = array();
        //检索条件
        !empty($param['processOrderCode'])? $values['StoreProcessConfirm']['process_order_code'] = util::checkInput($param['processOrderCode']):'';
        !empty($param['startTime'])? $values['StoreProcessConfirm']['start_time'] = util::checkInput($param['startTime']):'';
        !empty($param['endTime'])? $values['StoreProcessConfirm']['end_time'] = util::checkInput($param['endTime']):'';
        //获取数据
        $dataProvider = StoreProcessConfirm::search($values);
        //输出
        util::export($filename='仓内加工单确认报表', $columns = StoreProcessConfirm::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出仓内加工单确认材料明细报表
    public function storeProcessMaterialConfirmExport($param)
    {
        //校验导出权限
        util::operatePriContr(26.1, 'text');
        $values = array();
        //检索条件
        !empty($param['process_id'])? $values['process_id'] = util::checkInput($param['process_id']):'';
        !empty($param['process_order_code'])? $processOrderCode = util::checkInput($param['process_order_code']):'';
        //获取数据
        $dataProvider = StoreProcessMaterialConfirm::search($values);
        //输出
        util::export($filename='仓内加工单确认'. $processOrderCode . '材料明细报表', $columns = StoreProcessMaterialConfirm::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出仓内加工单确认商品明细报表
    public function storeProcessProductConfirmExport($param)
    {
        //校验导出权限
        util::operatePriContr(26.1, 'text');
        $values = array();
        //检索条件
        !empty($param['process_id'])? $values['process_id'] = util::checkInput($param['process_id']):'';
        !empty($param['process_order_code'])? $processOrderCode = util::checkInput($param['process_order_code']):'';
        //获取数据
        $dataProvider = StoreProcessProductConfirm::search($values);
        //输出
        util::export($filename='仓内加工单确认'. $processOrderCode . '商品明细报表', $columns = StoreProcessProductConfirm::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出仓内加工单取消报表
    public function storeProcessCancelExport($param) 
    {
        //校验导出权限
        util::operatePriContr(27.1, 'text');
        $values = array();
        //检索条件
        !empty($param['orderNo'])? $values['StoreProcessCancel']['order_no'] = util::checkInput($param['orderNo']):'';
        !empty($param['customerId'])? $values['StoreProcessCancel']['customer_id'] = util::checkInput($param['customerId']):'';
        !empty($param['warehouseCode'])? $values['StoreProcessCancel']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        !empty($param['startTime'])? $values['StoreProcessCancel']['start_time'] = util::checkInput($param['startTime']):'';
        !empty($param['endTime'])? $values['StoreProcessCancel']['end_time'] = util::checkInput($param['endTime']):'';
        //获取数据
        $dataProvider = StoreProcessCancel::search($values);
        //输出
        util::export($filename='仓内加工单取消报表', $columns = StoreProcessCancel::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出订单流水通知报表
    public function orderProcessReportExport($param)
    {
        //校验导出权限
        util::operatePriContr(35.1, 'text');
        $values = array();
        //检索条件
        !empty($param['orderCode'])? $values['OrderProcessReport']['order_code'] = util::checkInput($param['orderCode']):'';
        !empty($param['orderType'])? $values['OrderProcessReport']['order_type'] = util::checkInput($param['orderType']):'';
        !empty($param['customerId'])? $values['OrderProcessReport']['customer_id'] = util::checkInput($param['customerId']):'';
        !empty($param['warehouseCode'])? $values['OrderProcessReport']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        !empty($param['processStatus'])? $values['OrderProcessReport']['process_status'] = util::checkInput($param['processStatus']):'';
        !empty($param['startTime'])? $values['OrderProcessReport']['start_time'] = util::checkInput($param['startTime']):'';
        !empty($param['endTime'])? $values['OrderProcessReport']['end_time'] = util::checkInput($param['endTime']):'';
        //获取数据
        $dataProvider = OrderProcessReport::search($values);
        //输出
        util::export($filename='订单流水通知报表', $columns = OrderProcessReport::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
    //导出缺货订单报表
    public function deliveryOrderShortageExport($param){
        //校验导出权限
        util::operatePriContr(35.1, 'text');
        $values = array();
        //检索条件
        !empty($param['deliveryOrderCode'])? $values['DeliveryOrderShortage']['delivery_order_code'] = util::checkInput($param['deliveryOrderCode']):'';
        !empty($param['customerId'])? $values['DeliveryOrderShortage']['customer_id'] = util::checkInput($param['customerId']):'';
        !empty($param['warehouseCode'])? $values['DeliveryOrderShortage']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        !empty($param['erpCreateStartTime'])? $values['DeliveryOrderShortage']['erp_create_start_time'] = util::checkInput($param['erpCreateStartTime']):'';
        !empty($param['erpCreateEndTime'])? $values['DeliveryOrderShortage']['erp_create_end_time'] = util::checkInput($param['erpCreateEndTime']):'';
        !empty($param['midWarehouseCode'])? $values['DeliveryOrderShortage']['mid_warehouse_code'] = util::checkInput($param['midWarehouseCode']):'';
        !empty($param['createStartTime'])? $values['DeliveryOrderShortage']['create_start_time'] = util::checkInput($param['createStartTime']):'';
        !empty($param['createEndTime'])? $values['DeliveryOrderShortage']['create_end_time'] = util::checkInput($param['createEndTime']):'';
        
        //获取数据
        $dataProvider = DeliveryOrderShortage::search($values);
        //输出
        util::export($filename='缺货单通知报表', $columns = DeliveryOrderShortage::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    //导出唯品会JIT拣货单列表
    public function vipJitPickListExport($param){
        //校验导出权限
        util::operatePriContr(83.1, 'text');
        $values = array();
        //检索条件
        !empty($param['poNo'])? $values['VipJitPickList']['po_no'] = util::checkInput($param['poNo']):'';
        !empty($param['pickNo'])? $values['VipJitPickList']['pick_no'] = util::checkInput($param['pickNo']):'';
        !empty($param['warehouseCode'])? $values['VipJitPickList']['warehouse_code'] = util::checkInput($param['warehouseCode']):'';
        !empty($param['deliveryNo'])? $values['VipJitPickList']['delivery_no'] = util::checkInput($param['deliveryNo']):'';
        !empty($param['storageNo'])? $values['VipJitPickList']['storage_no'] = util::checkInput($param['storageNo']):'';
        !empty($param['status'])? $values['VipJitPickList']['status'] = util::checkInput($param['status']):'';
        !empty($param['vendorId'])? $values['VipJitPickList']['vendor_id'] = util::checkInput($param['vendorId']):'';
        !empty($param['brandName'])? $values['VipJitPickList']['brand_name'] = util::checkInput($param['brandName']):'';
        !empty($param['sellSite'])? $values['VipJitPickList']['sell_site'] = util::checkInput($param['sellSite']):'';
        $values['VipJitPickList']['operate_type'] = 'excel_export';
        //获取数据
        $dataProvider = VipJitPickList::search($values);
        //输出
        util::export($filename='唯品会JIT拣货单列表', $columns = VipJitPickList::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    //导出唯品会JIT采购单列表
    public function vipJitPoListExport($param){
        //校验导出权限
        util::operatePriContr(84.1, 'text');
        $values = array();
        //检索条件
        !empty($param['poNo'])? $values['VipJitPoList']['po_no'] = util::checkInput($param['poNo']):'';
        !empty($param['vendorName'])? $values['VipJitPoList']['vendor_name'] = util::checkInput($param['vendorName']):'';
        !empty($param['brandName'])? $values['VipJitPoList']['brand_name'] = util::checkInput($param['brandName']):'';
        !empty($param['scheduleName'])? $values['VipJitPoList']['schedule_name'] = util::checkInput($param['scheduleName']):'';
        !empty($param['sellSite'])? $values['VipJitPoList']['sell_site'] = util::checkInput($param['sellSite']):'';
        $values['VipJitPoList']['operate_type'] = 'excel_export';
        //获取数据
        $dataProvider = VipJitPoList::search($values);
        //输出
        util::export($filename='唯品会JIT采购单列表', $columns = VipJitPoList::getColumns(), $dataProvider);
        Yii::app()->end();
    }

    //导出唯品会JIT采购单列表
    public function vipJitPickDetailExport($param){
        //校验导出权限
        util::operatePriContr(83.1, 'text');
        $values = array();
        //检索条件
        !empty($param['pickNo'])? $values['pickNo'] = util::checkInput($param['pickNo']):'';
        $values['operate_type'] = 'excel_export';
        //获取数据
        $dataProvider = VipJitPickDetail::search($values);
        //输出
        util::export($filename='唯品会JIT拣货单明细', $columns = VipJitPickDetail::getColumns(), $dataProvider);
        Yii::app()->end();
    }
    
}//end class