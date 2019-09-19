$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '入库单取消列表',
		url : './index.php?r=inbound/cancelASNData/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 20,
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
						'PO' : '采购入库',
						'TR' :'调拨入库',
						'RS' :'销售退货入库',
						'IP' : '盘盈入库',
						'SCRK' : '奇门生产入库',
						'LYRK' : '奇门领用入库',
						'CCRK' : '奇门残次品入库',
						'CGRK' : '奇门采购入库',
						'DBRK' : '奇门调拨入库',
						'QTRK' : '奇门其他入库',
						'B2BRK' : '奇门B2B入库',
						'THRK' : '奇门退货入库',
						'HHRK' : '奇门换货入库'
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
			'CancelASNData[order_no]' : $("#orderNo").val(),
			'CancelASNData[order_type]' : $("#orderType").combobox('getValue'),
			'CancelASNData[customer_id]' : $("#customerId").val(),
			'CancelASNData[warehouse_code]' : $("#warehouseCode").val(),
			'CancelASNData[start_time]' : $("#startTime").datetimebox("getValue"),
			'CancelASNData[end_time]' : $("#endTime").datetimebox("getValue")
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
	window.location.href = 'index.php?r=export/index&exportType=cancelAsnData&orderNo='
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
