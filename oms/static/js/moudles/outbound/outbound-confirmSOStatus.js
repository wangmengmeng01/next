$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '出库单状态回传列表',
		url : './index.php?r=outbound/confirmSOStatus/data',
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
			field : 'order_status',
			title : '订单状态'
		}, {
			field : 'order_desc',
			title : '状态描述'
		} , {
			field : 'operator_time',
			title : '操作时间'
		} , {
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
			'ConfirmSOStatus[order_no]' : $("#orderNo").val(),
			'ConfirmSOStatus[order_type]' : $("#orderType").combobox('getValue'),
			'ConfirmSOStatus[customer_id]' : $("#customerId").val(),
			'ConfirmSOStatus[start_time]' : $("#startTime").datetimebox("getValue"),
			'ConfirmSOStatus[end_time]' : $("#endTime").datetimebox("getValue")
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
	var startTime = $.trim($("#startTime").datetimebox("getValue"));
	var endTime = $.trim($("#endTime").datetimebox("getValue"));
	if (orderNo == '' && customerId == '') {
		$.messager.show({
			title : '友情提示',
			msg : '订单号和货主ID查询条件不能都为空'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=confirmSoStatus&orderNo='
			+ orderNo
			+ '&orderType='
			+ orderType
			+ '&customerId='
			+ customerId
			+ '&startTime='
			+ startTime
			+ '&endTime=' 
			+ endTime;
}