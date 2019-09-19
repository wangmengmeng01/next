var prefix = "/crmkh/costreport/costreportCustCostExt";
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
								branchCode:$('#branchCode').val(),
								endProvinceId:$('#endProvinceId').val(),
								startProvinceId:$('#startProvinceId').val(),
								shipmentNo:$('#shipmentNo').val()
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
								{field:'senderAccountDt',title:'揽件日'},
								{field:'tdAccountDt',title:'TD结算日'},
								{field:'accountDt',title:'量本利结算日'},
								{field:'customerId',title:'客户编码'},
								{field:'customerName',title:'客户名称'},
								{field:'shipmentNo',title:'运单号'},
								{field:'startProvinceId',title:'始发省ID',visible:false},
								{field:'startProvinceName',title:'始发省'},
								{field:'endProvinceId',title:'目的省ID',visible:false},
								{field:'endProvinceName',title:'目的省'},				
								{field:'weight',title:'结算重量'},
								{field:'tsfFee',title:'中转费'},
								{field:'deliveryFee',title:'派费'},
								{field:'deliveryAdditionalWeightFee',title:'续重派费'},
								{field:'deliveryBalanceFee',title:'平衡派费'},
								{field:'shipmentFee',title:'面单费'},
								{field:'scanFee',title:'扫描费'}
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
/*$('#exportExcelbtn').click(function(){
	var accountDt = $('#month_year').val()==undefined?'':$('#month_year').val();
	var customerId = $('#customerId').val()==undefined?'':$('#customerId').val();
	var branchCode = $('#branchCode').val()==undefined?'':$('#branchCode').val();
	var startProvinceId = $('#startProvinceId').val()==undefined?'':$('#startProvinceId').val();
	var endProvinceId = $('#endProvinceId').val()==undefined?'':$('#endProvinceId').val();
	var shipmentNo = $('#shipmentNo').val()==undefined?'':$('#shipmentNo').val();	
	var url = prefix + "/exportExcel"+"?accountDt="+accountDt+"&customerId="+customerId+"&startProvinceId="+startProvinceId+"&endProvinceId="+endProvinceId+"&branchCode="+branchCode+"&shipmentNo="+shipmentNo;
	window.open(url);
});
*/


//导出excel
function exportExcel(action) {
	var limit =  $('#limitvalue').val();
	var offset = $('#offsetvalue').val();
	var accountDt = $('#accountDt').val()==undefined?'': $('#accountDt').val();
	var customerId = $('#customerId').val()==undefined?'': $('#customerId').val();
	var branchCode = $('#branchCode').val()==undefined?'':$('#branchCode').val();
	var startProvinceId = $('#startProvinceId').val()==undefined?'': $('#startProvinceId').val();
	var endProvinceId = $('#endProvinceId').val()==undefined?'':$('#endProvinceId').val();
	var shipmentNo = $('#shipmentNo').val()==undefined?'':$('#shipmentNo').val();
	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly");//将input元素设置为readonly
	
	  var url = prefix + action+"?accountDt="+accountDt+"&customerId="+customerId+"&branchCode="+branchCode+"&startProvinceId="+startProvinceId+"&endProvinceId="+endProvinceId+"&shipmentNo="+shipmentNo+"&limit="+limit+"&offset="+offset;
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
	        a.download = '支出报表明细-'+nowTime+'xlsx';
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
