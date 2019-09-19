var prefix = "/vipkf/system/fileExport";
$(function() {
	load();
	
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
$('#title').autocomplete({  
    source:function(query,process){ 
     debugger;
        var matchCount =this.options.items;//允许返回结果集最大数量  
        $.ajax({
         url: prefix+"/getTitle",
         async : false,
         dataType: 'json',
         jsonp: "callback",
         data: {"param":query},
         success: function (data) {
          debugger;
          /*data =$.parseJSON(data);
          if(!data) {  
              alert('输入的卡号不正确');  
           }*/ 
          return process(data);  
         }
     });
     },
    formatItem:function(item){  
        return item;  
     },  
    setValue:function(item){  
        return{'data-value':item,'real-value':item};  
     }  
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
						
						//grimm 双击触发的事件
	            		onDblClickRow: function (row) {
	            			edit(row.id);
	            		},
	            		
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
								state: $("#state").val(),
								title: $('#title').val().trim()
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
									field : 'id', 
									title : '序号' 
								},
																{
									field : 'userId', 
									title : '用户编号' 
								},
								{
									field : 'title', 
									title : '标题'
								},
																{
									field : 'createTime', 
									title : '创建时间' 
								},
								{
									field : 'startTime', 
									title : '开始时间' 
								},
																{
									field : 'endTime', 
									title : '结束时间' 
								},
								{
									field : 'handCount', 
									title : '处理条数' 
								},
																{
									field : 'state', 
									title : '执行状态',
									cellStyle : setColor,
									formatter:function(value,row,index){ 
						            	var value="";
						            	if(row.state=="1"){
						            		value = "等待中";
						            	}else if(row.state=="2"){
						            		value = "处理中";
						            	}else if(row.state=="3"){
						            		value = "执行完成" ;
						            	}else if(row.state=="4"){
						            		value = "执行失败" ;
						            	}else if(row.state=="5"){
						            		value = "无数据" ;
						            	}else{
						            	    value ="未知";
						            	}
											return value;
						            }
								},
                                                                {
		                        title : '下载',
		                        field : 'id',
		                        align : 'center',
		                        formatter : function(value, row, index) {
		                        	if(row.endTime && row.state==='3'){
										var e = '<a class="btn btn-danger btn-sm" href="#" mce_href="#" title="下载" onclick="downloadData(\''
											+ row.id
											+ '\')"><i class="glyphicon glyphicon-arrow-down"></i></a> ';
										return e  ;
		                            }else{
		                            	var e = '<a class="btn  btn-sm" href="#" mce_href="#" title="下载" disabled="true"><i class="glyphicon glyphicon-arrow-down"></i></a> ';
		                            	var f = '<a class="btn btn-primary btn-sm" href="#" mce_href="#" title="删除" onclick="remove(\''
		                            		+ row.id
		                            		+ '\')"><i class="glyphicon glyphicon-remove"></i></a> ';
		                            	if(row.state=='1'||row.state=='2'){
		                            		return f+e  ;
		                            	}
		                            	return e;
		                            }
		                        }
								} ]
					});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}
setInterval("reLoad()", 60000);//1分钟执行一次
function setColor(value, row, index){
	if(row.state=='3'){
		return {css:{color:'#0033ff'}};
	}else{
		return {css:{color:'red'}};
	}
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
				'id' : id
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
			ids[i] = row['id'];
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

function downloadData(id){
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	/*if (rows.length == 0) {
		layer.msg("请选择要下载的数据");
		return;
	}*/
	if (rows.length > 1) {
		layer.msg("一次只能选择一条数据");
		return;
	}
	layer.confirm("确认要下载选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = [];
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['id'];
		});
		    var $eleForm = $("<form method='get' enctype='multipart/form-data'><input name='ids[]' type='hidden' value="+ids+"></form>"
		    		); 
		    	$eleForm.attr("action",prefix + '/download');  
		    	$(document.body).append($eleForm);  
		    	//提交表单，实现下载  
		    	$eleForm.submit();   
		    	layer.closeAll();
			/*	parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index); */
	}, function() {

	});
}
