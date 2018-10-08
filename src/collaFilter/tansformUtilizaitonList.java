package collaFilter;
import java.util.ArrayList;
import java.util.List;
public class tansformUtilizaitonList {
	private List<double[]> original_data =null;    
    public ArrayList<double[]> pointList = null;  
    PearsonCorrelationScore PerCor = new PearsonCorrelationScore();
    public tansformUtilizaitonList(List<double[]> original_data){
   	 this.original_data=original_data;
   	 init();  	 
    }
    private void init() {
	        pointList = new ArrayList<double[]>();
	        for (int i = 0, j = original_data.size(); i < j; i++){
	            pointList.add(original_data.get(i));
	        }  
	        
	    }
}
