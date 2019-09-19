<?php
//获取登陆者角色
$roleArr = util::getUserRoleArr();

//校验登陆者是否为管理员，只有管理员有查看的权限
/*if (!in_array(AUTH_SYSTEM_MANAGER, $roleArr)) {
	die('<h3>无此权限，只有系统管理员才可以查看日志！</h3>');
}*/

//获取登陆者是否有操作权限
$operateFlag= util::isHasPri(21.1);
?>
<script type="text/javascript">
	var operateFlag = <?php echo $operateFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="OrderLog_formid">
		<lable>订单号：</lable>
		<input type="text" name="OrderLog[order_no]" class="easyui-textbox" style="width: 200px;" id="orderNo" />&nbsp;&nbsp;&nbsp;&nbsp;
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
			<option value='JQRK'>奇门拒签入库</option>
			<option value='HHRK'>奇门换货入库</option>
			<option value='PTCK'>奇门普通出库单（退仓）</option>
			<option value='DBCK'>奇门调拨出库</option>
			<option value='B2BCK'>奇门B2B出库</option>
			<option value='QTCK'>奇门其他出库</option>
			<option value='JYCK'>奇门一般交易出库</option>
			<option value='HHCK'>奇门换货出库</option>
			<option value='BFCK'>奇门补发出库</option>
			<option value='CNJG'>奇门仓内加工</option>
            <option value='ASN'>贝贝采购入库</option>
            <option value='STI'>贝贝调拨入库</option>
            <option value='RBR'>贝贝领用还回</option>
            <option value='STO'>贝贝调拨出库</option>
            <option value='RTS'>贝贝采购退货</option>
            <option value='RB'>贝贝领用出库</option>
            <option value='ESO'>贝贝线下销售</option>
            <option value='PY'>贝贝盘盈</option>
            <option value='PK'>贝贝盘亏</option>
            <option value='FROF'>贝贝冻结</option>
            <option value='FROG'>贝贝解冻</option>
            <option value='DEFD'>贝贝正转残</option>
            <option value='DEFG'>贝贝残转正</option>
            <option value='RMA'>贝贝销售退货</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>货主ID：</lable>
		<input type="text" name="OrderLog[customer_id]" class="easyui-textbox" style="width: 150px;" id="customerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>仓库编码：</lable>
		<input type="text" name="OrderLog[warehouse_code]" class="easyui-textbox" style="width: 150px;" id="warehouseCode" /><p></p>	
		<lable>接口类型：</lable>
		<select name="OrderLog[method]" id="method" class="easyui-combobox" editable="false" style="width: 200px;" data-options="panelHeight:250">
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
			<option value='deliveryorder.batchcreate'>奇门发货单创建接口 （批量）</option>
			<option value='taobao.qimen.deliveryorder.confirm'>奇门发货单确认</option>
			<option value='taobao.qimen.deliveryorder.batchconfirm'>奇门发货单确认接口  (批量)</option>
			<option value='taobao.qimen.sn.report'>奇门发货单SN通知接口</option>
			<option value='taobao.qimen.orderprocess.report'>奇门订单流水通知接口</option>
			<option value='taobao.qimen.itemlack.report'>奇门发货单缺货通知接口</option>
			<option value='order.cancel'>奇门单据取消</option>
			<option value='taobao.qimen.inventory.report'>奇门库存盘点通知</option>
			<option value='storeprocess.create'>奇门仓内加工单创建</option>
			<option value='taobao.qimen.storeprocess.confirm'>奇门仓内加工单确认</option>
			<option value='taobao.qimen.stockchange.report'>奇门库存异动通知</option>
            <option value='10'>网易考拉采购单推送接口</option>
            <option value='100'>网易考拉出库单推送接口</option>
            <option value='101'>网易考拉出库单取消接口</option>
            <option value='102'>网易考拉出库单回传接口</option>
            <option value='103'>网易考拉出库单出库确认接口</option>
            <option value='104'>网易考拉入库单推送接口</option>
            <option value='105'>网易考拉入库单回传接口</option>
            <option value='106'>网易考拉入库单入库确认接口</option>
            <option value='107'>网易考拉仓库回调推送理货报告状态信息接口</option>
            <option value='108'>网易考拉仓库回调推送理货报告详情信息接口</option>
            <option value='109'>网易考拉推送理货单审核状态给仓库</option>
            <option value='11'>网易考拉取消采购单</option>
            <option value='119'>网易考拉商品资料下发推送接口</option>
            <option value='150'>网易考拉SKU效期信息回调接口</option>
            <option value='20'>网易考拉订单推送接口</option>
            <option value='21'>网易考拉取消用户订单</option>
            <option value='30'>网易考拉采购单入库回调接口</option>
            <option value='31'>网易考拉用户订单出库回调</option>
            <option value='50'>网易考拉盘点情况回调接口</option>
            <option value='60'>网易考拉库存查询接口</option>
            <option value='kaola_getBillNo'>网易考拉子母件获取运单号接口</option>
            <option value='115'>网易考拉库存调整回调接口</option>
            <option value='beibei.outer.entryorder.create'>贝贝入库单创建接口</option>
            <option value='beibei.outer.entryorder.confirm'>贝贝入库单回传接口</option>
            <option value='beibei.outer.stockout.create'>贝贝出库单创建接口</option>
            <option value='beibei.outer.stockout.confirm'>贝贝出库单回传接口</option>
            <option value='beibei.outer.deliveryorder.create'>贝贝发货单创建接口</option>
            <option value='beibei.outer.bill.cancel'>贝贝单据取消接口</option>
            <option value='beibei.outer.stockchange.report'>贝贝库存异动接口</option>
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