package h3.graph;

import java.util.Set;

public interface Graph<N> {

    Set<N> getNodes();

    Set<Edge<N>> getEdges();

    Set<Edge<N>> getAdjacentEdges(N node);

    /**
     * Creates a mutable copy of this graph with the same nodes and edges.
     *
     * <p>
     * The nodes and edges in the copy are the same objects as in the original graph.
     * Only the graph structure is copied.
     * </p>
     */
    MutableGraph<N> toMutableGraph();

    /**
     * Creates an immutable copy of this graph with the same nodes and edges if the graph is mutable,
     * or returns the graph itself if it is already immutable.
     *
     * <p>
     * The nodes and edges in the copy are the same objects as in the original graph.
     * Only the graph structure is copied.
     * </p>
     */
    Graph<N> toGraph();


    @SuppressWarnings("unchecked")
    static <N> Graph<N> of() {
        return (Graph<N>) BasicGraph.EMPTY;
    }

    static <N> Graph<N> of(Set<N> nodes, Set<Edge<N>> edges) {
        return new BasicGraph<>(nodes, edges);
    }

    interface Edge<N> extends Comparable<Edge<N>> {

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
        default int compareTo(Graph.Edge<N> other) {
            return Integer.compare(weight(), other.weight());
        }

        static <N> Graph.Edge<N> of(N a, N b, int weight) {
            return new EdgeImpl<>(a, b, weight);
        }
    }
}
