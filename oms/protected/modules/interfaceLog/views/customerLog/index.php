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
	<form id="CustomerLog_formid">
		<lable>客商档案ID：</lable>
		<input type="text" name="CustomerLog[customer_id]" class="easyui-textbox" style="width:150px;" id="customerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>客商档案类型：</lable>		
		<select name="CustomerLog[customer_type]" id="customerType" class="easyui-combobox" editable="false" style="width: 150px;">
			<option value=''>全部</option>
			<option value='OW'>货主</option>
			<option value='WH'>仓库</option>
			<option value='VE'>供应商</option>
			<option value='OT'>店铺</option>
		</select>   <p></p>
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
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'" onClick="searchForm()"
			style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript"
	src="./static/js/moudles/interfaceLog/interfaceLog-customerLog.js"></script>