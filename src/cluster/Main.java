package cluster;

import java.util.ArrayList;
import java.util.Set;
/**
 * 聚类函数的主类
 * 目的是防止历史数据量过大导致程序计算缓慢 
 * 采用聚类的方法 聚成几类 然后根据每个类型的中心点做预测 加快计算
 * @author yanan
 *
 */
public class Main {
	public static void main(String[] args) {
		String datapath ="G://sourceScale/qps-cpu.csv"; 
        ArrayList<float[]> dataSet = new ArrayList<float[]>();
        ObtainData.addData(datapath);
        dataSet = ObtainData.dataSet;

        KMeansRun kRun =new KMeansRun(3, dataSet);//聚成3类

        Set<Cluster> clusterSet = kRun.run();
        System.out.println("单次迭代运行次数："+kRun.getIterTimes());
        for (Cluster cluster : clusterSet) {
            System.out.println(cluster);
        }           
    }
}
