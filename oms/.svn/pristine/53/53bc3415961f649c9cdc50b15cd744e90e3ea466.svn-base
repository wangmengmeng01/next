<style type="text/css">
    #form_seller{
        margin:0;
        padding:10px 30px;
    }
    .ftitle{
        font-size:12px;
        font-weight:bold;
        padding:5px 0;
        margin-bottom:10px;
        border-bottom:1px solid #ccc;
    }
    .fitem{
        margin-bottom:10px;
    }
    .fitem label{
        display:inline-block;
        width:80px;
    }
</style>
<div class="ftitle">承运商配置信息维护界面</div>
<form id="form_merchant">
    <table>
        <tr>
            <td>商家编码：</td>
            <td>
                <input type="text" name="Merchant[vendor_code]" style="width:200px;" value="<?php echo $model['vendor_code'] ?>" class='easyui-validatebox' readonly   data-options="required:true"/>
            </td>
        </tr>
        <tr>
            <td >PDD承运商编码：</td>
            <td>
                <input type="text" name="Merchant[provider_code]" value="<?php echo $model['provider_code'] ?>" style="width:450px;" class='easyui-validatebox' readonly   data-options="required:true"/>
            </td>
        </tr>
        <tr>
            <td>WMS承运商编码：</td>
            <td>
                <input type="text" name="Merchant[wms_provider_code]"  value="<?php echo $model['wms_provider_code']; ?>" style="width:200px;" class='easyui-validatebox' missingMessage="WMS承运商编码为必填项"   data-options="required:true"/>
            </td>
        </tr>
    </table>
    <input type="hidden" name="Merchant[id]" style="width:200px;" value="<?php echo $model['id'] ?>"  readonly   data-options="required:true"/>
</form>
