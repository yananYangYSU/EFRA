package collaFilter;
import java.util.ArrayList;


public class Main {
	public static void main(String[] args) {
		  String datapath ="G://sourceScale/clark/topklist.txt";//请求数据的文件
		  String datapath1 ="G://sourceScale/clark/topkcpumin.txt";//cpu负载的文件
		  double objective_data[]={9174,7961,6815,5236,5218,4884,4824,6664,8894,10540,13034,14460,15823,16416,18085,17606,17229,15169,15560,13648,12482,12397,11146,10176};//要做对应预测的请求数序列
          ArrayList<double[]> dataSet = new ArrayList<double[]>();
          ArrayList<double[]> dataSet1 = new ArrayList<double[]>();
          obtainData.addData(datapath);
          dataSet = obtainData.dataSet; 
          obtainData1.addData(datapath1);
          dataSet1 = obtainData1.dataSet;
          tansformUtilizaitonList TransUtilization = new tansformUtilizaitonList(dataSet1);
          collaborativefiltering CollFilter = new collaborativefiltering(dataSet,objective_data);
          double[] corr=CollFilter.ComputPerCorScore(); 
          double[] score=CollFilter.AllcateScore(corr,TransUtilization.pointList);
          for(int i=0;i<score.length;i++){ 
      		System.out.println(score[i]);//输出即为CPU cores
      	}
    }
}
