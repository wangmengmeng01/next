$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '唯品会JIT拣货单明细',
		url : './index.php?r=outbound/vipJitPickDetail/data&pickNo='+pickNo,
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'stock',
			title : '商品拣货数量'
		}, {
			field : 'barcode',
			title : '商品条码'
		}, {
			field : 'art_no',
			title : '货号'
		}, {
			field : 'product_name',
			title : '商品名称'
		}, {
			field : 'size',
			title : '尺码'
		}, {
			field : 'jit_type',
			title : 'jit类型'
		}, {
			field : 'amount',
			title : '商品数量'
		}] ],
		frozenColumns : [[{
			field : 'ck',
			checkbox : true
		}]],
		toolbar : [ {
			id : 'exportExcel',
			title : '导出Excel',
			text : '导出Excel',
			iconCls : 'icon-export',
			handler : function() {
				openExportExcel();
			}
		} ],
		onBeforeLoad : function() {
			if (excelExportFlag == 0 ) {
			    $('div.datagrid-toolbar a').eq(1).hide();
			}
		}
	})
})



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

	if (pickNo == '') {
		$.messager.show({
			title : '友情提示',
			msg : '非法操作'
		});
		return false;
	}
	
	window.location.href = 'index.php?r=export/index&exportType=vipJitPickDetail&pickNo=' + pickNo;
}



