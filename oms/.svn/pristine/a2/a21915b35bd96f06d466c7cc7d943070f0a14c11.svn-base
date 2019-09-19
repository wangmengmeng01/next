<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/28');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(28.1);
?>
<script type="text/javascript">
	var deatilViewUrl = '<?php echo Yii::app()->createUrl('outbound/deliveryOrderDetail/index', array('id'=>'uid','name'=>'uname')); ?>';
	var invoiceViewUrl = '<?php echo Yii::app()->createUrl('outbound/deliveryOrderInvoice/index', array('id'=>'uid','name'=>'uname')); ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="CstmSdLog_formid">
	    <lable>出库单号</lable>
	    <input type="text" name="DeliveryOrderCreate[delivery_order_code]" class="easyui-textbox" style="width:12%" id="deliveryOrderCode"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>订单类型：</lable>
		<select name="DeliveryOrderCreate[order_type]" id="orderType" class="easyui-combobox" editable="false" style="width: 16%;">
			<option value=''>全部</option>
			<option value='JYCK'>一般交易出库单</option>
			<option value='HHCK'>换货出库单</option>
			<option value='BFCK'>补发出库单</option>
			<option value='QTCK'>其他出库单</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>货主ID：</lable>
		<input type="text" name="DeliveryOrderCreate[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" /><p></p>
		<lable>仓库编码：</lable>
		<input type="text" name="DeliveryOrderCreate[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouseCode" />&nbsp;&nbsp;&nbsp;&nbsp;
        <lable>分销订单标志：</lable>
        <select name="DeliveryOrderCreate[fx_flag]" id="fxFlag" class="easyui-combobox" editable="false" style="width: 6%;">
            <option value=''>全部</option>
            <option value='1'>是</option>
            <option value='0'>否</option>
        </select>&nbsp;&nbsp;&nbsp;&nbsp;
        <lable>开始时间：</lable>
	    <input class="easyui-datetimebox" name="DeliveryOrderCreate[start_time]" style="width:200px" id="startTime" value="<?php echo date('Y-m-d', time()).' 00:00:00';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>结束时间：</lable>
	    <input class="easyui-datetimebox" name="DeliveryOrderCreate[end_time]" style="width:200px" id="endTime" value="<?php echo date('Y-m-d', time()).' 23:59:59';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="exportDg"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/delivery-deliveryOrderCreate.js"></script>