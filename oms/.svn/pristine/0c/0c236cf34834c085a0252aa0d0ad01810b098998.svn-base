$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '发货单回传报表',
		url : './index.php?r=outbound/deliveryOrderRecord/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'order_type',
			title : '订单类型',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'JYCK' : '一般交易出库单',
					'HHCK' : '换货出库单',
					'BFCK' : '补发出库单',
					'QTCK' : '其他出库单'
				};
				return jsondata[value];
			}
		}, {
			field : 'customer_id',
			title : '货主ID'
		}, {
			field : 'warehouse_code',
			title : '仓库编码'
		},{
			field : 'delivery_order_id',
			title : '仓储系统出库单号'
		},{
			field : 'order_status',
			title : '出库单状态'
		},{
			field : 'out_biz_code',
			title : '外部业务编码'
		},{
			field : 'confirm_type',
			title : '支持出库单多次发货'
		},{
			field : 'order_confirm_time',
			title : '订单完成时间'
		},{
			field : 'operator_code',
			title : '当前状态操作员编码'
		},{
			field : 'operator_name',
			title : '当前状态操作员姓名'
		},{
			field : 'operate_time',
			title : '当前状态操作时间'
		},{
			field : 'create_time',
			title : '入库时间'
		} ] ],
		frozenColumns : [[{
			field : 'ck',
			checkbox : true
		}, {
			field : 'delivery_order_code',
			title : '出库单号'
		}]],
		toolbar : [ {
			id : 'deatilView',
			title : '查看明细',
			text : '查看明细',
			iconCls : 'icon-detail',
			disabled : true,
			handler : function() {
				openDeatilView();
			}
		}, {
			id : 'packageView',
			title : '查看包裹',
			text : '查看包裹',
			iconCls : 'icon-detail',
			disabled : true,
			handler : function() {
				openPackageView();
			}
		}, {
			id : 'invoiceView',
			title : '查看发票',
			text : '查看发票',
			iconCls : 'icon-detail',
			disabled : true,
			handler : function() {
				openInvoiceView();
			}
		}, {
			id : 'exportExcel',
			title : '导出Excel',
			text : '导出Excel',
			iconCls : 'icon-export',
			handler : function() {
				openExportExcel();
			}
		} ],
		onCheck : function() {
			setBtn();
		},
		onCheckAll : function() {
			setBtn();
		},
		onSelect : function() {
			setBtn();
		},
		onUnselect : function() {
			setBtn();
		},
		onUncheck : function() {
			setBtn();
		},
		onUncheckAll : function() {
			setBtn();
		},
		onSelectAll : function() {
			setBtn();
		},
		onUnselectAll : function() {
			setBtn();
		},
		onLoadSuccess : function() {
			setBtn();
		},
		onBeforeLoad : function() {
			if (excelExportFlag == 0 ) {
			    $('div.datagrid-toolbar a').eq(1).hide();
			}
		}
	})
})

// 查看明细
function openDeatilView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '发货单回传明细报表', deatilViewUrl.replace('uid', rowData.delivery_id).replace('uname', rowData.delivery_order_code), '');
}

//查看包裹
function openPackageView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '发货单回传包裹报表', packageViewUrl.replace('uid', rowData.delivery_id).replace('uname', rowData.delivery_order_code), '');
}

//查看发票
function openInvoiceView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '发货单回传发票报表', invoiceViewUrl.replace('uid', rowData.delivery_id).replace('uname', rowData.delivery_order_code), '');
}

function setBtn() {
	var row = $("#dg").datagrid('getSelections');
	if (row.length == 1) {
		$("#deatilView").linkbutton("enable");
		$("#packageView").linkbutton("enable");
		$("#invoiceView").linkbutton("enable");
	} else if (row.length > 1) {
		$("#deatilView").linkbutton("disable");
		$("#packageView").linkbutton("disable");
		$("#invoiceView").linkbutton("disable");
	} else if (row.length == 0) {
		$("#deatilView").linkbutton("disable");
		$("#packageView").linkbutton("disable");
		$("#invoiceView").linkbutton("disable");
	}
}

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'DeliveryOrderRecord[delivery_order_code]' : $("#deliveryOrderCode").val(),
			'DeliveryOrderRecord[order_type]' : $("#orderType").combobox('getValue'),
			'DeliveryOrderRecord[customer_id]' : $("#customerId").val(),
			'DeliveryOrderRecord[warehouse_code]' : $("#warehouseCode").val(),
			'DeliveryOrderRecord[start_time]' : $("#startTime").datetimebox("getValue"),
			'DeliveryOrderRecord[end_time]' : $("#endTime").datetimebox("getValue")
		}
	})
}

/**
 * 增加TAB
 */
function addTab(divid, title, href, id) {
	var tt = parent.$('#' + divid);
	var initalPath = href;
	var content = '<iframe id="' + id + '" scrolling="yes" frameborder="0"'
			+ 'src="' + initalPath
			+ '" style="width:100%; height:99%;"></iframe>';
	if (!tt.tabs('exists', title)) {
		tt.tabs('add', {
			title : title,
			content : content,
			border : false,
			fit : true,
			closable : true
		});
	} else {
		tt.tabs('select', title);
		refreshTab({
			divId : tt,
			tabTitle : title,
			url : initalPath
		});
	}
}

/**
 * 如果当前选项卡的title已经存在、则刷新当前的选项卡
 */
function refreshTab(cfg) {
	var refresh_tab = cfg.tabTitle ? cfg.divId.tabs('getTab', cfg.tabTitle)
			: cfg.divId.tabs('getSelected');
	if (refresh_tab && refresh_tab.find('iframe').length > 0) {
		var _refresh_ifram = refresh_tab.find('iframe')[0];
		var refresh_url = cfg.url ? cfg.url : _refresh_ifram.src;
		// _refresh_ifram.src = refresh_url;
		_refresh_ifram.contentWindow.location.href = refresh_url;
	}
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
	var deliveryOrderCode = $.trim($("#deliveryOrderCode").val());
	var orderType = $.trim($("#orderType").combobox('getValue'));
	var customerId = $.trim($("#customerId").val());
	var warehouseCode = $.trim($("#warehouseCode").val());
	var startTime = $.trim($("#startTime").datetimebox("getValue"));
	var endTime = $.trim($("#endTime").datetimebox("getValue"));
	if (deliveryOrderCode == '' && customerId == '' && warehouseCode =='') {
		$.messager.show({
			title : '友情提示',
			msg : '订单号、货主ID和仓库编码查询条件不能都为空'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=deliveryOrderRecord&deliveryOrderCode='
			+ deliveryOrderCode
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