$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '店铺维护界面',
		url : './index.php?r=base/cstmSdLog/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 50,
		pagination : true,
		pageList : [ 10 ],
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'customer_id',
			title : '客商ID'
		}, {
			field : 'customer_type',
			title : '客商类型',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
						'BI' : '结算人',
						'CA' :'承运人',
						'CO' :'收货人',
						'IP' : '司机',
						'OT' : '其他',
						'OW' : '货主',
						'VE' : '供应商',
						'WH' : '仓库'
				};
				return jsondata[value];
			}
		}, {
			field : 'send_erp_num',
			title : '推送ERP次数'
		}, {
			field : 'send_erp_status',
			title : '推送ERP状态',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'1' : '成功',
					'0' : '失败'
				};
				return jsondata[value];
			}
		}, {
			field : 'send_wms_num',
			title : '推送WMS次数'
		}, {
			field : 'send_wms_status',
			title : '推送WMS状态',
			formatter : function(value, rowData, rowIndex) {
				var jsondata = {
					'1' : '成功',
					'0' : '失败'
				};
				return jsondata[value];
			}
		}, {
			field : 'create_time',
			title : '录入日期'
		} ] ],
		toolbar : [ {
			id : 'del',
			title : '删除',
			text : '删除',
			iconCls : 'icon-cut',
			disabled : true,
			handler : function() {
				openDelDlg();
				setBtn();
			}
		} ],
		onCheck : function() {
			setBtn();
		},
		onCheckAll : function() {
			setBtn();
		},
		onSelect : function() {
			setBtn();
		},
		onUnselect : function() {
			setBtn();
		},
		onUncheck : function() {
			setBtn();
		},
		onUncheckAll : function() {
			setBtn();
		},
		onSelectAll : function() {
			setBtn();
		},
		onUnselectAll : function() {
			setBtn();
		},
		onLoadSuccess : function() {
			setBtn();
		}
	})
})

function setBtn() {
	var row = $("#dg").datagrid('getSelections');
	if (row.length == 1) {
		$("#edit").linkbutton("enable");
		$("#del").linkbutton("enable");
	} else if (row.length > 1) {
		$("#edit").linkbutton("disable");
		$("#del").linkbutton("disable");
	} else if (row.length == 0) {
		$("#edit").linkbutton("disable");
		$("#del").linkbutton("disable");
	}

}
function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'CstmSdLog[customer_id]' : $("#customerID").val(),
			'CstmSdLog[customer_type]' : $("#customerType").val()
		}
	})
}
