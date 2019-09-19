<style type="text/css">
#form_logistics{
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
$("#LogisticsIsValid").combobox({
	data : [{ "id":'1', "text":"有效" },{ "id":'0', "text":"无效"}] ,
	valueField : 'id',
	textField : 'text',
	editable : false,
	panelHeight:'auto',
    value: '<?php echo isset($model['is_valid'])?$model['is_valid']:'1';?>'
});
</script>
<div class="ftitle">物流公司维护界面</div>
<form id="form_logistics"> 
<table>
	<tr>
		<td>物流公司编码：</td>
		<td>
			<input type="text" name="Logistics[logistics_code]" id="logisticsCode" style="width:200px;" value="<?php echo $model['logistics_code'] ?>" class='easyui-validatebox' required="true"  missingMessage="物流公司编码为必填项"  maxLength="20" data-options="required:true,validType:['code','length[2,20]']" />
		</td>
	</tr>
	<tr>
		<td>物流公司名称：</td>
		<td>
			<input type="text" name="Logistics[logistics_name]" id="logisticsName"  value="<?php echo $model['logistics_name'] ?>" style="width:200px;" class='easyui-validatebox' required="true"  missingMessage="物流公司名称为必填项"  maxLength="50" data-options="required:true,validType:['special','length[2,50]']"/>
		</td>
	</tr>
	<tr>
		<td>联系电话：</td>
		<td>
			<input type="text" name="Logistics[contact_tel]"  value="<?php echo $model['contact_tel'] ?>" style="width:200px;" class='easyui-validatebox' maxLength="20" validType="phone"/>
		</td>
	</tr>
	<tr>
		<td>联系地址：</td>
		<td>
			<input type="text" name="Logistics[address]" style="width:200px;"  value="<?php echo $model['address'] ?>" class='easyui-validatebox' maxLength="24" validType="special"/>
		</td>
	</tr>
	<tr>
		<td>有效性：</td>
		<td>
			<select class="easyui-combobox" id="LogisticsIsValid" name="Logistics[is_valid]" editable="false" style="width:150px;"/></select>
		</td>
	</tr>
	<tr>
		<td>备注：</td>
		<td>
			<textarea name="Logistics[remark]"  rows=5 cols=40  class="textarea easyui-validatebox" maxLength="200" validType="special"><?php echo $model['remark'] ?></textarea>
		</td>
	</tr>
</table>
</form>
