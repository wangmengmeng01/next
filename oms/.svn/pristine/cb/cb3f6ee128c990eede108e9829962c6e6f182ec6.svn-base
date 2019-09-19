$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '库存异动报表',
		url : './index.php?r=inventory/stockChangeReport/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'ck',
			checkbox : true
		}, {
			field : 'change_id',
			hidden : true,
			title : '异动id'
		}, {
			field : 'order_code',
			title : '引起异动的单据号'
		}, {
			field : 'order_type',
			title : '订单类型',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'JYCK' : '一般交易出库',
					'HHCK' : '换货出库',
					'BFCK' : '补发出库',
					'PTCK' : '普通出库',
					'DBCK' : '调拨出库',
					'QTCK' : '其他出库',
					'SCRK' : '生产入库',
					'LYRK' : '领用入库',
					'CCRK' : '残次品入库',
					'CGRK' : '采购入库',
					'DBRK' : '调拨入库',
					'QTRK' : '其他入库',
					'XTRK' : '销退入库',
					'HHRK' : '换货入库',
					'CNJG' : '仓内加工',
					'ZTTZ' : '状态调整',
					'JQRK' : '拒签入库'
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
			field : 'out_biz_code',
			title : '外部业务编码'
		}, {
			field : 'item_code',
			title : '商品编码'
		}, {
			field : 'item_id',
			title : '仓储系统商品ID'
		}, {
			field : 'inventory_type',
			title : '库存类型'
		}, {
			field : 'quantity',
			title : '盘盈盘亏商品变化量'
		}, {
			field : 'batch_code',
			title : '批次编码'
		}, {
			field : 'product_date',
			title : '商品生产日期'
		}, {
			field : 'expire_date',
			title : '商品过期日期'
		}, {
			field : 'produce_code',
			title : '生产批号'
		}, {
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
function openBatchView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '库存异动批次明细报表', batchViewUrl.replace('uid', rowData.change_id), '');
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

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'StockChangeReport[order_code]' : $("#orderCode").val(),
			'StockChangeReport[order_type]' : $("#orderType").combobox('getValue'),
			'StockChangeReport[customer_id]' : $("#customerId").val(),
			'StockChangeReport[warehouse_code]' : $("#warehouseCode").val(),
			'StockChangeReport[start_time]' : $("#startTime").datetimebox("getValue"),
			'StockChangeReport[end_time]' : $("#endTime").datetimebox("getValue")
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
	var orderCode = $.trim($("#orderCode").val());
	var orderType = $.trim($("#orderType").combobox('getValue'));
	var customerId = $.trim($("#customerId").val());
	var warehouseCode = $.trim($("#warehouseCode").val());
	var startTime = $.trim($("#startTime").datetimebox("getValue"));
	var endTime = $.trim($("#endTime").datetimebox("getValue"));
	if (orderCode == '' && customerId == '' && warehouseCode =='') {
		$.messager.show({
			title : '友情提示',
			msg : '单据号、货主ID和仓库编码查询条件不能都为空'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=stockChangeReport&orderCode='
			+ orderCode
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