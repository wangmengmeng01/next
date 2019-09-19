$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '商品资料接口日志',
		url : './index.php?r=interfaceLog/productLog/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 50,
		columns : [ [{
			field : 'customer_id',
			title : '货主ID'
		},{
			field : 'method',
			title : '接口方法',	
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'putSKUData' : '商品资料',
					'singleitem.synchronize' : '奇门商品同步接口 ',
					'items.synchronize' : '奇门商品同步接口 (批量)',
					'beibei.outer.product.sync' : '贝贝商品同步接口'
				};
				return jsondata[value];
			}
		},{
			field : 'msg_id',
			title : '接口日志主id'
		},{
			field : 'api_url',
			title : '调用接口地址'
		},{
			field : 'return_status',
			title : '推送状态',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'1' : '成功',
					'0' : '失败'					
				};
				return jsondata[value];
			}
		},{
			field : 'request_param',
			title : '请求值',
			width : 500,
			formatter : function(value, rowData, rowIndex) {
				if (value == null || value == undefined) {
					value = '';
				} else {
					value = value.replace(/</g, '&lt;');
					value = value.replace(/>/g, '&gt;');
				}
				return value;
			}
		},{
			field : 'filter_result',
			title : '过滤错误',
			width : 200,
			formatter : function(value, rowData, rowIndex) {
				if (value == null || value == undefined) {
					value = '';
				} else {
					value = value.replace(/</g, '&lt;');
					value = value.replace(/>/g, '&gt;');
				}
				return value;
			}
		},{
			field : 'response_param',
			title : '响应值',
			width : 400,
			formatter : function(value, rowData, rowIndex) {
				if (value == null || value == undefined) {
					value = '';
				} else {
					value = value.replace(/</g, '&lt;');
					value = value.replace(/>/g, '&gt;');
				}
				return value;
			}
		}, {
			field : 'create_time',
			title : '入库时间'
		} ] ],
		frozenColumns : [[{
			field : 'sku',
			title : 'SKU'
		}]]	
	})
})

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'ProductLog[sku]' : $("#sku").val(),
			'ProductLog[customer_id]' : $("#customerId").val(),
			'ProductLog[return_status]' : $("#returnStatus").combobox('getValue'),
			'ProductLog[start_time]' : $("#startTime").datetimebox("getValue"),
			'ProductLog[end_time]' : $("#endTime").datetimebox("getValue")
		}
	})
}
