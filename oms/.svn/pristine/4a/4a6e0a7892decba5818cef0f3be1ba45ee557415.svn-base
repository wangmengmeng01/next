<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/29');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(29.1);
?>
<script type="text/javascript">
	var deliveryId = '<?php echo $_GET['id'] ?>';
	var deliveryOrderCode = '<?php echo $_GET['name'] ?>';
	var invoiceViewUrl = '<?php echo Yii::app()->createUrl('outbound/deliveryRecordInvoiceDetail/index', array('id'=>'uid')); ?>';
	var excelExportFlag = <?php echo $excelExportFlag; ?>;
</script>
<div id="dg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/delivery-deliveryRecordInvoice.js"></script>