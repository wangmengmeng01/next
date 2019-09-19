<style type="text/css">
#form_seller{
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
$("#SellerIsValid").combobox({
	data : [{ "id":'1', "text":"有效" },{ "id":'0', "text":"无效"}] ,
	valueField : 'id',
	textField : 'text',
	editable : false,
	panelHeight:'auto',
    value: '<?php echo isset($model['is_valid'])?$model['is_valid']:'1';?>'
});
</script>
<div class="ftitle">商家授权信息维护界面</div>
<form id="form_seller"> 
<table>
	<tr>
		<td>商家ID：</td>
		<td>
			<input type="text" name="Seller[seller_id]" id="seller_id" style="width:200px;" value="<?php echo $model['seller_id'] ?>" class='easyui-validatebox' missingMessage="商家ID为必填项"  maxLength="20" data-options="required:true,validType:['code','length[2,20]']"/>
		</td>
	</tr>
	<tr>
		<td >授权口令：</td>
		<td>
			<input type="text" name="Seller[access_token]"  value="<?php echo $model['access_token'] ?>" style="width:450px;" class='easyui-validatebox' missingMessage="授权口令为必填项"  maxLength="50" data-options="required:true,validType:['token','length[2,100]']"/>
		</td>
	</tr>
	<tr>
		<td>电子面单平台：</td>
		<td>
			<input type="text" name="Seller[platform_elec]"  value="<?php echo $model['platform_elec']; ?>" style="width:200px;" class='easyui-validatebox' missingMessage="电子面单平台为必填项"  maxLength="8" data-options="required:true,validType:['platform','length[1,50]']"/>
		</td>
	</tr>
	<tr>
		<td>物流商产品类型：</td>
		<td>
			<input type="text" name="Seller[product_type]"  value="<?php echo $model['product_type']; ?>" style="width:200px;" class='easyui-validatebox' missingMessage="物流商产品类型为必填项"  maxLength="32" data-options="required:true,validType:['ptype','length[2,200]']"/>
		</td>
	</tr>
	<tr>
		<td>有效性：</td>
		<td>
			<select class="easyui-combobox" id="SellerIsValid" name="Seller[is_valid]" style="width:150px;"/></select>
		</td>
	</tr>
</table>
</form>
