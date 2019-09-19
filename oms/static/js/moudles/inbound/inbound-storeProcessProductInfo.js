$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '仓内加工单商品明细',
		url : './index.php?r=inbound/storeProcessProductInfo/data&process_id=' + processId,
		method : 'POST',
		rownumbers : true,
		height : $(window).height(),
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'process_id',
			title : '订单号',
			formatter : function(value, rowData, rowIndex) {
				return processOrderCode;
			}
		}, {
			field : 'process_order_code',
			title : '加工单编码'
		}, {
			field : 'item_code',
			title : '交易平台订单'
		}, {
			field : 'item_id',
			title : '交易平台子订单编码'
		}, {
			field : 'inventory_type',
			title : '货主编码'
		}, {
			field : 'quantity',
			title : '商品编码'
		}, {
			field : 'product_date',
			title : '仓储系统商品编码'
		}, {
			field : 'expire_date',
			title : '库存类型'
		}, {
			field : 'produce_code',
			title : '商品名称'
		}, {
			field : 'is_valid',
			title : '交易平台商品编码'
		}, {
			field : 'create_time',
			title : '创建时间'
		} ] ],
		toolbar : [ {
			id : 'exportExcel',
			title : '导出Excel',
			text : '导出Excel',
			iconCls : 'icon-export',
			handler : function() {
				openExportExcel();
			}
		} ],
		onBeforeLoad : function() {
			if (excelExportFlag == 0 ) {
			    $('div.datagrid-toolbar').hide();
			}			
		}
	})
})

/**
 * 导出excel
 * 
 */
function openExportExcel() {
	//校验是否有导出的权限
	if (excelExportFlag == 0 ) {
		$.messager.show({
			title : '友情提示',
			msg : '您没有导出excel的权限！'
		});
		return false;
	}
	var rows = $("#dg").datagrid("getRows");
	if(rows[0]===undefined){
		$.messager.show({
			title : '友情提示',
			msg : '没有需要导出的数据'
		});
		return false;
	}
	if (processId == '') {
		$.messager.show({
			title : '友情提示',
			msg : '非法操作'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=storeProcessProductInfo&process_id=' + processId + '&process_order_code=' + processOrderCode;
}
