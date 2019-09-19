var prefix = "/vipkf/report/reportFluctuate";
$(function() {
	load();	
    //刷新按钮
     $("#refresh").click(function(){ $('#exampleTable').bootstrapTable('refresh'); }); 
});

function load() {
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/list", // 服务器数据的加载地址
						dataField: "data",//这是返回的json数组的key.默认好像是"rows".这里只有前后端约定好就行
					//	showRefresh : true,
					//	showToggle : true,
					//	showColumns : true,
						iconSize : 'outline',
						toolbar : '#exampleToolbar',
						striped : true, // 设置为true会有隔行变色效果
						dataType : "json", // 服务器返回的数据类型
						pagination : true, // 设置为true会在底部显示分页条
						// queryParamsType : "limit",
						// //设置为limit则会发送符合RESTFull格式的参数
						singleSelect : false, // 设置为true将禁止多选
						// contentType : "application/x-www-form-urlencoded",
						// //发送到服务器的数据编码类型
						pageSize : 50, // 如果设置了分页，每页数据条数
						pageNumber : 1, // 如果设置了分布，首页页码
						//search : true, // 是否显示搜索框
						showColumns : false, // 是否显示内容下拉框（选择显示的列）
						//showFooter: true, //统计列求和 sum、average等
						sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
						responseHandler:responseHandler,//请求数据成功后，渲染表格前的方法
						
						//grimm 双击触发的事件
	            		onDblClickRow: function (row) {
	            			edit(row.id);
	            		},
	            		
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
								startDate:$('#searchStartDate').val(),
								endDate:$('#searchEndDate').val(),
								regionId:$('#searchRegionId').val(),
								provinceid:$('#searchProvinceid').val(),
								cityid:$('#searchCityid').val(),
								bdType:$('#searchBdType').val(),
								showType:$('#searchShowType').val(),
								branchCode:$('#searchBranchCode').val()
								
							};
						},
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						/*
																{
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(value, row, index) {
										var e = '<a class="btn btn-primary btn-sm '+s_edit_h+'" href="#" mce_href="#" title="编辑" onclick="edit(\''
												+ row.reportFluctuateId
												+ '\')"><i class="fa fa-edit"></i></a> ';
										var d = '<a class="btn btn-warning btn-sm '+s_remove_h+'" href="#" title="删除"  mce_href="#" onclick="remove(\''
												+ row.reportFluctuateId
												+ '\')"><i class="fa fa-remove"></i></a> ';
										var f = '<a class="btn btn-success btn-sm" href="#" title="备用"  mce_href="#" onclick="resetPwd(\''
												+ row.reportFluctuateId
												+ '\')"><i class="fa fa-key"></i></a> ';
										return e + d ;
									}
								} ]*/
						frozenColumns : [ [
										                   {field : 'orderSum', title : '总单量', hidden : true},
										                   {field : 'orderAvg', title : '日均单量', hidden : true},
										                   {field : 'bigarea', title : '大区',  hidden : true},
										                   {field : 'provinceid', title : '省', hidden : true},
										                   {field : 'tmpXuanze', title : '区别选择', hidden : true}
										                   
							        		   ]
							                ] ,
						columns : [ 
							{field : 'bigarea', title :  '大区', align : 'center', 
									 formatter:function(value,row, index){
										if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
							       	  return "--";
							       	  else
							             return value;
									}
							 },
		                   {field : 'provincename', width : 100, title : '省',align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                      return value;
								}
  		                   },
		                   {field : 'cityname', width : 100, title : '市', align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                      return value;
								}
  		                   },
		                   {field : 'branchName', width : 100, title : '所属网点', align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                      return value;
								}
  		                   },
		                   {field : 'customerId', width : 100, title : '客户编码', align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                      return value;
								}
  		                   },
		                   {field : 'customerName', width : 100, title : '客户名称',align : 'center',
  		                	 formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                      return value;
								
  		                	  }
  		                   },
  		                   {field : 'sellerId', title :  '商家ID', align : 'center', width : 100},
						   {field : 'sellerName', title :  '店铺名称', align : 'center', width : 100},
						   	
		                   {field : 'customerSourceType', width : 100, title : '客户来源',align : 'center',
  		                	 formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                      return value;
								
		                	  }
  		                   },
                 {field : 'orderSum', width : 100, title : '上月揽件量', align : 'center',
 		                	  formatter:function(value,row, index){
								if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
			                	  return "--";
			                	  else
			                      return value;
							}
 		                   },
	                   {field : 'orderAvg', width : 100, title : '上月日均量',align : 'center',
 		                	  formatter:function(value,row, index){
 									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
 				                	  return "--";
 				                	  else
 				                      return value;
 								}
 		                   },
	                   {field : 'showPriceLevel', width : 100, title : '上月客户类别',align : 'center'},
	                   {field : 'borderSum', width : 100, title : '揽件量', align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                      return value;
								}
  		                   },
		                   {field : 'borderAvg', width : 100, title : '日均量',align : 'center',
  		                	 formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                      return value;
								}
  		                   },
	                   {field : 'showBPriceLevel', width : 100, title : '客户类别',align : 'center'}
							                   ]
					});
}
//请求成功方法
function responseHandler(result){
    var errcode = result.code;//在此做了错误代码的判断
    if(errcode != 200){
        alert("错误代码" + errcode);
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
    	total : result.data.total, //总页数,前面的key必须为"total"
        data :  result.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}
function query(){
	$("#exampleTable").bootstrapTable('destroy');
	load();
}
//导出excel
function exportExcel(action) {
	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly");//将input元素设置为readonly
	
	var url = prefix + "/exportCustExcel"+"?startDate="+$('#searchStartDate').val()+"&endDate="+$('#searchEndDate').val()+"&regionId="+$('#searchRegionId').val()+"&provinceid="+$('#searchProvinceid').val()
	  			+"&cityid="+$('#searchCityid').val()+"&bdType="+$('#searchBdType').val()+"&branchCode="+$('#searchBranchCode').val()+"&showType="+$('#searchShowType').val();
	  var xhr = new XMLHttpRequest();
	  xhr.open('GET', url, true);    // 也可以使用POST方式，根据接口
	  xhr.responseType = "blob";  // 返回类型blob
	  // 定义请求完成的处理函数，请求前也可以增加加载框/禁用下载按钮逻辑
	  xhr.onload = function () {
	    // 请求完成
	    if (this.status === 200) {
	      // 返回200
	      var blob = this.response;
	      var reader = new FileReader();
	      reader.readAsDataURL(blob);  // 转换为base64，可以直接放入a表情href
	      reader.onload = function (e) {
	        // 转换完成，创建一个a标签用于下载
	        var a = document.createElement('a');
	        var date = new Date();
	        var nowTime = date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()+' '+date.getHours()+date.getMinutes()+date.getSeconds();
	        a.download = '波动表-客户表-'+nowTime+'.xlsx';
	        a.href = e.target.result;
	        $("body").append(a);  // 修复firefox中无法触发click
	        a.click();
	        $(a).remove();
	      };
	      
	      layer.msg("下载成功");
	    }
	    
	    layer.closeAll('loading');
	    $('#exportExcelbtn').attr("disabled",false);
	    $('#exportExcelbtn').removeAttr("readonly");//去除input元素的readonly属性
	  };
	  // 发送ajax请求
	  xhr.send()
	}
	
//波动表-点击省打开对应的市级数据
function openShiTable(City){
	var bigarea = City.split(',')[0];
	var provinceid = City.split(',')[1];
	var cityid = City.split(',')[2];
	var startDate = City.split(',')[3];
	var endDate = City.split(',')[4];
	var showType="branch";
	var regionId = bigarea;
	layer.open({
		type : 2,
		title : '城市总表',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/reportFluctuateShi/'+regionId+"/"+provinceid+"/"+cityid+"/"+startDate+"/"+endDate+"/"+showType// iframe的url
	});
}
function add() {
	layer.open({
		type : 2,
		title : '添加',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/add' // iframe的url
	});
}
function edit(id) {
	if(s_edit_h=='hidden'){
		return;
    }
    layer.open({
		type : 2,
		title : '编辑',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/edit/' + id // iframe的url
	});
}
function remove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/remove",
			type : "post",
			data : {
				'reportFluctuateId' : id
			},
			success : function(r) {
				if (r.code==0) {
					layer.msg(r.msg);
					reLoad();
				}else{
					layer.msg(r.msg);
				}
			}
		});
	})
}

function resetPwd(id) {
}
function batchRemove() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要删除的数据");
		return;
	}
	layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = [];
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['reportFluctuateId'];
		});
		$.ajax({
			type : 'POST',
			data : {
				"ids" : ids
			},
			url : prefix + '/batchRemove',
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function() {

	});
}
function resetForm(){
	document.getElementById("searchFluctuateData").reset(); 
}
