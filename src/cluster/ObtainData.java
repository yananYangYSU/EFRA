package cluster;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class ObtainData {
	public static  ArrayList<float[]> dataSet = new ArrayList<float[]>();
	
	public static void addData(String datapath){
		try{
			BufferedReader br = new BufferedReader(new FileReader(datapath));
		    String line = br.readLine();		    
		     while(line!=null&&!line.startsWith("#")){
		        String [] numbers = line.split(",");
		        float [] tmp = new float[numbers.length] ;
		        for(int i=0;i<numbers.length;i++){
		        	tmp[i] = Float.parseFloat(numbers[i]);
		        }
                dataSet.add(tmp);
		       line = br.readLine(); 
		    }    
		  br.close();	     
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
}
