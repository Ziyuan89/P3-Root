package h3.graph;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BasicGraph<N> implements Graph<N> {

    private final Map<N, Set<Graph.Edge<N>>> backing;
    private final Set<N> nodes;
    private final Set<Graph.Edge<N>> edges;

    public BasicGraph(Set<N> nodes, Set<Graph.Edge<N>> edges) {
        backing = nodes.stream()
            .map(n -> Map.entry(n, edges.stream()
                .filter(e -> Objects.equals(e.getA(), n) || Objects.equals(e.getB(), n))
                .collect(Collectors.toUnmodifiableSet())))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.nodes = Collections.unmodifiableSet(nodes);
        this.edges = Collections.unmodifiableSet(edges);
    }

    @Override
    public Set<N> getNodes() {
        return nodes;
    }

    @Override
    public Set<Edge<N>> getEdges() {
        return edges;
    }

    @Override
    public Set<Edge<N>> getAdjacentEdges(N node) {
        Set<Graph.Edge<N>> result = backing.get(node);
        if (result == null) {
            throw new IllegalArgumentException("Node not found: " + node);
        }
        return result;
    }
}
