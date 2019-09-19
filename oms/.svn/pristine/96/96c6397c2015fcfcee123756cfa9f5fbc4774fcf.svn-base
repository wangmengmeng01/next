<style type="text/css">
#form_addr{
	margin:0;
	padding:10px 30px;
}
.ftitle{
	font-size:12px;
	font-weight:bold;
	padding:5px 0;
	margin-bottom:10px;
	border-bottom:1px solid #ccc;
}
.fitem{
	margin-bottom:10px;
}
.fitem label{
	display:inline-block;
	width:80px;
}
</style>
<script type="text/javascript">
$("#AddrIsValid").combobox({
	data : [{ "id":'1', "text":"有效" },{ "id":'0', "text":"无效"}] ,
	valueField : 'id',
	textField : 'text',
	editable : false,
	panelHeight:'auto',
    value: '<?php echo isset($model['is_valid'])?$model['is_valid']:'1';?>'
});
</script>
<div class="ftitle">发货地址维护界面</div>
<form id="form_addr"> 
<table>
	<tr>
		<td>仓库地址编码：</td>
		<td>
			<input type="text" name="Addr[ship_addr_code]" id="ship_addr_code" style="width:200px;" value="<?php echo $model['ship_addr_code'] ?>" class='easyui-validatebox' missingMessage="仓库地址编码为必填项"  maxLength="20" data-options="required:true,validType:['code','length[2,20]']"/>
		</td>
	</tr>
	<tr>
		<td >发件地址：</td>
		<td>
			<input type="text" name="Addr[ship_address]"  value="<?php echo $model['ship_address'] ?>" style="width:300px;" class='easyui-validatebox' missingMessage="发件地址为必填项"  maxLength="200" data-options="required:true,validType:['addr','length[2,200]']"/>
		</td>
	</tr>
	<tr>
		<td>有效性：</td>
		<td>
			<select class="easyui-combobox" id="AddrIsValid" name="Addr[is_valid]" style="width:150px;"/></select>
		</td>
	</tr>
</table>
</form>
