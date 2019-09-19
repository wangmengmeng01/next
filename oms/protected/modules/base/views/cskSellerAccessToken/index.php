<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/38');
//获取登陆者是否有操作权限
$operateFlag = util::isHasPri(38.1);
?>
<script type="text/javascript">
	var updateUrl = '<?php echo $this->createUrl('cskSellerAccessToken/update', array('id'=>'uid'));?>';
	var deleteUrl = '<?php echo $this->createUrl('cskSellerAccessToken/delete');?>';
	var operateFlag = <?php echo $operateFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="Seller_formid">
		<lable>商家ID：</lable>
		<input type="text" name="Seller[seller_id]" class="easyui-textbox" style="width: 12%;" id="sellerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>授权口令：</lable>
		<input type="text" name="Seller[access_token]" class="easyui-textbox" style="width: 18%;" id="accessToken" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>电子面单平台：</lable>
		<input type="text" name="Seller[platform_elec]" class="easyui-textbox" style="width: 130px;" id="platformElec" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>物流商产品类型：</lable>
		<input type="text" name="Seller[product_type]" class="easyui-textbox" style="width: 160px;" id="productType" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>有效性：</lable>
		<select name="Seller[is_valid]" id="isValid" class="easyui-combobox" editable="false" style="width: 150px;">
			<option value=''>全部</option>
			<option value='1'>有效</option>
			<option value='0'>无效</option>
		</select> 
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'" onClick="searchForm()"
			style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript"
	src="./static/js/moudles/base/base-cskSellerAccessToken.js"></script>