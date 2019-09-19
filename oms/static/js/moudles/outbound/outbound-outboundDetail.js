$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '出库单下发明细',
		url : './index.php?r=outbound/outboundDetail/data&order_id=' + orderId,
		method : 'POST',
		rownumbers : true,
		height : $(window).height(),
		pagination : true,
		pageSize : 20,
		columns : [ [{
			field : 'order_id',
			title : '订单号',
			formatter : function(value, rowData, rowIndex) {
				return orderNo;
			}
		}, {
			field : 'line_no',
			title : '行号'
		}, {
			field : 'customer_id',
			title : '货主ID'
		}, {
			field : 'sku',
			title : 'SKU'
		}, {
			field : 'qty_ordered',
			title : '订货数'
		}, {
			field : 'price',
			title : '价格'
		},{
			field : 'lot_att01',
			title : '批次属性1'
		},{
			field : 'lot_att02',
			title : '批次属性2'
		},{
			field : 'lot_att03',
			title : '批次属性3'
		},{
			field : 'lot_att04',
			title : '批次属性4'
		},{
			field : 'lot_att05',
			title : '批次属性5'
		},{
			field : 'lot_att06',
			title : '批次属性6'
		},{
			field : 'lot_att07',
			title : '批次属性7'
		},{
			field : 'lot_att08',
			title : '批次属性8'
		},{
			field : 'lot_att09',
			title : '批次属性9'
		},{
			field : 'lot_att10',
			title : '批次属性10'
		},{
			field : 'lot_att11',
			title : '批次属性11'
		},{
			field : 'lot_att12',
			title : '批次属性12'
		}, {
			field : 'remark',
			title : '备注'
		},{
			field : 'is_valid',
			title : '有效性',
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
		toolbar : [ {
			id : 'exportExcel',
			title : '导出Excel',
			text : '导出Excel',
			iconCls : 'icon-export',
			handler : function() {
				openExportExcel(orderId, orderNo);
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
function openExportExcel(orderId, orderNo) {
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
	if (orderId == '') {
		$.messager.show({
			title : '友情提示',
			msg : '非法操作'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=outboundDetail&order_id=' + orderId + '&order_no=' + orderNo;
}
