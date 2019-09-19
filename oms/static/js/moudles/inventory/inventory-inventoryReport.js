$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '库存盘点通知报表',
		url : './index.php?r=inventory/inventoryReport/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'ck',
			checkbox : true
		}, {
			field : 'inventory_id',
			hidden : true,
			title : '盘点id'
		}, {
			field : 'check_order_code',
			title : '盘点单编码'
		}, {
			field : 'check_order_id',
			title : '仓储系统的盘点单编码'
		}, {
			field : 'customer_id',
			title : '货主ID'
		}, {
			field : 'warehouse_code',
			title : '仓库编码'
		}, {
			field : 'check_time',
			title : '盘点时间'
		}, {
			field : 'out_biz_code',
			title : '外部业务编码'
		}, {
			field : 'remark',
			title : '备注'
		}, {
			field : 'create_time',
			title : '创建时间'
		} ] ],
		toolbar : [ {
			id : 'detailView',
			title : '查看明细',
			text : '查看明细',
			iconCls : 'icon-detail',
			disabled : true,
			handler : function() {
				opendetailView();
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

// 查看批次明细
function opendetailView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '库存盘点明细报表', detailViewUrl.replace('uid', rowData.inventory_id).replace('uname', rowData.check_order_code), '');
}

function setBtn() {
	var row = $("#dg").datagrid('getSelections');
	if (row.length == 1) {
		$("#detailView").linkbutton("enable");
	} else if (row.length > 1) {
		$("#detailView").linkbutton("disable");
	} else if (row.length == 0) {
		$("#detailView").linkbutton("disable");
	}
}

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'InventoryReport[check_order_code]' : $("#checkOrderCode").val(),
			'InventoryReport[customer_id]' : $("#customerId").val(),
			'InventoryReport[warehouse_code]' : $("#warehouseCode").val(),
			'InventoryReport[start_time]' : $("#startTime").datetimebox("getValue"),
			'InventoryReport[end_time]' : $("#endTime").datetimebox("getValue")
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
	var checkOrderCode = $.trim($("#checkOrderCode").val());
	var customerId = $.trim($("#customerId").val());
	var warehouseCode = $.trim($("#warehouseCode").val());
	var startTime = $.trim($("#startTime").datetimebox("getValue"));
	var endTime = $.trim($("#endTime").datetimebox("getValue"));
	if (checkOrderCode == '' && customerId == '' && warehouseCode =='') {
		$.messager.show({
			title : '友情提示',
			msg : '盘点单编码、货主ID和仓库编码查询条件不能都为空'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=inventoryReport&checkOrderCode='
			+ checkOrderCode
			+ '&customerId='
			+ customerId
			+ '&warehouseCode='
			+ warehouseCode
			+ '&startTime='
			+ startTime
			+ '&endTime=' 
			+ endTime;
}