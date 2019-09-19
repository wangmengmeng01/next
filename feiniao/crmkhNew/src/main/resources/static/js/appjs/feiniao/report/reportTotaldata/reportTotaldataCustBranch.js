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
						url : prefix + "/listCust", // 服务器数据的加载地址
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
								customerId:$('#customerId').val(),
								showType:'cust'

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
									{field:'customerId',title:'客户编码', width : 180,align:'center'},
									{field:'customerName',title:'客户名称', width : 180,align:'center',
			        		  			formatter:function(value,row, index){
						                	  if(row.customerName=='rqcw'){
						              			  layer.msg("无数据，不能选今天和今天之后");
						                	  }else{
						                		  return value;
						                	  }
			        		  			}
									},
									{field:'sellerId', title:'商家ID', align : 'center', width : 100},
								   	{field:'sellerName', title:'店铺名称', align : 'center', width : 100},
								   	{field:'branchCode',title:'上级公司编码', width : 180,align:'center'},
									{field:'branchName',title:'上级公司名称', width : 180,align:'center'},
									{field:'customerSourceType',title:'客户来源', width : 180,align:'center'},
									{field:'yjbm',title:'网点编码', width : 180,align:'center'},
									{field:'yjmc',title:'网点名称', width : 180,align:'center'},
									{field:'orderSum',title:'单量', width : 180,align:'center'},
									{field:'orderAvg',title:'平均单量', width : 180,align:'center'},
									{field:'priceLevel',title:'客户类别', width : 180,align:'center'}
/*									{field:'dianziPriceSumAvg',title:'日均奖励金额(/元)', width : 180,align:'center'},
									{field:'dianziPriceSum',title:'总奖励金额(/元)', width : 180,align:'center'}*/
								]
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
	var customerId = $('#customerId').val();
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
/*	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly");//将input元素设置为readonly
*/	
	//  var url = prefix + action;
	var url = prefix + "/exportExcelCustBranch"+"?start_date="+$('#start_date').val()+"&end_date="+$('#end_date').val()+"&qu_date="+$('#qu_date').val()
			+"&showType=cust"+"&customerId="+$('#customerId').val();

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
	        a.download = '总表-客户表-'+nowTime+'.xlsx';
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

function countDown(){
	var str='文件导出队列';
	$(document).find("a").each(function(i,ele){
		console.log(ele);
		if(ele.innerText == str){
			ele.click();
		}
	});
}
	



function resetPwd(id) {
}

