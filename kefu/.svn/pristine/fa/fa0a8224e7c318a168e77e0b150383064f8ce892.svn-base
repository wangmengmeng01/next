$(document).ready(function () {
        $("#login").on('click',function(){$("#signupForm").submit();});
        
        $('#code').bind('keypress',function(event){if(event.keyCode == 13){ $("#signupForm").submit();}});

        
        validateRule();
    });

    $.validator.setDefaults({
        submitHandler: function () {
        	//Check();
            login();
        }
    });

    function login() {
    	//var password = $("#password").val();
        var password=encode64($("#password").val());
    	var username = $("#username").val();
    	var code = $("#code").val();
    	var passwordOrgin=$("#password").val();
    	var passwordSecret=b64_md5($("#password").val());//统一授权按此加密
    	//password = encryptByDES(password,"12345678");
    	//username = encryptByDES(username,"12345678");
        $.ajax({
            type: "POST",
            url: ctx+"login",
            data: {
            	"code": code,  
                "username":  username,
                "password" :password ,
                "passwordOrgin" :passwordOrgin ,
                "passwordSecret" :passwordSecret 
            	/*username : username,
            	code : code,
            	password : password*/
            },
            success: function (r) {
                if (r.code == 0) {
                    var index = layer.load(1, {
                        shade: [0.1,'#fff'] //0.1透明度的白色背景
                    });
                    parent.location.href = '/vipkf/index';
                } else {
                    layer.msg(r.msg);
                    
                    // 更换验证码  
                    var timestamp = new Date().getTime();  
                    $("#codeImg").attr("src", '/vipkf/captcha-image?' +timestamp );
                    if(r.code==803){
                    	$("#code").val("");
                        $("#code").focus();
                    } 
                }
            }
        });
    }

    function Check(){
    	var code = $("#code").val();
       	var username=$("#username").val();
    	var password=encode64($("#password").val());
    	var passwordOrgin=$("#password").val();
    	var passwordSecret=b64_md5($("#password").val());//统一授权按此加密
    	  $.ajax({
//      	      jsonpCallback:"success",
              type: "POST",
              url: ctx+"login",
              //url: ctx+"validate",
//               url: "http://10.19.105.152:7001/ydauth/actions/outer/user/login.action",
//               dataType: 'json', 
              data: 
              {
               "code": code,  
               "username":  username,
               "password" :password ,
               "passwordOrgin" :passwordOrgin ,
               "passwordSecret" :passwordSecret 
               
              },
              success: function (r) {
                  if (r.code == 0) {
                      var index = layer.load(1, {
                          shade: [0.1,'#fff'] //0.1透明度的白色背景
                      });
                      parent.location.href = '/vipkf/index';
                  } else {
                      layer.msg(r.msg);
                  }
              }/* ,
//               jsonp:"callback",  
              success: function (oData) {
            	  if(oData){
            		 login();
            	  }
              } */
          }); 
    }
    
    function validateRule() {
        var icon = "<i class='fa fa-times-circle'></i> ";
        $("#signupForm").validate({
            rules: {
                username: {
                    required: true
                },
                password: {
                    required: true
                }
            },
            messages: {
                username: {
                    required: icon + "请输入您的用户名工号",
                },
                password: {
                    required: icon + "请输入您的密码",
                }
            }
        })
    }
    
    // base64加密开始  
    var keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef" + "ghijklmnopqrstuv"  
            + "wxyz0123456789+/" + "=";  
    
    function encode64(input) {  
        var output = "";  
        var chr1, chr2, chr3 = "";  
        var enc1, enc2, enc3, enc4 = "";  
        var i = 0;  
        do {  
            chr1 = input.charCodeAt(i++);  
            chr2 = input.charCodeAt(i++);  
            chr3 = input.charCodeAt(i++);  
            enc1 = chr1 >> 2;  
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);  
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);  
            enc4 = chr3 & 63;  
            if (isNaN(chr2)) {  
                enc3 = enc4 = 64;  
            } else if (isNaN(chr3)) {  
                enc4 = 64;  
            }  
            output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2)  
                    + keyStr.charAt(enc3) + keyStr.charAt(enc4);  
            chr1 = chr2 = chr3 = "";  
            enc1 = enc2 = enc3 = enc4 = "";  
        } while (i < input.length);  
  
        return output;  
    } 
    
    
    function encryptByDES(message, key) { 
        var keyHex = CryptoJS.enc.Utf8.parse(key);  
        var encrypted = CryptoJS.DES.encrypt(message, keyHex, {    
        mode: CryptoJS.mode.ECB,    
        padding: CryptoJS.pad.Pkcs7    
        });   
        return encrypted.toString();    
    }

$('#forgot').on('click', function() {
    layer.msg('请联系管理员.');
});