$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '出库取消列表',
		url : './index.php?r=outbound/cancelSOData/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 50,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'order_no',
			title : '订单号'
		}, {
			field : 'order_type',
			title : '订单类型',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
						'SO' : '销售出库',
						'TO' :'调拨出库',
						'RP' :'采购退货出库',
						'IL' : '盘亏出库',
						'OO' : '线下出库',
						'PTCK' : '奇门普通出库单（退仓）',
						'DBCK' : '奇门调拨出库',
						'B2BCK' : '奇门B2B出库',
						'QTCK' : '奇门其他出库'
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
			'CancelSOData[order_no]' : $("#orderNo").val(),
			'CancelSOData[order_type]' : $("#orderType").combobox('getValue'),
			'CancelSOData[customer_id]' : $("#customerId").val(),
			'CancelSOData[warehouse_code]' : $("#warehouseCode").val(),
			'CancelSOData[start_time]' : $("#startTime").datetimebox("getValue"),
			'CancelSOData[end_time]' : $("#endTime").datetimebox("getValue")
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
	var orderType = $.trim($("#orderType").combobox('getValue'));
	var customerId = $.trim($("#customerId").val());	
	var warehouseCode = $.trim($("#warehouseCode").val());	
	var startTime = $.trim($("#startTime").datetimebox("getValue"));
	var endTime = $.trim($("#endTime").datetimebox("getValue"));
	if (orderNo == '' && customerId == '' && warehouseCode == '') {
		$.messager.show({
			title : '友情提示',
			msg : '订单号、货主ID和仓库编码查询条件不能都为空'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=cancelSoData&orderNo='
			+ orderNo
			+ '&orderType='
			+ orderType
			+ '&customerId='
			+ customerId
			+ '&warehouseCode='
			+ warehouseCode
			+ '&startTime='
			+ startTime
			+ '&endTime=' 
			+ endTime;
}
