<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/16');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(16.1);
?>
<script type="text/javascript">
	var deatilViewUrl = '<?php echo Yii::app()->createUrl('outbound/outboundRecodeDetail/index', array('id'=>'uid','name'=>'uname','rid'=>'reid','ctime'=>'createTime')); ?>';
	var packageViewUrl = '<?php echo Yii::app()->createUrl('outbound/outboundRecordPackage/index', array('id'=>'uid','name'=>'uname','rid'=>'reid','ctime'=>'createTime')); ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div region="north" border="true" split="true" style="background: #B3DFDA; padding: 10px">
	<form id="CstmSdLog_formid">
		<lable>OMS订单号：</lable>
		<input type="text" name="ConfirmSOData[oms_order_no]" class="easyui-textbox" style="width: 12%;" id="omsOrderNo" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>WMS订单号：</lable>
		<input type="text" name="ConfirmSOData[wms_order_no]" class="easyui-textbox" style="width: 12%;" id="wmsOrderNo" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>订单类型：</lable>
		<select name="ConfirmSOData[order_type]" id="orderType" class="easyui-combobox" editable="false" style="width: 16%;">
			<option value=''>全部</option>
			<option value='SO'>销售出库</option>
			<option value='TO'>调拨出库</option>
			<option value='RP'>采购退货出库</option>
			<option value='IL'>盘亏出库</option>
			<option value='OO'>线下出库</option>
			<option value='PTCK'>奇门普通出库单（退仓）</option>
			<option value='DBCK'>奇门调拨出库</option>
			<option value='B2BCK'>奇门B2B出库</option>
			<option value='QTCK'>奇门其他出库</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>货主ID：</lable>
		<input type="text" name="ConfirmSOData[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" /><p></p>
		<lable>仓库编码：</lable>
		<input type="text" name="ConfirmSOData[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouseCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>开始时间：</lable>
	    <input class="easyui-datetimebox" name="ConfirmSOData[start_time]" style="width:200px" id="startTime" value="<?php echo date('Y-m-d', time()).' 00:00:00';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>结束时间：</lable>
	    <input class="easyui-datetimebox" name="ConfirmSOData[end_time]" style="width:200px" id="endTime" value="<?php echo date('Y-m-d', time()).' 23:59:59';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/outbound-confirmSOData.js"></script>