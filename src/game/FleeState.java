package game;

import java.util.Collection;

/** A FleeState provides all the information necessary to<br>
 * get out of the sewer system and collect coins on the way.
 *
 * This interface provides access to the complete graph of the sewer system,<br>
 * which will allow computation of the path.<br>
 * Once you have determined how DiverMin should get out, call<br>
 * moveTo(Node) repeatedly to move to each node. <br>
 * Coins on a node are picked up automatically when that code is movedTo(...). */
public interface FleeState {
	/** Return the Node corresponding to DiverMin's location in the graph. */
	Node currentNode();

	/** Return the Node associated with the exit from the sewer system.<br>
	 * DiverMin has to move to this Node in order to get out. */
	Node getExit();

	/** Return a collection containing all the nodes in the graph.<br>
	 * They in no particular order. */
	Collection<Node> allNodes();

	/** Change DiverMin's location to n.<br>
	 * Throw an IllegalArgumentException if n is not directly connected to<br>
	 * DiverMin's location. */
	void moveTo(Node n);

	/** Pick up the coins on the current tile. */
	void grabCoins();

	/** Return the steps remaining to get out of the sewer system.<br>
	 * This value will change with every call to moveTo(Node),<br>
	 * and if it reaches 0 before you get out, you have failed to get out. */
	int stepsLeft();
}
