<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/25');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(25.1);
?>
<script type="text/javascript">
	var processId = '<?php echo $_GET['id'] ?>';
	var processOrderCode = '<?php echo $_GET['name'] ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div id="dg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/inbound/inbound-storeProcessMaterialInfo.js"></script>