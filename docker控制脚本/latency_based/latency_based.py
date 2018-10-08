#!/usr/bin/python3

import os
import sys
import time

date = int(sys.argv[1])
i = int(sys.argv[2])
date1 = sys.argv[3]



#filepath = open('/home/tank/youki/log_collect/shell/cpu_based/cpu_core.csv')
with open('/home/tank/youki/log_collect/shell/latency_based/cpu_alloc.csv') as f:
	core_pre = f.readlines()
#print(core_pre)

core_pre = list(map(eval,core_pre))


#print(core_pre)
if i == 1:
	core = core_pre[0]/10
	core = ("%.3f" % core)
	print('docker update rubis_apache1 --cpus '+str(core))
	os.system('docker update rubis_apache1 --cpus '+str(core))
	print('docker update rubis_apache2 --cpus '+str(core))
	os.system('docker update rubis_apache11 --cpus '+str(core))
	os.system('docker update rubis_apache3 --cpus '+str(core))
	os.system('docker update rubis_apache4 --cpus '+str(core))
	os.system('docker update rubis_apache5 --cpus '+str(core))
	os.system('docker update rubis_apache6 --cpus '+str(core))
	os.system('docker update rubis_apache7 --cpus '+str(core))
	os.system('docker update rubis_apache8 --cpus '+str(core))
	os.system('docker update rubis_apache9 --cpus '+str(core))
	os.system('docker update rubis_apache10 --cpus '+str(core))
	sys.exit()

#os.system('docker cp rubis_apache1:/var/log/apache2/ /home/tank/youki/log_collect/latency_based/logs/')
filename1 = '%s%s%s' % ('apache1_',date1,'_access.log')
filename2 = '%s%s%s' % ('apache11_',date1,'_access.log')
filename3 = '%s%s%s' % ('apache3_',date1,'_access.log')
filename4 = '%s%s%s' % ('apache4_',date1,'_access.log')
filename5 = '%s%s%s' % ('apache5_',date1,'_access.log')
filename6 = '%s%s%s' % ('apache6_',date1,'_access.log')
filename7 = '%s%s%s' % ('apache7_',date1,'_access.log')
filename8 = '%s%s%s' % ('apache8_',date1,'_access.log')
filename9 = '%s%s%s' % ('apache9_',date1,'_access.log')
filename10 = '%s%s%s' % ('apache10_',date1,'_access.log')
os.system('docker cp rubis_apache1:/var/log/apache2/'+filename1+' /home/tank/youki/log_collect/shell/latency_based/logs/')
os.system('docker cp rubis_apache11:/var/log/apache2/'+filename2+' /home/tank/youki/log_collect/shell/latency_based/logs/')
os.system('docker cp rubis_apache3:/var/log/apache2/'+filename3+' /home/tank/youki/log_collect/shell/latency_based/logs/')
os.system('docker cp rubis_apache4:/var/log/apache2/'+filename4+' /home/tank/youki/log_collect/shell/latency_based/logs/')
os.system('docker cp rubis_apache5:/var/log/apache2/'+filename5+' /home/tank/youki/log_collect/shell/latency_based/logs/')
os.system('docker cp rubis_apache6:/var/log/apache2/'+filename6+' /home/tank/youki/log_collect/shell/latency_based/logs/')
os.system('docker cp rubis_apache7:/var/log/apache2/'+filename7+' /home/tank/youki/log_collect/shell/latency_based/logs/')
os.system('docker cp rubis_apache8:/var/log/apache2/'+filename8+' /home/tank/youki/log_collect/shell/latency_based/logs/')
os.system('docker cp rubis_apache9:/var/log/apache2/'+filename9+' /home/tank/youki/log_collect/shell/latency_based/logs/')
os.system('docker cp rubis_apache10:/var/log/apache2/'+filename10+' /home/tank/youki/log_collect/shell/latency_based/logs/')



filedir = '/home/tank/youki/log_collect/shell/latency_based/logs/'
filenames = os.listdir(filedir)
count = 0
count1 = 0
timesec = []
for filename in filenames:
	filepath = filedir + '/' + filename
	for line in open(filepath):
		line1 = line.split()
		l =len(line1)
		if l == 11:
			flag = line.split()[5]
			if flag == '"GET':
				count = count + 1
				latency = int(line.split()[10])
				if latency >= 7875:
					count1 = count1 + 1
count2 = int(count * 0.008)
if count1 >= count2:
	core_pre[i-1] = core_pre[i-1] * 1.4
#	core_pre[i-1] = ("%.2f" % core_pre[i-1])
	core_pre[i] = core_pre[i] * 1.4
#	core_pre[i] = ("%.1f" % core_pre[i])
	core = core_pre[i-1] / 10
	core = ("%.3f" % core)
	os.system('docker update rubis_apache1 --cpus '+str(core))
	os.system('docker update rubis_apache11 --cpus '+str(core))
	os.system('docker update rubis_apache3 --cpus '+str(core))
	os.system('docker update rubis_apache4 --cpus '+str(core))
	os.system('docker update rubis_apache5 --cpus '+str(core))
	os.system('docker update rubis_apache6 --cpus '+str(core))
	os.system('docker update rubis_apache7 --cpus '+str(core))
	os.system('docker update rubis_apache8 --cpus '+str(core))
	os.system('docker update rubis_apache9 --cpus '+str(core))
	os.system('docker update rubis_apache10 --cpus '+str(core)) 
	f = open('/home/tank/youki/log_collect/shell/latency_based/cpu_alloc.csv','w')
	core_pre[i-1] = ("%.2f" % core_pre[i-1])
	core_pre[i] = ("%.2f" % core_pre[i])
	for x in core_pre:
		f.write(str(x)+'\n')
	f.close()
else:
	core = core_pre[i-1] / 10
	core = ("%.3f" % core)
	os.system('docker update rubis_apache1 --cpus '+str(core))
	os.system('docker update rubis_apache11 --cpus '+str(core))
	os.system('docker update rubis_apache3 --cpus '+str(core))
	os.system('docker update rubis_apache4 --cpus '+str(core))
	os.system('docker update rubis_apache5 --cpus '+str(core))
	os.system('docker update rubis_apache6 --cpus '+str(core))
	os.system('docker update rubis_apache7 --cpus '+str(core))
	os.system('docker update rubis_apache8 --cpus '+str(core))
	os.system('docker update rubis_apache9 --cpus '+str(core))
	os.system('docker update rubis_apache10 --cpus '+str(core))
print(date)
print(time.time())
os.system('rm -rf /home/tank/youki/log_collect/shell/latency_based/logs/*')	


