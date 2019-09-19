#!/bin/bash
phpExec=$1
selfpath=$(cd "$(dirname "$0")"; pwd)
function checkprocess(){
    if (ps aux|grep -v grep|grep "$1" )
    then
        echo "active"
    else
        #echo "miss"
        #echo $1
        $phpExec $1 $2 $3 &
    fi
}

#threadNum=`$phpExec "$selfpath/thread_num.php"`
#-------------------------------异步接口数据转发，$threadNum：配置队列进程数
#i=1
#for i in $threadNum
#do
#    checkprocess "$selfpath/crontab.php Order Push $i"
#    checkprocess "$selfpath/crontab.php Order Retry $i"
#    checkprocess "$selfpath/crontab.php Order ExcelPush 1" 
#    i=i+1
#done

#checkprocess "$selfpath/crontab.php ShipmentsQty Count"
#checkprocess "$selfpath/crontab.php DelLog Delete"
checkprocess "$selfpath/crontab.php UatDeleteLog Delete"

