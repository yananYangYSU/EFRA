package collaFilter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class obtainData1 {
public static  ArrayList<double[]> dataSet = new ArrayList<double[]>();
	
	public static void addData(String datapath){
		try{
			BufferedReader br = new BufferedReader(new FileReader(datapath));
		    String line = br.readLine();		    
		     while(line!=null&&!line.startsWith("#")){
		        String [] numbers = line.split(",");
		        double [] tmp = new double[numbers.length] ;
		        for(int i=0;i<numbers.length;i++){
		        	tmp[i] = Double.parseDouble(numbers[i]);
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
