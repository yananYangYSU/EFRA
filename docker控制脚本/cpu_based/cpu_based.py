#!/usr/bin/python3

import os
import sys

#filepath = open('/home/tank/youki/log_collect/shell/cpu_based/cpu_core.csv')
with open('/home/tank/youki/log_collect/shell/cpu_based/cpu_core.csv') as f:
	line = f.readlines()
lastline = line[-1]
cpucore = []
cpucore.append(float(lastline.split(',')[1]))
cpucore.append(float(lastline.split(',')[2]))
cpucore.append(float(lastline.split(',')[3]))
cpucore.append(float(lastline.split(',')[4]))
cpucore.append(float(lastline.split(',')[5]))
cpucore.append(float(lastline.split(',')[6]))
cpucore.append(float(lastline.split(',')[7]))
cpucore.append(float(lastline.split(',')[8]))
cpucore.append(float(lastline.split(',')[9]))
cpucore.append(float(lastline.split(',')[10]))


filepath = '/home/tank/youki/log_collect/shell/cpu_collect/03_08_cpu.csv'
f = open('/home/tank/youki/log_collect/shell/cpu_based/cpu_core.csv','a')
date1 = sys.argv[1]
cpurate1 = []
cpurate2 = []
cpurate3 = []
cpurate4 = []
cpurate5 = []
cpurate6 = []
cpurate7 = []
cpurate8 = []
cpurate9 = []
cpurate10 = []

for line in open(filepath):
	t = line.split(',')[0]
	tt = int(date1)-int(t)
	if tt > 0 and tt <= 60:
		cpu_rate1 = line.split(',')[1].split()[0]
		cpurate1.append(float(cpu_rate1.split('%')[0]))
		cpu_rate2 = line.split(',')[1].split()[1]
		cpurate2.append(float(cpu_rate2.split('%')[0])) 
		cpu_rate3 = line.split(',')[1].split()[2]
		cpurate3.append(float(cpu_rate3.split('%')[0])) 
		cpu_rate4 = line.split(',')[1].split()[3]
		cpurate4.append(float(cpu_rate4.split('%')[0])) 
		cpu_rate5 = line.split(',')[1].split()[4]
		cpurate5.append(float(cpu_rate5.split('%')[0])) 
		cpu_rate6 = line.split(',')[1].split()[5]
		cpurate6.append(float(cpu_rate6.split('%')[0])) 
		cpu_rate7 = line.split(',')[1].split()[6]
		cpurate7.append(float(cpu_rate7.split('%')[0])) 
		cpu_rate8 = line.split(',')[1].split()[7]
		cpurate8.append(float(cpu_rate8.split('%')[0])) 
		cpu_rate9 = line.split(',')[1].split()[8]
		cpurate9.append(float(cpu_rate9.split('%')[0])) 
		cpu_rate10 = line.split(',')[1].split()[9]
		cpurate10.append(float(cpu_rate10.split('%')[0]))
cpurate_core = []
cpu_core = []
#print(cpurate_cor
cpurate_core.append(sum(cpurate1)/cpucore[0]/60/100)
cpurate_core.append(sum(cpurate2)/cpucore[1]/60/100)
cpurate_core.append(sum(cpurate3)/cpucore[2]/60/100)
cpurate_core.append(sum(cpurate4)/cpucore[3]/60/100)
cpurate_core.append(sum(cpurate5)/cpucore[4]/60/100)
cpurate_core.append(sum(cpurate6)/cpucore[5]/60/100)
cpurate_core.append(sum(cpurate7)/cpucore[6]/60/100)
cpurate_core.append(sum(cpurate8)/cpucore[7]/60/100)
cpurate_core.append(sum(cpurate9)/cpucore[8]/60/100)
cpurate_core.append(sum(cpurate10)/cpucore[9]/60/100)
#cpurate_core.append(sum(cpurate10)/cpucore[10]/100)

cpu_core.append(sum(cpurate1)/60/100)
cpu_core.append(sum(cpurate2)/60/100)
cpu_core.append(sum(cpurate3)/60/100)
cpu_core.append(sum(cpurate4)/60/100)
cpu_core.append(sum(cpurate5)/60/100)
cpu_core.append(sum(cpurate6)/60/100)
cpu_core.append(sum(cpurate7)/60/100)
cpu_core.append(sum(cpurate8)/60/100)
cpu_core.append(sum(cpurate9)/60/100)
cpu_core.append(sum(cpurate10)/60/100)
#print(cpurate_core)
#print(cpu_core)

f.writelines(date1)
for i in range(len(cpurate_core)):
#	print(cpurate_core[i])
	if cpurate_core[i] > 0.65:
		cpucore[i] = cpucore[i] * 1.6
	elif cpurate_core[i] < 0.2:
		cpucore[i] = cpucore[i] / 2
	if cpucore[i] < 0.1:
		cpucore[i] = 0.1
	cpucore[i] = ("%.2f" % cpucore[i])
	f.writelines(','+str(cpucore[i]))
	j = i + 1
	if i == 1:
		j = 11
	os.system('docker update rubis_apache'+str(j)+' --cpus '+str(cpucore[i]))
f.writelines('\n')

