$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '出库单下发列表',
		url : './index.php?r=outbound/putSOData/data',
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
		}, {
			field : 'order_status',
			title : '订单状态',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'40' : '分配完成',
					'60' : '拣货完成',
					'63' : '复核完成',
					'99' : '订单完成',
					'90' : '取消',
					'NEW' : '未开始处理',
					'ACCEPT' : '仓库接单',
					'PARTDELIVERED' : '部分发货完成',
					'DELIVERED' : '发货完成',
					'EXCEPTION' : '异常',
					'CANCELED' : '取消',
					'CLOSED' : '关闭',
					'REJECT' : '拒单',
					'CANCELEDFAIL' : '取消失败'
				};
				return jsondata[value];
			}
		},{
            field : 'fx_flag',
            title : '分销订单标志',
            formatter : function(value, rowData, rowIndex) {
                var jsondata = {
                    '1' : '是',
                    '0' : '否'
                };
                return jsondata[value];
            }
        },{
            field : 'fx_customer',
            title : '客户来源'
        },{
            field : 'fx_branch_code',
            title : '网点编码'
        },{
            field : 'fx_branch_name',
            title : '网点名称'
        },{
            field : 'fx_is_depositpay',
            title : '是否押金支付',
            formatter : function(value, rowData, rowIndex) {
                var jsondata = {
                    '1' : '是',
                    '0' : '否'
                };
                return jsondata[value];
            }
        },{
            field : 'fx_distbt_code',
            title : '分拨中心编码'
        },{
            field : 'fx_distbt_name',
            title : '分拨中心名称'
        },{
			field : 'order_time',
			title : '订单创建时间'
		},{
			field : 'expected_shipment_time1',
			title : '预期发货时间'
		},{
			field : 'required_delivery_time',
			title : '要求交货时间'
		},{
			field : 'so_reference2',
			title : '平台订单号'
		},{
			field : 'so_reference3',
			title : '店铺名称'
		},{
			field : 'delivery_no',
			title : '快递单号'
		},{
			field : 'consignee_id',
			title : '下单平台'
		},{
			field : 'consignee_name',
			title : '收货人名称',
			formatter : function(value, rowData, rowIndex) {
				return value.substr(0,1)+'***';
			}
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
			title : '手机号',
			formatter : function(value, rowData, rowIndex) {
				return value.substr(0,3)+'****'+value.substr(7,4);
			}
		},{
			field : 'c_tel2',
			title : '固话',
			formatter : function(value, rowData, rowIndex) {
				if(value!==''){
				return value.substr(0,3)+'****'+value.substr(7,4);
				}
			}
		},{
			field : 'c_zip',
			title : '邮编'
		},{
			field : 'c_mail',
			title : '邮箱'
		},{
			field : 'c_address1',
			title : '地址',
			formatter : function(value, rowData, rowIndex) {
				return value.substr(0,value.indexOf("区")+1)+'****路****号';
			}
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
			title : '是否有效',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'1' : '有效',
					'0' : '无效'					
				};
				return jsondata[value];
			}
		}, {
			field : 'create_time',
			title : '入库时间'
		} ] ],
		frozenColumns : [[{
			field : 'ck',
			checkbox : true
		}, {
			field : 'order_no',
			title : '订单号'
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
		}, {
			id : 'importExcel',
			title : '导入Excel',
			text : '导入Excel',
			iconCls : 'icon-export',
			handler : function() {
				openImportExcel();
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
			if (excelImportFlag == 0 ) {
			    $('div.datagrid-toolbar a').eq(2).hide();
			}
		}
	})
})

// 查看明细
function openDeatilView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '出库单下发明细报表', deatilViewUrl.replace('uid', rowData.order_id).replace('uname', rowData.order_no), '');
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
			'PutSOData[order_no]' : $("#orderNo").val(),
			'PutSOData[order_type]' : $("#orderType").combobox('getValue'),
			'PutSOData[customer_id]' : $("#customerId").val(),
			'PutSOData[warehouse_code]' : $("#warehouseCode").val(),
            'PutSOData[fx_flag]' : $("#fxFlag").combobox('getValue'),
			'PutSOData[start_time]' : $("#startTime").datetimebox("getValue"),
			'PutSOData[end_time]' : $("#endTime").datetimebox("getValue")
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
	var orderNo = $.trim($("#orderNo").val());
	var orderType = $.trim($("#orderType").combobox('getValue'));
	var customerId = $.trim($("#customerId").val());
	var warehouseCode = $.trim($("#warehouseCode").val());
	var fxFlag = $.trim($("#fxFlag").combobox('getValue'));
	var startTime = $.trim($("#startTime").datetimebox("getValue"));
	var endTime = $.trim($("#endTime").datetimebox("getValue"));
	if (orderNo == '' && customerId == '' && warehouseCode == '') {
		$.messager.show({
			title : '友情提示',
			msg : '订单号、货主ID和仓库编码查询条件不能都为空'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=putSoData&orderNo='
			+ orderNo
			+ '&orderType='
			+ orderType
			+ '&customerId='
			+ customerId
			+ '&warehouseCode='
			+ warehouseCode
        	+ '&fxFlag='
        	+ fxFlag
			+ '&startTime='
			+ startTime
			+ '&endTime=' 
			+ endTime;
}

/**
 * 导入excel
 * 
 */
function openImportExcel() {
	//校验是否有导出的权限
	if (excelImportFlag == 0 ) {
		$.messager.show({
			title : '友情提示',
			msg : '您没有导入excel的权限！'
		});
		return false;
	}
	addTab('tt', '出库单下发导入', importLoadUrl, '');
}