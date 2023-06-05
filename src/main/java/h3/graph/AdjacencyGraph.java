package h3.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AdjacencyGraph<N> implements Graph<N> {

    final AdjacencyMatrix matrix;
    final Map<N, Integer> nodeIndices = new HashMap<>();
    final Map<Integer, N> indexNodes = new HashMap<>();
    private final Set<N> nodes;
    private final Set<Edge<N>> edges;

    public AdjacencyGraph(Set<N> nodes, Set<Edge<N>> edges) {
        matrix = new AdjacencyMatrix(nodes.size());
        this.nodes = Collections.unmodifiableSet(nodes);
        this.edges = Collections.unmodifiableSet(edges);
        int index = 0;
        for (N node : nodes) {
            nodeIndices.put(node, index);
            indexNodes.put(index, node);
        }
        for (Edge<N> edge : edges) {
            matrix.addEdge(nodeIndices.get(edge.getA()), nodeIndices.get(edge.getB()), edge.getWeight());
        }
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
        return null;
    }
}
