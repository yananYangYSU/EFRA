package dataTransform;

import java.util.List;

public class MathTool {
	
	public static double calAvg(List<Integer> list){
		int size=list.size();
		if(size==0)
			return 0;
		double sum=0;
		for(int i=0;i<size;i++){
			sum+=list.get(i);
		}
		return sum/size;
	}
	public static double calStd(List<Integer> list){
		int size=list.size();
		if(size==0)
			return 0;
		double avgValue=calAvg(list);
		double sum=0;
		for(int i=0;i<size;i++){
			sum+=(list.get(i)-avgValue)*(list.get(i)-avgValue);
		}
		sum=sum/(size-1);
		return Math.sqrt(sum);
	}
	public static double calStd(List<Integer> list,double avgValue){
		int size=list.size();
		if(size==0)
			return 0;
		double sum=0;
		for(int i=0;i<size;i++){
			sum+=(list.get(i)-avgValue)*(list.get(i)-avgValue);
		}
		sum=sum/(size-1);
		return Math.sqrt(sum);
	}
}
