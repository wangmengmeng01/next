$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '发货单下发报表',
		url : './index.php?r=outbound/deliveryOrderCreate/data',
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
					'JYCK' : '一般交易出库单',
					'HHCK' : '换货出库单',
					'BFCK' : '补发出库单',
					'QTCK' : '其他出库单'
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
			field : 'pre_delivery_order_code',
			title : '原出库单号（ERP分配）'
		},{
			field : 'pre_delivery_order_id',
			title : '原出库单号（WMS分配）'
		},{
			field : 'order_flag',
			title : '订单标记'
		},{
			field : 'source_platform_code',
			title : '订单来源平台编码'
		},{
			field : 'source_platform_name',
			title : '订单来源平台名称'
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
			field : 'deliv_create_time',
			title : '发货单创建时间'
		},{
			field : 'place_order_time',
			title : '前台订单 (店铺订单) 创建时间 (下单时间)'
		},{
			field : 'pay_time',
			title : '订单支付时间'
		},{
			field : 'pay_no',
			title : '支付平台交易号'
		},{
			field : 'operator_code',
			title : '操作员 (审核员) 编码'
		},{
			field : 'operator_name',
			title : '操作员 (审核员) 名称'
		},{
			field : 'operate_time',
			title : '操作 (审核) 时间'
		},{
			field : 'shop_nick',
			title : '店铺名称'
		},{
			field : 'seller_nick',
			title : '卖家名称'
		},{
			field : 'buyer_nick',
			title : '买家昵称'
		},{
			field : 'total_amount',
			title : '订单总金额 (元)'
		},{
			field : 'item_amount',
			title : '商品总金额 (元)'
		},{
			field : 'discount_amount',
			title : '订单折扣金额 (元)'
		},{
			field : 'freight',
			title : '快递费用 (元)'
		},{
			field : 'ar_amount',
			title : '应收金额 (元) , 消费者还需要支付多少'
		},{
			field : 'got_amount',
			title : '已收金额 (元) , 消费者已经支付多少'
		},{
			field : 'service_fee',
			title : 'COD服务费'
		},{
			field : 'logistics_code',
			title : '物流公司编码'
		},{
			field : 'logistics_name',
			title : '物流公司名称'
		},{
			field : 'express_code',
			title : '运单号'
		},{
			field : 'logistics_area_code',
			title : '快递区域编码, 大头笔信息'
		},{
			field : 'schedule_type',
			title : '投递时延要求'
		},{
			field : 'schedule_day',
			title : '要求送达日期'
		},{
			field : 'schedule_start_time',
			title : '投递时间范围要求 (开始时间)'
		},{
			field : 'schedule_end_time',
			title : '投递时间范围要求 (结束时间)'
		},{
			field : 'delivery_type',
			title : '发货服务类型'
		},{
			field : 'sender_company',
			title : '发件人--公司名称'
		},{
			field : 'sender_name',
			title : '发件人--姓名',
			formatter : function(value, rowData, rowIndex) {
				return value.substr(0,1)+'***';
			}
		},{
			field : 'sender_zipcode',
			title : '发件人--邮编'
		},{
			field : 'sender_tel',
			title : '发件人--固定电话',
			formatter : function(value, rowData, rowIndex) {
				return value.substr(0,3)+'****'+value.substr(7,4);
			}
		},{
			field : 'sender_mobile',
			title : '发件人--移动电话',
			formatter : function(value, rowData, rowIndex) {
				return value.substr(0,3)+'****'+value.substr(7,4);
			}
		},{
			field : 'sender_email',
			title : '发件人--电子邮箱'
		},{
			field : 'sender_countrycode',
			title : '发件人--国家二字码'
		},{
			field : 'sender_province',
			title : '发件人--省份'
		},{
			field : 'sender_city',
			title : '发件人--城市'
		},{
			field : 'sender_area',
			title : '发件人--区域'
		},{
			field : 'sender_town',
			title : '发件人--村镇'
		},{
			field : 'sender_detail_address',
			title : '发件人--详细地址',
			formatter : function(value, rowData, rowIndex) {
				return value.substr(0,value.indexOf("区")+1)+'****路****号';
			}
		},{
			field : 'receiver_company',
			title : '收件人--公司名称'
		},{
			field : 'receiver_name',
			title : '收件人--姓名',
			formatter : function(value, rowData, rowIndex) {
				return value.substr(0,1)+'***';
			}
		},{
			field : 'receiver_zipcode',
			title : '收件人--邮编'
		},{
			field : 'receiver_tel',
			title : '收件人--固定电话',
			formatter : function(value, rowData, rowIndex) {
				if(value!==''){
				return value.substr(0,3)+'****'+value.substr(7,4);
				}
			}
		},{
			field : 'receiver_mobile',
			title : '收件人--移动电话',
			formatter : function(value, rowData, rowIndex) {
				return value.substr(0,3)+'****'+value.substr(7,4);
			}
		},{
			field : 'receiver_email',
			title : '收件人--电子邮箱'
		},{
			field : 'receiver_countrycode',
			title : '收件人--国家二字码'
		},{
			field : 'receiver_province',
			title : '收件人--省份'
		},{
			field : 'receiver_city',
			title : '收件人--城市'
		},{
			field : 'receiver_area',
			title : '收件人--区域'
		},{
			field : 'receiver_town',
			title : '收件人--村镇'
		},{
			field : 'receiver_detail_address',
			title : '收件人--详细地址',
			formatter : function(value, rowData, rowIndex) {
				return value.substr(0,value.indexOf("区")+1)+'****路****号';
			}
		},{
			field : 'is_urgency',
			title : '是否紧急, Y/N, 默认为N'
		},{
			field : 'invoice_flag',
			title : '是否需要发票, Y/N, 默认为N'
		},{
			field : 'insurance_flag',
			title : '是否需要保险, Y/N, 默认为N'
		},{
			field : 'insurance_type',
			title : '保险类型'
		},{
			field : 'insurance_amount',
			title : '保险金额'
		},{
			field : 'buyer_message',
			title : '买家留言'
		},{
			field : 'seller_message',
			title : '卖家留言'
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
		},{
			field : 'remark',
			title : '备注'
		},{
			field : 'create_time',
			title : '入库时间'
		} ] ],
		frozenColumns : [[{
			field : 'ck',
			checkbox : true
		}, {
			field : 'delivery_order_code',
			title : '出库单号'
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
		},{
			id : 'invoiceView',
			title : '查看发票',
			text : '查看发票',
			iconCls : 'icon-detail',
			disabled : true,
			handler : function() {
				openInvoiceView();
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
	addTab('tt', '发货单下发明细报表', deatilViewUrl.replace('uid', rowData.delivery_id).replace('uname', rowData.delivery_order_code), '');
}

//查看发票
function openInvoiceView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '发货单下发发票报表', invoiceViewUrl.replace('uid', rowData.delivery_id).replace('uname', rowData.delivery_order_code), '');
}

function setBtn() {
	var row = $("#dg").datagrid('getSelections');
	if (row.length == 1) {
		$("#deatilView").linkbutton("enable");
		$("#invoiceView").linkbutton("enable");
	} else if (row.length > 1) {
		$("#deatilView").linkbutton("disable");
		$("#invoiceView").linkbutton("disable");
	} else if (row.length == 0) {
		$("#deatilView").linkbutton("disable");
		$("#invoiceView").linkbutton("disable");
	}
}

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'DeliveryOrderCreate[delivery_order_code]' : $("#deliveryOrderCode").val(),
			'DeliveryOrderCreate[order_type]' : $("#orderType").combobox('getValue'),
			'DeliveryOrderCreate[customer_id]' : $("#customerId").val(),
			'DeliveryOrderCreate[warehouse_code]' : $("#warehouseCode").val(),
            'DeliveryOrderCreate[fx_flag]' : $("#fxFlag").combobox('getValue'),
			'DeliveryOrderCreate[start_time]' : $("#startTime").datetimebox("getValue"),
			'DeliveryOrderCreate[end_time]' : $("#endTime").datetimebox("getValue")
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
	var deliveryOrderCode = $.trim($("#deliveryOrderCode").val());
	var orderType = $.trim($("#orderType").combobox('getValue'));
	var customerId = $.trim($("#customerId").val());
	var warehouseCode = $.trim($("#warehouseCode").val());
    var fxFlag = $.trim($("#fxFlag").combobox('getValue'));
	var startTime = $.trim($("#startTime").datetimebox("getValue"));
	var endTime = $.trim($("#endTime").datetimebox("getValue"));
	if (deliveryOrderCode == '' && customerId == '' && warehouseCode =='') {
		$.messager.show({
			title : '友情提示',
			msg : '订单号、货主ID和仓库编码查询条件不能都为空'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=deliveryOrderCreate&deliveryOrderCode='
			+ deliveryOrderCode
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