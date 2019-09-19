<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/24');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(24.1);
?>
<script type="text/javascript">
	var combineId = '<?php echo $_GET['id'] ?>';
	var combineItemCode = '<?php echo $_GET['name'] ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div id="dg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/base/base-combineProductDetail.js"></script>