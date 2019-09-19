$(function() {
	$("#dg").datagrid({
		loadMsg : '正在加载中',
		title : '缺货单确认',
		url : './index.php?r=outbound/deliveryOrderShortage/data',
		method : 'POST',
		rownumbers : true,
		height : $(window).height() - 107,
		pagination : true,
		pageSize : 20,
		columns : [ [ {
			field : 'order_id',
			hidden: true,
			title : 'id'
		}, {
			field : 'customer_id',
			title : '货主编码'
		}, {
			field : 'warehouse_code',
			title : '原仓库'
		}, {
			field : 'mid_warehouse_code',
			title : '推荐仓库'
		}, {
			field : 'af_warehouse_code',
			title : '确认仓库'
		}, {
			field : 'order_type',
			title : '订单类型',
			formatter : function(value,rowData,rowIndex){
				var jsonData = {
						'JYCK':'交易出库',
						'HHCK':'换货出库',
						'BFCK':'补发出库',
						'QTCK':'其他出库',
						'jit':'普通JIT',
						'jit_4a':'JIT分销'
				};
				return jsonData[value];
			}
		}, {
			field : 'short_sku',
			title : '缺货商品'
		}, {
            field : 'short_sku_num',
            title : '缺货数量'
        }, {
			field : 'all_sku',
			title : '所有商品'
		}, {
			field : 'erp_create_time',
			title : '订单创建时间'
		}, {
			field : 'delivery_create_time',
			title : '发货单创建时间'
		}, {
			field : 'create_time',
			title : '缺货通知时间'
		} ] ],
		frozenColumns : [[{
			field : 'ck',
			checkbox : true
		}, {
			field : 'delivery_order_code',
			title : '发货单号'
		}]],
		toolbar : [ {
			id : 'allocateWh',
			title : '分配仓库',
			text : '分配仓库',
			iconCls : 'icon-redo',
			handler : function() {
				openAllocateWarehouse();
			}
		}, {
			id : 'exportExcel',
			title : '导出Excel',
			text : '导出Excel',
			iconCls : 'icon-export',
			handler : function() {
				openExportExcel();
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
			if (excelExportFlag == 0 ) {
			    $('div.datagrid-toolbar a').eq(1).hide();
			}
		},
	})
})

function setBtn() {
	var row = $("#dg").datagrid('getSelections');
}

function searchForm() {
	$("#dg").datagrid({
		queryParams : {
			'DeliveryOrderShortage[delivery_order_code]' : $("#deliveryOrderCode").val(),
			'DeliveryOrderShortage[customer_id]' : $("#customerId").val(),
			'DeliveryOrderShortage[warehouse_code]' : $("#warehouseCode").val(),
			'DeliveryOrderShortage[erp_create_start_time]' : $("#erpCreateStartTime").combobox('getValue'),
			'DeliveryOrderShortage[erp_create_end_time]' : $("#erpCreateEndTime").combobox('getValue'),
			'DeliveryOrderShortage[mid_warehouse_code]' : $("#midWarehouseCode").val(),
			'DeliveryOrderShortage[create_start_time]' : $("#createStartTime").datetimebox("getValue"),
			'DeliveryOrderShortage[create_end_time]' : $("#createEndTime").datetimebox("getValue")
		}
	})
}

/**
 * 分配仓库
 */
function openAllocateWarehouse(){
	var rowData = $("#dg").datagrid('getSelections');
	
	if (rowData.length<1) {	
		$.messager.show({
			title : '友情提示',
			msg : '请勾选数据！'
		});
		return false;
	} 
	var rowDataString = JSON.stringify(rowData);
	var url = allocateUrl+'&row_data='+rowDataString;
	
	var recommendedWarehouse;
	$("#grid").datagrid({
		height : 316,
		url : url,
		singleSelect:true,
		fitColumns:true,
		columns:[[{
                field:'order_no',
                title:'订单号',
                align:'center',
                width:250,
            }, {
                field:'customer_id',
                title:'货主编码',
                align:'center',
                width:100,
            }, {
                field:'delivery_warehouse_code',
                title:'发货仓库',
                align:'center',
                width:100,
            }, {
                field:'item_code',
                title:'SKU',
                align:'center',
                width:150,
            }, {
                field:'item_name',
                title:'商品名称',
                align:'center',
                width:150,
            }, {
                field:'quantity',
                title:'数量',
                align:'center',
                width:50,
            }, {
                field:'available_quantity',
                title:'可用数量',
                align:'center',
                width:50,
            }, {
                field:'occupied_quantity',
                title:'占用量',
                align:'center',
                width:50,
            }, {
                field:'onhand_quantity',
                title:'在库量',
                align:'center',
                width:50,
            },
        ]],
        frozenColumns : [[{
			field : 'ck',
			checkbox : true
		}]],
		onLoadSuccess : function(data){
			if (data.status == 'error') {
				$("#dlg").dialog({
            		title : '分配仓库',
            		width : 850,
            		height : 400,
            	});
        		$.messager.show({
					title : '友情提示',
					msg : data.msg
				});
        		$("#dlg").dialog("close");	
        	} else {
        		$("#dlg").dialog({
            		title : '分配仓库',
            		width : 850,
            		height : 400,
            		buttons : [ {
            			id : 'confirm_recommended_wh',
            			text : '确认',
            			iconCls : 'icon-save',
            			handler : function(){
            			    $(this).bind('click', function(){
            			    	confirmRecommendedWh(rowData[0].warehouse_code,rowData[0].order_type);
            			    });
            			}
            		}, {
            			id : 'cancel',
            			text : '取消',
            			iconCls : 'icon-cancel',
            			handler : function() {
            				$('#dlg').dialog('close');
            			}
            		} ],
            	});
            	rows = data.rows;
            	recommendedWarehouse = rows[0].delivery_warehouse_code;
                var grid = $(this);
                mergeCells(grid);
                
                $('#grid').datagrid("selectRow", 0);  
        	}
		},
        onClickRow : function(index,row){
            var orderNo = row.order_no;
            var customer = row.customer_id;
            var deliveryWarehouse = row.delivery_warehouse_code;
            var shortageData = $(this).datagrid('getRows');
            
            for(var i=0;i< shortageData.length;i++){
                if(shortageData[i]['order_no'] == orderNo &&
                   shortageData[i]['customer_id'] == customer &&
                   shortageData[i]['warehouse_code'] == deliveryWarehouse){
                	$(this).datagrid('selectRow',i);
                }
            }
        }, 
		rowStyler:function(index,row){
				return 'background-color:#F4F4F4;';
		},
	});
	
    $('#confirm').bind('click', function(){
    	var confirmData = $("#grid").datagrid('getSelections');
    	if (confirmData.length>1) {
    		$.messager.show({
    			title : '友情提示',
    			msg : '只能选择一个仓库！'
    		});
    		return false;
    	}
    	$.ydSubmit({
			url : confirmWarehouseUrl + '&recommend_warehouse_code='+recommendedWarehouse+
										'&confirm_warehouse_code='+confirmData.delivery_warehouse_code+
										'&order_no='+confirmData.order_no+
										'&customer_id='+confirmData.customer_id,
			params : $('#form_customer').serializeToJson(true),
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
				return $("#ShortageForm").form('validate');
			}// 默认，一般不配置
		});
    });
}

//合并单元格
function mergeCells(grid){
	var rows = grid.datagrid("getRows");
	var Ostart=0,Oend=0;
	if (rows.length < 0) {return;}
	$.each(rows,function(i,row){
		if (row['order_no'] == rows[Ostart]['order_no'] && 
			row['customer_id'] == rows[Ostart]['customer_id'] && 
			row['delivery_warehouse_code'] == rows[Ostart]['delivery_warehouse_code']) {
			Oend = i;
		} else {
			grid.datagrid('mergeCells',{
				index : Ostart,
				field : 'order_no',
				rowspan : Oend-Ostart+1
			});
			grid.datagrid('mergeCells',{
				index : Ostart,
				field : 'customer_id',
				rowspan : Oend-Ostart+1
			});
			grid.datagrid('mergeCells',{
				index : Ostart,
				field : 'delivery_warehouse_code',
				rowspan : Oend-Ostart+1
			});
			grid.datagrid('mergeCells',{
				index : Ostart,
				field : 'ck',
				rowspan : Oend-Ostart+1
			});
			Ostart = i;
			Oend = i;
		}
	});
	grid.datagrid('mergeCells',{
        index:Ostart,
        field:'order_no',
        rowspan:Oend-Ostart+1
    });
	grid.datagrid('mergeCells',{
		index : Ostart,
		field : 'customer_id',
		rowspan : Oend-Ostart+1
	});
	grid.datagrid('mergeCells',{
		index : Ostart,
		field : 'delivery_warehouse_code',
		rowspan : Oend-Ostart+1
	});
	grid.datagrid('mergeCells',{
		index : Ostart,
		field : 'ck',
		rowspan : Oend-Ostart+1
	});
}

/**
 * 确认仓库编码
 */
function confirmRecommendedWh(warehouseCode,orderType){
	var rowData = $('#grid').datagrid("getSelected");
	var recommendedData = $('#grid').datagrid("getData");
	var ordernos = rowData.order_no;
	$.ajax({
		type: "POST",
	    url: confirmWarehouseUrl+
    		'&customer_id='+rowData.customer_id+    
    		'&recommended_wh_code='+recommendedData.rows[0].delivery_warehouse_code+
    		'&confirm_wh_code='+rowData.delivery_warehouse_code+
    		'&order_code='+ordernos+
    		'&order_type='+orderType+
    		'&warehouse_code='+warehouseCode,
	    dataType:"json",
	    contentType: "application/json",	      			   
	    success : function(data){
	    	if (data.status == 'error') {
	    		$.messager.show({
					title : '友情提示',
					msg : data.msg
				});
	    	}
	    	$("#dlg").dialog("close");	
	    	$('#dg').datagrid('reload');
	    }
	});
	//需要将ordernos
}

/**
 * 发送订单
 */
function sendOrderData(){
	var rowData = $("#dg").datagrid("getSelections");
	var rowDataString = JSON.stringify(rowData);
	$.messager.confirm("确认按钮", "确认发送订单吗？", function(r) {
		if (r) {
			$.ajax({
				type: "POST",
			    url: sendOrderInfoUrl+'&rows_data='+rowDataString,
			    dataType:"json",
			    contentType: "application/json",	      			   
			    success : function(data){
			    	if (data.status == 'error') {
			    		$.messager.show({
							title : '友情提示',
							msg : data.msg
						});
			    	}
			    	$('#dg').datagrid('reload');
			    }	
			});
		}
	});
}

/**
 * 取消选中数据
 */
function cancelSelectData(){
	$("#dg").datagrid("clearSelections");
}

/**
 * 导出excel
 */
function openExportExcel() {
	//校验是否有导出的权限
	if (excelExportFlag == 0 ) {
		$.messager.show({
			title : '友情提示',
			msg : '您没有导出excel的权限！'
		});
		return false;
	}
	var rows = $("#dg").datagrid("getRows");
	if(rows[0]===undefined){
		$.messager.show({
			title : '友情提示',
			msg : '导出数据为空，请查询后导出'
		});
		return false;
	}
	var deliveryOrderCode = $.trim($("#deliveryOrderCode").val());
	var customerId = $.trim($("#customerId").val());
	var warehouseCode = $.trim($("#warehouseCode").val());
	var erpCreateStartTime = $.trim($("#erpCreateStartTime").datetimebox("getValue"));
	var erpCreateEndTime = $.trim($("#erpCreateEndTime").datetimebox("getValue"));
	var midWarehouseCode = $.trim($("#midWarehouseCode").val());
	var createStartTime = $.trim($("#createStartTime").datetimebox("getValue"));
	var createEndTime = $.trim($("#createEndTime").datetimebox("getValue"));
	
	if (customerId == '') {      
		$.messager.show({
			title : '友情提示',
			msg : '货主编码不能都为空!'
		});
		return false;
	}
	window.location.href = 'index.php?r=export/index&exportType=deliveryOrderShortage&deliveryOrderCode='
			+ deliveryOrderCode
			+ '&customerId='
			+ customerId
			+ '&warehouseCode='
			+ warehouseCode
			+ '&erpCreateStartTime='
			+ erpCreateStartTime
			+ '&erpCreateEndTime='
			+ erpCreateEndTime
			+ '&midWarehouseCode='
			+ midWarehouseCode
			+ '&createStartTime='
			+ createStartTime
			+ '&createEndTime=' 
			+ createEndTime;
}