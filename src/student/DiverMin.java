package student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import game.FindState;
import game.FleeState;
import game.Node;
import game.NodeStatus;
import game.SewerDiver;

public class DiverMin extends SewerDiver {

	/**
	 * Get to the ring in as few steps as possible. Once you get there, <br>
	 * you must return from this function in order to pick<br>
	 * it up. If you continue to move after finding the ring rather <br>
	 * than returning, it will not count.<br>
	 * If you return from this function while not standing on top of the ring, <br>
	 * it will count as a failure.
	 *
	 * There is no limit to how many steps you can take, but you will receive<br>
	 * a score bonus multiplier for finding the ring in fewer steps.
	 *
	 * At every step, you know only your current tile's ID and the ID of all<br>
	 * open neighbor tiles, as well as the distance to the ring at each of <br>
	 * these tiles (ignoring walls and obstacles).
	 *
	 * In order to get information about the current state, use functions<br>
	 * currentLocation(), neighbors(), and distanceToRing() in state.<br>
	 * You know you are standing on the ring when distanceToRing() is 0.
	 *
	 * Use function moveTo(long id) in state to move to a neighboring<br>
	 * tile by its ID. Doing this will change state to reflect your new position.
	 *
	 * A suggested first implementation that will always find the ring, but <br>
	 * likely won't receive a large bonus multiplier, is a depth-first walk. <br>
	 * Some modification is necessary to make the search better, in general.
	 */
	@Override
	public void find(FindState state) {
		// TODO : Find the ring and return.
		// DO NOT WRITE ALL THE CODE HERE. DO NOT MAKE THIS METHOD RECURSIVE.
		// Instead, write your method elsewhere, with a good specification,
		// and call it from this one.
		List<Long> visited = new ArrayList<>();
		dfs(null, state, visited);

	}

	/**
	 * Visit every node reachable along a path of unvisited nodes from node u.
	 * Precondition: u is not visited.
	 */
	public static void dfs(Long prevState, FindState state, List<Long> visited) {
		// Base case
		if (state.distanceToRing() == 0) {
			return;
		} else {
			long u = state.currentLocation();
			visited.add(u);

			List<NodeStatus> neighbors = new ArrayList<>(state.neighbors());
			Collections.sort(neighbors);

			for (NodeStatus neighbor : neighbors) {
				long n = neighbor.getId();

				if (visited.contains(n) == false && state.distanceToRing() != 0) {
					state.moveTo(n);
					dfs(u, state, visited);
				}

			}
			if (state.distanceToRing() != 0) {
				state.moveTo(prevState);
			}

		}
	}

	/**
	 * Flee the sewer system before the steps are all used, trying to <br>
	 * collect as many coins as possible along the way. Your solution must ALWAYS
	 * <br>
	 * get out before the steps are all used, and this should be prioritized
	 * above<br>
	 * collecting coins.
	 *
	 * You now have access to the entire underlying graph, which can be accessed<br>
	 * through FleeState. currentNode() and getExit() will return Node objects<br>
	 * of interest, and getNodes() will return a collection of all nodes on the
	 * graph.
	 *
	 * You have to get out of the sewer system in the number of steps given by<br>
	 * getStepsRemaining(); for each move along an edge, this number is <br>
	 * decremented by the weight of the edge taken.
	 *
	 * Use moveTo(n) to move to a node n that is adjacent to the current node.<br>
	 * When n is moved-to, coins on node n are automatically picked up.
	 *
	 * You must return from this function while standing at the exit. Failing <br>
	 * to do so before steps run out or returning from the wrong node will be<br>
	 * considered a failed run.
	 *
	 * Initially, there are enough steps to get from the starting point to the<br>
	 * exit using the shortest path, although this will not collect many coins.<br>
	 * For this reason, a good starting solution is to use the shortest path to<br>
	 * the exit.
	 */
	// Maps each visited node to the number of times it has been visited
	HashMap<Node, Integer> visited = new HashMap<>();

	@Override
	public void flee(FleeState state) {
		// TODO: Get out of the sewer system before the steps are used up.
		// DO NOT WRITE ALL THE CODE HERE. Instead, write your method elsewhere,
		// with a good specification, and call it from this one.
		dijkstras(state, visited);
		return;
	}

	public void dijkstras(FleeState state, HashMap<Node, Integer> visited) {
		List<Node> shortest = Paths.shortest(state.currentNode(), state.getExit());

		for (Node n : shortest) {
			if (state.currentNode().getNeighbors().contains(n)) {
				state.moveTo(n);
				visited.put(n, 0);
			}
		}
		return;
	}

}
