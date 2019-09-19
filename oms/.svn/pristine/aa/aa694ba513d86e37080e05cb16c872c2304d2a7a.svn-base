<?php
//校验是否有查看权限
Yii::app()->runController('site/CheckPri/pos/8');
?>
<div region="north" border="true" split="true"	style="background: #B3DFDA; padding: 10px">
    <form id="supplier_formid">
       <label>供应商编码：</label>
       <input type="text" name="Supplier[supplier_code]" class="easyui-textbox" id="Supplier_supplier_code" />&nbsp;&nbsp;&nbsp;&nbsp;
       <label>供应商名称：</label>
       <input type="text" name="Supplier[descr_c]" class="easyui-textbox" id="Supplier_descr_c"/>&nbsp;&nbsp;&nbsp;&nbsp;
       <label>是否激活：</label>
       <select id="Supplier_active_flag" name="Supplier[active_flag]" class="easyui-combobox" editable="false" style="width: 16%;">
			<option value=''>全部</option>
			<option value='Y'>是</option>
			<option value='N'>否</option>
		</select>              
        <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onClick="searchForm()" style="font-weight:bold;">查询</a>
    </form>
</div>
<div id="dg"></div>
<div id="dlg"></div>
<div id="remark_div"></div>
<script type="text/javascript" src="./static/js/moudles/base/base-supplier.js"></script>