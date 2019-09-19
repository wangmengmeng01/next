$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '出库单明细回传列表',
		url : './index.php?r=outbound/confirmSOData/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'order_type',
			title : '订单类型',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'SO' : '销售出库',
					'TO' : '调拨出库',
					'RP' : '采购退货出库',
					'IL' : '盘亏出库',
					'OO' : '线下出库',
					'PTCK' : '奇门普通出库单（退仓）',
					'DBCK' : '奇门调拨出库',
					'B2BCK' : '奇门B2B出库',
					'QTCK' : '奇门其他出库'
				};
				return jsondata[value];
			}
		}, {
			field : 'customer_id',
			title : '货主ID'
		}, {
			field : 'warehouse_code',
			title : '仓库编码'
		},{
			field : 'order_time',
			title : '订单创建时间'
		},{
			field : 'delivery_no',
			title : '快递单号'
		},{
			field : 'expected_shipment_time1',
			title : '预期发货时间'
		},{
			field : 'required_delivery_time',
			title : '要求交货时间'
		},{
			field : 'asn_reference2',
			title : '平台订单号'
		},{
			field : 'so_reference3',
			title : '店铺名称'
		},{
			field : 'weight',
			title : '重量'
		},{
			field : 'consignee_id',
			title : '下单平台'
		},{
			field : 'consignee_name',
			title : '收货人名称'
		},{
			field : 'c_country',
			title : '国家代码'
		},{
			field : 'c_province',
			title : '省'
		},{
			field : 'c_city',
			title : '市'
		},{
			field : 'c_tel1',
			title : '手机号'
		},{
			field : 'c_tel2',
			title : '固话'
		},{
			field : 'c_zip',
			title : '邮编'
		},{
			field : 'c_mail',
			title : '邮箱'
		},{
			field : 'c_address1',
			title : '地址'
		},{
			field : 'user_define2',
			title : '退货原入库单号'
		},{
			field : 'user_define3',
			title : '平台发货仓库'
		},{
			field : 'user_define4',
			title : '订单下发方'
		},{
			field : 'user_define5',
			title : '客服备注'
		},{
			field : 'invoice_print_flag',
			title : '是否打印发票'
		},{
			field : 'remark',
			title : '备注（顾客留言）'
		},{
			field : 'h_edi_01',
			title : '支付方式'
		},{
			field : 'h_edi_02',
			title : '订单总价'
		}, {
			field : 'h_edi_03',
			title : '优惠金额'
		},{
			field : 'h_edi_04',
			title : '已付金额'
		},{
			field : 'h_edi_05',
			title : '是否货到付款'
		},{
			field : 'h_edi_06',
			title : '应付金额'
		},{
			field : 'h_edi_07',
			title : '支付宝交易号'
		},{
			field : 'h_edi_08',
			title : '是否保价'
		},{
			field : 'h_edi_09',
			title : '保价金额'
		},{
			field : 'h_edi_10',
			title : '运费'
		},{
			field : 'channel',
			title : '渠道'
		},{
			field : 'carrier_id',
			title : '承运商编码'
		},{
			field : 'carrier_name',
			title : '承运商名称'
		},{
			field : 'is_valid',
			title : '是否有效'
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
			id : 'packageView',
			title : '查看包裹信息',
			text : '查看包裹信息',
			iconCls : 'icon-detail',
			disabled : true,
			handler : function() {
				openPackageView();
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
	addTab('tt', '出单回传明细报表', deatilViewUrl.replace('uid', rowData.order_id).replace('uname', order_no).replace('reid', rowData.record_id).replace('createTime',rowData.create_time), '');
}

//查看包裹信息
function openPackageView() {
	var rowData = $("#dg").datagrid('getSelected');
	var order_no = '';
	if (rowData.oms_order_no != '') {
		order_no = rowData.oms_order_no;
	} else {
		order_no = rowData.wms_order_no;
	}
	addTab('tt', '出单回传包裹信息报表', packageViewUrl.replace('uid', rowData.order_id).replace('uname', order_no).replace('reid', rowData.record_id).replace('createTime',rowData.create_time), '');
}


function setBtn() {
	var row = $("#dg").datagrid('getSelections');
	if (row.length == 1) {
		$("#deatilView").linkbutton("enable");
		$("#packageView").linkbutton("enable");
	} else if (row.length > 1) {
		$("#deatilView").linkbutton("disable");
		$("#packageView").linkbutton("disable");
	} else if (row.length == 0) {
		$("#deatilView").linkbutton("disable");
		$("#packageView").linkbutton("disable");
	}
}

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'ConfirmSOData[oms_order_no]' : $("#omsOrderNo").val(),
			'ConfirmSOData[wms_order_no]' : $("#wmsOrderNo").val(),
			'ConfirmSOData[order_type]' : $("#orderType").combobox('getValue'),
			'ConfirmSOData[customer_id]' : $("#customerId").val(),
			'ConfirmSOData[warehouse_code]' : $("#warehouseCode").val(),
			'ConfirmSOData[start_time]' : $("#startTime").datetimebox("getValue"),
			'ConfirmSOData[end_time]' : $("#endTime").datetimebox("getValue")
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
	if (omsOrderNo == '' && wmsOrderNo == '' && customerId == '' && warehouseCode =='') {
		$.messager.show({
			title : '友情提示',
			msg : 'OMS订单号、WMS订单号、货主ID和仓库编码查询条件不能都为空'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=confirmSoData&omsOrderNo='
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