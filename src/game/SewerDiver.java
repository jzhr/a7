package game;

/** An abstract class representing what methods a sewer diver<br>
 * must implement in order to be used in solving the game. */
public abstract class SewerDiver {

	/** Explore the sewer, trying to find the ring in as few steps as possible. <br>
	 * Once you find the ring, return from findRing in order to pick it up. <br>
	 * If you continue to move after finding the ring rather than returning, <br>
	 * it will not count. If you return from this function while not standing on top <br>
	 * of the ring, it will count as a failure.
	 *
	 * There is no limit to how many steps you can take, but you will receive<br>
	 * a score bonus multiplier for finding the ring in fewer steps.
	 *
	 * At every step, you only know your current tile's ID and the ID of all<br>
	 * open neighbor tiles, as well as the distance to the ring at each of <br>
	 * these tiles (ignoring walls and obstacles).
	 *
	 * In order to get information about the current state, use functions<br>
	 * getCurrentLocation(), getNeighbors(), and getDistanceToTarget() in FindState.<br>
	 * You know you are standing on the ring when getDistanceToTarget() is 0.
	 *
	 * Use function moveTo(long id) in FindState to move to a neighboring tile <br>
	 * by its ID. Doing this will change state to reflect your new position.
	 *
	 * A suggested first implementation that will always find the find, but <br>
	 * likely won't receive a large bonus multiplier, is a depth-first walk.
	 *
	 * @param state the information available at the current state */
	public abstract void find(FindState state);

	/** Get out of the sewer in within a certain number of steps, trying to <br>
	 * collect as many coins as possible along the way. Your solution must ALWAYS <br>
	 * get out before using up all the steps, and this should be prioritized <br>
	 * above collecting coins.
	 *
	 * You now have access to the entire underlying graph, which can be accessed<br>
	 * through FleeState. currentNode() and getExit() return Node objects of<br>
	 * interest, and getNodes() returns a collection of all nodes on the graph.
	 *
	 * Look at interface FleeState.<br>
	 * You can find out how many steps are left for DiverMin to take using function<br>
	 * stepsLeft. Each time DiverMin traverses an edge, the steps left are<br>
	 * decremented by the weight of that edge. You can use grabCoins() to pick up<br>
	 * any coins on your current tile (this will fail if no coins are there), <br>
	 * and moveTo() to move to a destination node adjacent to your current node.
	 *
	 * You must return from this function while standing at the exit. Failing to <br>
	 * do so within the steps left or returning from the wrong location will be <br>
	 * considered a failed run.
	 *
	 * You will always have enough time to get out using the shortest path from <br>
	 * the starting position to the exit, although this will not collect many coins. <br>
	 * But for this reason, using Dijkstra's to plot the shortest path to the <br>
	 * exit is a good starting solution.
	 *
	 * @param state the information available at the current state */
	public abstract void flee(FleeState state);
}
