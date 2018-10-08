import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dataTransform.EventTran;
import dataTransform.FileOperation;
import period.OffSetPeriod;
 

public class Main {

	public static void main(String[] args) throws IOException {
		FileOperation readDataFile=new FileOperation();
		EventTran et=new EventTran();
		// TODO Auto-generated method stub
		List<Integer> rqsList=readDataFile.readIntFile("G://sourceScale/clark/rps.txt");//读取request per day的list
		
		List<Double> symbolList=new ArrayList<Double>();
		double min=-2.0;
		double max=2.5;
		double interval=0.5;
		for(double i=min;i<max+0.1;i=i+interval){
			symbolList.add(i); 
		}
		List<Double> normalList=et.normalList(rqsList);
		List<Character> list=et.mapSympol(normalList,symbolList); 
		//下面是两种计算周期的方法 上面的是论文里的方法，下面是一种简单的方法，结果是一样的
		System.out.println(new OffSetPeriod().getPeriodByCharOffset(list));//通过将请求数转化成正太分布的字母 来计算周期
		System.out.println(new OffSetPeriod().getPeriodByOffset(rqsList));//直接通过偏移法计算周期
		 
	}

}
