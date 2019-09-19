$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '客商档案推送列表',
		url : './index.php?r=base/customerSendLog/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 50,
		pagination : true,
		pageSize : 50,
		columns : [ [ {
			field : 'customer_id',
			title : '客商档案ID'
		}, {
			field : 'customer_type',
			title : '类型',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'OW' : '货主',
					'WH' : '仓库',
					'VE' : '供应商',
					'OT' : '店铺'
				};
				return jsondata[value];
			}
		},{
			field : 'send_erp_status',
			title : 'ERP推送状态',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'1' : '成功',
					'0' : '失败'
				};
				return jsondata[value];
			}
		},{
			field : 'send_erp_num',
			title : 'ERP推送次数'
		}, {
			field : 'erp_error_msg',
			title : 'ERP推送返回编码'
		}, {
			field : 'erp_error_msg',
			title : 'ERP推送返回编码描述'
		}, {
			field : 'send_wms_status',
			title : 'WMS推送状态',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'1' : '成功',
					'0' : '失败'
				};
				return jsondata[value];
			}
		}, {
			field : 'send_wms_num',
			title : 'WMS推送次数'
		}, {
			field : 'wms_error_code',
			title : 'WMS推送返回编码'
		}, {
			field : 'wms_error_msg',
			title : 'WMS推送返回编码描述'
		}, {
			field : 'create_time',
			title : '首次推送时间'
		}, {
			field : 'modify_time',
			title : '最后推送时间'
		} ] ]
	})
})

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'CustomerSendLog[customer_id]' : $("#customerId").val(),
			'CustomerSendLog[customer_type]' : $("#customerType").combobox('getValue'),
			'CustomerSendLog[send_erp_status]' : $("#sendErpStatus").combobox('getValue'),
			'CustomerSendLog[send_wms_status]' : $("#sendWmsStatus").combobox('getValue')
		}
	})
}
