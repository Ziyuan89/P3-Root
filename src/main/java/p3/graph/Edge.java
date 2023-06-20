package p3.graph;

public interface Edge<N> extends Comparable<Edge<N>> {

    N a();

    N b();

    /**
     * The weight of the edge. The precise meaning of this value is up to the user.
     *
     * <p>
     * This value is used to order edges in the graph.
     * </p>
     *
     * @return the weight of the edge
     */
    int weight();

    /**
     * Two edges are equal if they have the same nodes and weight.
     *
     * <p>More precisely, two edges <code>x</code> and <code>y</code> are equal iff:</p>
     * <ul>
     *     <li><code>Objects.equals(x.getA(), y.getA())</code></li>
     *     <li><code>Objects.equals(x.getB(), y.getB())</code></li>
     *     <li><code>x.getWeight() == y.getWeight()</code></li>
     * </ul>
     *
     * @param other the other edge
     * @return true if the edges are equal
     */
    boolean equals(Object other);

    /**
     * Edges are ordered by weight.
     *
     * @param other the other edge
     * @return Whether this edge's weight is less than, equal to, or greater than the other edge's weight
     */
    @Override
    default int compareTo(Edge<N> other) {
        return Integer.compare(weight(), other.weight());
    }

    static <N> Edge<N> of(N a, N b, int weight) {
        return new EdgeImpl<>(a, b, weight);
    }
}
