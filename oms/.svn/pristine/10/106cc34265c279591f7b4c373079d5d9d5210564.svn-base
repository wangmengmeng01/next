<style type="text/css">
#form_wms{
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
$("#WmsIsValid").combobox({
	data : [{ "id":'1', "text":"有效" },{ "id":'0', "text":"无效"}] ,
	valueField : 'id',
	textField : 'text',
	editable : false,
	panelHeight:'auto',
    value: '<?php echo isset($model['is_valid'])?$model['is_valid']:'1';?>'
});
</script>
<div class="ftitle">WMS软件维护界面</div>
<form id="form_wms"> 
<table>
	<tr>
		<td>WMS编码：</td>
		<td>
			<input type="text" name="Wms[wms_code]" id="wmsCode" style="width:250px;" value="<?php echo $model['wms_code'] ?>" class='easyui-validatebox' maxLength="20" data-options="required:true,validType:['code','length[2,20]']"  missingMessage="WMS编码为必填项"/>
		</td>
	</tr>
	<tr>
		<td>WMS名称：</td>
		<td>
			<input type="text" name="Wms[wms_name]" id="wmsName"  value="<?php echo $model['wms_name'] ?>" style="width:250px;" class='easyui-validatebox' maxLength="50" data-options="required:true,validType:['special','length[2,50]']"  missingMessage="WMS名称为必填项"/>
		</td>
	</tr>
	<tr>
		<td>接口客户编码：</td>
		<td>
			<input type="text" name="Wms[cilent_customerid]" style="width:250px;"  value="<?php echo $model['cilent_customerid'] ?>" class='easyui-validatebox' maxLength="32" required="true" validType="code" missingMessage="接口客户编码为必填项"/>
		</td>
	</tr>
	<tr>
		<td>接口Token号：</td>
		<td>
			<input type="text" name="Wms[app_token]" style="width:250px;"  value="<?php echo $model['app_token'] ?>" class='easyui-validatebox' maxLength="40" required="true" validType="code" missingMessage="接口Token号为必填项"/>
		</td>
	</tr>
	<tr>
		<td>接口验签key：</td>
		<td>
			<input type="text" name="Wms[app_key]" style="width:250px;"  value="<?php echo $model['app_key'] ?>" class='easyui-validatebox' maxLength="32" required="true" validType="code" missingMessage="接口验签key为必填项"/>
		</td>
	</tr>
	<tr>
		<td>接口秘钥：</td>
		<td>
			<input type="text" name="Wms[app_secret]" style="width:250px;"  value="<?php echo $model['app_secret'] ?>" class='easyui-validatebox' maxLength="32" required="true" validType="code" missingMessage="接口秘钥为必填项"/>
		</td>
	</tr>
	<tr>
		<td>联系人：</td>
		<td>
			<input type="text" name="Wms[contact]" style="width:250px;"  value="<?php echo $model['contact'] ?>" class='easyui-validatebox' maxLength="20" validType="special"/>
		</td>
	</tr>
	<tr>
		<td>联系人手机号：</td>
		<td>
			<input type="text" name="Wms[contact_phone]"  value="<?php echo $model['contact_phone'] ?>" style="width:250px;" class='easyui-validatebox' maxLength="24" validType="mobile"/>
		</td>
	</tr>
	<tr>
		<td>联系人固话：</td>
		<td>
			<input type="text" name="Wms[contact_tel]"  value="<?php echo $model['contact_tel'] ?>" style="width:250px;" class='easyui-validatebox' maxLength="24" validType="phone"/>
		</td>
	</tr>
	<tr>
		<td>联系地址：</td>
		<td>
			<input type="text" name="Wms[address]" style="width:350px;"  value="<?php echo $model['address'] ?>" class='easyui-validatebox' maxLength="100" validType = "length[2,255]" validType="special"/>
		</td>
	</tr>
	<tr>
		<td>有效性：</td>
		<td>
			<select class="easyui-combobox" id="WmsIsValid" name="Wms[is_valid]" editable="false" style="width:150px;"/></select>
		</td>
	</tr>
	<tr>
		<td>备注：</td>
		<td>
			<textarea name="Wms[remark]"  rows=5 cols=40  class="textarea easyui-validatebox" maxLength="200" validType="special"><?php echo $model['remark'] ?></textarea>
		</td>
	</tr>
</table>
</form>
