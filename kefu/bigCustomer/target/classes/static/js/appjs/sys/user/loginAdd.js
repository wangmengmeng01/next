var prefix = "/vipkf";

function submitForm(){
	$('#signupForm').submit();
}

$().ready(function() {
    //获取关注结构下拉框数据
    $.ajax({
        type:"GET",
        url:prefix+"/sys/loginUser/bigareaAndProvinceData",
        dataType: "json",
        success:function(res){
			// console.log(res);
            for(var key in res){
                $("#institution").append("<option value='" + res[key] + "'>" + res[key] + "</option>");
            }
        }
    });

    validateRule();
});

$.validator.setDefaults({
	submitHandler : function() {
		save();
	}
});
function getCheckedRoles() {
	var adIds = "";
	$("input:checkbox[name=role]:checked").each(function(i) {
		if (0 == i) {
			adIds = $(this).val();
		} else {
			adIds += ("," + $(this).val());
		}
	});
	return adIds;
}
function save() {
	$("#roleIds").val(getCheckedRoles());
	$.ajax({
		cache : true,
		type : "POST",
		url : prefix+"/sys/loginUser/save",
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






// // 验证手机号
jQuery.validator.addMethod("mobile", function (value, element) {
    var phone = $("input[name='mobile']").val();
    if(null==phone || phone==""){
    	return true;
	}
    var pattern = /^1[34578]\d{9}$/;
    return (pattern.test(phone));
},"请输入正确的手机号");

// // 验证身份证号
jQuery.validator.addMethod("idcdno", function (value, element) {
    var card = $("input[name='idcdNo']").val();
    if(card=="" || null==card){
    	return true;
	}
    var pattern = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    return pattern.test(card);
},"请输入正确的身份证号");
// // 验证mac
jQuery.validator.addMethod("macadress", function (value, element) {
    var mac = $("input[name='macAdress']").val();
    if(null==mac || mac==""){
        return true;
    }
    // var pattern = /[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}-[A-F\d]{2}/;
   var pattern= /^([0-9a-fA-F]{2})(([/\s-][0-9a-fA-F]{2}){5})$/;
    return pattern.test(mac);
}, "请输入正确的mac地址");

jQuery.validator.addMethod("wirelessMacAdress", function (value, element) {
    var mac = $("input[name='wirelessMacAdress']").val();
    if(null==mac || mac==""){
        return true;
    }
    var pattern = /^([0-9a-fA-F]{2})(([/\s-][0-9a-fA-F]{2}){5})$/;
    return pattern.test(mac);
}, "请输入正确的mac地址");
















function validateRule() {
	var icon = "<i class='fa fa-times-circle'></i> ";
	$("#signupForm").validate({
		rules : {
			name : {
				required : true,
                maxlength : 10,

            },
			username : {
				required : true,
				// minlength : 2,
				maxlength : 10,
				remote : {
					url : prefix+"/sys/user/exit", // 后台处理程序
					type : "post", // 数据发送方式
					dataType : "json", // 接受数据格式
					data : { // 要传递的数据
						username : function() {
							return $("#username").val();
						}
					}
				}
			},
			password : {
				required : true,
				minlength : 6
			},
			confirm_password : {
				required : true,
				minlength : 6,
				equalTo : "#password"
			},
			email : {
				required : true,
				email : true
			},
			topic : {
				required : "#newsletter:checked",
				minlength : 2
			},





            mobile: {
                // required: true,
                minlength: 11,
                maxlength: 11,
                mobile:true
            },
            idcdNo: {
                idcdno: true
            },
            macAdress: {
                macadress: true
            },

            wirelessMacAdress :{
                wirelessMacAdress: true
			},

            role :{
                required : true
            },

            institution :{
                required : true
            },











			agree : "required"
		},
		messages : {

			name : {
				required : icon + "请输入姓名",
                maxlength : icon+"姓名不能超过10个字符",
			},
			username : {
				required : icon + "请输入您的用户名",
				// minlength : icon + "用户名必须两个字符以上",
				maxlength : icon+"用户名不能超过10个字符",
				remote : icon + "用户名已经存在"
			},
			password : {
				required : icon + "请输入您的密码",
				minlength : icon + "密码必须6个字符以上"
			},
			confirm_password : {
				required : icon + "请再次输入密码",
				minlength : icon + "密码必须6个字符以上",
				equalTo : icon + "两次输入的密码不一致"
			},










            mobile: {
                required: icon + "请输入手机号",
                maxlength:icon +"请填写11位的手机号",
                minlength: icon + "请填写11位的手机号",
                mobile:icon +"请填写正确的手机号码"
            },
            idcdno:{
                idcdno:icon +"请填写正确的身份证号码"
            },
            macadress: {
                macadress: icon + "请输入正确的mac地址"
            },
            wirelessMacAdress: {
                wirelessMacAdress: icon + "请输入正确的mac地址"
            },
            role: {
                required: icon + "请选择所属角色",
            },
            institution: {
                required: icon + "请选择所属机构",
            },












			email : icon + "请输入您的E-mail",
		}
	})
}

var openDept = function(){
	layer.open({
		type:2,
		title:"选择部门",
		area : [ '300px', '450px' ],
		content:prefix+"/system/sysDept/treeView"
	})
};
function loadDept( deptId,deptName){
	$("#deptId").val(deptId);
	$("#deptName").val(deptName);
}