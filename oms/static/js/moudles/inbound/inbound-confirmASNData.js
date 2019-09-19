$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '入库单状态明细回传列表',
		url : './index.php?r=inbound/confirmASNData/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageList : [ 10 ],
		columns : [ [{
			field : 'order_type',
			title : '订单类型',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'PO' : '采购入库',
					'TR' : '调拨入库',
					'RS' : '销售退货入库',
					'IP' : '盘盈入库',
					'SCRK' : '奇门生产入库',
					'LYRK' : '奇门领用入库',
					'CCRK' : '奇门残次品入库',
					'CGRK' : '奇门采购入库',
					'DBRK' : '奇门调拨入库',
					'QTRK' : '奇门其他入库',
					'B2BRK' : '奇门B2B入库',
					'THRK' : '奇门退货入库',
					'HHRK' : '奇门换货入库'
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
			field : 'order_status',
			title : '订单状态',	
			formatter : function (value,rowData,rowIndex) {
				var jsondata = {
						'30' : '部分收货',
						'40' : '完全收货',
						'99' : '强制完成收货',
						'90' : '取消',
						'NEW' : '未开始处理',
						'ACCEPT' : '仓库接单',
						'PARTFULFILLED' : '部分收货完成',
						'FULFILLED' : '收货完成',
						'EXCEPTION' : '异常',
						'CANCELED' : '取消',
						'CLOSED' : '关闭',
						'REJECT' : '拒单',
						'CANCELEDFAIL' : '取消失败'
					};
				return jsondata[value];
			}
		},{
			field : 'order_desc',
			title : '状态描述'
		},{
			field : 'asn_creation_time',
			title : 'ASN创建时间'
		},{
			field : 'expected_arrive_time1',
			title : '预期到货时间'
		},{
			field : 'asn_reference2',
			title : '快递单号'
		},{
			field : 'asn_reference3',
			title : '平台单号或交易号'
		},{
			field : 'asn_reference4',
			title : '店铺名称'
		},{
			field : 'asn_reference5',
			title : '手机号码'
		},{
			field : 'pono',
			title : '原出库单号'
		},{
			field : 'i_contact',
			title : '退货联系人'
		},{
			field : 'issue_party_name',
			title : '平台旺旺号'
		},{
			field : 'country_of_origin',
			title : '原产国'
		},{
			field : 'country_of_destination',
			title : '目的国'
		},{
			field : 'place_of_loading',
			title : '装货地'
		},{
			field : 'place_of_discharge',
			title : '卸货地'
		},{
			field : 'placeof_delivery',
			title : '交货地'
		},{
			field : 'user_define1',
			title : '退货快递公司'
		},{
			field : 'user_define2',
			title : '退货快递单号'
		},{
			field : 'user_define3',
			title : '退货原因'
		},{
			field : 'user_define4',
			title : '订单下发方'
		},{
			field : 'user_define5',
			title : '退换货标识'
		},{
			field : 'supplier_code',
			title : '供应商编码'
		},{
			field : 'supplier_name',
			title : '供应商名称'
		},{
			field : 'carrier_id',
			title : '承运人ID'
		},{
			field : 'carrier_name',
			title : '承运人名称'
		}, {
			field : 'priority',
			title : '优先级'
		},{
			field : 'follow_up',
			title : '业务担当'
		},{
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
			field : 'oms_order_no',
			title : 'OMS订单号'
		}, {
			field : 'wms_order_no',
			title : 'WMS订单号'
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
	var order_no = '';
	if (rowData.oms_order_no != '') {
		order_no = rowData.oms_order_no;
	} else {
		order_no = rowData.wms_order_no;
	}
	addTab('tt', '入库单回传明细报表', deatilViewUrl.replace('uid', rowData.order_id).replace('uname', order_no).replace('reid', rowData.record_id).replace('createTime',rowData.create_time));
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

//查询
function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'ConfirmASNData[oms_order_no]' : $("#omsOrderNo").val(),
			'ConfirmASNData[wms_order_no]' : $("#wmsOrderNo").val(),
			'ConfirmASNData[order_type]' : $("#orderType").combobox('getValue'),
			'ConfirmASNData[customer_id]' : $("#customerId").val(),
			'ConfirmASNData[warehouse_code]' : $("#warehouseCode").val(),
			'ConfirmASNData[start_time]' : $("#startTime").datetimebox("getValue"),
			'ConfirmASNData[end_time]' : $("#endTime").datetimebox("getValue")
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
	var omsOrderNo = $.trim($("#omsOrderNo").val());
	var wmsOrderNo = $.trim($("#wmsOrderNo").val());
	var orderType = $.trim($("#orderType").combobox('getValue'));
	var customerId = $.trim($("#customerId").val());
	var warehouseCode = $.trim($("#warehouseCode").val());
	var startTime = $.trim($("#startTime").datetimebox("getValue"));
	var endTime = $.trim($("#endTime").datetimebox("getValue"));
	if (omsOrderNo == '' && wmsOrderNo == '' && customerId == '' && warehouseCode == '') {
		$.messager.show({
			title : '友情提示',
			msg : 'OMS订单号、WMS订单号、货主ID和仓库编码查询条件不能都为空'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=confirmAsnData&omsOrderNo='
			+ omsOrderNo
			+ '&wmsOrderNo='
			+ wmsOrderNo
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