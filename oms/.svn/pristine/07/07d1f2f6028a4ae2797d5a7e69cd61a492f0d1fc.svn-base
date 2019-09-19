$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '货主与仓库关系维护列表',
		url : './index.php?r=base/customerWarehouseBind/data',
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
			field : 'code',
			title : '仓库编码'
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
            id : 'pushWms',
            title : '同步货主到WMS',
            text : '同步货主到WMS',
            iconCls : 'icon-redo',
            disabled : true,
            handler : function() {
                openPushWmsDlg();
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

//同步货主信息给WMS
function openPushWmsDlg() {
    var rowData = $("#dg").datagrid('getSelected');
    var msg = '确定要推送货主编码：'+rowData.customer_id+'及相关信息给WMS？';
    $.messager.confirm("确认按钮", msg, function(r) {
        if (r) {
            $("#dlg").dialog({
                title : '推送数据到WMS',
                width : 400,
                height : 150,
                href : pushWmsUrl
            });
            $.post(pushWmsUrl, {
                'customer_id' : rowData.customer_id,
                'code' : rowData.code
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

function openAddDlg() {
	$("#dlg").dialog({
		title : '新增',
		width : 400,
		height : 300,
		closed : false,
		href : './index.php?r=base/customerWarehouseBind/add',
		buttons : [ {
			id : 'save',
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				$.ydSubmit({
					url : './index.php?r=base/customerWarehouseBind/add',
					params : $('#form_customerWarehouseBind').serializeToJson(true),
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
						return $("#form_customerWarehouseBind").form('validate');
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
			$('#form_customerWarehouseBind').form('reset');
		}
	});
}

function openEditDlg() {
	var rowData = $("#dg").datagrid('getSelected');
	$("#dlg").dialog({
		title : '修改',
		width : 400,
		height : 300,
		href : updateUrl.replace('uid', rowData.relation_id),
		buttons : [ {
			id : 'save',
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				$.ydSubmit({
					url : updateUrl.replace('uid', rowData.relation_id),
					params : $('#form_customerWarehouseBind').serializeToJson(true),
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
						return $("#form_customerWarehouseBind").form('validate');
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

function openDelDlg() {
	var row = $("#dg").datagrid("getSelected");
	$.messager.confirm("确认按钮", "确认要删除吗？", function(r) {
		if (r) {
			$.post(deleteUrl, {
				'id' : row.relation_id
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
        $("#pushWms").linkbutton("enable");
	} else if (row.length > 1) {
		$("#edit").linkbutton("disable");
		$("#del").linkbutton("disable");
        $("#pushWms").linkbutton("disable");
	} else if (row.length == 0) {
		$("#edit").linkbutton("disable");
		$("#del").linkbutton("disable");
        $("#pushWms").linkbutton("disable");
	}

}
function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'CustomerWarehouseBind[customer_id]' : $("#customerId").val(),
			'CustomerWarehouseBind[code]' : $("#code").val(),
			'CustomerWarehouseBind[is_valid]' : $("#isValid").combobox('getValue')
		}
	})
}
