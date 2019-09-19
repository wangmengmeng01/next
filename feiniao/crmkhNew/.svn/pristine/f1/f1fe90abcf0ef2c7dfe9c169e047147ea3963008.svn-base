var prefix = "/crmkh/report/reportCustRewardDetails";
//我们强烈推荐你在代码最外层把需要用到的模块先加载
layui.use(['layer', 'form', 'element'], function(){
	var layer = layui.layer,
	    form = layui.form,
	    element = layui.element
    //你的代码都应该写在这里面
});

$(function() {
	if(Chrome()){
		alert("各位领导,请使用谷歌浏览器进行操作!");
		return;
	}
	// 发送心跳检测,检测页面开关
	heartCheck();
	layui.use('laydate', function(){
		  var laydate = layui.laydate;
		//执行一个laydate实例
			laydate.render({
			    elem: '#searchStartDate', //指定元素
			    max:0,//设置最大范围内的日期时间值
			    showBottom: false
			});
			laydate.render({
			    elem: '#searchEndDate', //指定元素
				max:0,
				showBottom: false	
			});
			//给input赋值---开始时间默认T-1天，结束时间默认T-1天
		    $('#searchStartDate').val(GetDateStr(-1));
		    $('#searchEndDate').val(GetDateStr(-1));
		    
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

/**
 * 查询必须满足以下两种情况：
 * 1.开始时间和结束时间必须是同一年同一个月，不能跨域查询
 * 2.开始时间<结束时间
 */
$("#query").on("click",function(){//查询按钮的点击事件
	var startDate = $('#searchStartDate').val(),
		endDate = $('#searchEndDate').val(),
		startYear = startDate.substring(0,5),
		endYear = endDate.substring(0,5),
		startMonth = startDate.substring(5,7),
		endMonth = endDate.substring(5,7);
	var startTime = new Date(startDate.replace(/-/g,"/")).getTime(),
		endTime = new Date(endDate.replace(/-/g,"/")).getTime();
	var StartOrderSum = parseInt($("#searchStartOrderSum").val()),
		EndOrderSum = parseInt($("#searchEndOrderSum").val());
	if(!startDate || !endDate){
		layer.msg('查询时间不能为空！'); 
		return;
	}
	if(StartOrderSum>EndOrderSum){
		layer.msg('揽件量开始区间不能大于结束区间！'); 
		return;
	}
	if(startTime>endTime){
		layer.msg('开始时间不能大于结束时间！'); 
		return;
	}
	if( (startYear == endYear && startMonth !== endMonth) || startYear !== endYear){
		layer.msg('不能跨月查询！'); 
		return;
	}
	//$('#exampleTable').bootstrapTable('refresh');
	$("#exampleTable").bootstrapTable('destroy');
	load();
});


function load() {
	
/*	var addressIdMagicSuggest = $('#searchGs').magicSuggest({
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
        data: '/crmkh/report/reportCustRewardDetails/getCustBraData'
});*/
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
								customerId:$('#searchCustomerId').val(),
								startDate:$('#searchStartDate').val(),
								endDate:$('#searchEndDate').val(),
								startOrderSum:$('#searchStartOrderSum').val(),
								endOrderSum:$('#searchEndOrderSum').val(),
								customerSourceType:$('#searchCustomerSourceType').val(),
								gs:$('#searchGs').val()
								//gs:$('#searchGs').magicSuggest().getValue()[0]
							};
						},
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						columns : 
							[
								
								{
									field : 'recordId', 
									title : '序号'
								},
																{
									field : 'bigarea', 
									title : '大区' 
								},
																{
									field : 'provinceName', 
									title : '省名称' 
								},
																{
									field : 'cityName', 
									title : '城市名称' 
								},
																{
									field : 'mc', 
									title : '网点名称' 
								},
																{
									field : 'gs', 
									title : '网点编码' 
								},
																{
									field : 'branchCode', 
									title : '上级站点编码' 
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
									field : 'sellerId', 
									title : '商家ID' 
								},
																{
									field : 'sellerName', 
									title : '店铺名称' 
								},
																{
									field : 'showCustomerSourceType', title : '客户来源' ,align:'center'/*,
									formatter:function(value,row){
					                	 if(row.customerSourceType == "1"){
					                		 return "菜鸟";
					                	 }else if(row.customerSourceType == "2"){
					                		 return "二维码";
					                	 }else if(row.customerSourceType == "4"){
					                		 return "京东";
					                	 }else if(row.customerSourceType == "5"){
					                		 return "拼多多";
					                	 }
									}*/
								},
																{
									field : 'orderSum', 
									title : '揽件量' 
								},
																{
									field : 'orderAvg', 
									title : '日均揽件量' 
								},
																{
									field : 'allPriceSum', 
									title : '奖励金额' 
								},
																{
									field : 'showCustLevel', 
									title : '客户类别' ,align : 'center'/*,
									 formatter:function(value,row, index){
										 if(row.custLevel == "a"){
					                		 return "A类";
					                	 }else if(row.custLevel == "b"){
					                		 return "B类";
					                	 }else if(row.custLevel == "c"){
					                		 return "C类";
					                	 }else if(row.custLevel == "d"){
					                		 return "D类";
					                	 }else if(row.custLevel == "e"){
					                		 return "E类";
					                	 }else if(row.custLevel == "f"){
					                		 return "F类";
					                	 }else if(row.custLevel == "g"){
					                		 return "G类";
					                	 }
									}*/
								}
					 ]
					});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}

//导出excel
function exportExcel(action) {
	//layer.load(1);
/*	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly")//将input元素设置为readonly 
*/	var gs = $('#searchGs').val();
	
	  var url = prefix + action+"?startDate="+$('#searchStartDate').val()+"&endDate="+$('#searchEndDate').val()
	  			+"&customerId="+$('#searchCustomerId').val()+"&startOrderSum="+$('#searchStartOrderSum').val()
	  			+"&endOrderSum="+$('#searchEndOrderSum').val()+"&customerSourceType="+$('#searchCustomerSourceType').val()
	  			+"&gs="+gs;
	 /* var xhr = new XMLHttpRequest();
	  xhr.open('GET', url, true);    // 也可以使用POST方式，根据接口
	  xhr.send();
	  xhr.responseType = "json";  // 返回类型blob
*/	  // 发送ajax请求
	  // 定义请求完成的处理函数，请求前也可以增加加载框/禁用下载按钮逻辑
	 /* xhr.onload = function (res) {
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
	        a.download = '客户奖励明细表-'+nowTime+'.xlsx';
	        a.href = e.target.result;
	        $("body").append(a);  // 修复firefox中无法触发click
	        a.click();
	        $(a).remove();
	      }
	      
	    }
		  if (this.status === 200) {
			 if(res.code==0){
				 layer.msg(res.msg);
			 }else{
				 layer.msg(res.msg);
			 }
		  }
	    
	    layer.closeAll('loading');
	    $('#exportExcelbtn').attr("disabled",false);
	    $('#exportExcelbtn').removeAttr("readonly");//去除input元素的readonly属性
	  };*/
/*	  xhr.onload = function(e) {
		  if (this.status == 200) {
		    console.log('response', this.response); // JSON response  
		  }
		};
	  xhr.onreadystatechange = function () {  if(xhr.getResponseHeader('content-type')==='application/json'){
		  var data = xhr.responseText;
		  data=JSON.parse(data);//解析JSON为JS对象
		  alert(data);
      } else{
    	  var data = xhr.responseText;
		  data=JSON.parse(data);//解析JSON为JS对象
		  alert(data);
      }*/
	  var clock='';
	  $.ajax({
           url: url,
           dataType: 'json',
           method: 'GET',
           success: function(r) {
        	   if(r.code==902){
        		   layer.msg(r.message);
        	   }else if(r.code==903){
        		   layer.msg(r.message);
        	   }else if(r.code==904){
        		   layer.msg(r.message);
        	   }else{
        		   layer.msg("其他疑问");
        	   }
           },
          error: function(xhr) {
        	  layer.msg("网络阻塞");
          }
       })
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


function resetForm(){
	document.getElementById("searchRewardDetail").reset(); 
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
    	//状态码 850  报表生成中
		if(errcode == 850){
/*			if(tishiing==0){
				tishiing =1;
				//loading层
				tishi = layer.load(0, {time: 10*1000});
			}*/
			/*reLoad();*/
			//处理中,延迟一会自动请求
			//setTimeout(reLoad,1000);
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