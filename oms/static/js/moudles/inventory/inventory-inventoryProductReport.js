$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '库存盘点明细报表',
		url : './index.php?r=inventory/inventoryProductReport/data&inventory_id=' + inventoryId,
		method : 'POST',
		rownumbers : true,
		height : $(window).height(),
		pagination : true,
		pageSize : 20,
		columns : [ [ {
			field : 'inventory_id',
			title : '订单号',
			formatter : function(value, rowData, rowIndex) {
				return checkOrderCode;
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
			field : 'quantity',
			title : '盘盈盘亏商品变化量'
		}, {
			field : 'batch_code',
			title : '批次编码'
		},{
			field : 'product_date',
			title : '商品生产日期'
		},{
			field : 'expire_date',
			title : '商品过期日期'
		},{
			field : 'produce_code',
			title : '生产批号'
		},{
			field : 'sn_code',
			title : '商品序列号'
		},{
			field : 'remark',
			title : '备注'
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
	if (inventoryId == '') {
		$.messager.show({
			title : '友情提示',
			msg : '非法操作'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=inventoryProductReport&inventory_id=' + inventoryId + '&check_order_code=' + checkOrderCode ;
}
