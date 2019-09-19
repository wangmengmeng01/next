<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/84');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(84.1);
?>
<script type="text/javascript">
    var pickNo = '<?php echo $_GET['pickNo'] ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>

<div id="dg"></div>
<div id="exportDg"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/outbound-vipJitPickDetail.js"></script>