package p3.graph;

import java.util.Set;

public interface MutableGraph<N> extends Graph<N> {

    MutableGraph<N> putNode(N node);

    MutableGraph<N> putEdge(N a, N b, int weight);

    MutableGraph<N> putEdgesAndNodes(N a, N b, int weight);

    static <N> MutableGraph<N> of() {
        return new BasicMutableGraph<>();
    }

    static <N> MutableGraph<N> of(Set<N> nodes, Set<Edge<N>> edges) {
        return new BasicMutableGraph<>(nodes, edges);
    }
}
