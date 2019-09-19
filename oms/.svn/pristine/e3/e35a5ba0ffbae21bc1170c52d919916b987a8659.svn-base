<style>
body, div,p ,label ,input, img{margin:0;padding:0;}
body{font-family:Tahoma, Geneva, sans-serif ,"宋体"; font-size:14px;background-color:#FFFFFF}
a{ text-decoration:none; color:#666;}
a:hover{color:#f63;}
.login_c form label input ,.sys_btn{background-image: url(./static/image/ds_background.gif);}

/*------会员登录样式-------*/
#login{ width:100%;height:100%;background:url(./static/image/bg_repeat_x.jpg) repeat-x; }
.login_c{ height:800px; width:1004px; margin:0 auto; background: url(./static/image/bg.jpg) no-repeat; position:relative; }
.login_c form{ width:360px; position:absolute; right:15px; top:270px;}
.login_c form label{ display:block; height:51px;}
.login_c form label span{ display:inline-block; width:60px; font: normal 14px/51px "宋体"; text-align:right;}
.login_c form label input{ display:inline-block;font: normal 14px/24px "宋体"; border:none; width:192px; height:24px; padding:6px 5px; text-indent:5px; line-height:24px; background-position:0 0; *margin-top:10px; *border:1px solid #feedb5;}
.login_c form label input:hover{background-position:0 -38px;}
.sys_btn{display:block; float:left; width:96px; height:38px; background-position:0 -76px; cursor:pointer; text-indent:-9999px;}
.login_c form label a.login_btn{ background-position:0 -76px;}
.login_c form label a.rset_btn{ background-position:-106px -76px;}
.login_c form label a:hover.login_btn{ background-position:0 -116px;}
.login_c form label a:hover.rset_btn{ background-position:-106px -116px;}
.login_btn{ margin-left:60px; margin-top:10px;}
.rset_btn{ margin-left:10px; margin-top:10px;}
#imgVcode{ position:absolute; margin-top: 10px;}
#login_msg { width:360px;height:50px;position:absolute;top:220px;text-align:center;color:red}


</style>
<div id="login">
    <div class="login_c">
         <form id="login_form" method="post">
              <label><span>用户名：</span><input type="text" id="userName" name="userName" class="easyui-validatebox" missingMessage="用户名必须填写" data-options="required:true,validType:'username'" />
              <label><span>密码：</span><input type="password" id="passWord" name="passWord" class="easyui-validatebox" missingMessage="密码必须填写" data-options="required:true,validType:'password'" /></label>
              <label><span>验证码：</span><input type="text" id="Vcode" name="Vcode" class="easyui-validatebox" missingMessage="验证码必须填写" maxlength="6" data-options="required:true" />&nbsp;<img id="imgVcode" title="点击刷新验证码" src="./index.php?r=site/vcode" onclick="getVcode(this)" ></label>
              <label><a class="sys_btn login_btn" onclick="login()" iconCls="icon-ok">登录</a><a class="rset_btn sys_btn" onclick="reset()" iconCls="icon-no">重置</a></label>             
         </form>
         <div id="login_msg"></div>
    </div>    
</div>
<script type="text/javascript" src="./static/js/controllers/site-login.js"></script>