package scs.util.loadGen.loadDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader; 

import scs.pojo.heracles.QueryData;
import scs.util.repository.Repository; 
/**
 * redis服务请求发生驱动类
 * @author yanan
 *
 */
public class RedisDriver extends AbstractJobDriver{
	/**
	 * 单例代码块
	 */
	private static RedisDriver driver=null;
	public RedisDriver(){initVariables();}
	public synchronized static RedisDriver getInstance() {
		if (driver == null) {  
			driver = new RedisDriver();
		}  
		return driver;
	}

	@Override
	protected void initVariables() {
		// TODO Auto-generated method stub
	}
	/**
	 * 按毫秒级时间间隔开环发送请求
	 * 通过调用脚本 把脚本的命令行输出获取到并进行解析存储
	 * @param strategy 请求模式 possion 当前方法没有用处
	 * @return 
	 */
	@Override
	public void executeJob(String strategy) {
		if(Repository.onlineDataFlag==true){
			//System.out.println("sh "+Repository.scriptDir+"redis/StartContainer.sh "+Repository.onlineRequestIntensity*3000*Repository.windowSize+" "+Repository.onlineRequestIntensity);
			this.startQuery("sh "+Repository.scriptDir+"redis/StartContainer.sh "+Repository.onlineRequestIntensity*5000*Repository.windowSize+" "+Repository.onlineRequestIntensity);
		}else{
			//System.out.println("sh "+Repository.scriptDir+"redis/StopContainer.sh");
			this.stopQuery("sh "+Repository.scriptDir+"redis/StopContainer.sh");
		}
	}
	/**
	 * 开启查询
	 * @param scriptPath 脚本路径
	 */
	private void startQuery(String scriptPath){
		try {  
			Repository instance=Repository.getInstance();
			Process process = Runtime.getRuntime().exec(scriptPath); 
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is); 
			BufferedReader br = new BufferedReader(isr);
			String line=null;

			Repository.onlineQueryThreadRunning=true;
			while((line = br.readLine()) != null ) {  
				line=line.replace(".00"," ");
				String[] split=line.trim().split("\\s+");
				QueryData data=new QueryData();
				data.setGenerateTime(System.currentTimeMillis());
				data.setQps((int)Float.parseFloat((split[0])));
				data.setQueryTime((int)Float.parseFloat(split[3]));//取99分位数 命令行输出依次为 [QPS MEAN 95th 99th 99.9th 100th]
				instance.addWindowOnlineDataList(data);
			} 
			br.close(); 
			isr.close();
			is.close(); 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			Repository.onlineQueryThreadRunning=false;
		}
	}
	/**
	 * 关闭查询
	 * @param scriptPath
	 */
	private void stopQuery(String scriptPath){
		try { 
			String line = null,err;
			Process process = Runtime.getRuntime().exec(scriptPath); 
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			InputStreamReader isr = new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader(isr);   
			while (((err = br.readLine()) != null||(line = input.readLine()) != null)) {
				if(err==null){
					System.out.println(line); 
				}else{
					System.out.println(err);
				}
			} 
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}


}