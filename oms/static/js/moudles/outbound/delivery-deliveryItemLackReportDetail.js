$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '发货单缺货通知明细',
		url : './index.php?r=outbound/deliveryItemLackReportDetail/data&deliv_lack_id=' + delivLackId,
		method : 'POST',
		rownumbers : true,
		height : $(window).height(),
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'deliv_lack_id',
			title : '订单号',
			formatter : function(value, rowData, rowIndex) {
				return deliveryOrderCode;
			}
		}, {
			field : 'item_code',
			title : '商品编码'
		}, {
			field : 'item_id',
			title : '仓储系统商品ID'
		}, {
			field : 'inventory_type',
			title : '库存类型'
		}, {
			field : 'batch_code',
			title : '批次编码'
		}, {
			field : 'product_date',
			title : '商品生产日期'
		},{
			field : 'expire_date',
			title : '商品过期日期'
		},{
			field : 'produce_code',
			title : '生产批号'
		},{
			field : 'plan_aty',
			title : '应发商品数量'
		},{
			field : 'lack_aty',
			title : '缺货商品数量'
		},{
			field : 'reason',
			title : '缺货原因 (系统报缺, 实物报缺)'
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
				openExportExcel(delivLackId, deliveryOrderCode);
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
function openExportExcel(delivLackId, deliveryOrderCode) {
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
	if (delivLackId == '') {
		$.messager.show({
			title : '友情提示',
			msg : '非法操作'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=deliveryItemLackReportDetail&deliv_lack_id=' + delivLackId + '&delivery_order_code=' + deliveryOrderCode;
}
