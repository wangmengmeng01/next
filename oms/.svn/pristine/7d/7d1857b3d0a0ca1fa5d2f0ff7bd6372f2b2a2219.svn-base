$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '发货单SN通知明细',
		url : './index.php?r=outbound/deliverySnReportDetail/data&sn_id=' + snId,
		method : 'POST',
		rownumbers : true,
		height : $(window).height(),
		pagination : true,
		pageSize : 20,
		columns : [ [ {
			field : 'sn_id',
			title : '发货单订单号',
			formatter : function(value, rowData, rowIndex) {
				return deliveryOrderCode;
			}
		}, {
			field : 'item_code',
			title : '商品编码'
		}, {
			field : 'item_id',
			title : '商品仓储系统编码'
		}, {
			field : 'sn',
			title : '商品序列号'
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
				openExportExcel(snId, deliveryOrderCode);
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
function openExportExcel(snId, deliveryOrderCode) {
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
	if (snId == '') {
		$.messager.show({
			title : '友情提示',
			msg : '非法操作'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=deliverySnReportDetail&sn_id=' + snId + '&delivery_order_code=' + deliveryOrderCode;
}
