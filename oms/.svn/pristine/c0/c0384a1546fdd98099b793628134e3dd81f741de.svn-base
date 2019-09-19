<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/15');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(15.1);
//获取登陆者是否有excel导入权限
$excelImportFlag = util::isHasPri(15.2);
?>
<script type="text/javascript">
	var deatilViewUrl = '<?php echo Yii::app()->createUrl('outbound/outboundDetail/index', array('id'=>'uid','name'=>'uname')); ?>';
	var importUrl = '<?php echo Yii::app()->createUrl('base/import/index')?>'
	var importLoadUrl = '<?php echo Yii::app()->createUrl('base/import/index')?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
	var excelImportFlag = <?php echo $excelImportFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="CstmSdLog_formid">
		<lable>订单号：</lable>
		<input type="text" name="PutSOData[order_no]" class="easyui-textbox" style="width: 12%;" id="orderNo" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>订单类型：</lable>
		<select id="orderType" name="PutSOData[order_type]" editable="false" class="easyui-combobox" style="width: 16%;">
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
		<input type="text" name="PutSOData[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" /><p></p>
		<lable>仓库编码：</lable>
		<input type="text" name="PutSOData[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouseCode" />&nbsp;&nbsp;&nbsp;&nbsp;
        <lable>分销订单标志：</lable>
        <select name="PutSOData[fx_flag]" id="fxFlag" class="easyui-combobox" editable="false" style="width: 6%;">
            <option value=''>全部</option>
            <option value='1'>是</option>
            <option value='0'>否</option>
        </select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>开始时间：</lable>
		<input name="PutSOData[start_time]" class="easyui-datetimebox" style="width: 200px" id="startTime" value="<?php echo date('Y-m-d', time()).' 00:00:00';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>结束时间：</lable>
		<input name="PutSOData[end_time]" class="easyui-datetimebox" style="width: 200px" id="endTime" value="<?php echo date('Y-m-d', time()).' 23:59:59';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'" onClick="searchForm()"
			style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="exportDg"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/outbound-putSOData.js"></script>
