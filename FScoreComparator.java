//This class is only here to allow me to control how the
//priority queue evaluates the values and decides
//if a node should be moved up
//in this case, it compares the F value of the 2 nodes and
//similar to a compareTo it decides which node should be moved up
//if it has the lower F value.
import java.util.Comparator;

public class FScoreComparator implements Comparator<Node> {

	public int compare(Node node1, Node node2) {
		return node1.getF()-node2.getF();
	}

}
