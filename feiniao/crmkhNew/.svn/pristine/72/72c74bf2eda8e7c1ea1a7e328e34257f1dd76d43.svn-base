var prefix = "/crmkh/report/reportFluctuate";
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
						//pagination : true, // 设置为true会在底部显示分页条
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
								provinceid:$('#searchProvinceid').val(),
								showType:$('#searchShowType').val()
								
							};
						},
						
						frozenColumns : [ [
										                   {field : 'orderSum', title : '总单量', hidden : true},
										                   {field : 'orderAvg', title : '日均单量', hidden : true},
										                   {field : 'bigarea', title : '大区',  hidden : true},
										                   {field : 'provinceid', title : '省', hidden : true},
										                   {field : 'tmpXuanze', title : '区别选择', hidden : true}
										                   
							        		   ]
							                ] ,
						columns : [ [
										/*{
											checkbox : true, rowspan : 2,
										},*/
			        		   		  {field : 'customerName', title :  '地域', align : 'center', rowspan : 2, width : 180,
			        		   			formatter:function(value,row, index){
			        		  				if(row.customerName!=undefined){
													if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
							                	 	 return "--";
							                	  	else if(row.customerName.indexOf("合计")!=-1)
							                	  	return value;
							                		  else
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openShiTable(\""+row.bigarea+","+row.provinceid+","+row.cityid+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';

							                	}else{
							                		return value;
							                	}
										}
			        				  },
			        		   	      {field : '',title : '总量', align : 'center', colspan : 3},
			        		   	     // {field : '',title : '0-50票(A类)', align : 'center', colspan : 3},
			        		   	      {field : '',title : 'B类', align : 'center', colspan : 3},

			        		   	      {field : '',title : 'C类', align : 'center', colspan : 3},
			        		   	      {field : '',title : 'D类', align : 'center', colspan : 3},

			        		   	      {field : '',title : 'E类', align : 'center', colspan : 3},
			        			      {field : '',title : 'F类', align : 'center', colspan : 3},
			        			      {field : '',title : 'G类', align : 'center', colspan : 3}

			        		             ],[
		                   {field : 'czCustomerSum', width : 100, title : '差值',align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                      return value;
								}
  		                   },
		                   {field : 'lostCustomerSum', width : 100, title : '流失数', align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                      return value;
								}
  		                   },
		                   {field : 'addCustomerSum', width : 100, title : '新增数', align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                      return value;
								}
  		                   },
		                   {field : 'czBCustomerSum', width : 100, title : '差值', align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                      return value;
								}
  		                   },
		                   {field : 'blostCustomerSum', width : 100, title : '流失数',align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                			if(row.tmpXuanze == "选择省份"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",b_lost\");'>"+value+'</a>';

						                	}else if(row.tmpXuanze == "选择城市"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCityCustWindow(\""+row.startDate+","+row.endDate+","+row.cityid+",b_lost\");'>"+value+'</a>';

						                	}
									}
  		                   },
		                   {field : 'baddCustomerSum', width : 100, title : '新增数',align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                			if(row.tmpXuanze == "选择省份"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",b_add\");'>"+value+'</a>';
						                	}else if(row.tmpXuanze == "选择城市"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCityCustWindow(\""+row.startDate+","+row.endDate+","+row.cityid+",b_add\");'>"+value+'</a>';

						                	}
									return value;
									}
  		                   },
                 {field : 'czCCustomerSum', width : 100, title : '差值', align : 'center',
 		                	  formatter:function(value,row, index){
								if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
			                	  return "--";
			                	  else
			                      return value;
							}
 		                   },
	                   {field : 'clostCustomerSum', width : 100, title : '流失数',align : 'center',
 		                	  formatter:function(value,row, index){
								if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
			                	  return "--";
			                	  else
					                	if(row.tmpXuanze == "选择省份"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",c_lost\");'>"+value+'</a>';
					                	}else if(row.tmpXuanze == "选择城市"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCityCustWindow(\""+row.startDate+","+row.endDate+","+row.cityid+",c_lost\");'>"+value+'</a>';

					                	}		
								return value;
								}
 		                   },
	                   {field : 'caddCustomerSum', width : 100, title : '新增数',align : 'center',
 		                	  formatter:function(value,row, index){
								if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
			                	  return "--";
			                	  else
			                			if(row.tmpXuanze == "选择省份"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",c_add\");'>"+value+'</a>';
					                	}else if(row.tmpXuanze == "选择城市"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCityCustWindow(\""+row.startDate+","+row.endDate+","+row.cityid+",c_add\");'>"+value+'</a>';

					                	}			
								return value;
								}
 		                   },
	                   {field : 'czDCustomerSum', width : 100, title : '差值', align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                      return value;
								}
  		                   },
		                   {field : 'dlostCustomerSum', width : 100, title : '流失数',align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
						                	if(row.tmpXuanze == "选择省份"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",d_lost\");'>"+value+'</a>';
						                	}else if(row.tmpXuanze == "选择城市"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCityCustWindow(\""+row.startDate+","+row.endDate+","+row.cityid+",d_lost\");'>"+value+'</a>';

						                	}
									return value;
									}
  		                   },
	                   {field : 'daddCustomerSum', width : 100, title : '新增数',align : 'center',
 		                	  formatter:function(value,row, index){
								if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
			                	  return "--";
			                	  else
			                			if(row.tmpXuanze == "选择省份"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",d_add\");'>"+value+'</a>';
					                	}else if(row.tmpXuanze == "选择城市"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCityCustWindow(\""+row.startDate+","+row.endDate+","+row.cityid+",d_add\");'>"+value+'</a>';

					                	}	
								return value;
								}
 		                   },
	                   {field : 'czECustomerSum', width : 100, title : '差值', align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                      return value;
								}
  		                   },
		                   {field : 'elostCustomerSum', width : 100, title : '流失数',align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
						                	if(row.tmpXuanze == "选择省份"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",e_lost\");'>"+value+'</a>';
						                	}else if(row.tmpXuanze == "选择城市"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCityCustWindow(\""+row.startDate+","+row.endDate+","+row.cityid+",e_lost\");'>"+value+'</a>';

						                	}	
									return value;
									}
  		                   },
		                   {field : 'eaddCustomerSum', width : 100, title : '新增数',align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                			if(row.tmpXuanze == "选择省份"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",e_add\");'>"+value+'</a>';
						                	}else if(row.tmpXuanze == "选择城市"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCityCustWindow(\""+row.startDate+","+row.endDate+","+row.cityid+",e_add\");'>"+value+'</a>';

						                	}
									return value;
									}
  		                   },
		                   {field : 'czFCustomerSum', width : 100, title : '差值', align : 'center',
   		                	  formatter:function(value,row, index){
										if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
					                	  return "--";
					                	  else
					                      return value;
									}
   		                   },
			                   {field : 'flostCustomerSum', width : 100, title : '流失数',align : 'center',
   		                	  formatter:function(value,row, index){
										if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
					                	  return "--";
					                	  else
							                	if(row.tmpXuanze == "选择省份"){
						                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",f_lost\");'>"+value+'</a>';
							                	}else if(row.tmpXuanze == "选择城市"){
						                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCityCustWindow(\""+row.startDate+","+row.endDate+","+row.cityid+",f_lost\");'>"+value+'</a>';

							                	}		
										return value;
										}
   		                   },
			                   {field : 'faddCustomerSum', width : 100, title : '新增数',align : 'center',
   		                	  formatter:function(value,row, index){
										if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
					                	  return "--";
					                	  else
					                			if(row.tmpXuanze == "选择省份"){
						                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",f_add\");'>"+value+'</a>';
							                	}else if(row.tmpXuanze == "选择城市"){
						                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCityCustWindow(\""+row.startDate+","+row.endDate+","+row.cityid+",f_add\");'>"+value+'</a>';

							                	}					
										return value;
										}
   		                   },
			                   {field : 'czGCustomerSum', width : 100, title : '差值', align : 'center',
	     		                	  formatter:function(value,row, index){
											if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
						                	  return "--";
						                	  else
						                      return value;
										}
	     		                   },
				                   {field : 'glostCustomerSum', width : 100, title : '流失数',align : 'center',
	     		                	  formatter:function(value,row, index){
											if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
						                	  return "--";
						                	  else
								                	if(row.tmpXuanze == "选择省份"){
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",g_lost\");'>"+value+'</a>';
								                	}else if(row.tmpXuanze == "选择城市"){
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCityCustWindow(\""+row.startDate+","+row.endDate+","+row.cityid+",g_lost\");'>"+value+'</a>';

								                	}		
											return value;
											}
	     		                   },
				                   {field : 'gaddCustomerSum', width : 100, title : '新增数',align : 'center',
	     		                	  formatter:function(value,row, index){
											if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
						                	  return "--";
						                	  else
						                			if(row.tmpXuanze == "选择省份"){
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",g_add\");'>"+value+'</a>';
								                	}else if(row.tmpXuanze == "选择城市"){
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openCityCustWindow(\""+row.startDate+","+row.endDate+","+row.cityid+",g_add\");'>"+value+'</a>';

								                	}
											return value;
											}
	     		                   }
							                   ]]
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
	
	  var url = prefix + "/exportShengExcel"+"?startDate="+$('#searchStartDate').val()+"&endDate="+$('#searchEndDate').val()+"&provinceid="+$('#searchProvinceid').val()+"&showType="+$('#searchShowType').val();
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
	        a.download = '波动表-城市表-'+nowTime+'.xlsx';
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
//点击流失数或者新增数获取各市的客户波动情况
function openCityCustWindow(param){
		/*if(s_shengData_h=='hidden'){
			return;
		};*/
		var startDate  = param.split(',')[0];
		var endDate  = param.split(',')[1];
		var cityid  = param.split(',')[2];
		var bdType  = param.split(',')[3];
		var showType="openCityCustWindow";
		var index = layer.open({
			type : 2,
			title : '客户波动表',
			maxmin : true,
			shadeClose : false, // 点击遮罩关闭层
			area : [ '800px', '520px' ],
			content : prefix  + '/reportFluctuateCust/'+startDate+"/"+endDate+"/"+cityid+"/"+bdType+"/"+showType, // iframe的url
			
			yes: function(index,layero){
		    	$(layero).find("iframe")[0].contentWindow.submitForm();
		    },
		    btn2: function(index){
		      layer.close(index);
		    }
		});
		layer.full(index);
}
//点击流失数或者新增数获该省的客户波动情况
function openProvinceCustWindow(param){
		/*if(s_shengData_h=='hidden'){
			return;
		};*/
		var startDate  = param.split(',')[0];
		var endDate  = param.split(',')[1];
		var provinceid  = param.split(',')[2];
		var bdType  = param.split(',')[3];
		var showType="openProvinceCustWindow";
		var index = layer.open({
			type : 2,
			title : '客户波动表',
			maxmin : true,
			shadeClose : false, // 点击遮罩关闭层
			area : [ '800px', '520px' ],
			content : prefix  + '/reportFluctuateCust/'+startDate+"/"+endDate+"/"+provinceid+"/"+bdType+"/"+showType, // iframe的url
			
			yes: function(index,layero){
		    	$(layero).find("iframe")[0].contentWindow.submitForm();
		    },
		    btn2: function(index){
		      layer.close(index);
		    }
		});
		layer.full(index);
}
//波动表-点击市打开对应的网点数据
function openShiTable(City){
	var bigarea = City.split(',')[0];
	var provinceid = City.split(',')[1];
	var cityid = City.split(',')[2];
	var startDate = City.split(',')[3];
	var endDate = City.split(',')[4];
	var showType="branch";
	var regionId = bigarea;
	var index = layer.open({
		type : 2,
		title : '公司表',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/reportFluctuateShi/'+regionId+"/"+provinceid+"/"+cityid+"/"+startDate+"/"+endDate+"/"+showType,// iframe的url
		
		yes: function(index,layero){
	    	$(layero).find("iframe")[0].contentWindow.submitForm();
	    },
	    btn2: function(index){
	      layer.close(index);
	    }
	});
	layer.full(index);
}




function resetPwd(id) {
}

function resetForm(){
	document.getElementById("searchFluctuateData").reset(); 
}
