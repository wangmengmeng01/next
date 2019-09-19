$(function() {
	$("#dpg").datagrid({
		loadMsg : '正在加载中',
		title : '',
		url : './index.php?r=outbound/vipPushPick/data&vendorId='+vendorId+'&sellSite='+sellSite+'&platformCode='+platformCode+'&shopName='+shopName,
		method : 'POST',
		rownumbers : true,
		// height : $(window).height() - 120,
		pagination : false,
		pageSize : 20,
		columns : [ [  {
			field : 'whNo',
			title : '仓库'
		},{
			field : 'descr_c',
			title : '仓库名称'
		},{
			field : 'warehouse_code',
			title : '仓库编码'
		}] ],
		frozenColumns : [[{
			field : 'ck',
			checkbox : true
		}]],
		toolbar : [ {
                id : 'confirmPushPick',
                title : '确定',
                text : '确定',
                iconCls : 'icon-redo',
                handler : function() {
					$("#confirmPushPick").linkbutton("disable");
					isPush = true;
					confirmPushPick();
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
		onLoadSuccess : function(data) {
			if (data.rows.length > 0) {
				$('#dpg').datagrid("selectRow", 0);
			}
			setBtn();
		},
		onBeforeLoad : function() {

		}
	})
})

function setBtn() {
	if(isPush){
		return false;
	}
	var row = $("#dpg").datagrid('getSelections');
	if (row.length == 1) {
		$("#confirmPushPick").linkbutton("enable");
	} else if (row.length > 1) {
		$("#confirmPushPick").linkbutton("disable");
	} else if (row.length == 0) {
		$("#confirmPushPick").linkbutton("disable");
	}
}
/**
 * 确认下发拣货单
 *
 */
function confirmPushPick() {
	//要下发的拣货单
	var obj = $('#dlg').dialog('options');
	var queryParams = obj["queryParams"];
	//选择的仓库
	var rowData = $("#dpg").datagrid('getSelected');
    //下发拣货单相关接口调用
	var objData = {};
	var pick_nos = queryParams.split(","); //字符分割
	for (var i=0;i<pick_nos.length ;i++ )
	{
		objData.pick_no = pick_nos[i];
		objData.warehouse = rowData.warehouse_code;
        objData.vendor_id = rowData.vendor_id;
        var jsonData = JSON.stringify(objData);
		ajaxUrl('/vip_api.php?method=vip.pickorder.sync&selfreq='+selfreq+'&vendorid='+vendorId+'&warehouseid='+rowData.warehouse_code,'data='+jsonData);
	}

}

function ajaxUrl(url,dataStr='') {
    $.ajax({
        url:url,
        type:'post',
        data:dataStr,
        async : true, //默认为true 异步
        error:function(){
            //alert('error');
        },
        success:function(data){
        	var res = JSON.parse(data);
            $.messager.show({
                title:'友情提示',
                msg:res.message
            });
        }
    });
}




