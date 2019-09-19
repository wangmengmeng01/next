<style type="text/css">
#form_customerWarehouseBind{
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
$("#CustomerWarehouseBindIsValid").combobox({
	data : [{ "id":'1', "text":"有效" },{ "id":'0', "text":"无效"}] ,
	valueField : 'id',
	textField : 'text',
	editable : false,
	panelHeight:'auto',
    value: '<?php echo isset($model['is_valid'])?$model['is_valid']:'1';?>'
});
</script>
<div class="ftitle">货主与仓库关系维护</div>
<form id="form_customerWarehouseBind"> 
<table>
	<tr>
		<td>货主ID：</td>
		<td>
			<input type="text" name="CustomerWarehouseBind[customer_id]" id="customerId" style="width:200px;" value="<?php echo $model['customer_id'] ?>" class='easyui-validatebox' missingMessage="货主ID为必填项"  maxLength="20" data-options="required:true,validType:['code','length[2,20]']"/>
		</td>
	</tr>
	<tr>
		<td>仓库编码：</td>
		<td>
			<input type="text" name="CustomerWarehouseBind[code]"  value="<?php echo $model['code'] ?>" style="width:200px;" class='easyui-validatebox' missingMessage="仓库编码为必填项"  maxLength="30" data-options="required:true,validType:['code','length[2,30]']"/>
		</td>
	</tr>
	<tr>
		<td>有效性：</td>
		<td>
			<select class="easyui-combobox" id="CustomerWarehouseBindIsValid" name="CustomerWarehouseBind[is_valid]" style="width:150px;"/></select>
		</td>
	</tr>
</table>
</form>
