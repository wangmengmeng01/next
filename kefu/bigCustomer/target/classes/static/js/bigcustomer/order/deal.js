var prefix = "/vipkf/bigcustomer/order"
var fileArr=[];
$().ready(function() {
    validateRule();
    //上传附件
    /*layui.use("upload", function () {
        var upload = layui.upload;
        //执行实例
        var uploadInst = upload.render({
            elem: '#upload', //绑定元素
            url: prefix+'/upload', //上传接口
            size: 5000,
            accept: 'file',
            auto: true,    //关闭自动提交
            //bindAction: '#queRen',  //绑定表单按钮
            exts: 'xls|xlsx|zip|docx|pdf|rar|jpeg|jpg|png|bmp',
            done: function (r) {
                layer.closeAll('loading');
                $('#upload').attr("disabled",false);
                $('#upload').removeAttr("readonly");//去除input元素的readonly属性
                //将文件名回显到页面以及将文件地址存放到input框中
                $('#uploadSpan').text(r.fileName);
                $('#fileName').val(r.fileName);
                $('#uploadPath').val(r.filePath);
                layer.msg("文件上传成功");
                //reLoad();
            },
            error: function (r) {
                layer.msg(r.msg);
            },
            before: function(obj){
                /!*this.data={
                  "orderIds": $('#orderIds').val(),
                  "jieDanCause": $('#jieDanCause').val()
                };*!/
                layer.load(1);
                $('#upload').attr("disabled",true);
                $('#upload').attr("readonly","readonly");//将input元素设置为readonly
            }
        });
    });*/
    layui.use('upload', function(){
        var $ = layui.jquery
            ,upload = layui.upload;
        var flag=0;
        //多文件列表示例
        var demoListView = $('#demoList')
            ,uploadListIns = upload.render({
            elem: '#testList'
            ,url: prefix+'/upload'
            ,size: 5000
            ,accept: 'file'
            ,multiple: true
            ,auto: false
            ,bindAction: '#testListAction'
            ,exts: 'xls|xlsx|zip|docx|pdf|rar|jpeg|jpg|png|bmp|doc'
            ,choose: function(obj){
                var files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
                var arr = Object.keys(files);
                //读取本地文件
                obj.preview(function(index, file, result){
                    if(arr.length>5){
                        delete files[index]; //删除多的文件
                        flag++;
                        if(flag==1){
                            parent.layer.msg("限制上传5个文件!");
                            return;
                        }
                        return;
                    }else if(document.getElementsByTagName('tbody')[0].getElementsByTagName('tr').length==5){
                        parent.layer.msg("限制上传5个文件!");
                        return;
                    }
                    var tr = $(['<tr id="upload-'+ index +'">'
                        ,'<td>'+ file.name +'</td>'
                        ,'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
                        ,'<td>等待上传</td>'
                        ,'<td>'
                        ,'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
                        ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
                        ,'</td>'
                        ,'</tr>'].join(''));

                    //单个重传
                    tr.find('.demo-reload').on('click', function(){
                        obj.upload(index, file);
                    });

                    //删除
                    tr.find('.demo-delete').on('click', function(){
                        delete files[index]; //删除对应的文件
                        tr.remove();
                        uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                    });

                    demoListView.append(tr);
                });

            }
            ,done: function(res, index, upload){
                if(res.code == 200){ //上传成功
                    fileArr.push(res.data)
                    var tr = demoListView.find('tr#upload-'+ index)
                        ,tds = tr.children();
                    tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
                    tds.eq(3).html(''); //清空操作
                    return delete this.files[index]; //删除文件队列已经上传成功的文件
                }
                this.error(index, upload);
            }
            ,error: function(index, upload){
                var tr = demoListView.find('tr#upload-'+ index)
                    ,tds = tr.children();
                tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
                tds.eq(3).find('.demo-reload').removeClass('layui-hide'); //显示重传
            }
        });

    });

});

$.validator.setDefaults({
    submitHandler : function() {
        save();
    }
});

function save() {
    var jieDanData = {};
    jieDanData['orderIds'] = $("#orderIds").val();
    jieDanData['consultType'] = $("#consultType").val();
    //jieDanData['jieDanResult'] = $("#jieDanResult").val();
    jieDanData['jieDanCause'] = $("#jieDanCause").val();
    jieDanData['zeRenFang'] = $("#zeRenFang").val();
    for ( var i = 0; i <fileArr.length; i++){
        jieDanData['fileDOS['+i+'].fileType']=fileArr[i].fileType;
        jieDanData['fileDOS['+i+'].fileName']=fileArr[i].fileName;
        jieDanData['fileDOS['+i+'].fileSuffix']=fileArr[i].fileSuffix;
        jieDanData['fileDOS['+i+'].status']=fileArr[i].status;
        jieDanData['fileDOS['+i+'].uploadPath']=fileArr[i].uploadPath;
        jieDanData['fileDOS['+i+'].createTime']=fileArr[i].createTime;
        jieDanData['fileDOS['+i+'].operateName']=fileArr[i].operateName;
    }
    $.ajax({
        cache : true,
        type : "POST",
        url : prefix +"/dealSave",
        data : jieDanData,
        async : false,
        error : function(request) {
            parent.layer.alert("操作失败");
        },
        success : function(data) {
            if (data.code == 0) {
                parent.layer.msg("操作成功");
                parent.reLoad();
                //改变父页面的数据
                parent.$('#state').val("处理中");
                parent.$('#state').text("处理中");
                parent.$('#detail_state').val("处理中");
                parent.$('#detail_state').text("处理中");
                parent.$('#btnDiv').show();
                // parent.$('#btnDiv').hide();
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
                required : icon + "请输入姓名"
            }
        }
    });

}

function quitAudit(){
    // parent.reLoad(); 不需要重新加载
    var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
    parent.layer.close(index);
}

/*参数校验*/
function checkLengthT(that,count) {
    if ($(that).val().length >= count) {
        $(that).parent().append("<span class='textarea-check'>限定输入"+count+"个字符</span>")
    } else {
        $(that).parent().find('.textarea-check').remove()
    }
}

