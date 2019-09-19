<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/13');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(13.1);
?>
<script type="text/javascript">
	var deatilViewUrl = '<?php echo Yii::app()->createUrl('inbound/inboundRecodeDetail/index', array('id'=>'uid','name'=>'uname','rid'=>'reid','ctime'=>'createTime')); ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div region="north" border="true" split="true" style="background: #B3DFDA; padding: 10px">
	<form id="CstmSdLog_formid">
		<lable>OMS订单号：</lable>
		<input type="text" name="ConfirmASNData[oms_order_no]" class="easyui-textbox" style="width: 12%;" id="omsOrderNo" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>WMS订单号：</lable>
		<input type="text" name="ConfirmASNData[wms_order_no]" class="easyui-textbox" style="width: 12%;" id="wmsOrderNo" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>订单类型：</lable>
		<select name="ConfirmASNData[order_type]" id="orderType" class="easyui-combobox" editable="false" style="width: 16%;">
			<option value=''>全部</option>
			<option value='PO'>采购入库</option>
			<option value='TR'>调拨入库</option>
			<option value='RS'>销售退货入库</option>
			<option value='IP'>盘盈入库</option>
			<option value='SCRK'>奇门生产入库</option>
			<option value='LYRK'>奇门领用入库</option>
			<option value='CCRK'>奇门残次品入库</option>
			<option value='CGRK'>奇门采购入库</option>
			<option value='DBRK'>奇门调拨入库</option>
			<option value='QTRK'>奇门其他入库</option>
			<option value='B2BRK'>奇门B2B入库</option>
			<option value='THRK'>奇门退货入库</option>
			<option value='HHRK'>奇门换货入库</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>货主ID：</lable>
		<input type="text" name="ConfirmASNData[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" /><p></p>
		<lable>仓库编码：</lable>
		<input type="text" name="ConfirmASNData[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouseCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>开始时间：</lable>
	    <input class="easyui-datetimebox" name="ConfirmASNData[start_time]" style="width:200px" id="startTime" value="<?php echo date('Y-m-d', time()).' 00:00:00';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>结束时间：</lable>
	    <input class="easyui-datetimebox" name="ConfirmASNData[end_time]" style="width:200px" id="endTime" value="<?php echo date('Y-m-d', time()).' 23:59:59';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/inbound/inbound-confirmASNData.js"></script>