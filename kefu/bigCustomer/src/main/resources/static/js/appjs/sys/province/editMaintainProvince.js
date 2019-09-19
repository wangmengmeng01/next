var prefix = "/vipkf";
var cityIds;

function submitForm(){
	$('#signupForm').submit();
}
$(function() {
	getCityTreeData();
	validateRule();
});
$.validator.setDefaults({
	submitHandler : function() {
		getAllSelectNodes();
		update();
	}
});
function loadCityTree(cityTree) {
	$('#cityTree').jstree({
		"plugins" : [ "wholerow", "checkbox" ],
		'core' : {
			'data' : cityTree
		},
		"checkbox" : {
			//"keep_selected_style" : false,
			//"undetermined" : true
			//"three_state" : false,
			//"cascade" : ' up'
		}
	});
	$('#cityTree').jstree('open_all');
}
function getAllSelectNodes() {
	var ref = $('#cityTree').jstree(true); // 获得整个树
	cityIds = ref.get_selected(); // 获得所有选中节点的，返回值为数组
	$("#cityTree").find(".jstree-undetermined").each(function(i, element) {
		cityIds.push($(element).closest('.jstree-node').attr("id"));
	});
	console.log(cityIds); 
}
function getCityTreeData() {
	var provinceId = $('#provinceid').val();
	$.ajax({
		type : "GET",
		url : prefix+"/system/province/cityTree/" + provinceId,
		success : function(data) {
			loadCityTree(data);
		}
	});
}
function update() {
	$('#cityIds').val(cityIds);
	var province = $('#signupForm').serialize();
	$.ajax({
		cache : true,
		type : "POST",
		url : prefix+"/system/province/updateCity",
		data : province, // 你的formid
		async : false,
		error : function(request) {
			alert("Connection error");
		},
		success : function(r) {
			if (r.code == 0) {
				parent.layer.msg(r.msg);
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.msg(r.msg);
			}

		}
	});
}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			provincename : {
				required : true
			}
		},
		messages : {
			provincename : {
				required : icon + "请输入省总公司名"
			}
		}
	});
}