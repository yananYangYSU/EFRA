#!/bin/bash

i=1

while [ $i -le 180 ];
do
	Date=$(date +%s)
#	DATE1=$(date +%Y_%m_%d_%H_%M);
#	echo $Date
	modi=`expr $Date % 60`
	if [ $modi -eq "0" ]:
        	then
		Date1=`expr $Date - 60`
		DATE1=`date -d @$Date1 "+%Y_%m_%d_%H_%M"`
        	python latency_based.py $Date $i $DATE1
		i=`expr $i + 1`
	fi
	sleep 1
done


