<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/35');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(35.1);
?>
<script type="text/javascript">
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="CstmSdLog_formid">
	    <lable>单据号</lable>
	    <input type="text" name="OrderProcessReport[order_code]" class="easyui-textbox" style="width:12%" id="orderCode"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>订单类型：</lable>
		<select name="OrderProcessReport[order_type]" id="orderType" class="easyui-combobox" editable="false" style="width: 12%;">
			<option value=''>全部</option>
			<option value='JYCK'>一般交易出库单</option>
			<option value='HHCK'>换货出库单</option>
			<option value='BFCK'>补发出库单</option>
			<option value='PTCK'>普通出库单</option>
			<option value='DBCK'>调拨出库单</option>
			<option value='B2BRK'>B2B入库单</option>
			<option value='B2BCK'>B2B出库单</option>
			<option value='QTCK'>其他出库单</option>
			<option value='SCRK'>生产入库单</option>
			<option value='LYRK'>领用入库单</option>
			<option value='CCRK'>残次品入库单</option>
			<option value='CGRK'>采购入库单</option>
			<option value='DBRK'> 调拨入库单</option>
			<option value='QTRK'>其他入库单</option>
			<option value='XTRK'>销退入库单</option>
			<option value='HHRK'>换货入库单</option>
			<option value='CNJG'>仓内加工单</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>货主ID：</lable>
		<input type="text" name="OrderProcessReport[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>仓库编码：</lable>
		<input type="text" name="OrderProcessReport[warehouse_code]" class="easyui-textbox" style="width: 12%;" id="warehouseCode" /><p></p>
		<lable>单据状态：</lable>
		<select name="OrderProcessReport[process_status]" id="processStatus" class="easyui-combobox" editable="false" style="width: 12%;">
			<option value=''>全部</option>
			<option value='ACCEPT'>仓库接单</option>
			<option value='PARTFULFILLED'>部分收货完成</option>
			<option value='FULFILLED'>收货完成</option>
			<option value='PRINT'>打印</option>
			<option value='PICK'>拣货</option>
			<option value='CHECK'>复核</option>
			<option value='PACKAGE'>打包</option>
			<option value='WEIGH'>称重</option>
			<option value='READY'>待提货</option>
			<option value='DELIVERED'>已发货</option>
			<option value='REFUSE'>买家拒签</option>
			<option value='EXCEPTION'>异常</option>
			<option value='CLOSED'>关闭</option>
			<option value='CANCELED'>取消</option>
			<option value='REJECT'>仓库拒单</option>
			<option value='SIGN'>签收</option>
			<option value='TMSCANCELED'>快递拦截</option>
			<option value='OTHER'>其他</option>
			<option value='PARTDELIVERED'>部分发货完成</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>开始时间：</lable>
	    <input class="easyui-datetimebox" name="OrderProcessReport[start_time]" style="width:200px" id="startTime" value="<?php echo date('Y-m-d', time()).' 00:00:00';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>结束时间：</lable>
	    <input class="easyui-datetimebox" name="OrderProcessReport[end_time]" style="width:200px" id="endTime" value="<?php echo date('Y-m-d', time()).' 23:59:59';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="exportDg"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/outbound-orderProcessReport.js"></script>