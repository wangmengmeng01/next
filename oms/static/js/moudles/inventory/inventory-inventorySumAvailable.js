$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '库存汇总可用量报表',
		method : 'POST',
		rownumbers : true,
		height : $(window).height()-200,
		pagination : true,
		pageSize : 10,
        pageList:[10, 20, 30,40,50],
		columns : [ [ {
			field : 'customer_id',
			title : '货主ID'
		}, {
			field : 'customer_name',
			title : '货主名称'
		}, {
			field : 'warehouse_code',
			title : '仓库编码'
		}, {
			field : 'warehouse_name',
			title : '仓库名称'
		}, {
			field : 'sku',
			title : 'SKU'
		}, {
			field : 'product_name',
			title : '商品名称'
		}, {
			field : 'availableQuantity',
			title : '可用量'
		}, {
			field : 'occupiedQuantity',
			title : '占用量'
		}, {
			field : 'onhandQuantity',
			title : '在库量'
		}] ],
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
		},
		loadFilter: function(data){
			if (data.status == 'error'){
				$.messager.show({
					title : '友情提示',
					msg : data.msg
				});
				return { total: 0, rows: [] };
			}
			return data; 
		}
	})
});

function searchForm() {
	if($("#customer_id").val() == '' && $("#customer_name").val() ==''){
		$.messager.show({
			title : '友情提示',
			msg : '货主ID跟货主名称请至少输入一个！'
		});
		return ;
	}
	if($("#warehouse_code").val() == '' && $("#warehouse_name").val() ==''){
		$.messager.show({
			title : '友情提示',
			msg : '仓库编码跟仓库名称请至少输入一个！'
		});
		return ;
	}
	if($("#sku").val() == '' && $("#product_name").val() ==''){
		$.messager.show({
			title : '友情提示',
			msg : 'SKU跟商品名称请至少输入一个！'
		});
		return ;
	}
	$("#dg").datagrid({
		url : './index.php?r=inventory/inventorySumAvailable/data',
		queryParams : {
			'inventorySumAvailable[customer_id]' : $("#customer_id").val(),
			'inventorySumAvailable[warehouse_code]' : $("#warehouse_code").val(),
			'inventorySumAvailable[sku]' : $("#sku").val(),	
			'inventorySumAvailable[customer_name]' : $("#customer_name").val(),	
			'inventorySumAvailable[warehouse_name]' : $("#warehouse_name").val(),
			'inventorySumAvailable[product_name]' : $("#product_name").val()		
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
			msg : '没有需要导出的数据'
		});
		return false;
	}
	var sku = $("#sku").val();
	skuArr=sku.split("\n"); 
	var customer_id = $.trim($("#customer_id").val());
	var warehouse_code = $.trim($("#warehouse_code").val());
	var product_name = $("#product_name").val();
	product_nameArr=product_name.split("\n");
	var customer_name = $.trim($("#customer_name").val());
	var warehouse_name = $.trim($("#warehouse_name").val());
	if (sku == '' && customer_id == '' && warehouse_code == '') {
		$.messager.show({
			title : '友情提示',
			msg : 'SKU、货主ID和仓库编码查询条件不能都为空'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=inventorySumAvailable&sku=' 
		+ skuArr 
		+ '&customer_id=' 
		+ customer_id
		+ '&warehouse_code='
		+ warehouse_code
		+ '&product_name='
		+ product_nameArr
		+ '&customer_name='
		+ customer_name
		+ '&warehouse_name='
		+ warehouse_name
		+ '&act=export';
}
