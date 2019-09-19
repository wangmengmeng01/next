$(function() {
    $("#dg").datagrid({
        loadMsg : '正在加载中',
        title : '承运商信息维护界面',
        url : './index.php?r=base/cskMerchantDeploy/data',
        method : 'POST',
        rownumbers : true,
        height : $(window).height() - 50,
        pagination : true,
        pageSize : 50,
        columns : [ [ {
            field : 'ck',
            checkbox : true
        }, {
            field : 'vendor_code',
            title : '商家编码'
        }, {
            field : 'provider_code',
            title : '拼多多承运商编码'
        },{
            field : 'wms_provider_code',
            title : 'WMS承运商编码'
        },{
            field : 'update_time',
            title : '更新时间'
        },{
            field : 'create_time',
            title : '创建时间'
        } ] ],
        toolbar : [ {
            id : 'edit',
            title : '编辑',
            text : '编辑',
            iconCls : 'icon-edit',
            disabled : true,
            handler : function() {
                openEditDlg();
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


function openEditDlg() {
    var rowData = $("#dg").datagrid('getSelected');
    $("#dlg").dialog({
        title : '修改',
        width : 600,
        height : 550,
        href : updateUrl.replace('uid', rowData.id),
        buttons : [ {
            id : 'save',
            text : '保存',
            iconCls : 'icon-save',
            handler : function() {
                $.ydSubmit({
                    url : updateUrl.replace('uid', rowData.id),
                    params : $('#form_merchant').serializeToJson(true),
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
                        return $("#form_merchant").form('validate');
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
            $("#seller_id").attr('readonly', 'readonly');
        }
    });
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
            'vendor_code' : $("#vendor_code").val(),
            'provider_code' : $("#provider_code").val(),
            'wms_provider_code' : $("#wms_provider_code").val(),
        }
    })
}
