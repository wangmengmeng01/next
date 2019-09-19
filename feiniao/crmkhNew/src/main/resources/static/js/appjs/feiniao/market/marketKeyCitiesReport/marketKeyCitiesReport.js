var prefix = "/crmkh/market/marketKeyCitiesReport";
$(function() {
	
	   layui.use(['laydate','laypage','layer','form','upload'], function () {
	    	$=layui.jquery;
	    	var laydate = layui.laydate;
	    	layer = layui.layer;
	    	form = layui.form;
	    	laydate.render({
	            	elem: '#searchDate' //指定元素
	            	,type: 'month'
	            });
	    });
	   
		 //给input赋值---开始时间默认当前时间，结束时间默认当前时间
		$('#searchDate').val(GetDateStr(0));
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
//js获取当前日期前后N天的方法:
function GetDateStr(AddDayCount) { 
    var date = new Date(); 
    date.setDate(date.getDate()+AddDayCount);
    var y = date.getFullYear(); 
    var m = changeTime(date.getMonth());
    return y+"-"+m; 
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
					//	pagination : true, // 设置为true会在底部显示分页条
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
						
						//grimm 双击触发的事件
	            		onDblClickRow: function (row) {
	            			edit(row.id);
	            		},
	            		
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
								searchDate:$('#searchDate').val()
							};
						},
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						columns : [
						           [
									{
										field : 'basicalInformation', title : '基础信息', align : 'center', rowspan : 2, width : 180,
										formatter:function(value,row, index){
			        		  				if(row.colorFlag == 1){
			        		  					return '<span style="color:#ff2a00">'+value+'</span>';
			        		  				}else if(row.colorFlag == 2){
			        		  					return '<span style="color:#6084bf">'+value+'</span>';
			        		  				}else if(row.colorFlag == 3){
			        		  					return '<span style="color:#ab8700">'+value+'</span>';
			        		  				}else{
			        		  					return  value;
			        		  				}
										}

									},
									{
										field : 'responsiblePeople', title : '考核对象', align : 'center', rowspan : 2, width : 180  
									},
									{
										field : 'monthScore', title : '考核得分', align : 'center', rowspan : 2, width : 180  
									},
									{
										field : 'ab', title : 'A/B', align : 'center', rowspan : 2, width : 180  
									},
									{
										field : 'cd', title : 'C/D', align : 'center', rowspan : 2, width : 180  
									},
									{field : '',title : '快递行业', align : 'center', colspan : 2},
			        		   	      {field : '',title : '韵达', align : 'center', colspan : 3},

			        		   	      {field : '',title : '中通', align : 'center', colspan : 3},
			        		   	      {field : '',title : '圆通', align : 'center', colspan : 3},

			        		   	      {field : '',title : '申通', align : 'center', colspan : 3},
			        			      {field : '',title : '百世', align : 'center', colspan : 3}
						           ],
						           [
																{
									field : 'kdqsrjdl', 
									title : '日均量' 
								},
																{
									field : 'kdqsyzl', 
									title : '月总量' 
								},
																{
									field : 'ydqsyzl', 
									title : '月总量' 
								},
																{
									field : 'ydqsrjl', 
									title : '日均量' 
								},
																{
									field : 'ydqsscfezb', 
									title : '市场份额占比' ,
									formatter:function(value,row, index){
		        		  				if(row.zbName.indexOf("ydqsscfezb") != -1){
		        		  					return '<span style="color:#ff2a00">'+value+'</span>';
		        		  				}else{
		        		  					return  value;
		        		  				}
									}
								},
																{
									field : 'ztqsyzl', 
									title : '月总量' 
								},
																{
									field : 'ztqsrjl', 
									title : '日均量' 
								},
																{
									field : 'ztqsscfezb', 
									title : '市场份额占比',
									formatter:function(value,row, index){
		        		  				if(row.zbName.indexOf("ztqsscfezb") != -1){
		        		  					return '<span style="color:#ff2a00">'+value+'</span>';
		        		  				}else{
		        		  					return  value;
		        		  				}
									}
								},
																{
									field : 'ytqsyzl', 
									title : '月总量' 
								},
																{
									field : 'ytqsrjl', 
									title : '日均量' 
								},
																{
									field : 'ytqsscfezb', 
									title : '市场份额占比' ,
									formatter:function(value,row, index){
		        		  				if(row.zbName.indexOf("ytqsscfezb") != -1){
		        		  					return '<span style="color:#ff2a00">'+value+'</span>';
		        		  				}else{
		        		  					return  value;
		        		  				}
									}
								},
																{
									field : 'stqsyzl', 
									title : '月总量' 
								},
																{
									field : 'stqsrjl', 
									title : '日均量' 
								},
																{
									field : 'stqsscfezb', 
									title : '市场份额占比' ,
									formatter:function(value,row, index){
		        		  				if(row.zbName.indexOf("stqsscfezb") != -1){
		        		  					return '<span style="color:#ff2a00">'+value+'</span>';
		        		  				}else{
		        		  					return  value;
		        		  				}
									}
								},
																{
									field : 'bsqsyzl', 
									title : '月总量' 
								},
																{
									field : 'bsqsrjl', 
									title : '日均量' 
								},
																{
									field : 'bsqsscfezb', 
									title : '市场份额占比' ,
									formatter:function(value,row, index){
		        		  				if(row.zbName.indexOf("bsqsscfezb") != -1){
		        		  					return '<span style="color:#ff2a00">'+value+'</span>';
		        		  				}else{
		        		  					return  value;
		        		  				}
									}
								}
								]]
					});
}


function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}
function query() {	
	$("#exampleTable").bootstrapTable('destroy');
	load();
}
//导出excel
function exportExcel(action) {
	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly");//将input元素设置为readonly
	
	  var url = prefix + action+"?searchDate="+$('#searchDate').val();
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
	        a.download = '重点城市对比表-'+nowTime+'.xlsx';
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
function resetForm(){
	document.getElementById("signupForm").reset(); 
}
