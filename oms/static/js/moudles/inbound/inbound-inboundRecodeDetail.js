$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '入库单回传明细报表',
		url : './index.php?r=inbound/inboundRecodeDetail/data&order_id='+ orderId + '&record_id=' + recordId + '&create_time=' + createTime,
		method : 'POST',
		rownumbers : true,
		height : $(window).height(),
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'ck',
			checkbox : true
		}, {
			field : 'detail_id',
			hidden: true,
			title : '明细id'
		}, {
			field : 'order_id',
			title : '订单号',
			formatter : function(value, rowData, rowIndex) {
				return orderNo;
			}
		}, {
			field : 'line_no',
			title : '行号'
		}, {
			field : 'sku',
			title : 'SKU001'
		}, {
			field : 'customer_id',
			title : '货主ID'
		},{
			field : 'line_status',
			title : '订单状态'
		},{
			field : 'line_desc',
			title : '订单状态描述'
		}, {
			field : 'expected_qty',
			title : '预期数量'
		},{
			field : 'received_qty',
			title : '实收数量'
		},{
			field : 'received_time',
			title : '收货时间'
		}, {
			field : 'total_price',
			title : '总价'
		},{
			field : 'lot_att01',
			title : '批次属性1'
		},{
			field : 'lot_att02',
			title : '批次属性2'
		},{
			field : 'lot_att03',
			title : '批次属性3'
		},{
			field : 'lot_att04',
			title : '批次属性4'
		},{
			field : 'lot_att05',
			title : '批次属性5'
		},{
			field : 'lot_att06',
			title : '批次属性6'
		},{
			field : 'lot_att07',
			title : '批次属性7'
		},{
			field : 'lot_att08',
			title : '批次属性8'
		},{
			field : 'lot_att09',
			title : '批次属性9'
		},{
			field : 'lot_att10',
			title : '批次属性10'
		},{
			field : 'lot_att11',
			title : '批次属性11'
		},{
			field : 'lot_att12',
			title : '批次属性12'
		}, {
			field : 'remark',
			title : '备注'
		}, {
			field : 'create_time',
			title : '入库时间'
		} ] ],
		toolbar : [ {
			id : 'deatilView',
			title : '查看批次明细',
			text : '查看批次明细',
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
				openExportExcel(orderId, orderNo);
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
			    $('div.datagrid-toolbar').hide();
			}
		}
	})
})

// 查看明细
function openDeatilView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '入库单回传明细批次明细报表', deatilViewUrl.replace('uid', rowData.detail_id), '');
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
function openExportExcel(orderId, orderNo) {
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
	if (orderId == '') {
		$.messager.show({
			title : '友情提示',
			msg : '非法操作'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=inboundRecordDetail&order_id=' + orderId + '&record_id=' + recordId + '&order_no=' + orderNo + '&create_time=' + createTime;
}
