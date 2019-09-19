$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '发货单回传明细',
		url : './index.php?r=outbound/deliveryRecordDetail/data&delivery_id=' + deliveryId,
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
			hidden : true,
			title : '明细id'
		}, {
			field : 'delivery_id',
			title : '订单号',
			formatter : function(value, rowData, rowIndex) {
				return deliveryOrderCode;
			}
		}, {
			field : 'order_line_no',
			title : '单据行号'
		}, {
			field : 'order_source_code',
			title : '平台交易订单编码'
		}, {
			field : 'sub_source_order_code',
			title : '平台交易子订单编码'
		}, {
			field : 'customer_id',
			title : '货主编码'
		}, {
			field : 'item_code',
			title : '商品编码'
		},{
			field : 'item_id',
			title : '仓储系统商品编码'
		},{
			field : 'inventory_type',
			title : '库存类型'
		},{
			field : 'item_name',
			title : '商品名称'
		},{
			field : 'ext_code',
			title : '交易平台商品编码'
		},{
			field : 'plan_qty',
			title : '应发商品数量'
		},{
			field : 'actual_qty',
			title : '实发商品数量'
		},{
			field : 'batch_code',
			title : '批次编号'
		},{
			field : 'product_date',
			title : '商品生产日期'
		},{
			field : 'expire_date',
			title : '商品过期日期'
		},{
			field : 'produce_code',
			title : '生产批号'
		},{
			field : 'create_time',
			title : '创建时间'
		} ] ],
		toolbar : [ {
			id : 'batchView',
			title : '查看批次明细',
			text : '查看批次明细',
			iconCls : 'icon-detail',
			disabled : true,
			handler : function() {
				openBatchView();
			}
		}, {
			id : 'exportExcel',
			title : '导出Excel',
			text : '导出Excel',
			iconCls : 'icon-export',
			handler : function() {
				openExportExcel(deliveryId, deliveryOrderCode);
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

// 查看批次明细
function openBatchView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '发货单回传明细批次报表', batchViewUrl.replace('uid', rowData.detail_id), '');
}

function setBtn() {
	var row = $("#dg").datagrid('getSelections');
	if (row.length == 1) {
		$("#batchView").linkbutton("enable");
	} else if (row.length > 1) {
		$("#batchView").linkbutton("disable");
	} else if (row.length == 0) {
		$("#batchView").linkbutton("disable");
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
function openExportExcel(deliveryId, deliveryOrderCode) {
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
	if (deliveryId == '') {
		$.messager.show({
			title : '友情提示',
			msg : '非法操作'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=deliveryRecordDetail&delivery_id=' + deliveryId + '&delivery_order_code=' + deliveryOrderCode;
}
