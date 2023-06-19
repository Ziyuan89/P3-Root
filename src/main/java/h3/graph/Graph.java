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
}
