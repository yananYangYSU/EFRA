#!/bin/bash
file=/home/tank/sdcloud/redis/SET.qps
# -f 参数判断 $file 是否存在
if [ -f "$file" ]; then
	rm $file
fi
file=/home/tank/sdcloud/redis/GET.qps
# -f 参数判断 $file 是否存在
if [ -f "$file" ]; then
	rm $file
fi
file=/home/tank/sdcloud/redis/SET.lat
# -f 参数判断 $file 是否存在
if [ -f "$file" ]; then
	rm $file
fi
file=/home/tank/sdcloud/redis/GET.lat
# -f 参数判断 $file 是否存在
if [ -f "$file" ]; then
	rm $file
fi
/home/tank/sdcloud/tools/SDC_Bench-master/x86/redis-cluster-bench/redis-cluster-bench -h 172.17.192.37 -p 9001 -n 999999999 -c $2 -t set --interval 1

