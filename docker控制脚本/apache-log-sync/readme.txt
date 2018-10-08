本文件夹里的所有脚本均放置在apache容器内部
1. apache_log_collect.sh && cron.sh
每5分钟将apache日志（每分钟保存在一个文件里）复制到一个文件里，并将原日志文件清空。
1. latency_apache.sh && cron_apache.sh
每5分钟将apache日志（每分钟保存在一个文件里）复制到一个文件里，不将原日志文件清空。主要对应自己的方法