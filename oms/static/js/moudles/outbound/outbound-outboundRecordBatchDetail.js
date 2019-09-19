$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '发货单回传批次明细',
		url : './index.php?r=outbound/outboundRecordBatchDetail/data&detail_id=' + detailId,
		method : 'POST',
		rownumbers : true,
		height : $(window).height(),
		pagination : true,
		pageSize : 20,
		columns : [ [ {
			field : 'batch_code',
			title : '批次编码'
		}, {
			field : 'product_date',
			title : '商品生产日期'
		}, {
			field : 'expire_date',
			title : '商品过期日期'
		}, {
			field : 'produce_code',
			title : '生产批号'
		}, {
			field : 'inventory_type',
			title : '库存类型'
		},{
			field : 'actual_qty',
			title : '实收数量'
		},{
			field : 'create_time',
			title : '创建时间'
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
		onBeforeLoad : function() {
			if (excelExportFlag == 0 ) {
			    $('div.datagrid-toolbar').hide();
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
			msg : '没有需要导出的数据'
		});
		return false;
	}
	if (detailId == '') {
		$.messager.show({
			title : '友情提示',
			msg : '非法操作'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=outboundRecordBatchDetail&detail_id=' + detailId ;
}
