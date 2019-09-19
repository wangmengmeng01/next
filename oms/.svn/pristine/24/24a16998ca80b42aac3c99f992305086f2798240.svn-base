$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '库存查询列表',
		url : './index.php?r=inventory/queryINVData/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 50,
		columns : [ [ {
			field : 'customer_id',
			title : '货主ID'
		}, {
			field : 'warehouse_code',
			title : '仓库编码'
		}, {
			field : 'sku',
			title : 'SKU'
		}, {
			field : 'qty',
			title : '可用数量',
			formatter : function(value, rowData, rowIndex) {
			    var newValue = parseInt(value);
				return newValue;
			}
		}, {
			field : 'occupy_qty',
			title : '占用数',
			formatter : function(value, rowData, rowIndex) {
			    var newValue = parseInt(value);
				return newValue;
			}
		}, {
			field : 'qty_total',
			title : '总数量',
			formatter : function(value, rowData, rowIndex) {
			    var newValue = parseInt(value);
				return newValue;
			}
		}, {
			field : 'invver',
			title : '库存版本'
		}, {
			field : 'create_time',
			title : '入库时间'
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

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'QueryINVData[customer_id]' : $("#customerId").val(),
			'QueryINVData[warehouse_code]' : $("#warehouseCode").val(),
			'QueryINVData[sku]' : $("#sku").val()			
		}
	})
}

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
	var sku = $.trim($("#sku").val());
	var customerId = $.trim($("#customerId").val());
	var warehouseCode = $.trim($("#warehouseCode").val());
	if (sku == '' && customerId == '' && warehouseCode == '') {
		$.messager.show({
			title : '友情提示',
			msg : 'SKU、货主ID和仓库编码查询条件不能都为空'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=queryInvData&sku=' 
		+ sku 
		+ '&customerId=' 
		+ customerId
		+ '&warehouseCode='
		+ warehouseCode;
}
