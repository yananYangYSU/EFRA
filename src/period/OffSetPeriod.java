package period;

import java.util.List; 
import java.util.Set;
import java.util.TreeMap;
 
public class OffSetPeriod {
	public <T> int getPeriodByOffset(List<T> series){ 
		int period=0;
		int size=series.size();
		float distance=0;
		float firstItem=0;
		float secondItem=0;
		TreeMap<Float,Integer> periodsMap=new TreeMap<>();
		int maxOffset=size>>1;
		for(int offSet=1;offSet<maxOffset;offSet++){
			distance=0;
			size=series.size()-offSet;
			for(int i=0;i<size;i++){
				firstItem=(int)series.get(i);
				secondItem=(int)series.get(i+offSet);
				distance+=(firstItem-secondItem)*(firstItem-secondItem);
			}
			periodsMap.put(distance,offSet);
		}
		Set<Float> keys=periodsMap.keySet();
		for(Float key:keys){
			//System.out.println(key+" "+periodsMap.get(key));
			if(periodsMap.get(key)!=1){
				period=periodsMap.get(key);
				break;
			}
		}
		return period;
	}
	public int getPeriodByCharOffset(List<Character> series){ 
		int period=0;
		int size=series.size();
		float distance=0;
		float firstItem=0;
		float secondItem=0;
		TreeMap<Float,Integer> periodsMap=new TreeMap<>();
		int maxOffset=size>>1;
		for(int offSet=1;offSet<maxOffset;offSet++){
			distance=0;
			size=series.size()-offSet;
			for(int i=0;i<size;i++){
				firstItem=(int)series.get(i);
				secondItem=(int)series.get(i+offSet);
				distance+=(firstItem-secondItem)*(firstItem-secondItem);
			}
			periodsMap.put(distance,offSet);
		}
		Set<Float> keys=periodsMap.keySet();
		for(Float key:keys){
			//System.out.println(key+" "+periodsMap.get(key));
			if(periodsMap.get(key)!=1){
				period=periodsMap.get(key);
				break;
			}
		}
		return period;
	}
}	
