/*$(function () {
    //$('#mac').val(localStorage.getItem("mac"));
    $('#mac').val("00:50:56:c0:00:01");
});

// 人脸识别校验
var video = document.getElementById("video"); // 获取video标签
var context;
if(video){
    context = canvas.getContext("2d");
}
var con = {
    audio: false,
    video: {
        width: 1980,
        height: 1024,
    }
};

if (video && context) {
    navigator.mediaDevices.getUserMedia(con).then(function (stream) {
        try {
            video.src = window.URL.createObjectURL(stream);
        } catch (e) {
            console.log(e);
            video.srcObject = stream;
        }
        video.onloadmetadate = function (e) {
            video.play();
        }
    });
}

function getBase64() {
    var imgSrc = document.getElementById("canvas").toDataURL("image/png");
    if(imgSrc){
        return imgSrc.split("base64,")[1];
    }
}

// 人脸识别校验

// 短信验证登录
var InterValObj; // timer变量，控制时间
var count = 60; // 间隔函数，1秒执行
var curCount;// 当前剩余秒数
var code = ""; // 验证码
var codeLength = 6;// 验证码长度
function sendMessage() {
    curCount = 60;
    var phone = $("#phone").val();// 手机号码
    if (phone != "") {
        // 设置button效果，开始计时
        $("#btnSendCode").attr("disabled", "true");
        $("#btnSendCode").val("请在" + curCount + "秒内输入验证码");
        InterValObj = window.setInterval(SetRemainTime, 1000); // 启动计时器，1秒执行一次
        // 向后台发送处理数据
        $.ajax({
            type: "POST", // 用POST方式传输
            dataType: "json", // 数据格式:JSON
            async: false,
            url: '/vipkf/smsLogin', // 目标地址
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            },
            success: function (data) {
                if (data.code == 200) {
                    layer.msg("短信已成功发送,请填写");
                } else {
                    layer.msg("短信发送失败");
                }
            }
        });
    } else {
        layer.msg("手机号码不能为空！");
    }
}

// timer处理函数
function SetRemainTime() {
    if (curCount == 0) {
        window.clearInterval(InterValObj);// 停止计时器
        $("#btnSendCode").removeAttr("disabled");// 启用按钮
        $("#btnSendCode").val("重新发送验证码");
        code = ""; // 清除验证码。如果不清除，过时间后，输入收到的验证码依然有效
    } else {
        curCount--;
        $("#btnSendCode").val("请在" + curCount + "秒内输入验证码");
    }
}

// 短信验证登录
// 全程录像

var username;
$(document).ready(function () {
    $("#id_card").blur(function () {
        $("#hint_idCard").html("").hide();
    });
    $("#message_valid").blur(function () {
        $("#hint_messagevalid").html("").hide();
    });
    var val = $('#safeLevel').val();
    var arr = [];
    arr = val.split(',');
    username = arr[arr.length - 1];
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function () {

        login();
    }
});

function login() {
    var IDCard = $("#id_card").val();
    IDCard = (typeof IDCard == "undefined" || IDCard == null || IDCard == "") ? "" : IDCard;
    var messageValid = $("#message_valid").val();
    messageValid = (typeof messageValid == "undefined" || messageValid == null || messageValid == "") ? "" : messageValid;
    var mac = $("#mac").val();
    mac = (typeof mac == "undefined" || mac == null || mac == "") ? "" : mac;

    var base;
    if(context){
        context.drawImage(video, 0, 0, 400, 300);
        base = getBase64();
        layer.msg('人脸校验中,请稍后.....', {
            time: 1000
        }, function(){});
    }

    $.ajax({
        type: "POST",
        url: "/vipkf/system/validLogin",
        async: false,
        data: {
            "id_card": IDCard,
            "message_valid": messageValid,
            "username": username,
            "base": base,
            "mac": mac
        },
        success: function (r) {
            if (r.code == 200) {
                $("#hint_messagevalid").html("").hide();
                $("#hint_idCard").html("").hide();
                $("#mac").html("").hide();
                $("#hint_facevalid").html("").hide();

                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);
            } else if (r.code == 906 || r.code == 910) {
                $("#hint_messagevalid").html("").hide();
                $("#hint_idCard").html("").hide();
                $("#mac").html("").hide();
                $("#hint_facevalid").html("").hide();

                $("#hint_messagevalid").html(r.message).show();
            } else if (r.code == 911) {
                $("#hint_messagevalid").html("").hide();
                $("#hint_idCard").html("").hide();
                $("#mac").html("").hide();
                $("#hint_facevalid").html("").hide();

                $("#hint_idCard").html(r.message).show();
            } else if (r.code == 912) {
                $("#hint_messagevalid").html("").hide();
                $("#hint_idCard").html("").hide();
                $("#mac").html("").hide();
                $("#hint_facevalid").html("").hide();

                $("#hint_mac").html(r.message).show();
            } else if (r.code == 905) {
                $("#hint_messagevalid").html("").hide();
                $("#hint_idCard").html("").hide();
                $("#mac").html("").hide();
                $("#hint_facevalid").html("").hide();

                $("#hint_facevalid").html(r.message).show();
            }
        }
    });


}

function validateRule() {
    // 身份证号码验证
    jQuery.validator.addMethod("id_card", function (value, element) {
        var mobile = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$/;
        return this.optional(element) || (mobile.test(value));
    }, "身份证格式错误");
    // 手机号码验证
    jQuery.validator.addMethod("phone", function (value, element) {
        var mobile = /^1(3|4|5|7|8)\d{9}$/;
        return this.optional(element) || (mobile.test(value));
    }, "手机号码格式错误");
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            id_card: {
                required: true,
                id_card: "^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X)$"
            },
            message_valid: {
                required: true
            },
            phone: {
                required: true,
                // phone :"^1(3|4|5|7|8)\d{9}$"
            }
        },
        messages: {
            id_card: {
                required: icon + "请输入您的身份证",
                id_card: "身份证格式错误"
            },
            message_valid: {
                required: icon + "请输入您的短信验证码",
            },
            phone: {
                required: icon + "请输入您的手机号码",
                // phone :"手机号码格式错误"
            }
        }
    })
}
*/