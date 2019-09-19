$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '发货单发票明细',
		url : './index.php?r=outbound/deliveryRecordInvoiceDetail/data&bill_id=' + billId,
		method : 'POST',
		rownumbers : true,
		height : $(window).height(),
		pagination : true,
		pageSize : 20,
		columns : [ [ {
			field : 'item_name',
			title : '商品名称'
		}, {
			field : 'unit',
			title : '商品单位'
		}, {
			field : 'price',
			title : '商品单价'
		}, {
			field : 'quantity',
			title : '数量'
		}, {
			field : 'amount',
			title : '金额'
		},{
			field : 'create_time',
			title : '创建时间'
		} ] ],
		toolbar : [ {
			id : 'exportExcel',
			title : '导出Excel',
			text : '导出Excel',
			iconCls : 'icon-export',
			handler : function() {
				openExportExcel(billId);
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
function openExportExcel(billId) {
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
	if (billId == '') {
		$.messager.show({
			title : '友情提示',
			msg : '非法操作'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=deliveryRecordInvoiceDetail&bill_id=' + billId;
}
