package p3.graph;

import p3.SetUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AdjacencyGraph<N> implements Graph<N> {

    final AdjacencyMatrix matrix;
    final Map<N, Integer> nodeIndices = new HashMap<>();
    final Map<Integer, N> indexNodes = new HashMap<>();
    private final Set<N> nodes;
    private final Set<Edge<N>> edges;

    public AdjacencyGraph(Set<N> nodes, Set<Edge<N>> edges) {
        matrix = new AdjacencyMatrix(nodes.size());
        this.nodes = SetUtils.immutableCopyOf(nodes);
        this.edges = SetUtils.immutableCopyOf(edges);
        int index = 0;
        for (N node : nodes) {
            nodeIndices.put(node, index);
            indexNodes.put(index, node);
            index++;
        }
        for (Edge<N> edge : edges) {
            matrix.addEdge(nodeIndices.get(edge.a()), nodeIndices.get(edge.b()), edge.weight());
        }
    }

    public AdjacencyGraph(List<N> nodes, Set<Edge<N>> edges) {
        this(new HashSet<>(nodes), edges);
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
        return IntStream.range(0, nodeIndices.size())
            .mapToObj(i -> Edge.of(node, indexNodes.get(i), matrix.getWeight(nodeIndices.get(node), i)))
            .filter(edge -> edge.weight() > 0)
            .collect(Collectors.toSet());
    }

    @Override
    public MutableGraph<N> toMutableGraph() {
        return MutableGraph.of(nodes, edges);
    }

    @Override
    public Graph<N> toGraph() {
        return this;
    }
}
