/**************************************************************************/

/**
 * 获取项目根路径
 * */
function getRootPath() {
	if(window.top.contextPath == undefined){
	    var pathName = window.document.location.pathname;
	    var projectName = pathName.substring(0,pathName.substr(1).indexOf('/') + 1);
	    return projectName;
	}
	return window.top.contextPath;
}

/**
 * ajax请求处理sesion过期问题
 */
$(function(){
	$(document).ajaxComplete(function(event, XHR, settings){
		var status = XHR.getResponseHeader("sessionStatus");
		if (status == "400") {
			window.parent.location.href = getRootPath() + '/index.php';
		}
	});
});


/***
 * 格式化显示的数据
 * @returns
 */
function formartNullFun() {
	var value = arguments[0];
	if (typeof(value) == 'undefined') {
		return '';
	}
	if (value == null || value == '' || value == 'null') {
		return '';
	}
	return value;
}

/***
 * 判断对象是否为空
 * 空 返回 true
 */ 
function isNull() {
	if (arguments.length == 0) {
		return true;
	}
	var value = arguments[0];
	if (typeof(value) == 'undefined' || value == '' || value == null || value.length == 0) {
		return true;
	}
}

/**
 * 将提交的表单数据转换成JSON字符串
 * @returns
 */ 
function jsonToStr() {
	var data = arguments[0];
	if (typeof(data) == 'string') {
		return data;
	}
	var jsonData = '';
	for (var i = 0; i < data.length; i ++) {
		var value = data[i].value;
		//必须先替换换行符
		value = value.replace(/\r/ig,",").replace(/\n/ig,","); //如果是多行文本框控件，则把换行替换成逗号
		//通过json 格式化，否则传入到后台异常
		value = $.toJSON(value); //对特殊字符进行处理
		jsonData += ('"' + data[i].name + '":' + value + '');
		jsonData += (i == (data.length - 1) ? '' : ',');
	}
	jsonData = '{' + jsonData + '}';
	return jsonData;
}

/**************************** 数据验证格式化---end ***************************/
/************************************************************************/

/************************************************************************/
/**************************** easyui方法---start ***************************/
/***
 * 添加一个选项卡parameter（title: 选项卡标题, href： 选项卡地址） 
 */

function addTab(divid, title, href, id) {
	var tt = $('#'+ divid);
	var initalPath = "";
//	initalPath = getRootPath() + '/' + href;
	initalPath = href;
	var content = '<iframe id="'+ id +'" scrolling="yes" frameborder="0"'+
	'src="' + initalPath + '" style="width:100%; height:99%;"></iframe>';
	if (!tt.tabs('exists', title)) {
		tt.tabs('add',{
			title:title,
			content:content,
			border:false,
			fit:true,
			closable:true
		});
	}else {
		tt.tabs('select',title);
		refreshTab({divId:tt,tabTitle:title,url:initalPath});
	}
}

/**
 * 如果当前选项卡的title已经存在、则刷新当前的选项卡
 */
function refreshTab(cfg) {
	var refresh_tab = cfg.tabTitle?cfg.divId.tabs('getTab',cfg.tabTitle) : cfg.divId.tabs('getSelected');  
	if(refresh_tab && refresh_tab.find('iframe').length > 0) {
		var _refresh_ifram = refresh_tab.find('iframe')[0];  
		var refresh_url = cfg.url ? cfg.url : _refresh_ifram.src;  
		//_refresh_ifram.src = refresh_url;  
		_refresh_ifram.contentWindow.location.href = refresh_url;
	}  
}

/**************************** easyui方法---end ***************************/
/************************************************************************/

/**************************************************************************/
/******************************* 日期方法---start ****************************/
/**
 * 格式化日期
 * @param format 日期格式,如 yyyy-MM-dd
 * @returns	{String}
 */
Date.prototype.format = function(format) // author: meizz
{ 
  var o = { 
    "M+" : this.getMonth()+1, // month
    "d+" : this.getDate(),    // day
    "h+" : this.getHours(),   // hour
    "m+" : this.getMinutes(), // minute
    "s+" : this.getSeconds(), // second
    "q+" : Math.floor((this.getMonth()+3)/3),  // quarter
    "S" : this.getMilliseconds() // millisecond
  }; 
  if(/(y+)/.test(format)) format=format.replace(RegExp.$1, 
    (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
  for(var k in o)if(new RegExp("("+ k +")").test(format)) 
    format = format.replace(RegExp.$1, 
      RegExp.$1.length==1 ? o[k] : 
        ("00"+ o[k]).substr((""+ o[k]).length)); 
  return format; 
};

/**
 * 根据指定格式解析日期
 * @param date		日期字符串
 * @param format	格式字符串
 * @returns {Date}
 */
function parseDate(date, format){// author: xukj
	var result=new Date();
	if(/(y+)/.test(format))
		result.setFullYear(date.substring(format.indexOf(RegExp.$1),format.indexOf(RegExp.$1)+RegExp.$1.length));
	if(/(M+)/.test(format))
		result.setMonth(parseInt(date.substring(format.indexOf(RegExp.$1),format.indexOf(RegExp.$1)+RegExp.$1.length),10)-1);
	if(/(d+)/.test(format))
		result.setDate(date.substring(format.indexOf(RegExp.$1),format.indexOf(RegExp.$1)+RegExp.$1.length));
	if(/(h+)/.test(format))
		result.setHours(date.substring(format.indexOf(RegExp.$1),format.indexOf(RegExp.$1)+RegExp.$1.length));
	if(/(m+)/.test(format))
		result.setMinutes(date.substring(format.indexOf(RegExp.$1),format.indexOf(RegExp.$1)+RegExp.$1.length));
	if(/(s+)/.test(format))
		result.setSeconds(date.substring(format.indexOf(RegExp.$1),format.indexOf(RegExp.$1)+RegExp.$1.length));
	if(/(S+)/.test(format))
		result.setMilliseconds(date.substring(format.indexOf(RegExp.$1),format.indexOf(RegExp.$1)+RegExp.$1.length));
	return result;
}

/***
 * 获取当前时间之前多少天的日期(不包含时分秒)
 */
function getBeforeDate(day){
	var zdate = new Date();
	if (parseInt(day) == 0){
		return new Date(zdate.getTime()).format("yyyy-MM-dd");
	}
    var edate = new Date(zdate.getTime() - (day*24*60*60*1000) + (1*24*60*60*1000)).format("yyyy-MM-dd");
    return edate;
}

/***
 * 获取当前时间之前多少天的日期 开始时间
 */
function getBeforeDateStart(day){
	var zdate = new Date();
	if (parseInt(day) == 0){
		return new Date(zdate.getTime()).format("yyyy-MM-dd") + " 00:00:00";
	}
    var edate = new Date(zdate.getTime() - (day*24*60*60*1000) + (1*24*60*60*1000)).format("yyyy-MM-dd") + " 00:00:00";
    return edate;
}

/***
 * 获取当前时间之前多少天的日期 结束时间
 */
function getBeforeDateEnd(day){
	var zdate = new Date();
	if (parseInt(day) == 0){
		return new Date(zdate.getTime()).format("yyyy-MM-dd") + " 23:59:59";
	}
    var edate = new Date(zdate.getTime() - (day*24*60*60*1000) + (1*24*60*60*1000)).format("yyyy-MM-dd") + " 23:59:59";
    return edate;
}

/***
 * 获取当前时间之后多少天的日期 开始时间
 */
function getAfterDateStart(day){
	var zdate = new Date();
	if (parseInt(day) == 0){
		return new Date(zdate.getTime()).format("yyyy-MM-dd") + " 00:00:00";
	}
    var edate = new Date(zdate.getTime() + (day*24*60*60*1000) - (1*24*60*60*1000)).format("yyyy-MM-dd") + " 00:00:00";
    return edate;
}

/***
 * 获取当前时间之后多少天的日期 结束时间
 */
function getAfterDateEnd(day){
	var zdate = new Date();
	if (parseInt(day) == 0){
		return new Date(zdate.getTime()).format("yyyy-MM-dd") + " 23:59:59";
	}
    var edate = new Date(zdate.getTime() + (day*24*60*60*1000) - (1*24*60*60*1000)).format("yyyy-MM-dd") + " 23:59:59";
    return edate;
}


/******************************* 日期方法---end ****************************/
/**************************************************************************/


/***********************************************************************************/
/************************************cookie操作方法start*****************************/
function getCookiePath() {
	var href=document.location.href;
	return href.substring(href.indexOf(resourceCharts)+
			resourceCharts.length).replaceAll("/","").replaceAll("#","");
}

//设置cookie
function setCookie(cookieName,cvalue,expiredays,path) {
	var expireDate=new Date();
	var expireStr="";
	if(expiredays!=null) {
		expireDate.setTime(expireDate.getTime()+(expiredays*24*3600*1000));
		expireStr="; expires="+expireDate.toGMTString();
	}
	pathStr=(path==null)?"; path=/":"; path="+path;
	document.cookie=cookieName+'='+escape(cvalue)+expireStr+pathStr;
	
}

//取得cookie	
function getCookie(cookieName) {
	var index=-1;
 	if(document.cookie) 
 		index=document.cookie.indexOf(cookieName);
 	if(index==-1) {
 		return "";
 	} else {
 	     var iBegin = (document.cookie.indexOf("=", index) +1);
 	     if(iBegin==0)
 	    	 return "";
          var iEnd =document.cookie.indexOf(";", index);
          if (iEnd == -1)
          {
              iEnd = document.cookie.length;
          }
          return unescape(document.cookie.substring(iBegin,iEnd));
	}
} 

//为了删除指定名称的cookie，可以将其过期时间设定为一个过去的时间
function delCookie(name) {
   var date = new Date();
   date.setTime(date.getTime() - 10000);
   document.cookie = name + "=a; expires=" + date.toGMTString();
}

/**********************************cookie操作方法end*****************************/
/*****************************************************************************/

/*****************************************************************************/
/************************************String方法start*****************************/
/**
 * 字符串替换,过滤了一些特殊字符
 * @param s1	原始字符串
 * @param s2	需要替换的字符串
 * @returns		{String}
 */
String.prototype.replaceAll = function(s1, s2) {
	var r = new RegExp(s1.replace(/([\(\)\[\]\{\}\^\$\+\-\*\?\.\"\'\|\/\\])/g,
			"\\$1"), "ig");
	return this.replace(r, s2);
};

/**
 * 去掉字符串两端的空格
 * @returns
 */
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};

/**
 * 去掉字符串左端的空格
 * @returns
 */
String.prototype.ltrim = function() {
	return this.replace(/(^\s*)/g, "");
};

/**
 * 去掉字符串右端的空格
 * @returns
 */
String.prototype.rtrim = function() {
	return this.replace(/(\s*$)/g, "");
};

/************************************String方法end******************************/
/*****************************************************************************/

/**
*扩展Datagrid的重新调整宽度的方法
*/
$.fn.extend({
	resizeDataGrid : function(widthMargin, minWidth) {
		var width = $(document.body).width() - widthMargin;
		width = width < minWidth ? minWidth : width;
		$(this).datagrid('resize', {
			width : width
		});
	}
});

/**
 * 
 * 功能：关闭所有已经打开的tabs(可关闭的)
 */

function closeTabs(obj){
	var $tabs=obj;
	
	var closeTabsTitle = getAllTabObj($tabs);
	
	$.each(closeTabsTitle,function(){
		var title = this;
		$tabs.tabs('close',title);
	});
}

/**
 * 
 * @param tabs
 * @returns 返回所有可关闭tabs的title
 */
function getAllTabObj(tabs){
	//存放所有tab标题
	var closeTabsTitle = [];
	//所有tab对象
	var allTabs = tabs.tabs('tabs');
	$.each(allTabs,function(){
		var tab = this;
		var opt = tab.panel('options');
		//获取标题
		var title = opt.title;
		//是否可关闭 ture:会显示一个关闭按钮，点击该按钮将关闭选项卡
		var closable = opt.closable;
		if(closable){
			closeTabsTitle.push(title);
		}
	});
	return closeTabsTitle;
}