<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/18');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(18.1);
?>
<script type="text/javascript">
	var inventoryId = '<?php echo $_GET['id'] ?>';
	var checkOrderCode = '<?php echo $_GET['name'] ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div id="dg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/inventory/inventory-inventoryProductReport.js"></script>