$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '仓库库存量报表',
		url : './index.php?r=outbound/warehouseShipments/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 20,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'provincename',
			title : '省份'
		}, {
			field : 'branch_code',
			title : '网点编码'
		}, {
			field : 'branch_name',
			title : '网点名称'
		}, {
			field : 'wms_name',
			title : '使用系统'
		}, {
			field : 'warehouse_code',
			title : '仓库编码'
		}, {
			field : 'warehouse_name',
			title : '仓库名称'
		}, {
			field : 'shipments_qty',
			title : '发货量'
		}, {
            field : 'avg_qty',
            title : '日均发货量',
			formatter: function(value, rowData, rowIndex){
                if (value == '') {
                    return value;
				} else {
                    return Math.round(value);
				}
			}
        }, {
			field : 'rq',
			title : '时间'
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

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'WarehouseShipments[warehouse_name]' : $("#warehouseName").val(),
			'WarehouseShipments[warehouse_code]' : $("#warehouseCode").val(),
			'WarehouseShipments[gs_name]' : $("#gsName").val(),
			'WarehouseShipments[gs_code]' : $("#gsCode").val(),
			'WarehouseShipments[province]' : $("#province").combobox('getValue'),
			'WarehouseShipments[wms_name]' : $("#wmsName").combobox('getValue'),
			'WarehouseShipments[start_time]' : $("#startTime").datetimebox("getValue"),
			'WarehouseShipments[end_time]' : $("#endTime").datetimebox("getValue")
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
	var warehouseName = $.trim($("#warehouseName").val());
	var warehouseCode = $.trim($("#warehouseCode").val());
	var gsName = $.trim($("#gsName").val());
	var gsCode = $.trim($("#gsCode").val());
	var province = $.trim($("#province").combobox('getValue'));
	var wmsName = $.trim($("#wmsName").combobox('getValue'));
	var startTime = $.trim($("#startTime").datetimebox("getValue"));
	var endTime = $.trim($("#endTime").datetimebox("getValue"));
	
	window.location.href = 'index.php?r=export/index&exportType=warehouseShipments&warehouseName='
			+ warehouseName
			+ '&warehouseCode='
			+ warehouseCode
			+ '&gsName='
			+ gsName
			+ '&gsCode='
			+ gsCode
			+ '&province='
			+ province
			+ '&wmsName='
			+ wmsName
			+ '&startTime='
			+ startTime
			+ '&endTime=' 
			+ endTime;
}
