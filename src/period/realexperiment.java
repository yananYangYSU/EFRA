package period;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.JarException;

import dataTransform.FileOperation;

public class realexperiment {
	 
  public static void main(String argv[]) throws java.io.IOException {
    //String filename = "data/" + argv[0] + ".data";
	/*String filename = "E:\\TJU\\Write\\Optimizing Resource Provisioning\\test.txt";
    BufferedReader file = new BufferedReader(new FileReader(filename));
    String data = file.readLine();
    file.close();
*/
	/*String data="dcccbbbccdffggghggffeeeeddcbbbbcdeffghhhhhgfeeeeddcbbbbcdefgghihhggfffeedccccbbcdefgggghhggfeeeeedccbbbcdfggghhihgffeeeddccbbaabbcccddddeedddcddddcbaaaabbbcccdddddcdddd";
	data=data.substring(0,120); 
	TimeSeries t = new TimeSeries(data);*/
   /* Hashtable h = t.mineRelaxedPeriods(0.95);
    
    TreeMap<Integer,Double> map=new TreeMap<>();
    
    Set<Integer> keys=h.keySet();
    for(Integer item:keys){
    	map.put(item,(double)h.get(item));
    }
    keys=map.keySet();
    for(Integer item:keys){
    	  System.out.println(map.get(item));
    }
  */
	FileOperation fo=new FileOperation();
	List<Integer> list=fo.readIntFile("G://sourceScale/clark/rps.txt");
	System.out.println(new OffSetPeriod().getPeriodByOffset(list));
  }
}
