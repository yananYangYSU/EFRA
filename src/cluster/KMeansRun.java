package cluster;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
public class KMeansRun {
	private int kNum;                             
	private int iterRunTimes = 0;              
	private float disDiff = (float) 0.0000001;         

	private List<float[]> original_data =null;    
	private static List<Point> pointList = null;  
	private DistanceCompute disC = new DistanceCompute();
	private int len = 0;                          

	public KMeansRun(int k, List<float[]> original_data) {
		this.kNum = k;
		this.original_data = original_data;
		this.len = original_data.get(0).length; 
		check();
		init();
	}


	private void check() {
		if (kNum == 0){
			throw new IllegalArgumentException("k must be the number > 0");  
		}
		if (original_data == null){
			throw new IllegalArgumentException("program can't get real data");
		}
	} 


	private void init() {
		pointList = new ArrayList<Point>();
		for (int i = 0, j = original_data.size(); i < j; i++){
			pointList.add(new Point(i, original_data.get(i)));
		}
	}


	private Set<Cluster> chooseCenterCluster() {
		Set<Cluster> clusterSet = new HashSet<Cluster>();
		Random random = new Random();
		for (int id = 0; id < kNum; ) {
			Point point = pointList.get(random.nextInt(pointList.size()));

			boolean flag =true;
			for (Cluster cluster : clusterSet) {
				if (cluster.getCenter().equals(point)) {
					flag = false;
				}
			}
			if (flag) {
				Cluster cluster =new Cluster(id, point);
				clusterSet.add(cluster);
				id++;
			}
		}
		return clusterSet;  
	}

	public void cluster(Set<Cluster> clusterSet){
		for (Point point : pointList) {
			float min_dis = Integer.MAX_VALUE;
			for (Cluster cluster : clusterSet) {
				float tmp_dis = (float) Math.min(disC.getEuclideanDis(point, cluster.getCenter()), min_dis);
				if (tmp_dis != min_dis) {
					min_dis = tmp_dis;
					point.setClusterId(cluster.getId());
					point.setDist(min_dis);
				}
			}
		}
		for (Cluster cluster : clusterSet) {
			cluster.getMembers().clear();
			for (Point point : pointList) {
				if (point.getClusterid()==cluster.getId()) {
					cluster.addPoint(point);
				}
			}
		}
	}

	public boolean calculateCenter(Set<Cluster> clusterSet) {
		boolean ifNeedIter = false; 
		for (Cluster cluster : clusterSet) {
			List<Point> point_list = cluster.getMembers();
			float[] sumAll =new float[len];
			for (int i = 0; i < len; i++) {
				for (int j = 0; j < point_list.size(); j++) {
					sumAll[i] += point_list.get(j).getlocalArray()[i];
				}
			}
			for (int i = 0; i < sumAll.length; i++) {
				sumAll[i] = (float) sumAll[i]/point_list.size();
			}
			if(disC.getEuclideanDis(cluster.getCenter(), new Point(sumAll)) > disDiff){
				ifNeedIter = true;
			}
			cluster.setCenter(new Point(sumAll));
		}
		return ifNeedIter;
	}

	public Set<Cluster> run() {
		Set<Cluster> clusterSet= chooseCenterCluster();
		boolean ifNeedIter = true; 
		while (ifNeedIter) {
			cluster(clusterSet);
			ifNeedIter = calculateCenter(clusterSet);
			iterRunTimes ++ ;
		}
		return clusterSet;
	}
	public int getIterTimes() {
		return iterRunTimes;
	}
}
