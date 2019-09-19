<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/84');
//获取登陆者是否有excel导出权限
$excelExportFlag = util::isHasPri(84.1);
?>
<script type="text/javascript">
    var vipJitPickListUrl = '<?php echo Yii::app()->createUrl('outbound/vipJitPickList/index');?>';
    var vendorId = '<?php echo $_GET['vendorId'] ?>';
    var sellSite = '<?php echo $_GET['sellSite'] ?>';
    var platformCode = '<?php echo $_GET['platformCode'] ?>';
    var shopName = '<?php echo $_GET['shopName'] ?>';
    var isPush = false;
    var selfreq = '<?php echo VIP_OMS_SELF_REQ_SECRET;?>';
    var vendorId = '<?php echo VIP_VENDOR_ID;?>';
</script>
<div id="dpg"></div>
<script type="text/javascript" src="./static/js/moudles/outbound/outbound-vipPushPick.js"></script>