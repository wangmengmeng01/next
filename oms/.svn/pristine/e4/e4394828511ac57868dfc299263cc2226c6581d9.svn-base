<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/31');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(31.1);
?>
<script type="text/javascript">
	var deatilViewUrl = '<?php echo Yii::app()->createUrl('outbound/deliverySnReportDetail/index', array('id'=>'uid','name'=>'uname')); ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="CstmSdLog_formid">
	    <lable>出库单号</lable>
	    <input type="text" name="DeliverySnReport[delivery_order_code]" class="easyui-textbox" style="width:12%" id="deliveryOrderCode"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>订单类型：</lable>
		<select name="DeliverySnReport[order_type]" id="orderType" class="easyui-combobox" editable="false" style="width: 16%;">
			<option value=''>全部</option>
			<option value='JYCK'>一般交易出库单</option>
			<option value='HHCK'>换货出库单</option>
			<option value='BFCK'>补发出库单</option>
			<option value='PTCK'>普通出库单（退仓）</option>
			<option value='DBCK'>调拨出库</option>
			<option value='QTCK'>其他出库单</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>货主ID：</lable>
		<input type="text" name="DeliverySnReport[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" /><p></p>
		<lable>仓库编码：</lable>
		<input type="text" name="DeliverySnReport[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouseCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>开始时间：</lable>
	    <input class="easyui-datetimebox" name="DeliverySnReport[start_time]" style="width:200px" id="startTime" value="<?php echo date('Y-m-d', time()).' 00:00:00';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>结束时间：</lable>
	    <input class="easyui-datetimebox" name="DeliverySnReport[end_time]" style="width:200px" id="endTime" value="<?php echo date('Y-m-d', time()).' 23:59:59';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="exportDg"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/delivery-deliverySnReport.js"></script>