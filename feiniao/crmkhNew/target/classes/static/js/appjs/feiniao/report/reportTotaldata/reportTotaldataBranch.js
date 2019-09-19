var prefix = "/crmkh/report/reportTotaldata";
var	timeInterval;
	$(function() {
		if(Chrome()){
			alert("各位领导,请使用谷歌浏览器进行操作!");
			return;
		}
		// 发送心跳检测,检测页面开关
		heartCheck();
//		load();
		$("#date_style").on("change",function(){
			var timeType = $(this).val();
			switch(timeType){
				case "1":
					$("#group_date").show().siblings(":not('#group_select,#group-querybtn')").hide().find("input").val("").end().find("select option:first").prop("selected",true);
					break;
				case "2":
					$("#group_month").show().siblings(":not('#group_select,#group-querybtn')").hide().find("input").val("").end().find("select option:first").prop("selected",true);
					break;
				case "3":
					$("#group_quarter").show().siblings(":not('#group_select,#group-querybtn')").hide().find("input").val("").end().find("select option:first").prop("selected",true);
					break;
				case "4":
					$("#group_year").show().siblings(":not('#group_select,#group-querybtn')").hide().find("input").val("").end().find("select option:first").prop("selected",true);
					break;
				case "5":
					$("#group_time").show().siblings(":not('#group_select,#group-querybtn')").hide().find("input").val("").end().find("select option:first").prop("selected",true);
					break;
				default:
					$("#group_query").children().not("#group_select,#group-querybtn").hide().find("input").val("").end().find("select option:first").prop("selected",true);
					break;
			}
		});
		//上传excel按钮
	    layui.use(['laydate','laypage','layer','form','upload'], function () {
	    	$=layui.jquery;
	    	var laydate = layui.laydate;
	    	layer = layui.layer;
	    	form = layui.form;
	    	
	    	laydate.render({
	             	elem: '#qu_date' //指定元素
	    	       ,showBottom: false

	             });
	    	laydate.render({
		         	elem: '#start_date' //指定元素

		         });
	    	laydate.render({
		         	elem: '#end_date' //指定元素

		         });
	    	laydate.render({
	            	elem: '#month_year' //指定元素
	            	,type: 'month'
	            });
	    	laydate.render({
	                elem: '#quarter_year' //指定元素
	                ,type: 'year'

	            });
	    	laydate.render({
	            elem: '#year' //指定元素
	            ,type: 'year'
	        });
			//给input赋值---开始时间默认当前时间，结束时间默认当前时间
//		    $('#start_date').val(GetDateStr(0));
//		    $('#end_date').val(GetDateStr(0));  
		    $('#qu_date').val(GetDateStr(-1));  
		    var select = document.getElementById('date_style');
		    select.options[1].selected = true;
			$("#group_date").show().siblings(":not('#group_select,#group-querybtn')").hide().find("input").val("").end().find("select option:first").prop("selected",true);
	        load();
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
     
/*	     //导出按钮
	     $('#exportExcelbtn').click(function(){  
	         var $eleForm = $("<form method='post' enctype='application/x-www-form-urlencoded'><input type='hidden' name='date_style' value="+$("#date_style").find("option:selected").attr("value")+"><input type='hidden' name='qu_date' value="+ $('#qu_date').val()+"><input type='hidden' name='month_year' value="+ $('#month_year').val()+"><input type='hidden' name='quarter_year' value="+ $('#quarter_year').val()+"><input type='hidden' name='quarter_date' value="+ $('#quarter_date').val()+"><input type='hidden' name='year' value="+ $('#year').val()+"><input type='hidden' name='start_date' value="+ $('#start_date').val()+"><input type='hidden' name='end_date' value="+ $('#end_date').val()+"></form>");  
	       
	         $eleForm.attr("action",prefix+"/exportExcel");  
	       
	         $(document.body).append($eleForm);  
	       
	         //提交表单，实现下载  
	         $eleForm.submit();  
	     }); */	
	
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

function tab(date1,date2){
    var oDate1 = new Date(date1);
    var oDate2 = new Date(date2);
    if(oDate1.getTime() > oDate2.getTime()){
		layer.msg("起始日期不能大于结束日期");
        return false;
    }
}

function load() {
//	if ($('#qu_date').val()==""&&$('#start_date').val()==""&&$('#end_date').val()==""
//		&&$('#month_year').val()==""&&$('#quarter_year').val()==""&&$('year').val()=="") {
//			layer.msg("请选择日期查询");
//			return false;
//	}
//	if($('#start_date').val()!=""&&$('#end_date').val()!=""){
//		var start = $('#start_date').val();
//		var end   = $('#end_date').val();
//		var result = end.split('-')[1] - start.split('-')[1];
//		if(result != 0){
//			layer.msg("不能跨月选择");
//			return false;			
//		}
//		tab(start,end);
//	}
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/listBranch", // 服务器数据的加载地址
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
						pageSize : 30, // 如果设置了分页，每页数据条数
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
								date_style:$("#date_style").find("option:selected").attr("value"),
								qu_date:$('#qu_date').val(),
								month_year:$('#month_year').val(),
								quarter_year:$('#quarter_year').val(),
								quarter_date:$('#quarter_date').val(),
								year:$('#year').val(),
								start_date:$('#start_date').val(),
								end_date:$('#end_date').val(),
								provinceId:$('#provinceId').val(),
								cityId:$('#cityId').val(),
								gsbm:$('#gsbm').val(),
								showType:'branch'

							};
						},
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						frozenColumns : [ [ 
							               //    {field : 'ck', checkbox : true, align : 'center'},
							                   //{field : 'id', title : 'id', hidden : true},
										              //     {field : 'orderSum', title : '总单量', hidden : true},
										             //      {field : 'orderAvg', title : '日均单量', hidden : true},
										                   {field : 'bigarea', title : '大区',  hidden : true},
										                   {field : 'provinceID', title : '省', hidden : true}
							        		   ]
							                ] ,
							        		columns : [ [   
							        		   		  {field : 'customerName', title :  '地域', align : 'center', rowspan : 2,
							        		  			formatter:function(value,row, index){
															if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt'){
										                	  if(row.customerName=='rqcw'){
										              			  layer.msg("无数据，不能选今天和今天之后");
											              			var btn = $("#queryData");
											              			btn.button('reset');
										                		  window.clearInterval(timeInterval);//关闭定时器
										                	  }
										                	  if(row.customerName=='bt'){
										                		  var btn = $("#queryData");
											              			btn.button('reset');
										                		  window.clearInterval(timeInterval);//关闭定时器
										                	  }
																return "--";
															} else if(row.customerName.indexOf("合计")!=-1||row.customerName.indexOf("+")!=-1){
																var btn = $("#queryData");
										              			btn.button('reset');
																window.clearInterval(timeInterval);//关闭定时器
																 return value;
															 }else if(row.customerName.indexOf("wddl")!=-1){	
																 var btn = $("#queryData");
											              			btn.button('reset');
																 window.clearInterval(timeInterval);//关闭定时器
																 openCustomerTable(row.customerName);
																 return "--";
															 }
										                	  else{
										                		  var btn = $("#queryData");
											              			btn.button('reset');
										                		  window.clearInterval(timeInterval);//关闭定时器
         							                              return value;  
										                	  }
										                     // return value;  
														}
							        				  },
							        		   		  {field : 'orderSum', title :  '总单量', align : 'center', rowspan : 2,
								        		  			formatter:function(value,row, index){
																if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
											                	  return "--";
											                	  else
											                      return value;  
															}
							        				  },
							        		   		  {field : 'orderAvg', title :  '日均单量', align : 'center', rowspan : 2,
								        		  			formatter:function(value,row, index){
																if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
											                	  return "--";
											                	  else
											                      return value;  
															}
							        				  },
									        		  {field : 'yewuliang',title : '业务量类型', align : 'center', colspan : 3},
							        		   	      {field : 'dianzimiandan',title : '电子面单总量', align : 'center', colspan : 4},
							        		   	      {field : 'alei',title : 'A类', align : 'center', colspan : 6},
							        		   	      {field : 'blei',title : 'B类', align : 'center', colspan : 6},

							        		   	      {field : 'clei',title : 'C类', align : 'center', colspan : 6},
							        		   	      {field : 'dlei',title : 'D类', align : 'center', colspan : 6},

							        		   	      {field : 'elei',title : 'E类', align : 'center', colspan : 6},
							        			      {field : 'flei',title : 'F类', align : 'center', colspan : 6},
							        			      {field : 'glei',title : 'G类', align : 'center', colspan : 6}				        		             
							        		             
							        		             ],[
										                   {field : 'dianziOrderSum', width : 100, title : '电子面单',align : 'center',
								     		                	  formatter:function(value,row, index){
																		if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
													                	  return "--";
													                	  else
													                      return value;  
																	}
							     		                   },
										                   {field : 'ordinaryOrderSum', width : 100, title : '普通面单',align : 'center',
								     		                	  formatter:function(value,row, index){
																		if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
													                	  return "--";
													                	  else
													                      return value;  
																	}
							     		                   },
										                   {field : 'dianziPercent', width : 100, title : '电子面单占比',align : 'center',
								     		                	  formatter:function(value,row, index){
																		if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
													                	  return "--";
													                	  else
													                      return value;  
																	}
							     		                   },
										                   {field : 'customerSum', width : 100, title : '客户数',align : 'center',
								     		                	  formatter:function(value,row, index){
																		if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
													                	  return "--";
													                	  else
													                      return value;  
																	}
							     		                   },
							     		                   /*{field : 'customerAvgSum', width : 100, title : '日均单量',align : 'center',
								     		                	  formatter:function(value,row, index){
																		if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
													                	  return "--";
													                	  else
													                      return value;  
																	}
							     		                   },*/
							     		                  {field : 'dianziOrderSumAvg', width : 100, title : '日均单量',align : 'center',
								     		                	  formatter:function(value,row, index){
																		if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
													                	  return "--";
													                	  else
													                      return value;  
																	}
							     		                   },
							     		                  /* {field : 'customerPriceSum', width : 100, title : '日均奖励金额(/元)',align : 'center',
								     		                	  formatter:function(value,row, index){
																		if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
													                	  return "--";
													                	  else
													                      return value;  
																	}
							     		                   },*/
							     		                  {field : 'dianziPriceSumAvg', width : 100, title : '日均奖励金额(/元)',align : 'center',
								     		                	  formatter:function(value,row, index){
																		if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
													                	  return "--";
													                	  else
													                      return value;  
																	}
							     		                   },
							     		                   {field : 'dianziPriceSum', width : 100, title : '总奖励金额(/元)',align : 'center',
								     		                	  formatter:function(value,row, index){
																		if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
													                	  return "--";
													                	  else
													                      return value;  
																	}
							     		                   },
										                   {field : 'acustomerSum', width : 100, title : '客户数',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'aorderAvg', width : 100, title : '日均单量', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'aorderSum', width : 100, title : '总单量', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'apricePercent', width : 100, title : '单量占比', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'apriceSum', width : 100, title : '日均奖励金额(/元)',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'aallPriceSum', width : 100, title : '总奖励金额(/元)',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },

										                   {field : 'bcustomerSum', width : 100, title : '客户数', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'borderAvg', width : 100, title : '日均单量',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'borderSum', width : 100, title : '总单量',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'bpricePercent', width : 100, title : '单量占比', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'bpriceSum', width : 100, title : '日均奖励金额(/元)', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'ballPriceSum', width : 100, title : '总奖励金额(/元)',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },								        		  
										        		
										                   {field : 'ccustomerSum', width : 100, title : '客户数',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'corderAvg', width : 100, title : '日均单量', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'corderSum', width : 100, title : '总单量', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'cpricePercent', width : 100, title : '单量占比',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'cpriceSum', width : 100, title : '日均奖励金额(/元)', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'callPriceSum', width : 100, title : '总奖励金额(/元)', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
											   
										                   {field : 'dcustomerSum', width : 100, title : '客户数', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'dorderAvg', width : 100, title : '日均单量', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'dorderSum', width : 100, title : '总单量', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'dpricePercent', width : 100, title : '单量占比',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'dpriceSum', width : 100, title : '日均奖励金额(/元)', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'dallPriceSum', width : 100, title : '总奖励金额(/元)', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },

										                   {field : 'ecustomerSum', width : 100, title : '客户数', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'eorderAvg', width : 100, title : '日均单量',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'eorderSum', width : 100, title : '总单量', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'epricePercent', width : 100, title : '单量占比', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'epriceSum', width : 100, title : '日均奖励金额(/元)', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'eallPriceSum', width : 100, title : '总奖励金额(/元)',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },

										                   {field : 'fcustomerSum', width : 100, title : '客户数', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'forderAvg', width : 100, title : '日均单量',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'forderSum', width : 100, title : '总单量',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'fpricePercent', width : 100, title : '单量占比',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'fpriceSum', width : 100, title : '日均奖励金额(/元)',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'fallPriceSum', width : 100, title : '总奖励金额(/元)',align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },

										                   {field : 'gcustomerSum', width : 100, title : '客户数', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'gorderAvg', width : 100, title : '日均单量', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'gorderSum', width : 100, title : '总单量', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'gpricePercent', width : 100, title : '单量占比', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'gpriceSum', width : 100, title : '日均奖励金额(/元)', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   },
										                   {field : 'gallPriceSum', width : 100, title : '总返利金额(/元)', align : 'center',
							     		                	  formatter:function(value,row, index){
																	if(row.customerName=='no'||row.customerName=='wait'||row.customerName=='rqcw' ||row.customerName=='bt')
												                	  return "--";
												                	  else
												                      return value;  
																}
							     		                   }
											                   ]]
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

function query() {
	var provinceId = $('#provinceId').val();
	var cityId = $('#cityId').val();
	var gsbm = $('#gsbm').val();
	var timeType = $('#date_style').val();
	if(timeType==1 &&$('#qu_date').val()==""){		
		layer.msg("请选择日期查询");
		return false;

	}
	if(timeType==2 &&$('#month_year').val()==""){		
		layer.msg("请选择日期查询");
		return false;

	}
	if(timeType==3 &&$('#quarter_year').val()==""){		
		layer.msg("请选择日期查询");
		return false;

	}
	if(timeType==4 &&$('year').val()==""){		
		layer.msg("请选择日期查询");
		return false;

	}
	if(timeType==5 &&($('#start_date').val()==""||$('#end_date').val()=="")){		
		layer.msg("请选择日期查询");
		return false;

	}
	if($('#start_date').val()!=""&&$('#end_date').val()!=""){
		var start = $('#start_date').val();
		var end   = $('#end_date').val();
		var result = end.split('-')[1] - start.split('-')[1];
		if(result != 0){
			layer.msg("不能跨月查询!");
			return false;			
		}
		tab(start,end);
	}
	 
	$("#exampleTable").bootstrapTable('destroy');
	load();
}

function resetForm(){
    var select = document.getElementById('date_style');
    select.options[0].selected = true;
    var quarter = document.getElementById('quarter_date');
    quarter.options[0].selected = true;
    
	$("#qu_date").val("");
	$("#month_year").val("");
	$("#quarter_year").val("");
	$("#year").val("");
	$("#start_date").val("");
	$("#end_date").val("");
 }

//导出excel
function exportExcel(action) {
	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly");//将input元素设置为readonly
	
	  //var url = prefix + action;
	var url = prefix + "/exportExcelBranch"+"?start_date="+$('#start_date').val()+"&end_date="+$('#end_date').val()+"&qu_date="+$('#qu_date').val()
	+"&showType=branch"+"&provinceId="+$('#provinceId').val()+"&cityId="+$('#cityId').val()+"&gsbm="+$('#gsbm').val();
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
	        a.download = '总表-网点-'+nowTime+'.xlsx';
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


function resetPwd(id) {
}
