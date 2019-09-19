$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '唯品会JIT拣货单列表',
		url : './index.php?r=outbound/vipJitPickList/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'po_no',
			title : 'po编号'
		}, {
			field : 'co_mode',
			title : '合作模式'
		}, {
			field : 'sell_site',
			title : '唯品会仓库'
		}, {
			field : 'order_cate',
			title : '订单类别'
		}, {
			field : 'pick_num',
			title : '拣货数量'
		}, {
			field : 'create_time',
			title : '拣货单创建时间'
		}, {
			field : 'first_export_time',
			title : '首次导出时间'
		}, {
			field : 'export_num',
			title : '导出次数'
		}, {
			field : 'store_sn',
			title : '门店编码'
		}, {
			field : 'delivery_num',
			title : '发货数'
		}, {
			field : 'delivery_no',
			title : '运单号'
		}, {
			field : 'arrival_time',
			title : '要求到货时间'
		}, {
			field : 'carrier_name',
			title : '承运商'
		}, {
			field : 'storage_no',
			title : '入库单号'
		}, {
			field : 'confirm_time',
			title : '发货时间'
		},{
			field : 'status',
			title : '状态',
			// formatter : function(value, rowData, rowIndex) {
			// 	var jsondata = {
			// 			'1' : '已下发 ',
			// 			'2' : '已拣货 ',
			// 			'3' : '已发货 ',
			// 			'4' : '已送货'
			// 	};
			// 	return jsondata[value];
			// }
		}, {
			field : 'vendor_id',
			title : '供应商'
		}, {
			field : 'warehouse',
			title : '拣货仓'
		}, {
			field : 'send_time',
			title : '下发时间'
		} ] ],
		frozenColumns : [[{
			field : 'ck',
			checkbox : true
		}, {
			field : 'pick_no',
			title : '拣货单编号'
		}]],
		toolbar : [ {
			id : 'detailView',
			title : '查看明细',
			text : '查看明细',
			iconCls : 'icon-detail',
			disabled : true,
			handler : function() {
				openDetailView();
			}
		},{
			id : 'exportExcel',
			title : '导出Excel',
			text : '导出Excel',
			iconCls : 'icon-export',
			handler : function() {
				openExportExcel();
			}
		}, {
			id : 'toggleManualDownload',
			title : '手动下单',
			text : status==1 ? '关闭手动下单':'开启手动下单',
			toggle:true,
			iconCls : 'icon-export',
			handler : function() {
				toggleManualDownload();
			}
		}, {
			id : 'downloadPick',
			title : '下载拣货单',
			text : '下载拣货单',
			iconCls : 'icon-export',
			handler : function() {
				downloadPick();
			}
		},{
                id : 'pushPick',
                title : '下发拣货单',
                text : '下发拣货单',
                iconCls : 'icon-redo',
                handler : function() {
                    pushPick();
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
			'VipJitPickList[po_no]' : $("#poNo").val(),
			'VipJitPickList[pick_no]' : $("#pickNo").val(),
			'VipJitPickList[warehouse_code]' : $("#warehouseCode").val(),
			'VipJitPickList[delivery_no]' : $("#deliveryNo").val(),
			'VipJitPickList[storage_no]' : $("#storageNo").val(),
			'VipJitPickList[status]' : $("#status").combobox("getValue"),
			'VipJitPickList[vendor_id]' : $("#vendorId").val(),
			'VipJitPickList[brand_name]' : $("#brandName").val(),
			'VipJitPickList[sell_site]' : $("#sellSite").val()
		}
	})
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
	var poNo = $.trim($("#poNo").val());
	var pickNo = $.trim($("#pickNo").val());
	var warehouseCode = $.trim($("#warehouseCode").val());
	var deliveryNo = $.trim($("#deliveryNo").val());
	var storageNo = $.trim($("#storageNo").val());
	var status = $.trim($("#status").combobox("getValue"));
	var vendorId = $.trim($("#vendorId").val());
	var brandName = $.trim($("#brandName").val());
	var sellSite = $.trim($("#sellSite").val());

	// if (orderCode == '' && customerId == '' && warehouseCode =='') {
	// 	$.messager.show({
	// 		title : '友情提示',
	// 		msg : '单据号、货主ID和仓库编码查询条件不能都为空'
	// 	});
	// 	return false;
	// }
	
	window.location.href = 'index.php?r=export/index&exportType=vipJitPickList&poNo='
			+ poNo
			+ '&pickNo='
			+ pickNo
			+ '&warehouseCode='
			+ warehouseCode
			+ '&deliveryNo='
			+ deliveryNo
			+ '&storageNo='
			+ storageNo
			+ '&status='
			+ status
			+ '&vendorId='
			+ vendorId
			+ '&brandName='
			+ brandName
			+ '&sellSite='
			+ sellSite;
}

// 查看明细
function openDetailView() {
	var rowData = $("#dg").datagrid('getSelected');
	addTab('tt', '唯品会JIT拣货单明细', deatilViewUrl.replace('uid', rowData.id), '');
}

function setBtn() {
	var row = $("#dg").datagrid('getSelections');
	if (row.length == 1) {
		$("#detailView").linkbutton("enable");
        $("#pushPick").linkbutton("enable");
	} else if (row.length > 1) {
		$("#detailView").linkbutton("disable");
        $("#pushPick").linkbutton("enable");
	} else if (row.length == 0) {
		$("#detailView").linkbutton("disable");
        $("#pushPick").linkbutton("disable");
	}
	if(status==1) {
		$("#toggleManualDownload").linkbutton('select');
	} else {
		$("#toggleManualDownload").linkbutton('unselect');
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

function refreshParent() {
	refreshTab({
		divId : parent.$("#tt"),
		tabTitle : '唯品会JIT拣货单列表',
		url : vipJitPickListUrl
	})
}

/**
 * 手动下单
 *
 */
function toggleManualDownload() {
	$.ajax({
		url:manualDownloadUrl.replace('Status',status),
		type:'post',
		data:'',
		async : true, //默认为true 异步
		error:function(){
			//alert('error');
		},
		success:function(data){
			data = JSON.parse(data);
			if(data.code == 0){
				status = data.status;
				var text = data.status==1 ? '关闭手动下单': '开启手动下单';
				$("#toggleManualDownload").linkbutton({text:text});
			}
			$.messager.show({
				title : '友情提示',
				msg : data.msg
			});
			//alert('success');
		}
	});
}

/**
 * 下载采购单
 *
 */
function downloadPick() {
    //createPick
	var objData = {};
	objData.vendor_id = vendorId;
	var jsonData = JSON.stringify(objData);
    ajaxUrl('/vip_api.php?method=createPick&selfreq='+selfreq,'data='+jsonData);
    //getPickList
    // ajaxUrl('/','');
    //getPickDetail或getMultiPoPickDetail
    // ajaxUrl('/','');
}

function ajaxUrl(url,dataStr='') {
    $.ajax({
        url:url,
        type:'post',
        data:dataStr,
        async : true, //默认为true 异步
        error:function(){
            //alert('error');
        },
        success:function(data){
			$.messager.show({
				title : '友情提示',
				msg : data
			});
            //alert('success');
        }
    });
}

function pushPick() {
    var rowData = $("#dg").datagrid('getSelections');
    var pick_nos ='';
    for(var i=0;i<rowData.length;i++){
    	//限制送货仓和货主必须分别相同
		if(rowData[0].sell_site != rowData[i].sell_site || rowData[0].vendor_id != rowData[i].vendor_id){
			$.messager.show({
				title:'友情提示',
				msg:'一次只能选择相同货主、相同唯品会仓库的拣货单，重新勾选'
			});
			return false;
		}
		if((rowData[i].warehouse != '' && rowData[i].warehouse != null) || rowData[i].status =='已下发'){
			$.messager.show({
				title:'友情提示',
				msg:'已经下发的拣货单，不能重复下发，重新勾选'
			});
			return false;
		}
		pick_nos += rowData[i]['pick_no'];
		if(i<rowData.length-1) {
			pick_nos += ',';
		}
	}

    $("#dlg").dialog({
        title : '下发拣货单',
        width : 350,
        height : 400,
        href : pushPickUrl.replace('vendor_id',rowData[0].vendor_id).replace('sell_site',rowData[0].sell_site).replace('platform_code','VIP').replace('shop_name',rowData[0].store_sn),
		queryParams:pick_nos,
        buttons : [ {
            id : 'cancel',
            text : '关闭',
            iconCls : 'icon-cancel',
            handler : function() {
                $('#dlg').dialog('close');
            }
        } ],
        onLoad : function() {
            //
        },
		onClose:function () {
        	//为了让父tab中的js不失效
			refreshParent();
		}
    });
}



