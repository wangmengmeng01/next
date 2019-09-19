<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/35');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(35.1);
?>
<script type="text/javascript">
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
	var allocateUrl = '<?php echo $this->createUrl('deliveryOrderShortage/allocateWarehouse');?>';
	var confirmWarehouseUrl = '<?php echo $this->createUrl('deliveryOrderShortage/confirmWarehouse');?>';
	var sendOrderInfoUrl = '<?php echo $this->createUrl('deliveryOrderShortage/sendOrderInfo');?>';
</script>
<style type="text/css">
   #confirm_click{
   	text-align:center;
   }
   #dConfirm{
	margin:0 auto;
   }
</style>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="ShortageForm">
	    <lable>发货单号：</lable>
	    <input type="text" name="DeliveryOrderShortage[delivery_order_code]" class="easyui-textbox" style="width:12%" id="deliveryOrderCode"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>原仓库：</lable>
		<input type="text" name="DeliveryOrderShortage[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouseCode" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>订单创建时间：</lable>
	    <input class="easyui-datetimebox" name="DeliveryOrderShortage[erp_create_start_time]" style="width:200px" id="erpCreateStartTime" value="">&nbsp;--
	    <input class="easyui-datetimebox" name="DeliveryOrderShortage[erp_create_end_time]" style="width:200px" id="erpCreateEndTime" value=""><p></p>
		<lable>货主编码：</lable>        
		<input type="text" name="DeliveryOrderShortage[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<label>推荐仓库：</label>
		<input type="text" name="DeliveryOrderShortage[mid_warehouse_code]" class="easyui-textbox" style="width: 12%;" id="midWarehouseCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>发货单创建时间：</lable>   
	    <input class="easyui-datetimebox" name="DeliveryOrderShortage[create_start_time]" style="width:200px" id="createStartTime" value="<?php echo date('Y-m-d', time()).' 00:00:00';?>">&nbsp;--
	    <input class="easyui-datetimebox" name="DeliveryOrderShortage[create_end_time]" style="width:200px" id="createEndTime" value="<?php echo date('Y-m-d', time()).' 23:59:59';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg">
    <table id="grid"></table>
</div>
<div id='confirm_click'>
    <div id='dConfirm' onclick="sendOrderData();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">确认</div>
    <div id='dCancel' onclick="cancelSelectData();" class="easyui-linkbutton" data-options="iconCls:'icon-save'">取消</div>
</div>
<div id="exportDg"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/delivery-deliveryOrderShortage.js"></script>