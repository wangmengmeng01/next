var prefix = "/vipkf/system/logger";

//查看日志
function path1btn() {
	var path = $("#path1").val();
	layer.open({
		type : 2,
		title : '日志等级',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '450px', '260px' ],
		content : prefix + '/show?path='+path 
	});
}

//变更日志
function path2btn() {
	var path = $("#path2").val();
	var level = $("#level").val();
	layer.open({
		type : 2,
		title : '变更等级',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '450px', '260px' ],
		content : prefix + '/change?path='+path+'&level='+level 
	});
}