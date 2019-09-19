$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '客商档案接口日志',
		url : './index.php?r=interfaceLog/customerLog/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 80,
		pagination : true,
		pageSize : 50,
		columns : [ [{
			field : 'customer_type',
			title : '类型',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'OW' : '货主',
					'WH' : '仓库',
					'VE' : '供应商',
					'OT' : '店铺',
				};
				return jsondata[value];
			}
		},{
			field : 'method',
			title : '接口方法',	
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'putCustData' : '客商档案接口',
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
			field : 'customer_id',
			title : '客商档案ID'
		}]]	
	})
})

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'CustomerLog[customer_id]' : $("#customerId").val(),
			'CustomerLog[customer_type]' : $("#customerType").combobox('getValue'),
			'CustomerLog[return_status]' : $("#returnStatus").combobox('getValue'),
			'CustomerLog[start_time]' : $("#startTime").datetimebox("getValue"),
			'CustomerLog[end_time]' : $("#endTime").datetimebox("getValue")
		}
	})
}
