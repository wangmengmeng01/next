$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '发货单下发明细',
		url : './index.php?r=outbound/deliveryOrderDetail/data&delivery_id=' + deliveryId,
		method : 'POST',
		rownumbers : true,
		height : $(window).height(),
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'delivery_id',
			title : '订单号',
			formatter : function(value, rowData, rowIndex) {
				return deliveryOrderCode;
			}
		}, {
			field : 'order_line_no',
			title : '单据行号'
		}, {
			field : 'source_order_code',
			title : '交易平台订单'
		}, {
			field : 'sub_source_order_code',
			title : '交易平台子订单编码'
		}, {
			field : 'customer_id',
			title : '货主编码'
		}, {
			field : 'item_code',
			title : '商品编码'
		},{
			field : 'item_id',
			title : '仓储系统商品编码'
		},{
			field : 'inventory_type',
			title : '库存类型'
		},{
			field : 'item_name',
			title : '商品名称'
		},{
			field : 'ext_code',
			title : '交易平台商品编码'
		},{
			field : 'plan_qty',
			title : '应发商品数量'
		},{
			field : 'retail_price',
			title : '零售价'
		},{
			field : 'actual_price',
			title : '实际成交价'
		},{
			field : 'discount_amount',
			title : '单件商品折扣金额'
		},{
			field : 'batch_code',
			title : '批次编码'
		},{
			field : 'product_date',
			title : '生产日期'
		},{
			field : 'expire_date',
			title : '过期日期'
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
				openExportExcel(deliveryId, deliveryOrderCode);
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
function openExportExcel(deliveryId, deliveryOrderCode) {
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
	if (deliveryId == '') {
		$.messager.show({
			title : '友情提示',
			msg : '非法操作'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=deliveryOrderDetail&delivery_id=' + deliveryId + '&delivery_order_code=' + deliveryOrderCode;
}
