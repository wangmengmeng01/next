<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/40');
//获取登陆者是否有操作权限
$operateFlag = util::isHasPri(40.1);
?>
<script type="text/javascript">
	var updateUrl = '<?php echo $this->createUrl('cskSellerCustomeridRelation/update', array('id'=>'uid'));?>';
	var deleteUrl = '<?php echo $this->createUrl('cskSellerCustomeridRelation/delete');?>';
	var operateFlag = <?php echo $operateFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="Relation_formid">
		<lable>商家ID：</lable>
		<input type="text" name="Relation[seller_id]" class="easyui-textbox" style="width: 12%;" id="sellerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>货主代码：</lable>
		<input type="text" name="Relation[customer_code]" class="easyui-textbox" style="width: 130px;" id="customerCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>仓库地址编码：</lable>
		<input type="text" name="Relation[ship_addr_code]" class="easyui-textbox" style="width: 160px;" id="shipAddrCode" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>店铺名称：</lable>
		<input type="text" name="Relation[shop_name]" class="easyui-textbox" style="width: 18%;" id="shopName" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>有效性：</lable>
		<select name="Relation[is_valid]" id="isValid" class="easyui-combobox" editable="false" style="width: 150px;">
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
	src="./static/js/moudles/base/base-cskSellerCustomeridRelation.js"></script>