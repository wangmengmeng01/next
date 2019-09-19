function getVcode(obj) {
	var url = $(obj).attr('src') + '&k=' + Math.random();
	$(obj).attr('src', url);
}

$(function() {
	$('#userName,#passWord,imgVcode').keydown(function(e) {
		if (e.keyCode == 13) {
			login();
		}
	});
	if (window.location.href != top.location.href) {
		top.location.href = window.location.href;
	}

	var ua = navigator.userAgent.toLowerCase();
	if (/(msie 6|msie 7|msie 8)/.test(ua)) {
		$('#IEWarn').show();
	}
	$.extend($.fn.validatebox.defaults.rules, {
		username : {
			validator : function(value, param) {
				return /^[1-9]\d{7,8}$/.test(value);
			},
			message : '请输入正确的登录工号!'
		},
		password : {
			validator : function(value, param) {
				return value.length > 2;
			},
			message : '登录密码至少3个字符！'
		},
		vcode : {
			validator : function(value, param) {
				return /^[A-z\d]{4}$/.test(value);
			},
			message : '请输入正确的验证码！'
		}
	})
})
$(function() {
	$('input.easyui-validatebox').validatebox('disableValidation')
	.focus(function() {
		$(this).validatebox('enableValidation');
	}).blur(function() {
		$(this).validatebox('validate')
	});
});
function login() {
	if ($('#login_form').form('validate')) {
		$.post('./index.php?r=site/login', $('#login_form').serialize(),
				function(data) {
					if (data.status) {
						top.location.href = './'
					}
					$('#login_msg').html(data.msg);
					getVcode($('#imgVcode'));
				}, 'json')
	}
}

function reset() {
	$('#login_form').form('clear');
}