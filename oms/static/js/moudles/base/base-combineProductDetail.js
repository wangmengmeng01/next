$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '组合商品明细',
		url : './index.php?r=base/combineProductDetail/data&combine_id=' + combineId,
		method : 'POST',
		rownumbers : true,
		height : $(window).height(),
		pagination : true,
		pageSize : 20,
		columns : [ [ {
			field : 'combine_id',
			title : '组合商品的ERP编码',
			formatter : function(value, rowData, rowIndex) {
				return combineItemCode;
			}
		}, {
			field : 'item_code',
			title : '商品编码'
		}, {
			field : 'quantity',
			title : '组合商品中的该商品个数'
		}, {
			field : 'is_valid',
			title : '有效',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'1' : '有效',
					'0' : '无效'
				};
				return jsondata[value];
			}
		}, {
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
	if (combineId == '') {
		$.messager.show({
			title : '友情提示',
			msg : '非法操作'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=combineProductDetail&combine_id=' + combineId+ '&combine_item_code=' + combineItemCode;
}
