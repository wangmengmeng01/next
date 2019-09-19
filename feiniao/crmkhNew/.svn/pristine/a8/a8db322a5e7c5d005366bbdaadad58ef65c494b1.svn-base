
@ECHO OFF

ECHO 欢迎使用   开始系统设置...

ECHO 该设置不会对您的电脑使用带来影响

ECHO ---设置IE信任区域中...

REM 注释:首先介绍计算机定义的每个安全区域的项：Zones 。默认情况下，定义5个区域（编号从0到4：0：我的电脑；1：本地Intranet区域；2.受信任的站点区域;3.Internet区域;4.受限制的站点区域）

REM 注释:其中HKEY_CURRENT_USER指的是当前登录用户，此设置不会影响到其它登录的账号。

REM 注释:Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3为关键字的路径，根据之前关于Zones选项的解释，此路径不难看懂（末位3代表什么请看注释第一行括号内）。

REM 注释:通常，设置为0x00000000则将具体操作设置为允许；设置为0x00000001则导致出现提示；设置为0x00000003则禁止执行具体操作。

REM 注释:2400为需要设置的选项代号。

 

REM 3：Internet区域

REM .NET Framework

REM 	2400  	xaml	浏览器应用程序

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "2400"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2401	xps	文档

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "2401"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2402	松散	saml

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "2402"              /t REG_DWORD /d 0x00000000 /f > nul

 

REM .NET Framework	相关组件

REM 	2007：带有清单的权限的组件

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "2007"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2004：运行未用	Authenticode	签名的.NET组件

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "2004"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2001：运行已用	Authenticode	签名的.NET组件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "2001"              /t REG_DWORD /d 0x00000000 /f > nul

REM  ActiveX 控件和插件

REM 	2201：ActiveX	控件自动提示

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "2201"              /t REG_DWORD /d 0x00000000 /f > nul

REM		1405：对标记为可安全执行脚本的	ActiveX	控件执行脚本*

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "1405"              /t REG_DWORD /d 0x00000000 /f > nul

REM		1201:对没有标记为安全的	ActiveX	控件进行初始化和脚本运行 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "1201"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2000:二进制和脚本行为

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "2000"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	120B：仅允许批准的域在未经提示的情况下使用	ActiveX

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "120B"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1004：下载未签名的   ActiveX   控件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "1004"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1001：下载已签名的   ActiveX   控件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "1001"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2702：允许	ActiveX	筛选

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "2702"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1209：允许	Scriptlet

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "1209"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1208：允许运行以前未使用的 ActiveX 控件而不提示

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "1208"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1200：运行   ActiveX   控件和插件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "1200"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	在 ActiveX 控件上运行反恶意软件

REM 	120A：在没有使用外部播放器的网页上显示视频和动画

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "120A"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1806	加载应用程序和不安全文件

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "1806"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1607	跨域浏览子框架 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "1607"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1406	通过域访问数据资源 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\3" /v "1406"              /t REG_DWORD /d 0x00000000 /f > nul

 

 

 

 

 

REM 1：本地Intranet

REM .NET Framework

REM 	2400  	xaml	浏览器应用程序

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "2400"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2401	xps	文档

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "2401"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2402	松散	saml

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "2402"              /t REG_DWORD /d 0x00000000 /f > nul

 

REM .NET Framework	相关组件

REM 	2007：带有清单的权限的组件

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "2007"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2004：运行未用	Authenticode	签名的.NET组件

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "2004"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2001：运行已用	Authenticode	签名的.NET组件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "2001"              /t REG_DWORD /d 0x00000000 /f > nul

REM  ActiveX 控件和插件

REM 	2201：ActiveX	控件自动提示

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "2201"              /t REG_DWORD /d 0x00000000 /f > nul

REM		1405：对标记为可安全执行脚本的	ActiveX	控件执行脚本*

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "1405"              /t REG_DWORD /d 0x00000000 /f > nul

REM		1201:对没有标记为安全的	ActiveX	控件进行初始化和脚本运行 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "1201"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2000:二进制和脚本行为

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "2000"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	120B：仅允许批准的域在未经提示的情况下使用	ActiveX

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "120B"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1004：下载未签名的   ActiveX   控件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "1004"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1001：下载已签名的   ActiveX   控件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "1001"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2702：允许	ActiveX	筛选

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "2702"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1209：允许	Scriptlet

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "1209"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1208：允许运行以前未使用的 ActiveX 控件而不提示

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "1208"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1200：运行   ActiveX   控件和插件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "1200"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	在 ActiveX 控件上运行反恶意软件

REM 	120A：在没有使用外部播放器的网页上显示视频和动画

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "120A"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1806	加载应用程序和不安全文件

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "1806"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1607	跨域浏览子框架 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "1607"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1406	通过域访问数据资源 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\1" /v "1406"              /t REG_DWORD /d 0x00000000 /f > nul

 

 

 

 

 

REM 2.受信任的站点区域

REM .NET Framework

REM 	2400  	xaml	浏览器应用程序

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "2400"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2401	xps	文档

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "2401"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2402	松散	saml

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "2402"              /t REG_DWORD /d 0x00000000 /f > nul

 

REM .NET Framework	相关组件

REM 	2007：带有清单的权限的组件

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "2007"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2004：运行未用	Authenticode	签名的.NET组件

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "2004"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2001：运行已用	Authenticode	签名的.NET组件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "2001"              /t REG_DWORD /d 0x00000000 /f > nul

REM  ActiveX 控件和插件

REM 	2201：ActiveX	控件自动提示

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "2201"              /t REG_DWORD /d 0x00000000 /f > nul

REM		1405：对标记为可安全执行脚本的	ActiveX	控件执行脚本*

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1405"              /t REG_DWORD /d 0x00000000 /f > nul

REM		1201:对没有标记为安全的	ActiveX	控件进行初始化和脚本运行 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1201"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2000:二进制和脚本行为

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "2000"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	120B：仅允许批准的域在未经提示的情况下使用	ActiveX

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "120B"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1004：下载未签名的   ActiveX   控件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1004"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1001：下载已签名的   ActiveX   控件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1001"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2702：允许	ActiveX	筛选

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "2702"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1209：允许	Scriptlet

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1209"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1208：允许运行以前未使用的 ActiveX 控件而不提示

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1208"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1200：运行   ActiveX   控件和插件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1200"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	在 ActiveX 控件上运行反恶意软件

REM 	120A：在没有使用外部播放器的网页上显示视频和动画

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "120A"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1806	加载应用程序和不安全文件

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1806"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1607	跨域浏览子框架 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1607"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1406	通过域访问数据资源 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\2" /v "1406"              /t REG_DWORD /d 0x00000000 /f > nul

 

 

 

 

 

REM 4.受限制的站点区域

REM .NET Framework

REM 	2400  	xaml	浏览器应用程序

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "2400"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2401	xps	文档

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "2401"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2402	松散	saml

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "2402"              /t REG_DWORD /d 0x00000000 /f > nul

 

REM .NET Framework	相关组件

REM 	2007：带有清单的权限的组件

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "2007"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2004：运行未用	Authenticode	签名的.NET组件

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "2004"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2001：运行已用	Authenticode	签名的.NET组件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "2001"              /t REG_DWORD /d 0x00000000 /f > nul

REM  ActiveX 控件和插件

REM 	2201：ActiveX	控件自动提示

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "2201"              /t REG_DWORD /d 0x00000000 /f > nul

REM		1405：对标记为可安全执行脚本的	ActiveX	控件执行脚本*

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "1405"              /t REG_DWORD /d 0x00000000 /f > nul

REM		1201:对没有标记为安全的	ActiveX	控件进行初始化和脚本运行 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "1201"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2000:二进制和脚本行为

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "2000"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	120B：仅允许批准的域在未经提示的情况下使用	ActiveX

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "120B"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1004：下载未签名的   ActiveX   控件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "1004"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1001：下载已签名的   ActiveX   控件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "1001"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	2702：允许	ActiveX	筛选

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "2702"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1209：允许	Scriptlet

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "1209"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1208：允许运行以前未使用的 ActiveX 控件而不提示

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "1208"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1200：运行   ActiveX   控件和插件 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "1200"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	在 ActiveX 控件上运行反恶意软件

REM 	120A：在没有使用外部播放器的网页上显示视频和动画

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "120A"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1806	加载应用程序和不安全文件

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "1806"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1607	跨域浏览子框架 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "1607"              /t REG_DWORD /d 0x00000000 /f > nul

REM 	1406	通过域访问数据资源 

REG ADD "HKEY_CURRENT_USER\Software\Microsoft\Windows\CurrentVersion\Internet Settings\Zones\4" /v "1406"              /t REG_DWORD /d 0x00000000 /f > nul

 

 

ECHO IE信任区域设置完成

PAUSE

 
