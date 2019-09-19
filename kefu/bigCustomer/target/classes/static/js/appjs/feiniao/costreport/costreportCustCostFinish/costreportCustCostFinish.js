var prefix = "/vipkf/costreport/costreportCustCostFinish";
var prefixForward = "/vipkf/costreport/costreportOrderCostFinish";
var limit = 10;
var offset = 0;
$(function() {
	load();
	
	//上传excel按钮
/*    layui.use('upload', function () {
        var upload = layui.upload;
        //执行实例
        var uploadInst = upload.render({
            elem: '#uploadbtn', //绑定元素
            url: prefix+'/importExcel', //上传接口
            size: 1000,
            accept: 'file',
            exts: 'xls|xlsx',
            done: function (r) {
            	layer.closeAll('loading');
            	$('#uploadbtn').attr("disabled",false);
            	$('#uploadbtn').removeAttr("readonly");//去除input元素的readonly属性
                layer.msg(r.msg);
                reLoad();
            },
            error: function (r) {
                layer.msg(r.msg);
            },
            before: function(obj){ 
            	layer.load(1);
            	$('#uploadbtn').attr("disabled",true);
            	$('#uploadbtn').attr("readonly","readonly")//将input元素设置为readonly  
            }
        });
    });	*/
    
    //刷新按钮
     $("#refresh").click(function(){ $('#exampleTable').bootstrapTable('refresh'); }); 
});

function load() {
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/list", // 服务器数据的加载地址
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
						pageSize : 10, // 如果设置了分页，每页数据条数
						pageNumber : 1, // 如果设置了分布，首页页码
						//search : true, // 是否显示搜索框
						showColumns : false, // 是否显示内容下拉框（选择显示的列）
						//showFooter: true, //统计列求和 sum、average等
						sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
						dataField: "data",//这是返回的json数组的key.默认好像是"rows".这里只有前后端约定好就行
						responseHandler:responseHandler,//请求数据成功后，渲染表格前的方法
						//grimm 双击触发的事件
	            		onDblClickRow: function (row) {
	            			edit(row.id);
	            		},
	            		
						queryParams : function(params) {
							$('#limitvalue').val(params.limit);
							$('#offsetvalue').val(params.offset);
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
								accountDt:$('#accountDt').val(),
								customerId:$('#customerId').val(),
								startProvinceId:$('#startProvinceId').val(),
								endProvinceId:$('#endProvinceId').val()
					           // name:$('#searchName').val(),
					           // username:$('#searchName').val()
							};
						},
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						columns:[[
						          //{"title":"","colspan":1},
						          {"title":"","colspan":2},
						          {"title":"省份",align:'center',"colspan":2},
						          {"title":"业务量",align:'center',"colspan":2},
						          {"title":"业务支出",align:'center',"colspan":6},
		      					  {"title":"运营支出",align:'center',"colspan":7}],[
							//{field:'record_id',title:'数据记录主键ID',},
							{field:'customerId',title:'客户编码'},
							{field:'customerName',title:'客户名称'},
							{field:'startProvinceName',title:'始发省'},
							{field:'endProvinceName',title:'目的省'},
							{field:'orderSum',title:'票数',
								formatter:function(value,row, index){	
									if(row.startProvinceName!='首行合计')
//				                	  return "<a href='javascript:void(0)' onclick='openOrderCost("+index+");'>"+row.order_sum+"</a>";
				                	  return "<a href='javascript:void(0)' style='color:blue;' onclick='openOrderCost(\""+row.customerSourceType+","+row.branchCode+","+row.customerId+","+row.accountDt+","+row.startProvinceId+","+row.endProvinceId+"\");'>"+row.orderSum+"</a>";
								     	  else
				                      return value;
							
								}},
							{field:'weight',title:'重量'},
							{field:'tsfFee',title:'中转费'},
							{field:'deliveryFee',title:' 派费'},
							{field:'deliveryAdditionalWeightFee',title:'续重派费'},
							{field:'deliveryBalanceFee',title:'平衡派费'},
							{field:'shipmentFee',title:'面单费'},
							{field:'scanFee',title:'扫描费'},
							
							{field:'peopleCost',title:'人力成本'},
							{field:'materielCost',title:'物料费'},
							{field:'tsfCost',title:'运输成本'},
							{field:'hhCost',title:'回扣'},
							{field:'taxesCost',title:'税金'},
							{field:'packingCharge',title:'包仓费'},
							{field:'otherCost',title:'其他费用'},
							{field:'startProvinceId',title:'始发省id',visible:false},
							{field:'endProvinceId',title:'目的省id',visible:false}
							/* {field:'start_account_dt',title:'开始日期',},
							{field:'end_account_dt',title:'结束日期',},
							{field:'branch_code',title:'网点编码',},
							{field:'branch_name',title:'网点名称',},
							{field:'customer_source_type',title:'客户来源',}, */
						]]
					});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}


function query() {
	 
	   
	//$('#exampleTable').bootstrapTable('refresh');
	$("#exampleTable").bootstrapTable('destroy');
	load();
}

//请求成功方法
function responseHandler(result){
    var errcode = result.code;//在此做了错误代码的判断
    if(errcode != 200){
        //alert("错误代码" + errcode);
    	layer.msg('请勿重复请求'); 
        return;
    }
    //如果没有错误则返回数据，渲染表格
    return {
    	total : result.data.total, //总页数,前面的key必须为"total"
        data :  result.data.rows //行数据，前面的key要与之前设置的dataField的值一致.
    };
}
function openOrderCost(param){
	var customerSourceType  =param.split(',')[0];
	var branchCode  = param.split(',')[1];
	var customerId  = param.split(',')[2];
	var accountDt  = param.split(',')[3];
	var startProvinceId  = param.split(',')[4];
	var endProvinceId  = param.split(',')[5];
	
	if(customerId !=''&& customerId!=null && accountDt !='' && accountDt!=null
    && branchCode !='' && branchCode!=null && customerSourceType !='' && customerSourceType!=null
    && customerSourceType !='null' && branchCode!='null'
    && customerId !='null' && accountDt!='null'&&
    startProvinceId !=''&&startProvinceId !=null&&startProvinceId !='null'&&
    endProvinceId !=''&&endProvinceId !=null&&endProvinceId !='null'){
	var index =layer.open({
		type : 2,
		title : '支出明细',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefixForward  + '/reportProvinceExpenditureDetail/'+customerId+"/"+accountDt+"/"+customerSourceType+"/"+branchCode+"/"+startProvinceId+"/"+endProvinceId // iframe的url
	});
	layer.full(index);
	}
}
/*$('#exportExcelbtn').click(function(){
	var accountDt = $('#accountDt').val()==undefined?'': $('#accountDt').val();
	var customerId = $('#customerId').val()==undefined?'': $('#customerId').val();
	var startProvinceId = $('#startProvinceId').val()==undefined?'': $('#startProvinceId').val();
	var endProvinceId = $('#endProvinceId').val()==undefined?'':$('#endProvinceId').val();
	var url = prefix + "/exportExcel"+"?accountDt="+accountDt+"&customerId="+customerId+"&startProvinceId="+startProvinceId+"&endProvinceId="+endProvinceId;
	window.open(url);
}); 
*/

//导出excel
function exportExcel(action) {
	var limit =  $('#limitvalue').val();
	var offset = $('#offsetvalue').val();
	var accountDt = $('#accountDt').val()==undefined?'': $('#accountDt').val();
	var customerId = $('#customerId').val()==undefined?'': $('#customerId').val();
	var startProvinceId = $('#startProvinceId').val()==undefined?'': $('#startProvinceId').val();
	var endProvinceId = $('#endProvinceId').val()==undefined?'':$('#endProvinceId').val();
	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly");//将input元素设置为readonly
	
	  var url = prefix + action+"?accountDt="+accountDt+"&customerId="+customerId+"&startProvinceId="+startProvinceId+"&endProvinceId="+endProvinceId+"&limit="+limit+"&offset="+offset;
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
	        a.download = '支出报表-'+nowTime+'xlsx';
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
				'recordId' : id
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
			ids[i] = row['recordId'];
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
	document.getElementById("signupForm").reset(); 
}