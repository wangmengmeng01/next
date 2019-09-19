$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '出库单回传包裹',
		url : './index.php?r=outbound/outboundRecordPackage/data&order_id='+ orderId + '&record_id=' + recordId + '&create_time=' + createTime,
		method : 'POST',
		rownumbers : true,
		height : $(window).height(),
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'ck',
			checkbox : true
		}, {
			field : 'package_id',
			hidden: true,
			title : '包裹id'
		}, {
			field : 'order_id',
			title : '订单号',
			formatter : function(value, rowData, rowIndex) {
				return orderNo;
			}
		}, {
			field : 'logistics_name',
			title : '物流公司名称'
		}, {
			field : 'express_code',
			title : '运单号'
		}, {
			field : 'package_code',
			title : '包裹编号'
		}, {
			field : 'length',
			title : '包裹长度 (厘米)'
		},{
			field : 'width',
			title : '包裹宽度 (厘米)'
		},{
			field : 'height',
			title : '包裹高度 (厘米)'
		},{
			field : 'weight',
			title : '包裹重量 (千克)'
		},{
			field : 'volume',
			title : '包裹体积 (升, L)'
		},{
			field : 'create_time',
			title : '创建时间'
		} ] ],
		toolbar : [ {
			id : 'materialView',
			title : '查看材料明细',
			text : '查看材料明细',
			iconCls : 'icon-detail',
			disabled : true,
			handler : function() {
				openMaterialView();
			}
		}, {
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
			    $('div.datagrid-toolbar').hide();
			}			
		}
	})
})

// 查看材料明细
function openMaterialView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '出库单回传包裹材料明细报表', materialViewUrl.replace('uid', rowData.package_id), '');
}

//查看商品明细
function openProductView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '出库单回传包裹商品明细报表', productViewUrl.replace('uid', rowData.package_id), '');
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
	window.location.href = 'index.php?r=export/index&exportType=outboundRecordPackage&order_id=' + orderId + '&order_no=' + orderNo + '&record_id=' + recordId + '&create_time=' +createTime;
}
