<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/40');
//获取登陆者是否有操作权限
$operateFlag = util::isHasPri(40.1);
?>
<script type="text/javascript">
	var operateFlag = <?php echo $operateFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="Relation_formid">
		<lable>商家编码：</lable>
		<input type="text" name="Relation[seller_id]" class="easyui-textbox" style="width: 12%;" id="sellerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>商家名称：</lable>
		<input type="text" name="Relation[seller_id]" class="easyui-textbox" style="width: 12%;" id="sellerId" />&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton"
			data-options="iconCls:'icon-search'" onClick="searchForm()"
			style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/base/base-jdSellerSetting.js"></script>