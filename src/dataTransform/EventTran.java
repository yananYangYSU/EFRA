package dataTransform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventTran {
	private DateFormats dateFormat=DateFormats.getInstance();
	
	public void transData() throws IOException{
		FileOperation readDataFile=new FileOperation();
		List<Long> dataArray=readDataFile.readLogFile("G://sourceScale/Aug28_log");//读取网站原始log文件
		List<Integer> res=this.getEveData(dataArray,"1995-08-28 00:00:34","1995-09-03 23:59:59",3600);//按照一个小时3600s的间隔计算数量
		readDataFile.writeResFile(res,"G://sourceScale/res.txt","col");
		/*List<Double> res=readDataFile.readDoubleFile("G://sourceScale/normalized.txt");
		 List<Double> symbolList=new ArrayList<Double>();
		 symbolList.add(-2.0);
		 symbolList.add(-1.5);
		 symbolList.add(-1.0);
		 symbolList.add(-0.5);
		 symbolList.add(0.0);
		 symbolList.add(0.5);
		 symbolList.add(1.0);
		 symbolList.add(1.5);
		 symbolList.add(2.0);
		 symbolList.add(2.5); 
		List<Character> list=mapSympol(res, symbolList); 
		readDataFile.writeResFile(list,"G://sourceScale/symbol.txt","col");*/
		
	}
	/**
	 * 归一化
	 * @param list
	 * @return
	 */
	public List<Double> normalList(List<Integer> list){
		List<Double> resList=new ArrayList<Double>();
		double avgValue=MathTool.calAvg(list);
		double stdValue=MathTool.calStd(list,avgValue);
		int size=list.size();
		for(int i=0;i<size;i++){
			resList.add((list.get(i)-avgValue)/stdValue);
		}
		return resList;
	}
	/**
	 * @author yanan
	 * 根据查询类型进行数据处理，按天查询则按天生成平均值
	 * 假如查询了一天24小时的数据，每个小时5个数据点，原始数据数组一共120个元素
	 * 则此方法返回的数组只有24个元素，每个元素是对应小时的5个数据点的平均值
	 * @param dataArray 原始数据数组
	 * @param startTime
	 * @param endTime
	 * @param interval 时间间隔 单位s
	 * @return 平均值数据数组
	 */
	private List<Integer> getEveData(List<Long> dataArray,String startTime,String endTime,int interval){
		if(dataArray.size()==0) return null;

		List<Integer> eventNumberList=new ArrayList<Integer>();//用于封装平均值的数组	
		/**
		 * dataArray要在最后添加一条假记录，不然程序不会计算最后一天的数据
		 */
		dataArray.add(dateFormat.dateStringToTime("2100-01-01 00:00:00"));//在数组最后添加一条假记录 这个日期要非常大，这样才能满足>nextTime这个条件

		long start=0;//第i天的时间开始 /ms
		long end=dateFormat.dateStringToTime(endTime);

		long nextTime=0;//第i+1天的开始时间 /ms 
		int count=0;

		if(startTime.trim().length()>=13)
			startTime=startTime.substring(0,13);
		startTime=startTime+":00:00";//将日期yyyy-mm-dd HH补充完整
		start=dateFormat.dateStringToTime(startTime);
		interval=1000*interval;//一小时的毫秒间隔
		nextTime=start+interval;

		for(int i=0;i<dataArray.size();i++){//遍历查询出来的所有数据
			if(dataArray.get(i)<nextTime){
				count++;				
			}else{
				if(count>0){
					eventNumberList.add(count);
					count=0;
				}
				i--;//从上一个继续开始
				if(nextTime>=end)
					break;
				nextTime=nextTime+interval;//日期+1个interval
			}
		}
		return eventNumberList;
	}
	/**
	 * 把请求数的数组转换成字母序列
	 * 用于论文里的方法求周期
	 * 比如 小于1w的记做a,1w-2w之间的记做b ...
	 * @param valueList 请求数的 数组
	 * @param symbolList 转换后的数组
	 * @return
	 */
	public List<Character> mapSympol(List<Double> valueList,List<Double> symbolList){
		List<Character> resList=new ArrayList<Character>();
		for(int i=0;i<valueList.size();i++){
			for(int j=0;j<symbolList.size();j++){
				if(valueList.get(i)>symbolList.get(j)&&valueList.get(i)<=symbolList.get(j+1)){
					resList.add((char)(j+97));
					break;
				}
			} 
		}
		return resList;
		  
	}
}
