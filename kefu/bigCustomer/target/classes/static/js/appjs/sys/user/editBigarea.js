var prefix = "/vipkf";
var bigareaNames;

function submitForm(){
	$('#signupForm').submit();
}
$(function() {
	getBigareaTreeData();
	validateRule();
});
$.validator.setDefaults({
	submitHandler : function() {
		getAllSelectNodes();
		update();
	}
});
function loadProvinceTree(bigareaTree) {
	$('#bigareaTree').jstree({
		"plugins" : [ "wholerow", "checkbox" ],
		'core' : {
			'data' : bigareaTree
		},
		"checkbox" : {
			//"keep_selected_style" : false,
			//"undetermined" : true
			//"three_state" : false,
			//"cascade" : ' up'
		}
	});
	$('#bigareaTree').jstree('open_all');
}
function getAllSelectNodes() {
	var ref = $('#bigareaTree').jstree(true); // 获得整个树
	bigareaNames = ref.get_selected(); // 获得所有选中节点的，返回值为数组
	$("#bigareaTree").find(".jstree-undetermined").each(function(i, element) {
		bigareaNames.push($(element).closest('.jstree-node').attr("id"));
	});
	console.log(bigareaNames); 
}
function getBigareaTreeData() {
	var userId = $('#userId').val();
	$.ajax({
		type : "GET",
		url : prefix+"/sys/user/bigareaTree/" + userId,
		success : function(data) {
			loadProvinceTree(data);
		}
	});
}
function update() {
	$('#bigareaNames').val(bigareaNames);
	var user = $('#signupForm').serialize();
	$.ajax({
		cache : true,
		type : "POST",
		url : prefix+"/sys/user/updateBigarea",
		data : user, // 你的formid
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
			username : {
				required : true
			}
		},
		messages : {
			username : {
				required : icon + "请输入用户名"
			}
		}
	});
}