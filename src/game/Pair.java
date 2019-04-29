package game;

import java.util.Objects;

/** A Pair&lt;X,Y&gt; represents an immutable ordered pair of two Objects of<br>
 * types X and Y respectively.
 *
 * @author eperdew
 *
 * @param <X> The type of the first object in this Pair
 * @param <Y> The type of the second object in this Pair */
public final class Pair<X, Y> {
	X first;
	Y second;

	/** Constructor: a pair (x, y). */
	public Pair(X x, Y y) {
		first= x;
		second= y;
	}

	/** Return the first object in this Pair. */
	public X getX() {
		return first;
	}

	/** Return the second object in this Pair. */
	public Y getY() {
		return second;
	}

	/** Return true iff if ob is a Pair with elements equal to this one. */
	@Override
	public boolean equals(Object ob) {
		if (!(ob instanceof Pair<?, ?>)) return false;
		Pair<?, ?> p= (Pair<?, ?>) ob;
		return first.equals(p.first) && second.equals(p.second);
	}

	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	}

}
