var prefix = "/vipkf/bigcustomer/order"
var fileArr=[];
$().ready(function() {
    validateRule();
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
    var data = $('#signupForm').serialize();
    $.ajax({
        cache : true,
        type : "POST",
        url : prefix +"/lanJieFeign",
        data : data,
        async : false,
        error : function(request) {
            parent.layer.alert("拦截失败");
        },
        success : function(data) {
            if (data.code == 0) {
                parent.layer.msg("拦截成功");
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


/*
function lanJieTypeChange() {
    var obj = document.getElementById("lanJieType");
    var index = obj.selectedIndex;
    if(index=="0"){
        $("#lanJieHiden").attr("hidden",true);
    }else {
        $("#lanJieHiden").removeAttr("hidden");
    }
    
}
*/
