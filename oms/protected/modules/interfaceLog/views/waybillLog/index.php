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
	<form id="WaybillLog_formid">
		<label>货主ID：</label>
		<input type="text" name="WaybillLog[customer_id]" class="easyui-textbox" style="width: 150px;" id="customerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<label>交易订单号：</label>
		<input type="text" name="WaybillLog[order_list]" class="easyui-textbox" style="width: 220px;" id="orderList" />&nbsp;&nbsp;&nbsp;&nbsp;
		<label>应用编码：</label>
		<input type="text" name="WaybillLog[app_code]" class="easyui-textbox" style="width: 150px;" id="appCode" /><p></p>	
		<label>接口类型：</label>
		<select name="WaybillLog[method]" id="method" class="easyui-combobox" editable="false" style="width: 200px;">
		    <option value=''>全部</option>
			<option value='taobao.wlb.waybill.i.seller.authorization'>商家授权信息同步接口</option>
			<option value='taobao.wlb.waybill.i.search'>查看商家面单订购服务接口</option>
			<option value='taobao.wlb.waybill.i.get'>获取物流服务商电子面单号接口</option>
			<option value='taobao.wlb.waybill.i.fullupdate'>面单信息更新接口</option>
			<option value='taobao.wlb.waybill.i.cancel'>商家取消获取电子面单号接口</option>
			<option value='taobao.wlb.waybill.i.print'>打印确认接口</option>
			<option value='cainiao.waybill.ii.get'>电子面单云打印接口</option>
			<option value='cainiao.waybill.ii.cancel'>查询面单服务订购及面单使用情况</option>
			<option value='cainiao.waybill.ii.search'>打印确认接口</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<label>推送状态：</label>
		<select name="WaybillLog[return_status]" id="returnStatus" class="easyui-combobox" editable="false" style="width: 80px;">
		    <option value=''>全部</option>
			<option value='1'>成功</option>
			<option value='0'>失败</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<label>开始时间：</label>
		<input name="WaybillLog[start_time]" class="easyui-datetimebox" style="width: 180px" id="startTime" value="<?php echo date('Y-m-d', time()).' 00:00:00';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<label>结束时间：</label>
		<input name="WaybillLog[end_time]" class="easyui-datetimebox" style="width: 180px" id="endTime" value="<?php echo date('Y-m-d', time()).' 23:59:59';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript"
	src="./static/js/moudles/interfaceLog/interfaceLog-waybillLog.js"></script>