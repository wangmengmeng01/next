$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '仓内加工单创建报表',
		url : './index.php?r=inbound/storeProcessCreate/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'process_id',
			hidden: true,
			title : '加工单id'
		}, {
			field : 'customer_id',
			title : '货主编码'
		}, {
			field : 'warehouse_code',
			title : '仓库编码'
		}, {
			field : 'order_type',
			title : '订单类型',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'CNJG' : '仓内加工单'
				};
				return jsondata[value];
			}
		}, {
			field : 'status',
			title : '订单状态'
		}, {
			field : 'order_create_time',
			title : '加工单创建时间'
		}, {
			field : 'plan_time',
			title : '计划加工时间'
		}, {
			field : 'service_type',
			title : '加工类型',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
						'1' : '仓内组合加工',
						'2' : '仓内组合拆分'
				};
				return jsondata[value];
			}
		}, {
			field : 'plan_qty',
			title : '成品计划数量'
		}, {
			field : 'extend_props',
			title : '扩展属性'
		}, {
			field : 'is_valid',
			title : '是否有效',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
						'0' : '无效',
						'1' : '有效'
				};
				return jsondata[value];
			}
		}, {
			field : 'remark',
			title : '备注'
		}, {
			field : 'create_time',
			title : '入库时间'
		} ] ],
		frozenColumns : [[{
			field : 'ck',
			checkbox : true
		}, {
			field : 'process_order_code',
			title : '加工单编码'
		}]],
		toolbar : [ {
			id : 'materialView',
			title : '查看材料明细',
			text : '查看材料明细',
			iconCls : 'icon-detail',
			disabled : true,
			handler : function() {
				openMaterialView();
			}
		},{
			id : 'productView',
			title : '查看商品明细',
			text : '查看商品明细',
			iconCls : 'icon-detail',
			disabled : true,
			handler : function() {
				openProductView();
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

// 查看材料明细
function openMaterialView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '仓内加工单创建材料明细报表', materialViewUrl.replace('uid', rowData.process_id).replace('uname', rowData.process_order_code), '');
}

//查看商品明细
function openProductView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '仓内加工单创建商品明细报表', productViewUrl.replace('uid', rowData.process_id).replace('uname', rowData.process_order_code), '');
}

function setBtn() {
	var row = $("#dg").datagrid('getSelections');
	if (row.length == 1) {
		$("#materialView").linkbutton("enable");
		$("#productView").linkbutton("enable");
	} else if (row.length > 1) {
		$("#materialView").linkbutton("disable");
		$("#productView").linkbutton("disable");
	} else if (row.length == 0) {
		$("#materialView").linkbutton("disable");
		$("#productView").linkbutton("disable");
	}
}

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'StoreProcessCreate[process_order_code]' : $("#processOrderCode").val(),
			'StoreProcessCreate[customer_id]' : $("#customerId").val(),
			'StoreProcessCreate[warehouse_code]' : $("#warehouseCode").val(),
			'StoreProcessCreate[start_time]' : $("#startTime").datetimebox("getValue"),
			'StoreProcessCreate[end_time]' : $("#endTime").datetimebox("getValue")
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
	var processOrderCode = $.trim($("#processOrderCode").val());
	var customerId = $.trim($("#customerId").val());
	var warehouseCode = $.trim($("#warehouseCode").val());
	var startTime = $.trim($("#startTime").datetimebox("getValue"));
	var endTime = $.trim($("#endTime").datetimebox("getValue"));
	
	if (processOrderCode == '' && customerId == '' && warehouseCode =='') {
		$.messager.show({
			title : '友情提示',
			msg : '单据号、货主ID和仓库编码查询条件不能都为空'
		});
		return false;
	}
	
	window.location.href = 'index.php?r=export/index&exportType=storeProcessCreate&processOrderCode='
			+ processOrderCode
			+ '&customerId='
			+ customerId
			+ '&warehouseCode'
			+ warehouseCode
			+ '&startTime='
			+ startTime
			+ '&endTime=' 
			+ endTime;
}