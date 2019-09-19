$(function() {
	$.extend($.fn.validatebox.defaults.rules, {
		minLength: {
	        validator: function(value, param){
	            return value.length >= param[0];
	        },
	        message: '请输入至少{0}个字符.'
	    },
	    maxLength: {
	    	validator: function(value, param){
	            return value.length <= param[0];
	        },
	        message: '请输入最多{0}个字符.'
	    },
	    length:{
	    	validator: function(value, param){
	            return value.length >= param[0]&&value.length <= param[1];
	        },
	        message: '请输入{0}到{1}个字符.'
	    },
		mobile :{
			validator : function(value){
				return /^[0-9]{0,3}(13[0-9]|145|147|15[0-9]|18[0-9]|17[0-9])[0-9]{8}$/i.test(value);
			},
			message : '请输入有效的手机号码.'
		},
		phone :{
			validator : function(value){
				return /^(([0\+]\d{2,3}-)?(0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/i.test(value);
			},
			message : '请输入有效的固定电话号码.'
		},
		phoneNum :{
			validator : function(value){
				return /^(\d{2,3}-)?(\d{3,4}-\d{7,8})(-\d{1,6})?$|^1[34578][0-9]{9}$/.test(value);
			},
			message : '请输入有效的电话号码.'
		},
		code :{
			validator : function(value){
				return /^([A-Za-z0-9_-]|\.)+$/i.test(value);
			},
			message : '请输入有效的编码(只能包含字母、数字，不能包含特殊的字符).'
		},
		enname :{
			validator : function(value){
				return /^[A-Za-z0-9.·•]+$/i.test(value);
			},
			message : '请输入有效的英文名字(不能包含汉字).'
		},
		intRange:{
			validator : function(value,param){
				var reg = '/^\\d{' + param[0] + ',' + param[1] + '}$/'
				eval('var reg = ' + reg);
				return reg.test(value);
			},
			message : '请输入{0}到{1}位的整数.'
		},
		float:{
			validator : function(value){
				return /^\d+$|^\d+.?\d+$/i.test(value);
			},
			message : '请输入有效的数字.'
		},
		gsbm:{
			validator:function(value, param){
				return /^[1-9]\d{5,7}$/.test(value);
			},
			message:'请填写正确的网点编码！'
		},
		mailno:{
			validator:function(value, param){
				return /^[1-9]\d{5,20}$/.test(value);
			},
			message:'请填写正确的运单号码！'
		},
		ydmailno:{
			validator:function(value, param){
				return /^[1-9]\d{12}$/.test(value);
			},
			message:'请填写正确的运单号码！'
		},
		special:{		
			validator:function(value, param){
				return /^[A-Za-z0-9_<>\[\]\(\)\*\.\{\}@\-\/\|:\?;'\s\+=,.:;#%!\u4e00-\u9fa5]+$/.test(value);
			},
			message:'您输入的内容中有特殊字符！'
		},
		interfaceUrl:{
			validator:function(value, param){
				return /([\w-]+\.)+[\w-]+.([^a-z])(\/[\w-: .\/?%&=]*)?|[a-zA-Z\-\.][\w-]+.([^a-z])(\/[\w-: .\/?%&=]*)?/.test(value);
			},
			message:'您输入的接口地址格式错误！'
		}
	});
	
	//动态恢复和删除验证
	$.extend($.fn.validatebox.methods, {  
		remove: function(jq, newposition){  
			return jq.each(function(){  
				$(this).removeClass("validatebox-text validatebox-invalid").unbind('focus').unbind('blur');
			});  
		},
		reduce: function(jq, newposition){  
			return jq.each(function(){  
			   var opt = $(this).data().validatebox.options;
			   $(this).addClass("validatebox-text validatebox-invalid").validatebox(opt);
			});  
		}   
	});
});