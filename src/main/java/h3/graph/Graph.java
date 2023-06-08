package h3.graph;

import java.util.Set;

public interface Graph<N> {

    Set<N> getNodes();

    Set<Edge<N>> getEdges();

    Set<Edge<N>> getAdjacentEdges(N node);

    interface Edge<N> extends Comparable<Edge<N>> {

        N getA();

        N getB();

        /**
         * The weight of the edge. The precise meaning of this value is up to the user.
         *
         * <p>
         * This value is used to order edges in the graph.
         * </p>
         *
         * @return the weight of the edge
         */
        int getWeight();

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
            return Integer.compare(getWeight(), other.getWeight());
        }

        static <N> Graph.Edge<N> of(N a, N b, int weight) {
            return new Graph.Edge<>() {
                @Override
                public N getA() {
                    return a;
                }

                @Override
                public N getB() {
                    return b;
                }

                @Override
                public int getWeight() {
                    return weight;
                }
            };
        }
    }

    interface Builder<N> {

        Builder<N> addNode(N node);

        Builder<N> addEdge(N a, N b, int weight);

        Builder<N> addEdgesAndNodes(N a, N b, int weight);

        Graph<N> build();
    }
}
