$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '发货单取消报表',
		url : './index.php?r=inbound/storeProcessCancel/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 20,
		columns : [ [ {
			field : 'wms_order_id',
			title : '仓储系统单据编码'
		}, {
			field : 'order_type',
			title : '订单类型',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'CNJG' : '仓内加工单'
				};
				return jsondata[value];
			}
		}, {
			field : 'customer_id',
			title : '货主ID'
		}, {
			field : 'warehouse_code',
			title : '仓库编码'
		}, {
			field : 'reason',
			title : '取消原因'
		}, {
			field : 'create_time',
			title : '入库时间'
		} ] ],
		frozenColumns : [[{
			field : 'ck',
			checkbox : true
		}, {
			field : 'order_no',
			title : '仓内加工单号'
		}]],
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
			    $('div.datagrid-toolbar a').eq(1).hide();
			}
		}
	})
})

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'StoreProcessCancel[order_no]' : $("#orderNo").val(),
			'StoreProcessCancel[customer_id]' : $("#customerId").val(),
			'StoreProcessCancel[warehouse_code]' : $("#warehouseCode").val(),
			'StoreProcessCancel[start_time]' : $("#startTime").datetimebox("getValue"),
			'StoreProcessCancel[end_time]' : $("#endTime").datetimebox("getValue")
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
			msg : '导出数据为空，请查询后导出'
		});
		return false;
	}
	var orderNo = $.trim($("#orderNo").val());
	var customerId = $.trim($("#customerId").val());
	var warehouseCode = $.trim($("#warehouseCode").val());
	var startTime = $.trim($("#startTime").datetimebox("getValue"));
	var endTime = $.trim($("#endTime").datetimebox("getValue"));
	if (orderNo == '' && customerId == '' && warehouseCode =='') {
		$.messager.show({
			title : '友情提示',
			msg : '订单号、货主ID和仓库编码查询条件不能都为空'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=storeProcessCancel&orderNo='
			+ orderNo
			+ '&customerId='
			+ customerId
			+ '&warehouseCode='
			+ warehouseCode
			+ '&startTime='
			+ startTime
			+ '&endTime=' 
			+ endTime;
}