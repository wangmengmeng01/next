$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '订单流水通知报表',
		url : './index.php?r=outbound/orderProcessReport/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'process_id',
			hidden: true,
			title : '流水id'
		}, {
			field : 'order_id',
			title : '仓储系统单据号'
		}, {
			field : 'warehouse_code',
			title : '仓库编码'
		}, {
			field : 'order_type',
			title : '单据类型'
			/*
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
						'JYCK' : '一般交易出库单',
						'HHCK' : '换货出库单',
						'BFCK' : '补发出库单',
						'PTCK' : '普通出库单',
						'DBCK' : '调拨出库单',
						'B2BRK' : 'B2B入库单',
						'B2BCK' : 'B2B出库单',
						'QTCK' : '其他出库单',
						'SCRK' : '生产入库单',
						'LYRK' : '领用入库单',
						'CCRK' : '残次品入库单',
						'CGRK' : '采购入库单',
						'DBRK' : ' 调拨入库单',
						'QTRK' : '其他入库单',
						'XTRK' : '销退入库单',
						'HHRK' : '换货入库单',
						'CNJG' : '仓内加工单'
				};
				return jsondata[value];
			}*/
		}, {
			field : 'process_status',
			title : '单据状态',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
						'ACCEPT' : '仓库接单 ',
						'PARTFULFILLED' : '部分收货完成 ',
						'FULFILLED' : '收货完成 ',
						'PRINT' : '打印 ',
						'PICK' : '拣货 ',
						'CHECK' : '复核 ',
						'PACKAGE' : '打包 ',
						'WEIGH' : '称重 ',
						'READY' : '待提货 ',
						'DELIVERED' : '已发货 ',
						'REFUSE' : '买家拒签 ',
						'EXCEPTION' : '异常 ',
						'CLOSED' : '关闭 ',
						'CANCELED' : '取消 ',
						'REJECT' : '仓库拒单 ',
						'SIGN' : '签收 ',
						'TMSCANCELED' : '快递拦截 ',
						'OTHER' : '其他 ',
						'PARTDELIVERED' : '部分发货完成'
				};
				return jsondata[value];
			}
		}, {
			field : 'operator_code',
			title : '当前状态操作员编码'
		}, {
			field : 'operator_name',
			title : '当前状态操作员姓名'
		}, {
			field : 'operate_time',
			title : '当前状态操作时间'
		}, {
			field : 'operate_info',
			title : '操作内容'
		}, {
			field : 'extend_props',
			title : '备注'
		}, {
			field : 'remark',
			title : '扩展属性'
		}, {
			field : 'create_time',
			title : '入库时间'
		} ] ],
		frozenColumns : [[{
			field : 'ck',
			checkbox : true
		}, {
			field : 'order_code',
			title : '单据编码'
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
			'OrderProcessReport[order_code]' : $("#orderCode").val(),
			'OrderProcessReport[order_type]' : $("#orderType").combobox("getValue"),
			'OrderProcessReport[customer_id]' : $("#customerId").val(),
			'OrderProcessReport[warehouse_code]' : $("#warehouseCode").val(),
			'OrderProcessReport[process_status]' : $("#processStatus").combobox("getValue"),
			'OrderProcessReport[start_time]' : $("#startTime").datetimebox("getValue"),
			'OrderProcessReport[end_time]' : $("#endTime").datetimebox("getValue")
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
	var orderCode = $.trim($("#orderCode").val());
	var orderType = $.trim($("#orderType").combobox("getValue"));
	var customerId = $.trim($("#customerId").val());
	var warehouseCode = $.trim($("#warehouseCode").val());
	var processStatus = $.trim($("#processStatus").combobox("getValue"));
	var startTime = $.trim($("#startTime").datetimebox("getValue"));
	var endTime = $.trim($("#endTime").datetimebox("getValue"));
	
	if (orderCode == '' && customerId == '' && warehouseCode =='') {
		$.messager.show({
			title : '友情提示',
			msg : '单据号、货主ID和仓库编码查询条件不能都为空'
		});
		return false;
	}
	
	window.location.href = 'index.php?r=export/index&exportType=orderProcessReport&orderCode='
			+ orderCode
			+ '&orderType='
			+ orderType
			+ '&customerId='
			+ customerId
			+ '&warehouseCode'
			+ warehouseCode
			+ '&processStatus'
			+ processStatus
			+ '&startTime='
			+ startTime
			+ '&endTime=' 
			+ endTime;
}