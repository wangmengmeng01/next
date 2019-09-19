<style type="text/css">
#form_customer{
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
$("#CustomerActiveFlag").combobox({
	data : [{ "id":'Y', "text":"是" },{ "id":'N', "text":"否"}] ,
	valueField : 'id',
	textField : 'text',
	editable : false,
	panelHeight:'auto',
    value: '<?php echo isset($model['active_flag'])?$model['active_flag']:'Y';?>'
});
$("#CustomerIsValid").combobox({
	data : [{ "id":'1', "text":"有效" },{ "id":'0', "text":"无效"}] ,
	valueField : 'id',
	textField : 'text',
	editable : false,
	panelHeight:'auto',
    value: '<?php echo isset($model['is_valid'])?$model['is_valid']:'1';?>'
});
</script>
<div class="ftitle">客户维护界面</div>
<form id="form_customer"> 
<table>
	<tr>
		<td>货主ID：</td>
		<td>
			<input type="text" name="Customer[customer_id]" id="customer_id" style="width:200px;" value="<?php echo $model['customer_id'] ?>" class='easyui-validatebox' missingMessage="货主ID为必填项"  maxLength="20" data-options="required:true,validType:['code','length[2,20]']"/>
		</td>
	</tr>
	<tr>
		<td>货主名称：</td>
		<td>
			<input type="text" name="Customer[customer_name]"  value="<?php echo $model['customer_name'] ?>" style="width:200px;" class='easyui-validatebox' missingMessage="货主名称为必填项"  maxLength="50" data-options="required:true,validType:['special','length[2,50]']"/>
		</td>
	</tr>
	<tr>
		<td>所属网点编码：</td>
		<td>
			<input type="text" name="Customer[branch_code]"  value="<?php echo $model['branch_code']; ?>" style="width:200px;" class='easyui-validatebox' missingMessage="所属网点编码为必填项"  maxLength="8" data-options="required:true,validType:['gsbm','length[6,8]']"/>
		</td>
	</tr>
	<tr>
		<td>接口秘钥：</td>
		<td>
			<input type="text" name="Customer[app_secret]"  value="<?php echo $model['app_secret']; ?>" style="width:200px;" class='easyui-validatebox' missingMessage="接口秘钥为必填项"  maxLength="32" data-options="required:true,validType:['code','length[6,32]']"/>
		</td>
	</tr>
	<tr>
		<td>联系人：</td>
		<td>
			<input type="text" name="Customer[contact1]" style="width:200px;"  value="<?php echo $model['contact1'] ?>" class='easyui-validatebox' maxLength="20" validType="special"/>
		</td>
	</tr>
	<tr>
		<td>联系人手机号码：</td>
		<td>
			<input type="text" name="Customer[contact1_tel1]"  value="<?php echo $model['contact1_tel1'] ?>" style="width:200px;" class='easyui-validatebox' maxLength="24" validType="mobile"/>
		</td>
	</tr>
	<tr>
		<td>联系人电话号码：</td>
		<td>
			<input type="text" name="Customer[contact1_tel2]"  value="<?php echo $model['contact1_tel2'] ?>" style="width:200px;" class='easyui-validatebox' maxLength="24" validType="phone"/>
		</td>
	</tr>
	<tr>
		<td>联系地址：</td>
		<td>
			<input type="text" name="Customer[address1]" style="width:350px;"  value="<?php echo $model['address1'] ?>" class='easyui-validatebox' maxLength="100" validType="special"/>
		</td>
	</tr>
	<tr>
		<td>激活标志：</td>
		<td>
			<select class="easyui-combobox" id="CustomerActiveFlag" name="Customer[active_flag]" style="width:150px;"/></select>
		</td>
	</tr>
	<tr>
		<td>有效性：</td>
		<td>
			<select class="easyui-combobox" id="CustomerIsValid" name="Customer[is_valid]" style="width:150px;"/></select>
		</td>
	</tr>
	<tr>
		<td>备注：</td>
		<td>
			<textarea name="Customer[remark]" rows=5 cols=40  class="textarea easyui-validatebox" maxLength="200" validType="special"><?php echo $model['remark']; ?></textarea>
		</td>
	</tr>
</table>
</form>
