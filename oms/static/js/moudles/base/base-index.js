$(function(){
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '供应商维护界面',
		url : './index.php?r=base/default/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 100,
		pagination : true,
		pageList : [10],
		columns : [[
			{field : 'ck', checkbox : true},
			{field : 'supplier_code', title : '供应商代码' }, 
			{field : 'supplier_name', title : '供应商名称' },
			{field : 'supplier_contacts', title : '联系人' },
			{field : 'supplier_contact', title : '联系方式' },
			{field : 'supplier_address', title : '联系地址' }, 
			{field : 'supplier_remark', title : '备注' }, 
			{field : 'soa_name', title : '录入人' },
			{field : 'create_time', title : '录入日期' }
		]],
		toolbar : [
		           {   id : 'add',
			           title : '添加',
			           text: '添加',  
			           iconCls : 'icon-add',
			           disabled : false,
			           handler : function() { 
					      openAddDlg();
			           }  
			       },
			       {   id : 'edit',
				       title : '编辑',
				       text: '编辑',  
				       iconCls : 'icon-edit',
				       disabled : true,
				       handler : function() {
						   openEditDlg();
				       }
				   },
			       {   id : 'del',
				       title : '删除',
				       text: '删除',  
				       iconCls : 'icon-cut',
				       disabled : true,
				       handler : function() {
						    openDelDlg();
							setBtn();
				       }
				   }
	    ],
		onCheck : function() {
             setBtn();
		},
        onCheckAll: function() {
        	setBtn();
        },
        onSelect: function() {
			setBtn();
        },
        onUnselect: function() {
        	setBtn();
        },
        onUncheck: function() {
        	setBtn();
        },
        onUncheckAll: function() {
        	setBtn();
        },
        onSelectAll: function() {
        	setBtn();
        },
        onUnselectAll: function() {
        	setBtn();
        },
		onLoadSuccess: function () {
			setBtn();
		}
	})
})
function openAddDlg() {
		 $("#dlg").dialog({
				title : '新增',   
				width : 600,    
				height : 500,    
				closed : false,
				href :  './index.php?r=base/default/add',
				buttons : [{
					id : 'save',
					text:'保存',
					iconCls : 'icon-save',
					handler:function() {	
					   $.ydSubmit({
						  url: './index.php?r=base/default/add',
						  formId: 'yw0',
						  callback: function(data) {
						       $.messager.alert('友情提示', data.msg);
							   $("#dlg").dialog("close");
							   $("#dg").datagrid("reload");
						  },
						  paramType: 'json',
						  beforeSend: function() {}		// 默认，一般不配置
					  });
					}
				},{
					id : 'cancel',
					text : '关闭',
					iconCls : 'icon-cancel',
					handler:function() {
					     $('#dlg').dialog('close');
					}
				}],
				onLoad : function() {
					$('#yw0').form('reset');
				}
		    });
}
function openEditDlg() {
	var rowData = $("#dg").datagrid('getSelected');
	$("#dlg").dialog({
			title : '修改',
			width : 600,
			height : 500,
			href : updateUrl.replace('uid', rowData.supplier_id),
			buttons : [{
					id : 'save',
					text:'保存',
					iconCls : 'icon-save',
					handler:function() {	
					   $.ydSubmit({
						  url:  updateUrl.replace('uid', rowData.supplier_id),
						  formId: 'yw0',
						  callback: function(data) {
						       $.messager.alert('友情提示', data.msg);
							   $("#dlg").dialog("close");
							   $("#dg").datagrid("reload");
						  },
						  paramType: 'json',
						  beforeSend: function() {}		// 默认，一般不配置
					  }); 
					}
				},{
					id : 'cancel',
					text : '关闭',
					iconCls : 'icon-cancel',
					handler:function() {
						$('#dlg').dialog('close');
					}
				}],
			onLoad : function() {
				$("#Supplier_type_id").attr('disabled',true);
			}
	});
}
function openDelDlg() {
	 var row = $("#dg").datagrid("getSelected");
	 $.messager.confirm(
		  "确认按钮", 
		  "确认要删除吗？",
		  function (r) {
			if(r) {
				$.post(deleteUrl,{'del_id':row.supplier_id},function(data){
					if( data.status == 'ok') {
						$("#dg").datagrid("reload");
					} else 
					{
						$.messager.show({
							 title : '友情提示',
							 msg   :  data.msg
						});
					}																	   
				},'json');
			}
	}); 
	return false;
	
}
function setBtn() {
	var row = $("#dg").datagrid('getSelections');
	if ( row.length == 1 ) {
		$("#edit").linkbutton("enable");
		$("#del").linkbutton("enable");
	} else if ( row.length > 1 )  {
		$("#edit").linkbutton("disable");
		$("#del").linkbutton("disable");
	} else if ( row.length == 0 ) {
		$("#edit").linkbutton("disable");
		$("#del").linkbutton("disable");
	}
	
}
function searchForm() {
	$("#dg").datagrid({
		queryParams: {
			'Supplier[type_id]' : $("#Supplier_type_id").val(),
			'Supplier[supplier_name]' : $("#Supplier_supplier_name").val(),
			'Supplier[supplier_code]' : $("#Supplier_supplier_code").val()
		}
	})
}