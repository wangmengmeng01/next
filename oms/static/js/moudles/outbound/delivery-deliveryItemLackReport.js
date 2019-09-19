$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '发货单回传报表',
		url : './index.php?r=outbound/deliveryItemLackReport/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 20,
		columns : [ [ {
			field : 'deliv_lack_id',
			hidden: true,
			title : 'id'
		}, {
			field : 'customer_id',
			title : '货主ID'
		}, {
			field : 'warehouse_code',
			title : '仓库编码'
		}, {
			field : 'delivery_order_id',
			title : '仓库系统的发货单编码'
		}, {
			field : 'lack_create_time',
			title : '缺货回告创建时间'
		}, {
			field : 'out_biz_code',
			title : '外部业务编码'
		}, {
			field : 'create_time',
			title : '创建时间'
		} ] ],
		frozenColumns : [[{
			field : 'ck',
			checkbox : true
		}, {
			field : 'delivery_order_code',
			title : '发货单号'
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
	addTab('tt', '发货单缺货回传明细报表', deatilViewUrl.replace('uid', rowData.deliv_lack_id).replace('uname', rowData.delivery_order_code), '');
}


function setBtn() {
	var row = $("#dg").datagrid('getSelections');
	if (row.length == 1) {
		$("#deatilView").linkbutton("enable");
	} else if (row.length > 1) {
		$("#deatilView").linkbutton("disable");
	} else if (row.length == 0) {
		$("#deatilView").linkbutton("disable");
	}
}

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'DeliveryItemLackReport[delivery_order_code]' : $("#deliveryOrderCode").val(),
			'DeliveryItemLackReport[customer_id]' : $("#customerId").val(),
			'DeliveryItemLackReport[warehouse_code]' : $("#warehouseCode").val(),
			'DeliveryItemLackReport[start_time]' : $("#startTime").datetimebox("getValue"),
			'DeliveryItemLackReport[end_time]' : $("#endTime").datetimebox("getValue")
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
	window.location.href = 'index.php?r=export/index&exportType=deliveryItemLackReport&deliveryOrderCode='
			+ deliveryOrderCode
			+ '&customerId='
			+ customerId
			+ '&warehouseCode='
			+ warehouseCode
			+ '&startTime='
			+ startTime
			+ '&endTime=' 
			+ endTime;
}