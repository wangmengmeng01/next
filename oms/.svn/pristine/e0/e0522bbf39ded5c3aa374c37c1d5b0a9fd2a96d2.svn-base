$(function(){
	var menuHeight = $('#menu2').parent('div').height()-30;
	$('#menu2').height(menuHeight);
	
	var iframeId = "";
	$(".item").each(function(){
		$(this).click(function(event){
			$('.item').removeClass("selected");
			$(this).addClass("selected");
			iframeId = $(this).attr("iframeId");
			addTab('tt',''+ $(this).text() +'',''+ $(this).attr("href"), ''+ iframeId +'');
			event.stopPropagation();
			return false;
		});
	});

    //关闭所有按钮事件
    var $tt = $("#tt");
   $tt.tabs({
	  tools:[{
		  iconCls: 'icon-closeAll',
		  handler:function(){
			  closeTabs($tt);
		  }
	  }]
   });
})

function showMenu(obj,id)
{
	$('#menu1 li').removeClass('menuSelected');
	$(obj).addClass('menuSelected');
	$('#menu2 .menuItem').hide();
	$('#'+id).show();
}

/**
 * 增加TAB
 */
function addTab(divid, title, href, id) {
	var tt = $('#'+ divid);
	var initalPath = href;
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

//对弹出窗口进行配置.
function window_dialog(param, href) {
	$('#window-dialog-Iframe').attr('src', href);
	$('#window-dialog').dialog(param);
}

function closeWindow(){
	$('#window-dialog').dialog('close');
}

//返回要进行操作的对象
function window_iframe_handle(id)
{
	var obj = document.getElementById(id);
	if(obj==null){
		return null;
	}else{
		return obj.contentWindow;
	}
}