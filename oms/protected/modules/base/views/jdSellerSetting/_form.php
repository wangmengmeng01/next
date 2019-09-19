<style type="text/css">
#form_relation{
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
$("#RelationIsValid").combobox({
	data : [{ "id":'1', "text":"有效" },{ "id":'0', "text":"无效"}] ,
	valueField : 'id',
	textField : 'text',
	editable : false,
	panelHeight:'auto',
    value: '<?php echo isset($model['is_valid'])?$model['is_valid']:'1';?>'
});
</script>
<div class="ftitle">京东商家配置页面</div>
<form id="form_jd_seller"> 
<table>
	<tr>
		<td>商家编码：</td>
		<td>
			<input type="text" name="Relation[seller_id]" id="seller_id" style="width:200px;" value="<?php echo $model['seller_id'] ?>" class='easyui-validatebox' missingMessage="商家ID为必填项"  maxLength="20" data-options="required:true,validType:['code','length[2,20]']"/>
		</td>
	</tr>
	<tr>
		<td>商家名称：</td>
		<td>
			<input type="text" name="Relation[platform_elec]"  value="<?php echo $model['platform_elec'] ?>" style="width:450px;" class='easyui-validatebox' missingMessage="电子面单平台为必填项"  maxLength="50" data-options="required:true,validType:['platform','length[1,50]']"/>
		</td>
	</tr>
	<tr>
		<td>分拣中心编码：</td>
		<td>
			<input type="text" name="Relation[platform_mall]"  value="<?php echo $model['platform_mall']; ?>" style="width:200px;" class='easyui-validatebox' missingMessage="电商平台为必填项"  maxLength="50" data-options="required:true,validType:['code','length[1,50]']"/>
		</td>
	</tr>
	<tr>
		<td>市编码：</td>
		<td>
			<input type="text" name="Relation[shop_name]"  value="<?php echo $model['shop_name']; ?>" style="width:200px;" class='easyui-validatebox' missingMessage="店铺名称为必填项"  maxLength="50" data-options="required:true,validType:['shopName','length[1,50]']"/>
		</td>
	</tr>
	<tr>
		<td>电商平台商家编码：</td>
		<td>
			<input type="text" name="Relation[customer_code]"  value="<?php echo $model['customer_code']; ?>" style="width:200px;" class='easyui-validatebox' missingMessage="货主代码为必填项"  maxLength="50" data-options="required:true,validType:['code','length[1,50]']"/>
		</td>
	</tr>
	<tr>
		<td>仓库地址编码：</td>
		<td>
			<input type="text" name="Relation[ship_addr_code]"  value="<?php echo $model['ship_addr_code']; ?>" style="width:200px;" class='easyui-validatebox' missingMessage="仓库地址编码为必填项"  maxLength="200" data-options="required:true,validType:['code','length[1,30]']"/>
		</td>
	</tr>
	<tr>
		<td>销售平台：</td>
		<td>
			<select class="easyui-combobox" id="RelationIsValid" name="Relation[is_valid]" style="width:150px;"/></select>
		</td>
	</tr>
	<tr>
		<td>联系电话：</td>
		<td>
			<select class="easyui-combobox" id="RelationIsValid" name="Relation[is_valid]" style="width:150px;"/></select>
		</td>
	</tr>
	<tr>
		<td>店铺地址：</td>
		<td>
			<select class="easyui-combobox" id="RelationIsValid" name="Relation[is_valid]" style="width:150px;"/></select>
		</td>
	</tr>
</table>
</form>
