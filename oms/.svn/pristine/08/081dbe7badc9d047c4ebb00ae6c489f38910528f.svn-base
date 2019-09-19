<style type="text/css">
#form_customerBind{
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
$("#CustomerBindIsValid").combobox({
	data : [{ "id":'1', "text":"有效" },{ "id":'0', "text":"无效"}] ,
	valueField : 'id',
	textField : 'text',
	editable : false,
	panelHeight:'auto',
    value: '<?php echo isset($model['is_valid'])?$model['is_valid']:'1';?>'
});
</script>
<div class="ftitle">货主与ERP和WMS维护</div>
<form id="form_customerBind"> 
<table>
	<tr>
		<td>货主ID：</td>
		<td>
			<input type="text" name="CustomerBind[customer_id]" id="customerId" style="width:200px;" value="<?php echo $model['customer_id'] ?>" class='easyui-validatebox' missingMessage="货主ID为必填项"  maxLength="20" data-options="required:true,validType:['code','length[2,20]']"/>
		</td>
	</tr>
	<tr>
		<td>ERP编码：</td>
		<td>
			<input type="text" name="CustomerBind[erp_code]"  value="<?php echo $model['erp_code'] ?>" style="width:200px;" class='easyui-validatebox' missingMessage="ERP编码为必填项"  maxLength="30" data-options="required:true,validType:['code','length[2,30]']"/>
		</td>
	</tr>
	<tr>
		<td>ERP版本号：</td>
		<td>
			<input type="text" name="CustomerBind[erp_api_ver]"  value="<?php echo $model['erp_api_ver']; ?>" style="width:200px;" class='easyui-validatebox' missingMessage="ERP版本号为必填项"  maxLength="10" data-options="required:true,validType:['code']"/>
		</td>
	</tr>
	<tr>
		<td>ERP接口地址：</td>
		<td>
			<input type="text" name="CustomerBind[erp_api_url]"  value="<?php echo $model['erp_api_url']; ?>" style="width:450px;" class='easyui-validatebox' missingMessage="ERP接口地址为必填项"  maxLength="100" data-options="required:true,validType:['interfaceUrl']"/>
		</td>
	</tr>
	<tr>
		<td>WMS编码：</td>
		<td>
			<input type="text" name="CustomerBind[wms_code]"  value="<?php echo $model['wms_code'] ?>" style="width:200px;" class='easyui-validatebox' missingMessage="WMS编码为必填项"  maxLength="30" data-options="required:true,validType:['code','length[2,30]']"/>
		</td>
	</tr>
	<tr>
		<td>WMS版本号：</td>
		<td>
			<input type="text" name="CustomerBind[wms_api_ver]"  value="<?php echo $model['wms_api_ver']; ?>" style="width:200px;" class='easyui-validatebox' missingMessage="WMS版本号为必填项"  maxLength="10" data-options="required:true,validType:['code']"/>
		</td>
	</tr>
	<tr>
		<td>WMS接口地址：</td>
		<td>
			<input type="text" name="CustomerBind[wms_api_url]"  value="<?php echo $model['wms_api_url']; ?>" style="width:450px;" class='easyui-validatebox' missingMessage="WMS接口地址为必填项"  maxLength="100" data-options="required:true,validType:['interfaceUrl']"/>
		</td>
	</tr>	
	<tr>
		<td>有效性：</td>
		<td>
			<select class="easyui-combobox" id="CustomerBindIsValid" name="CustomerBind[is_valid]" style="width:150px;"/></select>
		</td>
	</tr>
</table>
</form>
