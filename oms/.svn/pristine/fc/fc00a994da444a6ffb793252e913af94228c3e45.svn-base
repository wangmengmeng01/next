$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '仓内加工单确认报表',
		url : './index.php?r=inbound/storeProcessConfirm/data',
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
			field : 'process_order_id',
			title : '仓储系统加工单ID'
		}, {
			field : 'out_biz_code',
			title : '外部业务编码'
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
			field : 'order_complete_time',
			title : '加工单完成时间'
		}, {
			field : 'actual_qty',
			title : '实际作业总数量'
		}, {
			field : 'extend_props',
			title : '扩展属性'
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
	addTab('tt', '仓内加工单确认材料明细报表', materialViewUrl.replace('uid', rowData.process_id).replace('uname', rowData.process_order_code), '');
}

//查看商品明细
function openProductView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '仓内加工单确认商品明细报表', productViewUrl.replace('uid', rowData.process_id).replace('uname', rowData.process_order_code), '');
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
			'StoreProcessConfirm[process_order_code]' : $("#processOrderCode").val(),
			'StoreProcessConfirm[start_time]' : $("#startTime").datetimebox("getValue"),
			'StoreProcessConfirm[end_time]' : $("#endTime").datetimebox("getValue")
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
	var startTime = $.trim($("#startTime").datetimebox("getValue"));
	var endTime = $.trim($("#endTime").datetimebox("getValue"));
	
	window.location.href = 'index.php?r=export/index&exportType=storeProcessConfirm&processOrderCode='
			+ processOrderCode
			+ '&startTime='
			+ startTime
			+ '&endTime=' 
			+ endTime;
}