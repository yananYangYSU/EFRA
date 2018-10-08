#EFRA系统功能代码
--------------------------------------------
java代码结构
src---+----cluster 聚类代码
      +----collaFilter 协同过滤预测代码
	  +----dataTransform 原始log数据转换代码
	  +----period 周期检测代码
	  注：所有包里的Main类为主类 方法已添加注释 运行时注意文件路径
data--+----normalized.txt 归一化后的请求数量/h 文件
      +----object.txt 最终的结果文件
	  +----redis_qps_cpuRate.txt redis模拟真实负载的cpu利用率
	  +----redis_qps_latency.txt redis模拟真实负载的访问延迟时间
	  +----rps.txt 请求数/h的文件
	  +----symbol.txt 将请求数转化成字母表示的文件 用于周期预测
	  +----topkcpumin.txt cpu的历史使用数据 用于协同过滤
	  +----topklist.txt 历史请求数 用于协同过滤
	 
python代码结构
python+----lstm.py 用于lstm算法的python代码
      +----data.csv lstm的输入文件
	  
	  log数据集下载地址:http://ita.ee.lbl.gov/html/traces.html