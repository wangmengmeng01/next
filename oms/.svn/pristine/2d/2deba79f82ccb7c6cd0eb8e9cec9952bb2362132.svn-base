$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '物流公司维护界面',
		url : './index.php?r=base/logistics/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 50,
		pagination : true,
		pageSize : 50,
		columns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			field : 'logistics_code',
			title : '物流公司编码'
		}, {
			field : 'logistics_name',
			title : '物流公司名称'
		}, {
			field : 'contact_tel',
			title : '联系电话'
		}, {
			field : 'address',
			title : '联系地址'
		}, {
			field : 'remark',
			title : '备注'
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
		width : 600,
		height : 500,
		closed : false,
		href : './index.php?r=base/logistics/add',
		buttons : [ {
			id : 'save',
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				$.ydSubmit({
					url : './index.php?r=base/logistics/add',
					params : $('#form_logistics').serializeToJson(true),
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
						return $("#form_logistics").form('validate');
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
			$('#form_logistics').form('reset');
		}
	});
}
function openEditDlg() {
	var rowData = $("#dg").datagrid('getSelected');
	$("#dlg").dialog({
		title : '修改',
		width : 600,
		height : 500,
		href : updateUrl.replace('uid', rowData.logistics_id),
		buttons : [ {
			id : 'save',
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				$.ydSubmit({
					url : updateUrl.replace('uid', rowData.logistics_id),
					params : $('#form_logistics').serializeToJson(true),
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
						return $("#form_logistics").form('validate');
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
			$("#logisticsCode").attr('readonly', 'readonly');
		}
	});
}
function openDelDlg() {
	var row = $("#dg").datagrid("getSelected");
	$.messager.confirm("确认按钮", "确认要删除吗？", function(r) {
		if (r) {
			$.post(deleteUrl, {
				'id' : row.logistics_id
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
			'Logistics[logistics_name]' : $("#Logistics_logistics_name").val(),
			'Logistics[logistics_code]' : $("#Logistics_logistics_code").val(),
			'Logistics[is_valid]' : $("#Logistics_isValid").combobox('getValue')
		}
	})
}
