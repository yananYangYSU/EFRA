package collaFilter;
import java.util.ArrayList;
import java.util.List;
   
public class collaborativefiltering {
	private List<double[]> original_data =null;    
	private double[] objective_data;
	private static ArrayList<double[]> pointList = null;  
	private int len;
	PearsonCorrelationScore PerCor = new PearsonCorrelationScore();
	public collaborativefiltering(List<double[]> original_data, double[] objective_data){
		this.objective_data=objective_data;
		this.original_data=original_data;
		this.len=objective_data.length;
		init();  	 
	}
	private void init() {
		pointList = new ArrayList<double[]>();
		for (int i = 0, j = original_data.size(); i < j; i++){
			pointList.add(original_data.get(i));
		}
 
	}


	public double[] ComputPerCorScore(){
		double [] corr = new double[pointList.size()]; 
		for(int i=0;i<pointList.size();i++){
			corr[i]= PerCor.getPearsonCorrelationScore(pointList.get(i), this.objective_data);
		}
	return corr;
}

public double[] AllcateScore(double[] corr,ArrayList<double[]> PointList){
	double corr_sum=0.0;
	double[] point_sum = new double [this.len];
	double[] score=new double [this.len];
	for(int i=0;i<corr.length;i++){
		corr_sum+=corr[i];
	}

	for(int i=0;i<this.len;i++){
		for(int j=0;j<PointList.size();j++){
			double[] tmp =PointList.get(j);   			 
			point_sum[i]= point_sum[i]+tmp[i]*corr[j];
		}
	}
	for(int i=0;i<this.len;i++){
		score[i]=point_sum[i]/corr_sum;
	}
	return score;

}
}
