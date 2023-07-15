package p3.util;

import p3.graph.Edge;
import p3.graph.Graph;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record SerializedGraph<N>(Set<N> nodes, List<SerializedEdge<N>> edges) {

    public Graph<N> toGraph() {
        return Graph.of(
            nodes,
            edges.stream()
                .map(edge -> Edge.of(
                    nodes.stream().filter(n -> n.equals(edge.a())).findFirst().orElseThrow(),
                    nodes.stream().filter(n -> n.equals(edge.b())).findFirst().orElseThrow(),
                    edge.weight()))
                .collect(Collectors.toSet())
        );
    }
}
