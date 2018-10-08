package cluster;
import java.util.ArrayList;
import java.util.List;

public class Cluster {
	 private int id;
	    private Point center;
	    private List<Point> members = new ArrayList<Point>();

	    public Cluster(int id, Point center) {
	        this.id = id;
	        this.center = center;
	    }

	    public Cluster(int id, Point center, List<Point> members) {
	        this.id = id;
	        this.center = center;
	        this.members = members;
	    }

	    public void addPoint(Point newPoint) {
	        if (!members.contains(newPoint)){
	            members.add(newPoint);
	        }else{
	            System.out.println( newPoint.toString());
	        }
	    }

	    public int getId() {
	        return id;
	    }

	    public Point getCenter() {
	        return center;
	    }

	    public void setCenter(Point center) {
	        this.center = center;
	    }

	    public List<Point> getMembers() {
	        return members;
	    }

	    @Override
	    public String toString() {
	        String toString = "Cluster \n" + "Cluster_id=" + this.id + ", center:{" + this.center.toString()+"}";
	        for (Point point : members) {
	            toString+="\n"+point.toString();
	        }
	        return toString+"\n";
	    }
}
