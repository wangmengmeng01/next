var prefix = "/crmkh/warning/warningBenchmark"
$().ready(function() {
	
	//获取机构名称下拉框数据
    $.ajax({
        type:"GET",
        url:"/crmkh/sys/loginUser/bigareaAndProvinceData",
        dataType: "json",
        success:function(res){
			// console.log(res);
            for(var key in res){
                $("#jgmc").append("<option value='" + res[key] + "'>" + res[key] + "</option>");
            }
        }
    });
	
	validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		update();
	}
});
function update() {
	$.ajax({
		cache : true,
		type : "POST",
		url : prefix + "/update",
		data : $('#signupForm').serialize(),// 你的formid
		async : false,
		error : function(request) {
			parent.layer.alert("Connection error");
		},
		success : function(data) {
			if (data.code == 0) {
				parent.layer.msg("操作成功");
				parent.reLoad();
				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
				parent.layer.close(index);

			} else {
				parent.layer.alert(data.msg)
			}

		}
	});

}
function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			name : {
				required : true
			}
		},
		messages : {
			name : {
				required : icon + "请输入名字"
			}
		}
	})
}