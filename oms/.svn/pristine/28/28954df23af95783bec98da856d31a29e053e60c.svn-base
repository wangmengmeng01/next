$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '货主与ERP和WMS关系维护列表',
		url : './index.php?r=base/customerBind/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 50,
		pagination : true,
		pageSize : 50,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'customer_id',
			title : '货主ID'
		}, {
			field : 'erp_code',
			title : 'ERP编码'
		},{
			field : 'erp_api_url',
			title : 'ERP接口地址'
		},{
			field : 'erp_api_ver',
			title : 'ERP接口版本'
		}, {
			field : 'wms_code',
			title : 'WMS编码'
		}, {
			field : 'wms_api_url',
			title : 'WMS接口地址'
		}, {
			field : 'wms_api_ver',
			title : 'WMS接口版本'
		}, {
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
			field : 'operator_name',
			title : '录入人'
		}, {
			field : 'create_time',
			title : '录入时间'
		} ] ],
		toolbar : [ {
			id : 'add',
			title : '添加',
			text : '添加',
			iconCls : 'icon-add',
			disabled : false,
			handler : function() {
				openAddDlg();
			}
		}, {
			id : 'edit',
			title : '编辑',
			text : '编辑',
			iconCls : 'icon-edit',
			disabled : true,
			handler : function() {
				openEditDlg();
			}
		}, {
			id : 'del',
			title : '删除',
			text : '删除',
			iconCls : 'icon-cut',
			disabled : true,
			handler : function() {
				openDelDlg();
				setBtn();
			}
		}, {
			id : 'pushErp',
			title : '推送数据到ERP',
			text : '推送数据到ERP',
			iconCls : 'icon-redo',
			disabled : true,
			handler : function() {
				openPushErpDlg();
				setBtn();
			}
		}, {
			id : 'pushWms',
			title : '推送数据到WMS',
			text : '推送数据到WMS',
			iconCls : 'icon-redo',
			disabled : true,
			handler : function() {
				openPushWmsDlg();
				setBtn();
			}
		}  ],
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
		},
		onBeforeLoad : function() {
			if (operateFlag == 0 ) {
			    $('div.datagrid-toolbar').hide();
			}
		}
	})
})

function openAddDlg() {
	$("#dlg").dialog({
		title : '新增',
		width : 650,
		height : 400,
		closed : false,
		href : './index.php?r=base/customerBind/add',
		buttons : [ {
			id : 'save',
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				$.ydSubmit({
					url : './index.php?r=base/customerBind/add',
					params : $('#form_customerBind').serializeToJson(true),
					callback : function(data) {
						$.messager.show({
							title : '友情提示',
							msg : data.msg
						});
						if (data.status == 'ok') {
							$("#dlg").dialog("close");
							$("#dg").datagrid("reload");
						}
					},
					paramType : 'json',
					beforeSend : function() {
						return $("#form_customerBind").form('validate');
					} // 默认，一般不配置
				});
			}
		}, {
			id : 'cancel',
			text : '关闭',
			iconCls : 'icon-cancel',
			handler : function() {
				$('#dlg').dialog('close');
			}
		} ],
		onLoad : function() {
			$('#form_customerBind').form('reset');
		}
	});
}

function openEditDlg() {
	var rowData = $("#dg").datagrid('getSelected');
	$("#dlg").dialog({
		title : '修改',
		width : 650,
		height : 400,
		href : updateUrl.replace('uid', rowData.customer_id),
		buttons : [ {
			id : 'save',
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				$.ydSubmit({
					url : updateUrl.replace('uid', rowData.customer_id),
					params : $('#form_customerBind').serializeToJson(true),
					callback : function(data) {
						$.messager.show({
							title : '友情提示',
							msg : data.msg
						});
						if (data.status == 'ok') {
							$("#dlg").dialog("close");
							$("#dg").datagrid("reload");
						}
					},
					paramType : 'json',
					beforeSend : function() {
						return $("#form_customerBind").form('validate');
					}// 默认，一般不配置
				});
			}
		}, {
			id : 'cancel',
			text : '关闭',
			iconCls : 'icon-cancel',
			handler : function() {
				$('#dlg').dialog('close');
			}
		} ],
		onLoad : function() {
			$("#customerId").attr('readonly', 'readonly');
		}
	});
}

//推送货主和仓库信息给ERP
function openPushErpDlg() {
	var rowData = $("#dg").datagrid('getSelected');
	var msg = '确定要推送货主ID：'+rowData.customer_id+'和该货主对应的仓库信息给ERP？';
	$.messager.confirm("确认按钮", msg, function(r) {
		if (r) {
			$("#dlg").dialog({
				title : '推送数据到ERP',
				width : 400,
				height : 150,
				href : pushErpUrl							
			});
			$.post(pushErpUrl, {
				'customer_id' : rowData.customer_id
			}, function(data) {
				$.messager.show({
					title : '友情提示',
					msg : data.msg
				});
				$('#dlg').dialog('close');					
			}, 'json');
		}
	});
}

//推送货主、仓库、店铺和供应商信息给WMS
function openPushWmsDlg() {
	var rowData = $("#dg").datagrid('getSelected');
	var msg = '确定要推送货主ID：'+rowData.customer_id+'和该货主对应的仓库、店铺和供应商信息给WMS？';
	$.messager.confirm("确认按钮", msg, function(r) {
		if (r) {
			$("#dlg").dialog({
				title : '推送数据到WMS',
				width : 400,
				height : 150,
				href : pushErpUrl							
			});
			$.post(pushWmsUrl, {
				'customer_id' : rowData.customer_id
			}, function(data) {
				$.messager.show({
					title : '友情提示',
					msg : data.msg
				});
				$('#dlg').dialog('close');					
			}, 'json');
		}
	});
}


function openDelDlg() {
	var row = $("#dg").datagrid("getSelected");
	$.messager.confirm("确认按钮", "确认要删除吗？", function(r) {
		if (r) {
			$.post(deleteUrl, {
				'id' : row.customer_id
			}, function(data) {
				$.messager.show({
					title : '友情提示',
					msg : data.msg
				});
				if (data.status == 'ok') {
					$("#dg").datagrid("reload");
				}
			}, 'json');
		}
	});
	return false;

}

function setBtn() {
	var row = $("#dg").datagrid('getSelections');
	if (row.length == 1) {
		if (row[0].is_valid == 1) {
			$("#del").linkbutton("enable");
		} else {
			$("#del").linkbutton("disable");
		}
		$("#edit").linkbutton("enable");
		$("#pushErp").linkbutton("enable");
		$("#pushWms").linkbutton("enable");
	} else if (row.length > 1) {
		$("#edit").linkbutton("disable");
		$("#del").linkbutton("disable");
		$("#pushErp").linkbutton("disable");
		$("#pushWms").linkbutton("disable");
	} else if (row.length == 0) {
		$("#edit").linkbutton("disable");
		$("#del").linkbutton("disable");
		$("#pushErp").linkbutton("disable");
		$("#pushWms").linkbutton("disable");
	}

}
function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'CustomerBind[customer_id]' : $("#customerId").val(),
			'CustomerBind[erp_code]' : $("#erpCode").val(),
			'CustomerBind[wms_code]' : $("#wmsCode").val(),
			'CustomerBind[is_valid]' : $("#isValid").combobox('getValue')
		}
	})
}
