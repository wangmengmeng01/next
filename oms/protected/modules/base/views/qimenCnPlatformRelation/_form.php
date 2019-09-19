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
<div class="ftitle">奇门-菜鸟电商平台编码对应信息维护界面</div>
<form id="form_relation"> 
<table>
    <tr style="display:none">
		<td>自增ID：</td>
		<td>
			<input type="text" name="Relation[id]" id="id" style="width:200px;" value="<?php echo $model['id'] ?>" class='easyui-validatebox' maxLength="20"/>
		</td>
	</tr>
	<tr>
		<td>奇门电商平台编码：</td>
		<td>
			<input type="text" name="Relation[qimen_platform_code]" id="seller_id" style="width:200px;" value="<?php echo $model['qimen_platform_code'] ?>" class='easyui-validatebox' missingMessage="奇门电商平台编码为必填项"  maxLength="50" data-options="required:true,validType:['code','length[1,50]']"/>
		</td>
	</tr>
	<tr>
		<td>奇门电商平台名称：</td>
		<td>
			<input type="text" name="Relation[qimen_platform_name]"  value="<?php echo $model['qimen_platform_name'] ?>" style="width:200px;" class='easyui-validatebox' maxLength="100" data-options="validType:['name','length[1,100]']"/>
		</td>
	</tr>
	<tr>
		<td>菜鸟电商平台编码：</td>
		<td>
			<input type="text" name="Relation[cn_platform_code]"  value="<?php echo $model['cn_platform_code']; ?>" style="width:200px;" class='easyui-validatebox' missingMessage="菜鸟电商平台编码为必填项"  maxLength="50" data-options="required:true,validType:['code','length[1,50]']"/>
		</td>
	</tr>
	<tr>
		<td>菜鸟电商平台名称：</td>
		<td>
			<input type="text" name="Relation[cn_platform_name]"  value="<?php echo $model['cn_platform_name']; ?>" style="width:200px;" class='easyui-validatebox' maxLength="100" data-options="validType:['name','length[1,100]']"/>
		</td>
	</tr>
	<tr>
		<td>有效性：</td>
		<td>
			<select class="easyui-combobox" id="RelationIsValid" name="Relation[is_valid]" style="width:150px;"/></select>
		</td>
	</tr>
</table>
</form>
