
/** NetId(s): nnnn, nnnn
 * Name(s):
 *
 * What I thought about this assignment:
 *
 *
 */
package student;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import game.Edge;
import game.Node;

/** This class contains the shortest-path algorithm and other methods<br>
 * for a undirected graph. */
public class Paths {

	/** Replace "-1" by the time you spent on A6 in hours.<br>
	 * Example: for 3 hours 15 minutes, use 3.25<br>
	 * Example: for 4 hours 30 minutes, use 4.50<br>
	 * Example: for 5 hours, use 5 or 5.0 */
	public static double timeSpent= -1;

	/** Return the shortest path from node v to node end <br>
	 * ---or the empty list if a path does not exist. <br>
	 * Note: The empty list is NOT "null"; it is a list with 0 elements. */
	public static List<Node> shortest(Node v, Node end) {
		/* TODO Implement this method.
		 * Read the A6 assignment handout for all details and
		 * be aware of changes announced on pinned Piazza note Assignment A6.
		 * Remember, the graph is undirected. */
		Heap<Node> F= new Heap<>(true);

		// dbSF contains an entry for each node in F or S. <br>
		// Thus, |dbSF| = |F| + |S|.
		// For each such node, dbSF contains the shortest known Distance
		// to the node and the node's Backpointer on that shortest path.
		HashMap<Node, DB> dbSF= new HashMap<>();

		F.add(v, 0);
		dbSF.put(v, new DB(0, null));
		// inv: See the A6 handout, together with def of F and dbSF.
		while (F.size() != 0) {
			Node f= F.poll();

			if (f == end) return path(dbSF, end);
			int fDist= dbSF.get(f).dist;

			for (Edge e : f.getExits()) {// for each neighbor w of f
				Node w= e.getOther(f);
				int newWdist= fDist + e.length;
				DB wDB= dbSF.get(w);
				if (wDB == null) { // if w not in F or S
					dbSF.put(w, new DB(newWdist, f));
					F.add(w, newWdist);
				} else if (newWdist < wDB.dist) {
					wDB.dist= newWdist;
					wDB.bkptr= f;
					F.changePriority(w, newWdist);
				}
			}
		}

		// no path from v to end
		return new LinkedList<>();
	}

	/** An instance contains information about a node: <br>
	 * the Distance of this node from the start node and <br>
	 * its Backpointer: the previous node on a shortest path <br>
	 * from the start node to this node. */
	private static class DB {
		/** shortest known distance from the start node to this one. */
		private int dist;
		/** backpointer on path (with shortest known distance) from start node to this one */
		private Node bkptr;

		/** Constructor: an instance with dist d from the start node<br>
		 * backpointer p. */
		private DB(int d, Node p) {
			dist= d;     // Distance from start node to this one.
			bkptr= p;    // Backpointer on the path (null if start node)
		}

		/** return a representation of this instance. */
		@Override
		public String toString() {
			return "dist " + dist + ", bckptr " + bkptr;
		}
	}

	/** Return the path from the start node to node end.<br>
	 * Precondition: DBdata contains all the necessary information about<br>
	 * ............. the path. */
	public static List<Node> path(HashMap<Node, DB> DBdata, Node end) {
		List<Node> path= new LinkedList<>();
		Node p= end;
		// invariant: All the nodes from p's successor to the end are in
		// path, in reverse order.
		while (p != null) {
			path.add(0, p);
			p= DBdata.get(p).bkptr;
		}
		return path;
	}

	/** Return the sum of the weights of the edges on path pa. <br>
	 * Precondition: pa contains at least 1 node. <br>
	 * If 1 node, it's a path of length 0, i.e. with no edges. */
	public static int pathSum(List<Node> pa) {
		synchronized (pa) {
			Node v= null;
			int sum= 0;
			// invariant: if v is null, n is the first node of the path.<br>
			// ......... if v is not null, v is the predecessor of n on the path.
			// sum = sum of weights on edges from first node to v
			for (Node n : pa) {
				if (v != null) sum= sum + v.getEdge(n).length;
				v= n;
			}
			return sum;
		}
	}

}
