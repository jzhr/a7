package game;

import java.util.Objects;

public class NodeStatus implements Comparable<NodeStatus> {
	private final long id;
	private final int distance;

	/** Constructor: an instance with id nodeId and distance dist to the ring */
	/* package */ NodeStatus(long nodeId, int dist) {
		id= nodeId;
		distance= dist;
	}

	/** Return the Id of the Node that corresponds to this NodeStatus. */
	public long getId() {
		return id;
	}

	/** Return the distance to the ring from the Node that corresponds to this NodeStatus. */
	public int getDistanceToTarget() {
		return distance;
	}

	/** Return neg or pos number depending on whether this's distance is<br>
	 * < or > other's distance. If the distances are equal, return neg, 0 or pos <br>
	 * depending on whether this id is <, = or > other's id. */
	@Override
	public int compareTo(NodeStatus other) {
		if (distance != other.distance) { return Integer.compare(distance, other.distance); }
		return Long.compare(id, other.id);
	}

	/** Return true iff ob is a NodeStatus and it has the same id as this one. */
	@Override
	public boolean equals(Object ob) {
		if (ob == this) return true;
		if (!(ob instanceof NodeStatus)) return false;
		return id == ((NodeStatus) ob).id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
