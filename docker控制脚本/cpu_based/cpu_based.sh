Date=$(date +%s);
date1=`expr $Date % 60`
if [ $date1 -eq "0" ]:
	then
	python cpu_based.py $Date
fi

