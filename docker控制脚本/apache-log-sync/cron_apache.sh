#!/bin/sh
while true
do

	DATE=`date +%s`;

	i=`expr $DATE % 300`;
#	echo $i;
	if [ "$i" -eq "0" ];
	then 
		sh /var/log/apache2/latency_apache.sh &
	fi
	sleep 1;
done
