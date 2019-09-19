<?php
//获取登陆者角色
$roleArr = util::getUserRoleArr();
//校验登陆者是否为管理员，只有管理员有查看的权限
if (!in_array(AUTH_SYSTEM_MANAGER, $roleArr)) {
	die('<h3>无此权限，只有系统管理员才可以查看日志！</h3>');
}
?>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="TimeConsumingLog_formid">
		<lable>订单号：</lable>
		<input type="text" name="TimeConsumingLog[order_no]" class="easyui-textbox" style="width: 200px;" id="orderNo" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>订单类型：</lable>		
		<select name="OrderLog[order_type]" id="orderType" class="easyui-combobox" editable="false" style="width: 150px;">
		    <option value=''>全部</option>
			<option value='PO'>采购入库</option>
			<option value='TR'>调拨入库</option>
			<option value='RS'>销售退货入库</option>
			<option value='IP'>盘盈入库</option>
			<option value='SO'>销售出库</option>
			<option value='TO'>调拨出库</option> 
			<option value='RP'>采购退货出库</option>
			<option value='IL'>盘亏出库</option>
			<option value='OO'>线下出库</option>
			<option value='B2B'>批量销售出库</option>
			<option value='SCRK'>奇门生产入库</option>
			<option value='LYRK'>奇门领用入库</option>
			<option value='CCRK'>奇门残次品入库</option>
			<option value='CGRK'>奇门采购入库</option>
			<option value='DBRK'>奇门调拨入库</option>
			<option value='QTRK'>奇门其他入库</option>
			<option value='B2BRK'>奇门B2B入库</option>
			<option value='THRK'>奇门退货入库</option>
			<option value='HHRK'>奇门换货入库</option>
			<option value='PTCK'>奇门普通出库单（退仓）</option>
			<option value='DBCK'>奇门调拨出库</option>
			<option value='B2BCK'>奇门B2B出库</option>
			<option value='QTCK'>奇门其他出库</option>
			<option value='JYCK'>奇门一般交易出库</option>
			<option value='HHCK'>奇门换货出库</option>
			<option value='BFCK'>奇门补发出库</option>
			<option value='CNJG'>奇门仓内加工</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>货主ID：</lable>
		<input type="text" name="OrderLog[customer_id]" class="easyui-textbox" style="width: 150px;" id="customerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>仓库编码：</lable>
		<input type="text" name="OrderLog[warehouse_code]" class="easyui-textbox" style="width: 150px;" id="warehouseCode" /><p></p>	
		<lable>接口类型：</lable>
		<select name="OrderLog[method]" id="method" class="easyui-combobox" editable="false" style="width: 150px;">
		    <option value=''>全部</option>
			<option value='putASNData'>入库单下发</option>
			<option value='cancelASNData'>入库单取消</option>
			<option value='confirmASNData'>入库单状态明细回传</option>
			<option value='putSOData'>出库单下发</option>
			<option value='cancelSOData'>出库单取消</option>
			<option value='confirmSOStatus'>出库单状态回传</option>
			<option value='confirmSOData'>出库单明细回传</option>
			<option value='inventoryReport'>库存盘点通知</option>
			<option value='entryorder.create'>奇门入库单创建</option>
			<option value='taobao.qimen.entryorder.confirm'>奇门入库单确认</option>
			<option value='returnorder.create'>奇门退货入库单创建</option>
			<option value='taobao.qimen.returnorder.confirm'>奇门退货入库单确认</option>
			<option value='stockout.create'>奇门出库单创建</option>
			<option value='taobao.qimen.stockout.confirm'>奇门出库单确认</option>
			<option value='deliveryorder.create'>奇门发货单创建</option>
			<option value='deliveryorder.batchcreate'>发货单创建接口 （批量）</option>
			<option value='taobao.qimen.deliveryorder.confirm'>奇门发货单确认</option>
			<option value='taobao.qimen.deliveryorder.batchconfirm'>发货单确认接口  (批量)</option>
			<option value='taobao.qimen.sn.report'>奇门发货单SN通知接口</option>
			<option value='taobao.qimen.orderprocess.report'>奇门订单流水通知接口</option>
			<option value='taobao.qimen.itemlack.report'>奇门发货单缺货通知接口</option>
			<option value='order.cancel'>奇门单据取消</option>
			<option value='taobao.qimen.inventory.report'>奇门库存盘点通知</option>
			<option value='storeprocess.create'>奇门仓内加工单创建</option>
			<option value='taobao.qimen.storeprocess.confirm'>奇门仓内加工单确认</option>
			<option value='taobao.qimen.stockchange.report'>奇门库存异动通知</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>推送状态：</lable>
		<select name="OrderLog[return_status]" id="returnStatus" class="easyui-combobox" editable="false" style="width: 100px;">
		    <option value=''>全部</option>
			<option value='1'>成功</option>
			<option value='0'>失败</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>开始时间：</lable>
		<input name="OrderLog[start_time]" class="easyui-datetimebox" style="width: 180px" id="startTime" value="<?php echo date('Y-m-d', time()).' 00:00:00';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>结束时间：</lable>
		<input name="OrderLog[end_time]" class="easyui-datetimebox" style="width: 180px" id="endTime" value="<?php echo date('Y-m-d', time()).' 23:59:59';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript"
	src="./static/js/moudles/interfaceLog/interfaceLog-orderLog.js"></script>