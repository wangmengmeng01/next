<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/17');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(17.1);
?>
<script type="text/javascript">
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div region="north" border="true" split="true" style="background: #B3DFDA; padding: 10px">
	<form id="ConfirmSOStatus_formid">
		<lable>订单号：</lable>
		<input type="text" name="ConfirmSOStatus[order_no]" class="easyui-textbox" style="width: 12%;" id="orderNo" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>订单类型：</lable>
		<select name="ConfirmSOStatus[order_type]" id="orderType" class="easyui-combobox" editable="false" style="width: 16%;">
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
		<input type="text" name="ConfirmSOStatus[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" /><p></p>
		<lable>开始时间：</lable>
	    <input type="text" name="ConfirmSOStatus[start_time]" class="easyui-datetimebox" style="width:200px" id="startTime" value="<?php echo date('Y-m-d', time()).' 00:00:00';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>结束时间：</lable>
	    <input type="text" name="ConfirmSOStatus[end_time]" class="easyui-datetimebox" style="width:200px" id="endTime" value="<?php echo date('Y-m-d', time()).' 23:59:59';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/outbound-confirmSOStatus.js"></script>