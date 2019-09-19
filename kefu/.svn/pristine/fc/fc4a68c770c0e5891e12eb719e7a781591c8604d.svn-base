//处理当返回490状态码的时候，弹窗验证页面
function popSafepage(data) {
    layer.open({
        type: 2,
        maxmin : true,
        closeBtn: 1,
        title: "安全验证",
        shadeClose: false, // 点击遮罩关闭层
        area: ['410px', '98%'],
        content: '/vipkf/system/validate?userlevel=' + data,// iframe的url
        success: function(layero, index) {
            layer.iframeAuto(index);
        }
    });
}
//心跳检测
function heartCheck() {
    window.setInterval(function () {
        $.ajax({
            type: "POST",
            dataType: "json",
            url: '/vipkf/system/heartCheck',
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                //layer.msg("心跳检测出现异常");
            },
            success: function(res){

            }
        });
    }, 10000);
}

function Chrome() {
    var userAgent = navigator.userAgent; // 取得浏览器的userAgent字符串
    var flag =false;
    $.ajax({
    type: "GET",
    dataType: "json",
    url: '/vipkf/system/leader',
    async :false,
    error: function (XMLHttpRequest, textStatus, errorThrown) {
        },
    //检查是否是谷歌浏览器
    success: function(res){
            if(res.code=='200'){
                if (!(userAgent.indexOf("Chrome") > -1)) {
                    flag =true;
                }
            }
        }
       });
    return flag;
}

//自动退出
$(function () {
    var maxTime = 60; // seconds
    var time = maxTime;
    $('body').on('keydown mousemove mousedown', function (e) {
        time = maxTime; // reset
    });
    var intervalId = setInterval(function () {
        time--;
        if (time <= 0) {
            ShowInvalidLoginMessage();
            clearInterval(intervalId);
        }
    }, 1000)
});

function ShowInvalidLoginMessage() {
    $('.page-tabs-content').children("[data-id]").not(":first").each(function () {
        $('.J_iframe[data-id="' + $(this).data('id') + '"]').remove();
        $(this).remove();
    });
    $('.page-tabs-content').children("[data-id]:first").each(function () {
        $('.J_iframe[data-id="' + $(this).data('id') + '"]').show();
        $(this).addClass("active");
    });
    $('.page-tabs-content').css("margin-left", "0");
}

