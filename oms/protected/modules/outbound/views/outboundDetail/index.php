<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/15');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(15.1);
?>
<script type="text/javascript">
	var orderId = '<?php echo $_GET['id'] ?>';
	var orderNo = '<?php echo $_GET['name'] ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div id="dg"></div>
<div id="remark_div"></div>
<script e="text/javascript" src="./static/js/moudles/outbound/outbound-outboundDetail.js"></script>