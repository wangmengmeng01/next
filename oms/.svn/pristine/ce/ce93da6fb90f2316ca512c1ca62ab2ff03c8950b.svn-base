<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/26');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(26.1);
?>
<script type="text/javascript">
	var materialViewUrl = '<?php echo Yii::app()->createUrl('inbound/storeProcessMaterialConfirm/index', array('id'=>'uid','name'=>'uname')); ?>';
	var productViewUrl = '<?php echo Yii::app()->createUrl('inbound/storeProcessProductConfirm/index', array('id'=>'uid','name'=>'uname')); ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div region="north" border="true" split="true"
	style="background: #B3DFDA; padding: 10px">
	<form id="STPC_formid">
	    <lable>加工单编码</lable>
	    <input type="text" name="StoreProcessConfirm[process_order_code]" class="easyui-textbox" style="width:12%" id="processOrderCode"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>开始时间：</lable>
	    <input class="easyui-datetimebox" name="StoreProcessConfirm[start_time]" style="width:200px" id="startTime" value="<?php echo date('Y-m-d', time()).' 00:00:00';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<lable>结束时间：</lable>
	    <input class="easyui-datetimebox" name="StoreProcessConfirm[end_time]" style="width:200px" id="endTime" value="<?php echo date('Y-m-d', time()).' 23:59:59';?>">&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight: bold;">查询</a>
	</form>
</div>
<div id="dg"></div>
<div id="exportDg"></div>
<script type="text/javascript" src="./static/js/moudles/inbound/inbound-storeProcessConfirm.js"></script>