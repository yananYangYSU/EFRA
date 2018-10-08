#!bin/sh




DATE=$(date +%Y-%m-%d" "%H:%M:"00");
DATE1=$(date +%Y_%m_%d_%H_%M);
DATE=`date -d "$DATE" +%s`;
#sleep 300
for file in /var/log/apache2/*.log
do
    if test -f $file
    then
        filename=${file##*/}
#        echo $filename;
        y=`echo $filename|cut -d \_ -f 2`;
        m=`echo $filename|cut -d \_ -f 3`;
        d=`echo $filename|cut -d \_ -f 4`;
        h=`echo $filename|cut -d \_ -f 5`;
        mi=`echo $filename|cut -d \_ -f 6`;
	filename_date=$y"-"$m"-"$d" "$h":"$mi":""00";
	filedate=`date -d "$filename_date" +%s`;
#	echo $DATE;
#	echo $filedate;
#	echo $filename_date;
	delta=`expr $DATE - $filedate`;
#	echo $delta;
        if [ "$delta" -gt "0" -a "$delta" -le "300" ];
	then
#		echo $file;
        	cp $file /var/log/apache2/access/
#		echo "">$file;
        fi
    fi
done
count=0;
for file in /var/log/apache2/access/*.log
do
	if [ $count -gt 0 ];
	then 
		sort -m -t " " -k 4 -o /var/log/apache2/access/all/"apache1_"$DATE1"_access".log  /var/log/apache2/access/all/"apache1_"$DATE1"_access".log $file;
		rm -rf $file;
		count=`expr $count + 1`;
	else
		sort -m -t " " -k 4 -o /var/log/apache2/access/all/"apache1_"$DATE1"_access".log $file;
		count=`expr $count + 1`;
		rm -rf $file;
	fi
done

