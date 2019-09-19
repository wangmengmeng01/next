var prefix = "/crmkh/costreport/costreportCustRouteIncome";
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
//     $("#refresh").click(function(){ $('#exampleTable').bootstrapTable('refresh'); }); 
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
						
						/*//grimm 双击触发的事件
	            		onDblClickRow: function (row) {
	            			edit(row.id);
	            		},*/
	            		
						queryParams : function(params) {
							var customerId = $('#searchCustomerId').val(); 
							var accountDt = $('#searchAccountDt').val();
							var startProvinceId = $('#searchStartProvinceId').val();
							var endProvinceId = $('#searchEndProvinceId').val();
							$('#limitvalue').val(params.limit);
							$('#offsetvalue').val(params.offset);
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
								customerId:customerId, 
								accountDt:accountDt, 
								startProvinceId:startProvinceId,
								endProvinceId:endProvinceId
							};
						},
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						columns :[
									 [
										 {"title":"","colspan":1},
										 {"title":"","colspan":1},
								         {"title":"省份",align:'center',"colspan":2},
								         {"title":"收入",align:'center',"colspan":3}
							         ],
									
							         [
								           								{
									       	field : 'customerId', 
									       	title : '客户编码' 
								         },
																		{
											field : 'customerName', 
											title : '客户名称' 
										 },

																		{
											field : 'startProvinceName', 
											title : '始发省' 
										 },

																		{
											field : 'endProvinceName', 
											title : '目的省' 
										 },
																		{
											field : 'orderSum', 
											title : '票数' ,
											formatter:function(value,row, index){
												if(row.startProvinceName !='合计')
													//return "<a href='javascript:void(0)' style='color:blue; onclick='openOrderDetail(\""+row.accountDt+","+row.branchCode+","+row.customerId+","+row.startProvinceId+","+row.endProvinceId+","+row.customerName+"\");'>"+row.orderSum+"</a>";
												return "<a href='javascript:void(0);' style='color:blue;' onclick='openOrderDetail(\""+row.accountDt+","+row.branchCode+","+row.customerId+","+row.startProvinceId+","+row.endProvinceId+","+row.customerName+"\");'>"+value+'</a>';    
							                	  else
							                	  return value;
											}
										 },
																		{
											field : 'weight', 
											title : '重量' 
										 },
																		{
											field : 'fee', 
											title : '运费' 
										 },
											{
												field : 'startProvinceId', 
												title : '始发省ID' ,
												visible:false
											 },
												{
													field : 'endProvinceId', 
													title : '目的省ID',
													visible:false
												 }
								     ]
								 ]
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


//点击票数，获取收入明细
function openOrderDetail(detail){
	var accountDt = detail.split(',')[0];
	var branchCode = detail.split(',')[1];
	var customerId = detail.split(',')[2];
	var startProvinceId = detail.split(',')[3];
	var endProvinceId = detail.split(',')[4];
	var customerName = detail.split(',')[5];
	var index = layer.open({
		type : 2,
		title : customerName+'客户-收入报表',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/getIncomeDetailHtml/'+accountDt+"/"+branchCode+"/"+customerId+"/"+startProvinceId+"/"+endProvinceId,// iframe的url
		
		yes: function(index,layero){
	    	$(layero).find("iframe")[0].contentWindow.submitForm();
	    },
	    btn2: function(index){
	      layer.close(index);
	    }
	});
	layer.full(index);
}

//导出excel 收入报表
function exportExcel(action) {
	var limit =  $('#limitvalue').val();
	var offset = $('#offsetvalue').val();
	layer.load(1);
	$('#exportExcelbtn').attr("disabled",true);
	$('#exportExcelbtn').attr("readonly","readonly");//将input元素设置为readonly
	
	  var url = prefix + action+"?startProvinceId="+$('#searchStartProvinceId').val()+"&endProvinceId="+$('#searchEndProvinceId').val()+
	  			"&customerId="+$('#searchCustomerId').val()+"&accountDt="+$('#searchAccountDt').val()+"&limit="+limit+"&offset="+offset;
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
	        a.download = '收入报表-'+nowTime+'.xlsx';
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

function resetForm(){
	document.getElementById("signupForm").reset(); 
}
