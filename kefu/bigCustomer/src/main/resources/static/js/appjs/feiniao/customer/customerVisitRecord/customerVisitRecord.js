var prefix = "/vipkf/customer/customerVisitRecord";
var prefix2 = "/vipkf/costreport/costreportOrderCostFinish";

$(function() {
	load();
	
	   layui.use(['laydate','laypage','layer','form','upload'], function () {
	    	$=layui.jquery;
	    	var laydate = layui.laydate;
	    	layer = layui.layer;
	    	form = layui.form;
	    	laydate.render({
	         	elem: '#startDate' //指定元素

	         });
	    	laydate.render({
	         	elem: '#endDate' //指定元素

	         });
	    	laydate.render({
	         	elem: '#startVisitDate' //指定元素

	         });
	    	laydate.render({
	         	elem: '#endVisitDate' //指定元素

	         });
	    });
	   
	//上传excel按钮
    layui.use('upload', function () {
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
    });	
    
    //刷新按钮
     $("#refresh").click(function(){ $('#exampleTable').bootstrapTable('refresh'); }); 
});

function load() {
	
	var addressIdMagicSuggest = $('#branchCode').magicSuggest({
        width:100,
        allowFreeEntries: false,
        maxSelection:1,
        autoSelect:false,
        valueField:"bm",
        displayField:"mc",
        placeholder:"",
        resultAsString:true,
        selectionStacked: true,
        queryParam:'queryString',
        //下拉框数据的获得：
        data: prefix2+'/searchCustBraData'
});
	
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
						
						//grimm 双击触发的事件
	            		onDblClickRow: function (row) {
	            			edit(row.id);
	            		},
	            		
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对		
								branchCode:$('#branchCode').magicSuggest().getValue()[0],
								startDate:$('#startDate').val(),
								endDate:$('#endDate').val(),
								startVisitDate:$('#startVisitDate').val(),
								endVisitDate:$('#endVisitDate').val(),
								provinceId:$("#customerProvince").find("option:selected").attr("value"),
								visitId:$('#visitId').val(),
								limit: params.limit,
								offset:params.offset
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
						columns : [
								{
									checkbox : true
								},
								   {
									title : '操作',
									field : 'id',
									align : 'center',
									formatter : function(value, row, index) {
									
										var d = "<a href='javascript:void(0);' style='color:blue;' onclick='seeReport(\""+row.recordId+"\");'>"+' 查看拜访记录 '+'</a>';  

										return d;
									}
								},
																{
									field : 'recordId', 
									title : '数据记录主键ID',
									visible:false
								},
																{
									field : 'visitId', 
									title : '拜访id' 
								},
																{
									field : 'createrName', 
									title : '业务员姓名' 
								},
																{
									field : 'belongBranch', 
									title : '创建人所属网点' 
								},
																{
									field : 'visitTime', 
									title : '拜访时间' 
								},
																{
									field : 'customerId', 
									title : '客户编码' 
								},
																{
									field : 'customerName', 
									title : '客户名称' 
								},
																{
									field : 'customerLinkman', 
									title : '客户联系人' 
								},
																{
									field : 'customerPhonenum', 
									title : '客户联系方式' 
								},
																{
									field : 'customerArea', 
									title : '客户所在地区' 
								},
																{
									field : 'customerProvince', 
									title : '客户所在省' 
								},
																{
									field : 'customerCity', 
									title : '客户所在城市' 
								},
																{
									field : 'customerDetailaddress', 
									title : '客户详细地址' 
								},
																{
									field : 'visitPerpose', 
									title : '拜访目的' 
								},
																{
									field : 'customerFeedback', 
									title : '客户反馈' 
								},
																{
									field : 'visitResult', 
									title : '洽谈结果' 
								},
																{
									field : 'creatTime', 
									title : '创建时间' 
								} ]
					});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}


function seeReport(data){
	var recordId = data;
	var index = layer.open({
		type : 2,
		title : '查看',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix  + '/openVisit/'+recordId// iframe的url
	});
}

//导出excel
function exportExcel(action) {
	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly");//将input元素设置为readonly
	
	  var url = prefix + action;
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
	        a.download = 'data.xlsx';
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
