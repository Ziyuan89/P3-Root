package p3.graph;

import p3.SetUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BasicGraph<N> implements Graph<N> {

    private final Map<N, Set<Edge<N>>> backing;
    private final Set<N> nodes;
    private final Set<Edge<N>> edges;

    public BasicGraph(Set<N> nodes, Set<Edge<N>> edges) {
        backing = nodes.stream()
            .map(n -> Map.entry(n, edges.stream()
                .filter(e -> Objects.equals(e.a(), n) || Objects.equals(e.b(), n))
                .collect(Collectors.toUnmodifiableSet())))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.nodes = SetUtils.immutableCopyOf(nodes);
        this.edges = SetUtils.immutableCopyOf(edges);
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
        Set<Edge<N>> result = backing.get(node);
        if (result == null) {
            throw new IllegalArgumentException("Node not found: " + node);
        }
        return result;
    }

    @Override
    public MutableGraph<N> toMutableGraph() {
        return MutableGraph.of(nodes, edges);
    }

    @Override
    public Graph<N> toGraph() {
        return this;
    }

    static Graph<Object> EMPTY = new BasicGraph<>(Set.of(), Set.of());
}
