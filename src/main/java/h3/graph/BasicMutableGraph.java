package h3.graph;

import h3.SetUtils;

import java.util.*;
import java.util.stream.Collectors;

public class BasicMutableGraph<N> implements MutableGraph<N> {

    private final Map<N, Set<Edge<N>>> backing;
    private final Set<N> nodes;
    private final Set<Edge<N>> edges;

    public BasicMutableGraph() {
        backing = new HashMap<>();
        nodes = new HashSet<>();
        edges = new HashSet<>();
    }

    public BasicMutableGraph(Set<N> nodes, Set<Edge<N>> edges) {
        backing = nodes.stream()
            .map(n -> Map.entry(n, edges.stream()
                .filter(e -> Objects.equals(e.a(), n) || Objects.equals(e.b(), n))
                .collect(Collectors.toCollection(HashSet::new))))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.nodes = SetUtils.mutableCopyOf(nodes);
        this.edges = SetUtils.mutableCopyOf(edges);
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
        return Graph.of(nodes, edges);
    }

    @Override
    public MutableGraph<N> putNode(N node) {
        if (!backing.containsKey(node)) {
            backing.put(node, new HashSet<>());
            nodes.add(node);
        }
        return this;
    }

    @Override
    public MutableGraph<N> putEdge(N a, N b, int weight) {
        final Set<Edge<N>> edgesA = backing.get(a);
        final Set<Edge<N>> edgesB = backing.get(b);
        if (edgesA == null || edgesB == null) {
            throw new IllegalArgumentException("Node not found: " + (edgesA == null ? a : b));
        }

        final Edge<N> edge = Edge.of(a, b, weight);
        edgesA.add(edge);
        edgesB.add(edge);
        edges.add(edge);
        return this;
    }

    @Override
    public MutableGraph<N> putEdgesAndNodes(N a, N b, int weight) {
        nodes.add(a);
        nodes.add(b);

        final Set<Edge<N>> edgesA = backing.computeIfAbsent(a, k -> new HashSet<>());
        final Set<Edge<N>> edgesB = backing.computeIfAbsent(b, k -> new HashSet<>());

        final Edge<N> edge = Edge.of(a, b, weight);
        edgesA.add(edge);
        edgesB.add(edge);

        backing.put(a, edgesA);
        backing.put(b, edgesB);

        edges.add(edge);

        return this;
    }
}
