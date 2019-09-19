var prefix = "/crmkh/report/reportFluctuate";
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
		    elem: '#searchStartDate',//指定元素
		    max:0,//设置最大范围内的日期时间值
		    showBottom: false
		});
		laydate.render({
		    elem: '#searchEndDate',//指定元素
		    max:0,
		    showBottom: false
		});
		//给input赋值---开始时间默认当前时间，结束时间默认当前时间
	    $('#searchStartDate').val(GetDateStr(-1));
	    $('#searchEndDate').val(GetDateStr(-1));
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
        load();
    });	
    
    //刷新按钮
     $("#refresh").click(function(){ $('#exampleTable').bootstrapTable('refresh'); }); 
});
//js获取当前日期前后N天的方法:
function GetDateStr(AddDayCount) { 
    var date = new Date(); 
    date.setDate(date.getDate()+AddDayCount);//获取AddDayCount天后的日期
    var y = date.getFullYear(); 
    var m = changeTime(date.getMonth()+1);//获取当前月份的日期
    var d = changeTime(date.getDate()); 
    return y+"-"+m+"-"+d; 
 } 
//如果月份和日期为一位数，需要在前面补0
function changeTime(t){
	return t<10 ? "0"+ t : t;
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
						dataField: "data",//这是返回的json数组的key.默认好像是"rows".这里只有前后端约定好就行
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
								startDate:$('#searchStartDate').val(),
								endDate:$('#searchEndDate').val()
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
			        		   		  {field : 'customerName', title :  '地域', align : 'center', rowspan : 2, width : 180,
			        		  			formatter:function(value,row, index){
			        		  				if(row.customerName!=undefined){
													if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
							                	 	 return "--";
							                	  	else if(row.customerName.indexOf("合计")!=-1||row.customerName.indexOf("网点")!=-1)
							                	  	return value;//即如果customerName是xx合计或xx网点直接返回值  没有点击事件
							                		  else
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openShengTable(\""+row.provinceid+","+row.startDate+","+row.endDate+"\");'>"+value+'</a>';

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
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt' || typeof(value) == 'undefined')
				                	  return "--";
				                	  else
				                	if(row.tmpXuanze == "集团合计"){
			                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openAllCustWindow(\""+row.startDate+","+row.endDate+",all,b_lost\");'>"+value+'</a>';
				                	}else if(row.tmpXuanze == "大区合计"){
			                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBigareaCustWindow(\""+row.startDate+","+row.endDate+","+row.bigarea+",b_lost\");'>"+value+'</a>';
				                	}else if(row.tmpXuanze == "选择省份"){
			                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",b_lost\");'>"+value+'</a>';

				                	}else if(row.tmpXuanze == "选择网点"){
			                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBranchCustWindow(\""+row.startDate+","+row.endDate+","+row.branchCode+",b_lost\");'>"+value+'</a>';

				                	}
									return value;
  		                	  }
  		                   },
		                   {field : 'baddCustomerSum', width : 100, title : '新增数',align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                			if(row.tmpXuanze == "集团合计"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openAllCustWindow(\""+row.startDate+","+row.endDate+",all,b_add\");'>"+value+'</a>';
						                	}else if(row.tmpXuanze == "大区合计"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBigareaCustWindow(\""+row.startDate+","+row.endDate+","+row.bigarea+",b_add\");'>"+value+'</a>';
						                	}else if(row.tmpXuanze == "选择省份"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",b_add\");'>"+value+'</a>';

						                	}else if(row.tmpXuanze == "选择网点"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBranchCustWindow(\""+row.startDate+","+row.endDate+","+row.branchCode+",b_add\");'>"+value+'</a>';

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
					                	if(row.tmpXuanze == "集团合计"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openAllCustWindow(\""+row.startDate+","+row.endDate+",all,c_lost\");'>"+value+'</a>';
					                	}else if(row.tmpXuanze == "大区合计"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBigareaCustWindow(\""+row.startDate+","+row.endDate+","+row.bigarea+",c_lost\");'>"+value+'</a>';
					                	}else if(row.tmpXuanze == "选择省份"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",c_lost\");'>"+value+'</a>';

					                	}else if(row.tmpXuanze == "选择网点"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBranchCustWindow(\""+row.startDate+","+row.endDate+","+row.branchCode+",c_lost\");'>"+value+'</a>';

					                	}		
								return value;
								}
 		                   },
	                   {field : 'caddCustomerSum', width : 100, title : '新增数',align : 'center',
 		                	  formatter:function(value,row, index){
								if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
			                	  return "--";
			                	  else
			                			if(row.tmpXuanze == "集团合计"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openAllCustWindow(\""+row.startDate+","+row.endDate+",all,c_add\");'>"+value+'</a>';
					                	}else if(row.tmpXuanze == "大区合计"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBigareaCustWindow(\""+row.startDate+","+row.endDate+","+row.bigarea+",c_add\");'>"+value+'</a>';
					                	}else if(row.tmpXuanze == "选择省份"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",c_add\");'>"+value+'</a>';

					                	}else if(row.tmpXuanze == "选择网点"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBranchCustWindow(\""+row.startDate+","+row.endDate+","+row.branchCode+",c_add\");'>"+value+'</a>';

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
						                	if(row.tmpXuanze == "集团合计"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openAllCustWindow(\""+row.startDate+","+row.endDate+",all,d_lost\");'>"+value+'</a>';
						                	}else if(row.tmpXuanze == "大区合计"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBigareaCustWindow(\""+row.startDate+","+row.endDate+","+row.bigarea+",d_lost\");'>"+value+'</a>';
						                	}else if(row.tmpXuanze == "选择省份"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",d_lost\");'>"+value+'</a>';

						                	}else if(row.tmpXuanze == "选择网点"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBranchCustWindow(\""+row.startDate+","+row.endDate+","+row.branchCode+",d_lost\");'>"+value+'</a>';

						                	}
									return value;
									}
  		                   },
	                   {field : 'daddCustomerSum', width : 100, title : '新增数',align : 'center',
 		                	  formatter:function(value,row, index){
								if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
			                	  return "--";
			                	  else
			                			if(row.tmpXuanze == "集团合计"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openAllCustWindow(\""+row.startDate+","+row.endDate+",all,d_add\");'>"+value+'</a>';
					                	}else if(row.tmpXuanze == "大区合计"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBigareaCustWindow(\""+row.startDate+","+row.endDate+","+row.bigarea+",d_add\");'>"+value+'</a>';
					                	}else if(row.tmpXuanze == "选择省份"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",d_add\");'>"+value+'</a>';

					                	}else if(row.tmpXuanze == "选择网点"){
				                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBranchCustWindow(\""+row.startDate+","+row.endDate+","+row.branchCode+",d_add\");'>"+value+'</a>';

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
						                	if(row.tmpXuanze == "集团合计"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openAllCustWindow(\""+row.startDate+","+row.endDate+",all,e_lost\");'>"+value+'</a>';
						                	}else if(row.tmpXuanze == "大区合计"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBigareaCustWindow(\""+row.startDate+","+row.endDate+","+row.bigarea+",e_lost\");'>"+value+'</a>';
						                	}else if(row.tmpXuanze == "选择省份"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",e_lost\");'>"+value+'</a>';

						                	}else if(row.tmpXuanze == "选择网点"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBranchCustWindow(\""+row.startDate+","+row.endDate+","+row.branchCode+",e_lost\");'>"+value+'</a>';

						                	}	
									return value;
									}
  		                   },
		                   {field : 'eaddCustomerSum', width : 100, title : '新增数',align : 'center',
  		                	  formatter:function(value,row, index){
									if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
				                	  return "--";
				                	  else
				                			if(row.tmpXuanze == "集团合计"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openAllCustWindow(\""+row.startDate+","+row.endDate+",all,e_add\");'>"+value+'</a>';
						                	}else if(row.tmpXuanze == "大区合计"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBigareaCustWindow(\""+row.startDate+","+row.endDate+","+row.bigarea+",e_add\");'>"+value+'</a>';
						                	}else if(row.tmpXuanze == "选择省份"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",e_add\");'>"+value+'</a>';

						                	}else if(row.tmpXuanze == "选择网点"){
					                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBranchCustWindow(\""+row.startDate+","+row.endDate+","+row.branchCode+",e_add\");'>"+value+'</a>';

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
							                	if(row.tmpXuanze == "集团合计"){
						                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openAllCustWindow(\""+row.startDate+","+row.endDate+",all,f_lost\");'>"+value+'</a>';
							                	}else if(row.tmpXuanze == "大区合计"){
						                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBigareaCustWindow(\""+row.startDate+","+row.endDate+","+row.bigarea+",f_lost\");'>"+value+'</a>';
							                	}else if(row.tmpXuanze == "选择省份"){
						                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",f_lost\");'>"+value+'</a>';

							                	}else if(row.tmpXuanze == "选择网点"){
						                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBranchCustWindow(\""+row.startDate+","+row.endDate+","+row.branchCode+",f_lost\");'>"+value+'</a>';

							                	}		
										return value;
										}
   		                   },
			                   {field : 'faddCustomerSum', width : 100, title : '新增数',align : 'center',
   		                	  formatter:function(value,row, index){
										if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
					                	  return "--";
					                	  else
					                			if(row.tmpXuanze == "集团合计"){
						                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openAllCustWindow(\""+row.startDate+","+row.endDate+",all,f_add\");'>"+value+'</a>';
							                	}else if(row.tmpXuanze == "大区合计"){
						                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBigareaCustWindow(\""+row.startDate+","+row.endDate+","+row.bigarea+",f_add\");'>"+value+'</a>';
							                	}else if(row.tmpXuanze == "选择省份"){
						                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",f_add\");'>"+value+'</a>';

							                	}else if(row.tmpXuanze == "选择网点"){
						                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBranchCustWindow(\""+row.provinceID+",f_add\");'>"+value+'</a>';

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
								                	if(row.tmpXuanze == "集团合计"){
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openAllCustWindow(\""+row.startDate+","+row.endDate+",all,g_lost\");'>"+value+'</a>';
								                	}else if(row.tmpXuanze == "大区合计"){
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBigareaCustWindow(\""+row.startDate+","+row.endDate+","+row.bigarea+",g_lost\");'>"+value+'</a>';
								                	}else if(row.tmpXuanze == "选择省份"){
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",g_lost\");'>"+value+'</a>';

								                	}else if(row.tmpXuanze == "选择网点"){
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBranchCustWindow(\""+row.startDate+","+row.endDate+","+row.branchCode+",g_lost\");'>"+value+'</a>';

								                	}		
											return value;
											}
	     		                   },
				                   {field : 'gaddCustomerSum', width : 100, title : '新增数',align : 'center',
	     		                	  formatter:function(value,row, index){
											if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
						                	  return "--";
						                	  else
						                			if(row.tmpXuanze == "集团合计"){
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openAllCustWindow(\""+row.startDate+","+row.endDate+",all,g_add\");'>"+value+'</a>';
								                	}else if(row.tmpXuanze == "大区合计"){
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBigareaCustWindow(\""+row.startDate+","+row.endDate+","+row.bigarea+",g_add\");'>"+value+'</a>';
								                	}else if(row.tmpXuanze == "选择省份"){
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openProvinceCustWindow(\""+row.startDate+","+row.endDate+","+row.provinceid+",g_add\");'>"+value+'</a>';

								                	}else if(row.tmpXuanze == "选择网点"){
							                              return "<a href='javascript:void(0);' style='color:blue;' onclick='openBranchCustWindow(\""+row.startDate+","+row.endDate+","+row.branchCode+",g_add\");'>"+value+'</a>';

								                	}
											return value;
											}
	     		                   }
							                   ]],
						
						formatNoMatches: function(){
					        return "";
					    }
						
					});
}

var tishi ;
var tishiing=0;
//请求成功方法
function responseHandler(result){
    var errcode = result.code;//在此做了错误代码的判断
    var data = result.data;
    if(errcode == 490){ 
    	popSafepage(data);
        return { total : 0  };
    }else if(errcode != 200){
		if(errcode == 850){
				if(tishiing==0){
					tishiing =1;
					//loading层
					tishi = layer.load(0, {time: 10*1000});
				}

				//处理中,延迟一会自动请求
				setTimeout(reLoad,1000);
				return;
    	}else{
    		layer.alert(result.message);
            return { total : 0  };
    	}
    }else{
    	layer.close(tishi);
    	tishiing =0;
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
 * 1.开始时间和结束时间必须是同一年同一个月，不能跨域查询
 * 2.开始时间<结束时间
 */
function query(){
	var startDate = $('#searchStartDate').val(),
		endDate = $('#searchEndDate').val(),
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
	if( (startYear == endYear && startMonth !== endMonth) || startYear !== endYear){
		layer.msg('不能跨月查询！'); 
		return;
	}
	$("#exampleTable").bootstrapTable('destroy');
	load();
}
//导出excel
function exportExcel(action) {
	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly");//将input元素设置为readonly
	
	  var url = prefix + action+"?startDate="+$('#searchStartDate').val()+"&endDate="+$('#searchEndDate').val();
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
	        a.download = '波动表-'+nowTime+'.xlsx';
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
	
//网点权限   点击流失数或者新增数获取该网点的客户波动情况
function openBranchCustWindow(param){
		/*if(s_shengData_h=='hidden'){
			return;
		};*/
		var startDate  = param.split(',')[0];
		var endDate  = param.split(',')[1];
		var branchCode  = param.split(',')[2];
		var bdType  = param.split(',')[3];
		var showType="openBranchCustWindow";
		var index = layer.open({
			type : 2,
			title : '客户波动表',
			maxmin : true,
			shadeClose : false, // 点击遮罩关闭层
			area : [ '800px', '520px' ],
			content : prefix  + '/reportFluctuateCust/'+startDate+"/"+endDate+"/"+branchCode+"/"+bdType+"/"+showType, // iframe的url
			
			yes: function(index,layero){
		    	$(layero).find("iframe")[0].contentWindow.submitForm();
		    },
		    btn2: function(index){
		      layer.close(index);
		    }
		});
		layer.full(index);
}
//点击流失数或者新增数获取省份的客户波动情况
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
//点击流失数或者新增数获取大区的客户波动情况
function openBigareaCustWindow(param){
		/*if(s_shengData_h=='hidden'){
			return;
		};*/
		var startDate  = param.split(',')[0];
		var endDate  = param.split(',')[1];
		var regionId  = param.split(',')[2];
		var bdType  = param.split(',')[3];
		var showType="openBigareaCustWindow";
		var index = layer.open({
			type : 2,
			title : '客户波动表',
			maxmin : true,
			shadeClose : false, // 点击遮罩关闭层
			area : [ '800px', '520px' ],
			content : prefix  + '/reportFluctuateCust/'+startDate+"/"+endDate+"/"+regionId+"/"+bdType+"/"+showType, // iframe的url
			
			yes: function(index,layero){
		    	$(layero).find("iframe")[0].contentWindow.submitForm();
		    },
		    btn2: function(index){
		      layer.close(index);
		    }
		});
		layer.full(index);
}
//点击流失数或者新增数获取集团的客户波动情况
function openAllCustWindow(param){
	/*if(s_shengData_h=='hidden'){
		return;
	};*/
	var startDate  = param.split(',')[0];
	var endDate  = param.split(',')[1];
	var value  = param.split(',')[2];
	var bdType  = param.split(',')[3];
	var showType="openAllCustWindow";
	var index = layer.open({
		type : 2,
		title : '客户波动表',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix  + '/reportFluctuateCust/'+startDate+"/"+endDate+"/"+bdType+"/"+showType, // iframe的url
		
		yes: function(index,layero){
	    	$(layero).find("iframe")[0].contentWindow.submitForm();
	    },
	    btn2: function(index){
	      layer.close(index);
	    }
	});
	layer.full(index);

}
//波动表-点击省打开对应的市级数据
function openShengTable(Sheng){
	//var bigarea = Sheng.split(',')[0];
	var provinceid = Sheng.split(',')[0];
	//var cityid = Sheng.split(',')[2];
	var startDate = Sheng.split(',')[1];
	var endDate = Sheng.split(',')[2];
	var showType="city";
	var index = layer.open({
		type : 2,
		title : '城市表',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/reportFluctuateSheng/'+provinceid+"/"+startDate+"/"+endDate+"/"+showType,// iframe的url
		
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
