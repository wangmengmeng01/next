## OMS 1.1.0 ##
===============

## 应用环境介绍

- [测试环境]: http://10.19.156.212:81/oms/index.php?r=site/login
- [服务器地址]: 10.19.156.212  22022 root urlndPECTNWxjjAF
- [数据库地址]: 10.19.156.186  3306  ggs  IhMacKjCEPLYduzXcGpwyy


- [UAT环境]: http://uat.omscp.yundasys.com:16111/oms/index.php?r=site/login
- [服务器地址]: 10.19.106.111  22022 root  5L3BKEV48CW2
- [数据库地址]: 10.19.106.109  3306  oms   mHm45aYfu7zIPLN5


- [线上环境]: http://oms.yundasys.com:2124/oms/index.php?r=site/login
10.0.2.124，
10.0.2.160，
10.0.2.161，
192.168.105.56

## 配置文件

- 应用/数据库配置文件 /yd/oms/config.php

## 数据库操作

global $db;
DbAction...

## 应用脚本

- */1 * * * * sh /yd/oms/protected/crontab.sh php &    
	a.用于统计前一天的仓库发货量   checkprocess "$selfpath/crontab.php ShipmentsQty Count"  
	b.用于统计删除半个月以前的日志 checkprocess "$selfpath/crontab.php DelLog Delete"
	c.用于定时获取京东商家授权信息 checkprocess "$selfpath/crontab.php JdWaybillInfo Get"

## 目录简要说明

/yd/oms/api.php          富勒接口,奇门接口（直连接口）入口文件
/yd/oms/cainiao_api.php  菜鸟电子面单接口入口文件
/yd/oms/storage_api.php  菜鸟仓储接口入口文件
/yd/oms/custom_api.php   保税仓接口入口文件
/yd/oms/jd_api.php       京东电子面单入口文件


## 需求分析师

- 牛飞燕

