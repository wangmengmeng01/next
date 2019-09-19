$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '唯品会JIT采购单列表',
		url : './index.php?r=outbound/vipJitPoList/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'co_mode',
			title : '合作模式'
		}, {
			field : 'sell_st_time',
			title : '档期开始销售时间'
		}, {
			field : 'sell_et_time',
			title : '档期结束销售时间'
		}, {
			field : 'stock',
			title : '虚拟总库存'
		}, {
			field : 'sales_volume',
			title : '销售数'
		}, {
			field : 'not_pick',
			title : '未拣货数'
		}, {
			field : 'trade_mode',
			title : '海淘档期新增贸易模式'
		}, {
			field : 'schedule_id',
			title : '档期号'
		}, {
			field : 'vendor_name',
			title : '供应商'
		}, {
			field : 'brand_name',
			title : '品牌'
		}, {
			field : 'sell_site',
			title : '唯品会仓库'
		}, {
			field : 'schedule_name',
			title : '档期名称'
		}, {
			field : 'po_start_time',
			title : 'po开始时间'
		}, {
			field : 'cooperation_no',
			title : '常态合作编码'
		}]],
		frozenColumns : [[{
			field : 'ck',
			checkbox : true
		}, {
			field : 'po_no',
			title : 'po编号'
		}]],
		toolbar : [ {
			id : 'exportExcel',
			title : '导出Excel',
			text : '导出Excel',
			iconCls : 'icon-export',
			handler : function() {
				openExportExcel();
			}
		},{
			id : 'getPoList',
			title : '下载采购单',
			text : '下载采购单',
			iconCls : 'icon-export',
			handler : function() {
				getPoList();
			}
		}],
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
			'VipJitPoList[po_no]' : $("#poNo").val(),
			'VipJitPoList[vendor_name]' : $("#vendorName").val(),
			'VipJitPoList[brand_name]' : $("#brandName").val(),
			'VipJitPoList[schedule_name]' : $("#scheduleName").val(),
			'VipJitPoList[sell_site]' : $("#sellSite").val()
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
	var vendorName = $.trim($("#vendorName").val());
	var brandName = $.trim($("#brandName").val());
	var scheduleName = $.trim($("#scheduleName").val());
	var sellSite = $.trim($("#sellSite").val());

	// if (orderCode == '' && customerId == '' && warehouseCode =='') {
	// 	$.messager.show({
	// 		title : '友情提示',
	// 		msg : '单据号、货主ID和仓库编码查询条件不能都为空'
	// 	});
	// 	return false;
	// }
	
	window.location.href = 'index.php?r=export/index&exportType=vipJitPoList&poNo='
			+ poNo
			+ '&vendorName='
			+ vendorName
			+ '&brandName='
			+ brandName
			+ '&scheduleName='
			+ scheduleName
			+ '&sellSite='
			+ sellSite;
}

/**
 * 下载采购单
 *
 */
function getPoList() {
	//getPoList
	var objData = {};
	objData.vendor_id = vendorId;
	var jsonData = JSON.stringify(objData);
	$.ajax({
		url:'/vip_api.php?method=getPoList&selfreq='+selfreq,
		type:'post',
		data:'data='+jsonData,
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