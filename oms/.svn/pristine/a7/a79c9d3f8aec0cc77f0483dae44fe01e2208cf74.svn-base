<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/11');
?>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="CustomerSendLog_formid">
		<lable>客商档案ID：</lable>
		<input type="text" name="CustomerSendLog[customer_id]" class="easyui-textbox" style="width: 12%;" id="customerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>类型：</lable>		
		<select name="CustomerSendLog[customer_type]" id="customerType" class="easyui-combobox" editable="false" style="width: 150px;">
			<option value=''>全部</option>
			<option value='OW'>货主</option>
			<option value='WH'>仓库</option>
			<option value='VE'>供应商</option>
			<option value='OT'>店铺</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>ERP推送状态：</lable>
		<select name="CustomerSendLog[send_erp_status]" id="sendErpStatus" class="easyui-combobox" editable="false" style="width: 150px;">
			<option value=''>全部</option>
			<option value='1'>成功</option>
			<option value='0'>失败</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>WMS推送状态：</lable>
		<select name="CustomerSendLog[send_wms_status]" id="sendWmsStatus" class="easyui-combobox" editable="false" style="width: 150px;">
			<option value=''>全部</option>
			<option value='1'>成功</option>
			<option value='0'>失败</option>
		</select>&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'" onClick="searchForm()"
			style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript"
	src="./static/js/moudles/base/base-customerSendLog.js"></script>