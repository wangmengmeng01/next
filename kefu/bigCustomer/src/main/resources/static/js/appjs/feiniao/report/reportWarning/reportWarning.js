var prefix = "/vipkf/report/reportWarning";
$(function() {
	if(Chrome()){
		alert("各位领导,请使用谷歌浏览器进行操作!");
		return;
	}
	// 发送心跳检测,检测页面开关
	heartCheck();
	//上传excel按钮
    layui.use(['upload','laydate'], function () {
        var upload = layui.upload;
        var laydate = layui.laydate;
        //执行一个laydate实例
		laydate.render({
		    elem: '#start_date', //指定元素
		    max:0,//设置最大范围内的日期时间值
		    showBottom: false
		});
		laydate.render({
		    elem: '#end_date', //指定元素
			max:0,
			showBottom: false	
		});
		//给input赋值---开始时间默认当前时间的T-1天，结束时间默认当前时间的T-1天
	    $('#start_date').val(GetDateStr(0));
	    $('#end_date').val(GetDateStr(0));
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
    load();
    
    //刷新按钮
     $("#refresh").click(function(){ $('#exampleTable').bootstrapTable('refresh'); }); 
     //导出功能的传参问题
//     $('#exportExcelbtn').click(function(){  
//         var $eleForm = $("<form method='post' enctype='application/x-www-form-urlencoded'><input type='hidden' name='start_date' value="+ $('#start_date').val()+"><input type='hidden' name='end_date' value="+ $('#end_date').val()+"></form>");  
//       
//         $eleForm.attr("action",prefix+"/exportExcel");  
//       
//         $(document.body).append($eleForm);  
//       
//         //提交表单，实现下载  
//         $eleForm.submit();  
//     }); 
     
});

//js获取当前日期前后N天的方法:
function GetDateStr(AddDayCount) { 
    var date = new Date(); 
    date.setDate(date.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = date.getFullYear(); 
    var m = changeTime(date.getMonth()+1);//获取当前月份的日期
    var d = changeTime(date.getDate()-1); 
    return y+"-"+m+"-"+d; 
 } 
//如果月份和日期为一位数，需要在前面补0
function changeTime(t){
	return t<10 ? "0"+ t : t;
}


function openshengwarningTable(val) {
	var bigarea = val.split(',')[0]; 			
	var provinceId = val.split(',')[1];
	var startDate = val.split(',')[2];
	var endDate = val.split(',')[3];
	var showType="city";

	var index = layer.open({
		type : 2,
		title : '城市表',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '520px' ],
		content : prefix + "/reportWarningSheng/"+bigarea+"/"+provinceId+"/"+startDate+"/"+endDate+"/"+showType // iframe的url
		
		/*
	    yes: function(index,layero){
	    	$(layero).find("iframe")[0].contentWindow.submitForm();
	    },
	    btn2: function(index){
	      layer.close(index);
	    }*/
	});
	layer.full(index);//设置弹出框为满屏
}

function openCustproTable(val) {

	var provinceId = val.split(',')[0];
	var numberLevel  = val.split(',')[1];
	var startDate = val.split(',')[2];
	var endDate = val.split(',')[3];
	var showType="custpronumber";
	
	var index = layer.open({
		type : 2,
		title : '城市表',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '520px' ],
		content : prefix + "/reportWarningNumberLevel/"+provinceId+"/"+numberLevel+"/"+startDate+"/"+endDate+"/"+showType, // iframe的url   branchCode+"/"+
	    
		yes: function(index,layero){
	    	$(layero).find("iframe")[0].contentWindow.submitForm();
	    },
	    btn2: function(index){
	      layer.close(index);
	    }
	});
	layer.full(index);
}
function openCustallTable(val) {

	var provinceId = "0";
	var numberLevel  = val.split(',')[1];
	var startDate = val.split(',')[2];
	var endDate = val.split(',')[3];
	var showType="custallnumber";
	
	var index =layer.open({
		type : 2,
		title : '城市表',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '520px' ],
		content : prefix + "/reportWarningNumberLevel/"+provinceId+"/"+numberLevel+"/"+startDate+"/"+endDate+"/"+showType, // iframe的url   branchCode+"/"+
	    
		yes: function(index,layero){
	    	$(layero).find("iframe")[0].contentWindow.submitForm();
	    },
	    btn2: function(index){
	      layer.close(index);
	    }
	});
	layer.full(index);
}

//网点权限
function openBranchTable(val) {
	var branchCode = val.split(',')[0];
	var startDate = val.split(',')[1];
	var endDate = val.split(',')[2];
	var showType="customer";
	
	var index = layer.open({
		type : 2,
		title : '客户预警表',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '520px' ],
		content : prefix + "/reportWarningBranch/"+branchCode+"/"+startDate+"/"+endDate+"/"+showType // iframe的url
		
	});
	layer.full(index);
}

function openCustcitTable(val) {
	var branchCode = val.split(',')[0];
	var numberLevel  = val.split(',')[1];
	var startDate = val.split(',')[2];
	var endDate = val.split(',')[3];
	var showType="custbranumber";
	
	var index = layer.open({
		type : 2,
		title : '客户预警表',
		maxmin : true,
		shadeClose : false,
		area : [ '800px', '520px' ],
		content : prefix + "/reportWarningNumberLevelBranch/"+branchCode+"/"+numberLevel+"/"+startDate+"/"+endDate+"/"+showType, // iframe的url
	    
		yes: function(index,layero){
	    	$(layero).find("iframe")[0].contentWindow.submitForm();
	    },
	    btn2: function(index){
	      layer.close(index);
	    }
	});
	layer.full(index);
}

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
						pagination : false, // 设置为true会在底部显示分页条
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
						dataField: "data",//这是返回的json数组的key.默认好像是"rows".默认的两个属性totalField="total"  dataField="rows"
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
					           // name:$('#searchName').val(),
					           // username:$('#searchName').val()
								startDate:$('#start_date').val(),
								endDate:$('#end_date').val()
								
							};
						},
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						               
						columns : [ [
								{field : 'customerName', title :  '地域', align : 'center', rowspan : 2, width : 180,
										formatter:function(value,row, index){
										if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
								    	  return "--";
										else if(row.customerName !=null && row.customerName !=''){
											if(row.customerName.indexOf("合计")==-1) {
												if(row.customerName.indexOf("网点")!=-1)
										               return "<a href='javascript:void(0);' style='color:blue;' onclick='openBranchTable(\""+row.branchCode+","+row.startDate+","+row.endDate+","+row.bigarea+","+row.provinceId+","+row.cityId+"\");'>"+value+'</a>';  
										  			
												else 
													return "<a href='javascript:void(0);' style='color:blue;' onclick='openshengwarningTable(\""+row.bigarea+","+row.provinceId+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>'; 
											}
											else
												return value;
								        }
								    	  else
								          return value;  
									}
								},
								{field : '',title : 'B类', align : 'center', colspan : 1},
		        		   	      {field : '',title : 'C类', align : 'center', colspan : 1},

		        		   	      {field : '',title : 'D类', align : 'center', colspan : 1},
		        		   	      {field : '',title : 'E类', align : 'center', colspan : 1},

		        		   	      {field : '',title : 'F类', align : 'center', colspan : 1},
		        			      {field : '',title : 'G类', align : 'center', colspan : 1}
		        		             
		        		        ],[
								{field : 'bcustomerSum', width : 100, title : '客户数',align : 'center',
									  formatter:function(value,row, index){
										  if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
											  return "--";
							        	  else if(row.customerName !=null && row.customerName !=''){ 
							        		  if(row.customerName.indexOf("集团合计")!=-1)
							                       return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustallTable(\""+''+","+'b'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							        		  
							        		  else if(row.customerName.indexOf("合计")==-1){
							        			  if(row.customerName.indexOf("网点")!=-1)
									            	  return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustcitTable(\""+row.branchCode+","+'b'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							        			  else 
								            	   return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustproTable(\""+row.provinceId+","+'b'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							        		  }
								              else
								             	   return value;
								        	  }
								          else
								              return value;
									  }
								  },
								  {field : 'ccustomerSum', width : 100, title : '客户数', align : 'center',
	     		                	  formatter:function(value,row, index){
											if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												return "--";
						                	else if(row.customerName !=null && row.customerName !=''){ 
						                		  if(row.customerName.indexOf("集团合计")!=-1)
						                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustallTable(\""+''+","+'c'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
						                		  else if(row.customerName.indexOf("网点")!=-1)
						                        	  return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustcitTable(\""+row.branchCode+","+'c'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							                      
						                          else  if(row.customerName.indexOf("合计")==-1)
						                        	  return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustproTable(\""+row.provinceId+","+'c'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
						                          
						                          else
					                            	  return value;
						                	  }
				                            else
				                            	return value;
	     		                	  }
	     		                   },							        		  								        		
				                   {field : 'dcustomerSum', width : 100, title : '客户数',align : 'center',
	     		                	  formatter:function(value,row, index){
											if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
						                	  return "--";
							                	  else if(row.customerName !=null && row.customerName !=''){ 
							                		  if(row.customerName.indexOf("集团合计")!=-1)
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustallTable(\""+''+","+'d'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							                		  
							                          else if(row.customerName.indexOf("合计")==-1){
							                        	  if(row.customerName.indexOf("网点")!=-1)
								                        	  return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustcitTable(\""+row.branchCode+","+'d'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							                        	  else 
							                        	  return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustproTable(\""+row.provinceId+","+'d'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							                          }
							                          else
						                            	  return value;
							                	  }
						                              else
					                            	  return value;
	     		                	  }
	     		                   },									   
				                   {field : 'ecustomerSum', width : 100, title : '客户数', align : 'center',
	     		                	  formatter:function(value,row, index){
											if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
						                	  return "--";
							                	  else if(row.customerName !=null && row.customerName !=''){ 
							                		  if(row.customerName.indexOf("集团合计")!=-1)
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustallTable(\""+''+","+'e'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							                		  
							                          else  if(row.customerName.indexOf("合计")==-1){
							                        	  if(row.customerName.indexOf("网点")!=-1)
								                        	  return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustcitTable(\""+row.branchCode+","+'e'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							                        	  else
							                        	  return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustproTable(\""+row.provinceId+","+'e'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							                          }
							                          else
						                            	  return value;
							                	  }
						                              else
					                            	  return value;
	     		                	  }
	     		                   },
				                   {field : 'fcustomerSum', width : 100, title : '客户数', align : 'center',
	     		                	  formatter:function(value,row, index){
											if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
						                	  return "--";
							                	  else if(row.customerName !=null && row.customerName !=''){ 
							                		  if(row.customerName.indexOf("集团合计")!=-1)
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustallTable(\""+''+","+'f'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							                		  
							                          else  if(row.customerName.indexOf("合计")==-1){
							                        	  if(row.customerName.indexOf("网点")!=-1)
								                        	  return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustcitTable(\""+row.branchCode+","+'f'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							                        	  else 
							                        	  return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustproTable(\""+row.provinceId+","+'f'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							                          }
							                          else
						                            	  return value;
							                	  }
						                          else
					                            	  return value;
	     		                	  }
	     		                   },
				                   {field : 'gcustomerSum', width : 100, title : '客户数', align : 'center',
	     		                	  formatter:function(value,row, index){
											if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
						                	  return "--";
							                	  else if(row.customerName !=null && row.customerName !=''){ 
							                		  if(row.customerName.indexOf("集团合计")!=-1)
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustallTable(\""+''+","+'g'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							                		  
							                          else if(row.customerName.indexOf("合计")==-1){
							                        	  if(row.customerName.indexOf("网点")!=-1)
								                        	  return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustcitTable(\""+row.branchCode+","+'g'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							                        	  else 
							                        	  return "<a href='javascript:void(0);' style='color:blue;' onclick='openCustproTable(\""+row.provinceId+","+'g'+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';  
							                          }
							                          else
						                            	  return value;
							                	  }
						                              else
				                            	  return value;
	     		                	  }
	     		                   }
					] ]
					});
}

//请求成功方法
function responseHandler(result){
    var errcode = result.code;//在此做了错误代码的判断
    var data = result.data;
    if(errcode == 490){
    	popSafepage(data);
        return { total : 0  };
    }else if(errcode != 200){
        layer.alert(data);
        return { total : 0  };
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

/**
 * 查询必须满足以下两种情况：
 * 1.开始时间和结束时间必须是同一年同一个月，不能跨月查询
 * 2.开始时间<结束时间
 */
function query(){
	var startDate = $('#start_date').val(),
		endDate = $('#end_date').val(),
		startYear = startDate.substring(0,5),
		endYear = endDate.substring(0,5),
		startMonth = startDate.substring(5,7),
		endMonth = endDate.substring(5,7);
	var startTime = new Date(startDate.replace(/-/g,"/")).getTime(),
		endTime = new Date(endDate.replace(/-/g,"/")).getTime();
	if(startTime>endTime){
		layer.msg('开始时间不能大于结束时间！'); 
		return;
	}
	/*if( (startYear == endYear && startMonth !== endMonth) || startYear !== endYear){
		layer.msg('不能跨月查询！'); 
		return;
	}*/
	$("#exampleTable").bootstrapTable('destroy');
	load();
}

//导出excel
function exportExcel(action) {
	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly");//将input元素设置为readonly 
	
	  var url = prefix + action+"?startDate="+$('#start_date').val()+"&endDate="+$('#end_date').val();
	  var xhr = new XMLHttpRequest();
	  xhr.open('GET', url, true);    // 也可以使用POST方式，根据接口   true表示异步
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
	    	var date = new Date();
		    var nowTime = date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()+' '+date.getHours()+date.getMinutes()+date.getSeconds();
		    var a = document.createElement('a');
	        a.download = '预警表'+nowTime+'.xlsx';
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
	  xhr.send();
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
				'bigarea' : id
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
			ids[i] = row['bigarea'];
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
	document.getElementById("searchWarningData").reset(); 
}